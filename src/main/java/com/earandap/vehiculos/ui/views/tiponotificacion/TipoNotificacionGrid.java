package com.earandap.vehiculos.ui.views.tiponotificacion;

import com.earandap.vehiculos.domain.TipoNotificacion;
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
 * Created by Angel Luis on 12/26/2015.
 */
@SpringComponent
@UIScope
public class TipoNotificacionGrid extends Grid {

    protected Container container;

    public static final String NUEVO_TIPONOTIFICACION = "NUEVO-TIPONOTIFICACION";
    public static final String ELIMINADO_TIPONOTIFICACION = "ELIMINADO-TIPONOTIFICACION";

    @Autowired
    private EventBus.ApplicationEventBus eventBus;

    @PostConstruct
    public void init() {
        this.container = new Container();
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
        this.addColumn("codigo").setHeaderCaption("C\u00F3digo");
        this.addColumn("descripcion").setHeaderCaption("Descripci\u00F3n");
        this.addColumn("acciones").setHeaderCaption("Acciones");

        initFooterRow();
        initFilter();
        eventBus.subscribe(this);

    }

    private void initFooterRow() {
        final Grid.FooterRow footerRow = this.appendFooterRow();
        footerRow.getCell("codigo").setHtml("Total:");
        footerRow.join("descripcion", "acciones");
        footerRow.getCell("descripcion").setHtml("<b>" + container.getItemIds().size() + "</b>");
        container.addItemSetChangeListener(event -> footerRow.getCell("descripcion")
                .setHtml("<b>" + event.getContainer().getItemIds().size() + "</b>"));
    }

    private void initFilter() {
        GridCellFilter filter = new GridCellFilter(this);

        // simple filters
        filter.setTextFilter("codigo", true, false, "Contiene");
        filter.setTextFilter("descripcion", true, false, "Contiene");
    }

    @EventBusListenerTopic(topic = TipoNotificacionGrid.NUEVO_TIPONOTIFICACION)
    @EventBusListenerMethod
    public void nuevaNotificacionListener(TipoNotificacion tipoNotificacion) {
        container.removeItem(tipoNotificacion.getId());
        container.addBean(tipoNotificacion);
    }

    @EventBusListenerTopic(topic = TipoNotificacionGrid.ELIMINADO_TIPONOTIFICACION)
    @EventBusListenerMethod
    public void eliminarElementoListener(Long id) {
        container.removeItem(id);
    }

    @PreDestroy
    void destroy() {
        eventBus.unsubscribe(this); // It's good manners to do this, even though we should be automatically unsubscribed when the UI is garbage collected
    }

    public void setData(List<TipoNotificacion> tipoNotificaciones) {
        container.removeAllItems();
        container.addAll(tipoNotificaciones);
    }

    public void setEditDeleteListener(EditDeleteButtonValueRenderer.EditDeleteButtonClickListener listener) {
        if (listener != null) {
            this.getColumn("acciones").setRenderer(new EditDeleteButtonValueRenderer(listener)).setWidth(100);
        }
    }

    public static class Container extends BeanContainer<Long, TipoNotificacion> {

        public Container() {
            super(TipoNotificacion.class);
            setBeanIdResolver(tipoNotificacion -> tipoNotificacion.getId());
        }
    }
}
