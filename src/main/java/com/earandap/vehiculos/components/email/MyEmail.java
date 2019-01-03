/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.earandap.vehiculos.components.email;

import java.util.List;
import javax.mail.internet.InternetAddress;

/**
 *
 * @author felix.batista
 */
public class MyEmail {

    private String asunto, cuerpo;
    //private InternetAddress[] destinatarios;
    private List<String> listaDestinatarios;

    public MyEmail(String asunto, String cuerpo, List<String> listaDestinatarios) {
        this.asunto = asunto;
        this.cuerpo = cuerpo;
        this.listaDestinatarios = listaDestinatarios;
    }
    
    public List<String> getListaDestinatarios() {
        return listaDestinatarios;
    }

    public void setListaDestinatarios(List<String> listaDestinatarios) {
        this.listaDestinatarios = listaDestinatarios;
    }


    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    /*public InternetAddress[] getDestinatarios() {
        return destinatarios;
    }

    public void setDestinatarios(InternetAddress[] destinatarios) {
        this.destinatarios = destinatarios;
    }*/
    
}
