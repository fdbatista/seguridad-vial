package com.earandap.vehiculos.ui.views.tipomensajenotificacion;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.TipoNotificacionMensaje;
import com.earandap.vehiculos.repository.TipoMensajeNotificacionRepository;
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

import javax.annotation.PostConstruct;

/**
 * Created by Angel Luis on 12/26/2015.
 */
@SpringView(name = TipoMenNotView.VIEW_NAME)
public class TipoMenNotView extends SecureView {

    public static final String VIEW_NAME = "notificacion-mensaje";

    @Autowired
    private TipoMenNotFormWindows window;

    @Autowired
    private TipoMenNotGrid grid;

    private PortletView content;

    private MenuBar bar;

    @Autowired
    private EventBus.ApplicationEventBus applicationEventBus;

    @Autowired
    private TipoMensajeNotificacionRepository tipoMensajeNotificacionRepository;

    @PostConstruct
    public void init(){
        bar = new MenuBar();
        bar.addItem("", FontAwesome.PLUS_SQUARE, menuItem -> {
            TipoMenNotFormWindows win = (TipoMenNotFormWindows) window.show(new TipoNotificacionMensaje.Id());
            UI.getCurrent().addWindow(win);
        });
        content = new PortletView("Listado de Notificaciones-Mensajes", FontAwesome.ADN, bar);
        content.setPrimaryStyleName("portlet-view");
        content.setSizeFull();
        content.addComponent(grid);
        grid.setEditListener(new EditDeleteButtonValueRenderer.EditDeleteButtonClickListener() {
            @Override
            public void onEdit(ClickableRenderer.RendererClickEvent event) {
                TipoNotificacionMensaje.Id id = (TipoNotificacionMensaje.Id) event.getItemId();
                TipoMenNotFormWindows win = (TipoMenNotFormWindows) window.show(id);
                UI.getCurrent().addWindow(win);
                eliminarObjeto((TipoNotificacionMensaje.Id) event.getItemId(), false);
            }

            @Override
            public void onDelete(ClickableRenderer.RendererClickEvent event) {
                MessageBox messageBox = MessageBox.showPlain(
                        Icon.QUESTION,
                        "Notificaci\u00F3n-Mensaje",
                        "Est\u00E1 seguro que quiere eliminar?",
                        ButtonId.YES,
                        ButtonId.NO);
                messageBox.getButton(ButtonId.YES).addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent e) {
                        eliminarObjeto((TipoNotificacionMensaje.Id) event.getItemId(), true);
                    }
                });
            }
        });
        addComponent(content);
    }
    
    private void eliminarObjeto(TipoNotificacionMensaje.Id id, boolean mostrarAlerta) {
        tipoMensajeNotificacionRepository.delete(id);
        applicationEventBus.publish(TipoMenNotGrid.DELETE_TIPOMENNOT, this, id);
        if (mostrarAlerta) {
            StaticMembers.showNotificationMessage("Notificaci\u00F3n", "Elemento eliminado con \u00E9xito");
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        super.enter(viewChangeEvent);
        grid.setData(tipoMensajeNotificacionRepository.findAll());
    }

    @Override
    public String getViewName() {
        return VIEW_NAME;
    }

}


