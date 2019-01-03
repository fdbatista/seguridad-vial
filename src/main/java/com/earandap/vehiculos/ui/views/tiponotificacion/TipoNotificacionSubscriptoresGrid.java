package com.earandap.vehiculos.ui.views.tiponotificacion;

import com.earandap.vehiculos.domain.Suscriptor;
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
 * Created by Angel Luis on 1/2/2016.
 */
@SpringComponent
@UIScope
public class TipoNotificacionSubscriptoresGrid extends Grid {

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
        //this.removeColumn("id");
        //this.removeColumn("tercero");
        //this.removeColumn("notificacion");
        //this.removeColumn("inactivo");
        //this.removeColumn("observacion");
        //this.removeColumn("telefono");
        //this.removeColumn("tipoDocumento");
        this.removeColumn("notificacion");
        this.removeColumn("id");

        //this.setColumnOrder("persona","observacion","acciones");

        initExtraHeaderRow();
    }

    private void initExtraHeaderRow() {
        final FooterRow footerRow = this.appendFooterRow();
        //footerRow.getCell("id").setHtml("Total:");
        footerRow.join("tercero","acciones");
        // inital total count
        footerRow.getCell("tercero").setHtml("<b>" + container.getItemIds().size() + "</b>");
        // filter change count recalculate
        container.addItemSetChangeListener(event -> footerRow.getCell("tercero")
                .setHtml("<b>" + event.getContainer().getItemIds().size() + "</b>"));
    }

    public void setDeleteListener(DeleteButtonValueRenderer.RendererClickListener listener) {
        if (listener != null) {
            this.getColumn("acciones").setRenderer(new DeleteButtonValueRenderer(listener)).setWidth(100);
        }
    }

    public static class Container extends BeanContainer<Suscriptor.Id, Suscriptor> {
        public Container() {
            super(Suscriptor.class);
            setBeanIdResolver(suscriptor -> suscriptor.getId());
        }
    }

}
