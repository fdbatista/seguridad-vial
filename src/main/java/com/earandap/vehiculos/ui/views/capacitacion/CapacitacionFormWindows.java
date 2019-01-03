package com.earandap.vehiculos.ui.views.capacitacion;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.*;
import com.earandap.vehiculos.domain.nomenclador.TipoActividad;
import com.earandap.vehiculos.repository.CapacitacionDocumentoRepository;
import com.earandap.vehiculos.repository.CapacitacionRepository;
import com.earandap.vehiculos.repository.ParametrosRepository;
import com.earandap.vehiculos.repository.PersonaCapacitacionRepository;
import com.earandap.vehiculos.repository.PersonaRepository;
import com.earandap.vehiculos.repository.TipoActividadRepository;
import com.earandap.vehiculos.ui.MainUI;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.DateRangeValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ClickableRenderer;
import com.vaadin.ui.themes.ValoTheme;
import de.steinwedel.messagebox.ButtonId;
import de.steinwedel.messagebox.Icon;
import de.steinwedel.messagebox.MessageBox;
import java.io.File;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.vaadin.gridutil.renderer.EditDeleteButtonValueRenderer;
import org.vaadin.spring.events.EventBus;
import org.vaadin.suggestfield.SuggestField;
import org.vaadin.suggestfield.client.SuggestFieldSuggestion;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by Angel Luis on 10/19/2015.
 */
@SpringComponent
@UIScope
public class CapacitacionFormWindows extends Window implements EditDeleteButtonValueRenderer.EditDeleteButtonClickListener {

    private String fileDir;
    private BeanFieldGroup<Capacitacion> capacitacionBeanFieldGroup;
    private Label capacitacionDocumentosField;
    TabSheet tabs;
    private boolean modificar = false;
    @Autowired
    private CapacitacionDocumentoRepository capacitacionDocumentoRepository;
    @Autowired
    private CapacitacionDocumentoGrid capacitacionDocumentosGrid;
    @Autowired
    private EventBus.ApplicationEventBus applicationEventBus;

    @Autowired
    private CapacitacionPersonaGrid capacitacionPersonaGrid;

    @Autowired
    private TipoActividadRepository tipoCapacitacionRepository;

    @Autowired
    private CapacitacionRepository capacitacionRepository;

    @Autowired
    private PersonaRepository personaRepository;
    
    @Autowired
    private ParametrosRepository parametrosRepository;

    @Autowired
    private PersonaCapacitacionRepository personaCapacitacionRepository;

    private VerticalLayout content;

    @PropertyId("tipoCapacitacion")
    private ComboBox tipoCapacitacionField;
    private BeanItemContainer<TipoActividad> tipoCapacitacionBeanItemContainer;

    @PropertyId("fechaCapacitacion")
    private DateField fechaCapacitacionField;

    @PropertyId("fechaVencimiento")
    private DateField fechaVencimientoField;

    @PropertyId("fechaProgramacion")
    private DateField fechaProgramacionField;

    @PropertyId("nombreCapacitacion")
    private TextField nombreCapacitacionField;

    @PropertyId("capacRealizada")
    private CheckBox capacRealizadaField;

    @PropertyId("puntajeMaximo")
    private TextField puntajeField;

    @PropertyId("observaciones")
    private TextArea observacionesField;

    @PropertyId("persona")
    private SuggestField personaField;

    @PropertyId("observacion")
    private TextField observacionespersonaField;

    @PropertyId("puntajeObtenido")
    private TextField puntajeObtenidoField;

    private Button addPersonaButton;
    private Button limpiarButton;

    private Map<Long, PersonaCapacitacion> personasAdicionadas = new LinkedHashMap<>();
    private Map<Long, PersonaCapacitacion> personasModificadas = new LinkedHashMap<>();
    private Map<Long, PersonaCapacitacion> personasEliminadas = new LinkedHashMap<>();

    public boolean isModificar() {
        return modificar;
    }

    public void setModificar(boolean modificar) {
        this.modificar = modificar;
    }

