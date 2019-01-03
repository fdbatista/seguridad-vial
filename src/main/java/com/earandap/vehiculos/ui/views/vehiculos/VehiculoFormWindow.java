package com.earandap.vehiculos.ui.views.vehiculos;

import com.earandap.vehiculos.domain.*;
import com.earandap.vehiculos.domain.nomenclador.ClaseVehiculo;
import com.earandap.vehiculos.domain.nomenclador.Color;
import com.earandap.vehiculos.domain.nomenclador.LineaVehiculo;
import com.earandap.vehiculos.domain.nomenclador.MarcaVehiculo;
import com.earandap.vehiculos.domain.nomenclador.TipoCarroceria;
import com.earandap.vehiculos.domain.nomenclador.TipoCombustible;
import com.earandap.vehiculos.domain.nomenclador.TipoServicio;
import com.earandap.vehiculos.repository.*;
import com.earandap.vehiculos.ui.MainUI;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
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

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;

/**
 * Created by Angel Luis on 10/1/2015.
 */
@SpringComponent
@UIScope
public class VehiculoFormWindow extends Window /*implements EditDeleteButtonValueRenderer.EditDeleteButtonClickListener*/ {

    private BeanFieldGroup<Vehiculo> vehiculoBeanFieldGroup;

    @Autowired
    private EventBus.ApplicationEventBus applicationEventBus;

    //tab1
    private VerticalLayout content;

    @PropertyId("claseVehiculo")
    private ComboBox tipoVehiculoField;
    private BeanItemContainer<ClaseVehiculo> tipoVehiculoBeanItemContainer;

    @PropertyId("marcaVehiculo")
    private ComboBox marcaVehiculoField;
    private BeanItemContainer<MarcaVehiculo> marcaVehiculoBeanItemContainer;

    @PropertyId("lineaVehiculo")
    private ComboBox lineaVehiculoField;
    private BeanItemContainer<LineaVehiculo> lineaVehiculoBeanItemContainer;

    @PropertyId("tipoCarroceria")
    private ComboBox tipoCarroceriaField;
    private BeanItemContainer<TipoCarroceria> tipoCarroceriaBeanItemContainer;
    
    @PropertyId("tipoCombustible")
    private ComboBox tipoCombustibleVehiculoField;
    private BeanItemContainer<TipoCombustible> tipoCombustibleVehiculoBeanItemContainer;
    
    @PropertyId("color")
    private ComboBox colorVehiculoField;
    private BeanItemContainer<Color> colorVehiculoBeanItemContainer;
    
    @PropertyId("tipoServicio")
    private ComboBox tipoServicioVehiculoField;
    private BeanItemContainer<TipoServicio> tipoServicioVehiculoBeanItemContainer;

    @PropertyId("placa")
    private TextField placaField;

    @PropertyId("modelo")
    private TextField modeloField;

    @PropertyId("cilindrada")
    private TextField cilindradaField;

    @PropertyId("capacidad")
    private TextField capacidadField;

    @PropertyId("vin")
    private TextField vinField;

    @PropertyId("propietario")
    private SuggestField propietarioField;

    @PropertyId("afiliadoA")
    private SuggestField empresaAfiliadaField;

    @PropertyId("serieMotor")
    private TextField numeroMotorField;
    
    @PropertyId("chasis")
    private TextField numeroChasisField;
    
    @PropertyId("serie")
    private TextField numeroSerieField;

    @PropertyId("kmInicial")
    private TextField kmInicialField;

    @PropertyId("kmActual")
    private TextField kmActualField;

    private boolean modificar;

    //tab 2
    private Label seguroField;
    //private Button nuevoSeguroButton;

    @Autowired
    private VehiculoSeguroGrid seguroGrid;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private SeguroRepository seguroRepository;

    @Autowired
    private ClaseVehiculoRepository claseVehiculoRepository;

    @Autowired
    private MarcaVehiculoRepository marcaVehiculoRepository;

    @Autowired
    private LineaVehiculoRepository lineaVehiculoRepository;
    
    @Autowired
    private TipoCarroceriaVehiculoRepository tipoCarroceriaRepository;
    
    @Autowired
    private TipoCombustibleVehiculoRepository tipoCombustibleVehiculoRepository;

    @Autowired
    private ColorVehiculoRepository colorVehiculoRepository;

