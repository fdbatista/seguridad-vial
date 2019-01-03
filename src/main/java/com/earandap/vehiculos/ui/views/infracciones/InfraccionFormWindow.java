package com.earandap.vehiculos.ui.views.infracciones;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.*;
import com.earandap.vehiculos.domain.nomenclador.TipoIncidente;
import com.earandap.vehiculos.domain.nomenclador.TipoSancion;
import com.earandap.vehiculos.repository.*;
import com.earandap.vehiculos.ui.MainUI;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents;
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
import org.vaadin.suggestfield.SuggestField;
import org.vaadin.suggestfield.client.SuggestFieldSuggestion;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by Angel Luis on 11/16/2015.
 */
@SpringComponent
@UIScope
public class InfraccionFormWindow extends Window {

    private BeanFieldGroup<Infraccion> infraccionBeanFieldGroup;

    @Autowired
    private EventBus.ApplicationEventBus applicationEventBus;


    //tab1
    private VerticalLayout content;

    @PropertyId("tipoSancion")
    private ComboBox tipoSancionField;
    private BeanItemContainer<TipoSancion> tipoSancionBeanItemContainer;
    
    @PropertyId("tipoIncidente")
    private ComboBox tipoIncidenteField;
    private BeanItemContainer<TipoIncidente> tipoIncidenteBeanItemContainer;
    
    @PropertyId("conductor")
    private SuggestField conductorField;

    @PropertyId("vehiculo")
    private SuggestField vehiculoField;

    @PropertyId("incidente")
    private ComboBox incidenteField;
    private BeanItemContainer<Incidente> incidenteBeanItemContainer;

    @PropertyId("causa")
    private TextArea causaField;

    @PropertyId("fechaInfraccion")
    private DateField fechaInfraccionField;

    @PropertyId("fechaPago")
    private DateField fechaPagoField;

    @PropertyId("valorMulta")
    private TextField valorMultaField;

    @PropertyId("detalle")
    private TextArea detalleField;

    @Autowired
    private InfraccionRepository infraccionRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private IncidenteRepository incidenteRepository;

    @Autowired
    private TipoSancionRepository tipoSancionRepository;
    
    @Autowired
    private TipoIncidenteRepository tipoIncidenteRepository;

    @Autowired
    private TerceroRepository terceroRepository;

    private boolean modificar;


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

        tabs.addTab(buildInfracciones(), "Infracciones");

        Component footer = buildFooter();
        content.addComponent(footer);
        content.setComponentAlignment(footer, Alignment.BOTTOM_CENTER);

        infraccionBeanFieldGroup = new BeanFieldGroup<>(Infraccion.class);
        infraccionBeanFieldGroup.bindMemberFields(this);

