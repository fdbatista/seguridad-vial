package com.earandap.vehiculos.ui.views.alistamiento;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.*;
import com.earandap.vehiculos.repository.AccionAlistamientoRepository;
import com.earandap.vehiculos.repository.AlistamientoDetalleRepository;
import com.earandap.vehiculos.repository.TerceroRepository;
import com.earandap.vehiculos.repository.VehiculoRepository;
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
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.vaadin.spring.events.EventBus;
import org.vaadin.suggestfield.SuggestField;
import org.vaadin.suggestfield.client.SuggestFieldSuggestion;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by Angel Luis on 11/2/2015.
 */
@SpringComponent
@UIScope
public class AlistamientoFormWindow extends Window {

    private BeanFieldGroup<AccionAlistamiento> accionAlistamientoBeanFieldGroup;

    @Autowired
    private EventBus.ApplicationEventBus applicationEventBus;

    //tab1
    private VerticalLayout content;

    @PropertyId("tecnico")
    private SuggestField tecnicoField;

    @PropertyId("vehiculo")
    private SuggestField vehiculoField;

    @PropertyId("operativo")
    private CheckBox operativoField;

    @PropertyId("fecha")
    private DateField fechaField;

    @PropertyId("detalle")
    private TextArea detalleField;

    @PropertyId("kmActual")
    private TextField kmActualField;

    //tab 2
    private Label detallesField;
    private Button nuevoDetalleButton;

    @Autowired
    private AlistamientoDetalleGrid alistamientoDetalleGrid;

    private boolean modificar;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private TerceroRepository terceroRepository;

    @Autowired
    private AccionAlistamientoRepository accionAlistamientoRepository;

    @Autowired
    private AlistamientoDetalleRepository alistamientoDetalleRepository;

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

        tabs.addTab(buildAlistamientos(), "Alistamientos");
        tabs.addTab(buildDetallesAlistamiento(), "Detalles de Alistamiento");

        Component footer = buildFooter();
        content.addComponent(footer);
        content.setComponentAlignment(footer, Alignment.BOTTOM_CENTER);

        accionAlistamientoBeanFieldGroup = new BeanFieldGroup<>(AccionAlistamiento.class);
        accionAlistamientoBeanFieldGroup.bindMemberFields(this);

