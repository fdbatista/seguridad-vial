package com.earandap.vehiculos.ui.views.alistamiento;

import com.earandap.vehiculos.domain.AlistamientoDetalle;
import com.earandap.vehiculos.domain.nomenclador.Actividad;
import com.earandap.vehiculos.domain.nomenclador.Resultado;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

/**
 * Created by Angel Luis on 11/4/2015.
 */
public class AlistamientoDetalleForm extends FormLayout {

    @PropertyId("resultado")
    private ComboBox resultadoField;
    private BeanItemContainer<Resultado> resultadoBeanItemContainer;

    @PropertyId("actividad")
    private ComboBox actividadField;
    private BeanItemContainer<Actividad> actividadBeanItemContainer;

    @PropertyId("detalle")
    private TextField detalleField;

    private BeanFieldGroup<AlistamientoDetalle> alistamientoDetalleBeanFieldGroup;

    private AlistamientoDetalle alistamientoDetalle;

    public AlistamientoDetalleForm(AlistamientoDetalle alistamientoDetalle, List<Actividad> actividades, List<Resultado> resultados) {


        this.setStyleName("mantenimientodetalle-form");

        this.alistamientoDetalle = alistamientoDetalle;

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

        detalleField = new TextField("Detalle");
        detalleField = new TextField("Detalle");
        detalleField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        detalleField.setWidth(15, Unit.EM);
        detalleField.setNullRepresentation("");
        detalleField.setMaxLength(200);
        this.addComponent(detalleField);

        alistamientoDetalleBeanFieldGroup = new BeanFieldGroup<>(AlistamientoDetalle.class);
        alistamientoDetalleBeanFieldGroup.setItemDataSource(alistamientoDetalle);
        alistamientoDetalleBeanFieldGroup.bindMemberFields(this);
    }

    public ComboBox getResultadoField() {
        return resultadoField;
    }

    public void setResultadoField(ComboBox resultadoField) {
        this.resultadoField = resultadoField;
    }

    public BeanItemContainer<Resultado> getResultadoBeanItemContainer() {
        return resultadoBeanItemContainer;
    }

    public void setResultadoBeanItemContainer(BeanItemContainer<Resultado> resultadoBeanItemContainer) {
        this.resultadoBeanItemContainer = resultadoBeanItemContainer;
    }

    public ComboBox getActividadField() {
        return actividadField;
    }

    public void setActividadField(ComboBox actividadField) {
        this.actividadField = actividadField;
    }

    public BeanItemContainer<Actividad> getActividadBeanItemContainer() {
        return actividadBeanItemContainer;
    }

    public void setActividadBeanItemContainer(BeanItemContainer<Actividad> actividadBeanItemContainer) {
        this.actividadBeanItemContainer = actividadBeanItemContainer;
    }

    public TextField getDetalleField() {
        return detalleField;
    }

    public void setDetalleField(TextField detalleField) {
        this.detalleField = detalleField;
    }

    public BeanFieldGroup<AlistamientoDetalle> getAlistamientoDetalleBeanFieldGroup() {
        return alistamientoDetalleBeanFieldGroup;
    }

    public void setAlistamientoDetalleBeanFieldGroup(BeanFieldGroup<AlistamientoDetalle> alistamientoDetalleBeanFieldGroup) {
        this.alistamientoDetalleBeanFieldGroup = alistamientoDetalleBeanFieldGroup;
    }

    public AlistamientoDetalle getAlistamientoDetalle() {
        return alistamientoDetalle;
    }

    public void setAlistamientoDetalle(AlistamientoDetalle alistamientoDetalle) {
        this.alistamientoDetalle = alistamientoDetalle;
    }
}
