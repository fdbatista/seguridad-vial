package com.earandap.vehiculos.ui.views.empleados;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.Empleado;
import com.earandap.vehiculos.domain.PerfilRecurso;
import com.earandap.vehiculos.domain.Persona;
import com.earandap.vehiculos.domain.Usuario;
import com.earandap.vehiculos.domain.nomenclador.*;
import com.earandap.vehiculos.repository.*;
import com.earandap.vehiculos.ui.MainUI;
import com.earandap.vehiculos.ui.components.PersonaForm;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.vaadin.spring.events.EventBus;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by Angel Luis on 10/13/2015.
 */
@SpringComponent
@UIScope
public class EmpleadoFormWindows extends Window implements PersonaForm.PersonaEvent {

    private BeanFieldGroup<Persona> empleadoBeanFieldGroup;

    @Autowired
    private EventBus.ApplicationEventBus applicationEventBus;

    @Autowired
    private PersonaForm personaForm;

    //tab1
    private VerticalLayout content;

    @PropertyId("inactivo")
    private CheckBox inactivoField;

    @PropertyId("empleado.cargo")
    private ComboBox cargoField;
    private BeanItemContainer<Cargo> cargoContainer;

    @Autowired
    private TipoDocumentoRepository tipoDocumentoRepository;

    @Autowired
    private SexoRepository sexoRepository;

    @Autowired
    private TipoSangreRepository tipoSangreRepository;

    @Autowired
    private MunicipioRepository municipioRepository;

    @Autowired
    private CargoRepository cargoRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    private boolean modificar;

    public void setModificar(boolean modificar) {
        this.modificar = modificar;
    }

    @PostConstruct

    public void init() {
        setWidth(700, Unit.PIXELS);
        setHeight(550, Unit.PIXELS);
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

        tabs.addTab(buildInfoPersonal(), "Informaci\u00F3n Personal");
        //tabs.addTab(buildInfoAdicional(),"Informaci\u00F3n Adicional");

        com.vaadin.ui.Component footer = buildFooter();
        content.addComponent(footer);
        content.setComponentAlignment(footer, Alignment.BOTTOM_CENTER);

        empleadoBeanFieldGroup = new BeanFieldGroup<>(Persona.class);
        empleadoBeanFieldGroup.bindMemberFields(personaForm);
        empleadoBeanFieldGroup.bindMemberFields(this);

        personaForm.updatePersona(this);

        this.setContent(content);
    }

    private com.vaadin.ui.Component buildInfoPersonal() {
        //VerticalLayout layout = new VerticalLayout();
        FormLayout infoPersonalForm = new FormLayout();
        infoPersonalForm.setMargin(true);
        infoPersonalForm.setSpacing(true);
        infoPersonalForm.setResponsive(true);

        infoPersonalForm.addComponent(personaForm);

        HorizontalLayout l1 = new HorizontalLayout();
        l1.setSpacing(true);
        infoPersonalForm.addComponent(l1);

        cargoContainer = new BeanItemContainer<>(Cargo.class);
        cargoField = new ComboBox("Cargo");
        cargoField.addStyleName(ValoTheme.COMBOBOX_TINY);
        cargoField.setWidth(9, Unit.EM);
        cargoField.setContainerDataSource(cargoContainer);
        cargoField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        cargoField.setItemCaptionPropertyId("descripcion");
        cargoField.setRequired(true);
        cargoField.setRequiredError("El cargo es requerido");
        cargoField.setNullSelectionAllowed(false);
        cargoField.setScrollToSelectedItem(true);
        l1.addComponent(cargoField);

        inactivoField = new CheckBox("Inactivo");
        inactivoField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        inactivoField.setWidth(9, Unit.EM);
        inactivoField.setStyleName("fixed-checkbox");
        l1.addComponent(inactivoField);

        return infoPersonalForm;
    }

