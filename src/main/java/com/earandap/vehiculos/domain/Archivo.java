package com.earandap.vehiculos.domain;


import javax.persistence.*;
import java.util.Date;

/**
 * @Autor Eduardo Aranda
 * @Version 0.1
 */
@Entity
@Table(name = "archivo")
@SequenceGenerator(sequenceName = "archivo_seq", name = "SEQ_ARCHIVO")
public class Archivo {

    @Id
    @Column(name = "archivo_id")
    @GeneratedValue(generator = "SEQ_ARCHIVO", strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "archivo_comentario")
    private String comentario;


    @Column(name = "archivo_url")
    private String url;

    @Column(name = "archivo_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    @Column(name = "archivo_creado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreado;

    @Column(name = "archivo_eliminado")
    private boolean eliminado;

    @ManyToOne
    @JoinColumn(name = "incidente_id")
    private Incidente incidente;

    public Archivo() {
    }

    public Archivo(long id) {
        this.id = id;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFechaCreado() {
        return fechaCreado;
    }

    public void setFechaCreado(Date fechaCreado) {
        this.fechaCreado = fechaCreado;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public Incidente getIncidente() {
        return incidente;
    }

    public void setIncidente(Incidente incidente) {
        this.incidente = incidente;
    }
}
