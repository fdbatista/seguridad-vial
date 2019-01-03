package com.earandap.vehiculos.ui.views.vehiculos;

import com.earandap.vehiculos.domain.Vehiculo;
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
 * Created by Angel Luis on 10/1/2015.
 */
@SpringComponent
@UIScope
public class VehiculoGrid extends Grid {

    protected Container container;

    public static final String NUEVO_VEHICULO = "NUEVO-VEHICULO";
    public static final String ELIMINADO_VEHICULO = "ELIMINADO-VEHICULO";

    @Autowired
    private EventBus.ApplicationEventBus eventBus;

    @PostConstruct
    public void init() {
        this.container = new Container();
        container.addNestedContainerProperty("propietario.nombreCompleto");
        container.addNestedContainerProperty("afiliadoA.razonSocial");
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
        this.addColumn("placa").setWidth(90);
        this.addColumn("propietario.nombreCompleto");
        this.addColumn("serieMotor").setHeaderCaption("Serie").setWidth(90);
        this.addColumn("kmInicial").setWidth(100);
        this.addColumn("kmActual").setWidth(100);
        this.addColumn("afiliadoA.razonSocial").setHeaderCaption("Empresa");
        this.addColumn("acciones").setHeaderCaption("Acciones");

        //setRenderer();
        initFooterRow();
        initFilter();
        eventBus.subscribe(this);

    }

    private void initFooterRow() {
        final FooterRow footerRow = this.appendFooterRow();
        footerRow.getCell("placa").setHtml("Total:");
        footerRow.join("propietario.nombreCompleto", "serieMotor", "kmInicial", "kmActual", "afiliadoA.razonSocial", "acciones");
        // inital total count
        footerRow.getCell("propietario.nombreCompleto").setHtml("<b>" + container.getItemIds().size() + "</b>");
        // filter change count recalculate
        container.addItemSetChangeListener(event -> footerRow.getCell("propietario.nombreCompleto")
                .setHtml("<b>" + event.getContainer().getItemIds().size() + "</b>"));
    }

    private void initFilter() {
        GridCellFilter filter = new GridCellFilter(this);

        // simple filters
        filter.setTextFilter("placa", true, false, "Contiene");
        filter.setTextFilter("propietario.nombreCompleto", true, false, "Contiene");
        filter.setTextFilter("serieMotor", true, false, "Contiene");
        filter.setTextFilter("kmInicial", true, false, "Contiene");
        filter.setTextFilter("kmActual", true, false, "Contiene");
        filter.setTextFilter("afiliadoA.razonSocial", true, false, "Contiene");
    }

    @EventBusListenerTopic(topic = VehiculoGrid.NUEVO_VEHICULO)
    @EventBusListenerMethod
    public void nuevoVehiculoListener(Vehiculo vehiculo) {
        container.removeItem(vehiculo.getId());
        container.addBean(vehiculo);
    }

    @EventBusListenerTopic(topic = VehiculoGrid.ELIMINADO_VEHICULO)
    @EventBusListenerMethod
    public void eliminarElementoListener(Long id) {
        container.removeItem(id);
    }

    @PreDestroy
    void destroy() {
        eventBus.unsubscribe(this); // It's good manners to do this, even though we should be automatically unsubscribed when the UI is garbage collected
    }

    public void setData(List<Vehiculo> vehiculos) {
        container.removeAllItems();
        container.addAll(vehiculos);
    }

    public void setEditDeleteListener(EditDeleteButtonValueRenderer.EditDeleteButtonClickListener listener) {
        if (listener != null) {
            this.getColumn("acciones").setRenderer(new EditDeleteButtonValueRenderer(listener)).setWidth(100);
        }
    }

    public static class Container extends BeanContainer<Long, Vehiculo> {

        public Container() {
            super(Vehiculo.class);
            setBeanIdResolver(vehiculo -> vehiculo.getId());
        }
    }
}
