package com.earandap.vehiculos.ui.views.vehiculos;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.CompanhiaSeguros;
import com.earandap.vehiculos.domain.Seguro;
import com.earandap.vehiculos.domain.nomenclador.TipoSeguro;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Date;
import java.util.List;

/**
 * Created by Angel Luis on 10/17/2015.
 */
public class SeguroForm extends FormLayout{

    @PropertyId("tipoSeguro")
    private ComboBox tipoSeguroField;
    private BeanItemContainer<TipoSeguro> tipoSeguroContainer;
    
    @PropertyId("companhia")
    private ComboBox companhiaField;
    private BeanItemContainer<CompanhiaSeguros> companhiaContainer;

    @PropertyId("numero")
    private TextField numeroField;

    @PropertyId("fechaEmision")
    private DateField fechaEmisionField;

    @PropertyId("fechaVencimiento")
    private DateField fechaVencimientoField;

    private BeanFieldGroup<Seguro> seguroBeanFieldGroup;

    public BeanFieldGroup<Seguro> getSeguroBeanFieldGroup() {
        return seguroBeanFieldGroup;
    }

    public void setSeguroBeanFieldGroup(BeanFieldGroup<Seguro> seguroBeanFieldGroup) {
        this.seguroBeanFieldGroup = seguroBeanFieldGroup;
    }

    public SeguroForm(Seguro seguro, List<TipoSeguro> tipoSeguros, List<CompanhiaSeguros> companhias) {

        this.setStyleName("seguro-form");
        
        tipoSeguroContainer = new BeanItemContainer<>(TipoSeguro.class);
        tipoSeguroContainer.addAll(tipoSeguros);
        
        companhiaContainer = new BeanItemContainer<>(CompanhiaSeguros.class);
        companhiaContainer.addAll(companhias);

        tipoSeguroField = new ComboBox("Tipo de Seguro");
        tipoSeguroField.addStyleName(ValoTheme.COMBOBOX_TINY);
        tipoSeguroField.setContainerDataSource(tipoSeguroContainer);
        tipoSeguroField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        tipoSeguroField.setItemCaptionPropertyId("descripcion");
        tipoSeguroField.setNullSelectionAllowed(false);
        tipoSeguroField.setScrollToSelectedItem(true);
        tipoSeguroField.setRequired(true);
        tipoSeguroField.setRequiredError("El tipo de seguro es requerido");
        tipoSeguroField.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                if (((TipoSeguro)tipoSeguroField.getValue()).getDescripcion().equals("SOAT"))
                {
                    companhiaField.setRequired(true);
                    companhiaField.setRequiredError("La compa\u00F1\u00EDa es requerida");
                }
                else
                {
                    companhiaField.setRequired(false);
                    companhiaField.setRequiredError(null);
                }
            }
        });
        this.addComponent(tipoSeguroField);
        
        companhiaField = new ComboBox("Compa\u00F1\u00EDa");
        companhiaField.addStyleName(ValoTheme.COMBOBOX_TINY);
        companhiaField.setContainerDataSource(companhiaContainer);
        companhiaField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        companhiaField.setItemCaptionPropertyId("nombre");
        companhiaField.setNullSelectionAllowed(false);
        companhiaField.setScrollToSelectedItem(true);
        this.addComponent(companhiaField);
        
        numeroField = new TextField("N\u00FAmero");
        numeroField.setStyleName(ValoTheme.TEXTFIELD_TINY);
        numeroField.setMaxLength(50);
        numeroField.setRequired(true);
        numeroField.setNullRepresentation("");
        numeroField.setRequiredError("El n\u00FAmero es requerido");
        this.addComponent(numeroField);

        fechaEmisionField = new DateField("Fecha Emisi\u00F3n");
        fechaEmisionField.setStyleName(ValoTheme.DATEFIELD_TINY);
        fechaEmisionField.setRangeEnd(new Date());
        StaticMembers.setErrorRangoEspanhol(fechaEmisionField);
        fechaEmisionField.setRequired(true);
        fechaEmisionField.setRequiredError("La fecha de emisi\u00F3n es requerida");
        this.addComponent(fechaEmisionField);

        fechaVencimientoField = new DateField("Fecha Vencimiento");
        fechaVencimientoField.setStyleName(ValoTheme.DATEFIELD_TINY);
        fechaVencimientoField.setRangeStart(fechaEmisionField.getRangeEnd());
        StaticMembers.setErrorRangoEspanhol(fechaVencimientoField);
        fechaVencimientoField.setRequired(true);
        fechaVencimientoField.setRequiredError("La fecha de vencimiento es requerida");
        this.addComponent(fechaVencimientoField);

        seguroBeanFieldGroup = new BeanFieldGroup<>(Seguro.class);
        seguroBeanFieldGroup.setItemDataSource(seguro);
        seguroBeanFieldGroup.bindMemberFields(this);
        
    }


}
