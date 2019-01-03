package com.earandap.vehiculos.ui.views.perfil;

import com.earandap.vehiculos.domain.Perfil;
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
 * Created by Angel Luis on 11/11/2015.
 */
@SpringComponent
@UIScope
public class PerfilGrid extends Grid {

    protected Container container;

    public static final String NUEVO_PERFIL = "NUEVO-PERFIL";
    public static final String ELIMINADO_PERFIL = "ELIMINADO-PERFIL";

    @Autowired
    private EventBus.ApplicationEventBus eventBus;

    @PostConstruct
    public void init(){
        this.container = new Container();
        //container.addNestedContainerProperty("perfil.nombre");
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
        this.addColumn("nombre");
        this.addColumn("acciones").setHeaderCaption("Acciones");

        initFooterRow();
        initFilter();
        eventBus.subscribe(this);

    }

    private void initFooterRow() {
        final Grid.FooterRow footerRow = this.appendFooterRow();
        footerRow.getCell("nombre").setHtml("Total:");
        footerRow.join("acciones");
        // inital total count
        footerRow.getCell("acciones").setHtml("<b>" + container.getItemIds().size() + "</b>");
        // filter change count recalculate
        container.addItemSetChangeListener(event -> footerRow.getCell("acciones")
                .setHtml("<b>" + event.getContainer().getItemIds().size() + "</b>"));
    }

    private void initFilter() {
        GridCellFilter filter = new GridCellFilter(this);

        // simple filters
        filter.setTextFilter("nombre", true, false, "Contiene");
    }

    @EventBusListenerTopic(topic = PerfilGrid.NUEVO_PERFIL)
    @EventBusListenerMethod
    public void nuevoUsuarioListener(Perfil perfil) {
        container.removeItem(perfil.getId());
        container.addBean(perfil);
    }
    
    @EventBusListenerTopic(topic = PerfilGrid.ELIMINADO_PERFIL)
    @EventBusListenerMethod
    public void eliminarElementoListener(Long id) {
        container.removeItem(id);
    }

    @PreDestroy
    void destroy() {
        eventBus.unsubscribe(this); // It's good manners to do this, even though we should be automatically unsubscribed when the UI is garbage collected
    }

    public void setData(List<Perfil> perfiles) {
        container.removeAllItems();
        container.addAll(perfiles);
    }

    public void setEditDeleteListener(EditDeleteButtonValueRenderer.EditDeleteButtonClickListener listener) {
        if (listener != null) {
            this.getColumn("acciones").setRenderer(new EditDeleteButtonValueRenderer(listener)).setWidth(100);
        }
    }

    public static class Container extends BeanContainer<Long, Perfil> {
        public Container() {
            super(Perfil.class);
            setBeanIdResolver(perfil -> perfil.getId());
        }
    }
}
