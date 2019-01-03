package com.earandap.vehiculos.ui.views.sucursales;

import com.earandap.vehiculos.domain.Empresa;
import com.earandap.vehiculos.domain.PerfilRecurso;
import com.earandap.vehiculos.domain.Sucursal;
import com.earandap.vehiculos.domain.Usuario;
import com.earandap.vehiculos.repository.EmpresaRepository;
import com.earandap.vehiculos.repository.SucursalRepository;
import com.earandap.vehiculos.ui.MainUI;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.vaadin.spring.events.EventBus;
import org.vaadin.suggestfield.SuggestField;
import org.vaadin.suggestfield.client.SuggestFieldSuggestion;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by Angel Luis on 11/16/2015.
 */
@SpringComponent
@UIScope
public class SucursalFormWindows extends Window{

    private BeanFieldGroup<Sucursal> sucursalBeanFieldGroup;

    @Autowired
    private EventBus.ApplicationEventBus applicationEventBus;

    //tab1
    private VerticalLayout content;

    //private Button buscarTerceroButton;

    @PropertyId("nombre")
    private TextField nombreField;

    @PropertyId("empresa")
    private SuggestField empresaAfiliadaField;

    @PropertyId("inactivo")
    private CheckBox inactivoField;

    @Autowired
    private SucursalRepository sucursalRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    private boolean modificar = false;

    public boolean isModificar() {
        return modificar;
    }

    public void setModificar(boolean modificar) {
        this.modificar = modificar;
    }

    @PostConstruct
    public void init() {
        setWidth(470, Unit.PIXELS);
        setHeight(350, Unit.PIXELS);
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

        tabs.addTab(buildInfoSucursal(), "Informaci\u00F3n Sucursal");

        com.vaadin.ui.Component footer = buildFooter();
        content.addComponent(footer);
        content.setComponentAlignment(footer, Alignment.BOTTOM_CENTER);

        sucursalBeanFieldGroup = new BeanFieldGroup<>(Sucursal.class);
        sucursalBeanFieldGroup.bindMemberFields(this);

        this.setContent(content);
    }


    private com.vaadin.ui.Component buildInfoSucursal() {
        FormLayout infosucursalForm = new FormLayout();
        infosucursalForm.setSpacing(true);
        infosucursalForm.setResponsive(true);

        HorizontalLayout l1 = new HorizontalLayout();
        l1.setSpacing(true);
        infosucursalForm.addComponent(l1);

        empresaAfiliadaField = new SuggestField();
        empresaAfiliadaField.setWidth(30, Unit.EM);
        empresaAfiliadaField.setCaption("Empresa");
        empresaAfiliadaField.setNewItemsAllowed(false);
        empresaAfiliadaField.setSuggestionHandler(s -> {
            List<Object> result = new ArrayList<>();
            List<Empresa> empresas = empresaRepository.search(s);
            for (Empresa empresa : empresas) {
                result.add(empresa);
            }
            if (result.size() == 0)
                Notification.show("No se encontraron empresas para el criterio introducido", Notification.Type.TRAY_NOTIFICATION);

            return result;
        });
        empresaAfiliadaField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        empresaAfiliadaField.setImmediate(true);
        empresaAfiliadaField.setTokenMode(false);
        empresaAfiliadaField.setSuggestionConverter(new EmpresaSuggestionConverter());
        empresaAfiliadaField.setPopupWidth(400);
        l1.addComponent(empresaAfiliadaField);

        HorizontalLayout l2 = new HorizontalLayout();
        l2.setSpacing(true);
        infosucursalForm.addComponent(l2);

        nombreField = new TextField("Nombre Sucursal");
        nombreField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        nombreField.setWidth(30, Sizeable.Unit.EM);
        nombreField.setNullRepresentation("");
        nombreField.setRequired(true);
        nombreField.setRequiredError("El nombre de la sucursal es requerido");
        nombreField.setMaxLength(50);
        l2.addComponent(nombreField);

        HorizontalLayout l3 = new HorizontalLayout();
        l3.setSpacing(true);
        infosucursalForm.addComponent(l3);

        inactivoField = new CheckBox("Inactivo");
        inactivoField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        inactivoField.setWidth(9, Sizeable.Unit.EM);
        l3.addComponent(inactivoField);

        return infosucursalForm;
    }

    private void limpiarFieldGroup() {
        sucursalBeanFieldGroup.setItemDataSource(new Sucursal());
    }

    private com.vaadin.ui.Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);
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
            Sucursal sucursalAux = sucursalBeanFieldGroup.getItemDataSource().getBean();
            for (PerfilRecurso perfilRecurso: permisos){
                if (perfilRecurso.getId().getRecurso().getCodigo().equals(SucursalView.VIEW_NAME)){
                    if ((perfilRecurso.isCrear() && sucursalAux.getId() == 0)||(perfilRecurso.isModificar() && sucursalAux.getId() != 0) )
                        tienePermiso = true;
                }
            }
            if (!tienePermiso) {
                ui.getNavigator().navigateTo("accessDeniedView");
                close();
                return;
            }else {
                try {
                    sucursalBeanFieldGroup.commit();
                    Sucursal sucursal = sucursalBeanFieldGroup.getItemDataSource().getBean();
                    /*if (!modificar)
                        if (sucursalRepository.searchSucursal(sucursal.getDocumento()) == null) {
                            TipoDocumento tipoDocumento = new TipoDocumento();
                            tipoDocumento.setId(19);
                            sucursal.setTipoDocumento(tipoDocumento);
                        } else {
                            throw new FieldGroup.CommitException(new Validator.InvalidValueException("Ya existe la sucursal con ese SID"));
                        }*/
                    sucursalRepository.save(sucursal);
                    applicationEventBus.publish(SucursalGrid.NUEVA_SUCURSAL, this, sucursal);
                    Notification notification = new Notification("Notificaci\u00F3n", "Sucursal guardada con \u00E9xito", Notification.Type.TRAY_NOTIFICATION);
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
        cancelar.addClickListener(event -> {
            this.limpiarFieldGroup();
            this.close();
        });
        Responsive.makeResponsive(footer);
        return footer;
    }


    public Window show(long ID) {
        Sucursal sucursal = sucursalRepository.findOne(ID);
        if (sucursal == null) {
            sucursal = new Sucursal();
            this.setModificar(false);
            sucursalBeanFieldGroup.setItemDataSource(sucursal);
        } else {
            this.setModificar(true);
            sucursalBeanFieldGroup.setItemDataSource(sucursal);
        }
        return this;
    }

    public class EmpresaSuggestionConverter implements SuggestField.SuggestionConverter {

        @Override
        public SuggestFieldSuggestion toSuggestion(Object item) {
            if (item != null) {
                Empresa empresa = (Empresa) item;
                return new SuggestFieldSuggestion(String.valueOf(empresa.getId()), empresa.getRazonSocial(), empresa.getRazonSocial());
            }
            return null;
        }

        @Override
        public Object toItem(SuggestFieldSuggestion suggestion) {
            return empresaRepository.findOne(Long.valueOf(suggestion.getId()));
        }
    }
}
