package com.earandap.vehiculos.ui.views.mantenimiento;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.*;
import com.earandap.vehiculos.domain.nomenclador.TipoMantenimiento;
import com.earandap.vehiculos.repository.*;
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
 * Created by Angel Luis on 10/26/2015.
 */
@SpringComponent
@UIScope
public class MantenimientoFormWindow extends Window {

    @Value("${app.upload.folder-dir}")
    private String fileDir;

    private BeanFieldGroup<AccionMantenimiento> accionMantenimientoBeanFieldGroup;

    @Autowired
    private EventBus.ApplicationEventBus applicationEventBus;

    @Autowired
    private MantenimientoDetalleRepository mantenimientoDetalleRepository;

    @Autowired
    private MantenimientoDocumentoGrid mantenimientoDocumentosGrid;

    private Label mantenimientoDocumentosField;

    //tab1
    private VerticalLayout content;

    @PropertyId("tipoMantenimiento")
    private ComboBox tipoMantenimientoField;
    private BeanItemContainer<TipoMantenimiento> tipoMantenimientoBeanItemContainer;

    @PropertyId("tecnico")
    private SuggestField tecnicoField;

    @PropertyId("vehiculo")
    private SuggestField vehiculoField;

    @PropertyId("km")
    private TextField kmField;

    @PropertyId("fecha")
    private DateField fechaField;

    //tab 2
    private Label detallesField;
    private Button nuevoDetalleButton;

    @Autowired
    private MantenimientoDetalleGrid mantenimientoDetalleGrid;

    @Autowired
    private AccionMantenimientoRepository accionMantenimientoRepository;

    @Autowired
    private MantenimientoDocumentoRepository mantenimientoDocumentoRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private TipoMantenimientoRepository tipoMantenimientoRepository;

    @Autowired
    private TerceroRepository terceroRepository;

    private boolean modificar;

    private AccionMantenimiento accionMantenimiento;

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

        tabs.addTab(buildMantenimientos(), "Mantenimientos");
        tabs.addTab(buildDetallesMantenimientos(), "Detalles de Mantenimiento");
        tabs.addTab(buildDocumentos(), "Ficheros de Evidencia");

        Component footer = buildFooter();
        content.addComponent(footer);
        content.setComponentAlignment(footer, Alignment.BOTTOM_CENTER);

        accionMantenimientoBeanFieldGroup = new BeanFieldGroup<>(AccionMantenimiento.class);
        accionMantenimientoBeanFieldGroup.bindMemberFields(this);

