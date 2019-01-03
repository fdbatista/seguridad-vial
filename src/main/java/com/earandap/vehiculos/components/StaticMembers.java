package com.earandap.vehiculos.components;

import com.earandap.vehiculos.ui.components.HibernateUtil;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.hibernate.Session;

/**
 *
 * @author felix.batista
 */
/*Label label = new Label("This will fade-out once you click the button");

        Button button = new Button("Hide Label");
        button.addClickListener(new Button.ClickListener() {
           public void buttonClick(ClickEvent event) {
              JavaScript.getCurrent().execute("$('.v-label').animate({opacity: 0.0}, 3000);");
              Page.getCurrent().getJavaScript().execute("$('.v-label').animate({opacity: 0.0}, 3000);");
           }
        });
        this.addComponent(label);
        this.addComponent(button);*/
public class StaticMembers {

    public static String rutaFicherosEmpresas = "empresas\\%1$d\\",
            rutaFicherosMantenimientos = "mttos\\%1$d\\",
            rutaFicherosIncidentes = "incidentes\\%1$d\\",
            rutaIconosNotificacion = "tipos_notif\\",
            rutaFicherosActividades = "actividades\\%1$d\\";

    public static String construirRutaDocumentoEmpresa(String rutaFicheros, String nombreFichero, Long idEmpresa) {
        String rutaDirectorio = rutaFicheros + String.format(rutaFicherosEmpresas, idEmpresa);
        File directorio = new File(rutaDirectorio);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
        return rutaDirectorio + nombreFichero;
    }

    public static String construirUrlDocumentoEmpresa(String urlFicheros, String nombreFichero, Long idEmpresa) {
        String rutaDirectorio = urlFicheros + String.format(rutaFicherosEmpresas, idEmpresa) + nombreFichero;
        return rutaDirectorio;
    }

    public static String construirRutaDocumentoMantenimiento(String rutaFicheros, String nombreFichero, Long idMtto) {
        String rutaDirectorio = rutaFicheros + String.format(rutaFicherosMantenimientos, idMtto);
        File directorio = new File(rutaDirectorio);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
        return rutaDirectorio + nombreFichero;
    }

    public static String construirUrlDocumentoMantenimiento(String urlFicheros, String nombreFichero, Long idMtto) {
        String rutaDirectorio = urlFicheros + String.format(rutaFicherosMantenimientos, idMtto) + nombreFichero;
        return rutaDirectorio;
    }

    public static String construirRutaDocumentoCapacitacion(String rutaFicheros, String nombreFichero, Long idCapac) {
        String rutaDirectorio = rutaFicheros + String.format(rutaFicherosActividades, idCapac);
        File directorio = new File(rutaDirectorio);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
        return rutaDirectorio + nombreFichero;
    }

    public static String construirRutaIconoNotificacion(String rutaFicheros, String nombreFichero) {
        String rutaDirectorio = rutaFicheros + rutaIconosNotificacion;
        File directorio = new File(rutaDirectorio);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
        return rutaDirectorio + nombreFichero;
    }

    public static String construirUrlDocumentoCapacitacion(String urlFicheros, String nombreFichero, Long idCapac) {
        String rutaDirectorio = urlFicheros + String.format(rutaFicherosActividades, idCapac) + nombreFichero;
        return rutaDirectorio;
    }

    public static String construirRutaDocumentoIncidente(String rutaFicheros, String nombreFichero, Long idIncidente) {
        String rutaDirectorio = rutaFicheros + String.format(rutaFicherosIncidentes, idIncidente);
        File directorio = new File(rutaDirectorio);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
        return rutaDirectorio + nombreFichero;
    }

    public static String construirUrlDocumentoIncidente(String urlFicheros, String nombreFichero, Long idIncidente) {
        String rutaDirectorio = urlFicheros + String.format(rutaFicherosIncidentes, idIncidente) + nombreFichero;
        return rutaDirectorio;
    }

