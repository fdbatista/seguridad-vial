package com.earandap.vehiculos.ui.components;

import com.earandap.vehiculos.domain.Municipios;
import com.earandap.vehiculos.domain.Persona;
import com.earandap.vehiculos.domain.nomenclador.Municipio;
import com.earandap.vehiculos.domain.nomenclador.Sexo;
import com.earandap.vehiculos.domain.nomenclador.TipoDocumento;
import com.earandap.vehiculos.domain.nomenclador.TipoSangre;
import com.earandap.vehiculos.repository.MunicipiosRepository;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.suggestfield.SuggestField;
import org.vaadin.suggestfield.client.SuggestFieldSuggestion;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angel Luis on 10/4/2015.
 */
@org.springframework.stereotype.Component
@Scope("prototype")
public class PersonaForm extends VerticalLayout {

    @PropertyId("tipoDocumento")
    private ComboBox tipoDocumentoField;
    private BeanItemContainer<TipoDocumento> tipoDocumentoContainer;

    @PropertyId("documento")
    private TextField documentoField;

    private Button buscarTerceroButton;

    private Button limpiarTerceroButton;

    @PropertyId("correo")
    private TextField correoField;

    @PropertyId("telefono")
    private TextField telefonoField;

    @PropertyId("direccion")
    private TextField direccionField;

    @PropertyId("observacion")
    private TextField observacionField;

    @PropertyId("primerNombre")
    private TextField primerNombreField;

    @PropertyId("segundoNombre")
    private TextField segundoNombreField;

    @PropertyId("primerApellido")
    private TextField primerApellidoField;

    @PropertyId("segundoApellido")
    private TextField segundoApellidoField;

    @PropertyId("sexo")
    private ComboBox sexoField;
    private BeanItemContainer<Sexo> sexoContainer;

    @PropertyId("fechaNacimiento")
    private DateField fechaNacimientoField;

    @PropertyId("foto")
    private Image fotoField;

    @PropertyId("celular")
    private TextField celularField;

//    @PropertyId("lugarExpedicionCedula")
//    private ComboBox lugarExpedicionCedulaField;
//    private BeanItemContainer<Municipio> lugarExpedicionCedulaBeanItemContainer;

    @PropertyId("lugarExpedicionCedula")
    //private ComboBox municipiosField;
    //private BeanItemContainer<Municipios> municipiosBeanItemContainer;
    private SuggestField lugarExpedicionCedulaField;

//    @PropertyId("municipio")
//    private ComboBox municipioField;
//    private BeanItemContainer<Municipio> municipioBeanItemContainer;

    @PropertyId("municipios")
    //private ComboBox municipiosField;
    //private BeanItemContainer<Municipios> municipiosBeanItemContainer;
    private SuggestField municipiosField;

    @PropertyId("tipoSangre")
    private ComboBox tipoSangreField;
    private BeanItemContainer<TipoSangre> tipoSangreBeanItemContainer;

    private PersonaEvent personaEvent;

    @Autowired
    private MunicipiosRepository municipiosRepository;

    public void updatePersona(PersonaEvent buscarPersona){
        this.personaEvent = buscarPersona;
    }

    public void esconderBotonesBusqueda(boolean esconder){
        buscarTerceroButton.setVisible(esconder);
        limpiarTerceroButton.setVisible(esconder);
    }

