package com.earandap.vehiculos.ui.views.incidentes;

import com.earandap.vehiculos.domain.Investigacion;
import com.earandap.vehiculos.domain.Tercero;
import com.earandap.vehiculos.repository.TerceroRepository;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.suggestfield.SuggestField;
import org.vaadin.suggestfield.client.SuggestFieldSuggestion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angel Luis on 11/30/2015.
 */
public class InvestigacionForm extends FormLayout {

    @PropertyId("tercero")
    private SuggestField terceroField;

    @PropertyId("relato")
    private TextArea relatoField;

    @PropertyId("fechaInvestigacion")
    private DateField fechaInvestigacionField;

    @PropertyId("leccion")
    private TextArea leccionAprendidaField;

    @PropertyId("socializada")
    private CheckBox socializadaField;

    @PropertyId("medioDivulgacion")
    private TextField medioDivulgacionField;

    private BeanFieldGroup<Investigacion> investigacionBeanFieldGroup;

    private Investigacion investigacion;

    private TerceroRepository terceroRepository;

    public InvestigacionForm(Investigacion investigacion) {

        this.setStyleName("seguro-form");
        this.investigacion = investigacion;


        //line 1
        HorizontalLayout l1 = new HorizontalLayout();
        l1.setSpacing(true);
        this.addComponent(l1);

        terceroField = new SuggestField();
        terceroField.setWidth(20, Unit.EM);
        terceroField.setCaption("Tercero");
        terceroField.setNewItemsAllowed(false);
        terceroField.setSuggestionHandler(s -> {
            List<Object> result = new ArrayList<>();
            List<Tercero> terceros = terceroRepository.search(s);
            for (Tercero tercero : terceros) {
                result.add(tercero);
            }
            if (result.size() == 0)
                Notification.show("No se encontraron terceros para el criterio introducido", Notification.Type.TRAY_NOTIFICATION);

            return result;
        });
        terceroField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        terceroField.setImmediate(true);
        terceroField.setTokenMode(false);
        terceroField.setSuggestionConverter(new TerceroSuggestionConverter());
        terceroField.setPopupWidth(200);
        terceroField.setRequired(true);
        terceroField.setRequiredError("El tercero es requerido");
        l1.addComponent(terceroField);


        fechaInvestigacionField = new DateField("Fecha Investigaci\u00F3n");
        fechaInvestigacionField.addStyleName(ValoTheme.DATEFIELD_TINY);
        fechaInvestigacionField.setWidth(12, Unit.EM);
        fechaInvestigacionField.setRequired(true);
        fechaInvestigacionField.setRequiredError("La fecha es requerida");
        l1.addComponent(fechaInvestigacionField);

        //line 2
        HorizontalLayout l2 = new HorizontalLayout();
        l1.setSpacing(true);
        this.addComponent(l2);

        relatoField = new TextArea("Relato");
        relatoField.addStyleName(ValoTheme.TEXTAREA_TINY);
        relatoField.setNullRepresentation("");
        relatoField.setColumns(40);
        relatoField.setRows(5);
        relatoField.setMaxLength(8000);
        relatoField.setRequired(true);
        relatoField.setRequiredError("El relato es requerido");
        l2.addComponent(relatoField);
        
        //line 3
        HorizontalLayout l3 = new HorizontalLayout();
        l3.setSpacing(true);
        this.addComponent(l3);
        
        leccionAprendidaField = new TextArea("Lecci\u00F3n Aprendida");
        leccionAprendidaField.addStyleName(ValoTheme.TEXTAREA_TINY);
        leccionAprendidaField.setNullRepresentation("");
        leccionAprendidaField.setColumns(40);
        leccionAprendidaField.setRows(5);
        leccionAprendidaField.setMaxLength(500);
        l3.addComponent(leccionAprendidaField);
        
        //line 4
        HorizontalLayout l4 = new HorizontalLayout();
        l4.setSpacing(true);
        this.addComponent(l4);
        
        socializadaField = new CheckBox("Socializada/Divulgada");
        socializadaField.addStyleName(ValoTheme.CHECKBOX_SMALL);
        socializadaField.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                if (socializadaField.getValue()) {
                    medioDivulgacionField.focus();
                    medioDivulgacionField.selectAll();
                } else {
                    medioDivulgacionField.setValue("");
                }
            }
        });
        l4.addComponent(socializadaField);
        
        //line 5
        HorizontalLayout l5 = new HorizontalLayout();
        l5.setSpacing(true);
        this.addComponent(l5);
        
        medioDivulgacionField = new TextField("Medio de Divulgaci\u00F3n");
        medioDivulgacionField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        medioDivulgacionField.setNullRepresentation("");
        medioDivulgacionField.setWidth(40, Unit.EM);
        medioDivulgacionField.setMaxLength(150);
        l5.addComponent(medioDivulgacionField);

        investigacionBeanFieldGroup = new BeanFieldGroup<>(Investigacion.class);
        investigacionBeanFieldGroup.setItemDataSource(investigacion);
        investigacionBeanFieldGroup.bindMemberFields(this);
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

    public TerceroRepository getTerceroRepository() {
        return terceroRepository;
    }

    public void setTerceroRepository(TerceroRepository terceroRepository) {
        this.terceroRepository = terceroRepository;
    }

    public BeanFieldGroup<Investigacion> getInvestigacionBeanFieldGroup() {
        return investigacionBeanFieldGroup;
    }

    public void setInvestigacionBeanFieldGroup(BeanFieldGroup<Investigacion> investigacionBeanFieldGroup) {
        this.investigacionBeanFieldGroup = investigacionBeanFieldGroup;
    }
}
