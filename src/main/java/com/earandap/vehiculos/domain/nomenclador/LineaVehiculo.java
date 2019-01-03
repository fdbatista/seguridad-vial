package com.earandap.vehiculos.domain.nomenclador;

import com.earandap.vehiculos.domain.Vehiculo;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;

/**
 * @Autor felix.batista
 */

@Entity
@Table(name = "linea")
@SequenceGenerator(sequenceName = "linea_seq", name = "SEQ_LINEA")
public class LineaVehiculo implements Serializable {

    @Id
    @GeneratedValue(generator = "SEQ_LINEA", strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "codigo", nullable = false, length = 10)
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 120)
    private String nombre;
    
    @ManyToOne
    @JoinColumn(name="id_marca")
    private MarcaVehiculo marcaVehiculo;
    
    @OneToMany(mappedBy="lineaVehiculo")
    private Set<Vehiculo> vehiculos;

    public Set<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(Set<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }

    public MarcaVehiculo getMarcaVehiculo() {
        return marcaVehiculo;
    }

    public void setMarcaVehiculo(MarcaVehiculo marcaVehiculo) {
        this.marcaVehiculo = marcaVehiculo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final LineaVehiculo other = (LineaVehiculo) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
