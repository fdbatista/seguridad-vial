package com.earandap.vehiculos.ui.views.contrato;

import com.earandap.vehiculos.domain.Contrato;
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
import org.vaadin.gridutil.renderer.EditButtonValueRenderer;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.text.DateFormat;
import java.util.List;

/**
 * Created by Angel Luis on 11/29/2015.
 */
@SpringComponent
@UIScope
public class ContratoGrid extends Grid {

    protected Container container;

    public static final String NUEVO_CONTRATO = "NUEVO_CONTRATO";

    @Autowired
    private EventBus.ApplicationEventBus eventBus;

    @PostConstruct
    public void init(){
        this.container = new Container();
        container.addNestedContainerProperty("tercero.nombreTercero");
        container.addNestedContainerProperty("tipoContrato.descripcion");
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
        this.addColumn("numeroContrato").setWidth(100).setHeaderCaption("N\u00FAmero");
        this.addColumn("tercero.nombreTercero").setHeaderCaption("Tercero");
        this.addColumn("tipoContrato.descripcion").setHeaderCaption("Tipo Contrato").setWidth(150);
        this.addColumn("fechaInicio").setWidth(200).setRenderer(new DateRenderer(DateFormat.getDateInstance()));
        this.addColumn("duracionContrato").setWidth(120).setHeaderCaption("Duraci\u00F3n");
        this.addColumn("acciones").setHeaderCaption("Acciones");

        initFooterRow();
        initFilter();
        eventBus.subscribe(this);

    }

    private void initFooterRow() {
        final FooterRow footerRow = this.appendFooterRow();
        footerRow.getCell("numeroContrato").setHtml("Total:");
        footerRow.join("tercero.nombreTercero", "tipoContrato.descripcion", "fechaInicio", "duracionContrato","acciones");
        // inital total count
        footerRow.getCell("tercero.nombreTercero").setHtml("<b>" + container.getItemIds().size() + "</b>");
        // filter change count recalculate
        container.addItemSetChangeListener(event -> footerRow.getCell("tercero.nombreTercero")
                .setHtml("<b>" + event.getContainer().getItemIds().size() + "</b>"));
    }

    private void initFilter() {
        GridCellFilter filter = new GridCellFilter(this);

        // simple filters
        filter.setTextFilter("numeroContrato", true, false, "Contiene");
        filter.setTextFilter("tipoContrato.descripcion", true, false, "Contiene");
        filter.setTextFilter("tercero.nombreTercero", true, false, "Contiene");
        filter.setDateFilter("fechaInicio");
        filter.setTextFilter("duracionContrato", true, false, "Contiene");
    }

    @EventBusListenerTopic(topic = ContratoGrid.NUEVO_CONTRATO)
    @EventBusListenerMethod
    public void nuevomantenimientoListener(Contrato contrato) {
        container.removeItem(contrato.getId());
        container.addBean(contrato);
    }

    @PreDestroy
    void destroy() {
        eventBus.unsubscribe(this); // It's good manners to do this, even though we should be automatically unsubscribed when the UI is garbage collected
    }

    public void setData(List<Contrato> contratos) {
        container.removeAllItems();
        container.addAll(contratos);
    }

    public void setEditListener(EditButtonValueRenderer.RendererClickListener listener) {
        if (listener != null) {
            this.getColumn("acciones").setRenderer(new EditButtonValueRenderer(listener)).setWidth(100);
        }
    }


    public static class Container extends BeanContainer<Long, Contrato> {
        public Container() {
            super(Contrato.class);
            setBeanIdResolver(contrato -> contrato.getId());
        }
    }
}
