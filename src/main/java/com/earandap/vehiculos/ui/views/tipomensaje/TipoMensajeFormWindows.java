package com.earandap.vehiculos.ui.views.tipomensaje;

import com.earandap.vehiculos.domain.PerfilRecurso;
import com.earandap.vehiculos.domain.TipoMensaje;
import com.earandap.vehiculos.domain.Usuario;
import com.earandap.vehiculos.repository.TipoMensajeRepository;
import com.earandap.vehiculos.ui.MainUI;
import com.earandap.vehiculos.ui.views.contrato.ContratoView;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.vaadin.spring.events.EventBus;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by Angel Luis on 12/26/2015.
 */
@SpringComponent
@UIScope
public class TipoMensajeFormWindows extends Window {

    private BeanFieldGroup<TipoMensaje> tipoMensajeBeanFieldGroup;

    @Autowired
    private EventBus.ApplicationEventBus applicationEventBus;

    //tab1
    private VerticalLayout content;

    @PropertyId("codigo")
    private TextField codigoField;

    @PropertyId("descripcion")
    private TextField descripcionField;


    @Autowired
    private TipoMensajeRepository tipoMensajeRepository;

    private boolean modificar;

    @PostConstruct
    public void init() {
        setWidth(250, Unit.PIXELS);
        setHeight(300, Unit.PIXELS);
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

        tabs.addTab(buildTipoMensaje(), "Tipo de Mensaje");

        Component footer = buildFooter();
        content.addComponent(footer);
        content.setComponentAlignment(footer, Alignment.BOTTOM_CENTER);

        tipoMensajeBeanFieldGroup = new BeanFieldGroup<>(TipoMensaje.class);
        tipoMensajeBeanFieldGroup.bindMemberFields(this);

        this.setContent(content);
    }

