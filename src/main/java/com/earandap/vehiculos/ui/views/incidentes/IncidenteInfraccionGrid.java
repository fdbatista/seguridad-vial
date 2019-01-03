package com.earandap.vehiculos.ui.views.incidentes;

import com.earandap.vehiculos.domain.Infraccion;
import com.earandap.vehiculos.repository.InfraccionRepository;
import com.earandap.vehiculos.repository.PersonaRepository;
import com.earandap.vehiculos.repository.TipoSancionRepository;
import com.earandap.vehiculos.repository.VehiculoRepository;
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
 * Created by Angel Luis on 11/18/2015.
 */
@SpringComponent
@UIScope
public class IncidenteInfraccionGrid extends Grid {


    protected Container container;

    private MessageBox mb;

    int id;

    @Autowired
    private InfraccionRepository infraccionRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private TipoSancionRepository tipoSancionRepository;

    private Map<Long,Infraccion> infraccionModificados;

    private Map<Long,Infraccion> infraccionAdicionados;

    private Map<Long,Infraccion> infraccionEliminados;

    public Map<Long, Infraccion> getInfraccionModificados() {
        return infraccionModificados;
    }

    public void setInfraccionModificados(Map<Long, Infraccion> infraccionModificados) {
        this.infraccionModificados = infraccionModificados;
    }

    public Map<Long, Infraccion> getInfraccionAdicionados() {
        return infraccionAdicionados;
    }

    public void setInfraccionAdicionados(Map<Long, Infraccion> infraccionAdicionados) {
        this.infraccionAdicionados = infraccionAdicionados;
    }

    public Map<Long, Infraccion> getInfraccionEliminados() {
        return infraccionEliminados;
    }

    public void setInfraccionEliminados(Map<Long, Infraccion> infraccionEliminados) {
        this.infraccionEliminados = infraccionEliminados;
    }

    @PostConstruct
    public void init() {
        this.container = new Container();
        Responsive.makeResponsive(this);
        this.setSizeFull();
        this.setHeight("280px");
        this.setSelectionMode(SelectionMode.SINGLE);

        id =-1;

        infraccionAdicionados = new HashMap<Long,Infraccion>();
        infraccionModificados = new HashMap<Long,Infraccion>();
        infraccionEliminados = new HashMap<Long,Infraccion>();

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

        container.addNestedContainerProperty("conductor.nombreCompleto");
        container.addNestedContainerProperty("tipoSancion.descripcion");
        container.addNestedContainerProperty("vehiculo.placa");

        this.removeAllColumns();
        this.addColumn("vehiculo.placa").setWidth(100);
        this.addColumn("conductor.nombreCompleto").setHeaderCaption("T\u00E9cnico");
        this.addColumn("tipoSancion.descripcion").setHeaderCaption("Tipo Sanci\u00F3n").setWidth(180);
        this.addColumn("actions").setHeaderCaption("Acciones");

        this.setEditDeleteListener(new EditDeleteButtonValueRenderer.EditDeleteButtonClickListener() {
            @Override
            public void onEdit(ClickableRenderer.RendererClickEvent rendererClickEvent) {
                Infraccion infraccion = container.getItem(rendererClickEvent.getItemId()).getBean();
                adicionaModificarBoton(infraccion,false);
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
                        Infraccion infraccion = container.getItem(rendererClickEvent.getItemId()).getBean();
                        if (infraccionAdicionados.containsKey(infraccion.getId()))
                            infraccionAdicionados.remove(infraccion.getId());
                        if (infraccionModificados.containsKey(infraccion.getId()))
                            infraccionModificados.remove(infraccion.getId());
                        infraccionEliminados.put(infraccion.getId(),infraccion);
                        container.removeItem(infraccion.getId());
                        //segurosEliminados.put(seguro.getId(),seguro);
                        Notification notification = new Notification("Notificaci\u00F3n", "La infracci\u00F3n se eliminar\u00E1 luego de guardar los cambios", Notification.Type.TRAY_NOTIFICATION);
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
        firstHeaderRow.join("vehiculo.placa", "conductor.nombreCompleto", "tipoSancion.descripcion");
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSizeFull();
        buttonLayout.setSpacing(true);
        buttonLayout.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
        firstHeaderRow.getCell("actions").setComponent(buttonLayout);
        Button adicionar = new Button("", (Button.ClickListener) event -> {
            this.adicionaModificarBoton(new Infraccion(),true);
        }
        );
        adicionar.setCaption("Adicionar infracción al incidente");
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
        infraccionAdicionados.clear();
        infraccionModificados.clear();
        infraccionEliminados.clear();
    }

    private void adicionaModificarBoton(Infraccion infraccion,Boolean adicionar){
        InfraccionForm form = new InfraccionForm(infraccion);
        form.setPersonaRepository(personaRepository);
        form.setVehiculoRepository(vehiculoRepository);
        form.getTipoSancionBeanItemContainer().addAll(tipoSancionRepository.findAll());
        mb = MessageBox.showCustomized(Icon.NONE, "Adicionar Infracción", form
                , buttonId -> {
            if (buttonId == ButtonId.YES) {
                try {
                    form.getInfraccionBeanFieldGroup().commit();
                    BeanItem<Infraccion> item = form.getInfraccionBeanFieldGroup().getItemDataSource();
                    if(adicionar) {
                        item.getBean().setId(id--);
                        infraccionAdicionados.put(item.getBean().getId(), item.getBean());
                        container.addBean(item.getBean());
                    }
                    else{
                        infraccionAdicionados.remove(item.getBean().getId());
                        infraccionModificados.put(item.getBean().getId(), item.getBean());
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
        mb.setWidth(700, Unit.PIXELS);
        mb.setHeight(500, Unit.PIXELS);
    }


    public static class Container extends BeanContainer<Long, Infraccion> {
        public Container() {
            super(Infraccion.class);
            setBeanIdResolver(infraccion -> infraccion.getId());
        }
    }
}
