package com.earandap.vehiculos.ui.views.capacitacion;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.CapacitacionDocumento;
import com.earandap.vehiculos.domain.nomenclador.TipoDocumentoCapacitacion;
import com.earandap.vehiculos.repository.CapacitacionDocumentoRepository;
import com.earandap.vehiculos.repository.TipoDocumentoCapacitacionRepository;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.FinishedListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.themes.ValoTheme;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by felix.batista.
 */
public class CapacitacionDocumentoForm extends FormLayout implements Receiver, FinishedListener {

    private BeanItemContainer<TipoDocumentoCapacitacion> tipoCapacitacionDocumentoBeanItemContainer;

    @PropertyId("tipoDocumentoCapacitacion")
    private ComboBox tipoDocumentoCapacitacionField;

    @PropertyId("rutaDocumento")
    private TextField rutaDocumentoField;

    private Upload upload;
    private File file;
    private String fileName, fileDir, nombreFichero;
    private boolean errorCarga;
    private BeanFieldGroup<CapacitacionDocumento> capacitacionDocumentoBeanFieldGroup;
    private CapacitacionDocumento capacitacionDocumento;
    private CapacitacionDocumentoRepository capacitacionDocumentoRepository;
    private TipoDocumentoCapacitacionRepository tipoDocumentoCapacitacionRepository;
    private Long idCapacitacion;
    private List<String> ficherosCargados;

    public CapacitacionDocumentoForm(boolean adicionar, Long idCapacitacion, CapacitacionDocumento capacitacionDocumento, List<TipoDocumentoCapacitacion> tipoDocumentoCapacitacions, String fileDir, String urlDir) {

        this.setStyleName("seguro-form");
        this.capacitacionDocumento = capacitacionDocumento;
        this.fileDir = fileDir;
        this.idCapacitacion = idCapacitacion;
        this.errorCarga = false;
        this.errorCarga = true;
        this.ficherosCargados = new ArrayList<>();

        tipoCapacitacionDocumentoBeanItemContainer = new BeanItemContainer<>(TipoDocumentoCapacitacion.class);
        tipoCapacitacionDocumentoBeanItemContainer.addAll(tipoDocumentoCapacitacions);

        tipoDocumentoCapacitacionField = new ComboBox("Tipo de Documento");
        tipoDocumentoCapacitacionField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        tipoDocumentoCapacitacionField.setWidth(20, Unit.EM);
        tipoDocumentoCapacitacionField.setItemCaptionPropertyId("descripcion");
        tipoDocumentoCapacitacionField.setContainerDataSource(tipoCapacitacionDocumentoBeanItemContainer);
        tipoDocumentoCapacitacionField.setRequired(true);
        tipoDocumentoCapacitacionField.setRequiredError("El tipo de documento es obligatorio");
        this.addComponent(tipoDocumentoCapacitacionField);
        
        rutaDocumentoField = new TextField("Documento");
        rutaDocumentoField.setNullRepresentation("");
        rutaDocumentoField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        rutaDocumentoField.setRequired(true);
        rutaDocumentoField.setRequiredError("Debe seleccionar un documento");
        rutaDocumentoField.setWidth(20, Unit.EM);
        this.addComponent(rutaDocumentoField);

        upload = new Upload("Archivo", this);
        upload.addFinishedListener(this);
        upload.setImmediate(true);
        upload.setButtonCaption("Seleccionar");
        upload.addStyleName(ValoTheme.BUTTON_PRIMARY);
        upload.addChangeListener(new Upload.ChangeListener() {
            @Override
            public void filenameChanged(Upload.ChangeEvent event) {
                String nombre = StaticMembers.getNombreFichero(event.getFilename());
                setValorRutaFicheroField(nombre);
                if (!ficherosCargados.contains(nombre)) {
                    ficherosCargados.add(nombre);
                }
            }
        });
        this.addComponent(upload);

        capacitacionDocumentoBeanFieldGroup = new BeanFieldGroup<>(CapacitacionDocumento.class);
        capacitacionDocumentoBeanFieldGroup.setItemDataSource(capacitacionDocumento);
        capacitacionDocumentoBeanFieldGroup.bindMemberFields(this);
        capacitacionDocumentoBeanFieldGroup.bindMemberFields(rutaDocumentoField);
        
        this.nombreFichero = rutaDocumentoField.getValue();
        rutaDocumentoField.setReadOnly(true);
    }
    
