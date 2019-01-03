package com.earandap.vehiculos.ui.views.usuario;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.repository.UsuarioRepository;
import com.earandap.vehiculos.ui.components.PortletView;
import com.earandap.vehiculos.ui.views.SecureView;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.renderers.ClickableRenderer;
import de.steinwedel.messagebox.ButtonId;
import de.steinwedel.messagebox.Icon;
import de.steinwedel.messagebox.MessageBox;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import org.vaadin.gridutil.renderer.EditDeleteButtonValueRenderer;
import org.vaadin.spring.events.EventBus;

/**
 * Created by Angel Luis on 11/9/2015.
 */
@SpringView(name = UsuarioView.VIEW_NAME)
public class UsuarioView extends SecureView implements EditDeleteButtonValueRenderer.EditDeleteButtonClickListener {

    public static final String VIEW_NAME = "usuarios";

    @Autowired
    private UsuarioFormWindows window;

    @Autowired
    private UsuarioGrid grid;

    private PortletView content;

    private MenuBar bar;

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private EventBus.ApplicationEventBus applicationEventBus;

    @PostConstruct
    public void init() {
        bar = new MenuBar();
        bar.addItem("", FontAwesome.PLUS_SQUARE, menuItem -> {
            UsuarioFormWindows win = (UsuarioFormWindows) window.show(-1);
            UI.getCurrent().addWindow(win);
        });
        content = new PortletView("Listado de Usuarios", FontAwesome.ADN, bar);
        content.setPrimaryStyleName("portlet-view");
        content.setSizeFull();
        content.addComponent(grid);
        grid.setEditDeleteListener(this);
        addComponent(content);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        super.enter(viewChangeEvent);
        grid.setData(usuarioRepository.findAll());
    }

    /*@Override
    public void click(ClickableRenderer.RendererClickEvent event) {
        long id = (long) event.getItemId();
        UsuarioFormWindows win = (UsuarioFormWindows) window.show(id);
        UI.getCurrent().addWindow(win);
    }*/
    @Override
    public String getViewName() {
        return VIEW_NAME;
    }

    @Override
    public void onEdit(ClickableRenderer.RendererClickEvent event) {
        long id = (long) event.getItemId();
        UsuarioFormWindows win = (UsuarioFormWindows) window.show(id);
        UI.getCurrent().addWindow(win);
    }

    @Override
    public void onDelete(ClickableRenderer.RendererClickEvent event) {
        MessageBox messageBox = MessageBox.showPlain(Icon.QUESTION, "Interrogante", "Est\u00E1 seguro de que quiere eliminar?", ButtonId.YES, ButtonId.NO);
        messageBox.getButton(ButtonId.YES).addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent e) {
                usuarioRepository.delete((Long) event.getItemId());
                applicationEventBus.publish(UsuarioGrid.ELIMINADO_USUARIO, this, event.getItemId());
                StaticMembers.showNotificationMessage("Notificaci\u00F3n", "Usuario eliminado con \u00E9xito");
            }
        });
    }
}
