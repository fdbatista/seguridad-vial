package com.earandap.vehiculos.ui.views.incidentes;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.AccidenteInvolucrado;
import com.earandap.vehiculos.domain.Persona;
import com.earandap.vehiculos.repository.AccidenteInvolucradoRepository;
import com.earandap.vehiculos.repository.PersonaRepository;
import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.data.util.converter.Converter;
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
import java.util.Locale;
import java.util.Map;

/**
 * Created by Angel Luis on 12/2/2015.
 */
@SpringComponent
@UIScope
public class AccidenteInvolucradoGrid extends Grid {

    protected Container container;

    private MessageBox mb;

    int id;

    @Autowired
    private AccidenteInvolucradoRepository accidenteInvolucradoRepository;

    @Autowired
    private PersonaRepository personaRepository;

    private Map<Long, AccidenteInvolucrado> accidenteInvolucradoModificados, accidenteInvolucradoAdicionados, accidenteInvolucradoEliminados;

    public Map<Long, AccidenteInvolucrado> getAccidenteInvolucradoModificados() {
        return accidenteInvolucradoModificados;
    }

    public void setAccidenteInvolucradoModificados(Map<Long, AccidenteInvolucrado> accidenteInvolucradoModificados) {
        this.accidenteInvolucradoModificados = accidenteInvolucradoModificados;
    }

    public Map<Long, AccidenteInvolucrado> getAccidenteInvolucradoAdicionados() {
        return accidenteInvolucradoAdicionados;
    }

    public void setAccidenteInvolucradoAdicionados(Map<Long, AccidenteInvolucrado> accidenteInvolucradoAdicionados) {
        this.accidenteInvolucradoAdicionados = accidenteInvolucradoAdicionados;
    }

    public Map<Long, AccidenteInvolucrado> getAccidenteInvolucradoEliminados() {
        return accidenteInvolucradoEliminados;
    }

    public void setAccidenteInvolucradoEliminados(Map<Long, AccidenteInvolucrado> accidenteInvolucradoEliminados) {
        this.accidenteInvolucradoEliminados = accidenteInvolucradoEliminados;
    }

