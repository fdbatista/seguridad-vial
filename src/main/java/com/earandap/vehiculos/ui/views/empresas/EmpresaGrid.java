package com.earandap.vehiculos.ui.views.empresas;

import com.earandap.vehiculos.domain.Empresa;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.gridutil.cell.GridCellFilter;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import org.vaadin.gridutil.renderer.EditDeleteButtonValueRenderer;

/**
 * Created by Angel Luis on 10/5/2015.
 */
@SpringComponent
@UIScope
public class EmpresaGrid extends Grid {

    protected Container container;

    public static final String NUEVA_EMPRESA = "NUEVA-EMPRESA";
    public static final String ELIMINADO_EMPRESA = "ELIMINADO-EMPRESA";

    @Autowired
    private EventBus.ApplicationEventBus eventBus;

    @PostConstruct
    public void init() {

        this.container = new Container();
        this.setSizeFull();
        this.setSelectionMode(SelectionMode.SINGLE);

        GeneratedPropertyContainer gpc = new GeneratedPropertyContainer(container);
        gpc.addGeneratedProperty("acciones", new PropertyValueGenerator<String>() {
            @Override
            public String getValue(Item item, Object itemId, Object propertyId) {
                return ".";
            }

            @Override
            public Class<String> getType() {
                return String.class;
            }
        });

        this.setContainerDataSource(gpc);

        this.removeAllColumns();
        this.addColumn("documento").setHeaderCaption("NIT").setWidth(100);
        this.addColumn("razonSocial").setHeaderCaption("Raz\u00F3n Social");
        this.addColumn("correo");
        this.addColumn("direccion").setHeaderCaption("Direcci\u00F3n");
        this.addColumn("acciones").setHeaderCaption("Acciones");

        initFooterRow();
        initFilter();
        eventBus.subscribe(this);

    }

    private void initFooterRow() {
        final FooterRow footerRow = this.appendFooterRow();
        footerRow.getCell("documento").setHtml("Total:");
        footerRow.join("razonSocial", "correo", "direccion", "acciones");
        footerRow.getCell("razonSocial").setHtml("<b>" + container.getItemIds().size() + "</b>");
        container.addItemSetChangeListener(event -> footerRow.getCell("razonSocial")
                .setHtml("<b>" + event.getContainer().getItemIds().size() + "</b>"));
    }

    private void initFilter() {
        GridCellFilter filter = new GridCellFilter(this);

        // simple filters
        filter.setTextFilter("documento", true, false, "Contiene");
        filter.setTextFilter("razonSocial", true, false, "Contiene");
        filter.setTextFilter("correo", true, false, "Contiene");
        filter.setTextFilter("direccion", true, false, "Contiene");
    }

    @EventBusListenerTopic(topic = EmpresaGrid.NUEVA_EMPRESA)
    @EventBusListenerMethod
    public void nuevaEmpresaListener(Empresa empresa) {
        container.removeItem(empresa.getId());
        container.addBean(empresa);
    }

    @EventBusListenerTopic(topic = EmpresaGrid.ELIMINADO_EMPRESA)
    @EventBusListenerMethod
    public void eliminarElementoListener(Long id) {
        container.removeItem(id);
    }

    @PreDestroy
    void destroy() {
        eventBus.unsubscribe(this); // It's good manners to do this, even though we should be automatically unsubscribed when the UI is garbage collected
    }

    public void setData(List<Empresa> empresas) {
        container.removeAllItems();
        container.addAll(empresas);
    }

    public void setEditDeleteListener(EditDeleteButtonValueRenderer.EditDeleteButtonClickListener listener) {
        if (listener != null) {
            this.getColumn("acciones").setRenderer(new EditDeleteButtonValueRenderer(listener)).setWidth(100);
        }
    }

    public static class Container extends BeanContainer<Long, Empresa> {

        public Container() {
            super(Empresa.class);
            setBeanIdResolver(empresa -> empresa.getId());
        }
    }
}
