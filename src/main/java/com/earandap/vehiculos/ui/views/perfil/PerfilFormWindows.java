package com.earandap.vehiculos.ui.views.perfil;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.Perfil;
import com.earandap.vehiculos.domain.PerfilRecurso;
import com.earandap.vehiculos.domain.Usuario;
import com.earandap.vehiculos.repository.PerfilRecursoRepository;
import com.earandap.vehiculos.repository.PerfilRepository;
import com.earandap.vehiculos.ui.MainUI;
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
 * Created by Angel Luis on 11/11/2015.
 */
@SpringComponent
@UIScope
public class PerfilFormWindows extends Window {

    private BeanFieldGroup<Perfil> perfilBeanFieldGroup;

    @Autowired
    private EventBus.ApplicationEventBus applicationEventBus;

    private boolean modificar;

    //tab1
    private VerticalLayout content;

    @PropertyId("nombre")
    private TextField nombreField;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private PerfilRecursoRepository perfilRecursoRepository;

    @Autowired
    private PerfilRecursoPanel perfilRecursoPanel;

    @Autowired
    private PerfilRecursoTable perfilRecursoTable;

    @PostConstruct
    public void init() {
        setWidth(800, Unit.PIXELS);
        setHeight(500, Unit.PIXELS);
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

        tabs.addTab(buildPerfil(), "Perfil");
        //tabs.addTab(buildInfoAdicional(),"Informaci\u00F3n Adicional");

        Component footer = buildFooter();
        content.addComponent(footer);
        content.setComponentAlignment(footer, Alignment.BOTTOM_CENTER);

        perfilBeanFieldGroup = new BeanFieldGroup<>(Perfil.class);
        perfilBeanFieldGroup.bindMemberFields(this);

        this.setContent(content);
    }

    private Component buildPerfil() {

        FormLayout infoForm = new FormLayout();
        infoForm.setResponsive(true);

        //line 1
        HorizontalLayout l1 = new HorizontalLayout();
        l1.setSpacing(true);
        infoForm.addComponent(l1);

        Label nombrelabel = new Label("Nombre");
        l1.addComponent(nombrelabel);

        nombreField = new TextField();
        nombreField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        nombreField.setWidth(20, Unit.EM);
        nombreField.setValidationVisible(true);
        nombreField.setNullRepresentation("");
        nombreField.setRequired(true);
        nombreField.setMaxLength(20);
        nombreField.setRequiredError("El nombre es requerido");
        l1.addComponent(nombreField);
        infoForm.addComponent(perfilRecursoTable);

        return infoForm;
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
            Set<PerfilRecurso> permisosAux = ((Usuario) auth.getPrincipal()).getPerfil().getPerfilRecursos();
            boolean tienePermiso = false;
            Perfil perfilAux = perfilBeanFieldGroup.getItemDataSource().getBean();
            for (PerfilRecurso perfilRecurso : permisosAux) {
                if (perfilRecurso.getId().getRecurso().getCodigo().equals(PerfilView.VIEW_NAME)) {
                    if ((perfilRecurso.isCrear() && perfilAux.getId() == 0) || (perfilRecurso.isModificar() && perfilAux.getId() != 0)) {
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
                    perfilBeanFieldGroup.commit();
                    Perfil perfil = perfilBeanFieldGroup.getItemDataSource().getBean();
                    List<PerfilRecurso> permisos = perfilRecursoTable.obtenerPermisos();
                    if (!modificar) {
                        if (perfilRepository.findOneByNombre(perfil.getNombre()).isPresent()) {
                            throw new FieldGroup.CommitException(new Validator.InvalidValueException("Ya existe un perfil con ese nombre"));
                        }

                    } else {
                        List<Perfil> perfiles = perfilRepository.findAllByIdNot(perfil.getId());
                        if (this.contieneNombre(perfiles, perfil.getNombre())) {
                            throw new FieldGroup.CommitException(new Validator.InvalidValueException("Ya existe un perfil con ese nombre"));
                        }
                    }

                    /*if (modificar) {
                        Long idPerfil = perfil.getId();
                        perfilRepository.delete(idPerfil);
                        applicationEventBus.publish(PerfilGrid.ELIMINADO_PERFIL, this, idPerfil);
                        perfil.setId(Long.valueOf("0"));
                    }*/

                    perfilRepository.save(perfil);
                    for (PerfilRecurso perfilRecurso : permisos) {
                        perfilRecurso.setPerfil(perfil);
                        perfilRecursoRepository.save(perfilRecurso);
                    }

                    applicationEventBus.publish(PerfilGrid.NUEVO_PERFIL, this, perfil);
                    StaticMembers.showNotificationMessage("Notificaci\u00F3n", "Perfil guardado con \u00E9xito");
                    close();
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
                        StaticMembers.showNotificationError("Error", e.getCause().getMessage());
                    }
                } catch (Exception e) {
                    StaticMembers.showNotificationError("Error", e.getCause().getMessage());
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

    private boolean contieneNombre(List<Perfil> perfiles, String nombre) {
        for (Perfil perfil : perfiles) {
            if (perfil.getNombre().equals(nombre)) {
                return true;
            }
        }
        return false;
    }

    public Window show(long ID) {

        Perfil perfil = perfilRepository.findOne(ID);
        if (perfil == null) {
            perfil = new Perfil();
            this.modificar = false;
        } else {
            this.modificar = true;
        }
        perfilBeanFieldGroup.setItemDataSource(perfil);
        perfilRecursoTable.construirTabla(perfil);
        return this;
    }
}