        this.setContent(content);
    }

    private Component buildDocumentos() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(new MarginInfo(true, false, true, false));
        layout.setSpacing(true);

        //line 1
        HorizontalLayout l1 = new HorizontalLayout();
        layout.addComponent(l1);
        l1.setSpacing(true);

        mantenimientoDocumentosField = new Label("Documentos");
        mantenimientoDocumentosField.addStyleName(ValoTheme.LABEL_BOLD);
        mantenimientoDocumentosField.setWidth(12, Unit.EM);
        l1.addComponent(mantenimientoDocumentosField);

        layout.addComponent(mantenimientoDocumentosGrid);

        return layout;
    }

    private Component buildMantenimientos() {
        FormLayout mantenimientoForm = new FormLayout();
        mantenimientoForm.setSpacing(true);
        mantenimientoForm.setResponsive(true);

        //line 1
        HorizontalLayout l1 = new HorizontalLayout();
        l1.setSpacing(true);
        mantenimientoForm.addComponent(l1);

        vehiculoField = new SuggestField();
        vehiculoField.setWidth(12, Unit.EM);
        vehiculoField.setCaption("Placa del Vehículo");
        vehiculoField.setNewItemsAllowed(false);
        vehiculoField.setSuggestionHandler(s -> {
            List<Object> result = new ArrayList<>();
            List<Vehiculo> vehiculos = vehiculoRepository.search(s);
            for (Vehiculo vehiculo : vehiculos) {
                result.add(vehiculo);
            }
            if (result.size() == 0) {
                Notification.show("No se encontraron vehículos para el criterio introducido", Notification.Type.TRAY_NOTIFICATION);
            }
            return result;
        });
        vehiculoField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        vehiculoField.setImmediate(true);
        vehiculoField.setTokenMode(false);
        vehiculoField.setSuggestionConverter(new VehiculoSuggestionConverter());
        vehiculoField.setPopupWidth(400);
        vehiculoField.setRequired(true);
        vehiculoField.setRequiredError("El Vehículo es requerido");
        l1.addComponent(vehiculoField);

        tipoMantenimientoBeanItemContainer = new BeanItemContainer<>(TipoMantenimiento.class);
        tipoMantenimientoField = new ComboBox("Tipo Mantenimiento");
        tipoMantenimientoField.addStyleName(ValoTheme.COMBOBOX_TINY);
        tipoMantenimientoField.setWidth(15, Unit.EM);
        tipoMantenimientoField.setContainerDataSource(tipoMantenimientoBeanItemContainer);
        tipoMantenimientoField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        tipoMantenimientoField.setItemCaptionPropertyId("descripcion");
        tipoMantenimientoField.setNullSelectionAllowed(false);
        tipoMantenimientoField.setScrollToSelectedItem(true);
        tipoMantenimientoField.setRequired(true);
        tipoMantenimientoField.setRequiredError("El tipo de mantenimiento es requerido");
        l1.addComponent(tipoMantenimientoField);

        //line 2
        HorizontalLayout l2 = new HorizontalLayout();
        l2.setSpacing(true);
        mantenimientoForm.addComponent(l2);

        tecnicoField = new SuggestField();
        tecnicoField.setWidth(28, Unit.EM);
        tecnicoField.setCaption("Encargado");
        tecnicoField.setNewItemsAllowed(false);
        tecnicoField.setSuggestionHandler(s -> {
            List<Object> result = new ArrayList<>();
            List<Tercero> terceros = terceroRepository.search(s);
            for (Tercero tercero : terceros) {
                result.add(tercero);
            }
            if (result.size() == 0) {
                Notification.show("No se encontraron encargados para el criterio introducido", Notification.Type.TRAY_NOTIFICATION);
            }
            return result;
        });
        tecnicoField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        tecnicoField.setImmediate(true);
        tecnicoField.setTokenMode(false);
        tecnicoField.setSuggestionConverter(new TerceroSuggestionConverter());
        tecnicoField.setPopupWidth(400);
        tecnicoField.setRequired(true);
        tecnicoField.setRequiredError("El encargado es requerido");
        l2.addComponent(tecnicoField);

        //line 2
        HorizontalLayout l3 = new HorizontalLayout();
        l3.setSpacing(true);
        mantenimientoForm.addComponent(l3);

        kmField = new TextField("Km Actual");
        kmField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        kmField.setWidth(9, Unit.EM);
        kmField.setValidationVisible(true);
        kmField.setNullRepresentation("");
        kmField.setRequiredError("La placa es requerida");
        l3.addComponent(kmField);

        fechaField = new DateField("Fecha Mantenimiento");
        fechaField.setStyleName(ValoTheme.DATEFIELD_TINY);
        fechaField.setRangeStart(fechaField.getRangeEnd());
        StaticMembers.setErrorRangoEspanhol(fechaField);
        fechaField.setWidth(12, Unit.EM);
        fechaField.setRequired(true);
        fechaField.setRequiredError("La fecha es requerida");
        l3.addComponent(fechaField);

        return mantenimientoForm;
    }

    private Component buildDetallesMantenimientos() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(new MarginInfo(true, false, true, false));
        layout.setSpacing(true);
        HorizontalLayout l1 = new HorizontalLayout();
        layout.addComponent(l1);
        l1.setSpacing(true);

        detallesField = new Label("Detalles del Mantenimiento");
        detallesField.addStyleName(ValoTheme.LABEL_BOLD);
        detallesField.setWidth(20, Unit.EM);
        l1.addComponent(detallesField);

        layout.addComponent(mantenimientoDetalleGrid);

        return layout;
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
            Set<PerfilRecurso> permisos = ((Usuario) auth.getPrincipal()).getPerfil().getPerfilRecursos();
            boolean tienePermiso = false;
            AccionMantenimiento accionMantenimientoAux = accionMantenimientoBeanFieldGroup.getItemDataSource().getBean();
            for (PerfilRecurso perfilRecurso : permisos) {
                if (perfilRecurso.getId().getRecurso().getCodigo().equals(MantenimientoView.VIEW_NAME)) {
                    if ((perfilRecurso.isCrear() && accionMantenimientoAux.getId() == 0) || (perfilRecurso.isModificar() && accionMantenimientoAux.getId() != 0)) {
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
                    accionMantenimientoBeanFieldGroup.commit();
                    accionMantenimiento = accionMantenimientoBeanFieldGroup.getItemDataSource().getBean();

                    Vehiculo veh = accionMantenimiento.getVehiculo();

                    if (accionMantenimiento.getKm() >= veh.getKmActual()) {

                        veh.setKmActual(accionMantenimiento.getKm());
                        vehiculoRepository.save(veh);
                        accionMantenimientoRepository.save(accionMantenimiento);

                        Map<Long, MantenimientoDetalle> mantenimientoDetalleAdicionados = mantenimientoDetalleGrid.getMantenimientoDetalleAdicionados();
                        for (Map.Entry<Long, MantenimientoDetalle> mantenimientoDetalleEntry : mantenimientoDetalleAdicionados.entrySet()) {
                            MantenimientoDetalle mantenimientoDetalle = mantenimientoDetalleEntry.getValue();
                            mantenimientoDetalle.setMantenimiento(accionMantenimiento);
                            mantenimientoDetalleRepository.save(mantenimientoDetalle);
                        }
                        Map<Long, MantenimientoDetalle> mantenimientoDetalleModficados = mantenimientoDetalleGrid.getMantenimientoDetalleModificados();
                        for (Map.Entry<Long, MantenimientoDetalle> mantenimientoDetalleEntry : mantenimientoDetalleModficados.entrySet()) {
                            MantenimientoDetalle mantenimientoDetalle = mantenimientoDetalleEntry.getValue();
                            mantenimientoDetalle.setMantenimiento(accionMantenimiento);
                            mantenimientoDetalleRepository.save(mantenimientoDetalle);
                        }
                        Map<Long, MantenimientoDetalle> mantenimientoDetalleEliminados = mantenimientoDetalleGrid.getMantenimientoDetalleEliminados();
                        for (Map.Entry<Long, MantenimientoDetalle> mantenimientoDetalleEntry : mantenimientoDetalleEliminados.entrySet()) {
                            MantenimientoDetalle mantenimientoDetalle = mantenimientoDetalleEntry.getValue();
                            mantenimientoDetalleRepository.delete(mantenimientoDetalle);
                        }
                        mantenimientoDetalleGrid.restablecerListados();

                        //Guardar documentos
                        Map<Long, MantenimientoDocumento> mantenimientoDocumentoAdicionados = mantenimientoDocumentosGrid.getMantenimientoDocumentoAdicionados();
                        for (Map.Entry<Long, MantenimientoDocumento> mantenimientoDocumentoMap : mantenimientoDocumentoAdicionados.entrySet()) {
                            MantenimientoDocumento mantenimientoDocumento = mantenimientoDocumentoMap.getValue();
                            mantenimientoDocumento.setMantenimiento(accionMantenimiento);
                            mantenimientoDocumentoRepository.save(mantenimientoDocumento);
                            if (!modificar) {
                                actualizarRutaFichero(mantenimientoDocumento);
                            }
                        }
                        Map<Long, MantenimientoDocumento> mantenimientoDocumentoModficados = mantenimientoDocumentosGrid.getMantenimientoDocumentoModificados();
                        for (Map.Entry<Long, MantenimientoDocumento> mantenimientoDocumentoMap : mantenimientoDocumentoModficados.entrySet()) {
                            MantenimientoDocumento mantenimientoDocumento = mantenimientoDocumentoMap.getValue();
                            mantenimientoDocumento.setMantenimiento(accionMantenimiento);
                            mantenimientoDocumentoRepository.save(mantenimientoDocumento);
                            if (!modificar) {
                                actualizarRutaFichero(mantenimientoDocumento);
                            }
                        }
                        Map<Long, MantenimientoDocumento> mantenimientoDocumentoEliminados = mantenimientoDocumentosGrid.getMantenimientoDocumentoEliminados();
                        for (Map.Entry<Long, MantenimientoDocumento> mantenimientoDocumentoMap : mantenimientoDocumentoEliminados.entrySet()) {
                            MantenimientoDocumento mantenimientoDocumento = mantenimientoDocumentoMap.getValue();
                            mantenimientoDocumentoRepository.delete(mantenimientoDocumento);
                            StaticMembers.eliminarFichero(new File(StaticMembers.construirRutaDocumentoMantenimiento(fileDir, mantenimientoDocumento.getRutaDocumento(), accionMantenimiento.getId())));
                        }
                        mantenimientoDocumentosGrid.restablecerListados();

                        applicationEventBus.publish(MantenimientoGrid.NUEVO_MANTENIMIENTO, this, accionMantenimiento);
                        StaticMembers.showNotificationMessage("Notificaci\u00F3n", "Mantenimiento guardado con \u00E9xito");
                        close();
                    } else {
                        StaticMembers.showNotificationError("Error", "El km actual debe ser mayor que el inicial");
                    }

                } catch (FieldGroup.CommitException e) {
                    if ((e instanceof FieldGroup.CommitException && e.getCause() instanceof Validator.InvalidValueException || e.getCause() instanceof Validator.EmptyValueException)) {
                        Collection<Validator.InvalidValueException> values = e.getInvalidFields().values();
                        if (!values.isEmpty()) {
                            Notification.show(values.iterator().next().getMessage(), e.getCause().getMessage(), Notification.Type.TRAY_NOTIFICATION);
                        } else if (e.getCause() instanceof Validator.InvalidValueException) {
                            Notification.show(e.getCause().getMessage(), Notification.Type.TRAY_NOTIFICATION);
                        }

                    } else {
                        StaticMembers.showNotificationError("Error", e.getCause().getMessage());
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

    private void actualizarRutaFichero(MantenimientoDocumento archivo) {
        String viejaRuta = StaticMembers.construirRutaDocumentoMantenimiento(fileDir, archivo.getRutaDocumento(), Long.valueOf("-1")),
                nuevaRuta = StaticMembers.construirRutaDocumentoMantenimiento(fileDir, archivo.getRutaDocumento(), archivo.getMantenimiento().getId());
        StaticMembers.moverFichero(viejaRuta, nuevaRuta);
    }

    public Window show(long ID) {

        tipoMantenimientoBeanItemContainer.removeAllItems();
        tipoMantenimientoBeanItemContainer.addAll(tipoMantenimientoRepository.findAll());
        mantenimientoDocumentosGrid.container.removeAllItems();
        accionMantenimiento = accionMantenimientoRepository.findOne(ID);
        if (accionMantenimiento == null) {
            accionMantenimiento = new AccionMantenimiento();
            accionMantenimiento.setRutaDocumento("");
            modificar = false;
        } else {
            modificar = true;
            mantenimientoDocumentosGrid.container.addAll(accionMantenimiento.getDocumentosMantenimiento());
        }
        accionMantenimientoBeanFieldGroup.setItemDataSource(accionMantenimiento);
        mantenimientoDocumentosGrid.idMantenimiento = ID;

        mantenimientoDetalleGrid.container.removeAllItems();
        mantenimientoDetalleGrid.container.addAll(accionMantenimiento.getDetalles());
        mantenimientoDetalleGrid.setAccionMantenimiento(accionMantenimiento);

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
