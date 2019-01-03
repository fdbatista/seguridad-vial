package com.earandap.vehiculos.ui.views.indicadores;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by felix.batista.
 */
public class IndicadorGrid extends Grid {

    //private HorizontalLayout buttonLayout;

    @PostConstruct
    public void init() {
        Responsive.makeResponsive(this);
        this.setSizeFull();
        this.setHeight("200px");
        this.setSelectionMode(Grid.SelectionMode.SINGLE);
        actualizarColumnas(new String[]{});
        //initExtraHeaderRow();
    }

    public void actualizarColumnas(String[] columnas) {
        this.removeAllColumns();
        this.getContainerDataSource().removeAllItems();
        
        IndexedContainer container = new IndexedContainer();
        for (String columna : columnas) {
            container.addContainerProperty(columna, String.class, "");
        }
        
        this.setContainerDataSource(container);
    }

    public void actualizarContenido(String[] columnas, List<Object[]> elems) {
        //JavaScript.getCurrent().execute("$('.data-grid').animate({opacity: 0.0}, 1000, function(){$('.data-grid').animate({opacity: 1.0}, 1000);});");
        //Page.getCurrent().getJavaScript().execute("$('.data-grid').animate({opacity: 0.0}, 500, function(){$('.data-grid').animate({opacity: 1.0}, 1000);});");
        this.setEnabled(true);

        int cantFilas = elems.size();
        
        if (cantFilas > 0) {

            if (columnas.length > 1) {
                this.actualizarColumnas(columnas);
                for (Object[] fila : elems) {
                    this.addRow(fila);
                }
            } else {
                this.actualizarColumnas(new String[]{"Resultados"});
                for (Object elem : elems) {
                    this.addRow(elem.toString());
                }
            }
        } else {
            this.actualizarColumnas(new String[]{"Resultados"});
            this.addRow("No se encontraron resultados.");
        }
        this.setEnabled(true);
    }

    public void initExtraHeaderRow() {

        /*Grid.HeaderRow firstHeaderRow = this.prependHeaderRow();
        firstHeaderRow.join("");
        buttonLayout = new HorizontalLayout();
        buttonLayout.setSizeFull();
        buttonLayout.setSpacing(true);
        buttonLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        firstHeaderRow.getCell("resultados").setComponent(buttonLayout);

        Button adicionar = new Button("", (Button.ClickListener) event -> {
            
        }
        );
        adicionar.setCaption("Descargar");
        adicionar.setIcon(FontAwesome.DOWNLOAD);
        adicionar.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        adicionar.addStyleName(ValoTheme.BUTTON_TINY);

        buttonLayout.addComponent(adicionar);*/
    }

}
