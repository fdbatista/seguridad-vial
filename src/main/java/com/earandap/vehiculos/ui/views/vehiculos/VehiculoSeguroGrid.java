package com.earandap.vehiculos.ui.views.vehiculos;

import com.earandap.vehiculos.domain.Seguro;
import com.earandap.vehiculos.repository.CompanhiaSegurosRepository;
import com.earandap.vehiculos.repository.SeguroRepository;
import com.earandap.vehiculos.repository.TipoSeguroRepository;
import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ClickableRenderer;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.themes.ValoTheme;
import de.steinwedel.messagebox.ButtonId;
import de.steinwedel.messagebox.Icon;
import de.steinwedel.messagebox.MessageBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.gridutil.renderer.EditDeleteButtonValueRenderer;

import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Angel Luis on 10/17/2015.
 */
@SpringComponent
@UIScope
public class VehiculoSeguroGrid extends Grid {

    protected Container container;

    private MessageBox mb;
    
    int id;

    @Autowired
    private SeguroRepository seguroRepository;

    private Map<Long, Seguro> segurosModificados;

    private Map<Long, Seguro> segurosAdicionados;

    private Map<Long, Seguro> segurosEliminados;

    public Map<Long, Seguro> getSegurosModificados() {
        return segurosModificados;
    }

    public Map<Long, Seguro> getSegurosAdicionados() {
        return segurosAdicionados;
    }

    public Map<Long, Seguro> getSegurosEliminados() {
        return segurosEliminados;
    }

    @Autowired
    private TipoSeguroRepository tipoSeguroRepository;

    @Autowired
    private CompanhiaSegurosRepository companhiaRepository;

