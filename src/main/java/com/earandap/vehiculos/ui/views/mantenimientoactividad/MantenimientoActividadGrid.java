package com.earandap.vehiculos.ui.views.mantenimientoactividad;

import com.earandap.vehiculos.domain.MantenimientoActividad;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.gridutil.cell.GridCellFilter;
import org.vaadin.gridutil.renderer.EditDeleteButtonValueRenderer;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

/**
 * Created by Angel Luis on 10/24/2015.
 */
@SpringComponent
@UIScope
public class MantenimientoActividadGrid extends Grid{

    private Container container;

    public static final String NUEVO_MANTENIMIENTOACTIVIDAD = "NUEVO-MANTENIMIENTOACTIVIDAD";
    public static final String DELETE_MANTENIMIENTOACTIVIDAD = "ELIMINAR-MANTENIMIENTOACTIVIDAD";

    @Autowired
    private EventBus.ApplicationEventBus eventBus;

    @PostConstruct
    public void init(){
        this.container = new Container();
        container.addNestedContainerProperty("claseVehiculo.descripcion");
        container.addNestedContainerProperty("actividad.descripcion");
        container.addNestedContainerProperty("tipoMantenimiento.descripcion");
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
        this.addColumn("claseVehiculo.descripcion").setHeaderCaption("Clase Veh\u00EDculo");
        this.addColumn("actividad.descripcion").setHeaderCaption("Actividad");
        this.addColumn("tipoMantenimiento.descripcion").setHeaderCaption("Descripci\u00F3n");
        this.addColumn("tiempoReposicion");
        this.addColumn("kmReposicion");
        this.addColumn("acciones").setHeaderCaption("Acciones");

        //setRenderer();
        initFooterRow();
        initFilter();
        eventBus.subscribe(this);

    }

    private void initFooterRow() {
        final FooterRow footerRow = this.appendFooterRow();
        footerRow.getCell("claseVehiculo.descripcion").setHtml("Total:");
        footerRow.join("actividad.descripcion", "tipoMantenimiento.descripcion", "tiempoReposicion", "kmReposicion", "acciones");
        // inital total count
        footerRow.getCell("actividad.descripcion").setHtml("<b>" + container.getItemIds().size() + "</b>");
        // filter change count recalculate
        container.addItemSetChangeListener(event -> footerRow.getCell("actividad.descripcion")
                .setHtml("<b>" + event.getContainer().getItemIds().size() + "</b>"));
    }

    private void initFilter() {
        GridCellFilter filter = new GridCellFilter(this);

        // simple filters
        filter.setTextFilter("claseVehiculo.descripcion", true, false, "Contiene");
        filter.setTextFilter("actividad.descripcion", true, false, "Contiene");
        filter.setTextFilter("tipoMantenimiento.descripcion", true, false, "Contiene");
        filter.setTextFilter("tiempoReposicion", true, false, "Contiene");
        filter.setTextFilter("kmReposicion", true, false, "Contiene");
    }

    @EventBusListenerTopic(topic = MantenimientoActividadGrid.NUEVO_MANTENIMIENTOACTIVIDAD)
    @EventBusListenerMethod
    public void nuevoMantenimientoActividadListener(MantenimientoActividad mantenimientoActividad) {
        container.removeItem(mantenimientoActividad.getId());
        container.addBean(mantenimientoActividad);
    }

    @EventBusListenerTopic(topic = MantenimientoActividadGrid.DELETE_MANTENIMIENTOACTIVIDAD)
    @EventBusListenerMethod
    public void eliminarMantenimientoActividadListener(MantenimientoActividad.Id id) {
        container.removeItem(id);
    }

    @PreDestroy
    void destroy() {
        eventBus.unsubscribe(this); // It's good manners to do this, even though we should be automatically unsubscribed when the UI is garbage collected
    }

    public void setData(List<MantenimientoActividad> mantenimientoActividades) {
        container.removeAllItems();
        container.addAll(mantenimientoActividades);
    }

    public void setEditListener(EditDeleteButtonValueRenderer.EditDeleteButtonClickListener listener) {
        if (listener != null) {
            this.getColumn("acciones").setRenderer(new EditDeleteButtonValueRenderer(listener)).setWidth(100);
        }
    }

    public static class Container extends BeanContainer<MantenimientoActividad.Id, MantenimientoActividad> {
        public Container() {
            super(MantenimientoActividad.class);
            setBeanIdResolver(mantenimientoActividad -> mantenimientoActividad.getId());
        }
    }
}
