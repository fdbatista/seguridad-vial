package com.earandap.vehiculos.ui.views.mantenimiento;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.AccionMantenimiento;
import com.earandap.vehiculos.domain.MantenimientoDetalle;
import com.earandap.vehiculos.domain.nomenclador.Actividad;
import com.earandap.vehiculos.domain.nomenclador.Resultado;
import com.earandap.vehiculos.repository.ActividadRepository;
import com.earandap.vehiculos.repository.MantenimientoActividadRepository;
import com.earandap.vehiculos.repository.ResultadoRepository;
import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.*;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ClickableRenderer;
import com.vaadin.ui.themes.ValoTheme;
import de.steinwedel.messagebox.ButtonId;
import de.steinwedel.messagebox.Icon;
import de.steinwedel.messagebox.MessageBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.gridutil.renderer.EditDeleteButtonValueRenderer;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by Angel Luis on 10/27/2015.
 */
@SpringComponent
@UIScope
public class MantenimientoDetalleGrid extends Grid {

    protected Container container;

    private MessageBox mb;

    int id;

    private AccionMantenimiento accionMantenimiento;

    private Map<Long, MantenimientoDetalle> mantenimientoDetalleModificados;

    private Map<Long, MantenimientoDetalle> mantenimientoDetalleAdicionados;

    private Map<Long, MantenimientoDetalle> mantenimientoDetalleEliminados;

    private Map<Long, Actividad> actividadesUsadas = new HashMap<>();

    private ComboBox actividadField;
    private BeanItemContainer<Actividad> actividadBeanItemContainer;

    private ComboBox resultadoField;
    private BeanItemContainer<Resultado> resultadoBeanItemContainer;

    @Autowired
    private ActividadRepository actividadRepository;

    @Autowired
    private ResultadoRepository resultadoRepository;

    @Autowired
    private MantenimientoActividadRepository mantenimientoActividadRepository;

    public AccionMantenimiento getAccionMantenimiento() {
        return accionMantenimiento;
    }

    public void setAccionMantenimiento(AccionMantenimiento accionMantenimiento) {
        this.accionMantenimiento = accionMantenimiento;
    }

    public Map<Long, MantenimientoDetalle> getMantenimientoDetalleModificados() {
        return mantenimientoDetalleModificados;
    }

    public void setMantenimientoDetalleModificados(Map<Long, MantenimientoDetalle> mantenimientoDetalleModificados) {
        this.mantenimientoDetalleModificados = mantenimientoDetalleModificados;
    }

    public Map<Long, MantenimientoDetalle> getMantenimientoDetalleAdicionados() {
        return mantenimientoDetalleAdicionados;
    }

    public void setMantenimientoDetalleAdicionados(Map<Long, MantenimientoDetalle> mantenimientoDetalleAdicionados) {
        this.mantenimientoDetalleAdicionados = mantenimientoDetalleAdicionados;
    }

    public Map<Long, MantenimientoDetalle> getMantenimientoDetalleEliminados() {
        return mantenimientoDetalleEliminados;
    }

    public void setMantenimientoDetalleEliminados(Map<Long, MantenimientoDetalle> mantenimientoDetalleEliminados) {
        this.mantenimientoDetalleEliminados = mantenimientoDetalleEliminados;
    }

