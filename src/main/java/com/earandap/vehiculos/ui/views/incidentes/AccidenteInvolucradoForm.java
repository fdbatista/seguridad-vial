package com.earandap.vehiculos.ui.views.incidentes;

import com.earandap.vehiculos.domain.AccidenteInvolucrado;
import com.earandap.vehiculos.domain.Persona;
import com.earandap.vehiculos.repository.PersonaRepository;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.suggestfield.SuggestField;
import org.vaadin.suggestfield.client.SuggestFieldSuggestion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angel Luis on 12/2/2015.
 */
public class AccidenteInvolucradoForm extends FormLayout {

    @PropertyId("involucrado")
    private SuggestField involucradoField;

    @PropertyId("responsable")
    private CheckBox responsableField;

    private BeanFieldGroup<AccidenteInvolucrado> accidenteInvolucradoBeanFieldGroup;

    private AccidenteInvolucrado accidenteInvolucrado;

    private PersonaRepository personaRepository;


    public PersonaRepository getPersonaRepository() {
        return personaRepository;
    }

    public void setPersonaRepository(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    public BeanFieldGroup<AccidenteInvolucrado> getAccidenteInvolucradoBeanFieldGroup() {
        return accidenteInvolucradoBeanFieldGroup;
    }

    public void setAccidenteInvolucradoBeanFieldGroup(BeanFieldGroup<AccidenteInvolucrado> accidenteInvolucradoBeanFieldGroup) {
        this.accidenteInvolucradoBeanFieldGroup = accidenteInvolucradoBeanFieldGroup;
    }

    public AccidenteInvolucradoForm(AccidenteInvolucrado accidenteInvolucrado) {

        this.setStyleName("seguro-form");
        this.setWidth(700, Unit.PIXELS);
        this.setHeight(500, Unit.PIXELS);
        this.accidenteInvolucrado = accidenteInvolucrado;


        //line 1
        HorizontalLayout l1 = new HorizontalLayout();
        l1.setSpacing(true);
        this.addComponent(l1);

        involucradoField = new SuggestField();
        involucradoField.setWidth(20, Unit.EM);
        involucradoField.setCaption("Persona");
        involucradoField.setNewItemsAllowed(false);
        involucradoField.setSuggestionHandler(s -> {
            List<Object> result = new ArrayList<>();
            List<Persona> all = personaRepository.findAll();
            List<Persona> involucrados = personaRepository.search(s);
            for (Persona persona : involucrados) {
                result.add(persona);
            }
            if (result.size() == 0)
                Notification.show("No se encontraron personas para el criterio introducido", Notification.Type.TRAY_NOTIFICATION);
            return result;
        });
        involucradoField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        involucradoField.setImmediate(true);
        involucradoField.setTokenMode(false);
        involucradoField.setSuggestionConverter(new ConductorSuggestionConverter());
        involucradoField.setPopupWidth(300);
        involucradoField.setRequired(true);
        involucradoField.setRequiredError("La persona es requerida");
        l1.addComponent(involucradoField);


        //line 2
        HorizontalLayout l2 = new HorizontalLayout();
        l2.setSpacing(true);
        this.addComponent(l2);

        responsableField = new CheckBox("Responsable");
        responsableField.addStyleName(ValoTheme.CHECKBOX_SMALL);
        responsableField.setStyleName("fixed-checkbox");
        l2.addComponent(responsableField);

        accidenteInvolucradoBeanFieldGroup = new BeanFieldGroup<>(AccidenteInvolucrado.class);
        accidenteInvolucradoBeanFieldGroup.setItemDataSource(accidenteInvolucrado);
        accidenteInvolucradoBeanFieldGroup.bindMemberFields(this);
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

}