        this.setContent(content);
    }

    private Component buildAlistamientos() {
        FormLayout alistamientoForm = new FormLayout();
        alistamientoForm.setSpacing(true);
        alistamientoForm.setResponsive(true);

        //line 1
        HorizontalLayout l1 = new HorizontalLayout();
        l1.setSpacing(true);
        alistamientoForm.addComponent(l1);

        vehiculoField = new SuggestField();
        vehiculoField.setWidth(12, Unit.EM);
        vehiculoField.setCaption("Placa del Vehículo");
        vehiculoField.setNewItemsAllowed(false);

        vehiculoField.setSuggestionHandler(s -> {
            List<Object> result = new ArrayList<>();
            List<Vehiculo> vehiculos = vehiculoRepository.search(s);
            for (Vehiculo vehiculo : vehiculos) {
                result.add(vehiculo);
            }
            if (result.size() == 0) {
                Notification.show("No se encontraron vehículos para el criterio introducido", Notification.Type.TRAY_NOTIFICATION);
            }
            return result;
        });
        vehiculoField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        vehiculoField.setImmediate(true);
        vehiculoField.setTokenMode(false);
        vehiculoField.setSuggestionConverter(new VehiculoSuggestionConverter());
        vehiculoField.setPopupWidth(400);
        vehiculoField.setRequired(true);
        vehiculoField.setRequiredError("El vehículo es requerido");
        l1.addComponent(vehiculoField);

        tecnicoField = new SuggestField();
        tecnicoField.setWidth(28, Unit.EM);
        tecnicoField.setCaption("Encargado");
        tecnicoField.setNewItemsAllowed(false);
        tecnicoField.setSuggestionHandler(s -> {
            List<Object> result = new ArrayList<>();
            List<Tercero> terceros = terceroRepository.search(s);
            for (Tercero tercero : terceros) {
                result.add(tercero);
            }
            if (result.size() == 0) {
                Notification.show("No se encontraron encargados para el criterio introducido", Notification.Type.TRAY_NOTIFICATION);
            }
            return result;
        });
        tecnicoField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        tecnicoField.setImmediate(true);
        tecnicoField.setTokenMode(false);
        tecnicoField.setSuggestionConverter(new TerceroSuggestionConverter());
        tecnicoField.setPopupWidth(400);
        tecnicoField.setRequired(true);
        tecnicoField.setRequiredError("El encargado es requerido");
        l1.addComponent(tecnicoField);

        //line 2
        HorizontalLayout l2 = new HorizontalLayout();
        l2.setSpacing(true);
        alistamientoForm.addComponent(l2);

        fechaField = new DateField("Fecha Alistamiento");
        fechaField.setStyleName(ValoTheme.DATEFIELD_TINY);
        fechaField.setRangeStart(fechaField.getRangeEnd());
        StaticMembers.setErrorRangoEspanhol(fechaField);
        fechaField.setWidth(12, Unit.EM);
        fechaField.setRequired(true);
        fechaField.setRequiredError("La fecha es requerida");
        l2.addComponent(fechaField);

        kmActualField = new TextField("KM Actual");
        kmActualField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        kmActualField.setWidth(11, Unit.EM);
        kmActualField.setValidationVisible(true);
        kmActualField.setNullRepresentation("");
        kmActualField.setRequired(true);
        kmActualField.setMaxLength(20);
        kmActualField.setRequiredError("El km actual es requerido");
        l2.addComponent(kmActualField);

        operativoField = new CheckBox("Operativo");
        operativoField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        operativoField.addStyleName("fixed-checkbox");
        operativoField.setWidth(9, Unit.EM);
        l2.addComponent(operativoField);

        //line 3
        HorizontalLayout l3 = new HorizontalLayout();
        l3.setSpacing(true);
        alistamientoForm.addComponent(l3);

        detalleField = new TextArea("Detalles");
        detalleField.addStyleName(ValoTheme.TEXTAREA_TINY);
        detalleField.setNullRepresentation("");
        detalleField.setColumns(35);
        detalleField.setRows(4);
        l3.addComponent(detalleField);

        return alistamientoForm;
    }

    private Component buildDetallesAlistamiento() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(new MarginInfo(true, false, true, false));
        layout.setSpacing(true);
        HorizontalLayout l1 = new HorizontalLayout();
        layout.addComponent(l1);
        l1.setSpacing(true);

        detallesField = new Label("Detalles del Alistamiento");
        detallesField.addStyleName(ValoTheme.LABEL_BOLD);
        detallesField.setWidth(20, Unit.EM);
        l1.addComponent(detallesField);

        layout.addComponent(alistamientoDetalleGrid);

        return layout;
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
            AccionAlistamiento accionAlistamiento = accionAlistamientoBeanFieldGroup.getItemDataSource().getBean();
            for (PerfilRecurso perfilRecurso : permisos) {
                if (perfilRecurso.getId().getRecurso().getCodigo().equals(AlistamientoView.VIEW_NAME)) {
                    if ((perfilRecurso.isCrear() && accionAlistamiento.getId() == 0) || (perfilRecurso.isModificar() && accionAlistamiento.getId() != 0)) {
                        tienePermiso = true;
                    }
                }
            }
            if (!tienePermiso) {
                ui.getNavigator().navigateTo("accessDeniedView");
                close();
                return;
            } else {
                save();
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

    //@ApplyAspect
    public void save() {
        try {
            accionAlistamientoBeanFieldGroup.commit();
            AccionAlistamiento accionAlistamiento = accionAlistamientoBeanFieldGroup.getItemDataSource().getBean();

            Vehiculo veh = accionAlistamiento.getVehiculo();

            if (accionAlistamiento.getKmActual() >= veh.getKmActual()){
                accionAlistamientoRepository.save(accionAlistamiento);
                veh.setKmActual(accionAlistamiento.getKmActual());
                vehiculoRepository.save(veh);

                Map<Long, AlistamientoDetalle> alistamientoDetalleAdicionados = alistamientoDetalleGrid.getAlistamientoDetalleAdicionados();
                for (Map.Entry<Long, AlistamientoDetalle> alistamientoDetalleEntry : alistamientoDetalleAdicionados.entrySet()) {
                    AlistamientoDetalle alistamientoDetalle = alistamientoDetalleEntry.getValue();
                    alistamientoDetalle.setAlistamiento(accionAlistamiento);
                    alistamientoDetalleRepository.save(alistamientoDetalle);
                }
                Map<Long, AlistamientoDetalle> alistamientoDetalleModficados = alistamientoDetalleGrid.getAlistamientoDetalleModificados();
                for (Map.Entry<Long, AlistamientoDetalle> alistamientoDetalleEntry : alistamientoDetalleModficados.entrySet()) {
                    AlistamientoDetalle alistamientoDetalle = alistamientoDetalleEntry.getValue();
                    alistamientoDetalle.setAlistamiento(accionAlistamiento);
                    alistamientoDetalleRepository.save(alistamientoDetalle);
                }
                Map<Long, AlistamientoDetalle> alistamientoDetalleEliminados = alistamientoDetalleGrid.getAlistamientoDetalleEliminados();
                for (Map.Entry<Long, AlistamientoDetalle> alistamientoDetalleEntry : alistamientoDetalleEliminados.entrySet()) {
                    AlistamientoDetalle alistamientoDetalle = alistamientoDetalleEntry.getValue();
                    alistamientoDetalleRepository.delete(alistamientoDetalle);
                }
                alistamientoDetalleGrid.restablecerListados();

                applicationEventBus.publish(AlistamientoGrid.NUEVO_ALISTAMIENTO, this, accionAlistamiento);
                Notification notification = new Notification("Notificaci\u00F3n", "Alistamiento guardado con \u00E9xito", Notification.Type.TRAY_NOTIFICATION);
                notification.setDelayMsec(3000);
                notification.setIcon(FontAwesome.INFO_CIRCLE);
                notification.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
                notification.show(Page.getCurrent());
                this.close();
            } else {
                StaticMembers.showNotificationError("Error", "El km actual debe ser mayor que el inicial");
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

    public Window show(long ID) {

        AccionAlistamiento accionAlistamiento = accionAlistamientoRepository.findOne(ID);
        if (accionAlistamiento == null) {
            accionAlistamiento = new AccionAlistamiento();
            modificar = false;
        } else {
            modificar = true;
        }
        accionAlistamientoBeanFieldGroup.setItemDataSource(accionAlistamiento);

        alistamientoDetalleGrid.container.removeAllItems();
        alistamientoDetalleGrid.container.addAll(accionAlistamiento.getDetalles());
        alistamientoDetalleGrid.setAccionAlistamiento(accionAlistamiento);

        return this;
    }

    public class TerceroSuggestionConverter implements SuggestField.SuggestionConverter {

        @Override
        public SuggestFieldSuggestion toSuggestion(Object item) {
            if (item != null) {
                Tercero tercero = (Tercero) item;
                return new SuggestFieldSuggestion(String.valueOf(tercero.getId()), tercero.getNombreTercero(), tercero.getNombreTercero());
            }
            return null;
        }

        @Override
        public Object toItem(SuggestFieldSuggestion suggestion) {
            return terceroRepository.findOne(Long.valueOf(suggestion.getId()));
        }
    }

    public class VehiculoSuggestionConverter implements SuggestField.SuggestionConverter {

        @Override
        public SuggestFieldSuggestion toSuggestion(Object item) {
            if (item != null) {
                Vehiculo vehiculo = (Vehiculo) item;
                return new SuggestFieldSuggestion(String.valueOf(vehiculo.getId()), vehiculo.getPlaca(), vehiculo.getPlaca());
            }
            return null;
        }

        @Override
        public Object toItem(SuggestFieldSuggestion suggestion) {
            return vehiculoRepository.findOne(Long.valueOf(suggestion.getId()));
        }
    }
}
