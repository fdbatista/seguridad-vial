package com.earandap.vehiculos.ui.views.tiponotificacion;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.repository.ParametrosRepository;
import com.earandap.vehiculos.repository.TipoNotificacionRepository;
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
import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import org.vaadin.gridutil.renderer.EditDeleteButtonValueRenderer;
import org.vaadin.spring.events.EventBus;

/**
 * Created by Angel Luis on 12/26/2015.
 */
@SpringView(name = TipoNotificacionView.VIEW_NAME)
public class TipoNotificacionView extends SecureView implements EditDeleteButtonValueRenderer.EditDeleteButtonClickListener {

    private String fileDir;
    public static final String VIEW_NAME = "tipo-notificacion";

    @Autowired
    private EventBus.ApplicationEventBus applicationEventBus;

    @Autowired
    private TipoNotificacionFormWindows window;

    @Autowired
    private TipoNotificacionGrid grid;

    private PortletView content;

    private MenuBar bar;

    @Autowired
    private TipoNotificacionRepository tipoNotificacionRepository;
    
    @Autowired
    private ParametrosRepository parametrosRepository;

    @PostConstruct
    public void init() {
        bar = new MenuBar();
        bar.addItem("", FontAwesome.PLUS_SQUARE, menuItem -> {
            TipoNotificacionFormWindows win = (TipoNotificacionFormWindows) window.show(-1, fileDir);
            UI.getCurrent().addWindow(win);
        });
        content = new PortletView("Listado de Tipos de Notificaciones", FontAwesome.ADN, bar);
        content.setPrimaryStyleName("portlet-view");
        content.setSizeFull();
        content.addComponent(grid);
        grid.setEditDeleteListener(this);
        addComponent(content);
        fileDir = parametrosRepository.findOne(Long.valueOf("2")).getValorParametro();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        super.enter(viewChangeEvent);
        grid.setData(tipoNotificacionRepository.findAll());
    }

    @Override
    public void onEdit(ClickableRenderer.RendererClickEvent event) {
        long id = (long) event.getItemId();
        TipoNotificacionFormWindows win = (TipoNotificacionFormWindows) window.show(id, fileDir);
        UI.getCurrent().addWindow(win);
    }

    @Override
    public void onDelete(ClickableRenderer.RendererClickEvent event) {
        MessageBox messageBox = MessageBox.showPlain(Icon.QUESTION, "Interrogante", "Est\u00E1 seguro de que quiere eliminar?", ButtonId.YES, ButtonId.NO);
        messageBox.getButton(ButtonId.YES).addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent e) {
                try {
                    Long elemId = (Long) event.getItemId();
                    String icono = tipoNotificacionRepository.findOne(elemId).getIcono();
                    tipoNotificacionRepository.delete(elemId);
                    if (tipoNotificacionRepository.findOne(elemId) != null) {
                        StaticMembers.showNotificationWarning("Advertencia", "El elemento no pudo ser eliminado porque posee dependencias");
                    } else {
                        applicationEventBus.publish(TipoNotificacionGrid.ELIMINADO_TIPONOTIFICACION, this, event.getItemId());
                        StaticMembers.eliminarFichero(new File(StaticMembers.construirRutaIconoNotificacion(parametrosRepository.findOne(Long.valueOf("2")).getValorParametro(), icono)));
                        StaticMembers.showNotificationMessage("Notificaci\u00F3n", "Elemento eliminado con \u00E9xito");
                    }
                } catch (org.hibernate.exception.ConstraintViolationException | org.springframework.dao.DataIntegrityViolationException exc) {
                    StaticMembers.showNotificationWarning("Advertencia", "El elemento no pudo ser eliminado porque posee dependencias");
                } catch (Exception exc) {
                    StaticMembers.showNotificationError("Error", "La operaci\u00F3n no se ha podido completar");
                }
            }
        });
    }

    @Override
    public String getViewName() {
        return VIEW_NAME;
    }
}
