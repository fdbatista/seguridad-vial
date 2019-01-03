package com.earandap.vehiculos.ui.views.mantenimiento;

import com.earandap.vehiculos.domain.MantenimientoDetalle;
import com.earandap.vehiculos.domain.nomenclador.Actividad;
import com.earandap.vehiculos.domain.nomenclador.Resultado;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

/**
 * Created by Angel Luis on 10/30/2015.
 */
public class MantenimientoDetalleForm extends FormLayout {

    @PropertyId("resultado")
    private ComboBox resultadoField;
    private BeanItemContainer<Resultado> resultadoBeanItemContainer;

    @PropertyId("actividad")
    private ComboBox actividadField;
    private BeanItemContainer<Actividad> actividadBeanItemContainer;

    private BeanFieldGroup<MantenimientoDetalle> mantenimientoDetalleBeanFieldGroup;

    private MantenimientoDetalle mantenimientoDetalle;

    public BeanFieldGroup<MantenimientoDetalle> getMantenimientoDetalleBeanFieldGroup() {
        return mantenimientoDetalleBeanFieldGroup;
    }

    public void setMantenimientoDetalleBeanFieldGroup(BeanFieldGroup<MantenimientoDetalle> mantenimientoDetalleBeanFieldGroup) {
        this.mantenimientoDetalleBeanFieldGroup = mantenimientoDetalleBeanFieldGroup;
    }

    public MantenimientoDetalleForm(MantenimientoDetalle mantenimientoDetalle, List<Actividad> actividades, List<Resultado> resultados) {

        this.setStyleName("mantenimientodetalle-form");

        this.mantenimientoDetalle = mantenimientoDetalle;

        actividadBeanItemContainer = new BeanItemContainer<Actividad>(Actividad.class);
        actividadBeanItemContainer.addAll(actividades);

        resultadoBeanItemContainer = new BeanItemContainer<Resultado>(Resultado.class);
        resultadoBeanItemContainer.addAll(resultados);

        actividadField = new ComboBox("Actividad");
        actividadField.addStyleName(ValoTheme.COMBOBOX_TINY);
        actividadField.setContainerDataSource(actividadBeanItemContainer);
        actividadField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        actividadField.setItemCaptionPropertyId("descripcion");
        actividadField.setNullSelectionAllowed(false);
        actividadField.setScrollToSelectedItem(true);
        actividadField.setRequired(true);
        actividadField.setRequiredError("La actividad es requerida");
        this.addComponent(actividadField);

        resultadoField = new ComboBox("Resultado");
        resultadoField.addStyleName(ValoTheme.COMBOBOX_TINY);
        resultadoField.setContainerDataSource(resultadoBeanItemContainer);
        resultadoField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        resultadoField.setItemCaptionPropertyId("descripcion");
        resultadoField.setNullSelectionAllowed(false);
        resultadoField.setScrollToSelectedItem(true);
        resultadoField.setRequired(true);
        resultadoField.setRequiredError("El resultado es requerido");
        this.addComponent(resultadoField);


        mantenimientoDetalleBeanFieldGroup = new BeanFieldGroup<>(MantenimientoDetalle.class);
        mantenimientoDetalleBeanFieldGroup.setItemDataSource(mantenimientoDetalle);
        mantenimientoDetalleBeanFieldGroup.bindMemberFields(this);
    }
}
