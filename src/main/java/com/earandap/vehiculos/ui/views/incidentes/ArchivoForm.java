package com.earandap.vehiculos.ui.views.incidentes;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.Archivo;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FinishedListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.themes.ValoTheme;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;

public class ArchivoForm extends FormLayout implements Receiver, FinishedListener {

    @PropertyId("fecha")
    private DateField fecha;
    
    @PropertyId("comentario")
    private TextArea comentario;
    
    @PropertyId("url")
    private TextField rutaDocumentoField;
    
    private Upload upload;
    private BeanFieldGroup<Archivo> incidenteArchivoBeanFieldGroup;
    private Archivo archivo;
    private String urlDir, fileDir, nombreFichero;
    private boolean errorCarga, adicionar;
    private Long idIncidente;
    private List<String> ficherosCargados;
    
    public ArchivoForm(boolean adicionar, Long idIncidente, Archivo archivo, String fileDir, String urlDir) {

        this.addStyleName("archivo-form");
        this.archivo = archivo;
        this.fileDir = fileDir;
        this.urlDir = urlDir;
        this.idIncidente = idIncidente;
        this.adicionar = adicionar;
        this.ficherosCargados = new ArrayList<>();

        fecha = new DateField("Fecha");
        fecha.setRangeEnd(new Date());
        StaticMembers.setErrorRangoEspanhol(fecha);
        this.addComponent(fecha);

        comentario = new TextArea("Comentarios");
        comentario.setNullRepresentation("");
        comentario.setColumns(20);
        comentario.setRows(2);
        this.addComponent(comentario);
        
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

        incidenteArchivoBeanFieldGroup = new BeanFieldGroup<>(Archivo.class);
        incidenteArchivoBeanFieldGroup.setItemDataSource(archivo);
        incidenteArchivoBeanFieldGroup.bindMemberFields(this);
        
        this.nombreFichero = rutaDocumentoField.getValue();
        rutaDocumentoField.setReadOnly(true);
    }

    public DateField getFecha() {
        return fecha;
    }

    public void setFecha(DateField fecha) {
        this.fecha = fecha;
    }

    public TextArea getComentario() {
        return comentario;
    }

    public void setComentario(TextArea comentario) {
        this.comentario = comentario;
    }

    public TextField getRutaDocumentoField() {
        return rutaDocumentoField;
    }

    public void setRutaDocumentoField(TextField rutaDocumentoField) {
        this.rutaDocumentoField = rutaDocumentoField;
    }

    public Upload getUpload() {
        return upload;
    }

    public void setUpload(Upload upload) {
        this.upload = upload;
    }

    public Archivo getArchivo() {
        return archivo;
    }

    public void setArchivo(Archivo archivo) {
        this.archivo = archivo;
    }

    public String getUrlDir() {
        return urlDir;
    }

    public void setUrlDir(String urlDir) {
        this.urlDir = urlDir;
    }

    public String getFileDir() {
        return fileDir;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
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

    public boolean isAdicionar() {
        return adicionar;
    }

    public void setAdicionar(boolean adicionar) {
        this.adicionar = adicionar;
    }

    public Long getIdIncidente() {
        return idIncidente;
    }

    public void setIdIncidente(Long idIncidente) {
        this.idIncidente = idIncidente;
    }

    public List<String> getFicherosCargados() {
        return ficherosCargados;
    }

    public void setFicherosCargados(List<String> ficherosCargados) {
        this.ficherosCargados = ficherosCargados;
    }

    private void setValorRutaFicheroField(String valor) {
        this.nombreFichero = valor;
        rutaDocumentoField.setReadOnly(false);
        rutaDocumentoField.setValue(this.nombreFichero);
        rutaDocumentoField.setReadOnly(true);
    }

    public BeanFieldGroup<Archivo> getIncidenteArchivoBeanFieldGroup() {
        return incidenteArchivoBeanFieldGroup;
    }

    public void setIncidenteArchivoBeanFieldGroup(BeanFieldGroup<Archivo> incidenteArchivoBeanFieldGroup) {
        this.incidenteArchivoBeanFieldGroup = incidenteArchivoBeanFieldGroup;
    }

    @Override
    public void uploadFinished(Upload.FinishedEvent event) {
        if (!errorCarga) {
            archivo.setFechaCreado(new Date());
            StaticMembers.showNotificationMessage("", "El fichero ha sido cargado correctamente");
        }
    }

    @Override
    public OutputStream receiveUpload(String filename, String mimeType) {
        FileOutputStream buffer = null;
        try {
            String rutaCompleta = StaticMembers.construirRutaDocumentoIncidente(this.fileDir, filename, this.idIncidente);
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
