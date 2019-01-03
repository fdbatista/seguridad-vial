package com.earandap.vehiculos.ui.views.contrato;

import com.earandap.vehiculos.domain.Contrato;
import com.earandap.vehiculos.domain.PerfilRecurso;
import com.earandap.vehiculos.domain.Tercero;
import com.earandap.vehiculos.domain.Usuario;
import com.earandap.vehiculos.domain.nomenclador.TipoContrato;
import com.earandap.vehiculos.repository.ContratoRepository;
import com.earandap.vehiculos.repository.PersonaRepository;
import com.earandap.vehiculos.repository.TerceroRepository;
import com.earandap.vehiculos.repository.TipoContratoRepository;
import com.earandap.vehiculos.ui.MainUI;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItemContainer;
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
import org.vaadin.suggestfield.SuggestField;
import org.vaadin.suggestfield.client.SuggestFieldSuggestion;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by Angel Luis on 11/29/2015.
 */
@SpringComponent
@UIScope
public class ContratoFormWindows extends Window {

    private BeanFieldGroup<Contrato> contratoBeanFieldGroup;

    @Autowired
    private EventBus.ApplicationEventBus applicationEventBus;


    //tab1
    private VerticalLayout content;

    @PropertyId("tipoContrato")
    private ComboBox tipoContratoField;
    private BeanItemContainer<TipoContrato> tipoContratoBeanItemContainer;

    @PropertyId("tercero")
    private SuggestField terceroField;

    @PropertyId("numeroContrato")
    private TextField numeroContratoField;

    @PropertyId("duracionContrato")
    private TextField duracionContratoField;

    @PropertyId("fechaInicio")
    private DateField fechaInicioField;

    @PropertyId("inactivo")
    private CheckBox inactivoField;




    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private TipoContratoRepository tipoContratoRepository;

    @Autowired
    private TerceroRepository terceroRepository;

    private boolean modificar;


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

        tabs.addTab(buildContratos(), "Contratos");

        Component footer = buildFooter();
        content.addComponent(footer);
        content.setComponentAlignment(footer, Alignment.BOTTOM_CENTER);

        contratoBeanFieldGroup = new BeanFieldGroup<>(Contrato.class);
        contratoBeanFieldGroup.bindMemberFields(this);

        this.setContent(content);
    }

    private Component buildContratos() {
        FormLayout contratoForm = new FormLayout();
        contratoForm.setSpacing(true);
        contratoForm.setResponsive(true);

        //line 1
        HorizontalLayout l1 = new HorizontalLayout();
        l1.setSpacing(true);
        contratoForm.addComponent(l1);

        terceroField = new SuggestField();
        terceroField.setWidth(28, Unit.EM);
        terceroField.setCaption("Tercero");
        terceroField.setNewItemsAllowed(false);
        terceroField.setSuggestionHandler(s -> {
            List<Object> result = new ArrayList<>();
            List<Tercero> terceros = terceroRepository.search(s);
            for (Tercero tercero : terceros) {
                result.add(tercero);
            }
            if (result.size() == 0)
                Notification.show("No se encontraron terceros para el criterio introducido", Notification.Type.TRAY_NOTIFICATION);

            return result;
        });
        terceroField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        terceroField.setImmediate(true);
        terceroField.setTokenMode(false);
        terceroField.setSuggestionConverter(new TerceroSuggestionConverter());
        terceroField.setPopupWidth(400);
        terceroField.setRequired(true);
        terceroField.setRequiredError("El tercero es requerido");
        l1.addComponent(terceroField);


        tipoContratoBeanItemContainer = new BeanItemContainer<>(TipoContrato.class);
        tipoContratoField = new ComboBox("Tipo Contrato");
        tipoContratoField.addStyleName(ValoTheme.COMBOBOX_TINY);
        tipoContratoField.setWidth(15, Unit.EM);
        tipoContratoField.setContainerDataSource(tipoContratoBeanItemContainer);
        tipoContratoField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        tipoContratoField.setItemCaptionPropertyId("descripcion");
        tipoContratoField.setNullSelectionAllowed(false);
        tipoContratoField.setScrollToSelectedItem(true);
        tipoContratoField.setRequired(true);
        tipoContratoField.setRequiredError("El tipo de contrato es requerido");
        l1.addComponent(tipoContratoField);


        //line 2
        HorizontalLayout l2 = new HorizontalLayout();
        l2.setSpacing(true);
        contratoForm.addComponent(l2);

        numeroContratoField = new TextField("N\u00FAmero de Contrato");
        numeroContratoField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        numeroContratoField.setWidth(12, Unit.EM);
        l2.addComponent(numeroContratoField);

        fechaInicioField = new DateField("Fecha Inicio");
        fechaInicioField.setStyleName(ValoTheme.DATEFIELD_TINY);
        fechaInicioField.setWidth(12, Unit.EM);
        l2.addComponent(fechaInicioField);

        //line 3
        HorizontalLayout l3 = new HorizontalLayout();
        l3.setSpacing(true);
        contratoForm.addComponent(l3);

        duracionContratoField = new TextField("Duraci\u00F3n contrato (Meses)");
        duracionContratoField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        duracionContratoField.setWidth(9, Unit.EM);
        l3.addComponent(duracionContratoField);

        inactivoField = new CheckBox("Inactivo");
        inactivoField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        inactivoField.setWidth(9, Unit.EM);
        inactivoField.setStyleName("fixed-checkbox");
        l3.addComponent(inactivoField);

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
            Contrato contratoAux = contratoBeanFieldGroup.getItemDataSource().getBean();
            for (PerfilRecurso perfilRecurso: permisos){
                if (perfilRecurso.getId().getRecurso().getCodigo().equals(ContratoView.VIEW_NAME)){
                    if ((perfilRecurso.isCrear() && contratoAux.getId() == 0)||(perfilRecurso.isModificar() && contratoAux.getId() != 0) )
                        tienePermiso = true;
                }
            }
            if (!tienePermiso) {
                ui.getNavigator().navigateTo("accessDeniedView");
                close();
                return;
            }else {
                try {
                    contratoBeanFieldGroup.commit();
                    Contrato contrato = contratoBeanFieldGroup.getItemDataSource().getBean();

                    contratoRepository.save(contrato);
                    applicationEventBus.publish(ContratoGrid.NUEVO_CONTRATO, this, contrato);
                    Notification notification = new Notification("Notificaci\u00F3n", "Contrato guardado con \u00E9xito", Notification.Type.TRAY_NOTIFICATION);
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

    public Window show(long ID) {

        tipoContratoBeanItemContainer.removeAllItems();
        tipoContratoBeanItemContainer.addAll(tipoContratoRepository.findAll());

        Contrato contrato = contratoRepository.findOne(ID);
        if (contrato == null) {
            contrato = new Contrato();
            modificar = false;
        }
        else{
            modificar = true;
        }
        contratoBeanFieldGroup.setItemDataSource(contrato);

        return this;
    }

    public class TerceroSuggestionConverter implements SuggestField.SuggestionConverter {

        @Override
        public SuggestFieldSuggestion toSuggestion(Object item) {
            if (item != null) {
                Tercero tercero = (Tercero) item;
                return new SuggestFieldSuggestion(String.valueOf(tercero.getId()), tercero.getNombreTercero(), tercero.getNombreTercero());
            }
            return null;
        }

        @Override
        public Object toItem(SuggestFieldSuggestion suggestion) {
            return terceroRepository.findOne(Long.valueOf(suggestion.getId()));
        }
    }


}