    public static void moverFichero(String rutaOrigen, String rutaDestino) {
        try {
            Files.move(Paths.get(rutaOrigen), Paths.get(rutaDestino), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void eliminarFichero(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File fileAux : files) {
                if (fileAux.isDirectory()) {
                    eliminarFichero(fileAux);
                } else {
                    fileAux.delete();
                }
            }
            file.delete();
        } else {
            file.delete();
        }
    }

    public static void eliminarFichero(String rutaFichero) {
        try {
            Files.delete(Paths.get(rutaFichero));
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public static void eliminarCarpetaActividad(String rutaFicheros, Long idElem) {
        eliminarFichero(new File(rutaFicheros + String.format(rutaFicherosActividades, idElem)));
    }

    public static void eliminarCarpetaMtto(String rutaFicheros, Long idElem) {
        eliminarFichero(new File(rutaFicheros + String.format(rutaFicherosMantenimientos, idElem)));
    }

    public static void eliminarCarpetaEmpresa(String rutaFicheros, Long idElem) {
        eliminarFichero(new File(rutaFicheros + String.format(rutaFicherosEmpresas, idElem)));
    }

    public static void mostrar(String valor) {
        System.out.println("\n" + valor);
    }

    public static void showNotificationError(String header, String message) {
        Notification notification = new Notification(header, message, Notification.Type.ERROR_MESSAGE);
        notification.setPosition(Position.BOTTOM_RIGHT);
        notification.setDelayMsec(3000);
        notification.setIcon(FontAwesome.EXCLAMATION_CIRCLE);
        notification.show(Page.getCurrent());
    }

    public static void showNotificationWarning(String header, String message) {
        Notification notification = new Notification(header, message, Notification.Type.WARNING_MESSAGE);
        notification.setPosition(Position.BOTTOM_RIGHT);
        notification.setDelayMsec(3000);
        notification.setIcon(FontAwesome.WARNING);
        notification.show(Page.getCurrent());
    }

    public static void showNotificationMessage(String header, String message) {
        Notification notification = new Notification(header, message, Notification.Type.TRAY_NOTIFICATION);
        notification.setDelayMsec(3000);
        notification.setIcon(FontAwesome.INFO_CIRCLE);
        notification.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
        notification.show(Page.getCurrent());
    }

    private static HibernateUtil getHibernateUtil(String dbUrl, String dbUsername, String dbPassword, String dbDriver) {
        HibernateUtil utility = new HibernateUtil(dbUrl, dbUsername, dbPassword, dbDriver);
        return utility;
    }

    private static Session abrirSesion(String dbUrl, String dbUsername, String dbPassword, String dbDriver) {
        Session session = getHibernateUtil(dbUrl, dbUsername, dbPassword, dbDriver).getSession();
        session.beginTransaction();
        return session;
    }

    private static void cerrarSesion(Session session) {
        session.getTransaction().commit();
        session.close();
    }

    public static List<Object[]> ejecutarConsulta(String sql, String dbUrl, String dbUsername, String dbPassword, String dbDriver) {
        List<Object[]> res;
        try {
            Session session = abrirSesion(dbUrl, dbUsername, dbPassword, dbDriver);
            res = session.createSQLQuery(sql).list();
            cerrarSesion(session);
        } catch (Exception e) {
            res = new ArrayList<>();
        }
        return res;
    }

    public static int ejecutarUpdate(String sql, String dbUrl, String dbUsername, String dbPassword, String dbDriver) {
        int res;
        try {
            Session session = abrirSesion(dbUrl, dbUsername, dbPassword, dbDriver);
            res = session.createSQLQuery(sql).executeUpdate();
            cerrarSesion(session);
        } catch (Exception e) {
            res = -1;
        }
        return res;
    }

    public static String getNombreFichero(String ruta) {
        int indice = ruta.lastIndexOf("\\") + 1;
        if (indice == 0) {
            indice = ruta.lastIndexOf("/") + 1;
        }
        return ruta.substring(indice);
    }

    public static void eliminarFicherosMantenimientoInvalidos(boolean cancelar, String rutaDocumentoActual, String rutaDocumentoPrevia, String fileDir, Long idObj, List<String> listaFicherosCargados) {
        String rutaCompleta;
        if (cancelar) {
            for (String nombreFichero : listaFicherosCargados) {
                if (!nombreFichero.equals(rutaDocumentoPrevia)) {
                    rutaCompleta = construirRutaDocumentoMantenimiento(fileDir, nombreFichero, idObj);
                    eliminarFichero(rutaCompleta);
                }
            }
        } else {
            if (!rutaDocumentoPrevia.equals(rutaDocumentoActual)) {
                if (!rutaDocumentoPrevia.equals("")) {
                    rutaCompleta = construirRutaDocumentoMantenimiento(fileDir, rutaDocumentoPrevia, idObj);
                    eliminarFichero(rutaCompleta);
                }
                for (String nombreFichero : listaFicherosCargados) {
                    if (!nombreFichero.equals(rutaDocumentoActual)) {
                        rutaCompleta = construirRutaDocumentoMantenimiento(fileDir, nombreFichero, idObj);
                        eliminarFichero(rutaCompleta);
                    }
                }
            }
        }
    }

    public static void eliminarFicherosEmpresaInvalidos(boolean cancelar, String rutaDocumentoActual, String rutaDocumentoPrevia, String fileDir, Long idObj, List<String> listaFicherosCargados) {
        String rutaCompleta;
        if (cancelar) {
            for (String nombreFichero : listaFicherosCargados) {
                if (!nombreFichero.equals(rutaDocumentoPrevia)) {
                    rutaCompleta = construirRutaDocumentoEmpresa(fileDir, nombreFichero, idObj);
                    eliminarFichero(rutaCompleta);
                }
            }
        } else {
            if (!rutaDocumentoPrevia.equals(rutaDocumentoActual)) {
                if (!rutaDocumentoPrevia.equals("")) {
                    rutaCompleta = construirRutaDocumentoEmpresa(fileDir, rutaDocumentoPrevia, idObj);
                    eliminarFichero(rutaCompleta);
                }
                for (String nombreFichero : listaFicherosCargados) {
                    if (!nombreFichero.equals(rutaDocumentoActual)) {
                        rutaCompleta = construirRutaDocumentoEmpresa(fileDir, nombreFichero, idObj);
                        eliminarFichero(rutaCompleta);
                    }
                }
            }
        }
    }

    public static void eliminarFicherosIncidenteInvalidos(boolean cancelar, String rutaDocumentoActual, String rutaDocumentoPrevia, String fileDir, Long idObj, List<String> listaFicherosCargados) {
        String rutaCompleta;
        if (cancelar) {
            for (String nombreFichero : listaFicherosCargados) {
                if (!nombreFichero.equals(rutaDocumentoPrevia)) {
                    rutaCompleta = construirRutaDocumentoIncidente(fileDir, nombreFichero, idObj);
                    eliminarFichero(rutaCompleta);
                }
            }
        } else {
            if (!rutaDocumentoPrevia.equals(rutaDocumentoActual)) {
                if (!rutaDocumentoPrevia.equals("")) {
                    rutaCompleta = construirRutaDocumentoIncidente(fileDir, rutaDocumentoPrevia, idObj);
                    eliminarFichero(rutaCompleta);
                }
                for (String nombreFichero : listaFicherosCargados) {
                    if (!nombreFichero.equals(rutaDocumentoActual)) {
                        rutaCompleta = construirRutaDocumentoIncidente(fileDir, nombreFichero, idObj);
                        eliminarFichero(rutaCompleta);
                    }
                }
            }
        }
    }

    public static void eliminarFicherosCapacitacionInvalidos(boolean cancelar, String rutaDocumentoActual, String rutaDocumentoPrevia, String fileDir, Long idObj, List<String> listaFicherosCargados) {
        String rutaCompleta;
        if (cancelar) {
            for (String nombreFichero : listaFicherosCargados) {
                if (!nombreFichero.equals(rutaDocumentoPrevia)) {
                    rutaCompleta = construirRutaDocumentoCapacitacion(fileDir, nombreFichero, idObj);
                    eliminarFichero(rutaCompleta);
                }
            }
        } else {
            if (!rutaDocumentoPrevia.equals(rutaDocumentoActual)) {
                if (!rutaDocumentoPrevia.equals("")) {
                    rutaCompleta = construirRutaDocumentoCapacitacion(fileDir, rutaDocumentoPrevia, idObj);
                    eliminarFichero(rutaCompleta);
                }
                for (String nombreFichero : listaFicherosCargados) {
                    if (!nombreFichero.equals(rutaDocumentoActual)) {
                        rutaCompleta = construirRutaDocumentoCapacitacion(fileDir, nombreFichero, idObj);
                        eliminarFichero(rutaCompleta);
                    }
                }
            }
        }
    }

    public static int convertirAEntero(String valor) {
        int res;
        try {
            res = Integer.valueOf(valor);
        } catch (Exception e) {
            res = -1;
        }
        return res;
    }

    public static void setErrorRangoEspanhol(DateField field) {
        field.setDateOutOfRangeMessage("La fecha est\u00E1 fuera del rango permitido");
        field.setParseErrorMessage("La fecha est\u00E1 fuera del rango permitido");
    }
    
    public static Long diasEntreFechas(Date fecha1, Date fecha2) {
        return TimeUnit.DAYS.convert(fecha1.getTime() - fecha2.getTime(), TimeUnit.MILLISECONDS);
    }

}
