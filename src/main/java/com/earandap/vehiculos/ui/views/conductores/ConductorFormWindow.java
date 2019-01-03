package com.earandap.vehiculos.ui.views.conductores;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.*;
import com.earandap.vehiculos.domain.nomenclador.Sexo;
import com.earandap.vehiculos.domain.nomenclador.TipoDocumento;
import com.earandap.vehiculos.domain.nomenclador.TipoSangre;
import com.earandap.vehiculos.repository.*;
import com.earandap.vehiculos.ui.MainUI;
import com.earandap.vehiculos.ui.components.PersonaForm;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.vaadin.spring.events.EventBus;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.*;

/**
 * Created by Angel Luis on 10/4/2015.
 */
@SpringComponent
@UIScope
public class ConductorFormWindow extends Window implements PersonaForm.PersonaEvent {

    private BeanFieldGroup<Persona> conductorBeanFieldGroup;

    @Autowired
    private EventBus.ApplicationEventBus applicationEventBus;

    @Autowired
    private PersonaForm personaForm;

    //tab1
    private VerticalLayout content;

    @PropertyId("inactivo")
    private CheckBox inactivoField;

    @PropertyId("conductor.annoLicencia")
    private DateField annoLicenciaField;

    @PropertyId("conductor.numeroLicencia")
    private TextField numeroLicenciaField;

    @PropertyId("conductor.restricciones")
    private TextField restriccionesField;

    @PropertyId("conductor.organismoExpedidor")
    private TextField organismoExpedidorField;

    //tab2
    Label licenciaField;

    @Autowired
    private ConductorLicenciaGrid conductorLicenciaGrid;

    @Autowired
    private LicenciaRepository licenciaRepository;

    @Autowired
    private TipoDocumentoRepository tipoDocumentoRepository;

    @Autowired
    private SexoRepository sexoRepository;

    @Autowired
    private TipoSangreRepository tipoSangreRepository;

    @Autowired
    private MunicipioRepository municipioRepository;

    @Autowired
    private MunicipiosRepository municipiosRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private ConductorRepository conductorRepository;

    private boolean modificar;

    public void setModificar(boolean modificar) {
        this.modificar = modificar;
    }

    @PostConstruct

    public void init() {
        setWidth(700, Unit.PIXELS);
        setHeight(620, Unit.PIXELS);
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
        tabs.addTab(buildInfoAdicional(), "Categor\u00EDas de Licencias Autorizadas");

        com.vaadin.ui.Component footer = buildFooter();
        content.addComponent(footer);
        content.setComponentAlignment(footer, Alignment.BOTTOM_CENTER);

        conductorBeanFieldGroup = new BeanFieldGroup<>(Persona.class);
        conductorBeanFieldGroup.bindMemberFields(personaForm);
        conductorBeanFieldGroup.bindMemberFields(this);

        personaForm.updatePersona(this);

        this.setContent(content);
    }

    private com.vaadin.ui.Component buildInfoPersonal() {
        FormLayout infoPersonalForm = new FormLayout();
        infoPersonalForm.setSpacing(true);
        infoPersonalForm.setResponsive(true);

        infoPersonalForm.addComponent(personaForm);

        HorizontalLayout l1 = new HorizontalLayout();
        l1.setSpacing(true);
        infoPersonalForm.addComponent(l1);

        numeroLicenciaField = new TextField("N\u00FAmero de Licencia");
        numeroLicenciaField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        numeroLicenciaField.setWidth(12, Unit.EM);
        numeroLicenciaField.setValidationVisible(true);
        numeroLicenciaField.setNullRepresentation("");
        numeroLicenciaField.setMaxLength(30);
        l1.addComponent(numeroLicenciaField);

        annoLicenciaField = new DateField("Otorgamiento de Licencia");
        annoLicenciaField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        annoLicenciaField.setWidth(12, Unit.EM);
        annoLicenciaField.setRequired(true);
        annoLicenciaField.setRequiredError("La fecha de otorgamiento es requerida");

        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.getInstance().get(Calendar.SHORT_FORMAT), 11, 31);
        annoLicenciaField.setRangeEnd(calendar.getTime());
        StaticMembers.setErrorRangoEspanhol(annoLicenciaField);
        annoLicenciaField.setRequiredError("El a\u00F1o de licencia es requerido");
        annoLicenciaField.setValidationVisible(true);
        l1.addComponent(annoLicenciaField);

        inactivoField = new CheckBox("Inactivo");
        inactivoField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        inactivoField.setWidth(9, Unit.EM);
        inactivoField.setStyleName("fixed-checkbox");
        l1.addComponent(inactivoField);

        //line 2
        HorizontalLayout l2 = new HorizontalLayout();
        l2.setSpacing(true);
        infoPersonalForm.addComponent(l2);

        restriccionesField = new TextField("Restricciones");
        restriccionesField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        restriccionesField.setWidth(24, Unit.EM);
        restriccionesField.setValidationVisible(true);
        restriccionesField.setNullRepresentation("");
        restriccionesField.setMaxLength(100);
        l2.addComponent(restriccionesField);

        organismoExpedidorField = new TextField("Organismo Expedidor");
        organismoExpedidorField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        organismoExpedidorField.setWidth(24, Unit.EM);
        organismoExpedidorField.setValidationVisible(true);
        organismoExpedidorField.setNullRepresentation("");
        organismoExpedidorField.setMaxLength(100);
        l2.addComponent(organismoExpedidorField);

