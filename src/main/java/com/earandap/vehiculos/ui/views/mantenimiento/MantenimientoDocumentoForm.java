package com.earandap.vehiculos.ui.views.mantenimiento;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.MantenimientoDocumento;
import com.earandap.vehiculos.repository.MantenimientoDocumentoRepository;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
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
public class MantenimientoDocumentoForm extends FormLayout implements Receiver, FinishedListener {

    @PropertyId("rutaDocumento")
    private TextField rutaDocumentoField;

    private Upload upload;
    private String urlDir, fileDir, nombreFichero;
    private boolean errorCarga;
    private BeanFieldGroup<MantenimientoDocumento> mantenimientoDocumentoBeanFieldGroup;
    private MantenimientoDocumento mantenimientoDocumento;
    private MantenimientoDocumentoRepository mantenimientoDocumentoRepository;
    private Long idMantenimiento;
    private List<String> ficherosCargados;

    public MantenimientoDocumentoForm(boolean adicionar, Long idMantenimiento, MantenimientoDocumento mantenimientoDocumento, String fileDir, String urlDir) {

        this.setStyleName("seguro-form");
        this.mantenimientoDocumento = mantenimientoDocumento;
        this.urlDir = urlDir;
        this.fileDir = fileDir;
        this.idMantenimiento = idMantenimiento;
        this.errorCarga = true;
        this.ficherosCargados = new ArrayList<>();

        rutaDocumentoField = new TextField("Documento");
        rutaDocumentoField.setNullRepresentation("");
        rutaDocumentoField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        rutaDocumentoField.setRequired(true);
        rutaDocumentoField.setRequiredError("Debe seleccionar un documento");
        rutaDocumentoField.setWidth(20, Unit.EM);
        this.addComponent(rutaDocumentoField);

        upload = new Upload("Archivo", this);
        upload.addFinishedListener(this);
        upload.setButtonCaption("Seleccionar");
        upload.setImmediate(true);
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

        mantenimientoDocumentoBeanFieldGroup = new BeanFieldGroup<>(MantenimientoDocumento.class);
        mantenimientoDocumentoBeanFieldGroup.setItemDataSource(mantenimientoDocumento);
        mantenimientoDocumentoBeanFieldGroup.bindMemberFields(this);
        mantenimientoDocumentoBeanFieldGroup.bindMemberFields(rutaDocumentoField);
        
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

    public BeanFieldGroup<MantenimientoDocumento> getMantenimientoDocumentoBeanFieldGroup() {
        return mantenimientoDocumentoBeanFieldGroup;
    }

    public void setMantenimientoDocumentoBeanFieldGroup(BeanFieldGroup<MantenimientoDocumento> mantenimientoDocumentoBeanFieldGroup) {
        this.mantenimientoDocumentoBeanFieldGroup = mantenimientoDocumentoBeanFieldGroup;
    }

    public MantenimientoDocumento getMantenimientoDocumento() {
        return mantenimientoDocumento;
    }

    public void setMantenimientoDocumento(MantenimientoDocumento mantenimientoDocumento) {
        this.mantenimientoDocumento = mantenimientoDocumento;
    }

    public MantenimientoDocumentoRepository getMantenimientoDocumentoRepository() {
        return mantenimientoDocumentoRepository;
    }

    public void setMantenimientoDocumentoRepository(MantenimientoDocumentoRepository mantenimientoDocumentoRepository) {
        this.mantenimientoDocumentoRepository = mantenimientoDocumentoRepository;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.mantenimientoDocumento.getId());
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
        final MantenimientoDocumentoForm other = (MantenimientoDocumentoForm) obj;
        if (!Objects.equals(this.mantenimientoDocumento.getId(), other.mantenimientoDocumento.getId())) {
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
            String rutaCompleta = StaticMembers.construirRutaDocumentoMantenimiento(this.fileDir, filename, this.idMantenimiento);
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
