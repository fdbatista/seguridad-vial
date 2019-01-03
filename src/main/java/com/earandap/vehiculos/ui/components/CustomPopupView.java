package com.earandap.vehiculos.ui.components;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.*;

import java.util.LinkedHashSet;

/**
 * @Autor Eduardo Aranda
 * @Version 0.1
 */
public class CustomPopupView extends PopupView {

    private LinkedHashSet<ClickMenuListener> listenerList = null;

    public CustomPopupView() {

        super(new PopupView.Content() {
            @Override
            public String getMinimizedValueAsHTML() {
                return "<span class=\"v-icon FontAwesome\">\uF0CA</span>";
            }

            @Override
            public Component getPopupComponent() {
                return new HorizontalLayout() {
                    {
                        setPrimaryStyleName("float-menu");
                    }
                };
            }
        });
        this.setHideOnMouseOut(false);

        HorizontalLayout layout = (HorizontalLayout) getContent().getPopupComponent();
        layout.addComponent(new VerticalLayout() {
            {
                setMargin(true);
                setPrimaryStyleName("float-menu-column");
                PopupMenuItem configMenuItem = new PopupMenuItem("Configuraci\u00F3n", FontAwesome.COGS);
                configMenuItem.addClickListener(event -> fireClick(configMenuItem));
                addComponent(configMenuItem);

                PopupMenuItem seguridadVialItem = new PopupMenuItem("Seguridad Vial", FontAwesome.APPLE);
                seguridadVialItem.addClickListener(event -> fireClick(seguridadVialItem));
                addComponent(seguridadVialItem);
            }
        });
        layout.addComponent(new VerticalLayout() {
            {
                setMargin(true);
                setPrimaryStyleName("float-menu-column");
                addComponent(new PopupMenuItem("Transporte", FontAwesome.CALENDAR));
                addComponent(new PopupMenuItem("Seguridad Vial", FontAwesome.APPLE));
            }
        });

        layout.addComponent(new VerticalLayout() {
            {
                setMargin(true);
                setPrimaryStyleName("float-menu-column");
                addComponent(new PopupMenuItem("Seguridad Vial", FontAwesome.APPLE));

            }
        });

    }

    public void addSaveListener(ClickMenuListener listener) {
        if (listenerList == null) {
            listenerList = new LinkedHashSet<>();
        }
        listenerList.add(listener);
    }

    public void fireClick(PopupMenuItem item) {
        if (listenerList != null) {
            final Object[] listeners = listenerList.toArray();
            for (int i = 0; i < listeners.length; i++) {
                ClickMenuListener listener = (ClickMenuListener) listeners[i];
                listener.clickMenu(item);
            }
        }
    }

    public static class PopupMenuItem extends Button {
        public PopupMenuItem(String name, Resource icon, ClickListener action) {
            this(name, icon);
            addClickListener(action);

        }

        public PopupMenuItem(String name, Resource icon) {
            super(name, icon);
            setPrimaryStyleName("popup-menu-item");

        }

    }

    public interface ClickMenuListener {
        void clickMenu(PopupMenuItem item);
    }
}
