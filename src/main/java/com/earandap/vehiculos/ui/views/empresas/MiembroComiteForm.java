package com.earandap.vehiculos.ui.views.empresas;

import com.earandap.vehiculos.domain.MiembroComite;
import com.earandap.vehiculos.domain.Persona;
import com.earandap.vehiculos.domain.Tercero;
import com.earandap.vehiculos.repository.MiembroComiteRepository;
import com.earandap.vehiculos.repository.PersonaRepository;
import com.earandap.vehiculos.repository.TerceroRepository;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.suggestfield.SuggestField;
import org.vaadin.suggestfield.client.SuggestFieldSuggestion;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Angel Luis on 11/18/2015.
 */
public class MiembroComiteForm extends FormLayout {

    /*@PropertyId("miembro")
    private ComboBox personaField;*/
    private BeanItemContainer<Persona> personaBeanItemContainer;

    @PropertyId("miembro")
    private SuggestField terceroField;

    @PropertyId("cargo")
    private TextField cargoField;

    @PropertyId("esResponsable")
    private CheckBox esResponsableField;

    private BeanFieldGroup<MiembroComite> miembroComiteBeanFieldGroup;

    private MiembroComite miembroComite;

    private PersonaRepository personaRepository;
    
    @Autowired
    private TerceroRepository terceroRepository;

    private MiembroComiteRepository miembroComiteRepository;

    public MiembroComiteForm(MiembroComite miembroComite) {

        this.setStyleName("seguro-form");
        this.miembroComite = miembroComite;
        personaBeanItemContainer = new BeanItemContainer<>(Persona.class);
        
        terceroField = new SuggestField();
        terceroField.setWidth(25, Unit.EM);
        terceroField.setCaption("Tercero");
        terceroField.setNewItemsAllowed(false);
        terceroField.setSuggestionHandler(s -> {
            List<Object> result = new ArrayList<>();
            List<Tercero> terceros = terceroRepository.search(s);
            for (Tercero tercero : terceros) {
                result.add(tercero);
            }
            if (result.size() == 0)
                Notification.show("No se encontraron personas con el criterio introducido", Notification.Type.TRAY_NOTIFICATION);

            return result;
        });
        terceroField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        terceroField.setImmediate(true);
        terceroField.setTokenMode(false);
        terceroField.setSuggestionConverter(new TerceroSuggestionConverter());
        terceroField.setPopupWidth(200);
        terceroField.setRequired(true);
        terceroField.setRequiredError("La persona es requerida");
        this.addComponent(terceroField);
        
        cargoField = new TextField("Cargo");
        cargoField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        cargoField.setNullRepresentation("");
        cargoField.setWidth(25, Unit.EM);
        this.addComponent(cargoField);

        esResponsableField = new CheckBox("Responsable");
        esResponsableField.addStyleName(ValoTheme.CHECKBOX_SMALL);
        this.addComponent(esResponsableField);

        miembroComiteBeanFieldGroup = new BeanFieldGroup<>(MiembroComite.class);
        miembroComiteBeanFieldGroup.setItemDataSource(miembroComite);
        miembroComiteBeanFieldGroup.bindMemberFields(this);
       
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

    public BeanItemContainer<Persona> getPersonaBeanItemContainer() {
        return personaBeanItemContainer;
    }

    public void setPersonaBeanItemContainer(BeanItemContainer<Persona> personaBeanItemContainer) {
        this.personaBeanItemContainer = personaBeanItemContainer;
    }

    public TextField getCargoField() {
        return cargoField;
    }

    public void setCargoField(TextField cargoField) {
        this.cargoField = cargoField;
    }

    public CheckBox getEsResponsableField() {
        return esResponsableField;
    }

    public void setEsResponsableField(CheckBox esResponsableField) {
        this.esResponsableField = esResponsableField;
    }

    public BeanFieldGroup<MiembroComite> getMiembroComiteBeanFieldGroup() {
        return miembroComiteBeanFieldGroup;
    }

    public void setMiembroComiteBeanFieldGroup(BeanFieldGroup<MiembroComite> miembroComiteBeanFieldGroup) {
        this.miembroComiteBeanFieldGroup = miembroComiteBeanFieldGroup;
    }

    public MiembroComite getMiembroComite() {
        return miembroComite;
    }

    public void setMiembroComite(MiembroComite miembroComite) {
        this.miembroComite = miembroComite;
    }

    public PersonaRepository getPersonaRepository() {
        return personaRepository;
    }

    public void setPersonaRepository(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    public MiembroComiteRepository getMiembroComiteRepository() {
        return miembroComiteRepository;
    }

    public void setMiembroComiteRepository(MiembroComiteRepository miembroComiteRepository) {
        this.miembroComiteRepository = miembroComiteRepository;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.miembroComite.getId());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MiembroComiteForm other = (MiembroComiteForm) obj;
        if (!Objects.equals(this.miembroComite.getId(), other.miembroComite.getId())) {
            return false;
        }
        return true;
    }

    public SuggestField getTerceroField() {
        return terceroField;
    }

    public void setTerceroField(SuggestField terceroField) {
        this.terceroField = terceroField;
    }

    public TerceroRepository getTerceroRepository() {
        return terceroRepository;
    }

    public void setTerceroRepository(TerceroRepository terceroRepository) {
        this.terceroRepository = terceroRepository;
    }
    
    

}
