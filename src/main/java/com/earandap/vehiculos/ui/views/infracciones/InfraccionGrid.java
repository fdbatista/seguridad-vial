package com.earandap.vehiculos.ui.views.infracciones;

import com.earandap.vehiculos.domain.Infraccion;
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
 * Created by Angel Luis on 11/16/2015.
 */
@SpringComponent
@UIScope
public class InfraccionGrid extends Grid {

    protected Container container;

    public static final String NUEVA_INFRACCION = "NUEVA-INFRACCION";
    public static final String ELIMINADO_INFRACCION = "ELIMINADO-INFRACCION";

    @Autowired
    private EventBus.ApplicationEventBus eventBus;

    @PostConstruct
    public void init() {
        this.container = new Container();
        container.addNestedContainerProperty("conductor.nombreCompleto");
        container.addNestedContainerProperty("tipoSancion.descripcion");
        container.addNestedContainerProperty("vehiculo.placa");
        this.setSizeFull();

        this.setSelectionMode(Grid.SelectionMode.SINGLE);

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
        this.addColumn("vehiculo.placa").setWidth(100);
        this.addColumn("conductor.nombreCompleto").setHeaderCaption("T\u00E9cnico");
        this.addColumn("tipoSancion.descripcion").setHeaderCaption("Tipo Sanci\u00F3n").setWidth(180);
        this.addColumn("acciones").setHeaderCaption("Acciones");

        initFooterRow();
        initFilter();
        eventBus.subscribe(this);

    }

    private void initFooterRow() {
        final Grid.FooterRow footerRow = this.appendFooterRow();
        footerRow.getCell("vehiculo.placa").setHtml("Total:");
        footerRow.join("conductor.nombreCompleto", "tipoSancion.descripcion", "acciones");
        // inital total count
        footerRow.getCell("conductor.nombreCompleto").setHtml("<b>" + container.getItemIds().size() + "</b>");
        // filter change count recalculate
        container.addItemSetChangeListener(event -> footerRow.getCell("conductor.nombreCompleto")
                .setHtml("<b>" + event.getContainer().getItemIds().size() + "</b>"));
    }

    private void initFilter() {
        GridCellFilter filter = new GridCellFilter(this);

        // simple filters
        filter.setTextFilter("vehiculo.placa", true, false, "Contiene");
        filter.setTextFilter("conductor.nombreCompleto", true, false, "Contiene");
        filter.setTextFilter("tipoSancion.descripcion", true, false, "Contiene");
    }

    @EventBusListenerTopic(topic = InfraccionGrid.NUEVA_INFRACCION)
    @EventBusListenerMethod
    public void nuevaInfraccionListener(Infraccion infraccion) {
        container.removeItem(infraccion.getId());
        container.addBean(infraccion);
    }

    @EventBusListenerTopic(topic = InfraccionGrid.ELIMINADO_INFRACCION)
    @EventBusListenerMethod
    public void eliminarElementoListener(Long id) {
        container.removeItem(id);
    }

    @PreDestroy
    void destroy() {
        eventBus.unsubscribe(this); // It's good manners to do this, even though we should be automatically unsubscribed when the UI is garbage collected
    }

    public void setData(List<Infraccion> infracciones) {
        container.removeAllItems();
        container.addAll(infracciones);
    }

    public void setEditDeleteListener(EditDeleteButtonValueRenderer.EditDeleteButtonClickListener listener) {
        if (listener != null) {
            this.getColumn("acciones").setRenderer(new EditDeleteButtonValueRenderer(listener)).setWidth(100);
        }
    }

    public static class Container extends BeanContainer<Long, Infraccion> {

        public Container() {
            super(Infraccion.class);
            setBeanIdResolver(infraccion -> infraccion.getId());
        }
    }
}
