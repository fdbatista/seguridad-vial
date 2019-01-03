package com.earandap.vehiculos.ui.views.nomenclador;

import com.earandap.vehiculos.domain.nomenclador.Nomenclador;
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
 * Created by angel on 02/04/16.
 */
@SpringComponent
@UIScope
public class NomencladorGrid extends Grid {

    protected Container container;

    public static final String NUEVO_NOMENCLADOR = "NUEVO-NOMENCLADOR";
    public static final String ELIMINADO_NOMENCLADOR = "ELIMINADO-NOMENCLADOR";

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
        this.addColumn("tipo").setWidth(200);
        this.addColumn("codigo").setHeaderCaption("Codigo").setWidth(200);
        this.addColumn("descripcion").setHeaderCaption("Descripcion");
        this.addColumn("acciones").setHeaderCaption("Acciones");

        initFooterRow();
        initFilter();
        eventBus.subscribe(this);

    }

    private void initFooterRow() {
        final Grid.FooterRow footerRow = this.appendFooterRow();
        footerRow.getCell("tipo").setHtml("Total:");
        footerRow.join("codigo", "descripcion", "acciones");
        // inital total count
        footerRow.getCell("codigo").setHtml("<b>" + container.getItemIds().size() + "</b>");
        // filter change count recalculate
        container.addItemSetChangeListener(event -> footerRow.getCell("codigo")
                .setHtml("<b>" + event.getContainer().getItemIds().size() + "</b>"));
    }

    private void initFilter() {
        GridCellFilter filter = new GridCellFilter(this);

        // simple filters
        filter.setTextFilter("tipo", true, false, "Contiene");
        filter.setTextFilter("codigo", true, false, "Contiene");
        filter.setTextFilter("descripcion", true, false, "Contiene");
        // filter.setDateFilter("fecha");
        //filter.setTextFilter("km", true, false, "Contiene");
    }

    @EventBusListenerTopic(topic = NomencladorGrid.NUEVO_NOMENCLADOR)
    @EventBusListenerMethod
    public void nuevoNomencladorListener(Nomenclador nomenclador) {
        container.removeItem(nomenclador.getId());
        container.addBean(nomenclador);
    }

    @EventBusListenerTopic(topic = NomencladorGrid.ELIMINADO_NOMENCLADOR)
    @EventBusListenerMethod
    public void eliminarElementoListener(Long id) {
        container.removeItem(id);
    }

    @PreDestroy
    void destroy() {
        eventBus.unsubscribe(this); // It's good manners to do this, even though we should be automatically unsubscribed when the UI is garbage collected
    }

    public void setData(List<Nomenclador> nomencladores) {
        container.removeAllItems();
        container.addAll(nomencladores);
    }

    public void setEditDeleteListener(EditDeleteButtonValueRenderer.EditDeleteButtonClickListener listener) {
        if (listener != null) {
            this.getColumn("acciones").setRenderer(new EditDeleteButtonValueRenderer(listener)).setWidth(100);
        }
    }

    public static class Container extends BeanContainer<Long, Nomenclador> {

        public Container() {
            super(Nomenclador.class);
            setBeanIdResolver(nomenclador -> nomenclador.getId());
        }
    }
}
