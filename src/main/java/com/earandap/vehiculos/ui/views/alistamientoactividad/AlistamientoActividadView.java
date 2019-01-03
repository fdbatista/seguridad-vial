package com.earandap.vehiculos.ui.views.alistamientoactividad;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.AlistamientoActividad;
import com.earandap.vehiculos.repository.AlistamientoActividadRepository;
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
import org.vaadin.gridutil.renderer.EditDeleteButtonValueRenderer;
import org.vaadin.spring.events.EventBus;

import javax.annotation.PostConstruct;

/**
 * Created by Angel Luis on 11/2/2015.
 */
@SpringView(name = AlistamientoActividadView.VIEW_NAME)
public class AlistamientoActividadView extends SecureView {

    public static final String VIEW_NAME = "alistamientoactividad";

    @Autowired
    private AlistamientoActividadFormWindows window;

    @Autowired
    private AlistamientoActividadGrid grid;

    private PortletView content;

    private MenuBar bar;

    @Autowired
    private EventBus.ApplicationEventBus applicationEventBus;

    @Autowired
    private AlistamientoActividadRepository alistamientoActividadRepository;

    @PostConstruct
    public void init() {
        bar = new MenuBar();
        bar.addItem("", FontAwesome.PLUS_SQUARE, menuItem -> {
            AlistamientoActividadFormWindows win = (AlistamientoActividadFormWindows) window.show(new AlistamientoActividad.Id());
            UI.getCurrent().addWindow(win);
        });
        content = new PortletView("Listado de Alistamientos-Actividad", FontAwesome.ADN, bar);
        content.setPrimaryStyleName("portlet-view");
        content.setSizeFull();
        content.addComponent(grid);
        grid.setEditListener(new EditDeleteButtonValueRenderer.EditDeleteButtonClickListener() {
            @Override
            public void onEdit(ClickableRenderer.RendererClickEvent event) {
                AlistamientoActividad.Id id = (AlistamientoActividad.Id) event.getItemId();
                AlistamientoActividadFormWindows win = (AlistamientoActividadFormWindows) window.show(id);
                UI.getCurrent().addWindow(win);
                eliminarObjeto(id, false);
            }

            @Override
            public void onDelete(ClickableRenderer.RendererClickEvent event) {
                MessageBox messageBox = MessageBox.showPlain(
                        Icon.QUESTION,
                        "Alistamiento-Actividad",
                        "Est\u00E1 seguro que quiere eliminar?",
                        ButtonId.YES,
                        ButtonId.NO);
                messageBox.getButton(ButtonId.YES).addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent e) {
                        eliminarObjeto((AlistamientoActividad.Id) event.getItemId(), true);
                    }
                });
            }
        });
        addComponent(content);
    }

    private void eliminarObjeto(AlistamientoActividad.Id id, boolean mostrarAlerta) {
        alistamientoActividadRepository.delete(id);
        applicationEventBus.publish(AlistamientoActividadGrid.DELETE_ALISTAMIENTOACTIVIDAD, this, id);
        if (mostrarAlerta) {
            StaticMembers.showNotificationMessage("Notificaci\u00F3n", "Alistamiento eliminado con \u00E9xito");
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        super.enter(viewChangeEvent);
        grid.setData(alistamientoActividadRepository.findAll());
    }

    @Override
    public String getViewName() {
        return VIEW_NAME;
    }

}
