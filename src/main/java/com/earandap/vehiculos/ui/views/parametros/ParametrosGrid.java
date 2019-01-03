package com.earandap.vehiculos.ui.views.parametros;

import com.earandap.vehiculos.domain.Parametro;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.gridutil.cell.GridCellFilter;
import org.vaadin.gridutil.renderer.EditButtonValueRenderer;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

/**
 * Created by angel on 02/04/16.
 */
@SpringComponent
@UIScope
public class ParametrosGrid extends Grid {

    protected Container container;

    public static final String NUEVO_PARAMETRO = "NUEVO-PARAMETRO";

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

        this.setContainerDataSource(gpc);

        this.removeAllColumns();
        this.addColumn("nombreParametro").setHeaderCaption("Nombre").setWidth(370);
        this.addColumn("valorParametro").setHeaderCaption("Valor");
        this.addColumn("acciones").setHeaderCaption("Acciones");

        initFooterRow();
        initFilter();
        eventBus.subscribe(this);

    }

    private void initFooterRow() {
        final Grid.FooterRow footerRow = this.appendFooterRow();
        footerRow.getCell("nombreParametro").setHtml("Total:");
        footerRow.join("nombreParametro", "valorParametro", "acciones");
        footerRow.getCell("nombreParametro").setHtml("<b>" + container.getItemIds().size() + "</b>");
        // filter change count recalculate
        container.addItemSetChangeListener(event -> footerRow.getCell("nombreParametro")
                .setHtml("<b>" + event.getContainer().getItemIds().size() + "</b>"));
    }

    private void initFilter() {
        GridCellFilter filter = new GridCellFilter(this);

        // simple filters
        filter.setTextFilter("nombreParametro", true, false, "Contiene");
        filter.setTextFilter("valorParametro", true, false, "Contiene");
        // filter.setDateFilter("fecha");
        //filter.setTextFilter("km", true, false, "Contiene");
    }

    @EventBusListenerTopic(topic = ParametrosGrid.NUEVO_PARAMETRO)
    @EventBusListenerMethod
    public void nuevoParametroListener(Parametro parametro) {
        container.removeItem(parametro.getId());
        container.addBean(parametro);
    }

    @PreDestroy
    void destroy() {
        eventBus.unsubscribe(this); // It's good manners to do this, even though we should be automatically unsubscribed when the UI is garbage collected
    }

    public void setData(List<Parametro> parametroes) {
        container.removeAllItems();
        container.addAll(parametroes);
    }

    public void setEditListener(EditButtonValueRenderer.RendererClickListener listener) {
        if (listener != null) {
            this.getColumn("acciones").setRenderer(new EditButtonValueRenderer(listener)).setWidth(100);
        }
    }

    public static class Container extends BeanContainer<Long, Parametro> {

        public Container() {
            super(Parametro.class);
            setBeanIdResolver(parametro -> parametro.getId());
        }
    }
}
