package com.earandap.vehiculos.ui.components;

import com.earandap.vehiculos.domain.nomenclador.TipoDocumento;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

/**
 * Created by Angel Luis on 10/4/2015.
 */
 @org.springframework.stereotype.Component
 @Scope("prototype")
public class TerceroForm extends VerticalLayout{

    private ComboBox tipoDocumentoField;
    private BeanItemContainer<TipoDocumento> tipoDocumentoContainer;

    private TextField documentoField;

    private Button buscarTerceroButton;



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
        tipoDocumentoField.setNullSelectionAllowed(false);
        tipoDocumentoField.setScrollToSelectedItem(true);
        l1.addComponent(tipoDocumentoField);


        documentoField = new TextField("No. Documento");
        documentoField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        documentoField.setWidth(9, Unit.EM);
        l1.addComponent(documentoField);

        buscarTerceroButton = new Button("Buscar");
        buscarTerceroButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        l1.addComponent(buscarTerceroButton);
    }

    public void updateTipoDocumentoContainer(BeanItemContainer<TipoDocumento> container){
        tipoDocumentoContainer = container;
    }



}
