package com.earandap.vehiculos.ui.views.incidentes;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.Investigacion;
import com.earandap.vehiculos.repository.InvestigacionRepository;
import com.earandap.vehiculos.repository.TerceroRepository;
import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ClickableRenderer;
import com.vaadin.ui.themes.ValoTheme;
import de.steinwedel.messagebox.ButtonId;
import de.steinwedel.messagebox.Icon;
import de.steinwedel.messagebox.MessageBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.gridutil.renderer.EditDeleteButtonValueRenderer;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Angel Luis on 11/30/2015.
 */
@SpringComponent
@UIScope
public class IncidenteInvestigacionGrid  extends Grid {

    protected Container container;
    private MessageBox mb;
    int id;

    @Autowired
    private InvestigacionRepository investigacionRepository;

    @Autowired
    private TerceroRepository terceroRepository;

    private Map<Long,Investigacion> investigacionModificados;

    private Map<Long,Investigacion> investigacionAdicionados;

    private Map<Long,Investigacion> investigacionEliminados;

    public Map<Long, Investigacion> getInvestigacionModificados() {
        return investigacionModificados;
    }

    public void setInvestigacionModificados(Map<Long, Investigacion> investigacionModificados) {
        this.investigacionModificados = investigacionModificados;
    }

    public Map<Long, Investigacion> getInvestigacionAdicionados() {
        return investigacionAdicionados;
    }

    public void setInvestigacionAdicionados(Map<Long, Investigacion> investigacionAdicionados) {
        this.investigacionAdicionados = investigacionAdicionados;
    }

    public Map<Long, Investigacion> getInvestigacionEliminados() {
        return investigacionEliminados;
    }

    public void setInvestigacionEliminados(Map<Long, Investigacion> investigacionEliminados) {
        this.investigacionEliminados = investigacionEliminados;
    }

