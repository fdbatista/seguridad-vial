package com.earandap.vehiculos.ui.views.incidentes;

import com.earandap.vehiculos.domain.IncidenteLesion;
import com.earandap.vehiculos.domain.nomenclador.TipoLesion;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

/**
 * Created by felix.batista.
 */
public class IncidenteLesionForm extends FormLayout{

    @PropertyId("tipoLesion")
    private ComboBox tipoLesionField;
    private BeanItemContainer<TipoLesion> tipoLesionContainer;
    
    @PropertyId("cantidad")
    private TextField cantidadField;

    private BeanFieldGroup<IncidenteLesion> incidenteLesionBeanFieldGroup;

    public BeanFieldGroup<IncidenteLesion> getIncidenteLesionBeanFieldGroup() {
        return incidenteLesionBeanFieldGroup;
    }

    public void setIncidenteLesionBeanFieldGroup(BeanFieldGroup<IncidenteLesion> incidenteLesionBeanFieldGroup) {
        this.incidenteLesionBeanFieldGroup = incidenteLesionBeanFieldGroup;
    }

    public IncidenteLesionForm(IncidenteLesion incidenteLesion, List<TipoLesion> tipoLesions) {

        this.setStyleName("seguro-form");
        
        tipoLesionContainer = new BeanItemContainer<>(TipoLesion.class);
        tipoLesionContainer.addAll(tipoLesions);

        tipoLesionField = new ComboBox("Tipo de lesi\u00F3n");
        tipoLesionField.addStyleName(ValoTheme.COMBOBOX_TINY);
        tipoLesionField.setContainerDataSource(tipoLesionContainer);
        tipoLesionField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        tipoLesionField.setItemCaptionPropertyId("descripcion");
        tipoLesionField.setNullSelectionAllowed(false);
        tipoLesionField.setScrollToSelectedItem(true);
        tipoLesionField.setRequired(true);
        tipoLesionField.setRequiredError("El tipo de lesi\u00F3n es requerido");
        tipoLesionField.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                cantidadField.focus();
                cantidadField.selectAll();
            }
        });
        this.addComponent(tipoLesionField);
        
        cantidadField = new TextField("Cantidad de lesionados");
        cantidadField.setStyleName(ValoTheme.TEXTFIELD_TINY);
        cantidadField.setMaxLength(50);
        cantidadField.setRequired(true);
        cantidadField.setNullRepresentation("");
        cantidadField.setRequiredError("La cantidad es requerida");
        this.addComponent(cantidadField);

        incidenteLesionBeanFieldGroup = new BeanFieldGroup<>(IncidenteLesion.class);
        incidenteLesionBeanFieldGroup.setItemDataSource(incidenteLesion);
        incidenteLesionBeanFieldGroup.bindMemberFields(this);
        
    }

}