    @PostConstruct
    public void init() {
        setWidth(800, Unit.PIXELS);
        setHeight(450, Unit.PIXELS);
        setModal(true);
        setCloseShortcut(ShortcutAction.KeyCode.ESCAPE, null);
        setResizable(false);
        setClosable(false);
        fileDir = parametrosRepository.findOne(Long.valueOf("2")).getValorParametro();

        Responsive.makeResponsive(this);

        content = new VerticalLayout();
        content.setSizeFull();
        content.setMargin(true);

        tabs = new TabSheet();
        content.addComponent(tabs);
        content.setExpandRatio(tabs, 1);

        tabs.addTab(buildCapacitacion(), "Cronograma de Actividades");
        tabs.addTab(buildListaPersona(), "Participantes");
        tabs.addTab(buildDocumentos(), "Evidencias");

        Component footer = buildFooter();
        content.addComponent(footer);
        content.setComponentAlignment(footer, Alignment.BOTTOM_CENTER);

        capacitacionBeanFieldGroup = new BeanFieldGroup<>(Capacitacion.class);
        capacitacionBeanFieldGroup.bindMemberFields(this);

        this.capacitacionPersonaGrid.setEditDeleteListener(this);

        this.setContent(content);
    }

    private Component buildDocumentos() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(new MarginInfo(true, false, true, false));
        layout.setSpacing(true);

        //line 1
        HorizontalLayout l1 = new HorizontalLayout();
        layout.addComponent(l1);
        l1.setSpacing(true);

        capacitacionDocumentosField = new Label("Documentos");
        capacitacionDocumentosField.addStyleName(ValoTheme.LABEL_BOLD);
        capacitacionDocumentosField.setWidth(12, Unit.EM);
        l1.addComponent(capacitacionDocumentosField);

        layout.addComponent(capacitacionDocumentosGrid);

