package com.earandap.vehiculos.ui.views.capacitacion;

import com.earandap.vehiculos.domain.PersonaCapacitacion;
import com.earandap.vehiculos.repository.PersonaCapacitacionRepository;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import de.steinwedel.messagebox.MessageBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.gridutil.renderer.EditDeleteButtonValueRenderer;

import javax.annotation.PostConstruct;

/**
 * Created by Angel Luis on 10/21/2015.
 */
@SpringComponent
@UIScope
public class CapacitacionPersonaGrid extends Grid {

    protected Container container;

    private MessageBox mb;

    int id;

    @Autowired
    private PersonaCapacitacionRepository personaCapacitacionRepository;

    @PostConstruct
    public void init() {
        this.container = new Container();
        Responsive.makeResponsive(this);
        this.setSizeFull();
        this.setHeight("250px");
        this.setSelectionMode(SelectionMode.SINGLE);

        id = -1;

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
        this.removeColumn("id");
        this.removeColumn("capacitacion");
        this.removeColumn("puntajeObtenido");
        this.setColumnOrder("persona", "observacion", /*"puntajeObtenido",*/ "acciones");

        initExtraHeaderRow();
    }

    private void initExtraHeaderRow() {
        final FooterRow footerRow = this.appendFooterRow();
        footerRow.getCell("persona").setHtml("Total:");
        footerRow.join("observacion", "acciones");
        // inital total count
        footerRow.getCell("observacion").setHtml("<b>" + container.getItemIds().size() + "</b>");
        // filter change count recalculate
        container.addItemSetChangeListener(event -> footerRow.getCell("observacion")
                .setHtml("<b>" + event.getContainer().getItemIds().size() + "</b>"));
    }

    public void setEditDeleteListener(EditDeleteButtonValueRenderer.EditDeleteButtonClickListener listener) {
        if (listener != null) {
            this.getColumn("acciones").setRenderer(new EditDeleteButtonValueRenderer(listener)).setWidth(100);
        }
    }

    public static class Container extends BeanContainer<PersonaCapacitacion.Id, PersonaCapacitacion> {

        public Container() {
            super(PersonaCapacitacion.class);
            setBeanIdResolver(personaCapacitacion -> personaCapacitacion.getId());
        }
    }

}
