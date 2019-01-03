package com.earandap.vehiculos.ui.views.indicadores;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.nativesql.IndicadorNative;
import com.earandap.vehiculos.ui.components.PortletView;
import com.earandap.vehiculos.ui.views.SecureView;
import com.vaadin.data.Property;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ClickableRenderer;
import com.vaadin.ui.themes.ValoTheme;
import java.util.Date;
import java.util.List;
import org.vaadin.gridutil.renderer.EditButtonValueRenderer;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

@SpringComponent
@UIScope
@SpringView(name = CausasView.VIEW_NAME)
public class CausasView extends SecureView implements EditButtonValueRenderer.RendererClickListener {

    public static final String VIEW_NAME = "causas-inmediatas-basicas";

    private PortletView content;
    private MenuBar bar;
    private DateField fechaInicialField, fechaFinalField;
    private Button btnConsultar, btnRestablecer;
    private IndicadorGrid indicadorGridAccidInvestSoc, indicadorGridAccidCausas;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.driver-class-name}")
    private String dbDriver;

    @PostConstruct
    public void init() {
        bar = new MenuBar();
        content = new PortletView("Causas Inmediatas y B\u00e1sicas", FontAwesome.BOOK, bar);
        content.setPrimaryStyleName("portlet-view");
        content.setSizeFull();
        inicializarGrids();

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setSizeFull();

        //line 1
        HorizontalLayout l1 = new HorizontalLayout();
        l1.setSpacing(true);
        layout.addComponent(l1);

        fechaInicialField = new DateField("Fecha Inicial");
        fechaInicialField.addStyleName(ValoTheme.DATEFIELD_TINY);
        fechaInicialField.setRequired(true);
        fechaInicialField.setRequiredError("Este dato es obligatorio");
        fechaInicialField.setWidth(12, Unit.EM);
        fechaInicialField.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                fechaFinalField.focus();
            }
        });
        l1.addComponent(fechaInicialField);

        fechaFinalField = new DateField("Fecha Final");
        fechaFinalField.addStyleName(ValoTheme.DATEFIELD_TINY);
        fechaFinalField.setRequired(true);
        fechaFinalField.setRequired(true);
        fechaFinalField.setRequiredError("Este dato es obligatorio");
        fechaFinalField.setWidth(12, Unit.EM);
        fechaFinalField.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                btnConsultar.focus();
            }
        });
        l1.addComponent(fechaFinalField);

        btnConsultar = new Button("Consultar");
        btnConsultar.addStyleName(ValoTheme.BUTTON_PRIMARY + " " + ValoTheme.BUTTON_TINY);
        btnConsultar.addStyleName("align-bottom-row");
        btnConsultar.setIcon(FontAwesome.DATABASE);
        btnConsultar.setDisableOnClick(true);
        btnConsultar.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        btnConsultar.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Date fechaInicial = fechaInicialField.getValue(), fechaFinal = fechaFinalField.getValue();
                if (fechaInicial == null) {
                    StaticMembers.showNotificationError("Error", "La fecha inicial es obligatoria");
                    fechaInicialField.focus();
                } else if (fechaFinal == null) {
                    StaticMembers.showNotificationError("Error", "La fecha final es obligatoria");
                    fechaFinalField.focus();
                } else if (fechaInicial.after(fechaFinal)) {
                    StaticMembers.showNotificationError("Error", "La fecha final debe ser mayor que la inicial");
                    fechaFinalField.focus();
                } else {
                    actualizarGrid();
                }
                btnConsultar.setEnabled(true);
            }
        });
        l1.addComponent(btnConsultar);

        btnRestablecer = new Button("Restablecer");
        btnRestablecer.addStyleName(ValoTheme.BUTTON_TINY);
        btnRestablecer.addStyleName("align-bottom-row");
        btnRestablecer.setIcon(FontAwesome.UNDO);
        btnRestablecer.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                indicadorGridAccidInvestSoc.actualizarColumnas(new String[]{});
                indicadorGridAccidCausas.actualizarColumnas(new String[]{});
                fechaInicialField.clear();
                fechaFinalField.clear();
                fechaInicialField.focus();
            }
        });
        l1.addComponent(btnRestablecer);

        //line 2
        HorizontalLayout l2 = new HorizontalLayout();
        l2.setSpacing(true);
        l2.setSizeFull();
        layout.addComponent(l2);
        
        Label lblAccidInvestSoc = new Label("Accidentes Investigados / Lecciones Divulgadas");
        l2.addComponent(lblAccidInvestSoc);

        //line 3
        HorizontalLayout l3 = new HorizontalLayout();
        l3.setSpacing(true);
        l3.setSizeFull();
        layout.addComponent(l3);

        l3.addComponent(indicadorGridAccidInvestSoc);
        
        //line 4
        
        HorizontalLayout l4 = new HorizontalLayout();
        l4.setSpacing(true);
        l4.setSizeFull();
        layout.addComponent(l4);
        
        Label lblAccidCausas = new Label("Accidentes por Causas");
        l4.addComponent(lblAccidCausas);

        //line 5
        
        HorizontalLayout l5 = new HorizontalLayout();
        l5.setSpacing(true);
        l5.setSizeFull();
        layout.addComponent(l5);

        l5.addComponent(indicadorGridAccidCausas);

        content.addComponent(layout);
        addComponent(content);
    }

    private void inicializarGrids() {
        indicadorGridAccidInvestSoc = new IndicadorGrid();
        indicadorGridAccidInvestSoc.init();
        indicadorGridAccidInvestSoc.setHeight("100px");
        indicadorGridAccidInvestSoc.addStyleName("data-grid");
        indicadorGridAccidCausas = new IndicadorGrid();
        indicadorGridAccidCausas.init();
        indicadorGridAccidCausas.addStyleName("data-grid");
    }

    private void actualizarGrid() {
        try {
            IndicadorNative obj = new IndicadorNative(dbUrl, dbUsername, dbPassword, dbDriver);

            List<Object[]> elemsInvestSoc = obj.consultarAccidentesInvestSoc(fechaInicialField.getValue(), fechaFinalField.getValue()),
                    elemsAccidCausas = obj.consultarAccidentesPorCausas(fechaInicialField.getValue(), fechaFinalField.getValue());
            //
            indicadorGridAccidCausas.actualizarContenido(new String[]{"Accidentes por Causa", "Total", "Porciento"}, elemsAccidCausas);
            indicadorGridAccidInvestSoc.actualizarContenido(new String[]{"Accidentes totales", "Investigados (%)", "Con lesiones", "Socializados (%)"}, elemsInvestSoc);
        } catch (Exception e) {
            StaticMembers.showNotificationError("Error", e.getMessage());
        } finally {
            btnConsultar.setEnabled(true);
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        super.enter(viewChangeEvent);
    }

    @Override
    public String getViewName() {
        return VIEW_NAME;
    }

    @Override
    public void click(ClickableRenderer.RendererClickEvent event) {

    }
}
