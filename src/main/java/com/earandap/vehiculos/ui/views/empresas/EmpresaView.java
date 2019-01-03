package com.earandap.vehiculos.ui.views.empresas;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.repository.EmpresaRepository;
import com.earandap.vehiculos.repository.ParametrosRepository;
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
 * Created by Angel Luis on 10/5/2015.
 */
@SpringView(name = EmpresaView.VIEW_NAME)
public class EmpresaView extends SecureView implements EditDeleteButtonValueRenderer.EditDeleteButtonClickListener {

    @Autowired
    private EventBus.ApplicationEventBus applicationEventBus;

    public static final String VIEW_NAME = "empresas";

    @Autowired
    private EmpresaFormWindow window;
    
    @Autowired
    private ParametrosRepository parametrosRepository;

    @Autowired
    private EmpresaGrid grid;

    private PortletView content;

    private MenuBar bar;

    @Autowired
    private EmpresaRepository empresaRepository;

    @PostConstruct
    public void init() {
        bar = new MenuBar();
        bar.addItem("", FontAwesome.PLUS_SQUARE, menuItem -> {
            EmpresaFormWindow win = (EmpresaFormWindow) window.show(-1);
            UI.getCurrent().addWindow(win);

        });
        content = new PortletView("Listado de Empresas", FontAwesome.ADN, bar);
        content.setPrimaryStyleName("portlet-view");
        content.setSizeFull();
        content.addComponent(grid);
        grid.setEditDeleteListener(this);
        addComponent(content);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        super.enter(viewChangeEvent);
        grid.setData(empresaRepository.findAll());
    }

    @Override
    public String getViewName() {
        return VIEW_NAME;
    }

    @Override
    public void onEdit(ClickableRenderer.RendererClickEvent event) {
        long id = (long) event.getItemId();
        EmpresaFormWindow win = (EmpresaFormWindow) window.show(id);
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
                    empresaRepository.delete(elemId);
                    if (empresaRepository.findOne(elemId) != null) {
                        StaticMembers.showNotificationWarning("Advertencia", "El elemento no pudo ser eliminado porque posee dependencias");
                    } else {
                        StaticMembers.eliminarCarpetaEmpresa(parametrosRepository.findOne(Long.valueOf("2")).getValorParametro(), elemId);
                        applicationEventBus.publish(EmpresaGrid.ELIMINADO_EMPRESA, this, event.getItemId());
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

}
