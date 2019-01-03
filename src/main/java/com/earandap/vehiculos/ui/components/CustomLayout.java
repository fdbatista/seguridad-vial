package com.earandap.vehiculos.ui.components;

import com.earandap.vehiculos.components.NotificationSchedule;
import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.Notificacion;
import com.earandap.vehiculos.domain.TerceroNotificacion;
import com.earandap.vehiculos.domain.Usuario;
import com.earandap.vehiculos.repository.TerceroNotificacionRepository;
import com.earandap.vehiculos.ui.MainUI;
import com.earandap.vehiculos.ui.MenuItemType;
import com.earandap.vehiculos.ui.views.contrasena.ContrasenaView;
import com.earandap.vehiculos.ui.views.terceronotificacion.TerceroNotificacionView;
import com.google.gwt.thirdparty.guava.common.eventbus.SubscriberExceptionContext;
import com.google.gwt.thirdparty.guava.common.eventbus.SubscriberExceptionHandler;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;
import java.io.File;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;
import org.vaadin.spring.security.VaadinSecurity;

import javax.annotation.PreDestroy;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

//@SpringComponent
//@UIScope
public class CustomLayout extends VerticalLayout implements HeaderMenuClickListener, SubscriberExceptionHandler {

    CssLayout header = new CssLayout();
    HorizontalLayout pageContainer = new HorizontalLayout();
    CssLayout content = new CssLayout();
    CustomMenu menu = new CustomMenu();
    Label notificationsBadge = new Label();
    private EventBus.ApplicationEventBus eventBus;
    VaadinSecurity vaadinSecurity;
    private TerceroNotificacionRepository terceroNotificacionRepository;
    private Panel panelNotificaciones;
    private List<TerceroNotificacion> terceroNotificaciones;
    public String fileDir;
    private Link verTodasLasNotificaciones;

    public TerceroNotificacionRepository getTerceroNotificacionRepository() {
        return terceroNotificacionRepository;
    }

    public void setTerceroNotificacionRepository(TerceroNotificacionRepository terceroNotificacionRepository) {
        this.terceroNotificacionRepository = terceroNotificacionRepository;
        this.actualizarCountNotificaciones();
    }

    public EventBus.ApplicationEventBus getEventBus() {
        return eventBus;
    }

    public void setEventBus(EventBus.ApplicationEventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.subscribe(this);
    }

    Window modulosPopup;
    Window userPopup;
    Window notificationPopup;

    private LinkedHashSet<HeaderMenuClickListener> headerMenuClickListeners = null;

    private Usuario user;

