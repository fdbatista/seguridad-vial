package com.earandap.vehiculos.ui.views.alistamiento;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.AccionAlistamiento;
import com.earandap.vehiculos.domain.AlistamientoDetalle;
import com.earandap.vehiculos.domain.nomenclador.Actividad;
import com.earandap.vehiculos.domain.nomenclador.Resultado;
import com.earandap.vehiculos.repository.ActividadRepository;
import com.earandap.vehiculos.repository.AlistamientoActividadRepository;
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
 * Created by Angel Luis on 11/4/2015.
 */
@SpringComponent
@UIScope
public class AlistamientoDetalleGrid extends Grid {

    protected Container container;

    private MessageBox mb;

    int id;

    private AccionAlistamiento accionAlistamiento;

    private Map<Long, AlistamientoDetalle> alistamientoDetalleModificados;

    private Map<Long, AlistamientoDetalle> alistamientoDetalleAdicionados;

    private Map<Long, AlistamientoDetalle> alistamientoDetalleEliminados;

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
    private AlistamientoActividadRepository alistamientoActividadRepository;

    public AccionAlistamiento getAccionAlistamiento() {
        return accionAlistamiento;
    }

    public void setAccionAlistamiento(AccionAlistamiento accionAlistamiento) {
        this.accionAlistamiento = accionAlistamiento;
    }

    public Map<Long, AlistamientoDetalle> getAlistamientoDetalleModificados() {
        return alistamientoDetalleModificados;
    }

    public void setAlistamientoDetalleModificados(Map<Long, AlistamientoDetalle> alistamientoDetalleModificados) {
        this.alistamientoDetalleModificados = alistamientoDetalleModificados;
    }

    public Map<Long, AlistamientoDetalle> getAlistamientoDetalleAdicionados() {
        return alistamientoDetalleAdicionados;
    }

    public void setAlistamientoDetalleAdicionados(Map<Long, AlistamientoDetalle> alistamientoDetalleAdicionados) {
        this.alistamientoDetalleAdicionados = alistamientoDetalleAdicionados;
    }

    public Map<Long, AlistamientoDetalle> getAlistamientoDetalleEliminados() {
        return alistamientoDetalleEliminados;
    }

    public void setAlistamientoDetalleEliminados(Map<Long, AlistamientoDetalle> alistamientoDetalleEliminados) {
        this.alistamientoDetalleEliminados = alistamientoDetalleEliminados;
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

        alistamientoDetalleAdicionados = new HashMap<Long, AlistamientoDetalle>();
        alistamientoDetalleModificados = new HashMap<Long, AlistamientoDetalle>();
        alistamientoDetalleEliminados = new HashMap<Long, AlistamientoDetalle>();

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
        this.addColumn("detalle").setHeaderCaption("Detalle");
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
                AlistamientoDetalle alistamientoDetalle = container.getItem(rendererClickEvent.getItemId()).getBean();
                adicionaModificarBoton(alistamientoDetalle, false);
            }

            @Override
            public void onDelete(ClickableRenderer.RendererClickEvent rendererClickEvent) {
                MessageBox messageBox = MessageBox.showPlain(
                        Icon.QUESTION,
                        "Interrogante",
                        "Est\u00E1 seguro de que quiere eliminar?",
                        ButtonId.YES,
                        ButtonId.NO);
                messageBox.getButton(ButtonId.YES).addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent e) {
                        AlistamientoDetalle alistamientoDetalle = container.getItem(rendererClickEvent.getItemId()).getBean();
                        if (alistamientoDetalleAdicionados.containsKey(alistamientoDetalle.getId())) {
                            alistamientoDetalleAdicionados.remove(alistamientoDetalle.getId());
                        }
                        if (alistamientoDetalleModificados.containsKey(alistamientoDetalle.getId())) {
                            alistamientoDetalleModificados.remove(alistamientoDetalle.getId());
                        }
                        alistamientoDetalleEliminados.put(alistamientoDetalle.getId(), alistamientoDetalle);
                        container.removeItem(alistamientoDetalle.getId());
                        StaticMembers.showNotificationMessage("Notificaci\u00F3n", "El Alistamiento-Detalle se eliminar\u00E1 luego de guardar los cambios");
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

                AlistamientoDetalle alistamientoDetalle = container.getItem(id).getBean();
                alistamientoDetalleAdicionados.remove(alistamientoDetalle.getId());
                alistamientoDetalleModificados.put(alistamientoDetalle.getId(), alistamientoDetalle);
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
        Grid.HeaderRow firstHeaderRow = this.prependHeaderRow();
        firstHeaderRow.join("actividad", "resultado", "detalle");
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
                lista = alistamientoActividadRepository.buscarActividadesInAlistamientoActividadExcludeId(accionAlistamiento.getVehiculo().getClaseVehiculo(), actividadesUsadas.keySet());
                if (lista.isEmpty()) {
                    listaVacia = true;
                }
            } else if (accionAlistamiento.getVehiculo() != null) {
                lista = alistamientoActividadRepository.buscarActividadesInAlistamientoActividad(accionAlistamiento.getVehiculo().getClaseVehiculo());
                if (lista.isEmpty()) {
                    listaVacia = true;
                }
            } else {
                Notification notification = new Notification("Veh\u00EDculo es requerido", Notification.Type.TRAY_NOTIFICATION);
                notification.setDelayMsec(3000);
                notification.setIcon(FontAwesome.INFO_CIRCLE);
                notification.setStyleName(ValoTheme.NOTIFICATION_WARNING);
                notification.show(Page.getCurrent());
            }

