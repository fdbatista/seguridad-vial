package com.earandap.vehiculos.domain;

import com.earandap.vehiculos.domain.nomenclador.TipoLesion;
import javax.persistence.*;

/**
 * @Autor felix.batista
 */
@Entity
@Table(name = "incidente_lesion")
@SequenceGenerator(sequenceName = "incidente_lesion_seq", name = "SEQ_INCIDENTE_LESION")
public class IncidenteLesion {

    @Id
    @Column(name = "incidente_lesion_id")
    @GeneratedValue(generator = "SEQ_INCIDENTE_LESION", strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "cantidad")
    private int cantidad;

    @ManyToOne
    @JoinColumn(name = "incidente_id")
    private Incidente incidente;

    @ManyToOne
    @JoinColumn(name = "tipo_lesion_id")
    private TipoLesion tipoLesion;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Incidente getIncidente() {
        return incidente;
    }

    public void setIncidente(Incidente incidente) {
        this.incidente = incidente;
    }

    public TipoLesion getTipoLesion() {
        return tipoLesion;
    }

    public void setTipoLesion(TipoLesion tipoLesion) {
        this.tipoLesion = tipoLesion;
    }

}
