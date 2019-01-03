package com.earandap.vehiculos.ui.components;

import com.earandap.vehiculos.components.StaticMembers;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FinishedListener;
import com.vaadin.ui.Upload.Receiver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class FileReceiver implements Receiver, FinishedListener {

    private boolean cargaCompleta, errorCarga;
    private String fileName;
    private String fileDir;
    private Long objId;
    public File file;

    public FileReceiver(String fileDir, Long objId) {
        this.fileDir = fileDir;
        this.objId = objId;
        this.cargaCompleta = false;
        this.errorCarga = true;
        this.fileName = "";
    }

    public boolean isCargaCompleta() {
        return cargaCompleta;
    }

    public void setCargaCompleta(boolean cargaCompleta) {
        this.cargaCompleta = cargaCompleta;
    }

    public boolean isErrorCarga() {
        return errorCarga;
    }

    public void setErrorCarga(boolean errorCarga) {
        this.errorCarga = errorCarga;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getObjId() {
        return objId;
    }

    public void setObjId(Long objId) {
        this.objId = objId;
    }

    public String getFileDir() {
        return fileDir;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }

    @Override
    public void uploadFinished(Upload.FinishedEvent event) {
        //Object data = event.getUpload().getData();
        this.cargaCompleta = true;
        if (!errorCarga) {
            StaticMembers.showNotificationMessage("", "El fichero ha sido cargado correctamente");
        }
    }

    @Override
    public OutputStream receiveUpload(String filename, String mimeType) {
        FileOutputStream fos = null;
        try {
            String rutaCompleta = StaticMembers.construirRutaDocumentoEmpresa(this.fileDir, filename, this.objId);
            this.file = new File(rutaCompleta);
            this.fileName = filename;
            fos = new FileOutputStream(this.file);
            errorCarga = false;
        } catch (final java.io.FileNotFoundException e) {
            StaticMembers.showNotificationError("", "La ruta del fichero es incorrecta");
            errorCarga = true;
        }
        return fos;
    }


}
