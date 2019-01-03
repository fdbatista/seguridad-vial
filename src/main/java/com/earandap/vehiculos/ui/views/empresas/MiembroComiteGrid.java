package com.earandap.vehiculos.ui.views.empresas;

import com.earandap.vehiculos.domain.MiembroComite;
import com.earandap.vehiculos.repository.PersonaRepository;
import com.earandap.vehiculos.repository.TerceroRepository;
import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.data.util.converter.StringToBooleanConverter;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ClickableRenderer;
import com.vaadin.ui.renderers.HtmlRenderer;
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
public class MiembroComiteGrid extends Grid {

    protected Container container;

    private MessageBox mb;

    private Long idResponsable;

    int id;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private TerceroRepository terceroRepository;

    private Map<Long, MiembroComite> miembroComiteModificados;

    private Map<Long, MiembroComite> miembroComiteAdicionados;

    private Map<Long, MiembroComite> miembroComiteEliminados;

    public Map<Long, MiembroComite> getMiembroComiteModificados() {
        return miembroComiteModificados;
    }

    public Long getIdResponsable() {
        return idResponsable;
    }

    public void setIdResponsable(Long idResponsable) {
        this.idResponsable = idResponsable;
    }

    public void setMiembroComiteModificados(Map<Long, MiembroComite> miembroComiteModificados) {
        this.miembroComiteModificados = miembroComiteModificados;
    }

    public Map<Long, MiembroComite> getMiembroComiteAdicionados() {
        return miembroComiteAdicionados;
    }

    public void setMiembroComiteAdicionados(Map<Long, MiembroComite> miembroComiteAdicionados) {
        this.miembroComiteAdicionados = miembroComiteAdicionados;
    }

    public Map<Long, MiembroComite> getMiembroComiteEliminados() {
        return miembroComiteEliminados;
    }

    public void setMiembroComiteEliminados(Map<Long, MiembroComite> miembroComiteEliminados) {
        this.miembroComiteEliminados = miembroComiteEliminados;
    }

    @PostConstruct
    public void init() {
        this.idResponsable = Long.valueOf("-1");
        this.container = new Container();
        container.addNestedContainerProperty("miembro.nombreCompleto");
        Responsive.makeResponsive(this);
        this.setSizeFull();
        this.setHeight("280px");
        this.setSelectionMode(SelectionMode.SINGLE);

        id = -1;

        miembroComiteAdicionados = new HashMap<Long, MiembroComite>();
        miembroComiteModificados = new HashMap<Long, MiembroComite>();
        miembroComiteEliminados = new HashMap<Long, MiembroComite>();

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

        this.removeAllColumns();
        this.addColumn("miembro.nombreCompleto").setHeaderCaption("Nombre").setWidth(250);
        this.addColumn("cargo").setHeaderCaption("Cargo");
        this.addColumn("esResponsable").setHeaderCaption("Resp.").setWidth(75);
        Grid.Column columna = this.getColumn("esResponsable");
        columna.setRenderer(new HtmlRenderer(),
                new StringToBooleanConverter(
                        FontAwesome.CHECK_SQUARE.getHtml(),
                        FontAwesome.SQUARE_O.getHtml()));
        this.addColumn("actions").setHeaderCaption("Acciones");

        this.setEditDeleteListener(new EditDeleteButtonValueRenderer.EditDeleteButtonClickListener() {
            @Override
            public void onEdit(ClickableRenderer.RendererClickEvent rendererClickEvent) {
                MiembroComite miembroComite = container.getItem(rendererClickEvent.getItemId()).getBean();
                adicionaModificarBoton(miembroComite, false);
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
                        MiembroComite miembroComite = container.getItem(rendererClickEvent.getItemId()).getBean();
                        Long itemId = miembroComite.getId();
                        if (miembroComiteAdicionados.containsKey(itemId)) {
                            miembroComiteAdicionados.remove(itemId);
                        }
                        if (miembroComiteModificados.containsKey(itemId)) {
                            miembroComiteModificados.remove(itemId);
                        }
                        miembroComiteEliminados.put(itemId, miembroComite);
                        container.removeItem(itemId);
                        Notification notification = new Notification("Notificaci\u00F3n", "El miembro se eliminar\u00E1 luego de guardar los cambios", Notification.Type.TRAY_NOTIFICATION);
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
        firstHeaderRow.join("miembro.nombreCompleto", "cargo", "esResponsable");
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSizeFull();
        buttonLayout.setSpacing(true);
        buttonLayout.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
        firstHeaderRow.getCell("actions").setComponent(buttonLayout);
        Button adicionar = new Button("", (Button.ClickListener) event -> {
            this.adicionaModificarBoton(new MiembroComite(), true);
        }
        );
        adicionar.setCaption("Adicionar miembro");
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
        miembroComiteAdicionados.clear();
        miembroComiteModificados.clear();
        miembroComiteEliminados.clear();
    }

    private void desmarcarResponsables(Map<Long, MiembroComite> lista, Long idIgnorar) {
        for (Map.Entry<Long, MiembroComite> miembroComiteMap : lista.entrySet()) {
            MiembroComite miembro = miembroComiteMap.getValue();
            if (miembro.getId() != idIgnorar) {
                miembro.setEsResponsable(false);
            }
        }
    }

    private void adicionaModificarBoton(MiembroComite miembroComite, Boolean adicionar) {
        MiembroComiteForm form = new MiembroComiteForm(miembroComite);
        form.setTerceroRepository(terceroRepository);
        mb = MessageBox.showCustomized(Icon.NONE, "Adicionar Miembro", form,
                buttonId -> {
                    if (buttonId == ButtonId.YES) {
                        try {
                            form.getMiembroComiteBeanFieldGroup().commit();
                            MiembroComite item = form.getMiembroComiteBeanFieldGroup().getItemDataSource().getBean();
                            if (item.isEsResponsable()) {
                                this.idResponsable = item.getId();
                            }
                            if (adicionar) {
                                item.setId(id--);
                                miembroComiteAdicionados.put(item.getId(), item);
                                container.addBean(item);
                            } else {
                                miembroComiteAdicionados.remove(item.getId());
                                miembroComiteModificados.put(item.getId(), item);
                                container.removeItem(item.getId());
                                container.addBean(item);
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
        mb.setWidth(450, Unit.PIXELS);
        mb.setHeight(300, Unit.PIXELS);
    }

    public static class Container extends BeanContainer<Long, MiembroComite> {

        public Container() {
            super(MiembroComite.class);
            setBeanIdResolver(miembroComite -> miembroComite.getId());
        }
    }
}