    public void createNotificaciones() {

        if (notificationPopup == null) {

            notificationPopup = new Window();
            notificationPopup.setWidth(325.0f, Unit.PIXELS);
            notificationPopup.addStyleName("float-menu");
            notificationPopup.addStyleName("float-menu-notification");
            notificationPopup.setClosable(false);
            notificationPopup.setResizable(false);
            notificationPopup.setDraggable(false);
            notificationPopup.setCloseShortcut(ShortcutAction.KeyCode.ESCAPE, null);
            notificationPopup.setContent(new VerticalLayout() {
                {
                    setMargin(true);
                    addComponent(new VerticalLayout() {
                        {
                            HorizontalLayout encabezadoLayout = new HorizontalLayout();
                            Label title = new Label("Notificaciones");
                            title.addStyleName(ValoTheme.LABEL_H3);
                            title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
                            title.addStyleName("encabezado");
                            encabezadoLayout.addComponent(title);

                            Button eliminarTodas = new Button("Eliminar todas");
                            eliminarTodas.addStyleName(Reindeer.BUTTON_LINK);
                            eliminarTodas.setIcon(FontAwesome.TRASH_O);
                            eliminarTodas.addStyleName("boton-link");
                            eliminarTodas.addStyleName("boton-elim-notificaciones");
                            eliminarTodas.addClickListener((event) -> {
                                terceroNotificacionRepository.delete(terceroNotificacionRepository.notificacionesNoLeidasByPerson(user.getPersona()));
                                actualizarSoloCountNotificaciones();
                                actualizarPanelNotificaciones();
                            });
                            encabezadoLayout.addComponent(eliminarTodas);
                            encabezadoLayout.setComponentAlignment(eliminarTodas, Alignment.MIDDLE_RIGHT);
                            addComponent(encabezadoLayout);

                            panelNotificaciones = new Panel();
                            panelNotificaciones.addStyleName("contenedor-notificaciones");
                            panelNotificaciones.setSizeFull();
                            panelNotificaciones.setHeight("300px");

                            if (user.getPersona() != null) {
                                VerticalLayout contenedorNotificacionesLayout = new VerticalLayout();
                                terceroNotificaciones = terceroNotificacionRepository.notificacionesNoLeidasByPerson(user.getPersona());
                                if (terceroNotificaciones.size() > 0) {
                                    for (TerceroNotificacion terceroNotificacion : terceroNotificaciones) {

                                        Notificacion notifAux = terceroNotificacion.getId().getNotificacion();
                                        HorizontalLayout notificationLayout = new HorizontalLayout();
                                        notificationLayout.addStyleName("notification-item");

                                        //Icono de la notificacion
                                        Image imgIcono = new Image("");
                                        imgIcono.addStyleName("icono-notificacion");
                                        String icono = notifAux.getTipoNotificacion().getIcono(), rutaFichero = StaticMembers.construirRutaIconoNotificacion(fileDir, icono);
                                        File fichero = new File(rutaFichero);
                                        imgIcono.setIcon((fichero.exists() ? new FileResource(fichero) : FontAwesome.PICTURE_O));

                                        //Layout para organizar el encabezado, la fecha y el boton ampliar
                                        VerticalLayout descYFechaLayout = new VerticalLayout();
                                        descYFechaLayout.setWidth("210px");

                                        //Label para el titulo
                                        Label tituloNotif = new Label(notifAux.getEncabezado());
                                        tituloNotif.addStyleName("notification-title");

                                        //Label para la fecha
                                        Label fechaNotif = new Label(notifAux.getFechaCreacion().toString());
                                        fechaNotif.addStyleName("notification-time");

                                        //Boton ampliar
                                        Button btnLeerMas = new Button("Leer m\u00E1s");
                                        btnLeerMas.setIcon(FontAwesome.ARROW_CIRCLE_RIGHT);
                                        btnLeerMas.addStyleName(Reindeer.BUTTON_LINK);
                                        btnLeerMas.addStyleName("boton-link");
                                        btnLeerMas.addClickListener((event) -> {
                                            ampliarNotificacion(terceroNotificacion);
                                        });

                                        descYFechaLayout.addComponent(tituloNotif);
                                        descYFechaLayout.addComponent(fechaNotif);
                                        descYFechaLayout.addComponent(btnLeerMas);

                                        notificationLayout.addComponents(imgIcono, descYFechaLayout);
                                        contenedorNotificacionesLayout.addComponent(notificationLayout);

                                    }
                                } else {
                                    Label label = new Label("No hay notificaciones sin leer");
                                    label.addStyleName("notification-title");
                                    contenedorNotificacionesLayout.addComponent(label);
                                }
                                panelNotificaciones.setContent(contenedorNotificacionesLayout);
                                addComponent(panelNotificaciones);

                                verTodasLasNotificaciones = new Link("Ver todas", new ExternalResource("#!" + TerceroNotificacionView.VIEW_NAME));
                                verTodasLasNotificaciones.addStyleName("margin-top");
                                addComponent(verTodasLasNotificaciones);
                            }
                        }
                    });
                }
            });
        }
    }