    @Autowired
    private TipoServicioVehiculoRepository tipoServicioVehiculoRepository;



    public void setModificar(boolean modificar) {
        this.modificar = modificar;
    }

    public ComboBox getMarcaVehiculoField() {
        return marcaVehiculoField;
    }

    public void setMarcaVehiculoField(ComboBox marcaVehiculoField) {
        this.marcaVehiculoField = marcaVehiculoField;
    }

    @PostConstruct
    public void init() {
        setWidth(700, Unit.PIXELS);
        setHeight(550, Unit.PIXELS);
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

        tabs.addTab(buildInfoGeneral(), "Informaci\u00F3n General");
        tabs.addTab(buildInfoAdicional(), "Seguros");

        Component footer = buildFooter();
        content.addComponent(footer);
        content.setComponentAlignment(footer, Alignment.BOTTOM_CENTER);

        vehiculoBeanFieldGroup = new BeanFieldGroup<>(Vehiculo.class);
        vehiculoBeanFieldGroup.bindMemberFields(this);

        this.setContent(content);
    }

    private Component buildInfoGeneral() {

        FormLayout infoGeneralForm = new FormLayout();
        infoGeneralForm.setSpacing(true);
        infoGeneralForm.setResponsive(true);

        //line 1
        HorizontalLayout l1 = new HorizontalLayout();
        l1.setSpacing(true);
        infoGeneralForm.addComponent(l1);

        placaField = new TextField("Placa");
        placaField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        placaField.setWidth(11, Unit.EM);
        placaField.setValidationVisible(true);
        placaField.setNullRepresentation("");
        placaField.setRequired(true);
        placaField.setMaxLength(20);
        placaField.setRequiredError("La placa es requerida");
        l1.addComponent(placaField);

        marcaVehiculoBeanItemContainer = new BeanItemContainer<>(MarcaVehiculo.class);
        marcaVehiculoField = new ComboBox("Marca");
        marcaVehiculoField.addStyleName(ValoTheme.COMBOBOX_TINY);
        marcaVehiculoField.setWidth(11, Unit.EM);
        marcaVehiculoField.setContainerDataSource(marcaVehiculoBeanItemContainer);
        marcaVehiculoField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        marcaVehiculoField.setItemCaptionPropertyId("nombre");
        marcaVehiculoField.setNullSelectionAllowed(false);
        marcaVehiculoField.setScrollToSelectedItem(true);
        marcaVehiculoField.setRequired(true);
        marcaVehiculoField.setRequiredError("La marca es requerida");
        marcaVehiculoField.addValueChangeListener(new ValueChangeListener() {
            public void valueChange(ValueChangeEvent event) {
                lineaVehiculoBeanItemContainer.removeAllItems();
                MarcaVehiculo marca = vehiculoBeanFieldGroup.getItemDataSource().getBean().getMarcaVehiculo();
                marca = ((MarcaVehiculo) marcaVehiculoField.getValue());
                lineaVehiculoBeanItemContainer.addAll(lineaVehiculoRepository.getByIdMarca(marca));
                lineaVehiculoField.setInputPrompt("Seleccione");
                lineaVehiculoField.setValue(null);
                lineaVehiculoField.focus();
            }
        });
        l1.addComponent(marcaVehiculoField);

        lineaVehiculoBeanItemContainer = new BeanItemContainer<>(LineaVehiculo.class);
        lineaVehiculoField = new ComboBox("L\u00EDnea");
        lineaVehiculoField.addStyleName(ValoTheme.COMBOBOX_TINY);
        lineaVehiculoField.setWidth(11, Unit.EM);
        lineaVehiculoField.setContainerDataSource(lineaVehiculoBeanItemContainer);
        lineaVehiculoField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        lineaVehiculoField.setItemCaptionPropertyId("nombre");
        lineaVehiculoField.setNullSelectionAllowed(false);
        lineaVehiculoField.setScrollToSelectedItem(true);
        lineaVehiculoField.setRequired(true);
        lineaVehiculoField.setRequiredError("La l\u00EDnea del veh\u00EDculo es requerida");
        l1.addComponent(lineaVehiculoField);

        tipoVehiculoBeanItemContainer = new BeanItemContainer<>(ClaseVehiculo.class);
        tipoVehiculoField = new ComboBox("Tipo Veh\u00EDculo");
        tipoVehiculoField.addStyleName(ValoTheme.COMBOBOX_TINY);
        tipoVehiculoField.setWidth(11, Unit.EM);
        tipoVehiculoField.setContainerDataSource(tipoVehiculoBeanItemContainer);
        tipoVehiculoField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        tipoVehiculoField.setItemCaptionPropertyId("descripcion");
        tipoVehiculoField.setNullSelectionAllowed(false);
        tipoVehiculoField.setScrollToSelectedItem(true);
        tipoVehiculoField.setRequired(true);
        tipoVehiculoField.setRequiredError("El tipo es requerido");
        l1.addComponent(tipoVehiculoField);

        //line 2
        HorizontalLayout l2 = new HorizontalLayout();
        infoGeneralForm.addComponent(l2);
        l2.setSpacing(true);

        modeloField = new TextField("Modelo");
        modeloField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        modeloField.setWidth(11, Unit.EM);
        modeloField.setValidationVisible(true);
        modeloField.setNullRepresentation("");
        modeloField.setRequired(true);
        modeloField.setMaxLength(20);
        modeloField.setRequiredError("El modelo es requerido");
        l2.addComponent(modeloField);

        cilindradaField = new TextField("Cilindrada C.C");
        cilindradaField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        cilindradaField.setWidth(11, Unit.EM);
        cilindradaField.setValidationVisible(true);
        cilindradaField.setNullRepresentation("");
        cilindradaField.setRequired(true);
        cilindradaField.setMaxLength(20);
        cilindradaField.setRequiredError("La cilindrada es requerida");
        if (!this.modificar) {
            cilindradaField.setValue("");
        }
        l2.addComponent(cilindradaField);

        capacidadField = new TextField("Capacidad KG/PS");
        capacidadField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        capacidadField.setWidth(11, Unit.EM);
        capacidadField.setValidationVisible(true);
        capacidadField.setNullRepresentation("");
        capacidadField.setRequired(true);
        capacidadField.setMaxLength(20);
        capacidadField.setRequiredError("La capacidad es requerida");
        if (!this.modificar) {
            capacidadField.setValue("");
        }
        l2.addComponent(capacidadField);

        vinField = new TextField("VIN");
        vinField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        vinField.setWidth(11, Unit.EM);
        vinField.setValidationVisible(true);
        vinField.setNullRepresentation("");
        vinField.setRequired(false);
        vinField.setMaxLength(20);
        capacidadField.setRequiredError("El VIN es requerido");
        l2.addComponent(vinField);

        //line 3
        HorizontalLayout l3 = new HorizontalLayout();
        infoGeneralForm.addComponent(l3);
        l3.setSpacing(true);

        tipoCarroceriaBeanItemContainer = new BeanItemContainer<>(TipoCarroceria.class);
        tipoCarroceriaField = new ComboBox("Carrocer\u00EDa");
        tipoCarroceriaField.addStyleName(ValoTheme.COMBOBOX_TINY);
        tipoCarroceriaField.setWidth(11, Unit.EM);
        tipoCarroceriaField.setContainerDataSource(tipoCarroceriaBeanItemContainer);
        tipoCarroceriaField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        tipoCarroceriaField.setItemCaptionPropertyId("descripcion");
        tipoCarroceriaField.setNullSelectionAllowed(false);
        tipoCarroceriaField.setScrollToSelectedItem(true);
        tipoCarroceriaField.setRequired(true);
        tipoCarroceriaField.setRequiredError("La carrocer\u00EDa es requerida");
        l3.addComponent(tipoCarroceriaField);

        tipoCombustibleVehiculoBeanItemContainer = new BeanItemContainer<>(TipoCombustible.class);
        tipoCombustibleVehiculoField = new ComboBox("Combustible");
        tipoCombustibleVehiculoField.addStyleName(ValoTheme.COMBOBOX_TINY);
        tipoCombustibleVehiculoField.setWidth(11, Unit.EM);
        tipoCombustibleVehiculoField.setContainerDataSource(tipoCombustibleVehiculoBeanItemContainer);
        tipoCombustibleVehiculoField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        tipoCombustibleVehiculoField.setItemCaptionPropertyId("descripcion");
        tipoCombustibleVehiculoField.setNullSelectionAllowed(false);
        tipoCombustibleVehiculoField.setScrollToSelectedItem(true);
        tipoCombustibleVehiculoField.setRequired(true);
        tipoCombustibleVehiculoField.setRequiredError("El combustible es requerido");
        l3.addComponent(tipoCombustibleVehiculoField);

        colorVehiculoBeanItemContainer = new BeanItemContainer<>(Color.class);
        colorVehiculoField = new ComboBox("Color");
        colorVehiculoField.addStyleName(ValoTheme.COMBOBOX_TINY);
        colorVehiculoField.setWidth(11, Unit.EM);
        colorVehiculoField.setContainerDataSource(colorVehiculoBeanItemContainer);
        colorVehiculoField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        colorVehiculoField.setItemCaptionPropertyId("descripcion");
        colorVehiculoField.setNullSelectionAllowed(false);
        colorVehiculoField.setScrollToSelectedItem(true);
        colorVehiculoField.setRequired(true);
        colorVehiculoField.setRequiredError("El color es requerido");
        l3.addComponent(colorVehiculoField);

        tipoServicioVehiculoBeanItemContainer = new BeanItemContainer<>(TipoServicio.class);
        tipoServicioVehiculoField = new ComboBox("Tipo Servicio");
        tipoServicioVehiculoField.addStyleName(ValoTheme.COMBOBOX_TINY);
        tipoServicioVehiculoField.setWidth(11, Unit.EM);
        tipoServicioVehiculoField.setContainerDataSource(tipoServicioVehiculoBeanItemContainer);
        tipoServicioVehiculoField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        tipoServicioVehiculoField.setItemCaptionPropertyId("descripcion");
        tipoServicioVehiculoField.setNullSelectionAllowed(false);
        tipoServicioVehiculoField.setScrollToSelectedItem(true);
        tipoServicioVehiculoField.setRequired(true);
        tipoServicioVehiculoField.setRequiredError("El tipo de servicio es requerido");
        l3.addComponent(tipoServicioVehiculoField);

        //line 4
        HorizontalLayout l4 = new HorizontalLayout();
        infoGeneralForm.addComponent(l4);
        l4.setSpacing(true);

        numeroMotorField = new TextField("N\u00FAmero del Motor");
        numeroMotorField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        numeroMotorField.setWidth(12, Unit.EM);
        numeroMotorField.setValidationVisible(true);
        numeroMotorField.setNullRepresentation("");
        l4.addComponent(numeroMotorField);
        
        numeroChasisField = new TextField("N\u00FAmero de Chasis");
        numeroChasisField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        numeroChasisField.setWidth(12, Unit.EM);
        numeroChasisField.setValidationVisible(true);
        numeroChasisField.setNullRepresentation("");
        l4.addComponent(numeroChasisField);
        
        numeroSerieField = new TextField("N\u00FAmero de Serie");
        numeroSerieField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        numeroSerieField.setWidth(12, Unit.EM);
        numeroSerieField.setValidationVisible(true);
        numeroSerieField.setNullRepresentation("");
        l4.addComponent(numeroSerieField);

        
        //line 5
        HorizontalLayout l5 = new HorizontalLayout();
        infoGeneralForm.addComponent(l5);
        l5.setSpacing(true);

        propietarioField = new SuggestField();
        propietarioField.setWidth(30, Unit.EM);
        propietarioField.setCaption("Propietario");
        propietarioField.setNewItemsAllowed(false);
        propietarioField.setSuggestionHandler(s -> {
            List<Object> result = new ArrayList<>();
            List<Persona> personas = personaRepository.search(s);
            for (Persona persona : personas) {
                result.add(persona);
            }
            if (result.size() == 0) {
                Notification.show("No se encontraron personas para el criterio introducido", Notification.Type.TRAY_NOTIFICATION);
            }

            return result;
        });
        propietarioField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        propietarioField.setImmediate(true);
        propietarioField.setTokenMode(false);
        propietarioField.setSuggestionConverter(new PersonaSuggestionConverter());
        propietarioField.setWidth("300px");
        propietarioField.setPopupWidth(400);
        l5.addComponent(propietarioField);
        
        kmInicialField = new TextField("KM Inicial");
        kmInicialField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        kmInicialField.setWidth(6, Unit.EM);
        kmInicialField.setValidationVisible(true);
        l5.addComponent(kmInicialField);

        kmActualField = new TextField("KM Actual");
        kmActualField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        kmActualField.setWidth(6, Unit.EM);
        kmInicialField.setValidationVisible(true);
        l5.addComponent(kmActualField);

        //line 6
        HorizontalLayout l6 = new HorizontalLayout();
        infoGeneralForm.addComponent(l6);
        l6.setSpacing(true);

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
            if (result.size() == 0) {
                Notification.show("No se encontraron empresas para el criterio introducido", Notification.Type.TRAY_NOTIFICATION);
            }

            return result;
        });
        empresaAfiliadaField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        empresaAfiliadaField.setImmediate(true);
        empresaAfiliadaField.setTokenMode(false);
        empresaAfiliadaField.setSuggestionConverter(new EmpresaSuggestionConverter());
        empresaAfiliadaField.setWidth("300px");
        empresaAfiliadaField.setPopupWidth(400);
        l6.addComponent(empresaAfiliadaField);

        return infoGeneralForm;
    }

    private Component buildInfoAdicional() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(new MarginInfo(true, false, true, false));
        layout.setSpacing(true);
        HorizontalLayout l1 = new HorizontalLayout();
        layout.addComponent(l1);
        l1.setSpacing(true);

        seguroField = new Label("Seguros y Vencimientos");
        seguroField.addStyleName(ValoTheme.LABEL_BOLD);
        seguroField.setWidth(12, Unit.EM);
        l1.addComponent(seguroField);

        layout.addComponent(seguroGrid);
        return layout;
    }
    
    private boolean existeSeguroSOAT(Object[] seguros) {
        int cantElems = seguros.length;
        Seguro seguroTmp;
        for (int i = 0; i < cantElems; i++) {
            seguroTmp = (Seguro) seguros[i];
            if (seguroTmp.getTipoSeguro().getDescripcion().equals("SOAT")) {
                return true;
            }
        }
        return false;
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
        guardar.addClickListener((Button.ClickEvent event) -> {
            MainUI ui = (MainUI) UI.getCurrent();
            Authentication auth = ui.getSecurity().getAuthentication();
            Set<PerfilRecurso> permisos = ((Usuario) auth.getPrincipal()).getPerfil().getPerfilRecursos();
            boolean tienePermiso = false;
            Vehiculo vehiculoAux = vehiculoBeanFieldGroup.getItemDataSource().getBean();
            
            Set<Seguro> segurosAnteriores = vehiculoAux.getSeguros();
            Map<Long, Seguro> segurosAdicionados = seguroGrid.getSegurosAdicionados();
            Map<Long, Seguro> segurosModificados = seguroGrid.getSegurosModificados();
            Map<Long, Seguro> segurosEliminados = seguroGrid.getSegurosEliminados();
            
            boolean existeSeguroSoat = existeSeguroSOAT(segurosAdicionados.values().toArray());
            
            if (!existeSeguroSoat) {
                existeSeguroSoat = existeSeguroSOAT(segurosModificados.values().toArray());
            }
            
            if (!existeSeguroSoat) {
                Object[] arrSegurosAnteriores = vehiculoAux.getSeguros().toArray();
                int cantElems = arrSegurosAnteriores.length;
                Seguro seguroAux;
                for (int i = 0; i < cantElems; i++) {
                    seguroAux = (Seguro) arrSegurosAnteriores[i];
                    if (seguroAux.getTipoSeguro().getDescripcion().equals("SOAT") && !segurosEliminados.containsValue(seguroAux))
                    {
                        existeSeguroSoat = true;
                        break;
                    }
                }
            }
            
            if (existeSeguroSoat) {
                for (PerfilRecurso perfilRecurso : permisos) {
                    if (perfilRecurso.getId().getRecurso().getCodigo().equals(VehiculoView.VIEW_NAME)) {
                        if ((perfilRecurso.isCrear() && vehiculoAux.getId() == 0) || (perfilRecurso.isModificar() && vehiculoAux.getId() != 0)) {
                            tienePermiso = true;
                        }
                    }
                }
                if (!tienePermiso) {
                    ui.getNavigator().navigateTo("accessDeniedView");
                    close();
                    return;
                } else {
                    try {
                        vehiculoBeanFieldGroup.commit();
                        Vehiculo vehiculo = vehiculoBeanFieldGroup.getItemDataSource().getBean();
                        if (!modificar) {
                            if (vehiculoRepository.findOneByPlaca(vehiculo.getPlaca()).isPresent()) {
                                throw new FieldGroup.CommitException(new Validator.InvalidValueException("Ya existe el veh\u00EDculo"));
                            }
                        }
                        vehiculoRepository.save(vehiculo);

                        for (Map.Entry<Long, Seguro> seguroMap : segurosAdicionados.entrySet()) {
                            Seguro seguro = seguroMap.getValue();
                            seguro.setVehiculo(vehiculo);
                            seguroRepository.save(seguro);
                        }

                        for (Map.Entry<Long, Seguro> seguroMap : segurosModificados.entrySet()) {
                            Seguro seguro = seguroMap.getValue();
                            seguro.setVehiculo(vehiculo);
                            seguroRepository.save(seguro);
                        }

                        for (Map.Entry<Long, Seguro> seguroMap : segurosEliminados.entrySet()) {
                            Seguro seguro = seguroMap.getValue();

                            seguroRepository.delete(seguro);
                        }
                        seguroGrid.restablecerListados();
                        applicationEventBus.publish(VehiculoGrid.NUEVO_VEHICULO, this, vehiculo);
                        Notification notification = new Notification("Notificaci\u00F3n", "Veh\u00EDculo guardado con \u00E9xito", Notification.Type.TRAY_NOTIFICATION);
                        notification.setDelayMsec(3000);
                        notification.setIcon(FontAwesome.INFO_CIRCLE);
                        notification.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
                        notification.show(Page.getCurrent());
                        close();
                    } catch (FieldGroup.CommitException e) {
                        if ((e instanceof FieldGroup.CommitException && e.getCause() instanceof Validator.InvalidValueException || e.getCause() instanceof Validator.EmptyValueException)) {
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
            }
            else {
                Notification notification = new Notification("Alerta", "Debe especificar un seguro tipo SOAT", Notification.Type.WARNING_MESSAGE);
                notification.setPosition(Position.BOTTOM_RIGHT);
                notification.setDelayMsec(3000);
                notification.setIcon(FontAwesome.WARNING);
                notification.show(Page.getCurrent());
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

        tipoVehiculoBeanItemContainer.removeAllItems();
        tipoVehiculoBeanItemContainer.addAll(claseVehiculoRepository.findAll());

        marcaVehiculoBeanItemContainer.removeAllItems();
        marcaVehiculoBeanItemContainer.addAll(marcaVehiculoRepository.findAll());

        tipoCarroceriaBeanItemContainer.removeAllItems();
        tipoCarroceriaBeanItemContainer.addAll(tipoCarroceriaRepository.findAll());

        tipoCombustibleVehiculoBeanItemContainer.removeAllItems();
        tipoCombustibleVehiculoBeanItemContainer.addAll(tipoCombustibleVehiculoRepository.findAll());

        colorVehiculoBeanItemContainer.removeAllItems();
        colorVehiculoBeanItemContainer.addAll(colorVehiculoRepository.findAll());

        tipoServicioVehiculoBeanItemContainer.removeAllItems();
        tipoServicioVehiculoBeanItemContainer.addAll(tipoServicioVehiculoRepository.findAll());

        Vehiculo vehiculo = vehiculoRepository.findOne(ID);
        if (vehiculo == null) {
            vehiculo = new Vehiculo();
            vehiculo.setCapacidad(null);
            vehiculo.setCilindrada(null);
            this.setModificar(false);
        } else {
            this.setModificar(true);
            placaField.setEnabled(false);
        }
        vehiculoBeanFieldGroup.setItemDataSource(vehiculo);
        vehiculoBeanFieldGroup.discard();

        seguroGrid.container.removeAllItems();
        seguroGrid.container.addAll(vehiculo.getSeguros());

        return this;
    }

    public class PersonaSuggestionConverter implements SuggestField.SuggestionConverter {

        @Override
        public SuggestFieldSuggestion toSuggestion(Object item) {
            if (item != null) {
                Persona persona = (Persona) item;
                return new SuggestFieldSuggestion(String.valueOf(persona.getId()), persona.getNombreCompleto(), persona.getNombreCompleto());
            }
            return null;
        }

        @Override
        public Object toItem(SuggestFieldSuggestion suggestion) {
            return personaRepository.findOne(Long.valueOf(suggestion.getId()));
        }
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