    @PostConstruct
    public void init() {
        this.container = new Container();
        Responsive.makeResponsive(this);
        this.setSizeFull();
        this.setHeight("280px");
        this.setSelectionMode(Grid.SelectionMode.SINGLE);
        this.setEditorEnabled(true);

        actividadField = new ComboBox();
        actividadField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        actividadField.setItemCaptionPropertyId("descripcion");
        actividadField.setNullSelectionAllowed(false);
        actividadField.setScrollToSelectedItem(true);
        actividadBeanItemContainer = new BeanItemContainer<Actividad>(Actividad.class);
        actividadBeanItemContainer.addAll(actividadRepository.findAll());
        actividadField.setContainerDataSource(actividadBeanItemContainer);

        resultadoField = new ComboBox();
        resultadoField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        resultadoField.setItemCaptionPropertyId("descripcion");
        resultadoField.setNullSelectionAllowed(false);
        resultadoField.setScrollToSelectedItem(true);
        resultadoBeanItemContainer = new BeanItemContainer<Resultado>(Resultado.class);
        resultadoBeanItemContainer.addAll(resultadoRepository.findAll());
        resultadoField.setContainerDataSource(resultadoBeanItemContainer);

        id = -1;

        mantenimientoDetalleAdicionados = new HashMap<Long, MantenimientoDetalle>();
        mantenimientoDetalleModificados = new HashMap<Long, MantenimientoDetalle>();
        mantenimientoDetalleEliminados = new HashMap<Long, MantenimientoDetalle>();

        GeneratedPropertyContainer gpc = new GeneratedPropertyContainer(container);
        gpc.addGeneratedProperty("actions", new PropertyValueGenerator<String>() {
            @Override
            public String getValue(Item item, Object itemId, Object propertyId) {
                return ".";
            }

            @Override
            public Class<String> getType() {
                return String.class;
            }
        });

        this.setContainerDataSource(gpc);

        this.removeAllColumns();
        this.addColumn("actividad").setHeaderCaption("Actividad").setEditorField(actividadField);
        this.addColumn("resultado").setHeaderCaption("Resultado").setEditorField(resultadoField);
        this.addColumn("actions").setHeaderCaption("Acciones");
        this.getColumn("actividad").setConverter(new Converter<String, Actividad>() {
            @Override
            public Actividad convertToModel(String value, Class<? extends Actividad> targetType, Locale locale) throws ConversionException {
                return null;
            }

            @Override
            public String convertToPresentation(Actividad value, Class<? extends String> targetType, Locale locale) throws ConversionException {
                return value.getDescripcion();
            }

            @Override
            public Class<Actividad> getModelType() {
                return Actividad.class;
            }

            @Override
            public Class<String> getPresentationType() {
                return String.class;
            }
        });
        this.getColumn("resultado").setConverter(new Converter<String, Resultado>() {
            @Override
            public Resultado convertToModel(String value, Class<? extends Resultado> targetType, Locale locale) throws ConversionException {
                return null;
            }

            @Override
            public String convertToPresentation(Resultado value, Class<? extends String> targetType, Locale locale) throws ConversionException {
                return value.getDescripcion();
            }

            @Override
            public Class<Resultado> getModelType() {
                return Resultado.class;
            }

            @Override
            public Class<String> getPresentationType() {
                return String.class;
            }
        });
        this.setEditDeleteListener(new EditDeleteButtonValueRenderer.EditDeleteButtonClickListener() {
            @Override
            public void onEdit(ClickableRenderer.RendererClickEvent rendererClickEvent) {
                MantenimientoDetalle mantenimientoDetalle = container.getItem(rendererClickEvent.getItemId()).getBean();
                adicionaModificarBoton(mantenimientoDetalle, false);
            }

            @Override
            public void onDelete(ClickableRenderer.RendererClickEvent rendererClickEvent) {
                MessageBox messageBox = MessageBox.showPlain(
                        Icon.QUESTION,
                        "Interrogante",
                        "Est\u00E1 seguro que quiere eliminar?",
                        ButtonId.YES,
                        ButtonId.NO);
                messageBox.getButton(ButtonId.YES).addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent e) {
                        MantenimientoDetalle mantenimientoDetalle = container.getItem(rendererClickEvent.getItemId()).getBean();
                        if (mantenimientoDetalleAdicionados.containsKey(mantenimientoDetalle.getId())) {
                            mantenimientoDetalleAdicionados.remove(mantenimientoDetalle.getId());
                        }
                        if (mantenimientoDetalleModificados.containsKey(mantenimientoDetalle.getId())) {
                            mantenimientoDetalleModificados.remove(mantenimientoDetalle.getId());
                        }
                        mantenimientoDetalleEliminados.put(mantenimientoDetalle.getId(), mantenimientoDetalle);
                        container.removeItem(mantenimientoDetalle.getId());
                        Notification notification = new Notification("Notificaci\u00F3n", "El Mantenimiento-Detalle se eliminara luego de guardar los cambios", Notification.Type.TRAY_NOTIFICATION);
                        notification.setDelayMsec(3000);
                        notification.setIcon(FontAwesome.INFO_CIRCLE);
                        notification.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
                        notification.show(Page.getCurrent());
                    }
                });
            }
        });
        initExtraHeaderRow();
        this.getEditorFieldGroup().addCommitHandler(new FieldGroup.CommitHandler() {
            @Override
            public void preCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
                System.out.println("--------------Pre-Commit------------");
            }

            @Override
            public void postCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
                System.out.println("--------------Post-Commit------------");
                actividadesUsadas.clear();
                List<Long> ids = container.getItemIds();
                for (Long id : ids) {
                    Actividad actividad = container.getItem(id).getBean().getActividad();
                    actividadesUsadas.put(actividad.getId(), actividad);
                }

                actividadBeanItemContainer.addAll(actividadRepository.findByIdNotIn(ids));

                Item item = commitEvent.getFieldBinder().getItemDataSource();
                Long id = (Long) item.getItemProperty("id").getValue();

                MantenimientoDetalle mantenimientoDetalle = container.getItem(id).getBean();
                mantenimientoDetalleAdicionados.remove(mantenimientoDetalle.getId());
                mantenimientoDetalleModificados.put(mantenimientoDetalle.getId(), mantenimientoDetalle);
            }
        });
        this.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                Long idActual = (Long) event.getItemId();
                actividadesUsadas.clear();
                List<Long> ids = container.getItemIds();
                for (Long id : ids) {
                    if (!id.equals(idActual)) {
                        Actividad actividad = container.getItem(id).getBean().getActividad();
                        actividadesUsadas.put(actividad.getId(), actividad);
                    }
                }

                actividadBeanItemContainer.removeAllItems();
                if (actividadesUsadas.keySet().isEmpty()) {
                    actividadBeanItemContainer.addAll(actividadRepository.findAll());
                } else {
                    actividadBeanItemContainer.addAll(actividadRepository.findByIdNotIn(actividadesUsadas.keySet()));
                }
            }
        });
    }

    private void initExtraHeaderRow() {
        HeaderRow firstHeaderRow = this.prependHeaderRow();
        firstHeaderRow.join("actividad", "resultado");
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSizeFull();
        buttonLayout.setSpacing(true);
        buttonLayout.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
        firstHeaderRow.getCell("actions").setComponent(buttonLayout);
        Button cargarActividades = new Button("", (Button.ClickListener) event -> {
            actividadesUsadas.clear();
            List<Long> ids = container.getItemIds();
            for (Long id : ids) {
                Actividad actividad = container.getItem(id).getBean().getActividad();
                actividadesUsadas.put(actividad.getId(), actividad);
            }
            List<Actividad> lista = new ArrayList<>();
            boolean listaVacia = false;
            if (!actividadesUsadas.keySet().isEmpty()) {
                lista = mantenimientoActividadRepository.buscarActividadesInMantenimientoActividad(accionMantenimiento.getVehiculo().getClaseVehiculo(), accionMantenimiento.getTipoMantenimiento(), actividadesUsadas.keySet());
                if (lista.isEmpty()) {
                    listaVacia = true;
                }
            } else {
                if (accionMantenimiento.getVehiculo() != null && accionMantenimiento.getTipoMantenimiento() != null) {
                    lista = mantenimientoActividadRepository.buscarActividadesInMantenimientoActividad(accionMantenimiento.getVehiculo().getClaseVehiculo(), accionMantenimiento.getTipoMantenimiento()); //Todo Falta qe esté en Actividades Mantenimiento(Cambiar el método).
                    if (lista.isEmpty()) {
                        listaVacia = true;
                    }
                } else {
                    Notification notification = new Notification("El tipo de Mantenimiento y Veh\u00EDculo son requeridos", Notification.Type.TRAY_NOTIFICATION);
                    notification.setDelayMsec(3000);
                    notification.setIcon(FontAwesome.INFO_CIRCLE);
                    notification.setStyleName(ValoTheme.NOTIFICATION_WARNING);
                    notification.show(Page.getCurrent());
                }
            }

            if (listaVacia) {
                Notification notification = new Notification("Existen toda las actividades seg\u00FAn clase de Veh\u00EDculo y tipo de mantenimiento", Notification.Type.TRAY_NOTIFICATION);
                notification.setDelayMsec(3000);
                notification.setIcon(FontAwesome.INFO_CIRCLE);
                notification.setStyleName(ValoTheme.NOTIFICATION_WARNING);
                notification.show(Page.getCurrent());
            }

            for (Actividad actividad : lista) {
                MantenimientoDetalle mantenimientoDetalleTemp = new MantenimientoDetalle();
                mantenimientoDetalleTemp.setId(id--);
                mantenimientoDetalleTemp.setMantenimiento(accionMantenimiento);
                mantenimientoDetalleTemp.setActividad(actividad);
                Resultado mejorResultado = resultadoRepository.findAll().get(1);//Todo Definir el mejor Resultado.
                mantenimientoDetalleTemp.setResultado(mejorResultado);
                container.addBean(mantenimientoDetalleTemp);
                mantenimientoDetalleAdicionados.put(mantenimientoDetalleTemp.getId(), mantenimientoDetalleTemp);
            }
        });

        cargarActividades.setCaption("Cargar Actividades");
        cargarActividades.setIcon(FontAwesome.UPLOAD);
        cargarActividades.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        cargarActividades.addStyleName(ValoTheme.BUTTON_TINY);
        buttonLayout.addComponent(cargarActividades);

        Button adicionar = new Button("", (Button.ClickListener) event -> {
            this.adicionaModificarBoton(new MantenimientoDetalle(), true);
        }
        );
        adicionar.setCaption("Adicionar detalles");
        adicionar.setIcon(FontAwesome.PLUS_SQUARE);
        adicionar.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        adicionar.addStyleName(ValoTheme.BUTTON_TINY);
        buttonLayout.addComponent(adicionar);

    }

    public void setEditDeleteListener(EditDeleteButtonValueRenderer.EditDeleteButtonClickListener listener) {
        if (listener != null) {
            this.getColumn("actions").setRenderer(new EditDeleteButtonValueRenderer(listener)).setWidth(100);
        }
    }

    private void adicionaModificarBoton(MantenimientoDetalle mantenimientoDetalle, Boolean adicionar) {
        actividadesUsadas.clear();
        List<Long> ids = container.getItemIds();
        for (Long id : ids) {
            Actividad actividad = container.getItem(id).getBean().getActividad();
            actividadesUsadas.put(actividad.getId(), actividad);
        }
        if (!adicionar) {
            actividadesUsadas.remove(mantenimientoDetalle.getActividad().getId());
        }
        Collection<Long> idactividadespermitidas = actividadesUsadas.keySet();
        List<Actividad> lista = actividadRepository.findAll();
        if (!actividadesUsadas.isEmpty()) {
            lista = actividadRepository.findByIdNotIn(actividadesUsadas.keySet());
        }

        MantenimientoDetalleForm form = new MantenimientoDetalleForm(mantenimientoDetalle, lista, resultadoRepository.findAll());
        mb = MessageBox.showCustomized(Icon.NONE, "Adicionar Detalles de Mantenimiento", form,
                buttonId -> {
                    if (buttonId == ButtonId.YES) {
                        try {
                            form.getMantenimientoDetalleBeanFieldGroup().commit();
                            BeanItem<MantenimientoDetalle> item = form.getMantenimientoDetalleBeanFieldGroup().getItemDataSource();

                            if (adicionar) {
                                if (!this.existeMantenimienteEnGrid(item.getBean())) {
                                    item.getBean().setId(id--);
                                    mantenimientoDetalleAdicionados.put(item.getBean().getId(), item.getBean());
                                    container.addBean(item.getBean());
                                } else {
                                    throw new FieldGroup.CommitException("Ya existe ese mantenimiento");
                                }
                            } else {
                                mantenimientoDetalleAdicionados.remove(item.getBean().getId());
                                mantenimientoDetalleModificados.put(item.getBean().getId(), item.getBean());
                                container.removeItem(item.getBean().getId());
                                container.addBean(item.getBean());
                            }
                            mb.close();

                        } catch (FieldGroup.CommitException e) {
                            Collection<Validator.InvalidValueException> values = e.getInvalidFields().values();
                            if (!values.isEmpty()) {
                                Notification.show(values.iterator().next().getMessage(), e.getCause().getMessage(), Notification.Type.TRAY_NOTIFICATION);
                            } else if (e.getCause() instanceof Validator.InvalidValueException) {
                                Notification.show(e.getCause().getMessage(), Notification.Type.TRAY_NOTIFICATION);
                            } else {
                                StaticMembers.showNotificationError("Error", e.getMessage());
                            }
                        }
                    } else {
                        mb.close();
                    }
                }, ButtonId.CANCEL, ButtonId.YES).setWidth("350px").setHeight("250px");
        mb.setModal(true);
        mb.setAutoClose(false);
        mb.getButton(ButtonId.YES).setCaption("Aceptar");
        mb.getButton(ButtonId.CANCEL).setCaption("Cancelar");
    }

    private boolean existeMantenimientoEnGrid(MantenimientoDetalle bean, List<MantenimientoDetalle> mantenimientoDetalles) {
        for (MantenimientoDetalle mantenimientoDetalle : mantenimientoDetalles) {
            if (bean.getActividad().equals(mantenimientoDetalle.getActividad()) && bean.getResultado().equals(mantenimientoDetalle.getResultado())) {
                return true;
            }
        }
        return false;
    }

    private boolean existeMantenimienteEnGrid(MantenimientoDetalle mantenimientoDetalle) {
        List<Long> ids = container.getItemIds();
        for (Long id : ids) {
            MantenimientoDetalle getMantenimienoDetalle = container.getItem(id).getBean();
            if (getMantenimienoDetalle.getActividad().equals(mantenimientoDetalle.getActividad()) && getMantenimienoDetalle.getResultado().equals(mantenimientoDetalle.getResultado())) {
                return true;
            }
        }
        return false;
    }

    public void restablecerListados() {
        mantenimientoDetalleEliminados.clear();
        mantenimientoDetalleModificados.clear();
        mantenimientoDetalleAdicionados.clear();
    }

    public static class Container extends BeanContainer<Long, MantenimientoDetalle> {

        public Container() {
            super(MantenimientoDetalle.class);
            setBeanIdResolver(mantenimientoDetalle -> mantenimientoDetalle.getId());
        }
    }
}