    private TerceroNotificacion[] getNotificacionesAnteriorSiguiente(TerceroNotificacion terceroNotificacion) {
        TerceroNotificacion[] res = new TerceroNotificacion[]{null, null};
        for (int i = 0; i < terceroNotificaciones.size(); i++) {
            if (terceroNotificaciones.get(i).equals(terceroNotificacion)) {
                if (i > 0) {
                    res[0] = terceroNotificaciones.get(i - 1);
                }
                if (i < terceroNotificaciones.size() - 1) {
                    res[1] = terceroNotificaciones.get(i + 1);
                }
                break;
            }
        }
        return res;
    }

    private void ampliarNotificacion(TerceroNotificacion terceroNotificacion) {

        Notificacion notifAux = terceroNotificacion.getId().getNotificacion();
        TerceroNotificacion[] notifAntSig = getNotificacionesAnteriorSiguiente(terceroNotificacion);

        //Layout general
        VerticalLayout contenedorNotificacionesLayout = new VerticalLayout();

        //Contenedor de los elementos
        VerticalLayout notificationLayout = new VerticalLayout();
        notificationLayout.addStyleName("notification-item");

        //Botones de navegacion
        HorizontalLayout navegarLayout = new HorizontalLayout();

        //Regresar a todas las notificaciones
        Button btnRegresar = new Button("Regresar");
        btnRegresar.setId("boton-regresar-notif");
        btnRegresar.setIcon(FontAwesome.ARROW_CIRCLE_LEFT);
        btnRegresar.addStyleName(Reindeer.BUTTON_LINK);
        btnRegresar.addStyleName("boton-link");
        btnRegresar.addClickListener((event) -> {
            actualizarPanelNotificaciones();
        });

        //Notificacion anterior
        Button btnNotifAnt = new Button("");
        btnNotifAnt.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        btnNotifAnt.setId("boton-notif-ant");
        btnNotifAnt.setIcon(FontAwesome.ARROW_LEFT);
        btnNotifAnt.addStyleName(Reindeer.BUTTON_LINK);
        btnNotifAnt.addStyleName("boton-link");
        btnNotifAnt.setEnabled(notifAntSig[0] != null);
        btnNotifAnt.addClickListener((event) -> {
            ampliarNotificacion(notifAntSig[0]);
        });

        //Notificacion siguiente
        Button btnNotifSig = new Button("");
        btnNotifSig.setId("boton-notif-sig");
        btnNotifSig.setIcon(FontAwesome.ARROW_RIGHT);
        btnNotifSig.addStyleName(Reindeer.BUTTON_LINK);
        btnNotifSig.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        btnNotifSig.addStyleName("boton-link");
        btnNotifSig.setEnabled(notifAntSig[1] != null);
        btnNotifSig.addClickListener((event) -> {
            ampliarNotificacion(notifAntSig[1]);
        });

        //Encabezado
        Label titleLabel = new Label(notifAux.getEncabezado());
        titleLabel.addStyleName("notification-title");

        //Descripcion
        Label contentLabel = new Label(notifAux.getDetalle() + "\n");
        contentLabel.addStyleName("notification-content");

        //Boton Entendido
        Button btnEntendido = new Button("Entendido");
        btnEntendido.setId("boton-entendido-notif");
        btnEntendido.setIcon(FontAwesome.THUMBS_UP);
        btnEntendido.addStyleName(Reindeer.BUTTON_LINK);
        btnEntendido.addStyleName("boton-link");
        btnEntendido.setEnabled(!terceroNotificacion.isLeido());
        btnEntendido.addClickListener((event) -> {
            terceroNotificacion.setLeido(true);
            terceroNotificacionRepository.save(terceroNotificacion);
            actualizarSoloCountNotificaciones();
            btnEntendido.setEnabled(false);
            if (notifAntSig[1] != null && !notifAntSig[1].isLeido()) {
                ampliarNotificacion(notifAntSig[1]);
            }
            else if (notifAntSig[0] != null && !notifAntSig[0].isLeido()) {
                ampliarNotificacion(notifAntSig[0]);
            }
            else {
                actualizarPanelNotificaciones();
            }
        });

        navegarLayout.addComponents(btnRegresar, btnNotifAnt, btnNotifSig);
        notificationLayout.addComponents(navegarLayout, titleLabel, contentLabel, btnEntendido);
        contenedorNotificacionesLayout.addComponent(notificationLayout);

        panelNotificaciones.setContent(contenedorNotificacionesLayout);
    }

