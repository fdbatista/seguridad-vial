package com.earandap.vehiculos.ui.views.terceronotificacion;

import com.earandap.vehiculos.domain.TerceroNotificacion;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.data.util.converter.Converter;
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
import java.util.Locale;

/**
 * Created by angel on 13/04/16.
 */
@SpringComponent
@UIScope
public class TerceroNotificacionGrid extends Grid {

    private Container container;

    public static final String NUEVO_TERCERONOTIFICACION = "NUEVO-TERCERONOTIFICACION";
    public static final String DELETE_TERCERONOTIFICACION = "ELIMINAR-TERCERONOTIFICACION";

    @Autowired
    private EventBus.ApplicationEventBus eventBus;

    @PostConstruct
    public void init(){
        this.container = new Container();
        container.addNestedContainerProperty("id.tercero.nombreTercero");
        container.addNestedContainerProperty("id.notificacion.fechaNotificacion");
        container.addNestedContainerProperty("id.notificacion.encabezado");
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
        this.addColumn("id.tercero.nombreTercero").setHeaderCaption("Tercero");

        this.addColumn("id.notificacion.fechaNotificacion").setHeaderCaption("Fecha Notificacion").setRenderer(new DateRenderer(DateFormat.getDateInstance())).setWidth(200);
        this.addColumn("id.notificacion.encabezado").setHeaderCaption("Encabezado");
        this.addColumn("leido").setHeaderCaption("Leida").setConverter(new Converter<String, Boolean>() {
            @Override
            public Boolean convertToModel(String value, Class<? extends Boolean> targetType, Locale locale) throws ConversionException {
                return null;
            }

            @Override
            public String convertToPresentation(Boolean value, Class<? extends String> targetType, Locale locale) throws ConversionException {
                return value?"Si":"No";
            }

            @Override
            public Class<Boolean> getModelType() {
                return Boolean.class;
            }

            @Override
            public Class<String> getPresentationType() {
                return String.class;
            }
        });
        this.addColumn("acciones").setHeaderCaption("Acciones");

        //setRenderer();
        initFooterRow();
        initFilter();
        eventBus.subscribe(this);

    }

    private void initFooterRow() {
        final FooterRow footerRow = this.appendFooterRow();
        footerRow.getCell("id.tercero.nombreTercero").setHtml("Total:");
        footerRow.join("id.notificacion.fechaNotificacion", "id.notificacion.encabezado", "leido",  "acciones");
        // inital total count
        footerRow.getCell("leido").setHtml("<b>" + container.getItemIds().size() + "</b>");
        // filter change count recalculate
        container.addItemSetChangeListener(event -> footerRow.getCell("leido")
                .setHtml("<b>" + event.getContainer().getItemIds().size() + "</b>"));
    }

    private void initFilter() {
        GridCellFilter filter = new GridCellFilter(this);

        // simple filters
        filter.setTextFilter("id.tercero.nombreTercero", true, false, "Contiene");
        filter.setDateFilter("id.notificacion.fechaNotificacion");
        filter.setTextFilter("id.notificacion.encabezado", true, false, "Contiene");
        filter.setBooleanFilter("leido");
    }

    @EventBusListenerTopic(topic = TerceroNotificacionGrid.NUEVO_TERCERONOTIFICACION)
    @EventBusListenerMethod
    public void nuevoTerceroNotificacionListener(TerceroNotificacion terceroNotificacion) {
        container.removeItem(terceroNotificacion.getId());
        container.addBean(terceroNotificacion);
    }

    @EventBusListenerTopic(topic = TerceroNotificacionGrid.DELETE_TERCERONOTIFICACION)
    @EventBusListenerMethod
    public void eliminarTerceroNotificacionListener(TerceroNotificacion.Id id) {
        container.removeItem(id);
    }

    @PreDestroy
    void destroy() {
        eventBus.unsubscribe(this); // It's good manners to do this, even though we should be automatically unsubscribed when the UI is garbage collected
    }

    public void setData(List<TerceroNotificacion> terceroNotificaciones) {
        container.removeAllItems();
        container.addAll(terceroNotificaciones);
    }

    public void setEditListener(EditDeleteButtonValueRenderer.EditDeleteButtonClickListener listener) {
        if (listener != null) {
            this.getColumn("acciones").setRenderer(new EditDeleteButtonValueRenderer(listener)).setWidth(100);
        }
    }

    public static class Container extends BeanContainer<TerceroNotificacion.Id, TerceroNotificacion> {
        public Container() {
            super(TerceroNotificacion.class);
            setBeanIdResolver(terceroNotificacion -> terceroNotificacion.getId());
        }
    }
}
