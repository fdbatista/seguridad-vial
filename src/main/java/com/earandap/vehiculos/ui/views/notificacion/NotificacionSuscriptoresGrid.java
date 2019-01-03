package com.earandap.vehiculos.ui.views.notificacion;

import com.earandap.vehiculos.domain.TerceroNotificacion;
import com.earandap.vehiculos.repository.TerceroRepository;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.gridutil.renderer.DeleteButtonValueRenderer;

import javax.annotation.PostConstruct;

/**
 * Created by Angel Luis on 1/3/2016.
 */
@SpringComponent
@UIScope
public class NotificacionSuscriptoresGrid extends Grid {

    protected Container container;

    int id;

    @Autowired
    private TerceroRepository terceroRepository;

    @PostConstruct
    public void init() {
        this.container = new Container();
        Responsive.makeResponsive(this);
        this.setSizeFull();
        this.setHeight("250px");
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
        this.removeColumn("notificacion");
        this.removeColumn("id");
        this.setColumnOrder("tercero","leido","notificado","activa");

        initExtraHeaderRow();
    }

    private void initExtraHeaderRow() {
        final FooterRow footerRow = this.appendFooterRow();
        footerRow.getCell("tercero").setHtml("Total:");
        footerRow.join("leido","notificado","activa","acciones");
        // inital total count
        footerRow.getCell("leido").setHtml("<b>" + container.getItemIds().size() + "</b>");
        // filter change count recalculate
        container.addItemSetChangeListener(event -> footerRow.getCell("leido")
                .setHtml("<b>" + event.getContainer().getItemIds().size() + "</b>"));
    }

    public void setDeleteListener(DeleteButtonValueRenderer.RendererClickListener listener) {
        if (listener != null) {
            this.getColumn("acciones").setRenderer(new DeleteButtonValueRenderer(listener)).setWidth(100);
        }
    }

    public static class Container extends BeanContainer<TerceroNotificacion.Id, TerceroNotificacion> {
        public Container() {
            super(TerceroNotificacion.class);
            setBeanIdResolver(terceroNotificacion -> terceroNotificacion.getId());
        }
    }

}
