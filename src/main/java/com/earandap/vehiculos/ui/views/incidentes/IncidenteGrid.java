package com.earandap.vehiculos.ui.views.incidentes;

import com.earandap.vehiculos.domain.Incidente;
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
import org.vaadin.gridutil.renderer.EditDeleteButtonValueRenderer;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.text.DateFormat;
import java.util.List;

/**
 * @Autor Eduardo Aranda
 * @Version 0.1
 */
@SpringComponent
@UIScope
public class IncidenteGrid extends Grid {

    protected Container container;
    public static final String NUEVO_INCIDENTE = "NUEVO-INCIDENTE";
    public static final String ELIMINADO_INCIDENTE = "ELIMINADO-INCIDENTE";

    @Autowired
    private EventBus.ApplicationEventBus eventBus;

    @PostConstruct
    public void init() {

        this.container = new Container();
        this.setSizeFull();
        this.setSelectionMode(SelectionMode.SINGLE);

        container.addNestedContainerProperty("vehiculo.placa");
        container.addNestedContainerProperty("conductor.persona.nombreCompleto");

        GeneratedPropertyContainer gpc = new GeneratedPropertyContainer(container);
        gpc.addGeneratedProperty("actions", new PropertyValueGenerator<String>() {
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
        this.addColumn("fecha");
        this.addColumn("vehiculo.placa").setHeaderCaption("Veh\u00EDculo");
        this.addColumn("conductor.persona.nombreCompleto").setHeaderCaption("Conductor");
        this.addColumn("actions").setHeaderCaption("Acciones");

        setRenderer();
        initFooterRow();
        initFilter();
        eventBus.subscribe(this);

    }

    private void setRenderer() {
        this.getColumn("fecha").setRenderer(new DateRenderer(DateFormat.getDateInstance())).setWidth(210);
    }

    private void initFooterRow() {
        final FooterRow footerRow = this.appendFooterRow();
        footerRow.getCell("fecha").setHtml("Total:");
        footerRow.join("fecha", "vehiculo.placa", "conductor.persona.nombreCompleto","actions");
        // inital total count
        footerRow.getCell("fecha").setHtml("<b>" + container.getItemIds().size() + "</b>");
        // filter change count recalculate
        container.addItemSetChangeListener(event -> footerRow.getCell("fecha")
                .setHtml("<b>" + event.getContainer().getItemIds().size() + "</b>"));
    }

    private void initFilter() {
        GridCellFilter filter = new GridCellFilter(this);

        // simple filters
        filter.setTextFilter("vehiculo.placa", true, false, "La placa contiene");
        filter.setTextFilter("conductor.persona.nombreCompleto", true, false, "El nombre contiene");
        filter.setDateFilter("fecha");
    }

    @EventBusListenerTopic(topic = IncidenteGrid.NUEVO_INCIDENTE)
    @EventBusListenerMethod
    public void nuevoIncidenteListener(Incidente incidente) {
        container.removeItem(incidente.getId());
        container.addBean(incidente);
    }

    @EventBusListenerTopic(topic = IncidenteGrid.ELIMINADO_INCIDENTE)
    @EventBusListenerMethod
    public void eliminarIncidenteListener(Long id) {
        container.removeItem(id);
    }

    @PreDestroy
    void destroy() {
        eventBus.unsubscribe(this); // It's good manners to do this, even though we should be automatically unsubscribed when the UI is garbage collected
    }

    public void setData(List<Incidente> incidentes) {
        container.removeAllItems();
        container.addAll(incidentes);
    }

    public void setEditDeleteListener(EditDeleteButtonValueRenderer.EditDeleteButtonClickListener listener) {
        if (listener != null) {
            this.getColumn("actions").setRenderer(new EditDeleteButtonValueRenderer(listener)).setWidth(100);
        }
    }

    public static class Container extends BeanContainer<Long, Incidente> {
        public Container() {
            super(Incidente.class);
            setBeanIdResolver(incidente -> incidente.getId());
        }
    }
}