    @PostConstruct
    public void init() {

        HorizontalLayout l1 = new HorizontalLayout();
        l1.setSpacing(true);
        this.addComponent(l1);

        tipoDocumentoContainer = new BeanItemContainer<>(TipoDocumento.class);

        tipoDocumentoField = new ComboBox("Tipo Documento");
        tipoDocumentoField.addStyleName(ValoTheme.COMBOBOX_TINY);
        tipoDocumentoField.setWidth(9, Unit.EM);
        tipoDocumentoField.setContainerDataSource(tipoDocumentoContainer);
        tipoDocumentoField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        tipoDocumentoField.setItemCaptionPropertyId("descripcion");
        tipoDocumentoField.setRequired(true);
        tipoDocumentoField.setRequiredError("El tipo de documento es requerido");
        tipoDocumentoField.setNullSelectionAllowed(false);
        tipoDocumentoField.setScrollToSelectedItem(true);
        l1.addComponent(tipoDocumentoField);

        documentoField = new TextField("No. Documento");
        documentoField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        documentoField.setWidth(9, Unit.EM);
        documentoField.setNullRepresentation("");
        documentoField.setRequired(true);
        documentoField.setMaxLength(30);
        documentoField.setRequiredError("El documento es requerido");
        l1.addComponent(documentoField);

        buscarTerceroButton = new Button();
        buscarTerceroButton.setIcon(FontAwesome.SEARCH);
        buscarTerceroButton.addStyleName(ValoTheme.BUTTON_SMALL);
        buscarTerceroButton.addStyleName("button-search-form");
        l1.addComponent(buscarTerceroButton);
        buscarTerceroButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Persona persona = personaEvent.buscar((TipoDocumento) tipoDocumentoField.getValue(),documentoField.getValue());
                if (persona.getTipoDocumento() == null) {
                    Notification notification = new Notification("Notificaci\u00F3n", "No se encuentra la persona con esos Datos", Notification.Type.WARNING_MESSAGE);
                    notification.setDelayMsec(3000);
                    notification.setIcon(FontAwesome.INFO_CIRCLE);
                    notification.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
                    notification.show(Page.getCurrent());
                }
            }
        });
        limpiarTerceroButton = new Button("Limpiar");
        //limpiarTerceroButton.setIcon(FontAwesome.SEARCH);
        limpiarTerceroButton.addStyleName(ValoTheme.BUTTON_SMALL);
        limpiarTerceroButton.addStyleName("button-search-form");
        l1.addComponent(limpiarTerceroButton);
        limpiarTerceroButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                mostrarBotonLimpiar(false);
                habilitarDocumentos(true);
                Persona persona = personaEvent.buscar(null,null);
            }
        });


        HorizontalLayout l2 = new HorizontalLayout();
        l2.setSpacing(true);
        this.addComponent(l2);

        primerNombreField = new TextField("1er Nombre");
        primerNombreField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        primerNombreField.setWidth(9, Unit.EM);
        primerNombreField.setNullRepresentation("");
        primerNombreField.setRequired(true);
        primerNombreField.setMaxLength(50);
        primerNombreField.setRequiredError("El nombre es requerido");
        l2.addComponent(primerNombreField);

        segundoNombreField = new TextField("2do Nombre");
        segundoNombreField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        segundoNombreField.setWidth(9, Unit.EM);
        segundoNombreField.setNullRepresentation("");
        segundoNombreField.setMaxLength(50);
        l2.addComponent(segundoNombreField);

        primerApellidoField = new TextField("1er Apellido");
        primerApellidoField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        primerApellidoField.setWidth(9, Unit.EM);
        primerApellidoField.setNullRepresentation("");
        primerApellidoField.setRequired(true);
        primerApellidoField.setMaxLength(50);
        primerApellidoField.setRequiredError("El apellido es requerido");
        l2.addComponent(primerApellidoField);

        segundoApellidoField = new TextField("2do Apellido");
        segundoApellidoField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        segundoApellidoField.setWidth(9, Unit.EM);
        segundoApellidoField.setNullRepresentation("");
        segundoApellidoField.setMaxLength(50);
        l2.addComponent(segundoApellidoField);

        HorizontalLayout l3 = new HorizontalLayout();
        l3.setSpacing(true);
        this.addComponent(l3);

        celularField = new TextField("Celular");
        celularField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        celularField.setWidth(12, Unit.EM);
        celularField.setNullRepresentation("");
        primerApellidoField.setMaxLength(20);
        l3.addComponent(celularField);

        telefonoField = new TextField("Telefono Fijo");
        telefonoField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        telefonoField.setWidth(12, Unit.EM);
        telefonoField.setNullRepresentation("");
        primerApellidoField.setMaxLength(20);
        l3.addComponent(telefonoField);

        sexoContainer = new BeanItemContainer<>(Sexo.class);
        sexoField = new ComboBox("Sexo");
        sexoField.addStyleName(ValoTheme.COMBOBOX_TINY);
        sexoField.setWidth(9, Unit.EM);
        sexoField.setContainerDataSource(sexoContainer);
        sexoField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        sexoField.setItemCaptionPropertyId("descripcion");
        sexoField.setRequired(true);
        sexoField.setRequiredError("El sexo es requerido");
        sexoField.setNullSelectionAllowed(false);
        sexoField.setScrollToSelectedItem(true);
        l3.addComponent(sexoField);

        tipoSangreBeanItemContainer = new BeanItemContainer<>(TipoSangre.class);
        tipoSangreField = new ComboBox("Tipo sangre");
        tipoSangreField.addStyleName(ValoTheme.COMBOBOX_TINY);
        tipoSangreField.setWidth(9, Unit.EM);
        tipoSangreField.setContainerDataSource(tipoSangreBeanItemContainer);
        tipoSangreField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        tipoSangreField.setItemCaptionPropertyId("descripcion");
        tipoSangreField.setNullSelectionAllowed(false);
        tipoSangreField.setScrollToSelectedItem(true);
        l3.addComponent(tipoSangreField);

        HorizontalLayout l4 = new HorizontalLayout();
        l4.setSpacing(true);
        this.addComponent(l4);

        direccionField = new TextField("Direccion de Residencia");
        direccionField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        direccionField.setWidth(35, Unit.EM);
        direccionField.setNullRepresentation("");
        direccionField.setMaxLength(100);
        l4.addComponent(direccionField);

