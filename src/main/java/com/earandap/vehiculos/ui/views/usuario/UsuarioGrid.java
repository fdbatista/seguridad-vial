package com.earandap.vehiculos.ui.views.usuario;

import com.earandap.vehiculos.domain.Usuario;
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
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.text.DateFormat;
import java.util.List;
import org.vaadin.gridutil.renderer.EditDeleteButtonValueRenderer;

/**
 * Created by Angel Luis on 11/6/2015.
 */
@SpringComponent
@UIScope
public class UsuarioGrid extends Grid {

    protected Container container;

    public static final String NUEVO_USUARIO = "NUEVO-USUARIO";
    public static final String ELIMINADO_USUARIO = "ELIMINADO-USUARIO";

    @Autowired
    private EventBus.ApplicationEventBus eventBus;

    //private EditButtonValueRenderer.RendererClickListener editButtonClickListener;

    @PostConstruct
    public void init() {
        this.container = new Container();
        container.addNestedContainerProperty("persona.nombreCompleto");
        container.addNestedContainerProperty("perfil.nombre");
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
        this.addColumn("usuario").setWidth(150);
        this.addColumn("persona.nombreCompleto").setHeaderCaption("Persona");
        this.addColumn("perfil.nombre").setHeaderCaption("Perfil").setWidth(200);
        this.addColumn("fechaCreacion").setHeaderCaption("Fecha Creación").setWidth(250).setRenderer(new DateRenderer(DateFormat.getDateInstance()));

        this.addColumn("acciones").setHeaderCaption("Acciones");

        initFooterRow();
        initFilter();
        eventBus.subscribe(this);
    }

    private void initFooterRow() {
        final Grid.FooterRow footerRow = this.appendFooterRow();
        footerRow.getCell("usuario").setHtml("Total:");
        footerRow.join("fechaCreacion", "persona.nombreCompleto", "perfil.nombre", "acciones");
        // inital total count
        footerRow.getCell("fechaCreacion").setHtml("<b>" + container.getItemIds().size() + "</b>");
        // filter change count recalculate
        container.addItemSetChangeListener(event -> footerRow.getCell("fechaCreacion")
                .setHtml("<b>" + event.getContainer().getItemIds().size() + "</b>"));
    }

    private void initFilter() {
        GridCellFilter filter = new GridCellFilter(this);

        // simple filters
        filter.setTextFilter("usuario", true, false, "");
        filter.setDateFilter("fechaCreacion");
        filter.setTextFilter("persona.nombreCompleto", true, false, "Contiene");
        filter.setTextFilter("perfil.nombre", true, false, "Contiene");
    }

    @EventBusListenerTopic(topic = UsuarioGrid.NUEVO_USUARIO)
    @EventBusListenerMethod
    public void nuevoUsuarioListener(Usuario usuario) {
        container.removeItem(usuario.getId());
        container.addBean(usuario);
    }
    
    @EventBusListenerTopic(topic = UsuarioGrid.ELIMINADO_USUARIO)
    @EventBusListenerMethod
    public void eliminarElementoListener(Long id) {
        container.removeItem(id);
    }

    @PreDestroy
    void destroy() {
        eventBus.unsubscribe(this); // It's good manners to do this, even though we should be automatically unsubscribed when the UI is garbage collected
    }

    public void setData(List<Usuario> usuarios) {
        container.removeAllItems();
        container.addAll(usuarios);
    }

    public void setEditDeleteListener(EditDeleteButtonValueRenderer.EditDeleteButtonClickListener listener) {
        if (listener != null) {
            this.getColumn("acciones").setRenderer(new EditDeleteButtonValueRenderer(listener)).setWidth(100);
        }
    }

    public static class Container extends BeanContainer<Long, Usuario> {

        public Container() {
            super(Usuario.class);
            setBeanIdResolver(usuario -> usuario.getId());
        }
    }
}
