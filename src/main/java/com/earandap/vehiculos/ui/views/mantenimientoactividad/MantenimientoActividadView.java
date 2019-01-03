package com.earandap.vehiculos.ui.views.mantenimientoactividad;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.MantenimientoActividad;
import com.earandap.vehiculos.repository.MantenimientoActividadRepository;
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
 * Created by Angel Luis on 10/24/2015.
 */
@SpringView(name = MantenimientoActividadView.VIEW_NAME)
public class MantenimientoActividadView extends SecureView implements EditDeleteButtonValueRenderer.EditDeleteButtonClickListener{

    public static final String VIEW_NAME = "mantenimientoactividad";

    @Autowired
    private MantenimientoActividadFormWindows window;

    @Autowired
    private MantenimientoActividadGrid grid;

    private PortletView content;

    private MenuBar bar;

    @Autowired
    private EventBus.ApplicationEventBus applicationEventBus;

    @Autowired
    private MantenimientoActividadRepository mantenimientoActividadRepository;

    @PostConstruct
    public void init(){
        bar = new MenuBar();
        bar.addItem("", FontAwesome.PLUS_SQUARE, menuItem -> {
            MantenimientoActividadFormWindows win = (MantenimientoActividadFormWindows) window.show(new MantenimientoActividad.Id());
            UI.getCurrent().addWindow(win);
        });
        content = new PortletView("Listado de Mantenimientos-Actividad", FontAwesome.ADN, bar);
        content.setPrimaryStyleName("portlet-view");
        content.setSizeFull();
        content.addComponent(grid);
        grid.setEditListener(this);
        addComponent(content);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        super.enter(viewChangeEvent);
        grid.setData(mantenimientoActividadRepository.findAll());
    }
    
    private void eliminarObjeto(MantenimientoActividad.Id id, boolean mostrarAlerta) {
        mantenimientoActividadRepository.delete(id);
        applicationEventBus.publish(MantenimientoActividadGrid.DELETE_MANTENIMIENTOACTIVIDAD, this, id);
        if (mostrarAlerta) {
            StaticMembers.showNotificationMessage("Notificaci\u00F3n", "Alistamiento eliminado con \u00E9xito");
        }
    }

    @Override
    public void onEdit(ClickableRenderer.RendererClickEvent rendererClickEvent) {
        MantenimientoActividad.Id id = (MantenimientoActividad.Id) rendererClickEvent.getItemId();
        MantenimientoActividadFormWindows win = (MantenimientoActividadFormWindows) window.show(id);
        UI.getCurrent().addWindow(win);
        eliminarObjeto(id, false);
    }

    @Override
    public void onDelete(ClickableRenderer.RendererClickEvent event) {
        MessageBox messageBox = MessageBox.showPlain(
                Icon.QUESTION,
                "Mantenimiento-Actividad",
                "Est\u00E1 seguro que quiere eliminar?",
                ButtonId.YES,
                ButtonId.NO);
        messageBox.getButton(ButtonId.YES).addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent e) {
                eliminarObjeto((MantenimientoActividad.Id) event.getItemId(), true);
            }
        });
    }

    @Override
    public String getViewName() {
        return VIEW_NAME;
    }
}
