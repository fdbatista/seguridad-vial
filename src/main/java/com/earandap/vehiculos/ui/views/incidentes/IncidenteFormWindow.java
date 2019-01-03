package com.earandap.vehiculos.ui.views.incidentes;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.*;
import com.earandap.vehiculos.domain.nomenclador.*;
import com.earandap.vehiculos.repository.*;
import com.earandap.vehiculos.ui.MainUI;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.vaadin.spring.events.EventBus;
import org.vaadin.suggestfield.SuggestField;
import org.vaadin.suggestfield.client.SuggestFieldSuggestion;

import javax.annotation.PostConstruct;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;

/**
 * @Autor Eduardo Aranda
 * @Version 0.1
 */
@SpringComponent
@UIScope
public class IncidenteFormWindow extends Window {

    private boolean modificar;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.driver-class-name}")
    private String dbDriver;

    private String fileDir;

    @Autowired
    private EventBus.ApplicationEventBus applicationEventBus;

    private BeanFieldGroup<Incidente> incidenteBeanFieldGroup;

    //tab 1
    private VerticalLayout content;

    @PropertyId("fecha")
    private DateField fechaIncidenteField;

    @PropertyId("vehiculo")
    private SuggestField placaVehiculoField;

    @PropertyId("conductor")
    private SuggestField conductorField;

    @PropertyId("observaciones")
    private TextArea observacionesField;

    @PropertyId("zona")
    private ComboBox zonaField;
    private BeanItemContainer<Zona> zonaContainer;

    @PropertyId("direccion")
    private TextField lugarField;

    @PropertyId("reportero")
    private SuggestField personaReportaField;

    @PropertyId("tipoEvento")
    private ComboBox tipoEventoField;
    private BeanItemContainer<TipoEvento> tipoEventoContainer;

    @PropertyId("tiempoConduccion")
    private TextField tiempoConduccionField;

    @PropertyId("tipoJornada")
    private ComboBox tipoJornadaField;
    private BeanItemContainer<TipoJornada> tipoJornadaContainer;

    @PropertyId("accidenteTrabajo")
    private CheckBox accidenteTrabajoField;

    @PropertyId("incapacidad")
    private CheckBox huboIncapacidadField;

    @PropertyId("diasIncapacidad")
    private TextField diasIncapacidadField;

    @PropertyId("factoresPersonales")
    private TextArea factoresPersonalesField;

    @PropertyId("factoresTrabajo")
    private TextArea factoresTrabajoField;

    //tab 2
    @PropertyId("partesAfectadas")
    private OptionGroup partesAfectadasField;
    private BeanItemContainer<PartesAfectada> partesAfectadaContainer;

    @PropertyId("causaInmediatas")
    private OptionGroup causasInmediatasField;
    private BeanItemContainer<CausaInmediata> causaInmediataContainer;

    @PropertyId("causaBasicas")
    private OptionGroup causasBasicasField;
    private BeanItemContainer<CausaBasica> causaBasicaContainer;

    @PropertyId("perdidas")
    private OptionGroup perdidasField;
    private BeanItemContainer<Perdida> perdidaContainer;

    @Autowired
    private IncidenteArchivoGrid incidenteArchivoGrid;

    //tab 3
    @Autowired
    private IncidenteRepository incidenteRepository;

    @Autowired
    private ZonaRepository zonaRepository;

    @Autowired
    private TipoEventoRepository tipoEventoRepository;

    @Autowired
    private TipoJornadaRepository tipoJornadaRepository;

    @Autowired
    private ParteAfectadaRepository parteAfectadaRepository;

    @Autowired
    private CausaInmediataRepository causaInmediataRepository;

    @Autowired
    private CausaBasicaRepository causaBasicaRepository;

    @Autowired
    private PerdidaRepository perdidaRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private ConductorRepository conductorRepository;
    
    @Autowired
    private TerceroRepository terceroRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private ArchivoRepository archivoRepository;

    @Autowired
    private InfraccionRepository infraccionRepository;

    @Autowired
    private InvestigacionRepository investigacionRepository;

    @Autowired
    private AccidenteInvolucradoRepository accidenteInvolucradoRepository;

    @Autowired
    private IncidenteLesionRepository incidenteLesionRepository;

    //tab 4
    private Label infraccionField;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private IncidenteInfraccionGrid incidenteInfraccionGrid;

    @Autowired
    private IncidenteInvestigacionGrid incidenteInvestigacionGrid;

    @Autowired
    private AccidenteInvolucradoGrid accidenteInvolucradoGrid;

    @Autowired
    private IncidenteLesionGrid incidenteLesionGrid;

    @PropertyId("lesionadosEmpresa")
    private TextField lesionadosEmpresaField;

    @PropertyId("condLesionadosEmpresa")
    private TextField condLesionadosEmpresaField;

    @PropertyId("mortalidadEmpresa")
    private TextField mortalidadEmpresaField;

    @PropertyId("mortalidadTerceros")
    private TextField mortalidadTercerosField;

    @PropertyId("indemnizacion")
    private TextField indemnizacionField;

    //tab Investigaciones
    /*@PropertyId("relato")
    private TextArea investigacionRelatoField;

    @PropertyId("leccion")
    private TextArea leccionAprendidaField;

    @PropertyId("socializada")
    private CheckBox socializadaField;

    @PropertyId("medioDivulgacion")
    private TextField medioDivulgacionField;*/
    @Autowired
    private ParametrosRepository parametrosRepository;

    @PostConstruct
    public void init() {
        setWidth(850, Unit.PIXELS);
        setHeight(600, Unit.PIXELS);
        setModal(true);
        setCloseShortcut(ShortcutAction.KeyCode.ESCAPE, null);
        setResizable(false);
        setClosable(false);
        this.fileDir = parametrosRepository.findOne(Long.valueOf("2")).getValorParametro();

        Responsive.makeResponsive(this);

        content = new VerticalLayout();
        content.setSizeFull();
        content.setMargin(true);

        TabSheet tabs = new TabSheet();
        content.addComponent(tabs);
        content.setExpandRatio(tabs, 1);

        tabs.addTab(buildInfoBasica(), "Informaci\u00F3n B\u00E1sica");
        tabs.addTab(buildInfoFactores(), "Factores");
        tabs.addTab(buildInfoCausas(), "Causas");
        tabs.addTab(buildInfoAccidente(), "Informaci\u00F3n del Accidente");
        tabs.addTab(buildInfracciones(), "Infracciones");
        tabs.addTab(buildInvestigaciones(), "Investigaciones");
        Component footer = buildFooter();
        content.addComponent(footer);
        content.setComponentAlignment(footer, Alignment.BOTTOM_CENTER);

        this.setContent(content);

        incidenteBeanFieldGroup = new BeanFieldGroup<>(Incidente.class);
        incidenteBeanFieldGroup.bindMemberFields(this);
    }

    private Component buildInfoBasica() {

        FormLayout layout = new FormLayout();
        layout.setSpacing(true);
        layout.setResponsive(true);

        //line 1
        HorizontalLayout l1 = new HorizontalLayout();
        l1.setSpacing(true);
        layout.addComponent(l1);

        fechaIncidenteField = new DateField("Fecha Incidente");
        fechaIncidenteField.addStyleName(ValoTheme.DATEFIELD_TINY);
        fechaIncidenteField.setWidth(9, Unit.EM);
        fechaIncidenteField.setRequired(true);
        fechaIncidenteField.setRangeEnd(new Date());
        StaticMembers.setErrorRangoEspanhol(fechaIncidenteField);
        fechaIncidenteField.setRequiredError("La fecha del incidente es requerida");
        l1.addComponent(fechaIncidenteField);

        placaVehiculoField = new SuggestField();
        placaVehiculoField.setWidth(12, Unit.EM);
        placaVehiculoField.setCaption("Placa del Vehículo");
        placaVehiculoField.setNewItemsAllowed(false);
        placaVehiculoField.setSuggestionHandler(s -> {
            List<Object> result = new ArrayList<>();
            List<Vehiculo> vehiculos = vehiculoRepository.search(s);
            for (Vehiculo vehiculo : vehiculos) {
                result.add(vehiculo);
            }
            if (result.size() == 0) {
                Notification.show("No se encontraron vehiculos para el criterio introducido", Notification.Type.TRAY_NOTIFICATION);
            }

            return result;
        });
        placaVehiculoField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        placaVehiculoField.setImmediate(true);
        placaVehiculoField.setTokenMode(false);
        placaVehiculoField.setSuggestionConverter(new VehiculoSuggestionConverter());
        placaVehiculoField.setPopupWidth(400);
        placaVehiculoField.setRequired(true);
        placaVehiculoField.setRequiredError("El Vehículo es requerido");
        l1.addComponent(placaVehiculoField);

        conductorField = new SuggestField();
        conductorField.setWidth(30, Unit.EM);
        conductorField.setRequired(true);
        conductorField.setRequiredError("El conductor es requerido");
        conductorField.setCaption("Conductor");
        conductorField.setNewItemsAllowed(false);
        conductorField.setSuggestionHandler(s -> {
            List<Object> result = new ArrayList<>();
            List<Conductor> conductores = conductorRepository.search(s);
            for (Conductor conductor : conductores) {
                result.add(conductor);
            }
            if (result.size() == 0) {
                Notification.show("No se encontraron conductores para el criterio introducido", Notification.Type.TRAY_NOTIFICATION);
            }
            return result;
        });
        conductorField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        conductorField.setImmediate(true);
        conductorField.setTokenMode(false);
        conductorField.setSuggestionConverter(new ConductorSuggestionConverter());
        conductorField.setWidth("250px");
        conductorField.setPopupWidth(400);
        l1.addComponent(conductorField);

        //line 2
        HorizontalLayout l2 = new HorizontalLayout();
        layout.addComponent(l2);
        l2.setSpacing(true);

        observacionesField = new TextArea("Observaciones");
        observacionesField.addStyleName(ValoTheme.TEXTAREA_TINY);
        observacionesField.setNullRepresentation("");
        observacionesField.setColumns(50);
        observacionesField.setRows(2);
        l2.addComponent(observacionesField);

        //line 3
        HorizontalLayout l3 = new HorizontalLayout();
        layout.addComponent(l3);
        l3.setSpacing(true);

        zonaContainer = new BeanItemContainer<>(Zona.class);

        zonaField = new ComboBox("Zona");
        zonaField.addStyleName(ValoTheme.COMBOBOX_TINY);
        zonaField.setContainerDataSource(zonaContainer);
        zonaField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        zonaField.setItemCaptionPropertyId("descripcion");
        zonaField.setNullSelectionAllowed(false);
        zonaField.setScrollToSelectedItem(true);

        l3.addComponent(zonaField);

        lugarField = new TextField("Lugar donde ocurri\u00F3");
        lugarField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        lugarField.setNullRepresentation("");
        lugarField.setWidth(33, Unit.EM);
        l3.addComponent(lugarField);

        //line 4
        HorizontalLayout l4 = new HorizontalLayout();
        layout.addComponent(l4);
        l4.setSpacing(true);

        personaReportaField = new SuggestField();
        personaReportaField.setWidth(30, Unit.EM);
        personaReportaField.setCaption("Persona que reporta:");
        personaReportaField.setNewItemsAllowed(false);
        personaReportaField.setSuggestionHandler(s -> {
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
        personaReportaField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        personaReportaField.setImmediate(true);
        personaReportaField.setTokenMode(false);
        personaReportaField.setSuggestionConverter(new PersonaSuggestionConverter());
        personaReportaField.setWidth("250px");
        personaReportaField.setPopupWidth(400);

        l4.addComponent(personaReportaField);

        tipoEventoContainer = new BeanItemContainer<>(TipoEvento.class);

        tipoEventoField = new ComboBox("Tipo de Evento");
        tipoEventoField.addStyleName(ValoTheme.COMBOBOX_TINY);
        tipoEventoField.setContainerDataSource(tipoEventoContainer);
        tipoEventoField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        tipoEventoField.setItemCaptionPropertyId("descripcion");
        tipoEventoField.setNullSelectionAllowed(false);
        tipoEventoField.setScrollToSelectedItem(true);

        l4.addComponent(tipoEventoField);

        //line 5
        HorizontalLayout l5 = new HorizontalLayout();
        layout.addComponent(l5);
        l5.setSpacing(true);

        HorizontalLayout c1 = new HorizontalLayout();
        c1.setSpacing(true);
        c1.setCaption("Tiempo de conducci\u00F3n");
        l5.addComponent(c1);

        tiempoConduccionField = new TextField();
        tiempoConduccionField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        tiempoConduccionField.setWidth(3, Unit.EM);
        c1.addComponent(tiempoConduccionField);
        c1.addComponent(new Label("horas"));

        tipoJornadaContainer = new BeanItemContainer<TipoJornada>(TipoJornada.class);

        tipoJornadaField = new ComboBox("Tipo jornada laboral");
        tipoJornadaField.addStyleName(ValoTheme.COMBOBOX_TINY);
        tipoJornadaField.setContainerDataSource(tipoJornadaContainer);
        tipoJornadaField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        tipoJornadaField.setItemCaptionPropertyId("descripcion");
        tipoJornadaField.setNullSelectionAllowed(false);
        tipoJornadaField.setScrollToSelectedItem(true);
        tipoJornadaField.setRequired(true);
        tipoJornadaField.setRequiredError("El tipo de jornada es requerido");
        l5.addComponent(tipoJornadaField);

        //line 6
        HorizontalLayout l6 = new HorizontalLayout();
        layout.addComponent(l6);
        l6.setSpacing(true);

        huboIncapacidadField = new CheckBox("Hubo incapacidad");
        huboIncapacidadField.addStyleName(ValoTheme.CHECKBOX_SMALL);
        l6.addComponent(huboIncapacidadField);

        HorizontalLayout c2 = new HorizontalLayout();
        c2.setSpacing(true);

        l6.addComponent(c2);
        diasIncapacidadField = new TextField();
        diasIncapacidadField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        diasIncapacidadField.setWidth(3, Unit.EM);
        c2.addComponent(diasIncapacidadField);
        c2.addComponent(new Label("d\u00EDas"));

        return layout;
    }

    private boolean tieneParteAfectada(PartesAfectada parteAfectada) {
        Set<PartesAfectada> partesAfectadas = incidenteBeanFieldGroup.getItemDataSource().getBean().getPartesAfectadas();
        for (PartesAfectada parte : partesAfectadas) {
            if (parte.getId() == parteAfectada.getId()) {
                return true;
            }
        }
        return false;
    }

    private void actualizarCheckBoxLists() {

        Incidente incidente = incidenteBeanFieldGroup.getItemDataSource().getBean();

        partesAfectadasField.removeAllItems();
        List<PartesAfectada> partesGral = partesAfectadaContainer.getItemIds();
        Set<PartesAfectada> partesAfectadas = incidente.getPartesAfectadas();
        for (PartesAfectada obj : partesGral) {
            partesAfectadasField.addItem(obj);
            if (partesAfectadas.contains(obj)) {
                partesAfectadasField.select(obj);
            }
        }

        causasInmediatasField.removeAllItems();
        List<CausaInmediata> causasInmediatasGral = causaInmediataContainer.getItemIds();
        Set<CausaInmediata> causasInmediatas = incidente.getCausaInmediatas();
        for (CausaInmediata obj : causasInmediatasGral) {
            causasInmediatasField.addItem(obj);
            if (causasInmediatas.contains(obj)) {
                causasInmediatasField.select(obj);
            }
        }

        causasBasicasField.removeAllItems();
        List<CausaBasica> causasBasicasGral = causaBasicaContainer.getItemIds();
        Set<CausaBasica> causasBasicas = incidente.getCausaBasicas();
        for (CausaBasica obj : causasBasicasGral) {
            causasBasicasField.addItem(obj);
            if (causasBasicas.contains(obj)) {
                causasBasicasField.select(obj);
            }
        }

        perdidasField.removeAllItems();
        List<Perdida> perdidasGral = perdidaContainer.getItemIds();
        Set<Perdida> perdidas = incidente.getPerdidas();
        for (Perdida obj : perdidasGral) {
            perdidasField.addItem(obj);
            if (perdidas.contains(obj)) {
                perdidasField.select(obj);
            }
        }

    }

    private Component buildInfoCausas() {

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setMargin(true);
        layout.setResponsive(true);

        HorizontalLayout l1 = new HorizontalLayout();
        l1.setSpacing(true);
        layout.addComponent(l1);

        partesAfectadaContainer = new BeanItemContainer<>(PartesAfectada.class);
        partesAfectadasField = new OptionGroup();
        partesAfectadasField.setMultiSelect(true);
        VerticalLayout panelLayoutPartesAfectadas = new VerticalLayout();
        Panel panelPartesAfectadas = new Panel("Partes afectadas");
        panelPartesAfectadas.setHeight("250px");
        panelPartesAfectadas.setWidth("155px");
        panelPartesAfectadas.addStyleName("padding");
        panelLayoutPartesAfectadas.addComponent(partesAfectadasField);
        panelPartesAfectadas.setContent(panelLayoutPartesAfectadas);
        l1.addComponent(panelPartesAfectadas);

        causaInmediataContainer = new BeanItemContainer<>(CausaInmediata.class);
        causasInmediatasField = new OptionGroup();
        causasInmediatasField.setMultiSelect(true);
        VerticalLayout panelLayoutCausasInmediatas = new VerticalLayout();
        Panel panelCausasInmediatas = new Panel("Causas Inmediatas");
        panelCausasInmediatas.setHeight("250px");
        panelCausasInmediatas.setWidth("235px");
        panelCausasInmediatas.addStyleName("padding");
        panelLayoutCausasInmediatas.addComponent(causasInmediatasField);
        panelCausasInmediatas.setContent(panelLayoutCausasInmediatas);
        l1.addComponent(panelCausasInmediatas);

        causaBasicaContainer = new BeanItemContainer<>(CausaBasica.class);
        causasBasicasField = new OptionGroup();
        causasBasicasField.setMultiSelect(true);
        VerticalLayout panelLayoutCausasBasicas = new VerticalLayout();
        Panel panelCausasBasicas = new Panel("Causas B\u00E1sicas");
        panelCausasBasicas.setHeight("250px");
        panelCausasBasicas.setWidth("218px");
        panelCausasBasicas.addStyleName("padding");
        panelLayoutCausasBasicas.addComponent(causasBasicasField);
        panelCausasBasicas.setContent(panelLayoutCausasBasicas);
        l1.addComponent(panelCausasBasicas);

        perdidaContainer = new BeanItemContainer<>(Perdida.class);
        perdidasField = new OptionGroup();
        perdidasField.setMultiSelect(true);
        VerticalLayout panelLayoutPerdidas = new VerticalLayout();
        Panel panelPerdidas = new Panel("P\u00E9rdidas");
        panelPerdidas.setHeight("250px");
        panelPerdidas.setWidth("157px");
        panelPerdidas.addStyleName("padding");
        panelLayoutPerdidas.addComponent(perdidasField);
        panelPerdidas.setContent(panelLayoutPerdidas);
        l1.addComponent(panelPerdidas);

        layout.addComponent(incidenteArchivoGrid);

        return layout;
    }

    private Component buildInfoFactores() {

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setMargin(true);
        layout.setResponsive(true);

        HorizontalLayout l1 = new HorizontalLayout();
        layout.addComponent(l1);
        l1.setSpacing(true);

        factoresPersonalesField = new TextArea("Factores Personales");
        factoresPersonalesField.addStyleName(ValoTheme.TEXTAREA_TINY);
        factoresPersonalesField.setNullRepresentation("");
        factoresPersonalesField.setColumns(50);
        factoresPersonalesField.setRows(2);
        factoresPersonalesField.setMaxLength(150);
        l1.addComponent(factoresPersonalesField);

        HorizontalLayout l2 = new HorizontalLayout();
        layout.addComponent(l2);
        l2.setSpacing(true);

        factoresTrabajoField = new TextArea("Factores del Trabajo");
        factoresTrabajoField.addStyleName(ValoTheme.TEXTAREA_TINY);
        factoresTrabajoField.setNullRepresentation("");
        factoresTrabajoField.setColumns(50);
        factoresTrabajoField.setRows(2);
        factoresTrabajoField.setMaxLength(150);
        l2.addComponent(factoresTrabajoField);

        return layout;
    }

    private Component buildInfoAccidente() {

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setMargin(true);
        layout.setResponsive(true);

        //line 1
        HorizontalLayout l1 = new HorizontalLayout();
        l1.setSpacing(true);
        layout.addComponent(l1);

        accidenteTrabajoField = new CheckBox("Accidente de trabajo");
        accidenteTrabajoField.addStyleName(ValoTheme.CHECKBOX_SMALL);
        l1.addComponent(accidenteTrabajoField);

        //line 2
        HorizontalLayout l2 = new HorizontalLayout();
        l2.setSpacing(true);
        layout.addComponent(l2);

        lesionadosEmpresaField = new TextField("Empleados lesionados");
        lesionadosEmpresaField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        lesionadosEmpresaField.setNullRepresentation("");
        lesionadosEmpresaField.setWidth(5, Unit.EM);
        lesionadosEmpresaField.setMaxLength(10);
        lesionadosEmpresaField.setRequired(true);
        lesionadosEmpresaField.setRequiredError("La cantidad de lesionados necesita un valor num\u00E9rico");
        l2.addComponent(lesionadosEmpresaField);

        condLesionadosEmpresaField = new TextField("Conductores lesionados");
        condLesionadosEmpresaField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        condLesionadosEmpresaField.setNullRepresentation("");
        condLesionadosEmpresaField.setWidth(5, Unit.EM);
        condLesionadosEmpresaField.setMaxLength(10);
        condLesionadosEmpresaField.setRequired(true);
        condLesionadosEmpresaField.setRequiredError("La cantidad de conductores necesita un valor num\u00E9rico");
        l2.addComponent(condLesionadosEmpresaField);

        mortalidadEmpresaField = new TextField("Mortalidad empresa");
        mortalidadEmpresaField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        mortalidadEmpresaField.setNullRepresentation("");
        mortalidadEmpresaField.setWidth(5, Unit.EM);
        mortalidadEmpresaField.setMaxLength(10);
        mortalidadEmpresaField.setRequired(true);
        mortalidadEmpresaField.setRequiredError("La mortalidad de la empresa necesita un valor num\u00E9rico");
        l2.addComponent(mortalidadEmpresaField);

        mortalidadTercerosField = new TextField("Mortalidad terceros");
        mortalidadTercerosField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        mortalidadTercerosField.setNullRepresentation("");
        mortalidadTercerosField.setWidth(5, Unit.EM);
        mortalidadTercerosField.setMaxLength(10);
        mortalidadTercerosField.setRequired(true);
        mortalidadTercerosField.setRequiredError("La mortalidad de terceros necesita un valor num\u00E9rico");
        l2.addComponent(mortalidadTercerosField);

        indemnizacionField = new TextField("Indemnizaciones");
        indemnizacionField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        indemnizacionField.setNullRepresentation("");
        indemnizacionField.setWidth(10, Unit.EM);
        indemnizacionField.setRequired(true);
        indemnizacionField.setRequiredError("Las indemnizaciones necesitan un valor num\u00E9rico");
        l2.addComponent(indemnizacionField);

        layout.addComponent(incidenteLesionGrid);
        layout.addComponent(accidenteInvolucradoGrid);

        return layout;
    }

    private Component buildInvestigaciones() {

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setMargin(true);
        layout.setResponsive(true);

        //line 1
        /*HorizontalLayout l1 = new HorizontalLayout();
        l1.setSpacing(true);
        layout.addComponent(l1);

        investigacionRelatoField = new TextArea("Relato Involucrado/Testigo");
        investigacionRelatoField.addStyleName(ValoTheme.TEXTAREA_TINY);
        investigacionRelatoField.setNullRepresentation("");
        investigacionRelatoField.setColumns(30);
        investigacionRelatoField.setRows(3);
        investigacionRelatoField.setMaxLength(500);
        l1.addComponent(investigacionRelatoField);

        leccionAprendidaField = new TextArea("Lecci\u00F3n Aprendida");
        leccionAprendidaField.addStyleName(ValoTheme.TEXTAREA_TINY);
        leccionAprendidaField.setNullRepresentation("");
        leccionAprendidaField.setColumns(30);
        leccionAprendidaField.setRows(3);
        leccionAprendidaField.setMaxLength(500);
        l1.addComponent(leccionAprendidaField);

        //line 2
        HorizontalLayout l2 = new HorizontalLayout();
        l2.setSpacing(true);
        layout.addComponent(l2);

        socializadaField = new CheckBox("Socializada/Divulgada");
        socializadaField.addStyleName(ValoTheme.CHECKBOX_SMALL);
        socializadaField.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                if (socializadaField.getValue()) {
                    medioDivulgacionField.focus();
                    medioDivulgacionField.selectAll();
                } else {
                    medioDivulgacionField.setValue("");
                }
            }
        });
        l2.addComponent(socializadaField);

        //line 3
        HorizontalLayout l3 = new HorizontalLayout();
        l3.setSpacing(true);
        layout.addComponent(l3);

        medioDivulgacionField = new TextField("Medio de Divulgaci\u00F3n");
        medioDivulgacionField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        medioDivulgacionField.setNullRepresentation("");
        medioDivulgacionField.setWidth(50, Unit.EM);
        medioDivulgacionField.setMaxLength(150);
        l3.addComponent(medioDivulgacionField);*/
        layout.addComponent(incidenteInvestigacionGrid);
        return layout;
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);
        footer.setSpacing(true);

        Button guardar = new Button("Guardar");
        guardar.addStyleName(ValoTheme.BUTTON_PRIMARY);
        footer.addComponent(guardar);
        footer.setComponentAlignment(guardar, Alignment.BOTTOM_RIGHT);
        guardar.addClickListener(event -> {
            MainUI ui = (MainUI) UI.getCurrent();
            Authentication auth = ui.getSecurity().getAuthentication();
            Set<PerfilRecurso> permisos = ((Usuario) auth.getPrincipal()).getPerfil().getPerfilRecursos();
            boolean tienePermiso = false;
            Incidente incidenteAux = incidenteBeanFieldGroup.getItemDataSource().getBean();

            int mortEmpresa, mortTerceros, cantLesionados;
            try {
                mortEmpresa = Integer.parseInt(mortalidadEmpresaField.getValue());
            } catch (Exception e) {
                mortEmpresa = 0;
            }

            try {
                mortTerceros = Integer.parseInt(mortalidadTercerosField.getValue());
            } catch (Exception e) {
                mortTerceros = 0;
            }

            try {
                cantLesionados = Integer.parseInt(lesionadosEmpresaField.getValue());
            } catch (Exception e) {
                cantLesionados = 0;
            }

            try {
                cantLesionados += Integer.parseInt(condLesionadosEmpresaField.getValue());
            } catch (Exception e) {
                cantLesionados += 0;
            }

            Set<Perdida> perdidas = (Set<Perdida>) perdidasField.getValue();
            boolean perdidasOk = true;

            for (Perdida perdida : perdidas) {
                if (perdida.getId() == 60 && (mortEmpresa + mortTerceros < 1)) {
                    perdidasOk = false;
                    break;
                }
            }

            Indexed lesionesRegistradas = incidenteLesionGrid.getContainerDataSource();
            int cantLesionesRegistradas = lesionesRegistradas.size(), sumaLesionadosRegistrados = 0;
            for (int i = 0; i < cantLesionesRegistradas; i++) {
                Property prop = lesionesRegistradas.getItem(lesionesRegistradas.getIdByIndex(i)).getItemProperty("cantidad");
                sumaLesionadosRegistrados += (int) prop.getValue();
            }

            if (!perdidasOk) {
                StaticMembers.showNotificationError("Error", "La mortalidad de la empresa o de terceros es requerida");
                mortalidadEmpresaField.focus();
                lesionadosEmpresaField.selectAll();
            } else if (sumaLesionadosRegistrados != cantLesionados) {
                String msjAux = (sumaLesionadosRegistrados == 1) ? "1 lesi\u00F3n" : sumaLesionadosRegistrados + " lesiones";
                StaticMembers.showNotificationError("Error", "Ha registrado " + msjAux + " en lugar de " + cantLesionados);
                lesionadosEmpresaField.focus();
                lesionadosEmpresaField.selectAll();
            } else if (cantLesionados + mortEmpresa > empleadoRepository.getEmployeesCount()) {
                StaticMembers.showNotificationError("Error", "Los lesionados y fallecidos no pueden exceder el total de trabajadores");
                lesionadosEmpresaField.focus();
                lesionadosEmpresaField.selectAll();
            } else {
                for (PerfilRecurso perfilRecurso : permisos) {
                    if (perfilRecurso.getId().getRecurso().getCodigo().equals(IncidenteView.VIEW_NAME)) {

                        if ((perfilRecurso.isCrear() && incidenteAux.getId() == 0) || (perfilRecurso.isModificar() && incidenteAux.getId() != 0)) {
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
                        BeanItem<Incidente> item = incidenteBeanFieldGroup.getItemDataSource();
                        incidenteBeanFieldGroup.commit();
                        Optional<Vehiculo> vehiculo = vehiculoRepository.findOneByPlaca(item.getBean().getVehiculo().getPlaca());
                        if (vehiculo.isPresent()) {
                            item.getBean().setVehiculo(vehiculo.get());
                        } else {
                            throw new FieldGroup.CommitException(new Validator.InvalidValueException("La placa no existe en ningun vehiculo registrado"));
                        }

                        Incidente incidente = incidenteRepository.save(item.getBean());

                        if (incidenteArchivoGrid.container.getItemIds().size() > 0) {
                            archivoRepository.setIncidente(incidente, incidenteArchivoGrid.container.getItemIds());
                        }

                        //Add Infracciones
                        Map<Long, Infraccion> infraccionesAdicionados = incidenteInfraccionGrid.getInfraccionAdicionados();
                        for (Map.Entry<Long, Infraccion> infraccionMap : infraccionesAdicionados.entrySet()) {
                            Infraccion infraccion = infraccionMap.getValue();
                            infraccion.setIncidente(incidente);
                            infraccionRepository.save(infraccion);
                        }
                        Map<Long, Infraccion> infraccionesModficados = incidenteInfraccionGrid.getInfraccionModificados();
                        for (Map.Entry<Long, Infraccion> infraccionMap : infraccionesModficados.entrySet()) {
                            Infraccion infraccion = infraccionMap.getValue();
                            infraccion.setIncidente(incidente);
                            infraccionRepository.save(infraccion);
                        }
                        Map<Long, Infraccion> infraccionesEliminados = incidenteInfraccionGrid.getInfraccionEliminados();
                        for (Map.Entry<Long, Infraccion> infraccionMap : infraccionesEliminados.entrySet()) {
                            Infraccion infraccion = infraccionMap.getValue();
                            infraccionRepository.delete(infraccion);
                        }
                        incidenteInfraccionGrid.restablecerListados();

                        //Add Investigaciones
                        Map<Long, Investigacion> investigacionesAdicionados = incidenteInvestigacionGrid.getInvestigacionAdicionados();
                        for (Map.Entry<Long, Investigacion> investigacionMap : investigacionesAdicionados.entrySet()) {
                            Investigacion investigacion = investigacionMap.getValue();
                            investigacion.setIncidente(incidente);
                            investigacionRepository.save(investigacion);
                        }
                        Map<Long, Investigacion> investigacionesModficados = incidenteInvestigacionGrid.getInvestigacionModificados();
                        for (Map.Entry<Long, Investigacion> investigacionMap : investigacionesModficados.entrySet()) {
                            Investigacion investigacion = investigacionMap.getValue();
                            investigacion.setIncidente(incidente);
                            investigacionRepository.save(investigacion);
                        }
                        Map<Long, Investigacion> investigacionesEliminados = incidenteInvestigacionGrid.getInvestigacionEliminados();
                        for (Map.Entry<Long, Investigacion> investigacionMap : investigacionesEliminados.entrySet()) {
                            Investigacion investigacion = investigacionMap.getValue();
                            investigacionRepository.delete(investigacion);
                        }
                        incidenteInvestigacionGrid.restablecerListados();

                        //Add AccidenteLesiones
                        Map<Long, IncidenteLesion> IncidenteLesionAdicionados = incidenteLesionGrid.getIncidenteLesionAdicionados();
                        for (Map.Entry<Long, IncidenteLesion> IncidenteLesionMap : IncidenteLesionAdicionados.entrySet()) {
                            IncidenteLesion IncidenteLesion = IncidenteLesionMap.getValue();
                            IncidenteLesion.setIncidente(incidente);
                            incidenteLesionRepository.save(IncidenteLesion);
                        }
                        Map<Long, IncidenteLesion> IncidenteLesionModficados = incidenteLesionGrid.getIncidenteLesionModificados();
                        for (Map.Entry<Long, IncidenteLesion> IncidenteLesionMap : IncidenteLesionModficados.entrySet()) {
                            IncidenteLesion IncidenteLesion = IncidenteLesionMap.getValue();
                            IncidenteLesion.setIncidente(incidente);
                            incidenteLesionRepository.save(IncidenteLesion);
                        }
                        Map<Long, IncidenteLesion> IncidenteLesionEliminados = incidenteLesionGrid.getIncidenteLesionEliminados();
                        for (Map.Entry<Long, IncidenteLesion> IncidenteLesionMap : IncidenteLesionEliminados.entrySet()) {
                            IncidenteLesion IncidenteLesion = IncidenteLesionMap.getValue();
                            incidenteLesionRepository.delete(IncidenteLesion);
                        }
                        incidenteLesionGrid.restablecerListados();

                        //Add AccidenteInvolucrado
                        Map<Long, AccidenteInvolucrado> accidenteInvolucradoAdicionados = accidenteInvolucradoGrid.getAccidenteInvolucradoAdicionados();
                        for (Map.Entry<Long, AccidenteInvolucrado> accidenteInvolucradoMap : accidenteInvolucradoAdicionados.entrySet()) {
                            AccidenteInvolucrado accidenteInvolucrado = accidenteInvolucradoMap.getValue();
                            accidenteInvolucrado.setIncidente(incidente);
                            accidenteInvolucradoRepository.save(accidenteInvolucrado);
                        }
                        Map<Long, AccidenteInvolucrado> accidenteInvolucradoModficados = accidenteInvolucradoGrid.getAccidenteInvolucradoModificados();
                        for (Map.Entry<Long, AccidenteInvolucrado> accidenteInvolucradoMap : accidenteInvolucradoModficados.entrySet()) {
                            AccidenteInvolucrado accidenteInvolucrado = accidenteInvolucradoMap.getValue();
                            accidenteInvolucrado.setIncidente(incidente);
                            accidenteInvolucradoRepository.save(accidenteInvolucrado);
                        }
                        Map<Long, AccidenteInvolucrado> accidenteInvolucradoEliminados = accidenteInvolucradoGrid.getAccidenteInvolucradoEliminados();
                        for (Map.Entry<Long, AccidenteInvolucrado> accidenteInvolucradoMap : accidenteInvolucradoEliminados.entrySet()) {
                            AccidenteInvolucrado accidenteInvolucrado = accidenteInvolucradoMap.getValue();
                            accidenteInvolucradoRepository.delete(accidenteInvolucrado);
                        }
                        accidenteInvolucradoGrid.restablecerListados();

                        //Guardar documentos
                        Map<Long, Archivo> archivosAdicionados = incidenteArchivoGrid.getIncidenteArchivoAdicionados();
                        for (Map.Entry<Long, Archivo> archivoMap : archivosAdicionados.entrySet()) {
                            Archivo archivo = archivoMap.getValue();
                            archivo.setIncidente(incidente);
                            archivoRepository.save(archivo);
                            if (!modificar) {
                                actualizarRutaFichero(archivo);
                            }
                        }
                        Map<Long, Archivo> archivoModificados = incidenteArchivoGrid.getIncidenteArchivoModificados();
                        for (Map.Entry<Long, Archivo> archivoMap : archivoModificados.entrySet()) {
                            Archivo archivo = archivoMap.getValue();
                            archivo.setIncidente(incidente);
                            archivoRepository.save(archivo);
                            /*if (!modificar) {
                                actualizarRutaFichero(archivo);
                            }*/
                        }
                        Map<Long, Archivo> archivoEliminados = incidenteArchivoGrid.getIncidenteArchivoEliminados();
                        for (Map.Entry<Long, Archivo> archivoMap : archivoEliminados.entrySet()) {
                            Archivo archivo = archivoMap.getValue();
                            archivoRepository.delete(archivo);
                            StaticMembers.eliminarFichero(new File(StaticMembers.construirRutaDocumentoIncidente(fileDir, archivo.getUrl(), incidente.getId())));
                        }
                        incidenteArchivoGrid.restablecerListados();

                        applicationEventBus.publish(IncidenteGrid.NUEVO_INCIDENTE, this, incidente);
                        close();
                        StaticMembers.showNotificationMessage("Notificaci\u00F3n", "Incidente guardado con \u00E9xito");
                    } catch (FieldGroup.CommitException e) {
                        if ((e instanceof FieldGroup.CommitException && e.getCause() instanceof Validator.InvalidValueException || e.getCause() instanceof Validator.EmptyValueException)) {
                            Collection<Validator.InvalidValueException> values = e.getInvalidFields().values();
                            if (!values.isEmpty()) {
                                Notification.show(values.iterator().next().getMessage(), e.getCause().getMessage(), Notification.Type.TRAY_NOTIFICATION);
                            } else if (e.getCause() instanceof Validator.InvalidValueException) {
                                Notification.show(e.getCause().getMessage(), Notification.Type.TRAY_NOTIFICATION);
                            }

                        } else {
                            StaticMembers.showNotificationError("Error", e.getMessage());
                        }
                    }
                }
            }
        });
        footer.setExpandRatio(guardar, 1);
        Button cancelar = new Button("Cancelar");
        footer.addComponent(cancelar);
        footer.setComponentAlignment(cancelar, Alignment.BOTTOM_RIGHT);
        cancelar.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                incidenteArchivoGrid.restablecerListados();
                close();
            }
        });

        Responsive.makeResponsive(footer);
        return footer;
    }

    private void actualizarRutaFichero(Archivo archivo) {
        String viejaRuta = StaticMembers.construirRutaDocumentoIncidente(fileDir, archivo.getUrl(), Long.valueOf("0")),
                nuevaRuta = StaticMembers.construirRutaDocumentoIncidente(fileDir, archivo.getUrl(), archivo.getIncidente().getId());
        StaticMembers.moverFichero(viejaRuta, nuevaRuta);
    }

    private Component buildInfracciones() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(new MarginInfo(true, false, true, false));
        layout.setSpacing(true);
        HorizontalLayout l1 = new HorizontalLayout();
        layout.addComponent(l1);
        l1.setSpacing(true);

        infraccionField = new Label("Infracciones");
        infraccionField.addStyleName(ValoTheme.LABEL_BOLD);
        infraccionField.setWidth(12, Unit.EM);
        l1.addComponent(infraccionField);

        layout.addComponent(incidenteInfraccionGrid);
        return layout;
    }

    public Window show(long ID) {

        zonaContainer.removeAllItems();
        zonaContainer.addAll((Collection<? extends Zona>) zonaRepository.findAll());

        tipoEventoContainer.removeAllItems();
        tipoEventoContainer.addAll((Collection<? extends TipoEvento>) tipoEventoRepository.findAll());

        tipoJornadaContainer.removeAllItems();
        tipoJornadaContainer.addAll((Collection<? extends TipoJornada>) tipoJornadaRepository.findAll());

        partesAfectadaContainer.removeAllItems();
        partesAfectadaContainer.addAll((Collection<? extends PartesAfectada>) parteAfectadaRepository.findAll());

        causaInmediataContainer.removeAllItems();
        causaInmediataContainer.addAll((Collection<? extends CausaInmediata>) causaInmediataRepository.findAll());

        causaBasicaContainer.removeAllItems();
        causaBasicaContainer.addAll((Collection<? extends CausaBasica>) causaBasicaRepository.findAll());

        perdidaContainer.removeAllItems();
        perdidaContainer.addAll((Collection<? extends Perdida>) perdidaRepository.findAll());

        Incidente incidente = incidenteRepository.findOne(ID);
        if (incidente == null) {
            incidente = new Incidente();
            incidente.setVehiculo(new Vehiculo());
            modificar = false;
        } else {
            modificar = true;
        }

        incidenteArchivoGrid.container.removeAllItems();
        incidenteArchivoGrid.container.addAll(incidente.getArchivos());
        incidenteArchivoGrid.setIncidente(incidente);

        incidenteBeanFieldGroup.setItemDataSource(incidente);

        incidenteInfraccionGrid.container.removeAllItems();
        incidenteInfraccionGrid.container.addAll(incidente.getInfracciones());

        incidenteInvestigacionGrid.container.removeAllItems();
        incidenteInvestigacionGrid.container.addAll(incidente.getInvestigaciones());

        incidenteLesionGrid.container.removeAllItems();
        incidenteLesionGrid.container.addAll(incidente.getLesiones());

        accidenteInvolucradoGrid.container.removeAllItems();
        accidenteInvolucradoGrid.container.addAll(incidente.getAccidenteInvolucrados());

        actualizarCheckBoxLists();

        return this;
    }

    public class ConductorSuggestionConverter implements SuggestField.SuggestionConverter {

        @Override
        public SuggestFieldSuggestion toSuggestion(Object item) {
            if (item != null) {
                Conductor conductor = (Conductor) item;
                return new SuggestFieldSuggestion(String.valueOf(conductor.getId()), conductor.getPersona().getNombreCompleto(), conductor.getPersona().getNombreCompleto());
            }
            return null;
        }

        @Override
        public Object toItem(SuggestFieldSuggestion suggestion) {
            return conductorRepository.findOne(Long.valueOf(suggestion.getId()));
        }
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

    public class VehiculoSuggestionConverter implements SuggestField.SuggestionConverter {

        @Override
        public SuggestFieldSuggestion toSuggestion(Object item) {
            if (item != null) {
                Vehiculo vehiculo = (Vehiculo) item;
                return new SuggestFieldSuggestion(String.valueOf(vehiculo.getId()), vehiculo.getPlaca(), vehiculo.getPlaca());
            }
            return null;
        }

        @Override
        public Object toItem(SuggestFieldSuggestion suggestion) {
            return vehiculoRepository.findOne(Long.valueOf(suggestion.getId()));
        }
    }

}
