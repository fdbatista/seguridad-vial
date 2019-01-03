package com.earandap.vehiculos.domain;

import com.earandap.vehiculos.domain.nomenclador.TipoMantenimiento;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;
import org.hibernate.annotations.Cascade;

/**
 * @Autor Eduardo Aranda
 * @Version 0.1
 */
@Entity
@Table(name = "accion_mantenimiento")
@DiscriminatorValue("ACCIONMANTENIMIENTO")
public class AccionMantenimiento extends Accion {

    @ManyToOne
    @JoinColumn(name = "tipomantenimiento_id")
    private TipoMantenimiento tipoMantenimiento;

    @Column(name = "accionmantenimiento_kilometrajeactual")
    private int km;
    
    @Column(name = "accionmantenimiento_rutafichero")
    private String rutaDocumento;

    @OneToMany(mappedBy = "mantenimiento", fetch = FetchType.EAGER)
    @Cascade(value = org.hibernate.annotations.CascadeType.REMOVE)
    private Set<MantenimientoDetalle> detalles = new LinkedHashSet<>();
    
    @OneToMany(mappedBy = "mantenimiento", fetch = FetchType.EAGER)
    @Cascade(value = org.hibernate.annotations.CascadeType.REMOVE)
    private Set<MantenimientoDocumento> documentosMantenimiento = new LinkedHashSet<>();

    public Set<MantenimientoDocumento> getDocumentosMantenimiento() {
        return documentosMantenimiento;
    }

    public void setDocumentosMantenimiento(Set<MantenimientoDocumento> documentosMantenimiento) {
        this.documentosMantenimiento = documentosMantenimiento;
    }

    public String getRutaDocumento() {
        return rutaDocumento;
    }

    public void setRutaDocumento(String rutaDocumento) {
        this.rutaDocumento = rutaDocumento;
    }

    public TipoMantenimiento getTipoMantenimiento() {
        return tipoMantenimiento;
    }

    public void setTipoMantenimiento(TipoMantenimiento tipoMantenimiento) {
        this.tipoMantenimiento = tipoMantenimiento;
    }

    public int getKm() {
        return km;
    }

    public void setKm(int km) {
        this.km = km;
    }

    public Set<MantenimientoDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(Set<MantenimientoDetalle> detalles) {
        this.detalles = detalles;
    }

}
