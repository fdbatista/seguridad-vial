package com.earandap.vehiculos.ui.components;

import com.earandap.vehiculos.ui.MainUI;
import com.earandap.vehiculos.ui.MenuItemType;
import com.earandap.vehiculos.ui.views.alistamiento.AlistamientoView;
import com.earandap.vehiculos.ui.views.alistamientoactividad.AlistamientoActividadView;
import com.earandap.vehiculos.ui.views.capacitacion.CapacitacionView;
import com.earandap.vehiculos.ui.views.conductores.ConductorView;
import com.earandap.vehiculos.ui.views.contrato.ContratoView;
import com.earandap.vehiculos.ui.views.empleados.EmpleadoView;
import com.earandap.vehiculos.ui.views.empresas.EmpresaView;
import com.earandap.vehiculos.ui.views.incidentes.IncidenteView;
import com.earandap.vehiculos.ui.views.indicadores.AccidentalidadEmpresaView;
import com.earandap.vehiculos.ui.views.indicadores.CapacitacionesSeguridadVialView;
import com.earandap.vehiculos.ui.views.indicadores.CausasView;
import com.earandap.vehiculos.ui.views.indicadores.InfraccionesTransitoView;
import com.earandap.vehiculos.ui.views.indicadores.InspeccionesSeguridadView;
import com.earandap.vehiculos.ui.views.indicadores.PruebasAlcoholemiaView;
import com.earandap.vehiculos.ui.views.infracciones.InfraccionView;
import com.earandap.vehiculos.ui.views.mantenimiento.MantenimientoView;
import com.earandap.vehiculos.ui.views.mantenimientoactividad.MantenimientoActividadView;
import com.earandap.vehiculos.ui.views.nomenclador.NomencladorView;
import com.earandap.vehiculos.ui.views.notificacion.NotificacionView;
import com.earandap.vehiculos.ui.views.parametros.ParametrosView;
import com.earandap.vehiculos.ui.views.perfil.PerfilView;
import com.earandap.vehiculos.ui.views.persona.PersonaView;
import com.earandap.vehiculos.ui.views.sucursales.SucursalView;
import com.earandap.vehiculos.ui.views.terceronotificacion.TerceroNotificacionView;
import com.earandap.vehiculos.ui.views.tipomensaje.TipoMensajeView;
import com.earandap.vehiculos.ui.views.tipomensajenotificacion.TipoMenNotView;
import com.earandap.vehiculos.ui.views.tiponotificacion.TipoNotificacionView;
import com.earandap.vehiculos.ui.views.usuario.UsuarioView;
import com.earandap.vehiculos.ui.views.vehiculos.VehiculoView;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.UI;

import java.util.*;

/**
 * @Autor Eduardo Aranda
 * @Version 0.1
 */
public class CustomMenu extends CssLayout {

    private LinkedHashMap<String, NeoMenuItem> menuItems = new LinkedHashMap<>();

