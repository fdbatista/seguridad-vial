package com.earandap.vehiculos.ui.views.tiponotificacion;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.*;
import com.earandap.vehiculos.domain.nomenclador.Cargo;
import com.earandap.vehiculos.repository.CargoRepository;
import com.earandap.vehiculos.repository.SuscriptorRepository;
import com.earandap.vehiculos.repository.TerceroRepository;
import com.earandap.vehiculos.repository.TipoNotificacionRepository;
import com.earandap.vehiculos.repository.UsuarioRepository;
import com.earandap.vehiculos.ui.MainUI;
import com.earandap.vehiculos.ui.views.contrato.ContratoView;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ClickableRenderer;
import com.vaadin.ui.themes.ValoTheme;
import de.steinwedel.messagebox.ButtonId;
import de.steinwedel.messagebox.Icon;
import de.steinwedel.messagebox.MessageBox;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.vaadin.spring.events.EventBus;
import org.vaadin.suggestfield.SuggestField;
import org.vaadin.suggestfield.client.SuggestFieldSuggestion;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by Angel Luis on 12/26/2015.
 */
@SpringComponent
@UIScope
public class TipoNotificacionFormWindows extends Window implements Upload.Receiver, Upload.FinishedListener {

    private BeanFieldGroup<TipoNotificacion> tipoNotificacionBeanFieldGroup;

    @Autowired
    private TipoNotificacionSubscriptoresGrid tipoNotificacionSubscriptoresGrid;

    @Autowired
    private TerceroRepository terceroRepository;

    @Autowired
    private SuscriptorRepository suscriptorRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EventBus.ApplicationEventBus applicationEventBus;

    @Autowired
    private CargoRepository cargoRepository;

    private SuggestField terceroField;
    private Button addSuscriptorButton;
    private Upload upload;
    private String fileDir, nombreFicheroActual, nombreFicheroAnterior, rutaFichero;
    private boolean errorCarga;
    private List<String> ficherosCargados;
    private TipoNotificacion tipoNotificacion;
    private Image imgIcono;

    private Map<Long, Suscriptor> suscriptorAdicionadas = new LinkedHashMap<>();
    private Map<Long, Suscriptor> suscriptorEliminadas = new LinkedHashMap<>();

    //tab1
    private VerticalLayout content;

    @PropertyId("codigo")
    private TextField codigoField;

    @PropertyId("descripcion")
    private TextField descripcionField;

    @PropertyId("icono")
    private TextField rutaDocumentoField;

    @Autowired
    private TipoNotificacionRepository tipoNotificacionRepository;

    private boolean modificar;

    //tab 2
    
    @PropertyId("admin")
    private CheckBox adminField;
    
    @PropertyId("cargos")
    private OptionGroup cargosField;
    private BeanItemContainer<Cargo> cargosContainer;