        return layout;
    }

    private Component buildCapacitacion() {

        FormLayout capacitacionForm = new FormLayout();
        capacitacionForm.setSpacing(true);
        capacitacionForm.setResponsive(true);

        //line 1
        HorizontalLayout l1 = new HorizontalLayout();
        l1.setSpacing(true);
        capacitacionForm.addComponent(l1);

        tipoCapacitacionBeanItemContainer = new BeanItemContainer<>(TipoActividad.class);

        tipoCapacitacionField = new ComboBox("Tipo");
        tipoCapacitacionField.addStyleName(ValoTheme.COMBOBOX_TINY);
        tipoCapacitacionField.setWidth(15, Unit.EM);
        tipoCapacitacionField.setContainerDataSource(tipoCapacitacionBeanItemContainer);
        tipoCapacitacionField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        tipoCapacitacionField.setItemCaptionPropertyId("descripcion");
        tipoCapacitacionField.setNullSelectionAllowed(false);
        tipoCapacitacionField.setScrollToSelectedItem(true);
        tipoCapacitacionField.setRequired(true);
        tipoCapacitacionField.setRequiredError("El tipo de actividad es requerido");
        l1.addComponent(tipoCapacitacionField);

        nombreCapacitacionField = new TextField("Nombre");
        nombreCapacitacionField.addStyleName(ValoTheme.TEXTAREA_TINY);
        nombreCapacitacionField.setNullRepresentation("");
        nombreCapacitacionField.setWidth(15, Unit.EM);
        nombreCapacitacionField.setRequired(true);
        nombreCapacitacionField.setRequiredError("El nombre es requerido");
        l1.addComponent(nombreCapacitacionField);

        puntajeField = new TextField("Puntaje m\u00E1ximo");
        puntajeField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        puntajeField.setWidth(5, Unit.EM);
        puntajeField.setNullRepresentation("");
        puntajeField.setMaxLength(5);
        l1.addComponent(puntajeField);

        capacRealizadaField = new CheckBox("Realizada");
        capacRealizadaField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        capacRealizadaField.setStyleName("fixed-checkbox");
        l1.addComponent(capacRealizadaField);

        //line 2
        HorizontalLayout l2 = new HorizontalLayout();
        l2.setSpacing(true);
        capacitacionForm.addComponent(l2);

        fechaCapacitacionField = new DateField("Fecha");
        fechaCapacitacionField.setStyleName(ValoTheme.DATEFIELD_TINY);
        fechaCapacitacionField.setRangeEnd(new Date());
        fechaCapacitacionField.setRequired(true);
        fechaCapacitacionField.setRequiredError("La fecha es requerida");
        StaticMembers.setErrorRangoEspanhol(fechaCapacitacionField);
        l2.addComponent(fechaCapacitacionField);

        fechaVencimientoField = new DateField("Vigencia");
        fechaVencimientoField.setStyleName(ValoTheme.DATEFIELD_TINY);
        fechaVencimientoField.setRangeStart(fechaCapacitacionField.getRangeEnd());
        StaticMembers.setErrorRangoEspanhol(fechaVencimientoField);
        l2.addComponent(fechaVencimientoField);

        fechaProgramacionField = new DateField("Fecha de Programaci\u00F3n");
        fechaProgramacionField.setStyleName(ValoTheme.DATEFIELD_TINY);
        StaticMembers.setErrorRangoEspanhol(fechaProgramacionField);
        l2.addComponent(fechaProgramacionField);

        //line 3
        HorizontalLayout l3 = new HorizontalLayout();
        l3.setSpacing(true);
        capacitacionForm.addComponent(l3);

        observacionesField = new TextArea("Observaciones");
        observacionesField.addStyleName(ValoTheme.TEXTAREA_TINY);
        observacionesField.setNullRepresentation("");
        observacionesField.setColumns(45);
        observacionesField.setRows(5);
        observacionesField.setMaxLength(500);
        l3.addComponent(observacionesField);

        return capacitacionForm;
    }

    private Component buildListaPersona() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(new MarginInfo(true, false, true, false));
        layout.setSpacing(true);

        HorizontalLayout l1 = new HorizontalLayout();
        layout.addComponent(l1);
        l1.setSpacing(true);

        personaField = new SuggestField();
        personaField.setWidth(30, Unit.EM);
        personaField.setCaption("Empleado");
        personaField.setNewItemsAllowed(false);
        personaField.setSuggestionHandler(s -> {
            List<Object> result = new ArrayList<>();
            List<Persona> personas = personaRepository.search(s);
            for (Persona persona : personas) {
                result.add(persona);
            }
            if (result.size() == 0) {
                Notification.show("No se encontraron empleados para el criterio introducido", Notification.Type.TRAY_NOTIFICATION);
            }

            return result;
        });
        personaField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        personaField.setImmediate(true);
        personaField.setTokenMode(false);
        personaField.setSuggestionConverter(new PersonaSuggestionConverter());
        personaField.setWidth("200px");
        personaField.setPopupWidth(400);
        l1.addComponent(personaField);

        observacionespersonaField = new TextField("Observaciones");
        observacionespersonaField.setStyleName(ValoTheme.TEXTFIELD_TINY);
        observacionespersonaField.setNullRepresentation("");
        observacionespersonaField.setWidth("350px");
        observacionesField.setMaxLength(200);
        l1.addComponent(observacionespersonaField);

        puntajeObtenidoField = new TextField("Puntaje");
        puntajeObtenidoField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        puntajeObtenidoField.setWidth(5, Unit.EM);
        puntajeObtenidoField.setNullRepresentation("");
        puntajeObtenidoField.setMaxLength(5);
        l1.addComponent(puntajeObtenidoField);

        addPersonaButton = new Button();
        addPersonaButton.setIcon(FontAwesome.PLUS);
        addPersonaButton.addStyleName(ValoTheme.BUTTON_SMALL);
        addPersonaButton.addStyleName("button-search-form");
        l1.addComponent(addPersonaButton);
        addPersonaButton.addClickListener(event -> {

            int puntajeObtenido;
            boolean conversionOk = false;
            String observaciones = observacionespersonaField.getValue();

            if (modificar) {
                Persona persona = (Persona) personaField.getValue();
                if (persona != null) {

                    try {
                        puntajeObtenido = Integer.parseInt(puntajeObtenidoField.getValue());
                        conversionOk = true;
                    } catch (Exception e) {
                        puntajeObtenido = 0;
                    }
                    if (conversionOk) {
                        if (puntajeObtenido <= Integer.valueOf(puntajeField.getValue())) {
                            PersonaCapacitacion personaCapacitacion = buscarPersonaCapacitacion(persona);
                            if (personaCapacitacion != null) {
                                capacitacionPersonaGrid.container.removeItem(personaCapacitacion.getId());
                                personaCapacitacion.setObservacion(observaciones);
                                personaCapacitacion.setPuntajeObtenido(puntajeObtenido);
                                capacitacionPersonaGrid.container.addBean(personaCapacitacion);
                                personasModificadas.put(personaCapacitacion.getPersona().getId(), personaCapacitacion);
                                limpiarBotones();
                                setFocoEnSuggestField(personaField);
                                this.botonesActivos(true, false);
                                this.modificar = false;
                            }
                        } else {
                            StaticMembers.showNotificationError("Error", "El puntaje obtenido no puede superar el m\u00e1ximo definido");
                            setFocoEnTextField(puntajeObtenidoField);
                        }
                    } else {
                        StaticMembers.showNotificationError("Error", "El puntaje obtenido debe ser correcto");
                        setFocoEnTextField(puntajeObtenidoField);
                    }
                } else {
                    StaticMembers.showNotificationWarning("Alerta", "Debe seleccionar una persona");
                    setFocoEnSuggestField(personaField);
                }

            } else {

                Persona persona = (Persona) personaField.getValue();
                if (persona != null) {
                    if (!existe(capacitacionPersonaGrid.container.getItemIds(), persona)) {
                        try {
                            puntajeObtenido = Integer.parseInt(puntajeObtenidoField.getValue());
                            conversionOk = true;
                        } catch (Exception e) {
                            puntajeObtenido = 0;
                        }
                        if (conversionOk) {
                            if (puntajeObtenido <= Integer.valueOf(puntajeField.getValue())) {
                                PersonaCapacitacion personaCapacitacion = new PersonaCapacitacion();
                                personaCapacitacion.setPersona(persona);
                                personaCapacitacion.setCapacitacion(new Capacitacion());
                                personaCapacitacion.setObservacion(observaciones);
                                personaCapacitacion.setPuntajeObtenido(puntajeObtenido);
                                capacitacionPersonaGrid.container.addBean(personaCapacitacion);
                                personasAdicionadas.put(persona.getId(), personaCapacitacion);
                                limpiarBotones();
                                setFocoEnSuggestField(personaField);
                                this.botonesActivos(true, false);
                                this.modificar = false;
                            } else {
                                StaticMembers.showNotificationError("Error", "El puntaje no puede superar el m\u00e1ximo definido");
                                setFocoEnTextField(puntajeObtenidoField);
                            }
                        } else {
                            StaticMembers.showNotificationError("Error", "El puntaje debe ser correcto");
                            setFocoEnTextField(puntajeObtenidoField);
                        }
                    } else {
                        StaticMembers.showNotificationError("Notificaci\u00F3n", "La persona ya est\u00E1 en la capacitaci\u00F3n");
                        setFocoEnSuggestField(personaField);
                    }
                } else {
                    StaticMembers.showNotificationError("Notificaci\u00F3n", "Debe seleccionar una persona");
                    setFocoEnSuggestField(personaField);
                }
            }
        });

        limpiarButton = new Button();
        limpiarButton.setIcon(FontAwesome.TIMES_CIRCLE_O);
        limpiarButton.addStyleName(ValoTheme.BUTTON_SMALL);
        limpiarButton.addStyleName("button-search-form");
        limpiarButton.addClickListener(event -> {
            this.botonesActivos(true, false);
            this.limpiarBotones();
            modificar = false;
        });
        l1.addComponent(limpiarButton);

        this.botonesActivos(true, false);
        layout.addComponent(capacitacionPersonaGrid);
        return layout;
    }

    private void setFocoEnTextField(TextField textField) {
        textField.focus();
        textField.selectAll();
    }

    private void setFocoEnSuggestField(SuggestField control) {
        control.focus();
    }

    private PersonaCapacitacion buscarPersonaCapacitacion(Persona persona) {
        for (PersonaCapacitacion.Id personaCapacitacionId : capacitacionPersonaGrid.container.getItemIds()) {
            if (personaCapacitacionId.getPersona().getId() == persona.getId()) {
                return capacitacionPersonaGrid.container.getItem(personaCapacitacionId).getBean();
            }
        }
        return null;
    }

    private boolean existe(List<PersonaCapacitacion.Id> itemIds, Persona persona) {
        for (PersonaCapacitacion.Id id : itemIds) {
            if (id.getPersona().getId() == persona.getId()) {
                return true;
            }
        }
        return false;
    }

    private void limpiarBotones() {
        personaField.setValue(null);
        observacionespersonaField.setValue("");
        puntajeObtenidoField.setValue("");
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);
        footer.setSpacing(true);

        Button guardar = new Button("Aceptar");
        guardar.addStyleName(ValoTheme.BUTTON_PRIMARY);
        footer.addComponent(guardar);
        footer.setComponentAlignment(guardar, Alignment.BOTTOM_RIGHT);
        guardar.addClickListener(event -> {
            MainUI ui = (MainUI) UI.getCurrent();
            Authentication auth = ui.getSecurity().getAuthentication();
            Set<PerfilRecurso> permisos = ((Usuario) auth.getPrincipal()).getPerfil().getPerfilRecursos();
            boolean tienePermiso = false;
            Capacitacion capacitacionAux = capacitacionBeanFieldGroup.getItemDataSource().getBean();
            for (PerfilRecurso perfilRecurso : permisos) {
                if (perfilRecurso.getId().getRecurso().getCodigo().equals(CapacitacionView.VIEW_NAME)) {
                    if ((perfilRecurso.isCrear() && capacitacionAux.getId() == 0) || (perfilRecurso.isModificar() && capacitacionAux.getId() != 0)) {
                        tienePermiso = true;
                    }
                }
            }
            if (!tienePermiso) {
                ui.getNavigator().navigateTo("accessDeniedView");
                close();
                return;
            } else {
                try {
                    capacitacionBeanFieldGroup.commit();
                    Capacitacion capacitacion = capacitacionBeanFieldGroup.getItemDataSource().getBean();
                    capacitacionRepository.save(capacitacion);
                    for (PersonaCapacitacion pc : personasAdicionadas.values()) {
                        pc.setCapacitacion(capacitacion);
                        personaCapacitacionRepository.save(pc);
                    }
                    for (PersonaCapacitacion pc : personasModificadas.values()) {
                        pc.setCapacitacion(capacitacion);
                        personaCapacitacionRepository.save(pc);
                    }
                    for (PersonaCapacitacion pc : personasEliminadas.values()) {
                        pc.setCapacitacion(capacitacion);
                        pc.setPersona(pc.getId().getPersona());
                        personasEliminadas.remove(pc.getId());
                        personaCapacitacionRepository.delete(pc.getId());
                    }

                    //Guardar documentos
                    Map<Long, CapacitacionDocumento> capacitacionDocumentoAdicionados = capacitacionDocumentosGrid.getCapacitacionDocumentoAdicionados();
                    for (Map.Entry<Long, CapacitacionDocumento> capacitacionDocumentoMap : capacitacionDocumentoAdicionados.entrySet()) {
                        CapacitacionDocumento capacitacionDocumento = capacitacionDocumentoMap.getValue();
                        capacitacionDocumento.setCapacitacion(capacitacion);
                        capacitacionDocumentoRepository.save(capacitacionDocumento);
                    }
                    Map<Long, CapacitacionDocumento> capacitacionDocumentoModficados = capacitacionDocumentosGrid.getCapacitacionDocumentoModificados();
                    for (Map.Entry<Long, CapacitacionDocumento> capacitacionDocumentoMap : capacitacionDocumentoModficados.entrySet()) {
                        CapacitacionDocumento capacitacionDocumento = capacitacionDocumentoMap.getValue();
                        capacitacionDocumento.setCapacitacion(capacitacion);
                        capacitacionDocumentoRepository.save(capacitacionDocumento);
                    }
                    Map<Long, CapacitacionDocumento> capacitacionDocumentoEliminados = capacitacionDocumentosGrid.getCapacitacionDocumentoEliminados();
                    for (Map.Entry<Long, CapacitacionDocumento> capacitacionDocumentoMap : capacitacionDocumentoEliminados.entrySet()) {
                        CapacitacionDocumento capacitacionDocumento = capacitacionDocumentoMap.getValue();
                        capacitacionDocumentoRepository.delete(capacitacionDocumento);
                        StaticMembers.eliminarFichero(new File(StaticMembers.construirRutaDocumentoCapacitacion(fileDir, capacitacionDocumento.getRutaDocumento(), capacitacion.getId())));
                    }
                    capacitacionDocumentosGrid.restablecerListados();

                    applicationEventBus.publish(CapacitacionGrid.NUEVA_CAPACITACION, this, capacitacion);
                    Notification notification = new Notification("Notificaci\u00F3n", "Actividad guardada con \u00E9xito", Notification.Type.TRAY_NOTIFICATION);
                    notification.setDelayMsec(3000);
                    notification.setIcon(FontAwesome.INFO_CIRCLE);
                    notification.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
                    notification.show(Page.getCurrent());
                    close();
                } catch (FieldGroup.CommitException e) {
                    if ((e instanceof FieldGroup.CommitException && e.getCause() instanceof Validator.InvalidValueException || e.getCause() instanceof Validator.EmptyValueException)) {
                        Collection<Validator.InvalidValueException> values = e.getInvalidFields().values();
                        if (!values.isEmpty()) {
                            Notification.show(values.iterator().next().getMessage(), e.getCause().getMessage(), Notification.Type.TRAY_NOTIFICATION);
                        } else if (e.getCause() instanceof Validator.InvalidValueException) {
                            Notification.show(e.getCause().getMessage(), Notification.Type.TRAY_NOTIFICATION);
                        }

                    } else {
                        Notification notification = new Notification("Ha ocurrido un error interno del sistema", e.getCause().getMessage(), Notification.Type.ERROR_MESSAGE);
                        notification.setPosition(Position.BOTTOM_RIGHT);
                        notification.setDelayMsec(3000);
                        notification.setIcon(FontAwesome.EXCLAMATION_CIRCLE);
                        notification.show(Page.getCurrent());
                    }
                }
            }
            this.limpiarMap();
            this.limpiarBotones();
            this.botonesActivos(true, false);
        });

        footer.setExpandRatio(guardar, 1);
        Button cancelar = new Button("Cancelar");
        cancelar.setStyleName(ValoTheme.BUTTON_PRIMARY);
        footer.addComponent(cancelar);
        footer.setComponentAlignment(cancelar, Alignment.BOTTOM_RIGHT);
        cancelar.addClickListener(event -> {
            this.limpiarMap();
            this.limpiarBotones();
            this.botonesActivos(true, false);
            close();
        });

        Responsive.makeResponsive(footer);
        return footer;
    }

    public void limpiarMap() {
        personasAdicionadas.clear();
        personasModificadas.clear();
        personasEliminadas.clear();
    }

    public void botonesActivos(boolean enableAdd, boolean visibleLimpiar) {
        personaField.setEnabled(enableAdd);
        limpiarButton.setVisible(visibleLimpiar);
    }

    public Window show(long ID) {

        tipoCapacitacionBeanItemContainer.removeAllItems();
        tipoCapacitacionBeanItemContainer.addAll((Collection<? extends TipoActividad>) tipoCapacitacionRepository.findAll());

        Capacitacion capacitacion = capacitacionRepository.findOne(ID);
        if (capacitacion == null) {
            capacitacion = new Capacitacion();
            this.setModificar(false);
            tabs.getTab(2).setVisible(false);
        } else {
            this.setModificar(true);
            tabs.getTab(2).setVisible(true);
        }

        capacitacionBeanFieldGroup.setItemDataSource(capacitacion);
        capacitacionPersonaGrid.container.removeAllItems();
        Set<PersonaCapacitacion> personas = capacitacion.getPersonas();
        capacitacionPersonaGrid.container.addAll(personas);
        modificar = false;

        capacitacionDocumentosGrid.idCapacitacion = capacitacion.getId();
        capacitacionDocumentosGrid.container.removeAllItems();
        capacitacionDocumentosGrid.container.addAll(capacitacion.getDocumentosCapacitacion());

        return this;
    }

    @Override
    public void onEdit(ClickableRenderer.RendererClickEvent rendererClickEvent) {
        PersonaCapacitacion personaCapacitacion = capacitacionPersonaGrid.container.getItem(rendererClickEvent.getItemId()).getBean();
        personaField.setValue(personaCapacitacion.getId().getPersona());
        personaField.setEnabled(false);
        observacionespersonaField.setValue(personaCapacitacion.getObservacion());
        puntajeObtenidoField.setValue(String.valueOf(personaCapacitacion.getPuntajeObtenido()));
        this.botonesActivos(false, true);
        this.modificar = true;
    }

    @Override
    public void onDelete(ClickableRenderer.RendererClickEvent rendererClickEvent) {
        MessageBox messageBox = MessageBox.showPlain(Icon.QUESTION,
                "Interrogante",
                "Est\u00E1 seguro que quiere eliminar?",
                ButtonId.YES,
                ButtonId.NO);
        messageBox.getButton(ButtonId.YES).addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent e) {
                PersonaCapacitacion personaCapacitacion = capacitacionPersonaGrid.container.getItem(rendererClickEvent.getItemId()).getBean();
                if (personasAdicionadas.containsKey(personaCapacitacion.getId().getPersona().getId())) {
                    personasAdicionadas.remove(personaCapacitacion.getId().getPersona().getId());
                }
                if (personasModificadas.containsKey(personaCapacitacion.getId().getPersona().getId())) {
                    personasModificadas.remove(personaCapacitacion.getId().getPersona().getId());
                }
                if (personaCapacitacion.getId().getCapacitacion().getId() != 0) {
                    personasEliminadas.put(personaCapacitacion.getPersona().getId(), personaCapacitacion);
                }
                capacitacionPersonaGrid.container.removeItem(personaCapacitacion.getId());
                //segurosEliminados.put(seguro.getId(),seguro);
                Notification notification = new Notification("Notificaci\u00F3n", "La persona se eliminar\u00E1 luego de guardar los cambios", Notification.Type.TRAY_NOTIFICATION);
                notification.setDelayMsec(3000);
                notification.setIcon(FontAwesome.INFO_CIRCLE);
                notification.setStyleName(ValoTheme.NOTIFICATION_WARNING);
                notification.show(Page.getCurrent());
            }
        });
        modificar = false;
        this.botonesActivos(true, false);
        this.limpiarBotones();
    }

    public class PersonaSuggestionConverter implements SuggestField.SuggestionConverter {

        @Override
        public SuggestFieldSuggestion toSuggestion(Object item) {
            if (item != null) {
                Persona persona = (Persona) item;
                return new SuggestFieldSuggestion(String.valueOf(persona.getId()), persona.getNombreCompleto(), persona.getNombreCompleto());
            }
            return null;
        }

        @Override
        public Object toItem(SuggestFieldSuggestion suggestion) {
            return personaRepository.findOne(Long.valueOf(suggestion.getId()));
        }
    }
}
