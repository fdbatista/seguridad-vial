package com.earandap.vehiculos.ui.components;

import com.vaadin.server.ExternalResource;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.FormLayout;

/**
 * Created by felix.batista.
 */
public class FileViewerForm extends FormLayout {

    public FileViewerForm(String url) {

        /*this.setStyleName("mantenimientodetalle-form");
        Resource res = new ThemeResource(resPath);
        Embedded object = new Embedded("My Object", res);
        this.addComponent(object);*/
        BrowserFrame browser = new BrowserFrame("", new ExternalResource(url));
        browser.setWidth("735px");
        browser.setHeight("450px");
        this.addComponent(browser);

    }

}
