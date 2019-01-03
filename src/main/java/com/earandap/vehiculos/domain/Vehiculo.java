package com.earandap.vehiculos.domain;


import com.earandap.vehiculos.domain.nomenclador.ClaseVehiculo;
import com.earandap.vehiculos.domain.nomenclador.Color;
import com.earandap.vehiculos.domain.nomenclador.LineaVehiculo;
import com.earandap.vehiculos.domain.nomenclador.MarcaVehiculo;
import com.earandap.vehiculos.domain.nomenclador.TipoCarroceria;
import com.earandap.vehiculos.domain.nomenclador.TipoCombustible;
import com.earandap.vehiculos.domain.nomenclador.TipoServicio;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;
import org.hibernate.annotations.Cascade;

/**
 * @Autor Eduardo Aranda
 * @Version 0.1
 */
@Entity
@Table(name = "vehiculo")
@SequenceGenerator(sequenceName = "vehiculo_seq", name = "SEQ_VEHICULO")
public class Vehiculo {

    @Id
    @Column(name = "vehiculo_id")
    @GeneratedValue(generator = "SEQ_VEHICULO", strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "vehiculo_placa")
    private String placa;

    @ManyToOne
    @JoinColumn(name = "tipovehiculo_id")
    private ClaseVehiculo claseVehiculo;

    @ManyToOne
    @JoinColumn(name = "propietario_id")
    private Persona propietario;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa afiliadoA;
    
    @ManyToOne
    @JoinColumn(name="linea_id")
    private LineaVehiculo lineaVehiculo;
    
    @ManyToOne
    @JoinColumn(name="marca_id")
    private MarcaVehiculo marcaVehiculo;

    @Column(name = "vehiculo_kminicial")
    private int kmInicial;

    @Column(name = "vehiculo_kmactual")
    private int kmActual;

    @Column(name = "vehiculo_seriemotor")
    private String serieMotor;
    
    @Column(name = "vehiculo_modelo")
    private String modelo;
    
    @Column(name = "vehiculo_cilindrada")
    private Float cilindrada;
    
    @Column(name = "vehiculo_capacidad")
    private Float capacidad;
    
    @Column(name = "vehiculo_vin")
    private String vin;

    @Column(name = "vehiculo_chasis")
    private String chasis;
    
    @Column(name = "vehiculo_serie")
    private String serie;
    
    @ManyToOne
    @JoinColumn(name="carroceria_id")
    private TipoCarroceria tipoCarroceria;
    
    @ManyToOne
    @JoinColumn(name="combustible_id")
    private TipoCombustible tipoCombustible;
    
    @ManyToOne
    @JoinColumn(name="color_id")
    private Color color;
    
    @ManyToOne
    @JoinColumn(name="tiposervicio_id")
    private TipoServicio tipoServicio;
    
    @OneToMany(mappedBy = "vehiculo", fetch = FetchType.EAGER)
    @Cascade(value = org.hibernate.annotations.CascadeType.REMOVE)
    private Set<Seguro> seguros = new LinkedHashSet<>();
    
    
    public String getChasis() {
        return chasis;
    }

    public void setChasis(String chasis) {
        this.chasis = chasis;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public TipoCarroceria getTipoCarroceria() {
        return tipoCarroceria;
    }

    public void setTipoCarroceria(TipoCarroceria tipoCarroceria) {
        this.tipoCarroceria = tipoCarroceria;
    }

    public TipoCombustible getTipoCombustible() {
        return tipoCombustible;
    }

    public void setTipoCombustible(TipoCombustible tipoCombustible) {
        this.tipoCombustible = tipoCombustible;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public TipoServicio getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(TipoServicio tipoServicio) {
        this.tipoServicio = tipoServicio;
    }
    
    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Float getCilindrada() {
        return cilindrada;
    }

    public void setCilindrada(Float cilindrada) {
        this.cilindrada = cilindrada;
    }

    public Float getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Float capacidad) {
        this.capacidad = capacidad;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public Set<Seguro> getSeguros() {
        return seguros;
    }

    public void setSeguros(Set<Seguro> seguros) {
        this.seguros = seguros;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Persona getPropietario() {
        return propietario;
    }

    public void setPropietario(Persona propietario) {
        this.propietario = propietario;
    }

    public Empresa getAfiliadoA() {
        return afiliadoA;
    }

    public void setAfiliadoA(Empresa afiliadoA) {
        this.afiliadoA = afiliadoA;
    }

    public int getKmInicial() {
        return kmInicial;
    }

    public void setKmInicial(int kmInicial) {
        this.kmInicial = kmInicial;
    }

    public int getKmActual() {
        return kmActual;
    }

    public void setKmActual(int kmActual) {
        this.kmActual = kmActual;
    }

    public String getSerieMotor() {
        return serieMotor;
    }

    public void setSerieMotor(String serieMotor) {
        this.serieMotor = serieMotor;
    }

    public ClaseVehiculo getClaseVehiculo() {
        return claseVehiculo;
    }

    public void setClaseVehiculo(ClaseVehiculo claseVehiculo) {
        this.claseVehiculo = claseVehiculo;
    }

    public LineaVehiculo getLineaVehiculo() {
        return lineaVehiculo;
    }

    public void setLineaVehiculo(LineaVehiculo lineaVehiculo) {
        this.lineaVehiculo = lineaVehiculo;
    }

    public MarcaVehiculo getMarcaVehiculo() {
        return marcaVehiculo;
    }

    public void setMarcaVehiculo(MarcaVehiculo marcaVehiculo) {
        this.marcaVehiculo = marcaVehiculo;
    }


}
