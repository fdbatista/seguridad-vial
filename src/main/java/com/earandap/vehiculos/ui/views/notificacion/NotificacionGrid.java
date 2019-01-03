package com.earandap.vehiculos.ui.views.notificacion;

import com.earandap.vehiculos.domain.Notificacion;
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
 * Created by Angel Luis on 12/24/2015.
 */
@SpringComponent
@UIScope
public class NotificacionGrid extends Grid{

    protected Container container;

    public static final String NUEVA_NOTIFICACION = "NUEVA-NOTIFICACION";
    public static final String ELIMINADO_NOTIFICACION = "ELIMINADO-NOTIFICACION";

    @Autowired
    private EventBus.ApplicationEventBus eventBus;

    @PostConstruct
    public void init(){
        this.container = new Container();
        this.container.addNestedContainerProperty("tipoNotificacion.descripcion");
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

        setContainerDataSource(gpc);

        this.removeAllColumns();
        this.addColumn("tipoNotificacion.descripcion").setWidth(275).setHeaderCaption("Tipo Notificaci\u00F3n");
        this.addColumn("encabezado").setHeaderCaption("Encabezado").setWidth(500);
        this.addColumn("fechaNotificacion").setHeaderCaption("Fecha Notificaci\u00F3n").setWidth(200).setRenderer(new DateRenderer(DateFormat.getDateInstance()));
        this.addColumn("acciones").setHeaderCaption("Acciones");

        initFooterRow();
        initFilter();
        eventBus.subscribe(this);

    }

    private void initFooterRow() {
        final Grid.FooterRow footerRow = this.appendFooterRow();
        footerRow.getCell("tipoNotificacion.descripcion").setHtml("Total:");
        footerRow.join("fechaNotificacion","encabezado"/*, "detalle", "fechaCreacion"*/,"acciones");
        footerRow.getCell("fechaNotificacion").setHtml("<b>" + container.getItemIds().size() + "</b>");
        container.addItemSetChangeListener(event -> footerRow.getCell("fechaNotificacion")
                .setHtml("<b>" + event.getContainer().getItemIds().size() + "</b>"));
    }

    private void initFilter() {
        GridCellFilter filter = new GridCellFilter(this);

        // simple filters
        filter.setTextFilter("tipoNotificacion.descripcion", true, false, "Contiene");
        filter.setDateFilter("fechaNotificacion");
        filter.setTextFilter("encabezado", true, false, "Contiene");
        /*filter.setTextFilter("detalle", true, false, "Contiene");
        filter.setDateFilter("fechaCreacion");*/
    }

    @EventBusListenerTopic(topic = NotificacionGrid.NUEVA_NOTIFICACION)
    @EventBusListenerMethod
    public void nuevaNotificacionListener(Notificacion notificacion) {
        container.removeItem(notificacion.getId());
        container.addBean(notificacion);
    }
    
    @EventBusListenerTopic(topic = NotificacionGrid.ELIMINADO_NOTIFICACION)
    @EventBusListenerMethod
    public void eliminarElementoListener(Long id) {
        container.removeItem(id);
    }

    @PreDestroy
    void destroy() {
        eventBus.unsubscribe(this); // It's good manners to do this, even though we should be automatically unsubscribed when the UI is garbage collected
    }

    public void setData(List<Notificacion> notificaciones) {
        container.removeAllItems();
        container.addAll(notificaciones);
    }

    public void setEditDeleteListener(EditDeleteButtonValueRenderer.EditDeleteButtonClickListener listener) {
        if (listener != null) {
            this.getColumn("acciones").setRenderer(new EditDeleteButtonValueRenderer(listener)).setWidth(100);
        }
    }

    public static class Container extends BeanContainer<Long, Notificacion> {
        public Container() {
            super(Notificacion.class);
            setBeanIdResolver(notificacion -> notificacion.getId());
        }
    }
}
