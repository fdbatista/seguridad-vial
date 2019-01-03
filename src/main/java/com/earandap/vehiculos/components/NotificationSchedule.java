package com.earandap.vehiculos.components;

/**
 * Created by Angel Luis on 12/31/2015.
 */
import com.earandap.vehiculos.components.email.JavaMailSender;
import com.earandap.vehiculos.components.email.MyEmail;
import com.earandap.vehiculos.domain.Capacitacion;
import com.earandap.vehiculos.domain.Licencia;
import com.earandap.vehiculos.domain.Notificacion;
import com.earandap.vehiculos.domain.Persona;
import com.earandap.vehiculos.domain.Seguro;
import com.earandap.vehiculos.domain.TerceroNotificacion;
import com.earandap.vehiculos.domain.TipoNotificacion;
import com.earandap.vehiculos.domain.Usuario;
import com.earandap.vehiculos.domain.nomenclador.Cargo;
import com.earandap.vehiculos.repository.CapacitacionRepository;
import com.earandap.vehiculos.repository.LicenciaRepository;
import com.earandap.vehiculos.repository.NotificacionRepository;
import com.earandap.vehiculos.repository.ParametrosRepository;
import com.earandap.vehiculos.repository.PersonaRepository;
import com.earandap.vehiculos.repository.SeguroRepository;
import com.earandap.vehiculos.repository.TerceroNotificacionRepository;
import com.earandap.vehiculos.repository.TipoNotificacionMensajeRepository;
import com.earandap.vehiculos.repository.TipoNotificacionRepository;
import com.earandap.vehiculos.repository.UsuarioRepository;
import com.earandap.vehiculos.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.vaadin.spring.events.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.mail.internet.InternetAddress;

@Component
public class NotificationSchedule {

    public static final String NUEVA_NOTIFICACION = "NUEVA-NOTIFICACION";

    private static final SimpleDateFormat formatoHoras = new SimpleDateFormat("HH:mm:ss");

    /*@Autowired
    private SmtpMailSender smtpMailSender;*/
    @Autowired
    private NotificacionService notificacionService;

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private TipoNotificacionRepository tipoNotificacionRepository;

    @Autowired
    private TerceroNotificacionRepository terceroNotificacionRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ParametrosRepository parametrosRepository;

    @Autowired
    private SeguroRepository seguroRepository;

    @Autowired
    private LicenciaRepository licenciaRepository;

    @Autowired
    private CapacitacionRepository capacitacionRepository;
    
    @Autowired
    private TipoNotificacionMensajeRepository tipoNotificacionMensajeRepository;

    @Autowired
    private EventBus.ApplicationEventBus applicationEventBus;

    private void adicionarNotificaciones(List<Notificacion> notificaciones) {
        for (Notificacion notificacion : notificaciones) {
            if (notificacionRepository.existe(notificacion.getDetalle()) == null) {
                notificacionRepository.save(notificacion);
                System.out.println("Notificacion #" + notificacion.getId() + " generada a las " + formatoHoras.format(new Date()));
            }
        }
    }
    
    private String construirDescripcionDias(Long dias) {
        return (dias == 0 ? "hoy" : (dias == 1 ? "mañana" : String.format("en %s dias", dias)));
    }

    @Scheduled(fixedRate = 10000)
    public void generarNotificaciones() {

        Date fechaActual = new Date();
        Long diasRestantes;
        List<Notificacion> notificaciones = new ArrayList<>();

        List<Seguro> seguros = seguroRepository.getSegurosProximoVencimiento();
        TipoNotificacion tipoNotificacion = tipoNotificacionRepository.findOne(Long.valueOf("1"));
        for (Seguro obj : seguros) {
            diasRestantes = StaticMembers.diasEntreFechas(obj.getFechaVencimiento(), fechaActual);
            Notificacion notificacion = new Notificacion();
            notificacion.setDetalle("El seguro #" + obj.getNumero() + " de tipo " + obj.getTipoSeguro().getDescripcion() + ", emitido por " + obj.getCompanhia().getNombre() + " para el vehiculo " + obj.getVehiculo().getPlaca() + ", vence " + construirDescripcionDias(diasRestantes));
            notificacion.setEncabezado(tipoNotificacion.getDescripcion());
            notificacion.setFechaCreacion(fechaActual);
            notificacion.setFechaNotificacion(fechaActual);
            notificacion.setProcesada(false);
            notificacion.setTipoNotificacion(tipoNotificacion);
            notificaciones.add(notificacion);
        }

        List<Licencia> licencias = licenciaRepository.getLicenciasProximoVencimiento();
        tipoNotificacion = tipoNotificacionRepository.findOne(Long.valueOf("2"));
        for (Licencia obj : licencias) {
            diasRestantes = StaticMembers.diasEntreFechas(obj.getVigencia(), fechaActual);
            Notificacion notificacion = new Notificacion();
            notificacion.setDetalle("La licencia de tipo " + obj.getTipoLicencia().getDescripcion() + " del conductor " + obj.getConductor().getPersona().getNombreCompleto() + " vence " + construirDescripcionDias(diasRestantes));
            notificacion.setEncabezado(tipoNotificacion.getDescripcion());
            notificacion.setFechaCreacion(fechaActual);
            notificacion.setFechaNotificacion(fechaActual);
            notificacion.setProcesada(false);
            notificacion.setTipoNotificacion(tipoNotificacion);
            notificaciones.add(notificacion);
        }

        List<Capacitacion> actividades = capacitacionRepository.getCapacitacionesProximoVencimiento();
        tipoNotificacion = tipoNotificacionRepository.findOne(Long.valueOf("3"));
        for (Capacitacion obj : actividades) {
            diasRestantes = StaticMembers.diasEntreFechas(obj.getFechaVencimiento(), fechaActual);
            Notificacion notificacion = new Notificacion();
            notificacion.setDetalle("La actividad '" + obj.getNombreCapacitacion() + "' de tipo " + obj.getTipoCapacitacion().getDescripcion() + " vence " + construirDescripcionDias(diasRestantes));
            notificacion.setEncabezado(tipoNotificacion.getDescripcion());
            notificacion.setFechaCreacion(fechaActual);
            notificacion.setFechaNotificacion(fechaActual);
            notificacion.setProcesada(false);
            notificacion.setTipoNotificacion(tipoNotificacion);
            notificaciones.add(notificacion);
        }
        adicionarNotificaciones(notificaciones);
    }

