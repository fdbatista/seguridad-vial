package com.earandap.vehiculos.ui.views.mantenimientoactividad;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.MantenimientoActividad;
import com.earandap.vehiculos.domain.PerfilRecurso;
import com.earandap.vehiculos.domain.Usuario;
import com.earandap.vehiculos.domain.nomenclador.Actividad;
import com.earandap.vehiculos.domain.nomenclador.ClaseVehiculo;
import com.earandap.vehiculos.domain.nomenclador.TipoMantenimiento;
import com.earandap.vehiculos.repository.ActividadRepository;
import com.earandap.vehiculos.repository.ClaseVehiculoRepository;
import com.earandap.vehiculos.repository.MantenimientoActividadRepository;
import com.earandap.vehiculos.repository.TipoMantenimientoRepository;
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
import org.springframework.security.core.Authentication;
import org.vaadin.spring.events.EventBus;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Set;

/**
 * Created by Angel Luis on 10/24/2015.
 */
@SpringComponent
@UIScope
public class MantenimientoActividadFormWindows extends Window {

    private BeanFieldGroup<MantenimientoActividad> mantenimientoActividadBeanFieldGroup;

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

    @PropertyId("tipoMantenimiento")
    private ComboBox tipoMantenimientoField;
    private BeanItemContainer<TipoMantenimiento> tipoMantenimientoBeanItemContainer;

    @PropertyId("tiempoReposicion")
    private TextField tiempoReposicionField;

    @PropertyId("kmReposicion")
    private TextField kmReposicionField;

    @PropertyId("inactivo")
    private CheckBox inactivoField;

    private boolean modificar;
    private MantenimientoActividad mantenimientoActividadAnterior;

    @Autowired
    private ClaseVehiculoRepository claseVehiculoRepository;

    @Autowired
    private ActividadRepository actividadRepository;

    @Autowired
    private TipoMantenimientoRepository tipoMantenimientoRepository;

    @Autowired
    private MantenimientoActividadRepository mantenimientoActividadRepository;

    @PostConstruct
    public void init() {
        setWidth(700, Unit.PIXELS);
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

        tabs.addTab(buildMantenimientoActividades(), "Mantenimiento");
        //tabs.addTab(buildInfoAdicional(),"Informaci\u00F3n Adicional");

        Component footer = buildFooter();
        content.addComponent(footer);
        content.setComponentAlignment(footer, Alignment.BOTTOM_CENTER);

        mantenimientoActividadBeanFieldGroup = new BeanFieldGroup<>(MantenimientoActividad.class);
        mantenimientoActividadBeanFieldGroup.bindMemberFields(this);

        this.setContent(content);
    }

