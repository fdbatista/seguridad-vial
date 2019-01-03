package com.earandap.vehiculos.ui.views.incidentes;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.Infraccion;
import com.earandap.vehiculos.domain.Persona;
import com.earandap.vehiculos.domain.Vehiculo;
import com.earandap.vehiculos.domain.nomenclador.TipoSancion;
import com.earandap.vehiculos.repository.PersonaRepository;
import com.earandap.vehiculos.repository.VehiculoRepository;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.suggestfield.SuggestField;
import org.vaadin.suggestfield.client.SuggestFieldSuggestion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Angel Luis on 11/18/2015.
 */
public class InfraccionForm extends FormLayout {

    @PropertyId("tipoSancion")
    private ComboBox tipoSancionField;
    private BeanItemContainer<TipoSancion> tipoSancionBeanItemContainer;

    @PropertyId("conductor")
    private SuggestField conductorField;

    @PropertyId("vehiculo")
    private SuggestField vehiculoField;

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

    private BeanFieldGroup<Infraccion> infraccionBeanFieldGroup;

    private Infraccion infraccion;

    private PersonaRepository personaRepository;

    private VehiculoRepository vehiculoRepository;

    public ComboBox getTipoSancionField() {
        return tipoSancionField;
    }

    public void setTipoSancionField(ComboBox tipoSancionField) {
        this.tipoSancionField = tipoSancionField;
    }

    public BeanItemContainer<TipoSancion> getTipoSancionBeanItemContainer() {
        return tipoSancionBeanItemContainer;
    }

    public void setTipoSancionBeanItemContainer(BeanItemContainer<TipoSancion> tipoSancionBeanItemContainer) {
        this.tipoSancionBeanItemContainer = tipoSancionBeanItemContainer;
    }

    public SuggestField getConductorField() {
        return conductorField;
    }

    public void setConductorField(SuggestField conductorField) {
        this.conductorField = conductorField;
    }

    public SuggestField getVehiculoField() {
        return vehiculoField;
    }

    public void setVehiculoField(SuggestField vehiculoField) {
        this.vehiculoField = vehiculoField;
    }

    public TextArea getCausaField() {
        return causaField;
    }

    public void setCausaField(TextArea causaField) {
        this.causaField = causaField;
    }

    public DateField getFechaInfraccionField() {
        return fechaInfraccionField;
    }

    public void setFechaInfraccionField(DateField fechaInfraccionField) {
        this.fechaInfraccionField = fechaInfraccionField;
    }

    public DateField getFechaPagoField() {
        return fechaPagoField;
    }

    public void setFechaPagoField(DateField fechaPagoField) {
        this.fechaPagoField = fechaPagoField;
    }

    public TextField getValorMultaField() {
        return valorMultaField;
    }

    public void setValorMultaField(TextField valorMultaField) {
        this.valorMultaField = valorMultaField;
    }

    public TextArea getDetalleField() {
        return detalleField;
    }

    public void setDetalleField(TextArea detalleField) {
        this.detalleField = detalleField;
    }

    public BeanFieldGroup<Infraccion> getInfraccionBeanFieldGroup() {
        return infraccionBeanFieldGroup;
    }

    public void setInfraccionBeanFieldGroup(BeanFieldGroup<Infraccion> infraccionBeanFieldGroup) {
        this.infraccionBeanFieldGroup = infraccionBeanFieldGroup;
    }

    public Infraccion getInfraccion() {
        return infraccion;
    }

    public void setInfraccion(Infraccion infraccion) {
        this.infraccion = infraccion;
    }

    public PersonaRepository getPersonaRepository() {
        return personaRepository;
    }

    public void setPersonaRepository(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    public VehiculoRepository getVehiculoRepository() {
        return vehiculoRepository;
    }

    public void setVehiculoRepository(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    public InfraccionForm(Infraccion infraccion) {

        this.setStyleName("seguro-form");
        this.setWidth(700, Unit.PIXELS);
        this.setHeight(500, Unit.PIXELS);
        this.infraccion = infraccion;
        tipoSancionBeanItemContainer = new BeanItemContainer<TipoSancion>(TipoSancion.class);


        //line 1
        HorizontalLayout l1 = new HorizontalLayout();
        l1.setSpacing(true);
        this.addComponent(l1);

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
                Notification.show("No se encontraron vehículos para el criterio introducido", Notification.Type.TRAY_NOTIFICATION);
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
        this.addComponent(l2);

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
        l2.addComponent(conductorField);

        //line 3
        HorizontalLayout l3 = new HorizontalLayout();
        l3.setSpacing(true);
        this.addComponent(l3);

        valorMultaField = new TextField("Valor Multa");
        valorMultaField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        valorMultaField.setWidth(10, Unit.EM);
        l3.addComponent(valorMultaField);

        fechaPagoField = new DateField("Fecha Pago");
        fechaPagoField.addStyleName(ValoTheme.DATEFIELD_TINY);
        fechaPagoField.setWidth(10, Unit.EM);
        l3.addComponent(fechaPagoField);


        //line 4
        HorizontalLayout l4 = new HorizontalLayout();
        l4.setSpacing(true);
        this.addComponent(l4);

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
        this.addComponent(l5);

        detalleField = new TextArea("Detalles");
        detalleField.addStyleName(ValoTheme.TEXTAREA_TINY);
        detalleField.setNullRepresentation("");
        detalleField.setColumns(40);
        detalleField.setRows(2);
        detalleField.setMaxLength(1000);
        l5.addComponent(detalleField);



        infraccionBeanFieldGroup = new BeanFieldGroup<>(Infraccion.class);
        infraccionBeanFieldGroup.setItemDataSource(infraccion);
        infraccionBeanFieldGroup.bindMemberFields(this);
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