    @PostConstruct
    public void init() {
        this.container = new Container();
        Responsive.makeResponsive(this);
        this.setSizeFull();
        this.setHeight("350px");
        this.setSelectionMode(SelectionMode.SINGLE);

        id =-1;

        investigacionAdicionados = new HashMap<Long,Investigacion>();
        investigacionModificados = new HashMap<Long,Investigacion>();
        investigacionEliminados = new HashMap<Long,Investigacion>();

        GeneratedPropertyContainer gpc = new GeneratedPropertyContainer(container);
        gpc.addGeneratedProperty("actions", new PropertyValueGenerator<String>() {
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

        container.addNestedContainerProperty("tercero.nombreTercero");

        this.removeAllColumns();
        this.addColumn("tercero.nombreTercero").setHeaderCaption("Tercero");
        this.addColumn("fechaInvestigacion").setHeaderCaption("Fecha Investigci\u00F3n").setWidth(180);
        this.addColumn("relato").setHeaderCaption("Relato");
        this.addColumn("actions").setHeaderCaption("Acciones");

        this.setEditDeleteListener(new EditDeleteButtonValueRenderer.EditDeleteButtonClickListener() {
            @Override
            public void onEdit(ClickableRenderer.RendererClickEvent rendererClickEvent) {
                Investigacion investigacion = container.getItem(rendererClickEvent.getItemId()).getBean();
                adicionaModificarBoton(investigacion,false);
            }

            @Override
            public void onDelete(ClickableRenderer.RendererClickEvent rendererClickEvent) {
                MessageBox messageBox = MessageBox.showPlain(
                        Icon.QUESTION,
                        "Interrogante",
                        "Est\u00E1 seguro que quiere eliminar?",
                        ButtonId.YES,
                        ButtonId.NO);
                messageBox.getButton(ButtonId.YES).addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent e) {
                        Investigacion investigacion = container.getItem(rendererClickEvent.getItemId()).getBean();
                        if (investigacionAdicionados.containsKey(investigacion.getId()))
                            investigacionAdicionados.remove(investigacion.getId());
                        if (investigacionModificados.containsKey(investigacion.getId()))
                            investigacionModificados.remove(investigacion.getId());
                        investigacionEliminados.put(investigacion.getId(),investigacion);
                        container.removeItem(investigacion.getId());
                        StaticMembers.showNotificationMessage("Notificaci\u00F3n", "La investigaci\u00F3n se eliminar\u00E1 luego de guardar los cambios");
                    }
                });
            }
        });
        initExtraHeaderRow();
    }

    private void initExtraHeaderRow() {
        HeaderRow firstHeaderRow = this.prependHeaderRow();
        firstHeaderRow.join("tercero.nombreTercero", "fechaInvestigacion", "relato");
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSizeFull();
        buttonLayout.setSpacing(true);
        buttonLayout.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
        firstHeaderRow.getCell("actions").setComponent(buttonLayout);
        Button adicionar = new Button("", (Button.ClickListener) event -> {
            this.adicionaModificarBoton(new Investigacion(),true);
        }
        );
        adicionar.setCaption("Adicionar investigaci\u00F3n al incidente");
        adicionar.setIcon(FontAwesome.PLUS_SQUARE);
        adicionar.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        adicionar.addStyleName(ValoTheme.BUTTON_TINY);
        buttonLayout.addComponent(adicionar);

    }

    public void setEditDeleteListener(EditDeleteButtonValueRenderer.EditDeleteButtonClickListener listener) {
        if (listener != null) {
            this.getColumn("actions").setRenderer(new EditDeleteButtonValueRenderer(listener)).setWidth(100);
        }
    }

    public void restablecerListados() {
        investigacionAdicionados.clear();
        investigacionModificados.clear();
        investigacionEliminados.clear();
    }

    private void adicionaModificarBoton(Investigacion investigacion,Boolean adicionar){
        InvestigacionForm form = new InvestigacionForm(investigacion);
        form.setTerceroRepository(terceroRepository);
        mb = MessageBox.showCustomized(Icon.NONE, "Adicionar Investigaci\u00F3n", form
                , buttonId -> {
            if (buttonId == ButtonId.YES) {
                try {
                    form.getInvestigacionBeanFieldGroup().commit();
                    BeanItem<Investigacion> item = form.getInvestigacionBeanFieldGroup().getItemDataSource();
                    if(adicionar) {
                        item.getBean().setId(id--);
                        investigacionAdicionados.put(item.getBean().getId(), item.getBean());
                        container.addBean(item.getBean());
                    }
                    else{
                        investigacionAdicionados.remove(item.getBean().getId());
                        investigacionModificados.put(item.getBean().getId(), item.getBean());
                        container.removeItem(item.getBean().getId());
                        container.addBean(item.getBean());
                    }
                    mb.close();

                } catch (FieldGroup.CommitException e) {
                    Collection<Validator.InvalidValueException> values = e.getInvalidFields().values();
                    if (!values.isEmpty()) {
                        Notification.show(values.iterator().next().getMessage(), e.getCause().getMessage(), Notification.Type.TRAY_NOTIFICATION);
                    } else if (e.getCause() instanceof Validator.InvalidValueException) {
                        Notification.show(e.getCause().getMessage(), Notification.Type.TRAY_NOTIFICATION);
                    }
                }
            } else {
                mb.close();
            }
        }, ButtonId.CANCEL, ButtonId.YES).setWidth("500px").setHeight("300px");
        mb.setModal(true);
        mb.setAutoClose(false);
        mb.getButton(ButtonId.YES).setCaption("Aceptar");
        mb.getButton(ButtonId.CANCEL).setCaption("Cancelar");
        mb.setWidth(550, Unit.PIXELS);
        mb.setHeight(550, Unit.PIXELS);
    }


    public static class Container extends BeanContainer<Long, Investigacion> {
        public Container() {
            super(Investigacion.class);
            setBeanIdResolver(investigacion -> investigacion.getId());
        }
    }
}
