package com.earandap.vehiculos.ui.views.persona;

import com.earandap.vehiculos.domain.PerfilRecurso;
import com.earandap.vehiculos.domain.Persona;
import com.earandap.vehiculos.domain.Usuario;
import com.earandap.vehiculos.domain.nomenclador.Municipio;
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
 * Created by angel on 23/07/16.
 */
@SpringComponent
@UIScope
public class PersonaFormWindows extends Window implements PersonaForm.PersonaEvent {

    private BeanFieldGroup<Persona> personaBeanFieldGroup;

    @Autowired
    private EventBus.ApplicationEventBus applicationEventBus;

    @Autowired
    private PersonaForm personaForm;

    //tab1
    private VerticalLayout content;

    @PropertyId("inactivo")
    private CheckBox inactivoField;


    @Autowired
    private TipoDocumentoRepository tipoDocumentoRepository;

    @Autowired
    private SexoRepository sexoRepository;

    @Autowired
    private TipoSangreRepository tipoSangreRepository;

    @Autowired
    private MunicipioRepository municipioRepository;

    @Autowired
    private PersonaRepository personaRepository;

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

        personaBeanFieldGroup = new BeanFieldGroup<>(Persona.class);
        personaBeanFieldGroup.bindMemberFields(personaForm);
        personaBeanFieldGroup.bindMemberFields(this);

        personaForm.updatePersona(this);
        personaForm.esconderBotonesBusqueda(true);

        this.setContent(content);
    }


    private com.vaadin.ui.Component buildInfoPersonal() {
        FormLayout infoPersonalForm = new FormLayout();
        infoPersonalForm.setMargin(true);
        infoPersonalForm.setSpacing(true);
        infoPersonalForm.setResponsive(true);


        infoPersonalForm.addComponent(personaForm);

        HorizontalLayout l1 = new HorizontalLayout();
        l1.setSpacing(true);
        infoPersonalForm.addComponent(l1);

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
            Set<PerfilRecurso> permisos = ((Usuario)auth.getPrincipal()).getPerfil().getPerfilRecursos();
            boolean tienePermiso = false;
            Persona personaAux = personaBeanFieldGroup.getItemDataSource().getBean();
            for (PerfilRecurso perfilRecurso: permisos){
                if (perfilRecurso.getId().getRecurso().getCodigo().equals(PersonaView.VIEW_NAME)){
                    if ((perfilRecurso.isCrear() && personaAux.getId() == 0)||(perfilRecurso.isModificar() && personaAux.getId() != 0) )
                        tienePermiso = true;
                }
            }
            if (!tienePermiso) {
                ui.getNavigator().navigateTo("accessDeniedView");
                close();
                return;
            }else {
                try {
                    personaBeanFieldGroup.commit();
                    Persona persona = personaBeanFieldGroup.getItemDataSource().getBean();
                    if (!modificar)
                        if (personaRepository.searchPersona(persona.getTipoDocumento(), persona.getDocumento()) != null) {
                            throw new FieldGroup.CommitException(new Validator.InvalidValueException("Ya existe la persona"));
                        }

                    personaRepository.save(persona);
                    applicationEventBus.publish(PersonaGrid.NUEVA_PERSONA, this, persona);
                    Notification notification = new Notification("Notificaci\u00F3n", "Persona guardada con \u00E9xito", Notification.Type.TRAY_NOTIFICATION);
                    notification.setDelayMsec(3000);
                    notification.setIcon(FontAwesome.INFO_CIRCLE);
                    notification.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
                    notification.show(Page.getCurrent());
                    close();
                } catch (FieldGroup.CommitException e) {
                    if ((e instanceof FieldGroup.CommitException && e.getCause() instanceof Validator
                            .InvalidValueException || e.getCause() instanceof Validator
                            .EmptyValueException)) {
                        Collection<Validator.InvalidValueException> values = e.getInvalidFields().values();
                        if (!values.isEmpty()) {
                            Notification.show("Datos Incorrectos", Notification.Type.TRAY_NOTIFICATION);
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
        cancelar.addClickListener(event -> {
            this.close();
        });

        Responsive.makeResponsive(footer);
        Responsive.makeResponsive(footer);
        return footer;
    }

    private void limpiarFieldGroup() {
        Persona persona = new Persona();
        personaBeanFieldGroup.setItemDataSource(persona);
    }

    public Window show(long ID) {
        personaForm.updateTipoDocumentoContainer((List<TipoDocumento>) tipoDocumentoRepository.findAll());
        personaForm.updateSexoContainer((List<Sexo>) sexoRepository.findAll());
        personaForm.updateTipoSangreContainer((List<TipoSangre>) tipoSangreRepository.findAll());
        personaForm.updatelugarExpedicionCedulaContainer((List<Municipio>) municipioRepository.findAll());
        personaForm.updateMunicipioContainer((List<Municipio>) municipioRepository.findAll());
        Persona persona = personaRepository.findOne(ID);
        if (persona == null) {
            persona = new Persona();
            this.setModificar(false);
            personaBeanFieldGroup.setItemDataSource(persona);
            personaForm.habilitarDocumentos(true);
        } else {
            this.setModificar(true);
            personaBeanFieldGroup.setItemDataSource(persona);
            personaForm.habilitarDocumentos(false);
        }

        personaForm.esconderBotonesBusqueda(false);
        return this;
    }

    @Override
    public Persona buscar(TipoDocumento tipoDocumento, String documento) {
        Persona persona = personaRepository.searchPersona(tipoDocumento, documento);

        if (persona == null) {
            persona = new Persona();
            personaBeanFieldGroup.setItemDataSource(persona);
        }else {
            personaBeanFieldGroup.setItemDataSource(persona);
            personaForm.habilitarDocumentos(false);
            personaForm.mostrarBotonLimpiar(true);
            this.setModificar(true);
        }
        return persona;
    }
}