    public CustomMenu() {
        setPrimaryStyleName("page-sidebar-menu");
        setWidth("100%");

        menuItems.put(AlistamientoView.VIEW_NAME, new NeoMenuItem("Alistamiento", FontAwesome.BARS, Arrays.asList("Administrador"), MenuItemType.SEGURIDAD_VIAL));
        menuItems.put(MantenimientoView.VIEW_NAME, new NeoMenuItem("Mantenimiento", FontAwesome.GEAR, Arrays.asList("Administrador"), MenuItemType.SEGURIDAD_VIAL));
        menuItems.put(IncidenteView.VIEW_NAME, new NeoMenuItem("Incidentes", FontAwesome.BRIEFCASE, Arrays.asList("Administrador"), MenuItemType.SEGURIDAD_VIAL));
        menuItems.put(CapacitacionView.VIEW_NAME, new NeoMenuItem("Cronograma de Actividades", FontAwesome.CALENDAR, Arrays.asList("Administrador"), MenuItemType.SEGURIDAD_VIAL));
        menuItems.put(InfraccionView.VIEW_NAME, new NeoMenuItem("Infracciones", FontAwesome.CERTIFICATE, Arrays.asList("Administrador"), MenuItemType.SEGURIDAD_VIAL));
        menuItems.put(ContratoView.VIEW_NAME, new NeoMenuItem("Contratos", FontAwesome.SUITCASE, Arrays.asList("Administrador"), MenuItemType.SEGURIDAD_VIAL));

        menuItems.put(VehiculoView.VIEW_NAME, new NeoMenuItem("Veh\u00EDculos", FontAwesome.AUTOMOBILE, Arrays.asList("Administrador"), MenuItemType.CONFIGURACION_TRANSPORTE));
        menuItems.put(ConductorView.VIEW_NAME, new NeoMenuItem("Conductores", FontAwesome.TRUCK, Arrays.asList("Administrador"), MenuItemType.CONFIGURACION_TRANSPORTE));
        menuItems.put(AlistamientoActividadView.VIEW_NAME, new NeoMenuItem("Alistamiento-Actividad", FontAwesome.CERTIFICATE, Arrays.asList("Administrador"), MenuItemType.CONFIGURACION_TRANSPORTE));
        menuItems.put(MantenimientoActividadView.VIEW_NAME, new NeoMenuItem("Mantenimiento-Actividad", FontAwesome.CERTIFICATE, Arrays.asList("Administrador"), MenuItemType.CONFIGURACION_TRANSPORTE));

        menuItems.put(EmpresaView.VIEW_NAME, new NeoMenuItem("Empresas", FontAwesome.BRIEFCASE, Arrays.asList("Administrador"), MenuItemType.CONFIGURACION_EMPRESA));
        menuItems.put(EmpleadoView.VIEW_NAME, new NeoMenuItem("Empleados", FontAwesome.USER, Arrays.asList("Administrador"), MenuItemType.CONFIGURACION_EMPRESA));
        menuItems.put(SucursalView.VIEW_NAME, new NeoMenuItem("Sucursales", FontAwesome.BUILDING, Arrays.asList("Administrador"), MenuItemType.CONFIGURACION_EMPRESA));

        menuItems.put(UsuarioView.VIEW_NAME, new NeoMenuItem("Usuarios", FontAwesome.USER, Arrays.asList("Administrador"), MenuItemType.ADMINISTRACION));
        menuItems.put(PerfilView.VIEW_NAME, new NeoMenuItem("Perfiles", FontAwesome.GROUP, Arrays.asList("Administrador"), MenuItemType.ADMINISTRACION));
        menuItems.put(PersonaView.VIEW_NAME, new NeoMenuItem("Personas", FontAwesome.MALE, Arrays.asList("Administrador"), MenuItemType.ADMINISTRACION));
        menuItems.put(NotificacionView.VIEW_NAME, new NeoMenuItem("Notificaciones", FontAwesome.ENVELOPE_SQUARE, Arrays.asList("Administrador"), MenuItemType.ADMINISTRACION));
        menuItems.put(TipoMensajeView.VIEW_NAME, new NeoMenuItem("Tipos de Mensajes", FontAwesome.INBOX, Arrays.asList("Administrador"), MenuItemType.ADMINISTRACION));
        menuItems.put(TipoNotificacionView.VIEW_NAME, new NeoMenuItem("Tipos de Notificaciones", FontAwesome.ENVELOPE, Arrays.asList("Administrador"), MenuItemType.ADMINISTRACION));
        menuItems.put(TipoMenNotView.VIEW_NAME, new NeoMenuItem("Notificaciones-Mensajes", FontAwesome.ENVELOPE_O, Arrays.asList("Administrador"), MenuItemType.ADMINISTRACION));
        menuItems.put(NomencladorView.VIEW_NAME, new NeoMenuItem("Nomencladores", FontAwesome.BOOK, Arrays.asList("Administrador"), MenuItemType.ADMINISTRACION));
        menuItems.put(TerceroNotificacionView.VIEW_NAME, new NeoMenuItem("Alertas", FontAwesome.WARNING, Arrays.asList("Administrador"), MenuItemType.ADMINISTRACION));
        menuItems.put(ParametrosView.VIEW_NAME, new NeoMenuItem("Parámetros Generales", FontAwesome.GEARS, Arrays.asList("Administrador"), MenuItemType.ADMINISTRACION));
        
        menuItems.put(AccidentalidadEmpresaView.VIEW_NAME, new NeoMenuItem("Accidentalidad Vial", FontAwesome.AMBULANCE, Arrays.asList("Administrador"), MenuItemType.INDICADORES));
        menuItems.put(CapacitacionesSeguridadVialView.VIEW_NAME, new NeoMenuItem("Capacitaciones Seg. Vial", FontAwesome.GRADUATION_CAP, Arrays.asList("Administrador"), MenuItemType.INDICADORES));
        menuItems.put(InfraccionesTransitoView.VIEW_NAME, new NeoMenuItem("Infracciones de Tr\u00e1nsito", FontAwesome.EXCLAMATION_TRIANGLE, Arrays.asList("Administrador"), MenuItemType.INDICADORES));
        menuItems.put(PruebasAlcoholemiaView.VIEW_NAME, new NeoMenuItem("Pruebas Alcoholemia", FontAwesome.BEER, Arrays.asList("Administrador"), MenuItemType.INDICADORES));
        menuItems.put(InspeccionesSeguridadView.VIEW_NAME, new NeoMenuItem("Inspecciones Seguridad", FontAwesome.VIDEO_CAMERA, Arrays.asList("Administrador"), MenuItemType.INDICADORES));
        menuItems.put(CausasView.VIEW_NAME, new NeoMenuItem("Causas Inmediatas y B\u00e1sicas", FontAwesome.BOOK, Arrays.asList("Administrador"), MenuItemType.INDICADORES));
        
    }

    public void updateMenu(MenuItemType type) {
        MainUI ui = (MainUI) UI.getCurrent();
        NeoMenuItem item = null;
        this.removeAllComponents();
        for (Map.Entry<String, NeoMenuItem> entry : menuItems.entrySet()) {
            item = entry.getValue();
            if (/*ui.getSecurity().hasAuthorities(item.getRoles()) &&*/type == item.getType()) {
                item.addClickListener(clickEvent -> ui.getNavigator().navigateTo(entry.getKey()));
                addComponent(item);
            }
        }
        if (item != null) {
            item.addStyleName("last");
            //item.addStyleName("active");
        }
    }

    public LinkedHashMap<String, NeoMenuItem> getMenuItems() {
        return menuItems;
    }

    public class NeoMenuItem extends Button {

        private List<String> roles = new ArrayList<>();
        private MenuItemType type;

        public NeoMenuItem(String caption, Resource icon) {
            super(caption, icon);
            setPrimaryStyleName("sidebar-menu-item");
            setWidth("100%");
        }

        public NeoMenuItem(String caption, Resource icon, List<String> roles, MenuItemType type) {
            this(caption, icon);
            this.roles = roles;
            this.type = type;
        }

        public String[] getRoles() {
            return (String[]) roles.toArray();
        }

        public MenuItemType getType() {
            return type;
        }

    }

}
