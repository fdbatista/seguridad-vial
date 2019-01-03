package com.earandap.vehiculos.ui.views.capacitacion;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.CapacitacionDocumento;
import com.earandap.vehiculos.repository.ParametrosRepository;
import com.earandap.vehiculos.repository.TipoDocumentoCapacitacionRepository;
import com.earandap.vehiculos.ui.components.FileViewerForm;
import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickEvent;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickListener;
import com.vaadin.ui.themes.ValoTheme;
import de.steinwedel.messagebox.ButtonId;
import de.steinwedel.messagebox.Icon;
import de.steinwedel.messagebox.MessageBox;
import java.io.File;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.vaadin.gridutil.renderer.DeleteButtonValueRenderer;

/**
 * Created by felix.batista.
 */
@SpringComponent
@UIScope
public class CapacitacionDocumentoGrid extends Grid {

    private String fileDir, urlDir, rutaDocumentoPrevia;
    protected Container container;
    private MessageBox mb;
    private Button downloadButton;
    private HorizontalLayout buttonLayout;
    public boolean downloadButtonInit;
    File downloadFile;
    FileDownloader fileDownloader;
    public Long idCapacitacion;
    int id;

    @Autowired
    private TipoDocumentoCapacitacionRepository tipoDocumentoCapacitacionRepository;

    @Autowired
    private ParametrosRepository parametrosRepository;

    private Map<Long, CapacitacionDocumento> capacitacionDocumentoModificados;
    private Map<Long, CapacitacionDocumento> capacitacionDocumentoAdicionados;
    private Map<Long, CapacitacionDocumento> capacitacionDocumentoEliminados;

    public Map<Long, CapacitacionDocumento> getCapacitacionDocumentoModificados() {
        return capacitacionDocumentoModificados;
    }

    public void setCapacitacionDocumentoModificados(Map<Long, CapacitacionDocumento> capacitacionDocumentoModificados) {
        this.capacitacionDocumentoModificados = capacitacionDocumentoModificados;
    }

    public Map<Long, CapacitacionDocumento> getCapacitacionDocumentoAdicionados() {
        return capacitacionDocumentoAdicionados;
    }

    public void setCapacitacionDocumentoAdicionados(Map<Long, CapacitacionDocumento> capacitacionDocumentoAdicionados) {
        this.capacitacionDocumentoAdicionados = capacitacionDocumentoAdicionados;
    }

    public Map<Long, CapacitacionDocumento> getCapacitacionDocumentoEliminados() {
        return capacitacionDocumentoEliminados;
    }

    public void setCapacitacionDocumentoEliminados(Map<Long, CapacitacionDocumento> capacitacionDocumentoEliminados) {
        this.capacitacionDocumentoEliminados = capacitacionDocumentoEliminados;
    }

    private void initDownloadButton() {
        this.downloadButton = new Button("");
        this.downloadButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        this.downloadButton.setId("downloadButtonUniqueId");
        downloadButtonInit = false;
    }

