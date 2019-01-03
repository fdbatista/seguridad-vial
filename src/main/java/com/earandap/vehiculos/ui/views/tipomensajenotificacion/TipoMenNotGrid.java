package com.earandap.vehiculos.ui.views.tipomensajenotificacion;

import com.earandap.vehiculos.domain.TipoNotificacionMensaje;
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
 * Created by Angel Luis on 12/26/2015.
 */
@SpringComponent
@UIScope
public class TipoMenNotGrid extends Grid {

    private Container container;

    public static final String NUEVO_TIPOMENNOT = "NUEVO-TIPOMENNOT";
    public static final String DELETE_TIPOMENNOT = "ELIMINAR-TIPOMENNOT";

    @Autowired
    private EventBus.ApplicationEventBus eventBus;

    @PostConstruct
    public void init(){
        this.container = new Container();
        container.addNestedContainerProperty("tipoNotificacion.descripcion");
        container.addNestedContainerProperty("tipoMensaje.descripcion");
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
        this.addColumn("tipoMensaje.descripcion").setHeaderCaption("Tipo de Mensaje");
        this.addColumn("tipoNotificacion.descripcion").setHeaderCaption("Tipo de Notificaci\u00F3n");
        this.addColumn("acciones").setHeaderCaption("Acciones");

        initFooterRow();
        initFilter();
        eventBus.subscribe(this);

    }

    private void initFooterRow() {
        final FooterRow footerRow = this.appendFooterRow();
        footerRow.getCell("tipoMensaje.descripcion").setHtml("Total:");
        footerRow.join("tipoNotificacion.descripcion", "acciones");
        // inital total count
        footerRow.getCell("tipoNotificacion.descripcion").setHtml("<b>" + container.getItemIds().size() + "</b>");
        // filter change count recalculate
        container.addItemSetChangeListener(event -> footerRow.getCell("tipoNotificacion.descripcion")
                .setHtml("<b>" + event.getContainer().getItemIds().size() + "</b>"));
    }

    private void initFilter() {
        GridCellFilter filter = new GridCellFilter(this);

        // simple filters
        filter.setTextFilter("tipoMensaje.descripcion", true, false, "Contiene");
        filter.setTextFilter("tipoNotificacion.descripcion", true, false, "Contiene");
    }

    @EventBusListenerTopic(topic = TipoMenNotGrid.NUEVO_TIPOMENNOT)
    @EventBusListenerMethod
    public void nuevoTipoMensajeNotificacionListener(TipoNotificacionMensaje tipoNotificacionMensaje) {
        container.removeItem(tipoNotificacionMensaje.getId());
        container.addBean(tipoNotificacionMensaje);
    }

    @EventBusListenerTopic(topic = TipoMenNotGrid.DELETE_TIPOMENNOT)
    @EventBusListenerMethod
    public void eliminarTipoMensajeNotificacionListener(TipoNotificacionMensaje.Id id) {
        container.removeItem(id);
    }

    @PreDestroy
    void destroy() {
        eventBus.unsubscribe(this); // It's good manners to do this, even though we should be automatically unsubscribed when the UI is garbage collected
    }

    public void setData(List<TipoNotificacionMensaje> tipoNotificacionMensajes) {
        container.removeAllItems();
        container.addAll(tipoNotificacionMensajes);
    }

    public void setEditListener(EditDeleteButtonValueRenderer.EditDeleteButtonClickListener listener) {
        if (listener != null) {
            this.getColumn("acciones").setRenderer(new EditDeleteButtonValueRenderer(listener)).setWidth(100);
        }
    }

    public static class Container extends BeanContainer<TipoNotificacionMensaje.Id, TipoNotificacionMensaje> {
        public Container() {
            super(TipoNotificacionMensaje.class);
            setBeanIdResolver(tipoNotificacionMensaje -> tipoNotificacionMensaje.getId());
        }
    }
}
