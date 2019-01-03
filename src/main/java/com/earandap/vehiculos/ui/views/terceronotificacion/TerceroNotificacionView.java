package com.earandap.vehiculos.ui.views.terceronotificacion;

import com.earandap.vehiculos.domain.TerceroNotificacion;
import com.earandap.vehiculos.domain.Usuario;
import com.earandap.vehiculos.repository.TerceroNotificacionRepository;
import com.earandap.vehiculos.ui.MainUI;
import com.earandap.vehiculos.ui.components.PortletView;
import com.earandap.vehiculos.ui.views.SecureView;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.renderers.ClickableRenderer;
import com.vaadin.ui.themes.ValoTheme;
import de.steinwedel.messagebox.ButtonId;
import de.steinwedel.messagebox.Icon;
import de.steinwedel.messagebox.MessageBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.gridutil.renderer.EditDeleteButtonValueRenderer;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.security.VaadinSecurity;

import javax.annotation.PostConstruct;

/**
 * Created by angel on 13/04/16.
 */
@SpringView(name = TerceroNotificacionView.VIEW_NAME)
public class TerceroNotificacionView extends SecureView implements EditDeleteButtonValueRenderer.EditDeleteButtonClickListener{

    public static final String VIEW_NAME = "terceronotificacion";

    @Autowired
    private TerceroNotificacionFormWindows window;

    @Autowired
    private TerceroNotificacionGrid grid;

    private PortletView content;

    private MenuBar bar;

    @Autowired
    private EventBus.ApplicationEventBus applicationEventBus;

    @Autowired
    private TerceroNotificacionRepository terceroNotificacionRepository;

    @PostConstruct
    public void init(){
        bar = new MenuBar();
        bar.setVisible(false);
        bar.addItem("", FontAwesome.PLUS_SQUARE, menuItem -> {
            TerceroNotificacionFormWindows win = (TerceroNotificacionFormWindows) window.show(new TerceroNotificacion.Id());
            UI.getCurrent().addWindow(win);
        });
        content = new PortletView("Listado de Tercero-Notificacion", FontAwesome.ADN, bar);
        content.setPrimaryStyleName("portlet-view");
        content.setSizeFull();
        content.addComponent(grid);
        grid.setEditListener(this);
        addComponent(content);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        super.enter(viewChangeEvent);
        VaadinSecurity vaadinSecurity = ((MainUI) UI.getCurrent()).getSecurity();
        Usuario user = ((Usuario) vaadinSecurity.getAuthentication().getPrincipal());
        //grid.setData(terceroNotificacionRepository.notificacionesByPerson(user.getPersona()));
        grid.setData(terceroNotificacionRepository.ultimasNotificacionesByPerson(user.getPersona().getId()));
    }


    @Override
    public void onEdit(ClickableRenderer.RendererClickEvent rendererClickEvent) {
        TerceroNotificacion.Id id = (TerceroNotificacion.Id) rendererClickEvent.getItemId();
        TerceroNotificacionFormWindows win = (TerceroNotificacionFormWindows) window.show(id);
        UI.getCurrent().addWindow(win);
    }

    @Override
    public void onDelete(ClickableRenderer.RendererClickEvent rendererClickEvent) {
        MessageBox messageBox = MessageBox.showPlain(
                Icon.QUESTION,
                "Interrogante",
                "Est\u00E1 seguro que quiere eliminar?",
                ButtonId.YES,
                ButtonId.NO);
        messageBox.getButton(ButtonId.YES).addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent e) {
                terceroNotificacionRepository.delete((TerceroNotificacion.Id) rendererClickEvent.getItemId());
                applicationEventBus.publish(TerceroNotificacionGrid.DELETE_TERCERONOTIFICACION, this, rendererClickEvent.getItemId());
                Notification notification = new Notification("Notificaci\u00F3n", "Notificacion de tercero eliminado con \u00E9xito", Notification.Type.TRAY_NOTIFICATION);
                notification.setDelayMsec(3000);
                notification.setIcon(FontAwesome.INFO_CIRCLE);
                notification.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
                notification.show(Page.getCurrent());
            }
        });
    }

    @Override
    public String getViewName() {
        return VIEW_NAME;
    }
}
