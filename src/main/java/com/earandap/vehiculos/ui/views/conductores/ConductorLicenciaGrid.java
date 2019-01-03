package com.earandap.vehiculos.ui.views.conductores;

import com.earandap.vehiculos.domain.Licencia;
import com.earandap.vehiculos.repository.LicenciaRepository;
import com.earandap.vehiculos.repository.TipoLicenciaRepository;
import com.earandap.vehiculos.repository.TipoServicioRepository;
import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.data.util.converter.Converter;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Angel Luis on 11/22/2015.
 */
@SpringComponent
@UIScope
public class ConductorLicenciaGrid extends Grid {

    protected Container container;

    private MessageBox mb;

    int id;

    @Autowired
    private LicenciaRepository licenciaRepository;

    private Map<Long,Licencia> licenciasModificados;

    private Map<Long,Licencia> licenciasAdicionados;

    private Map<Long,Licencia> licenciasEliminados;

    public Map<Long, Licencia> getLicenciasModificados() {
        return licenciasModificados;
    }

    public void setLicenciasModificados(Map<Long, Licencia> licenciasModificados) {
        this.licenciasModificados = licenciasModificados;
    }

    public Map<Long, Licencia> getLicenciasAdicionados() {
        return licenciasAdicionados;
    }

    public void setLicenciasAdicionados(Map<Long, Licencia> licenciasAdicionados) {
        this.licenciasAdicionados = licenciasAdicionados;
    }

    public Map<Long, Licencia> getLicenciasEliminados() {
        return licenciasEliminados;
    }

    public void setLicenciasEliminados(Map<Long, Licencia> licenciasEliminados) {
        this.licenciasEliminados = licenciasEliminados;
    }

    @Autowired
    private TipoLicenciaRepository tipoLicenciaRepository;
    
    @Autowired
    private TipoServicioRepository tipoServicioRepository;

    @PostConstruct
    public void init() {
        this.container = new Container();
        this.container.addNestedContainerProperty("tipoLicencia.descripcion");
        Responsive.makeResponsive(this);
        this.setSizeFull();
        this.setHeight("280px");
        this.setSelectionMode(SelectionMode.SINGLE);

        id =-1;

        licenciasAdicionados = new HashMap<Long,Licencia>();
        licenciasModificados = new HashMap<Long,Licencia>();
        licenciasEliminados = new HashMap<Long,Licencia>();

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
        this.addColumn("tipoLicencia.descripcion").setHeaderCaption("Tipo de Licencia");
        this.addColumn("autorizadoEmpresa").setHeaderCaption("Autorizado").setConverter(new Converter<String, Boolean>() {
            @Override
            public Boolean convertToModel(String value, Class<? extends Boolean> targetType, Locale locale) throws ConversionException {
                return null;
            }

            @Override
            public String convertToPresentation(Boolean value, Class<? extends String> targetType, Locale locale) throws ConversionException {
                return value?"SI":"NO";
            }

            @Override
            public Class<Boolean> getModelType() {
                return Boolean.class;
            }

            @Override
            public Class<String> getPresentationType() {
                return String.class;
            }
        });
        this.addColumn("actions").setHeaderCaption("Acciones");

        this.setEditDeleteListener(new EditDeleteButtonValueRenderer.EditDeleteButtonClickListener() {
            @Override
            public void onEdit(ClickableRenderer.RendererClickEvent rendererClickEvent) {
                Licencia licencia = container.getItem(rendererClickEvent.getItemId()).getBean();
                adicionaModificarBoton(licencia,false);
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
                        Licencia licencia = container.getItem(rendererClickEvent.getItemId()).getBean();
                        if (licenciasAdicionados.containsKey(licencia.getId()))
                            licenciasAdicionados.remove(licencia.getId());
                        if (licenciasModificados.containsKey(licencia.getId()))
                            licenciasModificados.remove(licencia.getId());
                        licenciasEliminados.put(licencia.getId(),licencia);
                        container.removeItem(licencia.getId());
                        Notification notification = new Notification("Notificaci\u00F3n", "La licencia se eliminar\u00E1 luego de guardar los cambios", Notification.Type.TRAY_NOTIFICATION);
                        notification.setDelayMsec(3000);
                        notification.setIcon(FontAwesome.INFO_CIRCLE);
                        notification.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
                        notification.show(Page.getCurrent());
                    }
                });
            }
        });
        initExtraHeaderRow();
    }

    private void initExtraHeaderRow() {
        HeaderRow firstHeaderRow = this.prependHeaderRow();
        firstHeaderRow.join("tipoLicencia.descripcion","autorizadoEmpresa");
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSizeFull();
        buttonLayout.setSpacing(true);
        buttonLayout.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
        firstHeaderRow.getCell("actions").setComponent(buttonLayout);
        Button adicionar = new Button("", (Button.ClickListener) event -> {
            this.adicionaModificarBoton(new Licencia(),true);
        }
        );
        adicionar.setCaption("Adicionar licencia al conductor");
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

    public void restablecerListados() {
        licenciasAdicionados.clear();
        licenciasModificados.clear();
        licenciasEliminados.clear();
    }

    private void adicionaModificarBoton(Licencia licencia,Boolean adicionar){
        LicenciaForm form = new LicenciaForm(licencia, tipoLicenciaRepository.findAll(), tipoServicioRepository.findAll());
        mb = MessageBox.showCustomized(Icon.NONE, "Adicionar Licencias", form
                , buttonId -> {
            if (buttonId == ButtonId.YES) {
                try {
                    form.getLicenciaBeanFieldGroup().commit();
                    BeanItem<Licencia> item = form.getLicenciaBeanFieldGroup().getItemDataSource();
                    if(adicionar) {
                        item.getBean().setId(id--);
                        licenciasAdicionados.put(item.getBean().getId(), item.getBean());
                        container.addBean(item.getBean());
                    }
                    else{
                        licenciasAdicionados.remove(item.getBean().getId());
                        licenciasModificados.put(item.getBean().getId(), item.getBean());
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
                    }
                }
            } else {
                mb.close();
            }
        }, ButtonId.CANCEL, ButtonId.YES).setWidth("500px").setHeight("400px");
        mb.setModal(true);
        mb.setAutoClose(false);
        mb.getButton(ButtonId.YES).setCaption("Aceptar");
        mb.getButton(ButtonId.CANCEL).setCaption("Cancelar");
    }


    public static class Container extends BeanContainer<Long, Licencia> {
        public Container() {
            super(Licencia.class);
            setBeanIdResolver(licencia -> licencia.getId());
        }
    }
}
