package com.earandap.vehiculos.ui.views.notificacion;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.*;
import com.earandap.vehiculos.domain.nomenclador.Cargo;
import com.earandap.vehiculos.repository.NotificacionRepository;
import com.earandap.vehiculos.repository.PersonaRepository;
import com.earandap.vehiculos.repository.SuscriptorRepository;
import com.earandap.vehiculos.repository.TerceroNotificacionRepository;
import com.earandap.vehiculos.repository.TerceroRepository;
import com.earandap.vehiculos.repository.TipoNotificacionMensajeRepository;
import com.earandap.vehiculos.repository.TipoNotificacionRepository;
import com.earandap.vehiculos.repository.UsuarioRepository;
import com.earandap.vehiculos.ui.MainUI;
import com.earandap.vehiculos.ui.views.contrato.ContratoView;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.vaadin.spring.events.EventBus;
import org.vaadin.suggestfield.SuggestField;
import org.vaadin.suggestfield.client.SuggestFieldSuggestion;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by Angel Luis on 12/24/2015.
 */
@SpringComponent
@UIScope
public class NotificacionFormWindows extends Window {

    private BeanFieldGroup<Notificacion> notificacionBeanFieldGroup;

    @Autowired
    private NotificacionSuscriptoresGrid notificacionSuscriptoresGrid;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EventBus.ApplicationEventBus applicationEventBus;

    private SuggestField terceroField;

    private Button addSuscriptorButton, loadSuscriptoresButton;

    //tab1
    private VerticalLayout content;

    @PropertyId("tipoNotificacion")
    private ComboBox tipoNotificacionField;
    private BeanItemContainer<TipoNotificacion> tipoNotificacionBeanItemContainer;

    @PropertyId("fechaNotificacion")
    private DateField fechaNotificacionField;

    @PropertyId("encabezado")
    private TextField encabezadoField;

    @PropertyId("detalle")
    private TextArea detalleField;

    @PropertyId("fechaCreacion")
    private DateField fechaCreacionField;

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private TipoNotificacionRepository tipoNotificacionRepository;

    @Autowired
    private TipoNotificacionMensajeRepository tipoNotificacionMensajeRepository;

    @Autowired
    private TerceroRepository terceroRepository;

    @Autowired
    private TerceroNotificacionRepository terceroNotificacionRepository;

    @Autowired
    private SuscriptorRepository suscriptorRepository;

    private boolean modificar;

    //tab 2
    @PostConstruct
    public void init() {
        setWidth(500, Unit.PIXELS);
        setHeight(450, Unit.PIXELS);
        setModal(true);
        setCloseShortcut(ShortcutAction.KeyCode.ESCAPE, null);
        setResizable(false);
        setClosable(false);

        Responsive.makeResponsive(this);

        content = new VerticalLayout();
        content.setSizeFull();
        content.setMargin(true);

        TabSheet tabs = new TabSheet();
        content.addComponent(tabs);
        content.setExpandRatio(tabs, 1);

        tabs.addTab(buildNotificacion(), "Notificaci\u00F3n");

        Component footer = buildFooter();
        content.addComponent(footer);
        content.setComponentAlignment(footer, Alignment.BOTTOM_CENTER);

        notificacionBeanFieldGroup = new BeanFieldGroup<>(Notificacion.class);
        notificacionBeanFieldGroup.bindMemberFields(this);

        this.setContent(content);
    }