        this.setContent(content);
    }

    private Component buildInfracciones() {
        FormLayout infraccionForm = new FormLayout();
        infraccionForm.setSpacing(true);
        infraccionForm.setResponsive(true);

        //line 1
        HorizontalLayout l1 = new HorizontalLayout();
        l1.setSpacing(true);
        infraccionForm.addComponent(l1);

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
            if (result.size() == 0)
                Notification.show("No se encontraron vehiculos para el criterio introducido", Notification.Type.TRAY_NOTIFICATION);

            return result;
        });
        vehiculoField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        vehiculoField.setImmediate(true);
        vehiculoField.setTokenMode(false);
        vehiculoField.setSuggestionConverter(new VehiculoSuggestionConverter());
        vehiculoField.setPopupWidth(400);
        vehiculoField.setRequired(true);
        vehiculoField.setRequiredError("El veh\u00EDculo es requerido");
        l1.addComponent(vehiculoField);


        tipoSancionBeanItemContainer = new BeanItemContainer<>(TipoSancion.class);
        tipoSancionField = new ComboBox("Tipo Sanci\u00F3n");
        tipoSancionField.addStyleName(ValoTheme.COMBOBOX_TINY);
        tipoSancionField.setWidth(15, Unit.EM);
        tipoSancionField.setContainerDataSource(tipoSancionBeanItemContainer);
        tipoSancionField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        tipoSancionField.setItemCaptionPropertyId("descripcion");
        tipoSancionField.setNullSelectionAllowed(false);
        tipoSancionField.setScrollToSelectedItem(true);
        tipoSancionField.setRequired(true);
        tipoSancionField.setRequiredError("El tipo de sanci\u00F3n es requerido");
        l1.addComponent(tipoSancionField);

        fechaInfraccionField = new DateField("Fecha Infracci\u00F3n");
        fechaInfraccionField.addStyleName(ValoTheme.DATEFIELD_TINY);
        fechaInfraccionField.setWidth(10, Unit.EM);
        fechaInfraccionField.setRequired(true);
        fechaInfraccionField.setRequiredError("La fecha de infracci\u00F3n es requerida");
        fechaInfraccionField.setRangeEnd(new Date());
        StaticMembers.setErrorRangoEspanhol(fechaInfraccionField);
        l1.addComponent(fechaInfraccionField);

        //line 2
        HorizontalLayout l2 = new HorizontalLayout();
        l2.setSpacing(true);
        infraccionForm.addComponent(l2);

        conductorField = new SuggestField();
        conductorField.setWidth(20, Unit.EM);
        conductorField.setCaption("Conductor");
        conductorField.setNewItemsAllowed(false);
        conductorField.setSuggestionHandler(s -> {
            List<Object> result = new ArrayList<>();
            List<Persona> conductores = personaRepository.searchConductor(s);
            for (Persona persona : conductores) {
                result.add(persona);
            }
            if (result.size() == 0)
                Notification.show("No se encontraron conductores para el criterio introducido", Notification.Type.TRAY_NOTIFICATION);

            return result;
        });
        conductorField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        conductorField.setImmediate(true);
        conductorField.setTokenMode(false);
        conductorField.setSuggestionConverter(new ConductorSuggestionConverter());
        conductorField.setPopupWidth(200);
        conductorField.setRequired(true);
        conductorField.setRequiredError("El conductor es requerido");
        conductorField.setDescription("Seleccione un conductor para<br />mostrar los incidentes relacionados");
        l2.addComponent(conductorField);

        incidenteBeanItemContainer = new BeanItemContainer<>(Incidente.class);
        incidenteField = new ComboBox("Incidente");
        incidenteField.addStyleName(ValoTheme.COMBOBOX_TINY);
        incidenteField.setWidth(18, Unit.EM);
        incidenteField.setContainerDataSource(incidenteBeanItemContainer);
        incidenteField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        incidenteField.setItemCaptionPropertyId("nombreConductorVehiculo");
        incidenteField.setDescription("Seleccione un conductor para<br />mostrar los incidentes relacionados");
        incidenteField.setNullSelectionAllowed(false);
        incidenteField.setScrollToSelectedItem(true);
        l2.addComponent(incidenteField);

        incidenteField.addFocusListener(new FieldEvents.FocusListener() {
            @Override
            public void focus(FieldEvents.FocusEvent event) {
                Persona persona = (Persona) conductorField.getValue();
                Vehiculo vehiculo = (Vehiculo) vehiculoField.getValue();
                incidenteBeanItemContainer.removeAllItems();
                if(vehiculo != null && persona != null)
                    incidenteBeanItemContainer.addAll(incidenteRepository.findByConductorAndVehiculo(persona.getConductor(),vehiculo));
            }
        });


        //line 3
        HorizontalLayout l3 = new HorizontalLayout();
        l3.setSpacing(true);
        infraccionForm.addComponent(l3);

        valorMultaField = new TextField("Valor Multa");
        valorMultaField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        valorMultaField.setWidth(10, Unit.EM);
        l3.addComponent(valorMultaField);

        fechaPagoField = new DateField("Fecha Pago");
        fechaPagoField.addStyleName(ValoTheme.DATEFIELD_TINY);
        fechaPagoField.setWidth(10, Unit.EM);
        l3.addComponent(fechaPagoField);
        
        tipoIncidenteBeanItemContainer = new BeanItemContainer<>(TipoIncidente.class);
        tipoIncidenteField = new ComboBox("Tipo Incidente");
        tipoIncidenteField.addStyleName(ValoTheme.COMBOBOX_TINY);
        tipoIncidenteField.setWidth(15, Unit.EM);
        tipoIncidenteField.setContainerDataSource(tipoIncidenteBeanItemContainer);
        tipoIncidenteField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        tipoIncidenteField.setItemCaptionPropertyId("descripcion");
        tipoIncidenteField.setNullSelectionAllowed(false);
        tipoIncidenteField.setScrollToSelectedItem(true);
        l3.addComponent(tipoIncidenteField);

        //line 4
        HorizontalLayout l4 = new HorizontalLayout();
        l4.setSpacing(true);
        infraccionForm.addComponent(l4);

        causaField = new TextArea("Causa");
        causaField.addStyleName(ValoTheme.TEXTAREA_TINY);
        causaField.setNullRepresentation("");
        causaField.setColumns(40);
        causaField.setRows(2);
        causaField.setMaxLength(1000);
        l4.addComponent(causaField);

        //line 5
        HorizontalLayout l5 = new HorizontalLayout();
        l5.setSpacing(true);
        infraccionForm.addComponent(l5);

        detalleField = new TextArea("Detalles");
        detalleField.addStyleName(ValoTheme.TEXTAREA_TINY);
        detalleField.setNullRepresentation("");
        detalleField.setColumns(40);
        detalleField.setRows(2);
        detalleField.setMaxLength(1000);
        l5.addComponent(detalleField);

        return infraccionForm;
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
            Set<PerfilRecurso> permisos = ((Usuario)auth.getPrincipal()).getPerfil().getPerfilRecursos();
            boolean tienePermiso = false;
            Infraccion infraccionAux = infraccionBeanFieldGroup.getItemDataSource().getBean();
            for (PerfilRecurso perfilRecurso: permisos){
                if (perfilRecurso.getId().getRecurso().getCodigo().equals(InfraccionView.VIEW_NAME)){
                    if ((perfilRecurso.isCrear() && infraccionAux.getId() == 0)||(perfilRecurso.isModificar() && infraccionAux.getId() != 0) )
                        tienePermiso = true;
                }
            }
            if (!tienePermiso) {
                ui.getNavigator().navigateTo("accessDeniedView");
                close();
                return;
            }else {
                try {
                    infraccionBeanFieldGroup.commit();
                    Infraccion infraccion = infraccionBeanFieldGroup.getItemDataSource().getBean();

                    infraccionRepository.save(infraccion);

                    applicationEventBus.publish(InfraccionGrid.NUEVA_INFRACCION, this, infraccion);
                    Notification notification = new Notification("Notificaci\u00F3n", "Infracci\u00F3n guardada con \u00E9xito", Notification.Type.TRAY_NOTIFICATION);
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

    public Window show(long ID) {

        tipoSancionBeanItemContainer.removeAllItems();
        tipoSancionBeanItemContainer.addAll(tipoSancionRepository.findAll());
        
        /*incidenteBeanItemContainer.removeAllItems();
        incidenteBeanItemContainer.addAll(incidenteRepository.findAll());*/
        
        tipoIncidenteBeanItemContainer.removeAllItems();
        tipoIncidenteBeanItemContainer.addAll(tipoIncidenteRepository.findAll());

        Infraccion infraccion = infraccionRepository.findOne(ID);
        if (infraccion == null) {
            infraccion = new Infraccion();
            modificar = false;
        }
        else{
            modificar = true;
        }
        infraccionBeanFieldGroup.setItemDataSource(infraccion);
        return this;
    }

    public class ConductorSuggestionConverter implements SuggestField.SuggestionConverter {

        @Override
        public SuggestFieldSuggestion toSuggestion(Object item) {
            if (item != null) {
                Persona persona = (Persona) item;
                return new SuggestFieldSuggestion(String.valueOf(persona.getId()), persona.getNombreTercero(), persona.getNombreTercero());
            }
            return null;
        }

        @Override
        public Object toItem(SuggestFieldSuggestion suggestion) {
            return personaRepository.findOne(Long.valueOf(suggestion.getId()));
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
