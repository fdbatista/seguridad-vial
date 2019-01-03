package com.earandap.vehiculos.ui.views.tipomensajenotificacion;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.*;
import com.earandap.vehiculos.repository.TipoMensajeNotificacionRepository;
import com.earandap.vehiculos.repository.TipoMensajeRepository;
import com.earandap.vehiculos.repository.TipoNotificacionRepository;
import com.earandap.vehiculos.ui.MainUI;
import com.earandap.vehiculos.ui.views.alistamientoactividad.AlistamientoActividadGrid;
import com.earandap.vehiculos.ui.views.alistamientoactividad.AlistamientoActividadView;
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
 * Created by Angel Luis on 12/26/2015.
 */
@SpringComponent
@UIScope
public class TipoMenNotFormWindows extends Window {

    private BeanFieldGroup<TipoNotificacionMensaje> tipoNotificacionMensajeBeanFieldGroup;
    private TipoNotificacionMensaje notificacionMensaje;
    private TipoMensaje tipoMensajeAnterior;
    private TipoNotificacion tipoNotificacionAnterior;

    @Autowired
    private EventBus.ApplicationEventBus applicationEventBus;

    //tab1
    private VerticalLayout content;

    @PropertyId("tipoNotificacion")
    private ComboBox tipoNotificacionField;
    private BeanItemContainer<TipoNotificacion> tipoNotificacionBeanItemContainer;

    @PropertyId("tipoMensaje")
    private ComboBox tipoMensajeField;
    private BeanItemContainer<TipoMensaje> tipoMensajeBeanItemContainer;

    @PropertyId("inactivo")
    private CheckBox activoField;

    private boolean modificar;

    @Autowired
    private TipoMensajeRepository tipoMensajeRepository;

    @Autowired
    private TipoNotificacionRepository tipoNotificacionRepository;

    @Autowired
    private TipoMensajeNotificacionRepository tipoMensajeNotificacionRepository;

    @PostConstruct
    public void init() {
        setWidth(370, Unit.PIXELS);
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

        tabs.addTab(buildTipoMensajeNotificaciones(), "TipoNotificaciones-TipoMensajes");

        Component footer = buildFooter();
        content.addComponent(footer);
        content.setComponentAlignment(footer, Alignment.BOTTOM_CENTER);

        tipoNotificacionMensajeBeanFieldGroup = new BeanFieldGroup<>(TipoNotificacionMensaje.class);
        tipoNotificacionMensajeBeanFieldGroup.bindMemberFields(this);

        this.setContent(content);
    }

    private Component buildTipoMensajeNotificaciones() {

        FormLayout infoGeneralForm = new FormLayout();
        infoGeneralForm.setSpacing(true);
        infoGeneralForm.setResponsive(true);

        //line 1
        HorizontalLayout l1 = new HorizontalLayout();
        l1.setSpacing(true);
        infoGeneralForm.addComponent(l1);

        tipoMensajeBeanItemContainer = new BeanItemContainer<>(TipoMensaje.class);
        tipoMensajeField = new ComboBox("Tipo de Mensaje");
        tipoMensajeField.addStyleName(ValoTheme.COMBOBOX_TINY);
        tipoMensajeField.setWidth(18, Unit.EM);
        tipoMensajeField.setContainerDataSource(tipoMensajeBeanItemContainer);
        tipoMensajeField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        tipoMensajeField.setItemCaptionPropertyId("descripcion");
        tipoMensajeField.setNullSelectionAllowed(false);
        tipoMensajeField.setScrollToSelectedItem(true);
        tipoMensajeField.setRequired(true);
        tipoMensajeField.setRequiredError("El tipo de mensaje es requerido");
        l1.addComponent(tipoMensajeField);

        //line 2
        HorizontalLayout l2 = new HorizontalLayout();
        l2.setSpacing(true);
        infoGeneralForm.addComponent(l2);

        tipoNotificacionBeanItemContainer = new BeanItemContainer<>(TipoNotificacion.class);
        tipoNotificacionField = new ComboBox("Tipo de Notificaci\u00F3n");
        tipoNotificacionField.addStyleName(ValoTheme.COMBOBOX_TINY);
        tipoNotificacionField.setWidth(18, Unit.EM);
        tipoNotificacionField.setContainerDataSource(tipoNotificacionBeanItemContainer);
        tipoNotificacionField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        tipoNotificacionField.setItemCaptionPropertyId("descripcion");
        tipoNotificacionField.setNullSelectionAllowed(false);
        tipoNotificacionField.setScrollToSelectedItem(true);
        tipoNotificacionField.setRequired(true);
        tipoNotificacionField.setRequiredError("El tipo de notificaci\u00F3n es requerido");
        l2.addComponent(tipoNotificacionField);

        //line 3
        HorizontalLayout l3 = new HorizontalLayout();
        l3.setSpacing(true);
        infoGeneralForm.addComponent(l3);

        activoField = new CheckBox("Activo");
        activoField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        activoField.setWidth(9, Unit.EM);
        activoField.setStyleName("fixed-checkbox");
        l3.addComponent(activoField);

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
            TipoNotificacionMensaje tipoNotificacionMensajeAux = tipoNotificacionMensajeBeanFieldGroup.getItemDataSource().getBean();
            for (PerfilRecurso perfilRecurso : permisos) {
                if (perfilRecurso.getId().getRecurso().getCodigo().equals(AlistamientoActividadView.VIEW_NAME)) {
                    if ((perfilRecurso.isCrear() && tipoNotificacionMensajeAux.getId().getTipoMensaje() == null) || (perfilRecurso.isModificar() && tipoNotificacionMensajeAux.getId().getTipoMensaje() != null)) {
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
                    tipoNotificacionMensajeBeanFieldGroup.commit();
                    if (!tipoMensajeNotificacionRepository.exists(notificacionMensaje.getId())) {
                        notificacionMensaje.setTipoMensaje((TipoMensaje) tipoMensajeField.getValue());
                        notificacionMensaje.setTipoNotificacion((TipoNotificacion) tipoNotificacionField.getValue());
                        tipoMensajeNotificacionRepository.save(notificacionMensaje);
                        applicationEventBus.publish(TipoMenNotGrid.NUEVO_TIPOMENNOT, this, notificacionMensaje);
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
                notificacionMensaje.setTipoMensaje(tipoMensajeAnterior);
                notificacionMensaje.setTipoNotificacion(tipoNotificacionAnterior);
                tipoMensajeNotificacionRepository.save(notificacionMensaje);
                applicationEventBus.publish(TipoMenNotGrid.NUEVO_TIPOMENNOT, this, notificacionMensaje);
            }
            close();
        });

        Responsive.makeResponsive(footer);
        return footer;
    }

    public Window show(TipoNotificacionMensaje.Id ID) {

        tipoMensajeBeanItemContainer.removeAllItems();
        tipoMensajeBeanItemContainer.addAll(tipoMensajeRepository.findAll());

        tipoNotificacionBeanItemContainer.removeAllItems();
        tipoNotificacionBeanItemContainer.addAll(tipoNotificacionRepository.findAll());

        notificacionMensaje = tipoMensajeNotificacionRepository.findOne(ID);
        if (notificacionMensaje == null) {
            notificacionMensaje = new TipoNotificacionMensaje();
            modificar = false;
        } else {
            modificar = true;
            tipoNotificacionAnterior = notificacionMensaje.getTipoNotificacion();
            tipoMensajeAnterior = notificacionMensaje.getTipoMensaje();
        }
        tipoNotificacionMensajeBeanFieldGroup.setItemDataSource(notificacionMensaje);

        return this;
    }
    
}
