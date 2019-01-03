package com.earandap.vehiculos.ui.views.nomenclador;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.PerfilRecurso;
import com.earandap.vehiculos.domain.Persona;
import com.earandap.vehiculos.domain.Usuario;
import com.earandap.vehiculos.domain.nomenclador.*;
import com.earandap.vehiculos.repository.NomencladorCleanRepository;
import com.earandap.vehiculos.repository.NomencladorRepository;
import com.earandap.vehiculos.ui.MainUI;
import com.earandap.vehiculos.ui.components.HibernateUtil;
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
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by angel on 02/04/16.
 */
@SpringComponent
@UIScope
public class NomencladorFormWindows extends Window {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.driver-class-name}")
    private String dbDriver;

    private BeanFieldGroup<Nomenclador> nomencladorBeanFieldGroup;

    @Autowired
    private EventBus.ApplicationEventBus applicationEventBus;

    private boolean modificar;

    //tab1
    private VerticalLayout content;

    @PropertyId("codigo")
    private TextField codigoField;

    @PropertyId("descripcion")
    private TextField descripcionField;

    @PropertyId("tipo")
    private ComboBox tipoField;

    @Autowired
    NomencladorRepository nomencladorRepository;

    @Autowired
    NomencladorCleanRepository nomencladorCleanRepository;

    @PostConstruct
    public void init() {
        setWidth(300, Sizeable.Unit.PIXELS);
        setHeight(400, Sizeable.Unit.PIXELS);
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

        tabs.addTab(buildUsario(), "Nomenclador");

        Component footer = buildFooter();
        content.addComponent(footer);
        content.setComponentAlignment(footer, Alignment.BOTTOM_CENTER);

        nomencladorBeanFieldGroup = new BeanFieldGroup<>(Nomenclador.class);
        nomencladorBeanFieldGroup.bindMemberFields(this);

        this.setContent(content);
    }

    private Component buildUsario() {

        FormLayout infoGeneralForm = new FormLayout();
        infoGeneralForm.setResponsive(true);

        //line 1
        HorizontalLayout l1 = new HorizontalLayout();
        infoGeneralForm.addComponent(l1);
        l1.setSpacing(true);

        tipoField = new ComboBox("Tipo");
        tipoField.addStyleName(ValoTheme.COMBOBOX_TINY);
        tipoField.setWidth(15, Unit.EM);
        tipoField.setRequired(true);
        tipoField.setRequiredError("El tipo es requerido");
        tipoField.setNullSelectionAllowed(false);
        tipoField.setScrollToSelectedItem(true);
        l1.addComponent(tipoField);

        //line 2
        HorizontalLayout l2 = new HorizontalLayout();
        infoGeneralForm.addComponent(l2);
        l2.setSpacing(true);

        codigoField = new TextField("C\u00F3digo");
        codigoField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        codigoField.addStyleName("uppercase");
        codigoField.setWidth(15, Sizeable.Unit.EM);
        codigoField.setValidationVisible(true);
        codigoField.setNullRepresentation("");
        codigoField.setRequired(true);
        codigoField.setMaxLength(50);
        codigoField.setRequiredError("El c\u00F3digo es requerido");
        l2.addComponent(codigoField);

        //line 3
        HorizontalLayout l3 = new HorizontalLayout();
        infoGeneralForm.addComponent(l3);
        l3.setSpacing(true);

        descripcionField = new TextField("Descripci\u00F3n");
        descripcionField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        descripcionField.addStyleName("uppercase");
        descripcionField.setWidth(15, Sizeable.Unit.EM);
        descripcionField.setValidationVisible(true);
        descripcionField.setNullRepresentation("");
        descripcionField.setRequired(true);
        descripcionField.setMaxLength(500);
        descripcionField.setRequiredError("La descripci\u00F3n es requerida");
        l3.addComponent(descripcionField);

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
            Nomenclador nomencladorAux = nomencladorBeanFieldGroup.getItemDataSource().getBean();
            for (PerfilRecurso perfilRecurso : permisos) {
                if (perfilRecurso.getId().getRecurso().getCodigo().equals(UsuarioView.VIEW_NAME)) {
                    if ((perfilRecurso.isCrear() && nomencladorAux.getId() == 0) || (perfilRecurso.isModificar() && nomencladorAux.getId() != 0)) {
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
                    nomencladorBeanFieldGroup.commit();
                    Nomenclador nomenclador = nomencladorBeanFieldGroup.getItemDataSource().getBean();
                    nomencladorRepository.save(nomenclador);
                    if (StaticMembers.ejecutarUpdate(String.format("UPDATE nomenclador SET nomenclador_tipo = '%s' where nomenclador_id = %s;", nomenclador.getTipo(), nomenclador.getId()), dbUrl, dbUsername, dbPassword, dbDriver) > -1) {
                        applicationEventBus.publish(NomencladorGrid.NUEVO_NOMENCLADOR, this, nomenclador);
                        StaticMembers.showNotificationMessage("Notificaci\u00F3n", "Nomenclador guardado con éxito");
                    } else {
                        StaticMembers.showNotificationMessage("Error", "No se ha podido guardar el nomenclador");
                    }
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

        Nomenclador nomenclador = nomencladorRepository.findOne(ID);
        //tipoField.addItems(TipoNomenclador.getValues());
        tipoField.removeAllItems();
        tipoField.addItems(nomencladorRepository.getTiposDistinct());

        if (nomenclador == null) {
            nomenclador = new Nomenclador();
            this.modificar = false;
            tipoField.select(null);
            tipoField.setEnabled(true);
        } else {
            this.modificar = true;
            tipoField.select(nomenclador.getTipo());
            tipoField.setEnabled(false);
        }
        nomencladorBeanFieldGroup.setItemDataSource(nomenclador);

        return this;
    }

}
