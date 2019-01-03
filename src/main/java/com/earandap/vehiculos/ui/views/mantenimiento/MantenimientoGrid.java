package com.earandap.vehiculos.ui.views.mantenimiento;

import com.earandap.vehiculos.domain.AccionMantenimiento;
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
 * Created by Angel Luis on 10/26/2015.
 */
@SpringComponent
@UIScope
public class MantenimientoGrid extends Grid {

    protected Container container;

    public static final String NUEVO_MANTENIMIENTO = "NUEVO-MANTENIMIENTO";
    public static final String ELIMINADO_MANTENIMIENTO = "ELIMINADO-MANTENIMIENTO";

    @Autowired
    private EventBus.ApplicationEventBus eventBus;

    @PostConstruct
    public void init() {
        this.container = new Container();
        container.addNestedContainerProperty("tecnico.nombreTercero");
        container.addNestedContainerProperty("tipoMantenimiento.descripcion");
        container.addNestedContainerProperty("vehiculo.placa");
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
        this.addColumn("vehiculo.placa").setWidth(100);
        this.addColumn("tipoMantenimiento.descripcion").setHeaderCaption("Tipo Mantenimiento").setWidth(180);
        this.addColumn("tecnico.nombreTercero").setHeaderCaption("T\u00E9cnico");
        this.addColumn("fecha").setWidth(200).setRenderer(new DateRenderer(DateFormat.getDateInstance()));
        this.addColumn("km").setWidth(100);
        this.addColumn("acciones").setHeaderCaption("Acciones");

        //setRenderer();
        initFooterRow();
        initFilter();
        eventBus.subscribe(this);

    }

    private void initFooterRow() {
        final FooterRow footerRow = this.appendFooterRow();
        footerRow.getCell("vehiculo.placa").setHtml("Total:");
        footerRow.join("tipoMantenimiento.descripcion", "tecnico.nombreTercero", "fecha", "km", "acciones");
        // inital total count
        footerRow.getCell("tipoMantenimiento.descripcion").setHtml("<b>" + container.getItemIds().size() + "</b>");
        // filter change count recalculate
        container.addItemSetChangeListener(event -> footerRow.getCell("tipoMantenimiento.descripcion")
                .setHtml("<b>" + event.getContainer().getItemIds().size() + "</b>"));
    }

    private void initFilter() {
        GridCellFilter filter = new GridCellFilter(this);

        // simple filters
        filter.setTextFilter("vehiculo.placa", true, false, "Contiene");
        filter.setTextFilter("tipoMantenimiento.descripcion", true, false, "Contiene");
        filter.setTextFilter("tecnico.nombreTercero", true, false, "Contiene");
        filter.setDateFilter("fecha");
        filter.setTextFilter("km", true, false, "Contiene");
    }

    @EventBusListenerTopic(topic = MantenimientoGrid.NUEVO_MANTENIMIENTO)
    @EventBusListenerMethod
    public void nuevomantenimientoListener(AccionMantenimiento accionMantenimiento) {
        container.removeItem(accionMantenimiento.getId());
        container.addBean(accionMantenimiento);
    }

    @EventBusListenerTopic(topic = MantenimientoGrid.ELIMINADO_MANTENIMIENTO)
    @EventBusListenerMethod
    public void eliminarElementoListener(Long id) {
        container.removeItem(id);
    }

    @PreDestroy
    void destroy() {
        eventBus.unsubscribe(this); // It's good manners to do this, even though we should be automatically unsubscribed when the UI is garbage collected
    }

    public void setData(List<AccionMantenimiento> accionMantenimientos) {
        container.removeAllItems();
        container.addAll(accionMantenimientos);
    }

    public void setEditDeleteListener(EditDeleteButtonValueRenderer.EditDeleteButtonClickListener listener) {
        if (listener != null) {
            this.getColumn("acciones").setRenderer(new EditDeleteButtonValueRenderer(listener)).setWidth(100);
        }
    }

    public static class Container extends BeanContainer<Long, AccionMantenimiento> {

        public Container() {
            super(AccionMantenimiento.class);
            setBeanIdResolver(accionMantenimiento -> accionMantenimiento.getId());
        }
    }
}