    private void actualizarPanelNotificaciones() {
        if (user.getPersona() != null) {
            VerticalLayout contenedorNotificacionesLayout = new VerticalLayout();
            terceroNotificaciones = terceroNotificacionRepository.notificacionesNoLeidasByPerson(user.getPersona());
            if (terceroNotificaciones.size() > 0) {
                for (TerceroNotificacion terceroNotificacion : terceroNotificaciones) {
                    Notificacion notifAux = terceroNotificacion.getId().getNotificacion();
                    HorizontalLayout notificationLayout = new HorizontalLayout();
                    notificationLayout.addStyleName("notification-item");

                    Image imgIcono = new Image("");
                    imgIcono.addStyleName("icono-notificacion");
                    String icono = notifAux.getTipoNotificacion().getIcono(), rutaFichero = StaticMembers.construirRutaIconoNotificacion(fileDir, icono);
                    File fichero = new File(rutaFichero);
                    imgIcono.setIcon((fichero.exists() ? new FileResource(fichero) : FontAwesome.PICTURE_O));

                    VerticalLayout descYFechaLayout = new VerticalLayout();
                    descYFechaLayout.setWidth("210px");
                    Label tituloNotif = new Label(notifAux.getEncabezado());
                    tituloNotif.addStyleName("notification-title");
                    Label fechaNotif = new Label(notifAux.getFechaCreacion().toString());
                    fechaNotif.addStyleName("notification-time");
                    //Boton ampliar
                    Button btnLeerMas = new Button("Leer m\u00E1s");
                    btnLeerMas.setIcon(FontAwesome.ARROW_CIRCLE_RIGHT);
                    btnLeerMas.addStyleName(Reindeer.BUTTON_LINK);
                    btnLeerMas.addStyleName("boton-link");
                    btnLeerMas.addClickListener((event) -> {
                        ampliarNotificacion(terceroNotificacion);
                    });
                    descYFechaLayout.addComponent(tituloNotif);
                    descYFechaLayout.addComponent(fechaNotif);
                    descYFechaLayout.addComponent(btnLeerMas);

                    notificationLayout.addComponents(imgIcono, descYFechaLayout);
                    contenedorNotificacionesLayout.addComponent(notificationLayout);
                }
            } else {
                Label label = new Label("No hay notificaciones sin leer");
                label.addStyleName("notification-title");
                contenedorNotificacionesLayout.addComponent(label);
            }
            panelNotificaciones.setContent(contenedorNotificacionesLayout);
        }
    }

