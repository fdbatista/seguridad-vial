package com.earandap.vehiculos.domain;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Cascade;

/**
 * @Autor Eduardo Aranda
 * @Version 0.1
 */
@Entity
@Table(name = "empresa")
@DiscriminatorValue("EMPRESA")
public class Empresa extends Tercero {

    @Column(name = "empresa_razonsocial")
    private String razonSocial;

    @OneToMany(mappedBy = "empresa", fetch = FetchType.EAGER)
    @Cascade(value = org.hibernate.annotations.CascadeType.REMOVE)
    private Set<MiembroComite> miembrosComite = new LinkedHashSet<>();

    @OneToMany(mappedBy = "empresa", fetch = FetchType.EAGER)
    @Cascade(value = org.hibernate.annotations.CascadeType.REMOVE)
    private Set<EmpresaDocumento> documentosEmpresa = new LinkedHashSet<>();

    @OneToMany(mappedBy = "empresa", fetch = FetchType.EAGER)
    @Cascade(value = org.hibernate.annotations.CascadeType.REMOVE)
    private Set<Sucursal> sucursales = new LinkedHashSet<>();

    public Set<Sucursal> getSucursales() {
        return sucursales;
    }

    public void setSucursales(Set<Sucursal> sucursales) {
        this.sucursales = sucursales;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    @Override
    public String getNombreTercero() {
        return this.getRazonSocial();
    }

    public Set<MiembroComite> getMiembrosComite() {
        return miembrosComite;
    }

    public void setMiembrosComite(Set<MiembroComite> miembrosComite) {
        this.miembrosComite = miembrosComite;
    }

    public Set<EmpresaDocumento> getDocumentosEmpresa() {
        return documentosEmpresa;
    }

    public void setDocumentosEmpresa(Set<EmpresaDocumento> documentosEmpresa) {
        this.documentosEmpresa = documentosEmpresa;
    }

}