//        municipioBeanItemContainer = new BeanItemContainer<>(Municipio.class);
//        municipioField = new ComboBox("Municipio");
//        municipioField.addStyleName(ValoTheme.COMBOBOX_TINY);
//        municipioField.setWidth(9, Unit.EM);
//        municipioField.setContainerDataSource(municipioBeanItemContainer);
//        municipioField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
//        municipioField.setItemCaptionPropertyId("descripcion");
//        municipioField.setNullSelectionAllowed(false);
//        municipioField.setScrollToSelectedItem(true);
        //l4.addComponent(municipioField);

//        municipiosBeanItemContainer = new BeanItemContainer<>(Municipios.class);
//        municipiosField = new ComboBox("Municipios");
//        municipiosField.addStyleName(ValoTheme.COMBOBOX_TINY);
//        municipiosField.setWidth(9, Unit.EM);
//        municipiosField.setContainerDataSource(municipiosBeanItemContainer);
//        municipiosField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
//        municipiosField.setItemCaptionPropertyId("nombre");
//        municipiosField.setNullSelectionAllowed(false);
//       municipiosField.setScrollToSelectedItem(true);
        municipiosField = new SuggestField();
        municipiosField.setWidth(12, Unit.EM);
        municipiosField.setCaption("Municipios");
        municipiosField.setNewItemsAllowed(false);
        municipiosField.setSuggestionHandler(s -> {
            List<Object> result = new ArrayList<>();
            List<Municipios> municipios = municipiosRepository.search(s);
            for (Municipios municipio : municipios) {
                result.add(municipio);
            }
            if (result.size() == 0)
                Notification.show("No se encontraron mnicipios para el criterio introducido", Notification.Type.TRAY_NOTIFICATION);

            return result;
        });
        municipiosField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        municipiosField.setImmediate(true);
        municipiosField.setTokenMode(false);
        municipiosField.setSuggestionConverter(new MunicipioSuggestionConverter());
        municipiosField.setPopupWidth(400);
        l4.addComponent(municipiosField);

        HorizontalLayout l5 = new HorizontalLayout();
        l5.setSpacing(true);
        this.addComponent(l5);

        correoField = new TextField("Correo");
        correoField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        correoField.setWidth(35, Unit.EM);
        correoField.setNullRepresentation("");
        l5.addComponent(correoField);

        fechaNacimientoField = new DateField("Fecha Nac.");
        fechaNacimientoField.addStyleName(ValoTheme.DATEFIELD_TINY);
        fechaNacimientoField.setWidth(9, Unit.EM);
        l5.addComponent(fechaNacimientoField);

        HorizontalLayout l6 = new HorizontalLayout();
        l6.setSpacing(true);
        this.addComponent(l6);

        observacionField = new TextField("Observaciones");
        observacionField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        observacionField.setWidth(35, Unit.EM);
        observacionField.setNullRepresentation("");
        observacionField.setMaxLength(200);
        l6.addComponent(observacionField);