    @PostConstruct
    public void init() {
        setWidth(400, Unit.PIXELS);
        setHeight(500, Unit.PIXELS);
        setModal(true);
        setCloseShortcut(ShortcutAction.KeyCode.ESCAPE, null);
        setResizable(false);
        setClosable(false);
        rutaFichero = "";
        fileDir = "";

        Responsive.makeResponsive(this);

        content = new VerticalLayout();
        content.setSizeFull();
        content.setMargin(true);

        TabSheet tabs = new TabSheet();
        content.addComponent(tabs);
        content.setExpandRatio(tabs, 1);

        tabs.addTab(buildTipoNotificacion(), "Tipo de Notificaci\u00F3n");
        tabs.addTab(buildSuscriptores(), "Suscriptores");

        Component footer = buildFooter();
        content.addComponent(footer);
        content.setComponentAlignment(footer, Alignment.BOTTOM_CENTER);

        tipoNotificacionBeanFieldGroup = new BeanFieldGroup<>(TipoNotificacion.class);
        tipoNotificacionBeanFieldGroup.bindMemberFields(this);

        this.tipoNotificacionSubscriptoresGrid.setDeleteListener(new ClickableRenderer.RendererClickListener() {
            @Override
            public void click(ClickableRenderer.RendererClickEvent event) {
                MessageBox messageBox = MessageBox.showPlain(Icon.QUESTION,
                        "Interrogante",
                        "Est\u00E1 seguro que quiere eliminar?",
                        ButtonId.YES,
                        ButtonId.NO);
                messageBox.getButton(ButtonId.YES).addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent e) {
                        Suscriptor suscriptor = tipoNotificacionSubscriptoresGrid.container.getItem(event.getItemId()).getBean();
                        if (suscriptorAdicionadas.containsKey(suscriptor.getId().getTercero().getId())) {
                            suscriptorAdicionadas.remove(suscriptor.getId().getTercero().getId());
                        }
                        if (suscriptor.getId().getNotificacion().getId() != 0) {
                            suscriptorEliminadas.put(suscriptor.getId().getTercero().getId(), suscriptor);
                        }
                        tipoNotificacionSubscriptoresGrid.container.removeItem(suscriptor.getId().getTercero().getId());

                        tipoNotificacionSubscriptoresGrid.container.removeItem(suscriptor.getId());
                        StaticMembers.showNotificationMessage("Notificaci\u00F3n", "El suscriptor se eliminar\u00E1 luego de guardar los cambios");
                    }
                });
            }
        });

        this.setContent(content);
    }

    public TipoNotificacion getTipoNotificacion() {
        return tipoNotificacion;
    }

    public void setTipoNotificacion(TipoNotificacion tipoNotificacion) {
        this.tipoNotificacion = tipoNotificacion;
    }

    public String getFileDir() {
        return fileDir;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }

    public String getNombreFicheroActual() {
        return nombreFicheroActual;
    }

    public void setNombreFicheroActual(String nombreFicheroActual) {
        this.nombreFicheroActual = nombreFicheroActual;
    }

    public boolean isErrorCarga() {
        return errorCarga;
    }

    public void setErrorCarga(boolean errorCarga) {
        this.errorCarga = errorCarga;
    }

    public List<String> getFicherosCargados() {
        return ficherosCargados;
    }

    public void setFicherosCargados(List<String> ficherosCargados) {
        this.ficherosCargados = ficherosCargados;
    }

    public Map<Long, Suscriptor> getSuscriptorAdicionadas() {
        return suscriptorAdicionadas;
    }

    public void setSuscriptorAdicionadas(Map<Long, Suscriptor> suscriptorAdicionadas) {
        this.suscriptorAdicionadas = suscriptorAdicionadas;
    }

    public Map<Long, Suscriptor> getSuscriptorEliminadas() {
        return suscriptorEliminadas;
    }

    public void setSuscriptorEliminadas(Map<Long, Suscriptor> suscriptorEliminadas) {
        this.suscriptorEliminadas = suscriptorEliminadas;
    }

    public boolean isModificar() {
        return this.modificar;
    }

    public void setModificar(boolean modificar) {
        this.modificar = modificar;
    }

    private Component buildTipoNotificacion() {
        FormLayout form = new FormLayout();
        form.setSpacing(true);
        form.setResponsive(true);

        //line 1
        HorizontalLayout l1 = new HorizontalLayout();
        l1.setSpacing(true);
        form.addComponent(l1);

        codigoField = new TextField("C\u00F3digo");
        codigoField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        codigoField.setWidth(24, Unit.EM);
        codigoField.setNullRepresentation("");
        l1.addComponent(codigoField);

        //line 2
        HorizontalLayout l2 = new HorizontalLayout();
        l2.setSpacing(true);
        form.addComponent(l2);

        descripcionField = new TextField("Descripci\u00F3n");
        descripcionField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        descripcionField.setWidth(24, Unit.EM);
        descripcionField.setNullRepresentation("");
        l2.addComponent(descripcionField);

        //line 3
        HorizontalLayout l3 = new HorizontalLayout();
        l3.setSpacing(true);
        form.addComponent(l3);

        rutaDocumentoField = new TextField("Icono");
        rutaDocumentoField.setNullRepresentation("");
        rutaDocumentoField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        rutaDocumentoField.setWidth(24, Unit.EM);
        l3.addComponent(rutaDocumentoField);

        //line 4
        HorizontalLayout l4 = new HorizontalLayout();
        l4.setSpacing(true);
        form.addComponent(l4);

        upload = new Upload("", this);
        upload.addFinishedListener(this);
        upload.setImmediate(true);
        upload.setButtonCaption("Seleccionar Icono");
        upload.addStyleName(ValoTheme.BUTTON_PRIMARY);
        upload.addChangeListener(new Upload.ChangeListener() {
            @Override
            public void filenameChanged(Upload.ChangeEvent event) {
                String nombre = StaticMembers.getNombreFichero(event.getFilename());
                setValorRutaFicheroField(nombre);
                if (!ficherosCargados.contains(nombre)) {
                    ficherosCargados.add(nombre);
                }
            }
        });
        l4.addComponent(upload);

        //line 5
        HorizontalLayout l5 = new HorizontalLayout();
        l5.setSpacing(true);
        form.addComponent(l5);

        imgIcono = new Image("");
        //imgIcono.addStyleName("img-icono");
        imgIcono.setWidth("32px");
        imgIcono.setHeight("32px");
        l5.addComponent(imgIcono);

        return form;
    }

    private void setValorRutaFicheroField(String valor) {
        this.nombreFicheroActual = valor;
        rutaDocumentoField.setReadOnly(false);
        rutaDocumentoField.setValue(this.nombreFicheroActual);
        rutaDocumentoField.setReadOnly(true);
    }

    private Component buildSuscriptores() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(new MarginInfo(true, false, true, false));
        layout.setSpacing(true);
        
        //line 1
        
        HorizontalLayout l1 = new HorizontalLayout();
        layout.addComponent(l1);
        l1.setSpacing(true);
        
        adminField = new CheckBox("Administradores del Sistema");
        adminField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        adminField.addStyleName("fixed-checkbox");
        adminField.setWidth(15, Unit.EM);
        l1.addComponent(adminField);
        
        //line 2
        
        HorizontalLayout l2 = new HorizontalLayout();
        layout.addComponent(l2);
        l2.setSpacing(true);

        cargosContainer = new BeanItemContainer<>(Cargo.class);
        cargosField = new OptionGroup();
        cargosField.setMultiSelect(true);
        Panel panelCargos = new Panel("Cargos");
        panelCargos.setSizeFull();
        panelCargos.setWidth("250px");
        panelCargos.addStyleName("padding");
        VerticalLayout panelLayoutCargos = new VerticalLayout();
        panelLayoutCargos.addComponent(cargosField);
        panelCargos.setContent(panelLayoutCargos);
        l2.addComponent(panelCargos);

        /*terceroField = new SuggestField();
        terceroField.setWidth(30, Unit.EM);
        terceroField.setCaption("Tercero");
        terceroField.setNewItemsAllowed(false);
        terceroField.setSuggestionHandler(s -> {
            List<Object> result = new ArrayList<>();
            List<Tercero> terceros = terceroRepository.search(s);
            for (Tercero tercero : terceros) {
                result.add(tercero);
            }
            if (result.size() == 0) {
                Notification.show("No se encontraron terceros para el criterio introducido", Notification.Type.TRAY_NOTIFICATION);
            }

            return result;
        });
        terceroField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        terceroField.setImmediate(true);
        terceroField.setTokenMode(false);
        terceroField.setSuggestionConverter(new TerceroSuggestionConverter());
        terceroField.setWidth("200px");
        terceroField.setPopupWidth(400);
        l1.addComponent(terceroField);

        addSuscriptorButton = new Button();
        addSuscriptorButton.setIcon(FontAwesome.PLUS);
        addSuscriptorButton.addStyleName(ValoTheme.BUTTON_SMALL);
        addSuscriptorButton.addStyleName("button-search-form");
        l1.addComponent(addSuscriptorButton);
        addSuscriptorButton.addClickListener(event -> {
            Tercero tercero = (Tercero) terceroField.getValue();
            if (tercero != null) {
                if (!existe(tipoNotificacionSubscriptoresGrid.container.getItemIds(), tercero)) {
                    Suscriptor suscriptor = new Suscriptor();
                    suscriptor.setTercero(tercero);
                    suscriptor.setNotificacion(new TipoNotificacion());
                    tipoNotificacionSubscriptoresGrid.container.addBean(suscriptor);
                    suscriptorAdicionadas.put(tercero.getId(), suscriptor);
                } else {
                    StaticMembers.showNotificationWarning("Notificaci\u00F3n", "El tercero ya es suscriptor");
                }
            } else {
                StaticMembers.showNotificationWarning("Notificaci\u00F3n", "Debe seleccionar un tercero");
            }
            terceroField.setValue(null);
        });

        layout.addComponent(tipoNotificacionSubscriptoresGrid);*/
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
            tipoNotificacion = tipoNotificacionBeanFieldGroup.getItemDataSource().getBean();
            for (PerfilRecurso perfilRecurso : permisos) {
                if (perfilRecurso.getId().getRecurso().getCodigo().equals(ContratoView.VIEW_NAME)) {
                    if ((perfilRecurso.isCrear() && tipoNotificacion.getId() == 0) || (perfilRecurso.isModificar() && tipoNotificacion.getId() != 0)) {
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
                    tipoNotificacionBeanFieldGroup.commit();
                    tipoNotificacion = tipoNotificacionBeanFieldGroup.getItemDataSource().getBean();
                    Set<Cargo> cargosSeleccionados = (Set<Cargo>) cargosField.getValue();
                    if (!cargosSeleccionados.isEmpty() || adminField.getValue()) {
                        if (!modificar) {
                            if (tipoNotificacionRepository.findOneByCodigo(tipoNotificacion.getCodigo()).isPresent() || (tipoNotificacionRepository.findOneByDescripcion(tipoNotificacion.getDescripcion()).isPresent())) {
                                throw new FieldGroup.CommitException(new Validator.InvalidValueException("Ya existe tipo de notificaci\u00F3n con ese c\u00F3digo o descripci\u00F3n"));
                            }
                        } else {
                            List<TipoNotificacion> tipoNotificaciones = tipoNotificacionRepository.findAllByIdNot(tipoNotificacion.getId());
                            if (this.contieneTipoNotificacion(tipoNotificaciones, tipoNotificacion.getCodigo(), tipoNotificacion.getDescripcion())) {
                                throw new FieldGroup.CommitException(new Validator.InvalidValueException("Ya existe tipo de notificaci\u00F3 con ese código o descripción"));
                            }
                        }
                        tipoNotificacionRepository.save(tipoNotificacion);
                        
                        applicationEventBus.publish(TipoNotificacionGrid.NUEVO_TIPONOTIFICACION, this, tipoNotificacion);
                        eliminarFicherosCargados();
                        StaticMembers.showNotificationMessage("Notificaci\u00F3n", "Tipo de notificaci\u00F3n guardado con \u00E9xito");
                        close();
                    } else {
                        StaticMembers.showNotificationError("Error", "Debe seleccionar al menos un cargo");
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
                        StaticMembers.showNotificationError("Ha ocurrido un error interno del sistema", e.getCause().getMessage());
                    }
                }
            }
            this.limpiarMap();
        });

        footer.setExpandRatio(guardar, 1);
        Button cancelar = new Button("Cancelar");
        cancelar.setStyleName(ValoTheme.BUTTON_PRIMARY);
        footer.addComponent(cancelar);
        footer.setComponentAlignment(cancelar, Alignment.BOTTOM_RIGHT);
        cancelar.addClickListener(event -> {
            this.limpiarMap();
            nombreFicheroActual = nombreFicheroAnterior;
            eliminarFicherosCargados();
            close();
        });
        Responsive.makeResponsive(footer);
        return footer;
    }

    private void eliminarFicherosCargados() {
        if (!nombreFicheroAnterior.equals(nombreFicheroActual)) {
            StaticMembers.eliminarFichero(StaticMembers.construirRutaIconoNotificacion(fileDir, nombreFicheroAnterior));
        }
        for (String nombreFicheroSubido : getFicherosCargados()) {
            if (!nombreFicheroActual.equals(nombreFicheroSubido)) {
                StaticMembers.eliminarFichero(StaticMembers.construirRutaIconoNotificacion(fileDir, nombreFicheroSubido));
            }
        }
    }

    private boolean contieneTipoNotificacion(List<TipoNotificacion> tipoNotificaciones, String codigo, String descripcion) {
        for (TipoNotificacion tipoNotificacion : tipoNotificaciones) {
            if (tipoNotificacion.getCodigo().equals(codigo) || tipoNotificacion.getDescripcion().equals(descripcion)) {
                return true;
            }
        }
        return false;
    }

    private boolean existe(List<Suscriptor.Id> itemIds, Tercero tercero) {
        for (Suscriptor.Id id : itemIds) {
            if (id.getTercero().getId() == tercero.getId()) {
                return true;
            }
        }
        return false;
    }

    public void limpiarMap() {
        suscriptorAdicionadas.clear();
        suscriptorEliminadas.clear();
    }

    public Window show(long ID, String fileDir) {
        inicializarAtributos(fileDir);
        this.tipoNotificacion = tipoNotificacionRepository.findOne(ID);
        if (tipoNotificacion == null) {
            this.tipoNotificacion = new TipoNotificacion();
            this.modificar = false;
            this.imgIcono.setIcon(FontAwesome.IMAGE);
            this.nombreFicheroAnterior = "";
        } else {
            this.modificar = true;
            setValorRutaFicheroField(tipoNotificacion.getIcono());
            this.rutaFichero = StaticMembers.construirRutaIconoNotificacion(this.fileDir, this.nombreFicheroActual);
            nombreFicheroAnterior = (nombreFicheroActual == null) ? "" : nombreFicheroActual;
            actualizarVistaPreviaIcono();
        }
        tipoNotificacionBeanFieldGroup.setItemDataSource(tipoNotificacion);
        /*tipoNotificacionSubscriptoresGrid.container.removeAllItems();
        tipoNotificacionSubscriptoresGrid.container.addAll(tipoNotificacion.getSuscriptores());*/

        cargosContainer.removeAllItems();
        cargosContainer.addAll((Collection<? extends Cargo>) cargoRepository.findAll());
        actualizarCheckBoxList();

        rutaDocumentoField.setReadOnly(true);

        return this;
    }

    private void actualizarCheckBoxList() {
        TipoNotificacion entidad = tipoNotificacionBeanFieldGroup.getItemDataSource().getBean();
        cargosField.removeAllItems();
        List<Cargo> cargosGral = cargosContainer.getItemIds();
        Set<Cargo> cargosEntidad = entidad.getCargos();
        for (Cargo obj : cargosGral) {
            cargosField.addItem(obj);
            if (cargosEntidad.contains(obj)) {
                cargosField.select(obj);
            }
        }
    }

    private void inicializarAtributos(String fileDir) {
        this.fileDir = fileDir;
        this.errorCarga = true;
        this.ficherosCargados = new ArrayList<>();
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

    private void actualizarVistaPreviaIcono() {
        File fichero = new File(rutaFichero);
        imgIcono.setIcon((fichero.exists() ? new FileResource(fichero) : FontAwesome.PICTURE_O));
    }

    @Override
    public void uploadFinished(Upload.FinishedEvent event) {
        if (!errorCarga) {
            actualizarVistaPreviaIcono();
            StaticMembers.showNotificationMessage("", "El fichero ha sido cargado correctamente");
        }
    }

    @Override
    public OutputStream receiveUpload(String filename, String mimeType) {
        FileOutputStream buffer = null;
        try {
            rutaFichero = StaticMembers.construirRutaIconoNotificacion(this.fileDir, filename);
            buffer = new FileOutputStream(new File(rutaFichero));
            errorCarga = false;
        } catch (final FileNotFoundException e) {
            errorCarga = true;
            StaticMembers.showNotificationError("Error", "La ruta del documento es incorrecta");
        } catch (final Exception e) {
            errorCarga = true;
            StaticMembers.showNotificationError("Error", "No se puede abrir el archivo");
        }
        return buffer;
    }

}
