package com.earandap.vehiculos.ui.views.usuario;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.Perfil;
import com.earandap.vehiculos.domain.PerfilRecurso;
import com.earandap.vehiculos.domain.Persona;
import com.earandap.vehiculos.domain.Usuario;
import com.earandap.vehiculos.repository.PerfilRepository;
import com.earandap.vehiculos.repository.PersonaRepository;
import com.earandap.vehiculos.repository.UsuarioRepository;
import com.earandap.vehiculos.ui.MainUI;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.vaadin.spring.events.EventBus;
import org.vaadin.suggestfield.SuggestField;
import org.vaadin.suggestfield.client.SuggestFieldSuggestion;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by Angel Luis on 11/6/2015.
 */
@SpringComponent
@UIScope
public class UsuarioFormWindows extends Window {

    private BeanFieldGroup<Usuario> usuarioBeanFieldGroup;

    @Autowired
    private EventBus.ApplicationEventBus applicationEventBus;

    private boolean modificar;

    //tab1
    private VerticalLayout content;

    @PropertyId("usuario")
    //@PropertyId("userName")
    private TextField usuarioField;

    //@PropertyId("contrasena")
    private PasswordField contrasenaField;

    private PasswordField repetircontrasenaField;

    @PropertyId("persona")
    private SuggestField personaField;

    @PropertyId("perfil")
    private ComboBox perfilField;
    private BeanItemContainer<Perfil> perfilBeanItemContainer;

    @PropertyId("inactivo")
    private CheckBox inactivoField;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        setWidth(450, Unit.PIXELS);
        setHeight(350, Unit.PIXELS);
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

        tabs.addTab(buildUsario(), "Usuario");
        //tabs.addTab(buildInfoAdicional(),"Informaci\u00F3n Adicional");

        Component footer = buildFooter();
        content.addComponent(footer);
        content.setComponentAlignment(footer, Alignment.BOTTOM_CENTER);

        usuarioBeanFieldGroup = new BeanFieldGroup<>(Usuario.class);
        usuarioBeanFieldGroup.bindMemberFields(this);

