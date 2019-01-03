package com.earandap.vehiculos.ui.views.sucursales;

import com.earandap.vehiculos.domain.Sucursal;
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
 * Created by Angel Luis on 11/16/2015.
 */
@SpringComponent
@UIScope
public class SucursalGrid extends Grid {

    protected Container container;

    public static final String NUEVA_SUCURSAL = "NUEVA-SUCURSAL";
    public static final String ELIMINADO_SUCURSAL = "ELIMINADO-SUCURSAL";

    @Autowired
    private EventBus.ApplicationEventBus eventBus;

    @PostConstruct
    public void init() {

        this.container = new Container();
        this.container.addNestedContainerProperty("empresa.nombreTercero");
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
        this.addColumn("nombre").setHeaderCaption("Nombre Sucursal");
        this.addColumn("empresa.nombreTercero").setHeaderCaption("Empresa");
        this.addColumn("acciones").setHeaderCaption("Acciones");
        //setRenderer();
        initFooterRow();
        initFilter();
        eventBus.subscribe(this);
    }

    private void initFooterRow() {
        final Grid.FooterRow footerRow = this.appendFooterRow();
        footerRow.getCell("nombre").setHtml("Total:");
        footerRow.join("empresa.nombreTercero", "acciones");
        // inital total count
        footerRow.getCell("empresa.nombreTercero").setHtml("<b>" + container.getItemIds().size() + "</b>");
        // filter change count recalculate
        container.addItemSetChangeListener(event -> footerRow.getCell("empresa.nombreTercero")
                .setHtml("<b>" + event.getContainer().getItemIds().size() + "</b>"));
    }

    private void initFilter() {
        GridCellFilter filter = new GridCellFilter(this);

        // simple filters
        filter.setTextFilter("nombre", true, false, "Contiene");
        filter.setTextFilter("empresa.nombreTercero", true, false, "Contiene");
    }

    @EventBusListenerTopic(topic = SucursalGrid.NUEVA_SUCURSAL)
    @EventBusListenerMethod
    public void nuevaSucursalListener(Sucursal sucursal) {
        container.removeItem(sucursal.getId());
        container.addBean(sucursal);
    }

    @EventBusListenerTopic(topic = SucursalGrid.ELIMINADO_SUCURSAL)
    @EventBusListenerMethod
    public void eliminarElementoListener(Long id) {
        container.removeItem(id);
    }

    @PreDestroy
    void destroy() {
        eventBus.unsubscribe(this); // It's good manners to do this, even though we should be automatically unsubscribed when the UI is garbage collected
    }

    public void setData(List<Sucursal> sucursales) {
        container.removeAllItems();
        container.addAll(sucursales);
    }

    public void setEditDeleteListener(EditDeleteButtonValueRenderer.EditDeleteButtonClickListener listener) {
        if (listener != null) {
            this.getColumn("acciones").setRenderer(new EditDeleteButtonValueRenderer(listener)).setWidth(100);
        }
    }

    public static class Container extends BeanContainer<Long, Sucursal> {

        public Container() {
            super(Sucursal.class);
            setBeanIdResolver(sucursal -> sucursal.getId());
        }
    }
}
