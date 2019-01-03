package com.earandap.vehiculos.ui.views.contrato;

import com.earandap.vehiculos.repository.ContratoRepository;
import com.earandap.vehiculos.ui.components.PortletView;
import com.earandap.vehiculos.ui.views.SecureView;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.renderers.ClickableRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.gridutil.renderer.EditButtonValueRenderer;

import javax.annotation.PostConstruct;

/**
 * Created by Angel Luis on 11/29/2015.
 */
@SpringView(name = ContratoView.VIEW_NAME)
public class ContratoView extends SecureView implements EditButtonValueRenderer.RendererClickListener{

    public static final String VIEW_NAME = "contratos";

    @Autowired
    private ContratoFormWindows window;

    @Autowired
    private ContratoGrid grid;

    private PortletView content;

    private MenuBar bar;

    @Autowired
    private ContratoRepository contratoRepository;

    @PostConstruct
    public void init(){
        bar = new MenuBar();
        bar.addItem("", FontAwesome.PLUS_SQUARE, menuItem -> {
            ContratoFormWindows win = (ContratoFormWindows) window.show(-1);
            UI.getCurrent().addWindow(win);
        });
        content = new PortletView("Listado de Contratos", FontAwesome.ADN, bar);
        content.setPrimaryStyleName("portlet-view");
        content.setSizeFull();
        content.addComponent(grid);
        grid.setEditListener(this);
        addComponent(content);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        super.enter(viewChangeEvent);
        grid.setData(contratoRepository.findAll());
    }


    @Override
    public void click(ClickableRenderer.RendererClickEvent event) {
        long id = (long) event.getItemId();
        ContratoFormWindows win = (ContratoFormWindows) window.show(id);
        UI.getCurrent().addWindow(win);
    }

    @Override
    public String getViewName() {
        return VIEW_NAME;
    }
}

