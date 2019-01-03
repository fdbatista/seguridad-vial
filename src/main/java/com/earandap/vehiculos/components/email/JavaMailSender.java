package com.earandap.vehiculos.components.email;

import com.earandap.vehiculos.domain.Parametro;
import com.earandap.vehiculos.repository.ParametrosRepository;
import java.util.List;
import java.util.Properties;
import javax.mail.Authenticator;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public class JavaMailSender {

    private List<Parametro> parametros;
    private String servidorCorreo, puertoCorreo, correoRemitente, claveRemitente, usarAutenticacion, usarTLS, usarSSL;
    Session sesion;

    public JavaMailSender(ParametrosRepository parametrosRepository) {
        parametros = parametrosRepository.findParametrosCorreo();

        servidorCorreo = parametros.get(0).getValorParametro();
        puertoCorreo = parametros.get(1).getValorParametro();
        correoRemitente = parametros.get(2).getValorParametro();
        claveRemitente = parametros.get(3).getValorParametro();
        usarAutenticacion = parametros.get(4).getValorParametro().toLowerCase().equals("no") ? "false" : "true";
        usarTLS = parametros.get(5).getValorParametro().toLowerCase().equals("no") ? "false" : "true";
        usarSSL = parametros.get(6).getValorParametro().toLowerCase().equals("no") ? "false" : "true";

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", servidorCorreo);
        properties.put("mail.smtp.port", puertoCorreo);
        properties.put("mail.smtp.username", correoRemitente);
        properties.put("mail.smtp.password", claveRemitente);
        properties.put("mail.smtp.ssl.enable", usarSSL);
        properties.put("mail.smtp.tls", usarTLS);

        if (usarAutenticacion.equals("true")) {
            properties.put("mail.smtp.auth", usarAutenticacion);
            Authenticator auth = new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(correoRemitente, claveRemitente);
                }
            };
            if (usarTLS.equals("true")) {
                properties.put("mail.smtp.starttls.enable", usarTLS);
                sesion = Session.getInstance(properties, auth);
            } else if (usarSSL.equals("true")) {
                properties.put("mail.smtp.socketFactory.port", "465");
                properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                sesion = Session.getDefaultInstance(properties, auth);
            }
        } else {
            sesion = Session.getInstance(properties, null);
        }
    }

    public void enviarCorreos(List<MyEmail> correos) {
        for (MyEmail correo : correos) {
            try {
                CommonsEmailService.send(correoRemitente, correo.getListaDestinatarios(), correo.getAsunto(), correo.getCuerpo());
                System.out.println("Correo enviado.");
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }

    }

}