    @PostConstruct
    public void init() {
        this.container = new Container();
        Responsive.makeResponsive(this);
        this.setSizeFull();
        this.setHeight("150px");
        this.setSelectionMode(SelectionMode.SINGLE);

        id = -1;

        accidenteInvolucradoAdicionados = new HashMap<>();
        accidenteInvolucradoModificados = new HashMap<>();
        accidenteInvolucradoEliminados = new HashMap<>();

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

        container.addNestedContainerProperty("involucrado.nombreCompleto");

        this.removeAllColumns();
        this.addColumn("involucrado.nombreCompleto").setHeaderCaption("Persona Involucrada");
        this.addColumn("responsable").setHeaderCaption("Responsable").setConverter(new Converter<String, Boolean>() {
            @Override
            public Boolean convertToModel(String value, Class<? extends Boolean> targetType, Locale locale) throws ConversionException {
                return null;
            }

            @Override
            public String convertToPresentation(Boolean value, Class<? extends String> targetType, Locale locale) throws ConversionException {
                return value ? "SI" : "NO";
            }

            @Override
            public Class<Boolean> getModelType() {
                return Boolean.class;
            }

            @Override
            public Class<String> getPresentationType() {
                return String.class;
            }
        });;
        this.addColumn("actions").setHeaderCaption("Acciones");

        this.setEditDeleteListener(new EditDeleteButtonValueRenderer.EditDeleteButtonClickListener() {
            @Override
            public void onEdit(ClickableRenderer.RendererClickEvent rendererClickEvent) {
                AccidenteInvolucrado accidenteInvolucrado = container.getItem(rendererClickEvent.getItemId()).getBean();
                adicionaModificarBoton(accidenteInvolucrado, false);
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
                        AccidenteInvolucrado accidenteInvolucrado = container.getItem(rendererClickEvent.getItemId()).getBean();
                        if (accidenteInvolucradoAdicionados.containsKey(accidenteInvolucrado.getId())) {
                            accidenteInvolucradoAdicionados.remove(accidenteInvolucrado.getId());
                        }
                        if (accidenteInvolucradoModificados.containsKey(accidenteInvolucrado.getId())) {
                            accidenteInvolucradoModificados.remove(accidenteInvolucrado.getId());
                        }
                        accidenteInvolucradoEliminados.put(accidenteInvolucrado.getId(), accidenteInvolucrado);
                        container.removeItem(accidenteInvolucrado.getId());
                        StaticMembers.showNotificationMessage("Notificaci\u00F3n", "El involucrado se eliminar\u00E1 luego de guardar los cambios");
                    }
                });
            }
        });
        initExtraHeaderRow();
    }

    private void initExtraHeaderRow() {
        HeaderRow firstHeaderRow = this.prependHeaderRow();
        firstHeaderRow.join("involucrado.nombreCompleto", "responsable");
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSizeFull();
        buttonLayout.setSpacing(true);
        buttonLayout.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
        firstHeaderRow.getCell("actions").setComponent(buttonLayout);
        Button adicionar = new Button("", (Button.ClickListener) event -> {
            this.adicionaModificarBoton(new AccidenteInvolucrado(), true);
        }
        );
        adicionar.setCaption("Adicionar involucrado al incidente");
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
        accidenteInvolucradoAdicionados.clear();
        accidenteInvolucradoModificados.clear();
        accidenteInvolucradoEliminados.clear();
    }

    private boolean personaAdicionada(AccidenteInvolucrado elem) {
        for (Long beanId : this.container.getItemIds()) {
            if (this.container.getItem(beanId).getBean().getInvolucrado().getId() == elem.getId()) {
                return true;
            }
        }
        return false;
    }

    private void adicionaModificarBoton(AccidenteInvolucrado accidenteInvolucrado, Boolean adicionar) {
        AccidenteInvolucradoForm form = new AccidenteInvolucradoForm(accidenteInvolucrado);
        form.setPersonaRepository(personaRepository);
        mb = MessageBox.showCustomized(Icon.NONE, "Adicionar Involucrado", form,
                buttonId -> {
                    if (buttonId == ButtonId.YES) {
                        try {
                            form.getAccidenteInvolucradoBeanFieldGroup().commit();
                            BeanItem<AccidenteInvolucrado> item = form.getAccidenteInvolucradoBeanFieldGroup().getItemDataSource();
                            
                            if (!personaAdicionada(item.getBean())) {
                                if (adicionar) {
                                    item.getBean().setId(id--);
                                    accidenteInvolucradoAdicionados.put(item.getBean().getId(), item.getBean());
                                    container.addBean(item.getBean());

                                } else {
                                    accidenteInvolucradoAdicionados.remove(item.getBean().getId());
                                    accidenteInvolucradoModificados.put(item.getBean().getId(), item.getBean());
                                    container.removeItem(item.getBean().getId());
                                    container.addBean(item.getBean());
                                }
                                mb.close();
                            }
                            else
                                StaticMembers.showNotificationError("Error", "La persona ya ha sido registrada");

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
                }, ButtonId.CANCEL, ButtonId.YES).setWidth("370px").setHeight("250px");
        mb.setModal(true);
        mb.setAutoClose(false);
        mb.getButton(ButtonId.YES).setCaption("Aceptar");
        mb.getButton(ButtonId.CANCEL).setCaption("Cancelar");
        mb.setWidth(370, Unit.PIXELS);
        mb.setHeight(250, Unit.PIXELS);
    }

    public static class Container extends BeanContainer<Long, AccidenteInvolucrado> {

        public Container() {
            super(AccidenteInvolucrado.class);
            setBeanIdResolver(accidenteInvolucrado -> accidenteInvolucrado.getId());
        }
    }
}