    private Component buildNotificacion() {
        FormLayout contratoForm = new FormLayout();
        contratoForm.setSpacing(true);
        contratoForm.setResponsive(true);

        //line 1
        HorizontalLayout l1 = new HorizontalLayout();
        l1.setSpacing(true);
        contratoForm.addComponent(l1);

        tipoNotificacionBeanItemContainer = new BeanItemContainer<>(TipoNotificacion.class);
        tipoNotificacionField = new ComboBox("Tipo Notificaci\u00F3n");
        tipoNotificacionField.addStyleName(ValoTheme.COMBOBOX_TINY);
        tipoNotificacionField.setWidth(22, Unit.EM);
        tipoNotificacionField.setContainerDataSource(tipoNotificacionBeanItemContainer);
        tipoNotificacionField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        tipoNotificacionField.setItemCaptionPropertyId("descripcion");
        tipoNotificacionField.setNullSelectionAllowed(false);
        tipoNotificacionField.setScrollToSelectedItem(true);
        tipoNotificacionField.setRequired(true);
        tipoNotificacionField.setRequiredError("El tipo de notificaci\u00f3n es requerido");
        l1.addComponent(tipoNotificacionField);

        fechaNotificacionField = new DateField("Fecha Notificaci\u00F3n");
        fechaNotificacionField.setStyleName(ValoTheme.DATEFIELD_TINY);
        fechaNotificacionField.setRangeStart(new Date());
        fechaNotificacionField.setWidth(12, Unit.EM);
        StaticMembers.setErrorRangoEspanhol(fechaNotificacionField);
        fechaNotificacionField.setRequired(true);
        fechaNotificacionField.setRequiredError("La fecha es requerida");
        l1.addComponent(fechaNotificacionField);

        //line 2
        HorizontalLayout l2 = new HorizontalLayout();
        l2.setSpacing(true);
        contratoForm.addComponent(l2);

        encabezadoField = new TextField("Encabezado");
        encabezadoField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        encabezadoField.setWidth(35, Unit.EM);
        encabezadoField.setNullRepresentation("");
        encabezadoField.setMaxLength(125);
        encabezadoField.setRequired(true);
        encabezadoField.setRequiredError("El encabezado es requerido");
        l2.addComponent(encabezadoField);

        //line 3
        HorizontalLayout l3 = new HorizontalLayout();
        l3.setSpacing(true);
        contratoForm.addComponent(l3);

        detalleField = new TextArea("Detalles");
        detalleField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        detalleField.setNullRepresentation("");
        detalleField.setColumns(35);
        detalleField.setRows(5);
        detalleField.setMaxLength(250);
        detalleField.setRequired(true);
        detalleField.setRequiredError("Los detalles son requeridos");
        l3.addComponent(detalleField);

        return contratoForm;
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
            Notificacion notificacion = notificacionBeanFieldGroup.getItemDataSource().getBean();
            
            for (PerfilRecurso perfilRecurso : permisos) {
                if (perfilRecurso.getId().getRecurso().getCodigo().equals(ContratoView.VIEW_NAME)) {
                    if ((perfilRecurso.isCrear() && notificacion.getId() == 0) || (perfilRecurso.isModificar() && notificacion.getId() != 0)) {
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
                    notificacionBeanFieldGroup.commit();
                    if (notificacion.getFechaCreacion() == null) {
                        notificacion.setProcesada(false);
                        notificacion.setFechaCreacion(new Date());
                    }
                    notificacionRepository.save(notificacion);

                    /*List<TerceroNotificacion> tercerosNotificables = new ArrayList<>();
                    List<Long> idsTercerosNotificados = new ArrayList<>();
                    TipoNotificacion tipoNotificacion = notificacion.getTipoNotificacion();
                    Set<Cargo> cargosAgregados = tipoNotificacion.getCargos();
                    Set<TipoNotificacionMensaje> tiposNotificacionMensajes = tipoNotificacion.getTiposNotificacionMensajes();

                    if (tipoNotificacion.isAdmin()) {
                        List<Usuario> admins = usuarioRepository.findAdministradores(Long.valueOf("1"));
                        for (Usuario admin : admins) {
                            Persona persona = admin.getPersona();
                            Long idPersona = persona.getId();
                            if (!idsTercerosNotificados.contains(idPersona)) {
                                TerceroNotificacion nuevo = new TerceroNotificacion();
                                nuevo.setActiva(true);
                                nuevo.setLeido(false);
                                nuevo.setNotificado(false);
                                nuevo.setNotificacion(notificacion);
                                nuevo.setTercero(persona);
                                tercerosNotificables.add(nuevo);
                                idsTercerosNotificados.add(idPersona);
                            }
                        }
                    }

                    for (Cargo cargo : cargosAgregados) {
                        String nombreCargo = cargo.getDescripcion().toLowerCase();

                        if (nombreCargo.equals("empleado")) {
                            List<Persona> personas = personaRepository.findByEmpleadoNotNull();
                            for (Persona persona : personas) {
                                Long idPersona = persona.getId();
                                if (!idsTercerosNotificados.contains(idPersona)) {
                                    TerceroNotificacion nuevo = new TerceroNotificacion();
                                    nuevo.setActiva(true);
                                    nuevo.setLeido(false);
                                    nuevo.setNotificado(false);
                                    nuevo.setNotificacion(notificacion);
                                    nuevo.setTercero(persona);
                                    tercerosNotificables.add(nuevo);
                                    idsTercerosNotificados.add(idPersona);
                                }
                            }
                        } else if (nombreCargo.equals("conductor")) {
                            List<Persona> personas = personaRepository.findByConductorIsNotNull();
                            for (Persona persona : personas) {
                                Long idPersona = persona.getId();
                                if (!idsTercerosNotificados.contains(idPersona)) {
                                    TerceroNotificacion nuevo = new TerceroNotificacion();
                                    nuevo.setActiva(true);
                                    nuevo.setLeido(false);
                                    nuevo.setNotificado(false);
                                    nuevo.setNotificacion(notificacion);
                                    nuevo.setTercero(persona);
                                    tercerosNotificables.add(nuevo);
                                    idsTercerosNotificados.add(idPersona);
                                }
                            }
                        }

                    }
                    terceroNotificacionRepository.save(tercerosNotificables);*/

                    applicationEventBus.publish(NotificacionGrid.NUEVA_NOTIFICACION, this, notificacion);
                    StaticMembers.showNotificationMessage("Notificaci\u00F3n", "Notificaci\u00F3n guardada con \u00E9xito");
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
        });

        footer.setExpandRatio(guardar, 1);
        Button cancelar = new Button("Cancelar");
        cancelar.setStyleName(ValoTheme.BUTTON_PRIMARY);
        footer.addComponent(cancelar);
        footer.setComponentAlignment(cancelar, Alignment.BOTTOM_RIGHT);
        cancelar.addClickListener(event -> close());

        Responsive.makeResponsive(footer);
        return footer;
    }

    public Window show(long ID) {

        tipoNotificacionBeanItemContainer.removeAllItems();
        tipoNotificacionBeanItemContainer.addAll(tipoNotificacionRepository.findAll());

        Notificacion notificacion = notificacionRepository.findOne(ID);
        if (notificacion == null) {
            notificacion = new Notificacion();
            modificar = false;
        } else {
            modificar = true;
        }
        notificacionBeanFieldGroup.setItemDataSource(notificacion);

        return this;
    }

    public class TerceroSuggestionConverter implements SuggestField.SuggestionConverter {

        @Override
        public SuggestFieldSuggestion toSuggestion(Object item) {
            if (item != null) {
                Tercero tercero = (Tercero) item;
                return new SuggestFieldSuggestion(String.valueOf(tercero.getId()), tercero.getNombreTercero(), tercero.getNombreTercero());
            }
            return null;
        }

        @Override
        public Object toItem(SuggestFieldSuggestion suggestion) {
            return terceroRepository.findOne(Long.valueOf(suggestion.getId()));
        }
    }

}
