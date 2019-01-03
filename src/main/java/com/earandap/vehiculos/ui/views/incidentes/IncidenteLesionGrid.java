package com.earandap.vehiculos.ui.views.incidentes;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.IncidenteLesion;
import com.earandap.vehiculos.repository.IncidenteLesionRepository;
import com.earandap.vehiculos.repository.TipoLesionRepository;
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
import java.util.List;
import java.util.Map;

/**
 * Created by felix.batista
 */
@SpringComponent
@UIScope
public class IncidenteLesionGrid extends Grid {

    protected Container container;

    private MessageBox mb;

    int id;

    @Autowired
    private IncidenteLesionRepository incidenteLesionRepository;

    @Autowired
    private TipoLesionRepository tipoLesionRepository;

    private Map<Long, IncidenteLesion> incidenteLesionModificados;

    private Map<Long, IncidenteLesion> incidenteLesionAdicionados;

    private Map<Long, IncidenteLesion> incidenteLesionEliminados;

    public Map<Long, IncidenteLesion> getIncidenteLesionModificados() {
        return incidenteLesionModificados;
    }

    public void setIncidenteLesionModificados(Map<Long, IncidenteLesion> incidenteLesionModificados) {
        this.incidenteLesionModificados = incidenteLesionModificados;
    }

    public Map<Long, IncidenteLesion> getIncidenteLesionAdicionados() {
        return incidenteLesionAdicionados;
    }

    public void setIncidenteLesionAdicionados(Map<Long, IncidenteLesion> incidenteLesionAdicionados) {
        this.incidenteLesionAdicionados = incidenteLesionAdicionados;
    }

    public Map<Long, IncidenteLesion> getIncidenteLesionEliminados() {
        return incidenteLesionEliminados;
    }

    public void setIncidenteLesionEliminados(Map<Long, IncidenteLesion> incidenteLesionEliminados) {
        this.incidenteLesionEliminados = incidenteLesionEliminados;
    }

    @PostConstruct
    public void init() {
        this.container = new Container();
        Responsive.makeResponsive(this);
        this.setSizeFull();
        this.setHeight("200px");
        this.setSelectionMode(SelectionMode.SINGLE);

        id = -1;

        incidenteLesionAdicionados = new HashMap<Long, IncidenteLesion>();
        incidenteLesionModificados = new HashMap<Long, IncidenteLesion>();
        incidenteLesionEliminados = new HashMap<Long, IncidenteLesion>();

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
        
        /*final BeanItemContainer<IncidenteLesion> ds = new BeanItemContainer<>(IncidenteLesion.class, incidenteLesionRepository.findAll());
        Grid grid = new Grid("Employees", ds);*/

        //container.addNestedContainerProperty("tipoLesion.descripcion");
        this.removeAllColumns();
        this.addColumn("tipoLesion").setHeaderCaption("Tipo de Lesi\u00F3n");
        this.addColumn("cantidad").setHeaderCaption("Cantidad");
        this.addColumn("actions").setHeaderCaption("Acciones");
        
        List<Long> ids = container.getItemIds();

        this.setEditDeleteListener(new EditDeleteButtonValueRenderer.EditDeleteButtonClickListener() {
            @Override
            public void onEdit(ClickableRenderer.RendererClickEvent rendererClickEvent) {
                IncidenteLesion incidenteLesion = container.getItem(rendererClickEvent.getItemId()).getBean();
                adicionaModificarBoton(incidenteLesion, false);
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
                        IncidenteLesion incidenteLesion = container.getItem(rendererClickEvent.getItemId()).getBean();
                        if (incidenteLesionAdicionados.containsKey(incidenteLesion.getId())) {
                            incidenteLesionAdicionados.remove(incidenteLesion.getId());
                        }
                        if (incidenteLesionModificados.containsKey(incidenteLesion.getId())) {
                            incidenteLesionModificados.remove(incidenteLesion.getId());
                        }
                        incidenteLesionEliminados.put(incidenteLesion.getId(), incidenteLesion);
                        container.removeItem(incidenteLesion.getId());
                        //segurosEliminados.put(seguro.getId(),seguro);
                        Notification notification = new Notification("Notificaci\u00F3n", "La lesi\u00F3n se eliminar\u00E1 luego de guardar los cambios", Notification.Type.TRAY_NOTIFICATION);
                        notification.setDelayMsec(3000);
                        notification.setIcon(FontAwesome.INFO_CIRCLE);
                        notification.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
                        notification.show(Page.getCurrent());
                    }
                });
            }
        });
        initExtraHeaderRow();
    }

    private void initExtraHeaderRow() {
        HeaderRow firstHeaderRow = this.prependHeaderRow();
        firstHeaderRow.join("tipoLesion", "cantidad");
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSizeFull();
        buttonLayout.setSpacing(true);
        buttonLayout.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
        firstHeaderRow.getCell("actions").setComponent(buttonLayout);
        Button adicionar = new Button("", (Button.ClickListener) event -> {
            this.adicionaModificarBoton(new IncidenteLesion(), true);
        }
        );
        adicionar.setCaption("Adicionar lesi\u00F3n al incidente");
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
        incidenteLesionAdicionados.clear();
        incidenteLesionModificados.clear();
        incidenteLesionEliminados.clear();
    }

    private void adicionaModificarBoton(IncidenteLesion incidenteLesion, Boolean adicionar) {
        IncidenteLesionForm form = new IncidenteLesionForm(incidenteLesion, tipoLesionRepository.findAll());
        mb = MessageBox.showCustomized(Icon.NONE, "Adicionar lesi\u00F3n", form, buttonId -> {
            if (buttonId == ButtonId.YES) {
                try {
                    form.getIncidenteLesionBeanFieldGroup().commit();
                    BeanItem<IncidenteLesion> item = form.getIncidenteLesionBeanFieldGroup().getItemDataSource();
                    IncidenteLesion obj = item.getBean();
                    if (obj.getCantidad() > 0)
                    {
                        if (adicionar) {
                            item.getBean().setId(id--);
                            incidenteLesionAdicionados.put(item.getBean().getId(), item.getBean());
                            container.addBean(item.getBean());
                        } else {
                            incidenteLesionAdicionados.remove(item.getBean().getId());
                            incidenteLesionModificados.put(item.getBean().getId(), item.getBean());
                            container.removeItem(item.getBean().getId());
                            container.addBean(item.getBean());
                        }
                        mb.close();
                    }
                    else
                        StaticMembers.showNotificationError("Error", "La cantidad es requerida");
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
        }, ButtonId.CANCEL, ButtonId.YES).setWidth("350px").setHeight("200px");
        mb.setModal(true);
        mb.setAutoClose(false);
        mb.getButton(ButtonId.YES).setCaption("Aceptar");
        mb.getButton(ButtonId.CANCEL).setCaption("Cancelar");
        mb.setWidth(400, Unit.PIXELS);
        mb.setHeight(220, Unit.PIXELS);
    }

    public static class Container extends BeanContainer<Long, IncidenteLesion> {

        public Container() {
            super(IncidenteLesion.class);
            setBeanIdResolver(incidenteLesion -> incidenteLesion.getId());
        }
    }
}
