package com.earandap.vehiculos.ui.views.parametros;

import com.earandap.vehiculos.domain.Parametro;
import com.earandap.vehiculos.repository.ParametrosRepository;
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
import java.util.List;

/**
 * Created by angel on 02/04/16.
 */
@SpringView(name = ParametrosView.VIEW_NAME)
public class ParametrosView extends SecureView implements EditButtonValueRenderer.RendererClickListener{

    public static final String VIEW_NAME = "parametros";

    @Autowired
    private ParametrosFormWindows window;

    @Autowired
    private ParametrosGrid grid;

    private PortletView content;

    private MenuBar bar;

    @Autowired
    private ParametrosRepository nomencladorRepository;

    @PostConstruct
    public void init(){
        bar = new MenuBar();
        bar.addItem("", FontAwesome.PLUS_SQUARE, menuItem -> {
            ParametrosFormWindows win = (ParametrosFormWindows) window.show(-1);
            UI.getCurrent().addWindow(win);
        });
        content = new PortletView("Listado de Parametros", FontAwesome.ADN, bar);
        content.setPrimaryStyleName("portlet-view");
        content.setSizeFull();
        content.addComponent(grid);
        grid.setEditListener(this);
        addComponent(content);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        super.enter(viewChangeEvent);
        grid.setData((List<Parametro>) nomencladorRepository.findAll());
    }

    @Override
    public String getViewName() {
        return VIEW_NAME;
    }

    @Override
    public void click(ClickableRenderer.RendererClickEvent event) {
        long id = (long) event.getItemId();
        ParametrosFormWindows win = (ParametrosFormWindows) window.show(id);
        UI.getCurrent().addWindow(win);
    }


}

