package com.earandap.vehiculos.ui.components;

import com.vaadin.server.Resource;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;

/**
 * @Autor Eduardo Aranda
 * @Version 0.1
 */
public class PortletView extends CssLayout {

    private PorletTitle porletTitle;

    private MenuBar menuBar;

    public PortletView(String title, Resource resource, MenuBar bar) {
        this.menuBar = bar;
        this.menuBar.addStyleName("actions");
        this.porletTitle = new PorletTitle(title, menuBar, resource);
        addComponent(porletTitle);
    }

    class PorletTitle extends CssLayout {

        private String title;

        private Resource icon;

        private MenuBar menuBar;

        public PorletTitle(String title, MenuBar bar) {
            this(title, bar, null);
        }

        public PorletTitle(String title, MenuBar bar, Resource icon) {

            this.title = title;
            this.icon = icon;
            this.menuBar = bar;

            this.setSizeFull();
            this.setPrimaryStyleName("portlet-title");
            this.setIcon(icon);
            Label caption = new Label(title);
            caption.setPrimaryStyleName("caption");
            caption.setSizeUndefined();
            this.addComponent(caption);
            this.addComponent(bar);
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public Resource getIcon() {
            return icon;
        }

        @Override
        public void setIcon(Resource icon) {
            this.icon = icon;
        }
    }
}