    private Component buildMantenimientoActividades() {

        FormLayout infoGeneralForm = new FormLayout();
        infoGeneralForm.setSpacing(true);
        infoGeneralForm.setResponsive(true);

        //line 1
        HorizontalLayout l1 = new HorizontalLayout();
        l1.setSpacing(true);
        infoGeneralForm.addComponent(l1);

        tipoMantenimientoBeanItemContainer = new BeanItemContainer<>(TipoMantenimiento.class);
        tipoMantenimientoField = new ComboBox("Tipo Mantenimiento");
        tipoMantenimientoField.addStyleName(ValoTheme.COMBOBOX_TINY);
        tipoMantenimientoField.setWidth(18, Unit.EM);
        tipoMantenimientoField.setContainerDataSource(tipoMantenimientoBeanItemContainer);
        tipoMantenimientoField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        tipoMantenimientoField.setItemCaptionPropertyId("descripcion");
        tipoMantenimientoField.setNullSelectionAllowed(false);
        tipoMantenimientoField.setScrollToSelectedItem(true);
        tipoMantenimientoField.setRequired(true);
        tipoMantenimientoField.setRequiredError("El tipo de mantenimiento es requerido");
        l1.addComponent(tipoMantenimientoField);

        claseVehiculoBeanItemContainer = new BeanItemContainer<>(ClaseVehiculo.class);
        claseVehiculoField = new ComboBox("Clase de Veh\u00EDculo");
        claseVehiculoField.addStyleName(ValoTheme.COMBOBOX_TINY);
        claseVehiculoField.setWidth(18, Unit.EM);
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
        actividadField.setWidth(20, Unit.EM);
        actividadField.setContainerDataSource(actividadBeanItemContainer);
        actividadField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        actividadField.setItemCaptionPropertyId("descripcion");
        actividadField.setNullSelectionAllowed(false);
        actividadField.setScrollToSelectedItem(true);
        actividadField.setRequired(true);
        actividadField.setRequiredError("La actividad es requerida");
        l2.addComponent(actividadField);

        tiempoReposicionField = new TextField("Tiempo Reposici\u00F3n");
        tiempoReposicionField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        tiempoReposicionField.setWidth(9, Unit.EM);
        tiempoReposicionField.setValidationVisible(true);
        tiempoReposicionField.setNullRepresentation("");
        //Agregar Validación para solo numeros
        l2.addComponent(tiempoReposicionField);

        //line 3
        HorizontalLayout l3 = new HorizontalLayout();
        l3.setSpacing(true);
        infoGeneralForm.addComponent(l3);

        kmReposicionField = new TextField("Km Reposici\u00F3n");
        kmReposicionField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        kmReposicionField.setWidth(9, Unit.EM);
        kmReposicionField.setValidationVisible(true);
        kmReposicionField.setNullRepresentation("");
        //Agregar Validación para solo numeros
        l3.addComponent(kmReposicionField);

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
            MantenimientoActividad mantenimientoActividadNuevo = mantenimientoActividadBeanFieldGroup.getItemDataSource().getBean();
            for (PerfilRecurso perfilRecurso : permisos) {
                if (perfilRecurso.getId().getRecurso().getCodigo().equals(MantenimientoActividadView.VIEW_NAME)) {
                    if ((perfilRecurso.isCrear() && mantenimientoActividadNuevo.getId().getActividad() == null) || (perfilRecurso.isModificar() && mantenimientoActividadNuevo.getId().getActividad() != null)) {
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
                    /*
                    MantenimientoActividad.Id antes = new MantenimientoActividad.Id(
                            mantenimientoActividadBeanFieldGroup.getItemDataSource().getBean().getClaseVehiculo(),
                            mantenimientoActividadBeanFieldGroup.getItemDataSource().getBean().getActividad(),
                            mantenimientoActividadBeanFieldGroup.getItemDataSource().getBean().getTipoMantenimiento()
                    );

                    mantenimientoActividadBeanFieldGroup.commit();
                    MantenimientoActividad mantenimientoActividad = mantenimientoActividadBeanFieldGroup.getItemDataSource().getBean();
                    if (modificar) {
                        mantenimientoActividadRepository.save(mantenimientoActividad);
                    } else {
                        if (!mantenimientoActividadRepository.exists(mantenimientoActividad.getId())) {
                            mantenimientoActividadRepository.save(mantenimientoActividad);
                        } else {
                            throw new FieldGroup.CommitException(new Validator.InvalidValueException("Ya existe el Mantenimiento-Actividad"));
                        }
                    }
                    applicationEventBus.publish(MantenimientoActividadGrid.NUEVO_MANTENIMIENTOACTIVIDAD, this, mantenimientoActividad);
                    Notification notification = new Notification("Notificaci\u00F3n", "Mantenimiento-Actividad guardado con \u00E9xito", Notification.Type.TRAY_NOTIFICATION);
                    notification.setDelayMsec(3000);
                    notification.setIcon(FontAwesome.INFO_CIRCLE);
                    notification.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
                    notification.show(Page.getCurrent());
                    close();*/
                    mantenimientoActividadBeanFieldGroup.commit();
                    if (!mantenimientoActividadRepository.exists(mantenimientoActividadNuevo.getId())/* || alistamientoActividadNuevo.getId() == alistamientoActividadAnterior.getId()*/) {
                        mantenimientoActividadRepository.save(mantenimientoActividadNuevo);
                        applicationEventBus.publish(MantenimientoActividadGrid.NUEVO_MANTENIMIENTOACTIVIDAD, this, mantenimientoActividadNuevo);
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
                mantenimientoActividadRepository.save(mantenimientoActividadAnterior);
                applicationEventBus.publish(MantenimientoActividadGrid.NUEVO_MANTENIMIENTOACTIVIDAD, this, mantenimientoActividadAnterior);
            }
            close();
        });

        Responsive.makeResponsive(footer);
        return footer;
    }

    public Window show(MantenimientoActividad.Id ID) {

        claseVehiculoBeanItemContainer.removeAllItems();
        claseVehiculoBeanItemContainer.addAll(claseVehiculoRepository.findAll());

        actividadBeanItemContainer.removeAllItems();
        actividadBeanItemContainer.addAll(actividadRepository.findAll());

        tipoMantenimientoBeanItemContainer.removeAllItems();
        tipoMantenimientoBeanItemContainer.addAll(tipoMantenimientoRepository.findAll());

        mantenimientoActividadAnterior = mantenimientoActividadRepository.findOne(ID);
        if (mantenimientoActividadAnterior == null) {
            mantenimientoActividadAnterior = new MantenimientoActividad();
            modificar = false;
        } else {
            modificar = true;
        }
        mantenimientoActividadBeanFieldGroup.setItemDataSource(mantenimientoActividadAnterior);

        return this;
    }
}