    public CustomLayout() {

        vaadinSecurity = ((MainUI) UI.getCurrent()).getSecurity();
        user = ((Usuario) vaadinSecurity.getAuthentication().getPrincipal());
        setSizeFull();
        addClickHeaderMenuListener(this);

        //eventBus.subscribe(this);
        Button modulos = new Button();
        modulos.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        modulos.setIcon(FontAwesome.TH);
        modulos.addClickListener((Button.ClickListener) event -> {
            if (modulosPopup == null) {
                modulosPopup = new Window();
                modulosPopup.setWidth(300.0f, Unit.PIXELS);
                modulosPopup.addStyleName("float-menu");
                modulosPopup.setClosable(false);
                modulosPopup.setResizable(false);
                modulosPopup.setDraggable(false);
                modulosPopup.setCloseShortcut(ShortcutAction.KeyCode.ESCAPE, null);
                modulosPopup.setContent(new VerticalLayout() {
                    {
                        setMargin(true);
                        setSpacing(true);
                        addComponent(new HorizontalLayout() {
                            {
                                setMargin(true);
                                setSpacing(true);
                                setPrimaryStyleName("float-menu-column");
                                /* road safety menu item*/
                                PopupMenuItem roadItem = new PopupMenuItem("Seguridad Vial", FontAwesome.ROAD, MenuItemType.SEGURIDAD_VIAL);
                                roadItem.addClickListener(event -> {
                                    fireClickHeaderMenu(roadItem);
                                });
                                addComponent(roadItem);


                                /* transporte menu item*/
                                PopupMenuItem transportItem = new PopupMenuItem("Transporte", FontAwesome.AUTOMOBILE, MenuItemType.CONFIGURACION_TRANSPORTE);
                                transportItem.addClickListener(event -> {
                                    fireClickHeaderMenu(transportItem);
                                });
                                addComponent(transportItem);

                                PopupMenuItem item = new PopupMenuItem("Empresa", FontAwesome.HOME, MenuItemType.CONFIGURACION_EMPRESA);
                                item.addClickListener(event -> {
                                    fireClickHeaderMenu(item);
                                });
                                addComponent(item);
                            }
                        });

                        addComponent(new HorizontalLayout() {
                            {
                                setMargin(true);
                                setPrimaryStyleName("float-menu-column");

                                PopupMenuItem item = new PopupMenuItem("Administraci\u00F3n", FontAwesome.COGS, MenuItemType.ADMINISTRACION);
                                item.addClickListener(event -> {
                                    fireClickHeaderMenu(item);
                                });
                                addComponent(item);

                                PopupMenuItem indicItem = new PopupMenuItem("Indicadores", FontAwesome.BAR_CHART_O, MenuItemType.INDICADORES);
                                indicItem.addClickListener(event -> {
                                    fireClickHeaderMenu(indicItem);
                                });
                                addComponent(indicItem);

                            }
                        });

                    }
                });
            }

            if (!modulosPopup.isAttached()) {
                closeUserMenu();
                closeNotificacionesMenu();
                modulosPopup.setPositionY(event.getClientY() - event.getRelativeY() + 60);
                getUI().addWindow(modulosPopup);
                modulosPopup.focus();
            } else {
                modulosPopup.close();
            }
        });

        Button notificaciones;
        notificaciones = new Button();
        notificaciones.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        notificaciones.setIcon(FontAwesome.BELL);
        notificaciones.addClickListener((Button.ClickListener) event -> {
            this.createNotificaciones();

            if (!notificationPopup.isAttached()) {
                closeModulosMenu();
                closeUserMenu();
                notificationPopup.setPositionY(event.getClientY() - event.getRelativeY() + 60);
                getUI().addWindow(notificationPopup);
                notificationPopup.focus();
            } else {
                notificationPopup.close();
            }
        });

        Component aa = buildBadgeWrapper(notificaciones);

        Button user = new Button();
        user.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        user.setIcon(FontAwesome.USER);
        user.addClickListener((Button.ClickListener) event -> {
            if (userPopup == null) {

                userPopup = new Window();
                userPopup.setWidth(300.0f, Unit.PIXELS);
                userPopup.addStyleName("float-menu");
                userPopup.addStyleName("float-menu-user");
                userPopup.setClosable(false);
                userPopup.setResizable(false);
                userPopup.setDraggable(false);
                userPopup.setCloseShortcut(ShortcutAction.KeyCode.ESCAPE, null);
                userPopup.setContent(new VerticalLayout() {
                    {
                        setMargin(true);
                        setSpacing(true);
                        addComponent(new VerticalLayout() {
                            {
                                Usuario usuario = (Usuario) vaadinSecurity.getAuthentication().getPrincipal();
                                if (usuario.getPersona() != null) {
                                    Label nombre = new Label(usuario.getPersona().getNombreCompleto() + "<br/>" + usuario.getPersona().getCorreo(), ContentMode.HTML);
                                    addComponent(nombre);
                                }
                                HorizontalLayout horizontalLayout = new HorizontalLayout();
                                horizontalLayout.addComponent(new Link("Salir", new ExternalResource("/logout")));

                                Link contrasenaLink = new Link("Cambiar Contrasena", null);
                                contrasenaLink = new Link("Cambiar Contrasena", new ExternalResource("#!" + ContrasenaView.VIEW_NAME));
                                horizontalLayout.addComponent(contrasenaLink);

                                horizontalLayout.setSpacing(true);

                                addComponent(horizontalLayout);
                            }
                        });
                    }
                });
            }

            if (!userPopup.isAttached()) {
                closeModulosMenu();
                closeNotificacionesMenu();
                userPopup.setPositionY(event.getClientY() - event.getRelativeY() + 60);
                getUI().addWindow(userPopup);
                userPopup.focus();
            } else {
                userPopup.close();
            }
        });


        /*Setting up header*/
        header.setPrimaryStyleName("page-header navbar navbar-fixed-top");
        header.addComponent(new CssLayout() {
            {
                setPrimaryStyleName("top-menu");
                HorizontalLayout tools = new HorizontalLayout(new Label(((Usuario) vaadinSecurity.getAuthentication().getPrincipal()).getUsername()), modulos, aa/*notificaciones*/, user);
                tools.setSpacing(true);
                tools.addStyleName("toolbar");
                addComponent(tools);

            }
        });

        /*Setting up page container*/
        pageContainer.setPrimaryStyleName("page-container");
        pageContainer.setSizeFull();

        /*Setting up sidebar wrapper*/
        pageContainer.addComponent(new CssLayout() {
            {
                setPrimaryStyleName("page-sidebar-wrapper");

                addComponent(new CssLayout() {
                    {
                        setPrimaryStyleName("page-sidebar navbar-collapse collapse");
                        addComponent(menu);
                    }
                });
            }
        });

        /*Setting up page content wrapper */
        pageContainer.addComponent(new CssLayout() {
            {
                setPrimaryStyleName("page-content-wrapper");
                setSizeFull();
                addComponent(new CssLayout() {
                    {
                        setPrimaryStyleName("page-content");
                        setSizeFull();
                        content.setSizeFull();
                        addComponent(content);
                    }
                });

            }
        });
        pageContainer.setExpandRatio(pageContainer.getComponent(1), 1);


        /*Setting up root*/
        addComponents(header, pageContainer);
        setExpandRatio(pageContainer, 1);

        String page = ((MainUI) UI.getCurrent()).getPage().getUriFragment();
        if (page != null) {
            page = page.substring(1);
            Set<Map.Entry<String, CustomMenu.NeoMenuItem>> a = menu.getMenuItems().entrySet();
            CustomMenu.NeoMenuItem item = null;
            for (Map.Entry<String, CustomMenu.NeoMenuItem> entry : a) {
                item = entry.getValue();
                if (page.equals(entry.getKey())) {
                    menu.updateMenu(item.getType());
                }
            }
        } else {
            menu.updateMenu(MenuItemType.SEGURIDAD_VIAL);
        }

    }

