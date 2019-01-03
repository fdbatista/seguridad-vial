package com.earandap.vehiculos.ui.views.alistamientoactividad;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.AlistamientoActividad;
import com.earandap.vehiculos.domain.PerfilRecurso;
import com.earandap.vehiculos.domain.Usuario;
import com.earandap.vehiculos.domain.nomenclador.Actividad;
import com.earandap.vehiculos.domain.nomenclador.ClaseVehiculo;
import com.earandap.vehiculos.repository.ActividadRepository;
import com.earandap.vehiculos.repository.AlistamientoActividadRepository;
import com.earandap.vehiculos.repository.ClaseVehiculoRepository;
import com.earandap.vehiculos.ui.MainUI;
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
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by Angel Luis on 11/2/2015.
 */
@SpringComponent
@UIScope
public class AlistamientoActividadFormWindows extends Window {

    private BeanFieldGroup<AlistamientoActividad> alistamientoActividadBeanFieldGroup;
    private Long idClaseAnterior, idActividadAnterior;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.driver-class-name}")
    private String dbDriver;

    @Autowired
    private EventBus.ApplicationEventBus applicationEventBus;

    //tab1
    private VerticalLayout content;

    @PropertyId("claseVehiculo")
    private ComboBox claseVehiculoField;
    private BeanItemContainer<ClaseVehiculo> claseVehiculoBeanItemContainer;

    @PropertyId("actividad")
    private ComboBox actividadField;
    private BeanItemContainer<Actividad> actividadBeanItemContainer;

    @PropertyId("inactivo")
    private CheckBox inactivoField;

    //private boolean modificar;
    @Autowired
    private ClaseVehiculoRepository claseVehiculoRepository;

    @Autowired
    private ActividadRepository actividadRepository;

    @Autowired
    private AlistamientoActividadRepository alistamientoActividadRepository;

    private AlistamientoActividad alistamientoActividadAnterior;
    private boolean modificar;

    @PostConstruct
    public void init() {
        setWidth(250, Unit.PIXELS);
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

        tabs.addTab(buildAlistamientoActividades(), "Alistamiento");

        Component footer = buildFooter();
        content.addComponent(footer);
        content.setComponentAlignment(footer, Alignment.BOTTOM_CENTER);

        alistamientoActividadBeanFieldGroup = new BeanFieldGroup<>(AlistamientoActividad.class);
        alistamientoActividadBeanFieldGroup.bindMemberFields(this);

        this.setContent(content);
    }

    private Component buildAlistamientoActividades() {

        FormLayout infoGeneralForm = new FormLayout();
        infoGeneralForm.setSpacing(true);
        infoGeneralForm.setResponsive(true);

        //line 1
        HorizontalLayout l1 = new HorizontalLayout();
        l1.setSpacing(true);
        infoGeneralForm.addComponent(l1);

        claseVehiculoBeanItemContainer = new BeanItemContainer<>(ClaseVehiculo.class);
        claseVehiculoField = new ComboBox("Clase de Veh\u00EDculo");
        claseVehiculoField.addStyleName(ValoTheme.COMBOBOX_TINY);
        claseVehiculoField.setWidth(15, Unit.EM);
        claseVehiculoField.setContainerDataSource(claseVehiculoBeanItemContainer);
        claseVehiculoField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        claseVehiculoField.setItemCaptionPropertyId("descripcion");
        claseVehiculoField.setNullSelectionAllowed(false);
        claseVehiculoField.setScrollToSelectedItem(true);
        claseVehiculoField.setRequired(true);
        claseVehiculoField.setRequiredError("El tipo veh\u00EDculo es requerido");
        l1.addComponent(claseVehiculoField);

        //line 2
        HorizontalLayout l2 = new HorizontalLayout();
        l2.setSpacing(true);
        infoGeneralForm.addComponent(l2);

        actividadBeanItemContainer = new BeanItemContainer<>(Actividad.class);

        actividadField = new ComboBox("Actividad");
        actividadField.addStyleName(ValoTheme.COMBOBOX_TINY);
        actividadField.setWidth(15, Unit.EM);
        actividadField.setContainerDataSource(actividadBeanItemContainer);
        actividadField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        actividadField.setItemCaptionPropertyId("descripcion");
        actividadField.setRequired(true);
        actividadField.setRequiredError("La actividad es requerida");
        actividadField.setNullSelectionAllowed(false);
        actividadField.setScrollToSelectedItem(true);

        l2.addComponent(actividadField);

        //line 3
        HorizontalLayout l3 = new HorizontalLayout();
        l3.setSpacing(true);
        infoGeneralForm.addComponent(l3);

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
            Authentication auth = ui.getSecurity().getAuthentication();
            Set<PerfilRecurso> permisos = ((Usuario) auth.getPrincipal()).getPerfil().getPerfilRecursos();
            boolean tienePermiso = false;
            AlistamientoActividad alistamientoActividadNuevo = alistamientoActividadBeanFieldGroup.getItemDataSource().getBean();
            for (PerfilRecurso perfilRecurso : permisos) {
                if (perfilRecurso.getId().getRecurso().getCodigo().equals(AlistamientoActividadView.VIEW_NAME)) {
                    if ((perfilRecurso.isCrear() && alistamientoActividadNuevo.getId().getActividad() == null) || (perfilRecurso.isModificar() && alistamientoActividadNuevo.getId().getActividad() != null)) {
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
                    alistamientoActividadBeanFieldGroup.commit();

                    if (!alistamientoActividadRepository.exists(alistamientoActividadNuevo.getId())/* || alistamientoActividadNuevo.getId() == alistamientoActividadAnterior.getId()*/) {
                        alistamientoActividadRepository.save(alistamientoActividadNuevo);
                        applicationEventBus.publish(AlistamientoActividadGrid.NUEVO_ALISTAMIENTOACTIVIDAD, this, alistamientoActividadNuevo);
                        StaticMembers.showNotificationMessage("Notificaci\u00F3n", "Elemento guardado con \u00E9xito");
                        close();
                    } else {
                        StaticMembers.showNotificationError("Error", "Ya existe un elemento con esos datos");
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
                        StaticMembers.showNotificationError("Error", e.getCause().getMessage());
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
            if (modificar) {
                alistamientoActividadRepository.save(alistamientoActividadAnterior);
                applicationEventBus.publish(AlistamientoActividadGrid.NUEVO_ALISTAMIENTOACTIVIDAD, this, alistamientoActividadAnterior);
            }
            close();
        });

        Responsive.makeResponsive(footer);
        return footer;
    }

    public Window show(AlistamientoActividad.Id ID/*, AlistamientoActividad obj*/) {

        //this.obj = obj;
        claseVehiculoBeanItemContainer.removeAllItems();
        claseVehiculoBeanItemContainer.addAll(claseVehiculoRepository.findAll());

        actividadBeanItemContainer.removeAllItems();
        actividadBeanItemContainer.addAll(actividadRepository.findAll());

        alistamientoActividadAnterior = alistamientoActividadRepository.findOne(ID);
        if (alistamientoActividadAnterior == null) {
            alistamientoActividadAnterior = new AlistamientoActividad();
            modificar = false;
        } else {
            modificar = true;
        }
        alistamientoActividadBeanFieldGroup.setItemDataSource(alistamientoActividadAnterior);

        return this;
    }

    /*private void deshabilitarCampos(boolean b) {
        actividadField.setEnabled(!b);
        claseVehiculoField.setEnabled(!b);
    }*/
}