    @PostConstruct
    public void init() {
        this.container = new Container();
        container.addNestedContainerProperty("tipoDocumentoCapacitacion.descripcion");
        Responsive.makeResponsive(this);
        this.setSizeFull();
        this.setHeight("280px");
        this.setSelectionMode(SelectionMode.SINGLE);
        this.fileDir = parametrosRepository.findOne(Long.valueOf("2")).getValorParametro();
        this.urlDir = parametrosRepository.findOne(Long.valueOf("3")).getValorParametro();

        initDownloadButton();

        id = -1;

        capacitacionDocumentoAdicionados = new HashMap<Long, CapacitacionDocumento>();
        capacitacionDocumentoModificados = new HashMap<Long, CapacitacionDocumento>();
        capacitacionDocumentoEliminados = new HashMap<Long, CapacitacionDocumento>();

        GeneratedPropertyContainer gpc = new GeneratedPropertyContainer(container);

        gpc.addGeneratedProperty("ver", new PropertyValueGenerator<String>() {
            @Override
            public String getValue(Item item, Object itemId, Object propertyId) {
                //return "\uf006";
                return "Ver";
            }

            @Override
            public Class<String> getType() {
                return String.class;
            }
        });

        gpc.addGeneratedProperty("descargar", new PropertyValueGenerator<String>() {
            @Override
            public String getValue(Item item, Object itemId, Object propertyId) {
                //return "\uf019";
                return "Bajar";
            }

            @Override
            public Class<String> getType() {
                return String.class;
            }
        });

        gpc.addGeneratedProperty("eliminar", new PropertyValueGenerator<String>() {
            @Override
            public String getValue(Item item, Object itemId, Object propertyId) {
                return "Eliminar";
            }

            @Override
            public Class<String> getType() {
                return String.class;
            }
        });

        this.setContainerDataSource(gpc);

        this.removeAllColumns();
        this.addColumn("tipoDocumentoCapacitacion.descripcion").setHeaderCaption("Tipo").setWidth(250);
        this.addColumn("rutaDocumento").setHeaderCaption("Documento").setWidth(285);

        this.addColumn("ver").setHeaderCaption("").setWidth(67).setRenderer(new ButtonRenderer(new RendererClickListener() {
            @Override
            public void click(RendererClickEvent event) {
                CapacitacionDocumento empDoc = container.getItem(event.getItemId()).getBean();
                File fichero = new File(StaticMembers.construirRutaDocumentoCapacitacion(fileDir, empDoc.getRutaDocumento(), idCapacitacion));
                if (fichero.exists()) {
                    //String urlTest = "http://docs.google.com/gview?url=http://www.15minutemondays.com/wp-content/uploads/2015/02/EmbeddedWordDocExample.doc&embedded=true";
                    String url = "http://docs.google.com/gview?url=" + StaticMembers.construirUrlDocumentoCapacitacion(urlDir, empDoc.getRutaDocumento(), idCapacitacion) + "&embedded=true";
                    FileViewerForm form = new FileViewerForm(url);
                    mb = MessageBox.showCustomized(Icon.NONE, "Visualizar documento: " + empDoc.getRutaDocumento(), form, buttonId -> {
                        if (buttonId == ButtonId.NO) {
                            mb.close();
                        }
                    }, ButtonId.NO).setWidth("800px").setHeight("600px");
                    mb.setModal(true);
                    mb.setAutoClose(false);
                    mb.setWidth(800, Unit.PIXELS);
                    mb.setHeight(600, Unit.PIXELS);
                    mb.getButton(ButtonId.NO).setCaption("Cerrar");
                } else {
                    StaticMembers.showNotificationError("Error", "El fichero solicitado no existe");
                }
            }
        }));

        this.addColumn("descargar").setHeaderCaption("").setWidth(77).setRenderer(new ButtonRenderer(new RendererClickListener() {
            @Override
            public void click(RendererClickEvent event) {
                CapacitacionDocumento empDoc = container.getItem(event.getItemId()).getBean();
                File fichero = new File(StaticMembers.construirRutaDocumentoCapacitacion(fileDir, empDoc.getRutaDocumento(), idCapacitacion));
                if (fichero.exists()) {
                    downloadFile = fichero;
                    if (!downloadButtonInit) {
                        fileDownloader = new FileDownloader(new FileResource(downloadFile));
                        downloadButtonInit = true;
                    } else {
                        fileDownloader.setFileDownloadResource(new FileResource(downloadFile));
                    }
                    fileDownloader.extend(downloadButton);

                    Page.getCurrent().getJavaScript().execute("document.getElementById('downloadButtonUniqueId').click();");
                } else {
                    StaticMembers.showNotificationError("Error", "El fichero solicitado no existe");
                }
            }
        }
        ));

        this.addColumn("eliminar").setHeaderCaption("").setWidth(94).setRenderer(new ButtonRenderer(new RendererClickListener() {
            @Override
            public void click(RendererClickEvent event) {

                MessageBox messageBox = MessageBox.showPlain(
                        Icon.QUESTION,
                        "Interrogante",
                        "Est\u00E1 seguro de que quiere eliminar?",
                        ButtonId.YES,
                        ButtonId.NO);
                messageBox.getButton(ButtonId.YES).addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent e) {
                        CapacitacionDocumento capacitacionDocumento = container.getItem(event.getItemId()).getBean();
                        Long itemId = capacitacionDocumento.getId();
                        if (capacitacionDocumentoAdicionados.containsKey(itemId)) {
                            capacitacionDocumentoAdicionados.remove(itemId);
                        }
                        if (capacitacionDocumentoModificados.containsKey(itemId)) {
                            capacitacionDocumentoModificados.remove(itemId);
                        }
                        capacitacionDocumentoEliminados.put(itemId, capacitacionDocumento);
                        container.removeItem(itemId);
                        StaticMembers.showNotificationMessage("Notificaci\u00F3n", "El documento se eliminar\u00E1 luego de guardar los cambios");
                    }
                });

            }
        }
        ));

        initExtraHeaderRow();
    }

    private void initExtraHeaderRow() {
        HeaderRow firstHeaderRow = this.prependHeaderRow();
        firstHeaderRow.join("tipoDocumentoCapacitacion.descripcion", "rutaDocumento", "ver", "descargar", "eliminar");
        buttonLayout = new HorizontalLayout();
        buttonLayout.setSizeFull();
        buttonLayout.setSpacing(true);
        buttonLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        firstHeaderRow.getCell("tipoDocumentoCapacitacion.descripcion").setComponent(buttonLayout);

        Button adicionar = new Button("", (Button.ClickListener) event -> {
            this.adicionaModificarBoton(new CapacitacionDocumento(), true);
        }
        );
        adicionar.setCaption("Adicionar documento");
        adicionar.setIcon(FontAwesome.UPLOAD);
        adicionar.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        adicionar.addStyleName(ValoTheme.BUTTON_TINY);

        buttonLayout.addComponent(adicionar);
        buttonLayout.addComponent(downloadButton);

    }

    public void setDeleteListener(DeleteButtonValueRenderer.RendererClickListener listener) {
        if (listener != null) {
            this.getColumn("eliminar").setRenderer(new DeleteButtonValueRenderer(listener)).setWidth(100);
        }
    }

    public void restablecerListados() {
        capacitacionDocumentoAdicionados.clear();
        capacitacionDocumentoModificados.clear();
        capacitacionDocumentoEliminados.clear();
    }

    private void adicionaModificarBoton(CapacitacionDocumento capacitacionDocumento, Boolean adicionar) {
        CapacitacionDocumentoForm form = new CapacitacionDocumentoForm(adicionar, idCapacitacion, capacitacionDocumento, tipoDocumentoCapacitacionRepository.findAll(), fileDir, urlDir);
        form.setTipoDocumentoCapacitacionRepository(tipoDocumentoCapacitacionRepository);
        rutaDocumentoPrevia = capacitacionDocumento.getRutaDocumento();
        rutaDocumentoPrevia = (rutaDocumentoPrevia == null) ? "" : rutaDocumentoPrevia;

        mb = MessageBox.showCustomized(Icon.NONE, "Adicionar Documento", form,
                buttonId -> {
                    if (buttonId == ButtonId.YES) {
                        try {
                            form.getCapacitacionDocumentoBeanFieldGroup().commit();
                            CapacitacionDocumento item = form.getCapacitacionDocumentoBeanFieldGroup().getItemDataSource().getBean();

                            if (form.getNombreFichero().equals("")) {
                                StaticMembers.showNotificationError("Error", "Debe seleccionar un documento");
                            } else {
                                if (adicionar) {
                                    item.setId(id--);
                                    capacitacionDocumentoAdicionados.put(item.getId(), item);
                                    container.addBean(item);
                                } else {
                                    capacitacionDocumentoAdicionados.remove(item.getId());
                                    capacitacionDocumentoModificados.put(item.getId(), item);
                                    container.removeItem(item.getId());
                                    container.addBean(item);
                                }
                                StaticMembers.eliminarFicherosCapacitacionInvalidos(false, form.getNombreFichero(), rutaDocumentoPrevia, fileDir, idCapacitacion, form.getFicherosCargados());
                                mb.close();
                            }
                        } catch (FieldGroup.CommitException e) {
                            Collection<Validator.InvalidValueException> values = e.getInvalidFields().values();
                            if (!values.isEmpty()) {
                                Notification.show(values.iterator().next().getMessage(), e.getCause().getMessage(), Notification.Type.TRAY_NOTIFICATION);
                            } else if (e.getCause() instanceof Validator.InvalidValueException) {
                                Notification.show(e.getCause().getMessage(), Notification.Type.TRAY_NOTIFICATION);
                            }
                        }
                    } else {
                        StaticMembers.eliminarFicherosCapacitacionInvalidos(true, form.getNombreFichero(), rutaDocumentoPrevia, fileDir, idCapacitacion, form.getFicherosCargados());
                        mb.close();
                    }
                }, ButtonId.CANCEL, ButtonId.YES).setWidth("440px").setHeight("300px");
        mb.setModal(true);
        mb.setAutoClose(false);
        mb.getButton(ButtonId.YES).setCaption("Aceptar");
        mb.getButton(ButtonId.CANCEL).setCaption("Cancelar");
        mb.setWidth(440, Unit.PIXELS);
        mb.setHeight(300, Unit.PIXELS);

    }

    public static class Container extends BeanContainer<Long, CapacitacionDocumento> {

        public Container() {
            super(CapacitacionDocumento.class);
            setBeanIdResolver(capacitacionDocumento -> capacitacionDocumento.getId());
        }
    }
}
