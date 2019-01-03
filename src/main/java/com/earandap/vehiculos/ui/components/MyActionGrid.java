package com.earandap.vehiculos.ui.components;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.anna.gridactionrenderer.ActionGrid;
import org.vaadin.anna.gridactionrenderer.GridAction;
import org.vaadin.anna.gridactionrenderer.GridActionRenderer.GridActionClickEvent;
import org.vaadin.anna.gridactionrenderer.GridDownloadAction;

import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

public class MyActionGrid extends ActionGrid {

    public MyActionGrid() {
        super(createActions());

        addStyleName("demogrid");
        addColumn("name");
        setCaption("Action Grid");

        Column column = addColumn("actions");
        column.setRenderer(getGridActionRenderer());

        for (int i = 0; i < 25; ++i) {
            Item item = getContainerDataSource().addItem(i);
            item.getItemProperty("name").setValue("Item" + i);
            item.getItemProperty("actions").setValue(i % 3 == 0 ? "1,2" : "-1");
        }
    }

    private static List<GridAction> createActions() {
        List<GridAction> actions = new ArrayList<GridAction>();
        actions.add(new GridAction(FontAwesome.USER, "user"));
        actions.add(new GridAction(FontAwesome.GEAR, "settings"));
        actions.add(new GridDownloadAction(FontAwesome.TICKET, "ticket"));
        return actions;
    }

    @Override
    public void click(GridActionClickEvent event) {
        Item item = getContainerDataSource().getItem(event.getItemId());
        Notification.show(item.getItemProperty("name").getValue() + " - "
                + event.getAction().getDescription(), Type.ERROR_MESSAGE);
    }

}
