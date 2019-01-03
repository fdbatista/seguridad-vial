package com.earandap.vehiculos.ui.views.capacitacion;

import com.earandap.vehiculos.domain.Capacitacion;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.DateRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.gridutil.cell.GridCellFilter;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.text.DateFormat;
import java.util.List;
import org.vaadin.gridutil.renderer.EditDeleteButtonValueRenderer;

/**
 * Created by Angel Luis on 10/19/2015.
 */
@SpringComponent
@UIScope
public class CapacitacionGrid extends Grid {

    protected Container container;

    public static final String NUEVA_CAPACITACION = "NUEVA-CAPACITACION";
    public static final String ELIMINADO_CAPACITACION = "ELIMINADO-CAPACITACION";

    @Autowired
    private EventBus.ApplicationEventBus eventBus;

    @PostConstruct
    public void init() {
        this.container = new Container();
        container.addNestedContainerProperty("tipoCapacitacion.descripcion");
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
        this.addColumn("tipoCapacitacion.descripcion").setHeaderCaption("Tipo Actividad").setWidth(150);
        this.addColumn("nombreCapacitacion").setHeaderCaption("Nombre").setWidth(150);
        this.addColumn("fechaCapacitacion").setHeaderCaption("Fecha").setWidth(180).setRenderer(new DateRenderer(DateFormat.getDateInstance()));
        this.addColumn("fechaVencimiento").setWidth(180).setHeaderCaption("Vigencia").setRenderer(new DateRenderer(DateFormat.getDateInstance()));
        this.addColumn("observaciones");
        this.addColumn("acciones").setHeaderCaption("Acciones");

        //setRenderer();
        initFooterRow();
        initFilter();
        eventBus.subscribe(this);

    }

    private void initFooterRow() {
        final FooterRow footerRow = this.appendFooterRow();
        footerRow.getCell("tipoCapacitacion.descripcion").setHtml("Total:");
        footerRow.join("nombreCapacitacion", "fechaCapacitacion", "fechaVencimiento", "observaciones", "acciones");
        // inital total count
        footerRow.getCell("nombreCapacitacion").setHtml("<b>" + container.getItemIds().size() + "</b>");
        // filter change count recalculate
        container.addItemSetChangeListener(event -> footerRow.getCell("nombreCapacitacion")
                .setHtml("<b>" + event.getContainer().getItemIds().size() + "</b>"));
    }

    private void initFilter() {
        GridCellFilter filter = new GridCellFilter(this);

        // simple filters
        filter.setTextFilter("tipoCapacitacion.descripcion", true, false, "Contiene");
        filter.setTextFilter("nombreCapacitacion", true, false, "Contiene");
        filter.setDateFilter("fechaCapacitacion");
        filter.setDateFilter("fechaVencimiento");
        filter.setTextFilter("observaciones", true, false, "Contiene");
    }

    @EventBusListenerTopic(topic = CapacitacionGrid.NUEVA_CAPACITACION)
    @EventBusListenerMethod
    public void nuevaCapacitacionListener(Capacitacion capacitacion) {
        container.removeItem(capacitacion.getId());
        container.addBean(capacitacion);
    }

    @EventBusListenerTopic(topic = CapacitacionGrid.ELIMINADO_CAPACITACION)
    @EventBusListenerMethod
    public void eliminarElementoListener(Long id) {
        container.removeItem(id);
    }

    @PreDestroy
    void destroy() {
        eventBus.unsubscribe(this); // It's good manners to do this, even though we should be automatically unsubscribed when the UI is garbage collected
    }

    public void setData(List<Capacitacion> capacitaciones) {
        container.removeAllItems();
        container.addAll(capacitaciones);
    }

    public void setEditDeleteListener(EditDeleteButtonValueRenderer.EditDeleteButtonClickListener listener) {
        if (listener != null) {
            this.getColumn("acciones").setRenderer(new EditDeleteButtonValueRenderer(listener)).setWidth(100);
        }
    }

    public static class Container extends BeanContainer<Long, Capacitacion> {

        public Container() {
            super(Capacitacion.class);
            setBeanIdResolver(capacitacion -> capacitacion.getId());
        }
    }
}