        this.setContent(content);
    }

    private Component buildUsario() {

        FormLayout infoGeneralForm = new FormLayout();
        infoGeneralForm.setResponsive(true);

        //line 1
        HorizontalLayout l1 = new HorizontalLayout();
        l1.setSpacing(true);
        infoGeneralForm.addComponent(l1);

        personaField = new SuggestField();
        personaField.setWidth(20, Unit.EM);
        personaField.setCaption("Persona");
        personaField.setRequired(true);
        personaField.setRequiredError("Debe seleccionar una persona");
        personaField.setNewItemsAllowed(false);
        personaField.setSuggestionHandler(s -> {
            List<Object> result = new ArrayList<>();
            List<Persona> personas = personaRepository.search(s);
            for (Persona persona : personas) {
                result.add(persona);
            }
            if (result.size() == 0) {
                Notification.show("No se encontraron personas con el criterio introducido", Notification.Type.TRAY_NOTIFICATION);
            }
            return result;
        });
        personaField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        personaField.setImmediate(true);
        personaField.setTokenMode(false);
        personaField.setSuggestionConverter(new PersonaSuggestionConverter());
        personaField.setPopupWidth(400);
        l1.addComponent(personaField);

        //line 2
        HorizontalLayout l2 = new HorizontalLayout();
        infoGeneralForm.addComponent(l2);
        l2.setSpacing(true);

        usuarioField = new TextField("Usuario");
        usuarioField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        usuarioField.setWidth(12, Unit.EM);
        usuarioField.setValidationVisible(true);
        usuarioField.setNullRepresentation("");
        usuarioField.setRequired(true);
        usuarioField.setMaxLength(20);
        usuarioField.setRequiredError("El usuario es requerido");
        l1.addComponent(usuarioField);

        contrasenaField = new PasswordField("Contrase\u00F1a");
        contrasenaField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        contrasenaField.setWidth(16, Unit.EM);
        contrasenaField.setValidationVisible(true);
        contrasenaField.setNullRepresentation("");
        contrasenaField.setMaxLength(20);
        l2.addComponent(contrasenaField);

        repetircontrasenaField = new PasswordField("Repetir Contrase\u00F1a");
        repetircontrasenaField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        repetircontrasenaField.setWidth(16, Unit.EM);
        repetircontrasenaField.setValidationVisible(true);
        repetircontrasenaField.setNullRepresentation("");
        repetircontrasenaField.setMaxLength(20);
        l2.addComponent(repetircontrasenaField);

        //line 3
        HorizontalLayout l3 = new HorizontalLayout();
        infoGeneralForm.addComponent(l3);
        l3.setSpacing(true);

        perfilBeanItemContainer = new BeanItemContainer<>(Perfil.class);
        perfilField = new ComboBox("Perfil");
        perfilField.addStyleName(ValoTheme.COMBOBOX_TINY);
        perfilField.setWidth(16, Unit.EM);
        perfilField.setContainerDataSource(perfilBeanItemContainer);
        perfilField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        perfilField.setItemCaptionPropertyId("nombre");
        perfilField.setNullSelectionAllowed(false);
        perfilField.setScrollToSelectedItem(true);
        perfilField.setRequired(true);
        perfilField.setRequiredError("El perfil es requerido");
        l3.addComponent(perfilField);

        inactivoField = new CheckBox("Inactivo");
        inactivoField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        inactivoField.setWidth(9, Unit.EM);
        inactivoField.setStyleName("fixed-checkbox");
        l3.addComponent(inactivoField);

        return infoGeneralForm;
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
            org.springframework.security.core.Authentication auth = ui.getSecurity().getAuthentication();
            Set<PerfilRecurso> permisos = ((Usuario) auth.getPrincipal()).getPerfil().getPerfilRecursos();
            boolean tienePermiso = false;
            Usuario usuarioAux = usuarioBeanFieldGroup.getItemDataSource().getBean();
            for (PerfilRecurso perfilRecurso : permisos) {
                if (perfilRecurso.getId().getRecurso().getCodigo().equals(UsuarioView.VIEW_NAME)) {
                    if ((perfilRecurso.isCrear() && usuarioAux.getId() == 0) || (perfilRecurso.isModificar() && usuarioAux.getId() != 0)) {
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
                    usuarioBeanFieldGroup.commit();
                    Usuario usuario = usuarioBeanFieldGroup.getItemDataSource().getBean();

                    boolean error = false;

                    if (modificar) {
                        List<Usuario> usuarios = usuarioRepository.findAllByIdNot(usuario.getId());
                        if (this.contienePersona(usuarios, usuario.getPersona()) || this.contieneUsuario(usuarios, usuario.getUsuario())) {
                            error = true;
                            StaticMembers.showNotificationError("Error", "Ya existe el usuario o la persona");
                            usuarioField.focus();
                        } else if (!contrasenaField.getValue().equals("")) {
                            if (!contrasenaField.getValue().equals(repetircontrasenaField.getValue())) {
                                error = true;
                                StaticMembers.showNotificationError("Error", "La nueva contrase\u00F1a no coincide");
                                repetircontrasenaField.focus();
                                repetircontrasenaField.selectAll();
                            } else {
                                usuario.setContrasena(passwordEncoder.encode(contrasenaField.getValue()));
                                usuario.setFechaCreacion(new Date());
                            }
                        }
                    } else {
                        if (usuarioRepository.findOneByUsuario(usuario.getUsuario()).isPresent() || (usuario.getPersona() != null && usuarioRepository.findOneByPersona(usuario.getPersona()).isPresent())) {
                            error = true;
                            StaticMembers.showNotificationError("Error", "Ya existe el usuario o la persona");
                            usuarioField.focus();
                        } else if (!contrasenaField.getValue().equals(repetircontrasenaField.getValue())) {
                            error = true;
                            StaticMembers.showNotificationError("Error", "La nueva contrase\u00F1a no coincide");
                            repetircontrasenaField.focus();
                            repetircontrasenaField.selectAll();
                        } else {
                            usuario.setContrasena(passwordEncoder.encode(contrasenaField.getValue()));
                            usuario.setFechaCreacion(new Date());
                        }
                    }
                    if (!error) {
                        usuario.setUsuario(usuarioField.getValue().trim().toLowerCase());
                        /*if (modificar) {
                            Long idUsuario = usuario.getId();
                            usuarioRepository.delete(idUsuario);
                            applicationEventBus.publish(UsuarioGrid.ELIMINADO_USUARIO, this, idUsuario);
                            usuario.setId(Long.valueOf("0"));
                        }*/
                        usuarioRepository.save(usuario);
                        applicationEventBus.publish(UsuarioGrid.NUEVO_USUARIO, this, usuario);
                        StaticMembers.showNotificationMessage("Notificaci\u00F3n", "Usuario guardado con \u00E9xito");
                        close();
                    }
                } catch (FieldGroup.CommitException e) {
                    if ((e instanceof FieldGroup.CommitException || e.getCause() instanceof Validator.EmptyValueException) || e.getCause() instanceof Validator.InvalidValueException) {
                        Collection<Validator.InvalidValueException> values = e.getInvalidFields().values();
                        if (!values.isEmpty()) {
                            Notification.show(values.iterator().next().getMessage(), e.getCause().getMessage(), Notification.Type.TRAY_NOTIFICATION);
                        } else if (e.getCause() instanceof Validator.InvalidValueException) {
                            Notification.show(e.getCause().getMessage(), Notification.Type.TRAY_NOTIFICATION);
                        } else {
                            Notification.show(e.getMessage(), Notification.Type.TRAY_NOTIFICATION);
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

    private boolean contienePersona(List<Usuario> usuarios, Persona persona) {
        for (Usuario usuario : usuarios) {
            if (usuario != null && usuario.getPersona() != null && persona != null && usuario.getPersona().getId() == persona.getId()) {
                return true;
            }
        }
        return false;
    }

    private boolean contieneUsuario(List<Usuario> usuarios, String usuario) {
        for (Usuario usuarioIt : usuarios) {
            if (usuarioIt.getUsuario().equals(usuario)) {
                return true;
            }
        }
        return false;
    }

    public Window show(long ID) {

        perfilBeanItemContainer.removeAllItems();
        perfilBeanItemContainer.addAll(perfilRepository.findAll());

        Usuario usuario = usuarioRepository.findOne(ID);
        if (usuario == null) {
            usuario = new Usuario();
            usuarioBeanFieldGroup.setItemDataSource(usuario);
            this.repetircontrasenaField.setValue("");
            //this.setHabilitarContrasenna(true);
            this.modificar = false;
        } else {
            usuarioBeanFieldGroup.setItemDataSource(usuario);
            this.modificar = true;
            this.repetircontrasenaField.setValue(this.contrasenaField.getValue());
            //this.setHabilitarContrasenna(false);
        }

        return this;
    }

    private void setHabilitarContrasenna(boolean value) {
        contrasenaField.setEnabled(value);
        repetircontrasenaField.setEnabled(value);
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
