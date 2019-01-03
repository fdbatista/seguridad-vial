package com.earandap.vehiculos.ui.views.empresas;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.EmpresaDocumento;
import com.earandap.vehiculos.domain.nomenclador.TipoDocumentoEmpresa;
import com.earandap.vehiculos.repository.EmpresaDocumentoRepository;
import com.earandap.vehiculos.repository.TipoDocumentoEmpresaRepository;
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
public class EmpresaDocumentoForm extends FormLayout implements Receiver, FinishedListener {

    private BeanItemContainer<TipoDocumentoEmpresa> tipoEmpresaDocumentoBeanItemContainer;

    @PropertyId("tipoDocumentoEmpresa")
    private ComboBox tipoDocumentoEmpresaField;

    @PropertyId("rutaDocumento")
    private TextField rutaDocumentoField;

    private Upload upload;
    private String urlDir, fileDir, nombreFichero;
    private boolean errorCarga;
    private BeanFieldGroup<EmpresaDocumento> empresaDocumentoBeanFieldGroup;
    private EmpresaDocumento empresaDocumento;
    private EmpresaDocumentoRepository empresaDocumentoRepository;
    private TipoDocumentoEmpresaRepository tipoDocumentoEmpresaRepository;
    private Long idEmpresa;
    private List<String> ficherosCargados;

    public EmpresaDocumentoForm(boolean adicionar, Long idEmpresa, EmpresaDocumento empresaDocumento, List<TipoDocumentoEmpresa> tipoDocumentoEmpresas, String fileDir, String urlDir) {

        this.setStyleName("seguro-form");
        this.empresaDocumento = empresaDocumento;
        this.urlDir = urlDir;
        this.fileDir = fileDir;
        this.idEmpresa = idEmpresa;
        this.errorCarga = true;
        this.ficherosCargados = new ArrayList<>();

        tipoEmpresaDocumentoBeanItemContainer = new BeanItemContainer<>(TipoDocumentoEmpresa.class);
        tipoEmpresaDocumentoBeanItemContainer.addAll(tipoDocumentoEmpresas);

        tipoDocumentoEmpresaField = new ComboBox("Tipo de Documento");
        tipoDocumentoEmpresaField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        tipoDocumentoEmpresaField.setWidth(20, Unit.EM);
        tipoDocumentoEmpresaField.setItemCaptionPropertyId("descripcion");
        tipoDocumentoEmpresaField.setContainerDataSource(tipoEmpresaDocumentoBeanItemContainer);
        tipoDocumentoEmpresaField.setRequired(true);
        tipoDocumentoEmpresaField.setRequiredError("El tipo de documento es obligatorio");
        this.addComponent(tipoDocumentoEmpresaField);

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

        empresaDocumentoBeanFieldGroup = new BeanFieldGroup<>(EmpresaDocumento.class);
        empresaDocumentoBeanFieldGroup.setItemDataSource(empresaDocumento);
        empresaDocumentoBeanFieldGroup.bindMemberFields(this);
        empresaDocumentoBeanFieldGroup.bindMemberFields(rutaDocumentoField);
        
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

    public TipoDocumentoEmpresaRepository getTipoDocumentoEmpresaRepository() {
        return tipoDocumentoEmpresaRepository;
    }

    public void setTipoDocumentoEmpresaRepository(TipoDocumentoEmpresaRepository tipoDocumentoEmpresaRepository) {
        this.tipoDocumentoEmpresaRepository = tipoDocumentoEmpresaRepository;
    }

    public BeanItemContainer<TipoDocumentoEmpresa> getTipoEmpresaDocumentoBeanItemContainer() {
        return tipoEmpresaDocumentoBeanItemContainer;
    }

    public void setTipoEmpresaDocumentoBeanItemContainer(BeanItemContainer<TipoDocumentoEmpresa> tipoEmpresaDocumentoBeanItemContainer) {
        this.tipoEmpresaDocumentoBeanItemContainer = tipoEmpresaDocumentoBeanItemContainer;
    }

    public ComboBox getTipoDocumentoEmpresaField() {
        return tipoDocumentoEmpresaField;
    }

    public void setTipoDocumentoEmpresaField(ComboBox tipoDocumentoEmpresaField) {
        this.tipoDocumentoEmpresaField = tipoDocumentoEmpresaField;
    }

    public BeanFieldGroup<EmpresaDocumento> getEmpresaDocumentoBeanFieldGroup() {
        return empresaDocumentoBeanFieldGroup;
    }

    public void setEmpresaDocumentoBeanFieldGroup(BeanFieldGroup<EmpresaDocumento> empresaDocumentoBeanFieldGroup) {
        this.empresaDocumentoBeanFieldGroup = empresaDocumentoBeanFieldGroup;
    }

    public EmpresaDocumento getEmpresaDocumento() {
        return empresaDocumento;
    }

    public void setEmpresaDocumento(EmpresaDocumento empresaDocumento) {
        this.empresaDocumento = empresaDocumento;
    }

    public EmpresaDocumentoRepository getEmpresaDocumentoRepository() {
        return empresaDocumentoRepository;
    }

    public void setEmpresaDocumentoRepository(EmpresaDocumentoRepository empresaDocumentoRepository) {
        this.empresaDocumentoRepository = empresaDocumentoRepository;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.empresaDocumento.getId());
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
        final EmpresaDocumentoForm other = (EmpresaDocumentoForm) obj;
        if (!Objects.equals(this.empresaDocumento.getId(), other.empresaDocumento.getId())) {
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
            String rutaCompleta = StaticMembers.construirRutaDocumentoEmpresa(this.fileDir, filename, this.idEmpresa);
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