//        lugarExpedicionCedulaBeanItemContainer = new BeanItemContainer<>(Municipio.class);
//        lugarExpedicionCedulaField = new ComboBox("Lugar Exp. Cedula");
//        lugarExpedicionCedulaField.addStyleName(ValoTheme.COMBOBOX_TINY);
//        lugarExpedicionCedulaField.setWidth(9, Unit.EM);
//        lugarExpedicionCedulaField.setContainerDataSource(lugarExpedicionCedulaBeanItemContainer);
//        lugarExpedicionCedulaField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
//        lugarExpedicionCedulaField.setItemCaptionPropertyId("descripcion");
//        lugarExpedicionCedulaField.setNullSelectionAllowed(false);
//        lugarExpedicionCedulaField.setScrollToSelectedItem(true);
        lugarExpedicionCedulaField = new SuggestField();
        lugarExpedicionCedulaField.setWidth(12, Unit.EM);
        lugarExpedicionCedulaField.setCaption("Lugar Exp. Cedula");
        lugarExpedicionCedulaField.setNewItemsAllowed(false);
        lugarExpedicionCedulaField.setSuggestionHandler(s -> {
            List<Object> result = new ArrayList<>();
            List<Municipios> municipios = municipiosRepository.search(s);
            for (Municipios municipio : municipios) {
                result.add(municipio);
            }
            if (result.size() == 0)
                Notification.show("No se encontraron mnicipios para el criterio introducido", Notification.Type.TRAY_NOTIFICATION);

            return result;
        });
        lugarExpedicionCedulaField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        lugarExpedicionCedulaField.setImmediate(true);
        lugarExpedicionCedulaField.setTokenMode(false);
        lugarExpedicionCedulaField.setSuggestionConverter(new MunicipioSuggestionConverter());
        lugarExpedicionCedulaField.setPopupWidth(400);
        l6.addComponent(lugarExpedicionCedulaField);


    }

    public void updateTipoDocumentoContainer(List<TipoDocumento> tipoDocumentos){
        tipoDocumentoContainer.removeAllItems();
        tipoDocumentoContainer.addAll(tipoDocumentos);
    }

    public void updateSexoContainer(List<Sexo> sexos){
        sexoContainer.removeAllItems();
        sexoContainer.addAll(sexos);
    }

    public void updateTipoSangreContainer(List<TipoSangre> tiposSangre){
        tipoSangreBeanItemContainer.removeAllItems();
        tipoSangreBeanItemContainer.addAll(tiposSangre);
    }

    public void updatelugarExpedicionCedulaContainer(List<Municipio> municipios){
        //lugarExpedicionCedulaBeanItemContainer.removeAllItems();
       // lugarExpedicionCedulaBeanItemContainer.addAll(municipios);
    }
    public void updateMunicipioContainer(List<Municipio> municipios){
        //municipioBeanItemContainer.removeAllItems();
        //municipioBeanItemContainer.addAll(municipios);
    }

    public void updateMunicipiosContainer(List<Municipios> municipios){
        //municipiosBeanItemContainer.removeAllItems();
        //municipiosBeanItemContainer.addAll(municipios);
    }

    public void habilitarDocumentos(boolean valor) {
        tipoDocumentoField.setEnabled(valor);
        documentoField.setEnabled(valor);
        buscarTerceroButton.setVisible(valor);
    }
    public void mostrarBotonLimpiar(boolean valor) {
        limpiarTerceroButton.setVisible(valor);
    }

    public interface PersonaEvent{
        public Persona buscar(TipoDocumento tipoDocumento,String documento);
    }

    public class MunicipioSuggestionConverter implements SuggestField.SuggestionConverter {

        @Override
        public SuggestFieldSuggestion toSuggestion(Object item) {
            if (item != null) {
                Municipios municipio = (Municipios) item;
                return new SuggestFieldSuggestion(String.valueOf(municipio.getId()), municipio.getNombre() + ", " + municipio.getDepartamento().getNombre(), municipio.getNombre() + ", " + municipio.getDepartamento().getNombre());
            }
            return null;
        }

        @Override
        public Object toItem(SuggestFieldSuggestion suggestion) {
            return municipiosRepository.findOne(Long.valueOf(suggestion.getId()));
        }
    }
}