            if (listaVacia) {
                Notification notification = new Notification("Existen todas las actividades seg\u00FAn clase de veh\u00EDculo y tipo de alistamiento", Notification.Type.TRAY_NOTIFICATION);
                notification.setDelayMsec(3000);
                notification.setIcon(FontAwesome.INFO_CIRCLE);
                notification.setStyleName(ValoTheme.NOTIFICATION_WARNING);
                notification.show(Page.getCurrent());
            }

            for (Actividad actividad : lista) {
                AlistamientoDetalle alistamientoDetalleTemp = new AlistamientoDetalle();
                alistamientoDetalleTemp.setId(id--);
                alistamientoDetalleTemp.setAlistamiento(accionAlistamiento);
                alistamientoDetalleTemp.setActividad(actividad);
                Resultado mejorResultado = resultadoRepository.findAll().get(1);//Todo Definir el mejor Resultado.
                alistamientoDetalleTemp.setResultado(mejorResultado);
                container.addBean(alistamientoDetalleTemp);
                alistamientoDetalleAdicionados.put(alistamientoDetalleTemp.getId(), alistamientoDetalleTemp);
            }
        });

        cargarActividades.setCaption("Cargar Actividades");
        cargarActividades.setIcon(FontAwesome.UPLOAD);
        cargarActividades.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        cargarActividades.addStyleName(ValoTheme.BUTTON_TINY);
        buttonLayout.addComponent(cargarActividades);

        Button adicionar = new Button("", (Button.ClickListener) event -> {
            this.adicionaModificarBoton(new AlistamientoDetalle(), true);
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

    private void adicionaModificarBoton(AlistamientoDetalle alistamientoDetalle, Boolean adicionar) {
        actividadesUsadas.clear();
        List<Long> ids = container.getItemIds();
        for (Long id : ids) {
            Actividad actividad = container.getItem(id).getBean().getActividad();
            actividadesUsadas.put(actividad.getId(), actividad);
        }
        if (!adicionar) {
            actividadesUsadas.remove(alistamientoDetalle.getActividad().getId());
        }
        Collection<Long> idactividadespermitidas = actividadesUsadas.keySet();
        List<Actividad> lista = actividadRepository.findAll();
        if (!actividadesUsadas.isEmpty()) {
            lista = actividadRepository.findByIdNotIn(actividadesUsadas.keySet());
        }

        AlistamientoDetalleForm form = new AlistamientoDetalleForm(alistamientoDetalle, lista, resultadoRepository.findAll());
        mb = MessageBox.showCustomized(Icon.NONE, "Adicionar Detalles de Alistamiento", form, buttonId -> {
            if (buttonId == ButtonId.YES) {
                try {
                    form.getAlistamientoDetalleBeanFieldGroup().commit();
                    BeanItem<AlistamientoDetalle> item = form.getAlistamientoDetalleBeanFieldGroup().getItemDataSource();

                    if (adicionar) {
                        if (!this.existeAlistamientoEnGrid(item.getBean())) {
                            item.getBean().setId(id--);
                            alistamientoDetalleAdicionados.put(item.getBean().getId(), item.getBean());
                            container.addBean(item.getBean());
                        } else {
                            throw new FieldGroup.CommitException("Ya existe ese alistamiento");
                        }
                    } else {
                        alistamientoDetalleAdicionados.remove(item.getBean().getId());
                        alistamientoDetalleModificados.put(item.getBean().getId(), item.getBean());
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
                        Notification notification = new Notification(e.getMessage(), Notification.Type.TRAY_NOTIFICATION);
                        notification.setDelayMsec(3000);
                        notification.setIcon(FontAwesome.INFO_CIRCLE);
                        notification.setStyleName(ValoTheme.NOTIFICATION_ERROR);
                        notification.show(Page.getCurrent());
                    }

                }
            } else {
                mb.close();
            }
        }, ButtonId.CANCEL, ButtonId.YES).setWidth("500px").setHeight("300px");
        mb.setModal(true);
        mb.setAutoClose(false);
        mb.getButton(ButtonId.YES).setCaption("Aceptar");
        mb.getButton(ButtonId.CANCEL).setCaption("Cancelar");
    }

    private boolean existeAlistamientoEnGrid(AlistamientoDetalle bean, List<AlistamientoDetalle> alistamientoDetalles) {
        for (AlistamientoDetalle alistamientoDetalle : alistamientoDetalles) {
            if (bean.getActividad().equals(alistamientoDetalle.getActividad()) && bean.getResultado().equals(alistamientoDetalle.getResultado())) {
                return true;
            }
        }
        return false;
    }

    private boolean existeAlistamientoEnGrid(AlistamientoDetalle alistamientoDetalle) {
        List<Long> ids = container.getItemIds();
        for (Long id : ids) {
            AlistamientoDetalle getAlistamientoDetalle = container.getItem(id).getBean();
            if (getAlistamientoDetalle.getActividad().equals(alistamientoDetalle.getActividad()) && getAlistamientoDetalle.getResultado().equals(alistamientoDetalle.getResultado())) {
                return true;
            }
        }
        return false;
    }

    public void restablecerListados() {
        alistamientoDetalleEliminados.clear();
        alistamientoDetalleModificados.clear();
        alistamientoDetalleAdicionados.clear();
    }

    public static class Container extends BeanContainer<Long, AlistamientoDetalle> {

        public Container() {
            super(AlistamientoDetalle.class);
            setBeanIdResolver(alistamientoDetalle -> alistamientoDetalle.getId());
        }
    }
}
