package com.earandap.vehiculos.ui.views.empleados;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.Persona;
import com.earandap.vehiculos.repository.EmpleadoRepository;
import com.earandap.vehiculos.repository.PersonaRepository;
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
 * Created by Angel Luis on 10/13/2015.
 */
@SpringView(name = EmpleadoView.VIEW_NAME)
public class EmpleadoView extends SecureView implements EditDeleteButtonValueRenderer.EditDeleteButtonClickListener {
    
    public static final String VIEW_NAME = "empleados";

    @Autowired
    private EventBus.ApplicationEventBus applicationEventBus;

    @Autowired
    private EmpleadoGrid grid;

    @Autowired
    private EmpleadoFormWindows window;

    private PortletView content;

    private MenuBar bar;

    @Autowired
    private PersonaRepository personaRepository;
    
    @Autowired
    private EmpleadoRepository empleadoRepository;

    @PostConstruct
    public void init(){
        bar = new MenuBar();
        bar.addItem("", FontAwesome.PLUS_SQUARE, menuItem -> {
            EmpleadoFormWindows win = (EmpleadoFormWindows) window.show(-1);
            UI.getCurrent().addWindow(win);

        });
        content = new PortletView("Listado de Empleados", FontAwesome.ADN, bar);
        content.setPrimaryStyleName("portlet-view");
        content.setSizeFull();
        content.addComponent(grid);
        grid.setEditDeleteListener(this);
        addComponent(content);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        super.enter(viewChangeEvent);
        grid.setData(personaRepository.findByEmpleadoNotNull());
    }

    @Override
    public void onEdit(ClickableRenderer.RendererClickEvent event) {
        long id = (long) event.getItemId();
        EmpleadoFormWindows win = (EmpleadoFormWindows) window.show(id);
        UI.getCurrent().addWindow(win);
    }

    @Override
    public void onDelete(ClickableRenderer.RendererClickEvent event) {
        MessageBox messageBox = MessageBox.showPlain(Icon.QUESTION, "Interrogante", "Est\u00E1 seguro de que quiere eliminar?", ButtonId.YES, ButtonId.NO);
        messageBox.getButton(ButtonId.YES).addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent e) {
                try {
                    Long personaId = (Long) event.getItemId();
                    Persona persona = personaRepository.findOne(personaId);
                    Long empleadoId = persona.getEmpleado().getId();
                    persona.setEmpleado(null);
                    personaRepository.save(persona);
                    empleadoRepository.delete(empleadoId);
                    if (empleadoRepository.findOne(empleadoId) != null) {
                        StaticMembers.showNotificationWarning("Advertencia", "El elemento no pudo ser eliminado porque posee dependencias");
                    } else {
                        applicationEventBus.publish(EmpleadoGrid.ELIMINADO_EMPLEADO, this, event.getItemId());
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


