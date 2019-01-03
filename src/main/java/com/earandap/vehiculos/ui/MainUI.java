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
package com.earandap.vehiculos.ui;

import com.earandap.vehiculos.repository.ParametrosRepository;
import com.earandap.vehiculos.repository.TerceroNotificacionRepository;
import com.earandap.vehiculos.ui.components.CustomLayout;
import com.earandap.vehiculos.ui.components.SpringSystemMessagesProvider;
import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.security.VaadinSecurity;

@SpringUI
@Theme("neodent")
@Push(transport = Transport.WEBSOCKET)
@Widgetset("com.earandap.vehiculos.ui.widgetset.WidgetSet")

@StyleSheet({
    "vaadin://resources/css/font-awesome.min.css",
    "vaadin://resources/css/styles.css",})

@JavaScript({
    "vaadin://resources/js/jquery-2.1.4.min.js",
    "vaadin://resources/js/jquery-media.js",
    "vaadin://resources/js/init.js",})

public class MainUI extends UI {

    @Autowired
    VaadinSecurity security;

    @Autowired
    SpringViewProvider ViewProvider;

    private CustomLayout root;

    private ComponentContainer viewDisplay;

    @Autowired
    private EventBus.ApplicationEventBus eventBus;

    @Autowired
    private TerceroNotificacionRepository terceroNotificacionRepository;
    
    @Autowired
    private ParametrosRepository parametrosRepository;

    @Override
    protected void init(VaadinRequest request) {
        
        getPage().setTitle("Seguridad Vial");
        
        Locale myLocale = Locale.forLanguageTag("es-ES");
        this.setLocale(myLocale);
        VaadinSession.getCurrent().setLocale(myLocale);
        VaadinService.getCurrent().setSystemMessagesProvider(new SpringSystemMessagesProvider());

        root = new CustomLayout();
        root.setEventBus(eventBus);
        root.fileDir = parametrosRepository.findOne(Long.valueOf("2")).getValorParametro();
        root.setTerceroNotificacionRepository(terceroNotificacionRepository);

        setContent(root);
        viewDisplay = root.getContentContainer();

        Responsive.makeResponsive(this);

        Navigator navigator = new Navigator(this, viewDisplay);
        navigator.addProvider(ViewProvider);
        setNavigator(navigator);

        /*UI.getCurrent().access(new Runnable() {
            @Override
            public void run() {
                StringBuilder script = new StringBuilder();
                script
                        .append("var head = document.getElementsByTagName('head')[0];")
                        .append("var x = document.createElement(\"script\");")
                        .append("var t = document.createTextNode(\"$(document).ready(function() {$('.media').media();});\");")
                        //...do other stuff to set up the "script" tag (add content, source, whatever)
                        .append("x.appendChild(t);")
                        //.append("head.appendChild(script);");
                        .append("document.body.appendChild(x);");
                Page.getCurrent().getJavaScript().execute(script.toString());
                UI.getCurrent().push();
            }
        });*/
    }

    public VaadinSecurity getSecurity() {
        return security;
    }

    public void setSecurity(VaadinSecurity security) {
        this.security = security;
    }

    public CustomLayout getRoot() {
        return root;
    }

}