    private void setValorRutaFicheroField(String valor) {
        this.nombreFichero = valor;
        rutaDocumentoField.setReadOnly(false);
        rutaDocumentoField.setValue(this.nombreFichero);
        rutaDocumentoField.setReadOnly(true);
    }

    public List<String> getFicherosCargados() {
        return ficherosCargados;
    }

    public String getNombreFichero() {
        return nombreFichero;
    }

    public void setNombreFichero(String nombreFichero) {
        this.nombreFichero = nombreFichero;
    }

    public boolean isErrorCarga() {
        return errorCarga;
    }

    public void setErrorCarga(boolean errorCarga) {
        this.errorCarga = errorCarga;
    }

    public TipoDocumentoCapacitacionRepository getTipoDocumentoCapacitacionRepository() {
        return tipoDocumentoCapacitacionRepository;
    }

    public void setTipoDocumentoCapacitacionRepository(TipoDocumentoCapacitacionRepository tipoDocumentoCapacitacionRepository) {
        this.tipoDocumentoCapacitacionRepository = tipoDocumentoCapacitacionRepository;
    }

    public BeanItemContainer<TipoDocumentoCapacitacion> getTipoCapacitacionDocumentoBeanItemContainer() {
        return tipoCapacitacionDocumentoBeanItemContainer;
    }

    public void setTipoCapacitacionDocumentoBeanItemContainer(BeanItemContainer<TipoDocumentoCapacitacion> tipoCapacitacionDocumentoBeanItemContainer) {
        this.tipoCapacitacionDocumentoBeanItemContainer = tipoCapacitacionDocumentoBeanItemContainer;
    }

    public ComboBox getTipoDocumentoCapacitacionField() {
        return tipoDocumentoCapacitacionField;
    }

    public void setTipoDocumentoCapacitacionField(ComboBox tipoDocumentoCapacitacionField) {
        this.tipoDocumentoCapacitacionField = tipoDocumentoCapacitacionField;
    }

    public BeanFieldGroup<CapacitacionDocumento> getCapacitacionDocumentoBeanFieldGroup() {
        return capacitacionDocumentoBeanFieldGroup;
    }

    public void setCapacitacionDocumentoBeanFieldGroup(BeanFieldGroup<CapacitacionDocumento> capacitacionDocumentoBeanFieldGroup) {
        this.capacitacionDocumentoBeanFieldGroup = capacitacionDocumentoBeanFieldGroup;
    }

    public CapacitacionDocumento getCapacitacionDocumento() {
        return capacitacionDocumento;
    }

    public void setCapacitacionDocumento(CapacitacionDocumento capacitacionDocumento) {
        this.capacitacionDocumento = capacitacionDocumento;
    }

    public CapacitacionDocumentoRepository getCapacitacionDocumentoRepository() {
        return capacitacionDocumentoRepository;
    }

    public void setCapacitacionDocumentoRepository(CapacitacionDocumentoRepository capacitacionDocumentoRepository) {
        this.capacitacionDocumentoRepository = capacitacionDocumentoRepository;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.capacitacionDocumento.getId());
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
        final CapacitacionDocumentoForm other = (CapacitacionDocumentoForm) obj;
        if (!Objects.equals(this.capacitacionDocumento.getId(), other.capacitacionDocumento.getId())) {
            return false;
        }
        return true;
    }

    @Override
    public void uploadFinished(FinishedEvent event) {
        if (!errorCarga) {
            StaticMembers.showNotificationMessage("", "El fichero ha sido cargado correctamente");
        }
    }

    @Override
    public OutputStream receiveUpload(String filename, String mimeType) {
        FileOutputStream buffer = null;
        try {
            String rutaCompleta = StaticMembers.construirRutaDocumentoCapacitacion(this.fileDir, filename, this.idCapacitacion);
            buffer = new FileOutputStream(new File(rutaCompleta));
            errorCarga = false;
        } catch (final FileNotFoundException e) {
            errorCarga = true;
            StaticMembers.showNotificationError("Error", "La ruta del documento es incorrecta");
        } catch (final Exception e) {
            errorCarga = true;
            StaticMembers.showNotificationError("Error", "No se puede abrir el archivo");
        }
        return buffer;
    }

}
