package com.earandap.vehiculos.ui.views.conductores;

import com.earandap.vehiculos.domain.Persona;
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
 * Created by Angel Luis on 10/4/2015.
 */
@SpringComponent
@UIScope
public class ConductorGrid extends Grid {

    protected Container container;

    public static final String NUEVO_CONDUCTOR = "NUEVO-CONDUCTOR";
    public static final String ELIMINADO_CONDUCTOR = "ELIMINADO-CONDUCTOR";

    @Autowired
    private EventBus.ApplicationEventBus eventBus;

    @PostConstruct
    public void init() {
        this.container = new Container();
        container.addNestedContainerProperty("tipoDocumento.descripcion");
        container.addNestedContainerProperty("annosLicencia");
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
        this.addColumn("documento").setWidth(90);
        this.addColumn("tipoDocumento.descripcion").setHeaderCaption("Tipo Documento");
        this.addColumn("nombreCompleto").setHeaderCaption("Nombre");
        this.addColumn("correo");
        this.addColumn("annosLicencia").setHeaderCaption("A\u00F1o Licencia").setWidth(100)/*.setRenderer(new DateRenderer(new SimpleDateFormat("yyyy")))*/;
        this.addColumn("acciones").setHeaderCaption("Acciones");

        initFooterRow();
        initFilter();
        eventBus.subscribe(this);

    }

    private void initFooterRow() {
        final FooterRow footerRow = this.appendFooterRow();
        footerRow.getCell("documento").setHtml("Total:");
        footerRow.join("tipoDocumento.descripcion", "nombreCompleto", "correo", "annosLicencia", "acciones");
        // inital total count
        footerRow.getCell("tipoDocumento.descripcion").setHtml("<b>" + container.getItemIds().size() + "</b>");
        // filter change count recalculate
        container.addItemSetChangeListener(event -> footerRow.getCell("tipoDocumento.descripcion")
                .setHtml("<b>" + event.getContainer().getItemIds().size() + "</b>"));
    }

    private void initFilter() {
        GridCellFilter filter = new GridCellFilter(this);

        // simple filters
        filter.setTextFilter("documento", true, false, "");
        filter.setTextFilter("tipoDocumento.descripcion", true, false, "Contiene");
        filter.setTextFilter("nombreCompleto", true, false, "Contiene");
        filter.setTextFilter("correo", true, false, "Contiene");
        filter.setTextFilter("annosLicencia", true, false, "Contiene");
    }

    @EventBusListenerTopic(topic = ConductorGrid.NUEVO_CONDUCTOR)
    @EventBusListenerMethod
    public void nuevoConductoListener(Persona conductor) {
        container.removeItem(conductor.getId());
        container.addBean(conductor);
    }

    @EventBusListenerTopic(topic = ConductorGrid.ELIMINADO_CONDUCTOR)
    @EventBusListenerMethod
    public void eliminarElementoListener(Long id) {
        container.removeItem(id);
    }

    @PreDestroy
    void destroy() {
        eventBus.unsubscribe(this); // It's good manners to do this, even though we should be automatically unsubscribed when the UI is garbage collected
    }

    public void setData(List<Persona> conductores) {
        container.removeAllItems();
        container.addAll(conductores);
    }

    public void setEditDeleteListener(EditDeleteButtonValueRenderer.EditDeleteButtonClickListener listener) {
        if (listener != null) {
            this.getColumn("acciones").setRenderer(new EditDeleteButtonValueRenderer(listener)).setWidth(100);
        }
    }

    public static class Container extends BeanContainer<Long, Persona> {

        public Container() {
            super(Persona.class);
            setBeanIdResolver(conductor -> conductor.getId());
        }
    }
}