        return infoPersonalForm;
    }

    private com.vaadin.ui.Component buildInfoAdicional() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(new MarginInfo(true, false, true, false));
        layout.setSpacing(true);
        HorizontalLayout l1 = new HorizontalLayout();
        layout.addComponent(l1);
        l1.setSpacing(true);

        licenciaField = new Label("Licencias");
        licenciaField.addStyleName(ValoTheme.LABEL_BOLD);
        licenciaField.setWidth(12, Unit.EM);
        l1.addComponent(licenciaField);

        layout.addComponent(conductorLicenciaGrid);
        return layout;
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
            Persona persona = conductorBeanFieldGroup.getItemDataSource().getBean();
            for (PerfilRecurso perfilRecurso : permisos) {
                if (perfilRecurso.getId().getRecurso().getCodigo().equals(ConductorView.VIEW_NAME)) {

                    if ((perfilRecurso.isCrear() && persona.getConductor().getId() == 0) || (perfilRecurso.isModificar() && persona.getConductor().getId() != 0)) {
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
                    conductorBeanFieldGroup.commit();

                    if (!modificar) {
                        if (personaRepository.searchPersona(persona.getTipoDocumento(), persona.getDocumento()) != null) {
                            throw new FieldGroup.CommitException(new Validator.InvalidValueException("Ya existe el conductor"));
                        }
                    }

                    Conductor conductor = persona.getConductor();

                    if (persona.getId() == 0) {
                        persona.setConductor(null);
                        personaRepository.save(persona);
                    }
                    conductor.setPersona(persona);
                    conductorRepository.save(conductor);
                    persona.setConductor(conductor);
                    personaRepository.save(persona);

                    Map<Long, Licencia> licenciaAdicionados = conductorLicenciaGrid.getLicenciasAdicionados();
                    for (Map.Entry<Long, Licencia> licenciaEntry : licenciaAdicionados.entrySet()) {
                        Licencia licencia = licenciaEntry.getValue();
                        licencia.setConductor(persona.getConductor());
                        licenciaRepository.save(licencia);
                    }
                    Map<Long, Licencia> licenciaModficados = conductorLicenciaGrid.getLicenciasModificados();
                    for (Map.Entry<Long, Licencia> licenciaEntry : licenciaModficados.entrySet()) {
                        Licencia licencia = licenciaEntry.getValue();
                        licencia.setConductor(persona.getConductor());
                        licenciaRepository.save(licencia);
                    }
                    Map<Long, Licencia> licenciaEliminados = conductorLicenciaGrid.getLicenciasEliminados();
                    for (Map.Entry<Long, Licencia> licenciaEntry : licenciaEliminados.entrySet()) {
                        Licencia licencia = licenciaEntry.getValue();
                        licenciaRepository.delete(licencia);
                    }
                    conductorLicenciaGrid.restablecerListados();

                    applicationEventBus.publish(ConductorGrid.NUEVO_CONDUCTOR, this, persona);
                    StaticMembers.showNotificationMessage("Notificaci\u00F3n", "Conductor guardado con \u00E9xito");
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
        Conductor conductor = new Conductor();
        persona.setConductor(conductor);
        conductorBeanFieldGroup.setItemDataSource(persona);
        personaForm.habilitarDocumentos(true);
    }

    public Window show(long ID) {
        personaForm.updateTipoDocumentoContainer((List<TipoDocumento>) tipoDocumentoRepository.findAll());
        personaForm.updateSexoContainer((List<Sexo>) sexoRepository.findAll());
        personaForm.updateTipoSangreContainer((List<TipoSangre>) tipoSangreRepository.findAll());
        //personaForm.updatelugarExpedicionCedulaContainer((List<Municipio>) municipioRepository.findAll());
        //personaForm.updateMunicipioContainer((List<Municipio>) municipioRepository.findAll());
        //personaForm.updateMunicipiosContainer((List<Municipios>) municipiosRepository.findAll());
        Persona persona = personaRepository.findOne(ID);
        if (persona == null) {
            persona = new Persona();
            Conductor conductor = new Conductor();
            conductor.setPersona(persona);

            Calendar calendar = new GregorianCalendar();
            int a = Calendar.getInstance().get(Calendar.YEAR);
            calendar.set(Calendar.getInstance().get(Calendar.YEAR), 11, 31);
            conductor.setAnnoLicencia(calendar.getTime());
            persona.setConductor(conductor);
            this.setModificar(false);
            conductorBeanFieldGroup.setItemDataSource(persona);
            personaForm.habilitarDocumentos(true);
        } else {
            this.setModificar(true);
            conductorBeanFieldGroup.setItemDataSource(persona);
            personaForm.habilitarDocumentos(false);
        }
        personaForm.mostrarBotonLimpiar(false);
        conductorLicenciaGrid.container.removeAllItems();
        conductorLicenciaGrid.container.addAll(persona.getConductor().getLicencias());
        return this;
    }

    @Override
    public Persona buscar(TipoDocumento tipoDocumento, String documento) {
        Persona persona = personaRepository.searchPersona(tipoDocumento, documento);

        if (persona == null) {
            persona = new Persona();
            Conductor conductor = new Conductor();
            conductorBeanFieldGroup.setItemDataSource(persona);
            persona.setConductor(conductor);
        } else {
            if (persona.getConductor() == null) {
                Conductor conductor = new Conductor();
                persona.setConductor(conductor);
            }
            conductorBeanFieldGroup.setItemDataSource(persona);
            personaForm.habilitarDocumentos(false);
            personaForm.mostrarBotonLimpiar(true);
            this.setModificar(true);
        }
        return persona;
    }

}