    @PostConstruct
    public void init() {
        this.container = new Container();
        Responsive.makeResponsive(this);
        this.setSizeFull();
        this.setHeight("320px");
        this.setSelectionMode(SelectionMode.SINGLE);

        id = -1;

        segurosAdicionados = new HashMap<Long, Seguro>();
        segurosModificados = new HashMap<Long, Seguro>();
        segurosEliminados = new HashMap<Long, Seguro>();

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
        this.addColumn("tipoSeguro").setHeaderCaption("Tipo de Seguro");
        this.addColumn("numero").setHeaderCaption("N\u00FAmero del Seguro");
        this.addColumn("fechaEmision").setRenderer(new DateRenderer(DateFormat.getDateInstance()));
        this.addColumn("fechaVencimiento").setRenderer(new DateRenderer(DateFormat.getDateInstance()));
        this.addColumn("actions").setHeaderCaption("Acciones");

        this.setEditDeleteListener(new EditDeleteButtonValueRenderer.EditDeleteButtonClickListener() {
            @Override
            public void onEdit(ClickableRenderer.RendererClickEvent rendererClickEvent) {
                Seguro seguro = container.getItem(rendererClickEvent.getItemId()).getBean();
                //tipoSeguroAnterior = seguro.getTipoSeguro();
                adicionaModificarBoton(seguro, false);
            }

            @Override
            public void onDelete(ClickableRenderer.RendererClickEvent rendererClickEvent) {
                MessageBox messageBox = MessageBox.showPlain(
                        Icon.QUESTION,
                        "Interrogante",
                        "¿Est\u00E1 seguro de que desea eliminar?",
                        ButtonId.YES,
                        ButtonId.NO);
                messageBox.getButton(ButtonId.YES).addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent e) {
                        Seguro seguro = container.getItem(rendererClickEvent.getItemId()).getBean();
                        /*boolean existeSoat;

                        if (!seguro.getTipoSeguro().getDescripcion().equals("SOAT")) {
                            existeSoat = true;
                        } else {
                            existeSoat = existeSOAT(seguro);
                        }

                        if (existeSoat) {*/
                            if (segurosAdicionados.containsKey(seguro.getId())) {
                                segurosAdicionados.remove(seguro.getId());
                            }
                            if (segurosModificados.containsKey(seguro.getId())) {
                                segurosModificados.remove(seguro.getId());
                            }
                            segurosEliminados.put(seguro.getId(), seguro);
                            container.removeItem(seguro.getId());
                            Notification notification = new Notification("Notificaci\u00F3n", "El seguro se eliminar\u00E1 luego de guardar los cambios", Notification.Type.TRAY_NOTIFICATION);
                            notification.setDelayMsec(3000);
                            notification.setIcon(FontAwesome.INFO_CIRCLE);
                            notification.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
                            notification.show(Page.getCurrent());
                        /*} else {
                            Notification notification = new Notification("Alerta", "Debe especificar un seguro tipo SOAT", Notification.Type.WARNING_MESSAGE);
                            notification.setPosition(Position.BOTTOM_RIGHT);
                            notification.setDelayMsec(3000);
                            notification.setIcon(FontAwesome.WARNING);
                            notification.show(Page.getCurrent());
                        }*/
                    }
                });
            }
        });
        initExtraHeaderRow();
    }

    private boolean existeSOAT(Seguro seguro) {
        Seguro seguroAux;
        List<Long> ids = container.getItemIds();

        int cantSeguros = ids.size();
        for (int i = 0; i < cantSeguros; i++) {
            seguroAux = container.getItem(ids.get(i)).getBean();
            if (seguroAux.getId() != seguro.getId() && seguroAux.getTipoSeguro().getDescripcion().equals("SOAT")) {
                return true;
            }
        }
        return false;
    }

    private void initExtraHeaderRow() {
        HeaderRow firstHeaderRow = this.prependHeaderRow();
        firstHeaderRow.join("tipoSeguro", "numero", "fechaEmision", "fechaVencimiento");
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSizeFull();
        buttonLayout.setSpacing(true);
        buttonLayout.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
        firstHeaderRow.getCell("actions").setComponent(buttonLayout);
        Button adicionar = new Button("", (Button.ClickListener) event -> {
            this.adicionaModificarBoton(new Seguro(), true);
        }
        );
        adicionar.setCaption("Adicionar seguro al veh\u00EDculo");
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
        segurosAdicionados.clear();
        segurosModificados.clear();
        segurosEliminados.clear();
    }

    private void adicionaModificarBoton(Seguro seguro, Boolean adicionar) {
        SeguroForm form = new SeguroForm(seguro, tipoSeguroRepository.findAll(), companhiaRepository.findAll());
        mb = MessageBox.showCustomized(Icon.NONE, "Adicionar Seguros", form,
                buttonId -> {
                    if (buttonId == ButtonId.YES) {
                        try {
                            form.getSeguroBeanFieldGroup().commit();
                            BeanItem<Seguro> item = form.getSeguroBeanFieldGroup().getItemDataSource();
                            if (adicionar) {
                                item.getBean().setId(id--);
                                segurosAdicionados.put(item.getBean().getId(), item.getBean());
                                container.addBean(item.getBean());
                            } else {
                                /*boolean existeSoat;
                                if (!tipoSeguroAnterior.getDescripcion().equals("SOAT")) {
                                    existeSoat = true;
                                } else {
                                    existeSoat = existeSOAT(seguro);
                                }

                                if (existeSoat) {*/
                                    segurosAdicionados.remove(item.getBean().getId());
                                    segurosModificados.put(item.getBean().getId(), item.getBean());
                                    container.removeItem(item.getBean().getId());
                                    container.addBean(item.getBean());
                                /*} else {
                                    Notification notification = new Notification("Alerta", "Debe especificar un seguro tipo SOAT", Notification.Type.WARNING_MESSAGE);
                                    notification.setPosition(Position.BOTTOM_RIGHT);
                                    notification.setDelayMsec(3000);
                                    notification.setIcon(FontAwesome.WARNING);
                                    notification.show(Page.getCurrent());
                                }*/

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
                }, ButtonId.CANCEL, ButtonId.YES).setWidth("500px").setHeight("350px");
        mb.setModal(true);
        mb.setAutoClose(false);
        mb.getButton(ButtonId.YES).setCaption("Aceptar");
        mb.getButton(ButtonId.CANCEL).setCaption("Cancelar");
    }

    public static class Container extends BeanContainer<Long, Seguro> {

        public Container() {
            super(Seguro.class);
            setBeanIdResolver(seguro -> seguro.getId());
        }
    }
}
