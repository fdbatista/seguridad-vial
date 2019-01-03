package com.earandap.vehiculos.domain;

import com.earandap.vehiculos.domain.nomenclador.Sexo;
import com.earandap.vehiculos.domain.nomenclador.TipoSangre;

import javax.persistence.*;
import java.util.*;

/**
 * @Autor Eduardo Aranda
 * @Version 0.1
 */
@Entity
@Table(name = "persona")
@DiscriminatorValue("PERSONA")
public class Persona extends Tercero {

    @ManyToOne
    @JoinColumn(name = "lugarexpedicioncedula_id")
    private Municipios lugarExpedicionCedula;

    @Column(name = "persona_primernombre")
    private String primerNombre;

    @Column(name = "persona_segundonombre")
    private String segundoNombre;

    @Column(name = "persona_primerapellido")
    private String primerApellido;

    @Column(name = "persona_segundoapellido")
    private String segundoApellido;

    @ManyToOne
    @JoinColumn(name = "sexo_id")
    private Sexo sexo;

    @Column(name = "persona_fechanacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;

    @ManyToOne
    @JoinColumn(name = "tiposangre_id")
    private TipoSangre tipoSangre;

    @Column(name = "persona_foto")
    private String foto;

    @Column(name = "persona_celular")
    private String celular;

    @ManyToOne
    @JoinColumn(name = "municipio_id")
    private Municipios municipios;

    @OneToOne
    @JoinColumn(name="conductor_id")
    private Conductor conductor;

    public void setMunicipios(Municipios municipios) {
        this.municipios = municipios;
    }

    @OneToOne
    @JoinColumn(name="empleado_id")
    private Empleado empleado;

    @OneToMany(mappedBy = "id.persona", fetch = FetchType.EAGER)
    private Set<PersonaCapacitacion> capacitaciones = new LinkedHashSet<>();

    public Municipios getLugarExpedicionCedula() {
        return lugarExpedicionCedula;
    }

    public void setLugarExpedicionCedula(Municipios lugarExpedicionCedula) {
        this.lugarExpedicionCedula = lugarExpedicionCedula;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public TipoSangre getTipoSangre() {
        return tipoSangre;
    }

    public void setTipoSangre(TipoSangre tipoSangre) {
        this.tipoSangre = tipoSangre;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public Municipios getMunicipios() {
        return municipios;
    }

    public void setMunicipio(Municipios municipios) {
        this.municipios = municipios;
    }

    public Conductor getConductor() {
        return conductor;
    }

    public void setConductor(Conductor conductor) {
        this.conductor = conductor;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public String getNombreCompleto() {
        StringBuilder builder = new StringBuilder("");
        if(primerNombre !=null)
            builder.append(primerNombre).append(" ");
        if (segundoNombre != null)
            builder.append(segundoNombre).append(" ");
        if (primerApellido != null)
            builder.append(primerApellido);
        if(segundoApellido != null)
            builder.append(" ").append(segundoApellido);

        return builder.toString();
    }

    @Override
    public String toString() {
        return this.getNombreCompleto();
    }

    @Override
    public String getNombreTercero() {
        return this.getNombreCompleto();
    }

    public String getAnnosLicencia(){
        if (conductor == null)
            return "";
        Calendar c = new GregorianCalendar();
        Calendar d = new GregorianCalendar();
        d.setTime(conductor.getAnnoLicencia());
       int annos =  Calendar.getInstance().get(Calendar.YEAR) - d.get(Calendar.YEAR);
        return "" + annos;
    }
}