    @Scheduled(fixedRate = 7000)
    public void notificarTerceros() {

        List<Notificacion> notificaciones = notificacionRepository.findPendientes();

        if (notificaciones.size() > 0) {
            List<TerceroNotificacion> terceroNotificaciones = new ArrayList<>();
            List<Long> idsTercerosNotificados = new ArrayList<>();
            JavaMailSender mailSender = new JavaMailSender(parametrosRepository);
            List<MyEmail> correos = new ArrayList<>();

            for (Notificacion notificacion : notificaciones) {
                //List<InternetAddress> destinatariosCorreo = new ArrayList<>();
                List<String> listaDestinatariosCorreo = new ArrayList<>();
                TipoNotificacion tipoNotificacion = notificacion.getTipoNotificacion();
                Set<Cargo> cargosAgregados = tipoNotificacion.getCargos();

                if (tipoNotificacion.isAdmin()) {
                    List<Usuario> admins = usuarioRepository.findAdministradores(Long.valueOf("1"));
                    for (Usuario admin : admins) {
                        Persona persona = admin.getPersona();
                        Long idPersona = persona.getId();
                        if (!idsTercerosNotificados.contains(idPersona)) {
                            TerceroNotificacion terceroNotificacion = new TerceroNotificacion();
                            terceroNotificacion.setActiva(true);
                            terceroNotificacion.setLeido(false);
                            terceroNotificacion.setNotificado(false);
                            terceroNotificacion.setNotificacion(notificacion);
                            terceroNotificacion.setTercero(persona);
                            terceroNotificaciones.add(terceroNotificacion);
                            idsTercerosNotificados.add(idPersona);
                            applicationEventBus.publish(NUEVA_NOTIFICACION, this, terceroNotificacion);
                        }
                        
                        if (tipoNotificacion.isViaEmail()) {
                            String email = (persona.getCorreo() == null) ? "" : persona.getCorreo();
                            if (!email.equals("")) {
                                try {
                                    //destinatariosCorreo.add(new InternetAddress(email));
                                    listaDestinatariosCorreo.add(email);
                                } catch (Exception exc) {
                                    System.out.println(exc.getMessage());
                                }
                            }
                        }
                    }
                }

                for (Cargo cargo : cargosAgregados) {
                    String nombreCargo = cargo.getDescripcion().toLowerCase();
                    List<Persona> personas = new ArrayList<>();
                    if (nombreCargo.equals("empleado")) {
                        personas = personaRepository.findByEmpleadoNotNull();
                    } else if (nombreCargo.equals("conductor")) {
                        personas = personaRepository.findByConductorIsNotNull();
                    }
                    for (Persona persona : personas) {
                        Long idPersona = persona.getId();
                        if (!idsTercerosNotificados.contains(idPersona)) {
                            TerceroNotificacion terceroNotificacion = new TerceroNotificacion();
                            terceroNotificacion.setActiva(true);
                            terceroNotificacion.setLeido(false);
                            terceroNotificacion.setNotificado(false);
                            terceroNotificacion.setNotificacion(notificacion);
                            terceroNotificacion.setTercero(persona);
                            terceroNotificaciones.add(terceroNotificacion);
                            idsTercerosNotificados.add(idPersona);
                            applicationEventBus.publish(NUEVA_NOTIFICACION, this, terceroNotificacion);
                        }
                        if (tipoNotificacion.isViaEmail()) {
                            String email = (persona.getCorreo() == null) ? "" : persona.getCorreo();
                            if (!email.equals("")) {
                                try {
                                    //destinatariosCorreo.add(new InternetAddress(email));
                                    listaDestinatariosCorreo.add(email);
                                } catch (Exception exc) {
                                    System.out.println(exc.getMessage());
                                }
                            }
                        }
                    }
                }
                //InternetAddress[] arrDestinatarios = new InternetAddress[listaDestinatariosCorreo.size()];
                //destinatariosCorreo.toArray(arrDestinatarios);
                MyEmail email = new MyEmail(notificacion.getEncabezado(), notificacion.getDetalle(), listaDestinatariosCorreo);
                correos.add(email);
            }
            terceroNotificacionRepository.save(terceroNotificaciones);
            mailSender.enviarCorreos(correos);
            notificacionService.actualizarNotificacionesProcesadas(notificaciones);
            System.out.println("Terceros notificados a las " + formatoHoras.format(new Date()));
        }
    }
    
}