    private com.vaadin.ui.Component buildFooter() {
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
            Persona persona = empleadoBeanFieldGroup.getItemDataSource().getBean();
            for (PerfilRecurso perfilRecurso : permisos) {
                if (perfilRecurso.getId().getRecurso().getCodigo().equals(EmpleadoView.VIEW_NAME)) {
                    if ((perfilRecurso.isCrear() && persona.getEmpleado().getId() == 0) || (perfilRecurso.isModificar() && persona.getEmpleado().getId() != 0)) {
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
                    empleadoBeanFieldGroup.commit();

                    if (!modificar) {
                        if (personaRepository.searchPersona(persona.getTipoDocumento(), persona.getDocumento()) != null) {
                            throw new FieldGroup.CommitException(new Validator.InvalidValueException("Ya existe el empleado"));
                        }
                    }

                    Empleado empleado = persona.getEmpleado();

                    if (persona.getId() == 0) {
                        persona.setEmpleado(null);
                        personaRepository.save(persona);
                    }
                    empleado.setPersona(persona);
                    empleadoRepository.save(empleado);
                    persona.setEmpleado(empleado);
                    personaRepository.save(persona);

                    applicationEventBus.publish(EmpleadoGrid.NUEVO_EMPLEADO, this, persona);
                    StaticMembers.showNotificationMessage("Notificaci\u00F3n", "Empleado guardado con \u00E9xito");
                    close();
                } catch (FieldGroup.CommitException e) {
                    if ((e instanceof FieldGroup.CommitException && e.getCause() instanceof Validator.InvalidValueException || e.getCause() instanceof Validator.EmptyValueException)) {
                        Collection<Validator.InvalidValueException> values = e.getInvalidFields().values();
                        if (!values.isEmpty()) {
                            Notification.show("Datos Incorrectos", Notification.Type.TRAY_NOTIFICATION);
                        } else if (e.getCause() instanceof Validator.InvalidValueException) {
                            Notification.show(e.getCause().getMessage(), Notification.Type.TRAY_NOTIFICATION);
                        }
                    } else {
                        StaticMembers.showNotificationError("Ha ocurrido un error interno del sistema", e.getCause().getMessage());
                    }
                }
            }
        });

        footer.setExpandRatio(guardar, 1);
        Button cancelar = new Button("Cancelar");
        cancelar.setStyleName(ValoTheme.BUTTON_PRIMARY);
        footer.addComponent(cancelar);
        footer.setComponentAlignment(cancelar, Alignment.BOTTOM_RIGHT);
        cancelar.addClickListener(event -> {
            this.close();
        });

        Responsive.makeResponsive(footer);
        Responsive.makeResponsive(footer);
        return footer;
    }

    private void limpiarFieldGroup() {
        Persona persona = new Persona();
        Empleado empleado = new Empleado();
        persona.setEmpleado(empleado);
        empleadoBeanFieldGroup.setItemDataSource(persona);
    }

    public Window show(long ID) {
        personaForm.updateTipoDocumentoContainer((List<TipoDocumento>) tipoDocumentoRepository.findAll());
        personaForm.updateSexoContainer((List<Sexo>) sexoRepository.findAll());
        personaForm.updateTipoSangreContainer((List<TipoSangre>) tipoSangreRepository.findAll());
        personaForm.updatelugarExpedicionCedulaContainer((List<Municipio>) municipioRepository.findAll());
        personaForm.updateMunicipioContainer((List<Municipio>) municipioRepository.findAll());
        cargoContainer.addAll(cargoRepository.findAll());
        Persona persona = personaRepository.findOne(ID);
        if (persona == null) {
            persona = new Persona();
            Empleado empleado = new Empleado();
            empleado.setPersona(persona);
            persona.setEmpleado(empleado);
            this.setModificar(false);
            empleadoBeanFieldGroup.setItemDataSource(persona);
            personaForm.habilitarDocumentos(true);
        } else {
            this.setModificar(true);
            empleadoBeanFieldGroup.setItemDataSource(persona);
            personaForm.habilitarDocumentos(false);
        }

        personaForm.mostrarBotonLimpiar(false);
        return this;
    }

    @Override
    public Persona buscar(TipoDocumento tipoDocumento, String documento) {
        Persona persona = personaRepository.searchPersona(tipoDocumento, documento);

        if (persona == null) {
            persona = new Persona();
            Empleado empleado = new Empleado();
            empleadoBeanFieldGroup.setItemDataSource(persona);
            persona.setEmpleado(empleado);
        } else {
            if (persona.getEmpleado() == null) {
                Empleado empleado = new Empleado();
                persona.setEmpleado(empleado);
            }
            empleadoBeanFieldGroup.setItemDataSource(persona);
            personaForm.habilitarDocumentos(false);
            personaForm.mostrarBotonLimpiar(true);
            this.setModificar(true);
        }
        return persona;
    }
}