    public void addClickHeaderMenuListener(HeaderMenuClickListener listener) {
        if (headerMenuClickListeners == null) {
            headerMenuClickListeners = new LinkedHashSet<>();
        }
        headerMenuClickListeners.add(listener);
    }

    public void fireClickHeaderMenu(PopupMenuItem item) {
        if (headerMenuClickListeners != null) {
            final Object[] listeners = headerMenuClickListeners.toArray();
            for (int i = 0; i < listeners.length; i++) {
                HeaderMenuClickListener listener = (HeaderMenuClickListener) listeners[i];
                listener.clickMenu(item);
            }
        }
    }

    public ComponentContainer getContentContainer() {
        return content;
    }

    public CustomMenu getMenu() {
        return menu;
    }

    @Override
    public void clickMenu(PopupMenuItem item) {
        menu.updateMenu(item.type);
        modulosPopup.close();
    }

    public static class PopupMenuItem extends Button {

        private MenuItemType type;

        public PopupMenuItem(String name, Resource icon, MenuItemType type, ClickListener action) {
            this(name, icon, type);
            addClickListener(action);
        }

        public PopupMenuItem(String name, Resource icon, MenuItemType type) {
            super(name, icon);
            this.type = type;
            setPrimaryStyleName("popup-menu-item");
        }

    }

