package com.earandap.vehiculos.ui.views.parametros;

import com.earandap.vehiculos.domain.Parametro;
import com.earandap.vehiculos.domain.PerfilRecurso;
import com.earandap.vehiculos.domain.Persona;
import com.earandap.vehiculos.domain.Usuario;
import com.earandap.vehiculos.repository.ParametrosRepository;
import com.earandap.vehiculos.ui.MainUI;
import com.earandap.vehiculos.ui.views.usuario.UsuarioView;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by angel on 02/04/16.
 */
@SpringComponent
@UIScope
public class ParametrosFormWindows extends Window {

    private BeanFieldGroup<Parametro> parametroBeanFieldGroup;

    @Autowired
    private EventBus.ApplicationEventBus applicationEventBus;

    private boolean modificar;

    //tab1
    private VerticalLayout content;

    @PropertyId("nombreParametro")
    private TextField nombreField;

    @PropertyId("valorParametro")
    private TextField valorField;

    //@PropertyId("tipo")
    //private TextField tipoField;
    //@PropertyId("empleado.cargo")
    //private ComboBox tipoField;
    //private BeanItemContainer<String> tipoContainer;
    @Autowired
    ParametrosRepository parametroRepository;

    @PostConstruct
    public void init() {
        setWidth(330, Sizeable.Unit.PIXELS);
        setHeight(300, Sizeable.Unit.PIXELS);
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

        tabs.addTab(buildUsario(), "Par\u00E1metros");

        Component footer = buildFooter();
        content.addComponent(footer);
        content.setComponentAlignment(footer, Alignment.BOTTOM_CENTER);

        parametroBeanFieldGroup = new BeanFieldGroup<>(Parametro.class);
        parametroBeanFieldGroup.bindMemberFields(this);

        this.setContent(content);
    }

    private Component buildUsario() {

        FormLayout infoGeneralForm = new FormLayout();
        infoGeneralForm.setResponsive(true);

        //line 1
        HorizontalLayout l1 = new HorizontalLayout();
        infoGeneralForm.addComponent(l1);
        //l1.setSpacing(true);

        nombreField = new TextField("Nombre");
        nombreField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        nombreField.setWidth(22, Sizeable.Unit.EM);
        nombreField.setValidationVisible(true);
        nombreField.setNullRepresentation("");
        nombreField.setRequired(true);
        nombreField.setMaxLength(50);
        nombreField.setRequiredError("El nombre es requerido");
        l1.addComponent(nombreField);

        //line 2
        HorizontalLayout l2 = new HorizontalLayout();
        infoGeneralForm.addComponent(l2);
        l2.setSpacing(true);

        valorField = new TextField("Valor");
        valorField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        valorField.setWidth(22, Sizeable.Unit.EM);
        valorField.setValidationVisible(true);
        valorField.setNullRepresentation("");
        valorField.setRequired(true);
        valorField.setMaxLength(500);
        valorField.setRequiredError("El valor es requerido");
        l2.addComponent(valorField);

        return infoGeneralForm;
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);
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
            Parametro parametroAux = parametroBeanFieldGroup.getItemDataSource().getBean();
            for (PerfilRecurso perfilRecurso : permisos) {
                if (perfilRecurso.getId().getRecurso().getCodigo().equals(UsuarioView.VIEW_NAME)) {
                    if ((perfilRecurso.isCrear() && parametroAux.getId() == 0) || (perfilRecurso.isModificar() && parametroAux.getId() != 0)) {
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
                    parametroBeanFieldGroup.commit();
                    Parametro parametro = parametroBeanFieldGroup.getItemDataSource().getBean();
                    
                    if (parametro.getNombreParametro().equals("")) {
                        Notification notification = new Notification("Error", "El nombre es requerido", Notification.Type.TRAY_NOTIFICATION);
                        notification.setPosition(Position.BOTTOM_RIGHT);
                        notification.setDelayMsec(3000);
                        notification.setIcon(FontAwesome.EXCLAMATION_CIRCLE);
                        notification.show(Page.getCurrent());
                    }
                    else if (parametro.getValorParametro().equals("")) {
                        Notification notification = new Notification("Error", "El valor es requerido", Notification.Type.TRAY_NOTIFICATION);
                        notification.setPosition(Position.BOTTOM_RIGHT);
                        notification.setDelayMsec(3000);
                        notification.setIcon(FontAwesome.EXCLAMATION_CIRCLE);
                        notification.show(Page.getCurrent());
                    }
                    
                    else {
                        if (!modificar) {
                            Parametro obj = new Parametro();
                            obj.setNombreParametro(parametro.getNombreParametro());
                            obj.setValorParametro(parametro.getValorParametro());
                            parametroRepository.save(obj);
                            applicationEventBus.publish(ParametrosGrid.NUEVO_PARAMETRO, this, obj);
                        } else {
                            parametroRepository.save(parametro);
                            applicationEventBus.publish(ParametrosGrid.NUEVO_PARAMETRO, this, parametro);
                        }
                        Notification notification = new Notification("Notificaci\u00F3n", "Par\u00E1metro guardado con \u00E9xito", Notification.Type.TRAY_NOTIFICATION);
                        notification.setPosition(Position.BOTTOM_RIGHT);
                        notification.setDelayMsec(3000);
                        notification.setIcon(FontAwesome.EXCLAMATION_CIRCLE);
                        notification.show(Page.getCurrent());
                        close();
                    }
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

    private boolean contienePersona(List<Usuario> usuarios, Persona persona) {
        for (Usuario usuario : usuarios) {
            if (usuario.getPersona() != null && usuario.getPersona().getId() == persona.getId()) {
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

        Parametro parametro = parametroRepository.findOne(ID);
        //tipoField.addItems(TipoParametros.getValues());
        if (parametro == null) {
            parametro = new Parametro();
            this.modificar = false;
            /*tipoField.select(null);
            tipoField.setEnabled(true);*/
        } else {
            this.modificar = true;
            /*tipoField.select(parametro.getTipo());
            tipoField.setEnabled(false);*/
        }
        parametroBeanFieldGroup.setItemDataSource(parametro);

        return this;
    }

}
