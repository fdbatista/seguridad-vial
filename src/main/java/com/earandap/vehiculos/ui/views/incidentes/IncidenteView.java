/*
 * Copyright 2015 The original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.earandap.vehiculos.ui.views.incidentes;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.Incidente;
import com.earandap.vehiculos.repository.IncidenteRepository;
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
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.gridutil.renderer.EditDeleteButtonValueRenderer;
import org.vaadin.spring.events.EventBus;

import javax.annotation.PostConstruct;

/**
 * View that is available for all users.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@SpringView(name = IncidenteView.VIEW_NAME)
public class IncidenteView extends SecureView implements EditDeleteButtonValueRenderer.EditDeleteButtonClickListener {

    public static final String VIEW_NAME = "incidentes";

    @Autowired
    private IncidenteFormWindow window;

    @Autowired
    private IncidenteRepository incidenteRepository;

    @Autowired
    private IncidenteGrid grid;

    @Autowired
    private EventBus.ApplicationEventBus applicationEventBus;

    private PortletView content;

    private MenuBar bar;

    @PostConstruct
    public void init() {
        bar = new MenuBar();
        bar.addItem("", FontAwesome.PLUS_SQUARE, menuItem -> {
            IncidenteFormWindow win = (IncidenteFormWindow) window.show(-1);
            UI.getCurrent().addWindow(win);
        });
        content = new PortletView("Listado de Incidentes", FontAwesome.ADN, bar);
        content.setPrimaryStyleName("portlet-view");
        content.setSizeFull();
        content.addComponent(grid);
        grid.setEditDeleteListener(this);
        addComponent(content);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        super.enter(viewChangeEvent);
        grid.setData(incidenteRepository.findAll());
    }

    @Override
    public void onEdit(ClickableRenderer.RendererClickEvent event) {
        UI.getCurrent().addWindow(window.show((Long) event.getItemId()));
    }

    @Override
    public void onDelete(ClickableRenderer.RendererClickEvent event) {
        MessageBox messageBox = MessageBox.showPlain(
                Icon.QUESTION,
                "Incidente",
                "Est\u00E1 seguro que quiere eliminar?",
                ButtonId.YES,
                ButtonId.NO);
        messageBox.getButton(ButtonId.YES).addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent e) {
                incidenteRepository.delete((Long) event.getItemId());
                applicationEventBus.publish(IncidenteGrid.ELIMINADO_INCIDENTE, this, event.getItemId());
                StaticMembers.showNotificationMessage("Notificaci\u00F3n", "Incidente eliminado con \u00E9xito");
            }
        });


    }
    @Override
    public String getViewName() {
        return VIEW_NAME;
    }
}
