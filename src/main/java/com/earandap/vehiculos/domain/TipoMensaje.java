package com.earandap.vehiculos.domain;


import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cascade;

/**
 * Created by Angel Luis on 12/18/2015.
 */
@Entity
@Table(name = "tipo_mensaje")
@SequenceGenerator(sequenceName = "tipomensaje_seq", name = "SEQ_TIPOMENSAJE")
public class TipoMensaje {

    @Id
    @Column(name = "tipomensaje_id")
    @GeneratedValue(generator = "SEQ_TIPOMENSAJE", strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "tipomensaje_codigo")
    private String codigo;

    @Column(name = "tipomensaje_descripcion")
    private String descripcion;
    
    @OneToMany(mappedBy = "id.tipoMensaje", fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private Set<TipoNotificacionMensaje> tiposNotificacionMensajes = new LinkedHashSet<>();

    public Set<TipoNotificacionMensaje> getTiposNotificacionMensajes() {
        return tiposNotificacionMensajes;
    }

    public void setTiposNotificacionMensajes(Set<TipoNotificacionMensaje> tiposNotificacionMensajes) {
        this.tiposNotificacionMensajes = tiposNotificacionMensajes;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TipoMensaje)) return false;

        TipoMensaje that = (TipoMensaje) o;

        if (id != that.id) return false;
        if (codigo != null ? !codigo.equals(that.codigo) : that.codigo != null) return false;
        return !(descripcion != null ? !descripcion.equals(that.descripcion) : that.descripcion != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (codigo != null ? codigo.hashCode() : 0);
        result = 31 * result + (descripcion != null ? descripcion.hashCode() : 0);
        return result;
    }
}
