package com.earandap.vehiculos.ui.views.conductores;

import com.earandap.vehiculos.domain.Licencia;
import com.earandap.vehiculos.domain.nomenclador.TipoLicencia;
import com.earandap.vehiculos.domain.nomenclador.TipoServicio;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.themes.ValoTheme;
import java.util.Calendar;
import java.util.GregorianCalendar;

import java.util.List;

/**
 * Created by Angel Luis on 11/22/2015.
 */
public class LicenciaForm extends FormLayout {

    @PropertyId("tipoLicencia")
    private ComboBox tipoLicenciaField;
    private BeanItemContainer<TipoLicencia> tipoLicenciaContainer;
    
    private TextArea descLicenciaField;
    
    @PropertyId("tipoServicio")
    private ComboBox tipoServicioField;
    private BeanItemContainer<TipoServicio> tipoServicioContainer;
    
    @PropertyId("vigencia")
    private DateField vigenciaField;

    @PropertyId("autorizadoEmpresa")
    private CheckBox autorizadoEmpresaField;

    private BeanFieldGroup<Licencia> licenciaBeanFieldGroup;

    private Licencia licencia;

    public ComboBox getTipoLicenciaField() {
        return tipoLicenciaField;
    }

    public void setTipoLicenciaField(ComboBox tipoLicenciaField) {
        this.tipoLicenciaField = tipoLicenciaField;
    }

    public BeanItemContainer<TipoLicencia> getTipoLicenciaContainer() {
        return tipoLicenciaContainer;
    }

    public void setTipoLicenciaContainer(BeanItemContainer<TipoLicencia> tipoLicenciaContainer) {
        this.tipoLicenciaContainer = tipoLicenciaContainer;
    }

    public CheckBox getAutorizadoEmpresaField() {
        return autorizadoEmpresaField;
    }

    public void setAutorizadoEmpresaField(CheckBox autorizadoEmpresaField) {
        this.autorizadoEmpresaField = autorizadoEmpresaField;
    }

    public BeanFieldGroup<Licencia> getLicenciaBeanFieldGroup() {
        return licenciaBeanFieldGroup;
    }

    public void setLicenciaBeanFieldGroup(BeanFieldGroup<Licencia> licenciaBeanFieldGroup) {
        this.licenciaBeanFieldGroup = licenciaBeanFieldGroup;
    }

    public Licencia getLicencia() {
        return licencia;
    }

    public void setLicencia(Licencia licencia) {
        this.licencia = licencia;
    }

    public LicenciaForm(Licencia licencia, List<TipoLicencia> tipoLicencias, List<TipoServicio> tipoServicios) {

        this.setStyleName("seguro-form");
        this.licencia = licencia;
        descLicenciaField = new TextArea("Clase de Veh\u00EDculo");
        
        if (this.licencia.getId() == 0)
        {
            //this.licencia.setTipoLicenciaDescripcion("");
            descLicenciaField.setValue("");
        }
        
        tipoLicenciaContainer = new BeanItemContainer<>(TipoLicencia.class);
        tipoLicenciaContainer.addAll(tipoLicencias);
        tipoLicenciaField = new ComboBox("Categor\u00EDa");
        tipoLicenciaField.setWidth(9, Unit.EM);
        tipoLicenciaField.addStyleName(ValoTheme.COMBOBOX_TINY);
        tipoLicenciaField.setContainerDataSource(tipoLicenciaContainer);
        tipoLicenciaField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        tipoLicenciaField.setItemCaptionPropertyId("codigo");
        tipoLicenciaField.setNullSelectionAllowed(false);
        tipoLicenciaField.setScrollToSelectedItem(true);
        tipoLicenciaField.setRequired(true);
        tipoLicenciaField.setRequiredError("La categor\u00EDa es requerida");
        tipoLicenciaField.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                descLicenciaField.setValue(((TipoLicencia)tipoLicenciaField.getValue()).getDescripcion());
            }
        });
        this.addComponent(tipoLicenciaField);
        
        descLicenciaField.addStyleName(ValoTheme.TEXTAREA_BORDERLESS + " solo-lectura");
        descLicenciaField.setWidth(20, Unit.EM);
        descLicenciaField.setHeight(4, Unit.EM);
        this.addComponent(descLicenciaField);
        
        vigenciaField = new DateField("Vigencia");
        vigenciaField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        vigenciaField.setWidth(12, Unit.EM);
        vigenciaField.setRequired(true);
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.getInstance().get(Calendar.SHORT_FORMAT ),11,31);
        vigenciaField.setRequiredError("La fecha de vigencia es requerida");
        vigenciaField.setValidationVisible(true);
        this.addComponent(vigenciaField);
        
        tipoServicioContainer = new BeanItemContainer<>(TipoServicio.class);
        tipoServicioContainer.addAll(tipoServicios);
        tipoServicioField = new ComboBox("Servicio");
        tipoServicioField.addStyleName(ValoTheme.COMBOBOX_TINY);
        tipoServicioField.setContainerDataSource(tipoServicioContainer);
        tipoServicioField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        tipoServicioField.setItemCaptionPropertyId("descripcion");
        tipoServicioField.setNullSelectionAllowed(false);
        tipoServicioField.setScrollToSelectedItem(true);
        this.addComponent(tipoServicioField);
        
        autorizadoEmpresaField = new CheckBox("Autorizada");
        autorizadoEmpresaField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        autorizadoEmpresaField.setWidth(9, Unit.EM);
        autorizadoEmpresaField.setStyleName("fixed-checkbox");
        this.addComponent(autorizadoEmpresaField);

        licenciaBeanFieldGroup = new BeanFieldGroup<>(Licencia.class);
        licenciaBeanFieldGroup.setItemDataSource(licencia);
        licenciaBeanFieldGroup.bindMemberFields(this);
    }
}