    private Component buildTipoMensaje() {
        FormLayout contratoForm = new FormLayout();
        contratoForm.setSpacing(true);
        contratoForm.setResponsive(true);

        //line 1
        HorizontalLayout l1 = new HorizontalLayout();
        l1.setSpacing(true);
        contratoForm.addComponent(l1);

        codigoField = new TextField("C\u00F3digo");
        codigoField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        codigoField.setWidth(12, Unit.EM);
        codigoField.setNullRepresentation("");
        l1.addComponent(codigoField);

        //line 2
        HorizontalLayout l2 = new HorizontalLayout();
        l2.setSpacing(true);
        contratoForm.addComponent(l2);

        descripcionField = new TextField("Descripci\u00F3n");
        descripcionField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        descripcionField.setWidth(12, Unit.EM);
        descripcionField.setNullRepresentation("");
        l2.addComponent(descripcionField);

        return contratoForm;
    }


    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);
        footer.setSpacing(true);

        Button guardar = new Button("Aceptar");
        guardar.addStyleName(ValoTheme.BUTTON_PRIMARY);
        footer.addComponent(guardar);
        footer.setComponentAlignment(guardar, Alignment.BOTTOM_RIGHT);
        guardar.addClickListener(event -> {
            MainUI ui = (MainUI) UI.getCurrent();
            Authentication auth = ui.getSecurity().getAuthentication();
            Set<PerfilRecurso> permisos = ((Usuario)auth.getPrincipal()).getPerfil().getPerfilRecursos();
            boolean tienePermiso = false;
            TipoMensaje tipoMensajeAux = tipoMensajeBeanFieldGroup.getItemDataSource().getBean();
            for (PerfilRecurso perfilRecurso: permisos){
                if (perfilRecurso.getId().getRecurso().getCodigo().equals(ContratoView.VIEW_NAME)){
                    if ((perfilRecurso.isCrear() && tipoMensajeAux.getId() == 0)||(perfilRecurso.isModificar() && tipoMensajeAux.getId() != 0) )
                        tienePermiso = true;
                }
            }
            if (!tienePermiso) {
                ui.getNavigator().navigateTo("accessDeniedView");
                close();
                return;
            }else {
                try {
                    tipoMensajeBeanFieldGroup.commit();
                    TipoMensaje tipoMensaje = tipoMensajeBeanFieldGroup.getItemDataSource().getBean();

                    if (!modificar) {
                        if (tipoMensajeRepository.findOneByCodigo(tipoMensaje.getCodigo()).isPresent() || ( tipoMensajeRepository.findOneByDescripcion(tipoMensaje.getDescripcion()).isPresent())) {
                            throw new FieldGroup.CommitException(new Validator.InvalidValueException("Ya existe tipo de mensaje con ese c\u00F3digo o descripci\u00F3n"));
                        }
                    }else {
                        List<TipoMensaje> tipoMensajes = tipoMensajeRepository.findAllByIdNot(tipoMensaje.getId());
                        if (this.contieneTipoMensaje(tipoMensajes, tipoMensaje.getCodigo(), tipoMensaje.getDescripcion()))
                            throw new FieldGroup.CommitException(new Validator.InvalidValueException("Ya existe tipo de mensaje con ese código o descripción"));
                    }
                    tipoMensajeRepository.save(tipoMensaje);
                    applicationEventBus.publish(TipoMensajeGrid.NUEVO_TIPOMENSAJE, this, tipoMensaje);
                    Notification notification = new Notification("Notificaci\u00F3n", "Tipo de Mensaje guardado con \u00E9xito", Notification.Type.TRAY_NOTIFICATION);
                    notification.setDelayMsec(3000);
                    notification.setIcon(FontAwesome.INFO_CIRCLE);
                    notification.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
                    notification.show(Page.getCurrent());
                    close();
                } catch (FieldGroup.CommitException e) {
                    if ((e instanceof FieldGroup.CommitException && e.getCause() instanceof Validator
                            .InvalidValueException || e.getCause() instanceof Validator
                            .EmptyValueException)) {
                        Collection<Validator.InvalidValueException> values = e.getInvalidFields().values();
                        if (!values.isEmpty()) {
                            Notification.show(values.iterator().next().getMessage(), e.getCause().getMessage(), Notification.Type.TRAY_NOTIFICATION);
                        } else if (e.getCause() instanceof Validator.InvalidValueException) {
                            Notification.show(e.getCause().getMessage(), Notification.Type.TRAY_NOTIFICATION);
                        }

                    } else {
                        Notification notification = new Notification("Ha ocurrido un error interno del sistema", e.getCause().getMessage(), Notification.Type.ERROR_MESSAGE);
                        notification.setPosition(Position.BOTTOM_RIGHT);
                        notification.setDelayMsec(3000);
                        notification.setIcon(FontAwesome.EXCLAMATION_CIRCLE);
                        notification.show(Page.getCurrent());
                    }
                }
            }
        });

        footer.setExpandRatio(guardar, 1);
        Button cancelar = new Button("Cancelar");
        cancelar.setStyleName(ValoTheme.BUTTON_PRIMARY);
        footer.addComponent(cancelar);
        footer.setComponentAlignment(cancelar, Alignment.BOTTOM_RIGHT);
        cancelar.addClickListener(event -> close());

        Responsive.makeResponsive(footer);
        return footer;
    }

    private boolean contieneTipoMensaje(List<TipoMensaje> tipoMensajes, String codigo, String descripcion) {
        for (TipoMensaje tipoMensaje: tipoMensajes)
            if (tipoMensaje.getCodigo().equals(codigo) || tipoMensaje.getDescripcion().equals(descripcion))
                return true;
        return false;
    }

    public Window show(long ID) {

        TipoMensaje tipoMensaje = tipoMensajeRepository.findOne(ID);
        if (tipoMensaje == null) {
            tipoMensaje = new TipoMensaje();
            modificar = false;
        }
        else{
            modificar = true;
        }
        tipoMensajeBeanFieldGroup.setItemDataSource(tipoMensaje);

        return this;
    }

}
