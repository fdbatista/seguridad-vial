package com.earandap.vehiculos.ui.views.alistamiento;

import com.earandap.vehiculos.domain.AccionAlistamiento;
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
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.vaadin.gridutil.renderer.EditDeleteButtonValueRenderer;

/**
 * Created by Angel Luis on 11/2/2015.
 */
@SpringComponent
@UIScope
public class AlistamientoGrid extends Grid {

    protected Container container;

    public static final String NUEVO_ALISTAMIENTO = "NUEVO-ALISTAMIENTO";
    public static final String ELIMINADO_ALISTAMIENTO = "ELIMINADO-ALISTAMIENTO";

    @Autowired
    private EventBus.ApplicationEventBus eventBus;

    @PostConstruct
    public void init() {
        this.container = new Container();
        container.addNestedContainerProperty("tecnico.nombreTercero");
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
        this.addColumn("vehiculo.placa").setWidth(200);
        this.addColumn("tecnico.nombreTercero").setHeaderCaption("T\u00E9cnico");
        this.addColumn("fecha").setWidth(250).setRenderer(new DateRenderer(DateFormat.getDateInstance()));
        this.addColumn("operativo").setWidth(150).setConverter(new Converter<String, Boolean>() {
            @Override
            public Boolean convertToModel(String value, Class<? extends Boolean> targetType, Locale locale) throws ConversionException {
                return null;
            }

            @Override
            public String convertToPresentation(Boolean value, Class<? extends String> targetType, Locale locale) throws ConversionException {
                return value ? "Operativo" : "No Operativo";
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

        initFooterRow();
        initFilter();
        eventBus.subscribe(this);

    }

    private void initFooterRow() {
        final FooterRow footerRow = this.appendFooterRow();
        footerRow.getCell("vehiculo.placa").setHtml("Total:");
        footerRow.join("tecnico.nombreTercero", "fecha", "operativo", "acciones");
        // inital total count
        footerRow.getCell("tecnico.nombreTercero").setHtml("<b>" + container.getItemIds().size() + "</b>");
        // filter change count recalculate
        container.addItemSetChangeListener(event -> footerRow.getCell("tecnico.nombreTercero")
                .setHtml("<b>" + event.getContainer().getItemIds().size() + "</b>"));
    }

    private void initFilter() {
        GridCellFilter filter = new GridCellFilter(this);

        // simple filters
        filter.setTextFilter("vehiculo.placa", true, false, "Contiene");
        filter.setTextFilter("tecnico.nombreTercero", true, false, "Contiene");
        filter.setDateFilter("fecha");
        filter.setBooleanFilter("operativo");
        //filter.setTextFilter("operativo",true,false,"Contiene");
        List<String> l = new ArrayList<>();

    }

    @EventBusListenerTopic(topic = AlistamientoGrid.NUEVO_ALISTAMIENTO)
    @EventBusListenerMethod
    public void nuevoalistamientoListener(AccionAlistamiento accionAlistamiento) {
        container.removeItem(accionAlistamiento.getId());
        container.addBean(accionAlistamiento);
    }

    @EventBusListenerTopic(topic = AlistamientoGrid.ELIMINADO_ALISTAMIENTO)
    @EventBusListenerMethod
    public void eliminarElementoListener(Long id) {
        container.removeItem(id);
    }

    @PreDestroy
    void destroy() {
        eventBus.unsubscribe(this); // It's good manners to do this, even though we should be automatically unsubscribed when the UI is garbage collected
    }

    public void setData(List<AccionAlistamiento> accionAlistamientos) {
        container.removeAllItems();
        container.addAll(accionAlistamientos);
    }

    public void setEditDeleteListener(EditDeleteButtonValueRenderer.EditDeleteButtonClickListener listener) {
        if (listener != null) {
            this.getColumn("acciones").setRenderer(new EditDeleteButtonValueRenderer(listener)).setWidth(100);
        }
    }

    public static class Container extends BeanContainer<Long, AccionAlistamiento> {

        public Container() {
            super(AccionAlistamiento.class);
            setBeanIdResolver(accionAlistamiento -> accionAlistamiento.getId());
        }
    }
}
