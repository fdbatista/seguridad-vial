package com.earandap.vehiculos.ui.views.terceronotificacion;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.TerceroNotificacion;
import com.earandap.vehiculos.domain.TipoNotificacion;
import com.earandap.vehiculos.repository.NotificacionRepository;
import com.earandap.vehiculos.repository.TerceroNotificacionRepository;
import com.earandap.vehiculos.repository.TerceroRepository;
import com.earandap.vehiculos.repository.TipoNotificacionRepository;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * Created by angel on 27/04/16.
 */
@SpringComponent
@UIScope
public class TerceroNotificacionFormWindows extends Window {

    private BeanFieldGroup<TerceroNotificacion> terceroNotificacionBeanFieldGroup;

    @Autowired
    private EventBus.ApplicationEventBus applicationEventBus;

    //tab1
    private VerticalLayout content;

    @PropertyId("id.notificacion.tipoNotificacion")
    private ComboBox tipoNotificacionField;
    private BeanItemContainer<TipoNotificacion> tipoNotificacionBeanItemContainer;

    @PropertyId("id.notificacion.fechaNotificacion")
    private DateField fechaNotificacionField;

    @PropertyId("id.notificacion.encabezado")
    private TextField encabezadoField;

    @PropertyId("id.notificacion.detalle")
    private TextArea detalleField;

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private TerceroNotificacionRepository terceroNotificacionRepository;

    @Autowired
    private TipoNotificacionRepository tipoNotificacionRepository;

    @Autowired
    private TerceroRepository terceroRepository;


    @PostConstruct
    public void init() {
        setWidth(700, Unit.PIXELS);
        setHeight(450, Unit.PIXELS);
        setModal(true);
        setCloseShortcut(ShortcutAction.KeyCode.ESCAPE, null);
        setResizable(false);
        setClosable(false);

        Responsive.makeResponsive(this);

        content = new VerticalLayout();
        content.setSizeFull();
        content.setMargin(true);

        TabSheet tabs = new TabSheet();
        content.addComponent(tabs);
        content.setExpandRatio(tabs, 1);

        tabs.addTab(buildTerceroNotificacion(), "Detalles de Notificaci\u00F3n");

        Component footer = buildFooter();
        content.addComponent(footer);
        content.setComponentAlignment(footer, Alignment.BOTTOM_CENTER);

        terceroNotificacionBeanFieldGroup = new BeanFieldGroup<>(TerceroNotificacion.class);
        terceroNotificacionBeanFieldGroup.bindMemberFields(this);

        this.setContent(content);
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);
        footer.setSpacing(true);

        Button cerrar = new Button("Cerrar");
        cerrar.setStyleName(ValoTheme.BUTTON_PRIMARY);
        footer.addComponent(cerrar);
        footer.setComponentAlignment(cerrar, Alignment.BOTTOM_RIGHT);
        cerrar.addClickListener(event -> {
            TerceroNotificacion terceroNotificacion = terceroNotificacionBeanFieldGroup.getItemDataSource().getBean();
            applicationEventBus.publish(TerceroNotificacionGrid.NUEVO_TERCERONOTIFICACION, this, terceroNotificacion);
            applicationEventBus.publish("NUEVA-NOTIFICACION",this,terceroNotificacion);
            close();
        });

        Responsive.makeResponsive(footer);
        return footer;
    }

    private Component buildTerceroNotificacion() {
        FormLayout contratoForm = new FormLayout();
        contratoForm.setSpacing(true);
        contratoForm.setResponsive(true);
        contratoForm.setEnabled(false);

        //line 1
        HorizontalLayout l1 = new HorizontalLayout();
        l1.setSpacing(true);
        contratoForm.addComponent(l1);


        tipoNotificacionBeanItemContainer = new BeanItemContainer<>(TipoNotificacion.class);
        tipoNotificacionField = new ComboBox("Tipo Notificaci\u00F3n");
        tipoNotificacionField.addStyleName(ValoTheme.COMBOBOX_TINY);
        tipoNotificacionField.setWidth(15, Unit.EM);
        tipoNotificacionField.setContainerDataSource(tipoNotificacionBeanItemContainer);
        tipoNotificacionField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        tipoNotificacionField.setItemCaptionPropertyId("descripcion");
        tipoNotificacionField.setNullSelectionAllowed(false);
        tipoNotificacionField.setScrollToSelectedItem(true);
        l1.addComponent(tipoNotificacionField);

        fechaNotificacionField = new DateField("Fecha Notificaci\u00F3n");
        fechaNotificacionField.setStyleName(ValoTheme.DATEFIELD_TINY);
        fechaNotificacionField.setRangeStart(new Date());
        StaticMembers.setErrorRangoEspanhol(fechaNotificacionField);
        fechaNotificacionField.setWidth(12, Unit.EM);
        l1.addComponent(fechaNotificacionField);

        //line 2
        HorizontalLayout l2 = new HorizontalLayout();
        l2.setSpacing(true);
        contratoForm.addComponent(l2);

        encabezadoField = new TextField("Encabezado");
        encabezadoField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        encabezadoField.setWidth(12, Unit.EM);
        encabezadoField.setNullRepresentation("");
        l2.addComponent(encabezadoField);


        //line 3
        HorizontalLayout l3 = new HorizontalLayout();
        l3.setSpacing(true);
        contratoForm.addComponent(l3);

        detalleField = new TextArea("Detalles");
        detalleField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        detalleField.setNullRepresentation("");
        detalleField.setColumns(45);
        detalleField.setRows(8);
        detalleField.setMaxLength(600);
        l3.addComponent(detalleField);

        return contratoForm;
    }


    public Window show(TerceroNotificacion.Id ID) {

        tipoNotificacionBeanItemContainer.removeAllItems();
        tipoNotificacionBeanItemContainer.addAll(tipoNotificacionRepository.findAll());

        TerceroNotificacion terceroNotificacion = terceroNotificacionRepository.findOne(ID);
        if (terceroNotificacion == null) {
            terceroNotificacion = new TerceroNotificacion();
        }else{
            terceroNotificacion.setLeido(true);
            terceroNotificacionRepository.save(terceroNotificacion);
        }
        terceroNotificacionBeanFieldGroup.setItemDataSource(terceroNotificacion);

        return this;
    }



}
