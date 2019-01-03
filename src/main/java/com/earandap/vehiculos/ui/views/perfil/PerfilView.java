package com.earandap.vehiculos.ui.views.perfil;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.Perfil;
import com.earandap.vehiculos.repository.PerfilRepository;
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
 * Created by Angel Luis on 11/11/2015.
 */
@SpringView(name = PerfilView.VIEW_NAME)
public class PerfilView extends SecureView implements EditDeleteButtonValueRenderer.EditDeleteButtonClickListener {

    public static final String VIEW_NAME = "perfiles";

    @Autowired
    private PerfilFormWindows window;

    @Autowired
    private PerfilGrid grid;

    private PortletView content;

    private MenuBar bar;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private EventBus.ApplicationEventBus applicationEventBus;

    @PostConstruct
    public void init() {
        bar = new MenuBar();
        bar.addItem("", FontAwesome.PLUS_SQUARE, menuItem -> {
            PerfilFormWindows win = (PerfilFormWindows) window.show(-1);
            UI.getCurrent().addWindow(win);
        });
        content = new PortletView("Listado de Perfiles", FontAwesome.ADN, bar);
        content.setPrimaryStyleName("portlet-view");
        content.setSizeFull();
        content.addComponent(grid);
        grid.setEditDeleteListener(this);
        addComponent(content);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        super.enter(viewChangeEvent);
        grid.setData(perfilRepository.findAll());
    }

    @Override
    public void onEdit(ClickableRenderer.RendererClickEvent event) {
        long id = (long) event.getItemId();
        PerfilFormWindows win = (PerfilFormWindows) window.show(id);
        UI.getCurrent().addWindow(win);
    }

    @Override
    public void onDelete(ClickableRenderer.RendererClickEvent event) {
        MessageBox messageBox = MessageBox.showPlain(Icon.QUESTION, "Interrogante", "Est\u00E1 seguro de que quiere eliminar?", ButtonId.YES, ButtonId.NO);
        messageBox.getButton(ButtonId.YES).addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent e) {
                Object itemId = event.getItemId();
                Perfil perfil = grid.container.getItem(itemId).getBean();
                int cantUsuarios = perfil.getUsuarios().size();
                if (cantUsuarios > 0) {
                    StaticMembers.showNotificationError("Notificaci\u00F3n", (cantUsuarios == 1 ? "Existe 1 usuario con este perfil asignado" : String.format("Existen %1$d usuarios con este perfil asignado", cantUsuarios)));
                } else {
                    perfilRepository.delete((Long) event.getItemId());
                    applicationEventBus.publish(PerfilGrid.ELIMINADO_PERFIL, this, event.getItemId());
                    StaticMembers.showNotificationMessage("Notificaci\u00F3n", "Perfil eliminado con \u00E9xito");
                }
            }
        });
    }

    @Override
    public String getViewName() {
        return VIEW_NAME;
    }
}
