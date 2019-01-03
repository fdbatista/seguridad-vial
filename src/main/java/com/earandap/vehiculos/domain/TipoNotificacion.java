package com.earandap.vehiculos.domain;

import com.earandap.vehiculos.domain.nomenclador.Cargo;
import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * Created by Angel Luis on 12/18/2015.
 */
@Entity
@Table(name = "tipo_notificacion")
@SequenceGenerator(sequenceName = "tiponotificacion_seq", name = "SEQ_TIPONOTIFICACION")
public class TipoNotificacion {

    @Id
    @Column(name = "tiponotificacion_id")
    @GeneratedValue(generator = "SEQ_TIPONOTIFICACION", strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "tiponotificacion_icono")
    private String icono;

    @Column(name = "tiponotificacion_descripcion")
    private String descripcion;

    @Column(name = "tiponotificacion_codigo")
    private String codigo;
    
    @Column(name = "tiponotificacion_admin")
    private boolean admin;

    @OneToMany(mappedBy = "id.tipoNotificacion", fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private Set<TipoNotificacionMensaje> tiposNotificacionMensajes = new LinkedHashSet<>();
    
    @ManyToMany
    @JoinTable(name = "tipo_notificacion_cargo", joinColumns = {
        @JoinColumn(name = "tipo_notificacion_id")}, inverseJoinColumns = {
        @JoinColumn(name = "cargo_id")})
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Cargo> cargos = new LinkedHashSet<>();

    public Set<TipoNotificacionMensaje> getTiposNotificacionMensajes() {
        return tiposNotificacionMensajes;
    }

    public void setTiposNotificacionMensajes(Set<TipoNotificacionMensaje> tiposNotificacionMensajes) {
        this.tiposNotificacionMensajes = tiposNotificacionMensajes;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public Set<Cargo> getCargos() {
        return cargos;
    }

    public void setCargos(Set<Cargo> cargos) {
        this.cargos = cargos;
    }
    
    /*
    public Set<Suscriptor> getSuscriptores() {
        return suscriptores;
    }

    public void setSuscriptores(Set<Suscriptor> suscriptores) {
        this.suscriptores = suscriptores;
    }
     */
    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public boolean isViaEmail() {
        for (TipoNotificacionMensaje tipoNotificacionMensaje : tiposNotificacionMensajes) {
            if (tipoNotificacionMensaje.getTipoMensaje().getCodigo().toLowerCase().equals("mail") && tipoNotificacionMensaje.isActivo())
                return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TipoNotificacion)) {
            return false;
        }

        TipoNotificacion that = (TipoNotificacion) o;

        if (id != that.id) {
            return false;
        }
        if (descripcion != null ? !descripcion.equals(that.descripcion) : that.descripcion != null) {
            return false;
        }
        return !(codigo != null ? !codigo.equals(that.codigo) : that.codigo != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (descripcion != null ? descripcion.hashCode() : 0);
        result = 31 * result + (codigo != null ? codigo.hashCode() : 0);
        return result;
    }
}