    public void closeUserMenu() {
        if (userPopup != null) {
            userPopup.close();
        }
    }

    public void closeModulosMenu() {
        if (modulosPopup != null) {
            modulosPopup.close();
        }
    }

    public void closeNotificacionesMenu() {
        if (notificationPopup != null) {
            notificationPopup.close();
        }
    }

    private Component buildBadgeWrapper(Component menuItemButton) {

        CssLayout dashboardWrapper = new CssLayout(menuItemButton);
        dashboardWrapper.addStyleName("badgewrapper");
        dashboardWrapper.addStyleName(ValoTheme.MENU_ITEM);
        notificationsBadge.addStyleName(ValoTheme.MENU_BADGE);
        notificationsBadge.setWidthUndefined();
        notificationsBadge.setVisible(false);
        notificationsBadge.setId("ID-Label");
        notificationsBadge.setValue(String.valueOf(2));
        dashboardWrapper.addComponent(notificationsBadge);
        return dashboardWrapper;
    }

    @EventBusListenerTopic(topic = NotificationSchedule.NUEVA_NOTIFICACION)
    @EventBusListenerMethod
    public void nuevaNotificacionListener(TerceroNotificacion terceroNotificacion) {
        this.actualizarCountNotificaciones();
    }

    @PreDestroy
    void destroy() {
        eventBus.unsubscribe(this);
    }

    @Override
    public void handleException(Throwable throwable, SubscriberExceptionContext subscriberExceptionContext) {
        throwable.printStackTrace();
    }

    public void actualizarCountNotificaciones() {
        notificationPopup = null;
        this.createNotificaciones();
        int notificationCount = terceroNotificacionRepository.countNotificacionesNoLeidoByPerson(user.getPersona());
        notificationsBadge.setVisible(notificationCount > 0);
        notificationsBadge.setValue(String.valueOf(notificationCount));
    }

    public void actualizarSoloCountNotificaciones() {
        int notificationCount = terceroNotificacionRepository.countNotificacionesNoLeidoByPerson(user.getPersona());
        notificationsBadge.setVisible(notificationCount > 0);
        notificationsBadge.setValue(String.valueOf(notificationCount));
    }

    /*public static class NotificationsCountUpdatedEvent {
    }

    public static final class NotificationsButton extends Button {

        private static final String STYLE_UNREAD = "unread";
        public static final String ID = "dashboard-notifications";

        public NotificationsButton(EventBus.ApplicationEventBus eventBus) {
            setIcon(FontAwesome.BELL);
            setId(ID);
            addStyleName("notifications");
            addStyleName(ValoTheme.BUTTON_ICON_ONLY);
            eventBus.subscribe(this);
        }

        @Subscribe
        public void updateNotificationsCount(final NotificationsCountUpdatedEvent event) {
            //setUnreadCount(DashboardUI.getDataProvider().getUnreadNotificationsCount());
        }

        public void setUnreadCount(final int count) {
            setCaption(String.valueOf(count));

            String description = "Notifications";
            if (count > 0) {
                addStyleName(STYLE_UNREAD);
                description += " (" + count + " unread)";
            } else {
                removeStyleName(STYLE_UNREAD);
            }
            setDescription(description);
        }
    }*/
}
