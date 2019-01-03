package com.earandap.vehiculos.ui.views.incidentes;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.Archivo;
import com.earandap.vehiculos.domain.Incidente;
import com.earandap.vehiculos.repository.ParametrosRepository;
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
import com.vaadin.ui.renderers.ClickableRenderer;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickEvent;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickListener;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.themes.ValoTheme;
import de.steinwedel.messagebox.ButtonId;
import de.steinwedel.messagebox.Icon;
import de.steinwedel.messagebox.MessageBox;
import java.io.File;

import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Autor Eduardo Aranda
 * @Version 0.1
 */
@SpringComponent
@UIScope
public class IncidenteArchivoGrid extends Grid {

    @Autowired
    private ParametrosRepository parametrosRepository;

    protected Container container;
    private String fileDir, urlDir, rutaDocumentoPrevia;
    private MessageBox mb;
    private Incidente incidente;
    private Map<Long, Archivo> incidenteArchivoAdicionados, incidenteArchivoModificados, incidenteArchivoEliminados;
    private File downloadFile;
    private Button downloadButton;
    private FileDownloader fileDownloader;
    public boolean downloadButtonInit;
    public int id;

    @PostConstruct
    public void init() {
        this.container = new Container();
        Responsive.makeResponsive(this);
        this.setSizeFull();
        this.setHeight("180px");
        this.setSelectionMode(SelectionMode.NONE);
        this.fileDir = parametrosRepository.findOne(Long.valueOf("2")).getValorParametro();
        this.urlDir = parametrosRepository.findOne(Long.valueOf("3")).getValorParametro();
        this.rutaDocumentoPrevia = "";
        initDownloadButton();
        id = -1;

        incidenteArchivoAdicionados = new HashMap<>();
        incidenteArchivoModificados = new HashMap<>();
        incidenteArchivoEliminados = new HashMap<>();

        GeneratedPropertyContainer gpc = new GeneratedPropertyContainer(container);

        gpc.addGeneratedProperty("ver", new PropertyValueGenerator<String>() {
            @Override
            public String getValue(Item item, Object itemId, Object propertyId) {
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
        this.addColumn("url").setHeaderCaption("Nombre");
        this.addColumn("fecha").setRenderer(new DateRenderer(DateFormat.getDateInstance()));

        this.addColumn("ver").setHeaderCaption("").setWidth(67).setRenderer(new ButtonRenderer(new RendererClickListener() {
            @Override
            public void click(ClickableRenderer.RendererClickEvent event) {
                Archivo archivo = container.getItem(event.getItemId()).getBean();
                File fichero = new File(StaticMembers.construirRutaDocumentoIncidente(fileDir, archivo.getUrl(), incidente.getId()));
                if (fichero.exists()) {
                    String url = "http://docs.google.com/gview?url=" + StaticMembers.construirRutaDocumentoIncidente(urlDir, archivo.getUrl(), incidente.getId()) + "&embedded=true";
                    FileViewerForm form = new FileViewerForm(url);
                    mb = MessageBox.showCustomized(Icon.NONE, "Visualizar documento: " + archivo.getUrl(), form, buttonId -> {
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
                Archivo archivo = container.getItem(event.getItemId()).getBean();
                File fichero = new File(StaticMembers.construirRutaDocumentoIncidente(fileDir, archivo.getUrl(), incidente.getId()));
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

        this.addColumn("eliminar").setHeaderCaption("").setWidth(94).setRenderer(new ButtonRenderer(new ClickableRenderer.RendererClickListener() {
            @Override
            public void click(ClickableRenderer.RendererClickEvent event) {

                MessageBox messageBox = MessageBox.showPlain(
                        Icon.QUESTION,
                        "Interrogante",
                        "Est\u00E1 seguro de que quiere eliminar?",
                        ButtonId.YES,
                        ButtonId.NO);
                messageBox.getButton(ButtonId.YES).addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent e) {
                        Archivo archivo = container.getItem(event.getItemId()).getBean();
                        Long itemId = archivo.getId();
                        if (incidenteArchivoAdicionados.containsKey(itemId)) {
                            incidenteArchivoAdicionados.remove(itemId);
                        }
                        if (incidenteArchivoModificados.containsKey(itemId)) {
                            incidenteArchivoModificados.remove(itemId);
                        }
                        incidenteArchivoEliminados.put(itemId, archivo);
                        container.removeItem(itemId);
                        StaticMembers.showNotificationMessage("Notificaci\u00F3n", "El documento se eliminar\u00E1 luego de guardar los cambios");
                    }
                });
            }
        }
        ));

        initExtraHeaderRow();
    }

    private void initDownloadButton() {
        this.downloadButton = new Button("");
        this.downloadButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        this.downloadButton.setId("downloadButtonUniqueId");
        downloadButtonInit = false;
    }

    private void adicionaModificarBoton(Archivo archivo, Boolean adicionar) {
        ArchivoForm form = new ArchivoForm(adicionar, incidente.getId(), archivo, fileDir, urlDir);
        rutaDocumentoPrevia = archivo.getUrl();
        rutaDocumentoPrevia = (rutaDocumentoPrevia == null) ? "" : rutaDocumentoPrevia;

        mb = MessageBox.showCustomized(Icon.NONE, "Adicionar Documento", form,
                buttonId -> {
                    if (buttonId == ButtonId.YES) {
                        try {
                            form.getIncidenteArchivoBeanFieldGroup().commit();
                            Archivo item = form.getIncidenteArchivoBeanFieldGroup().getItemDataSource().getBean();

                            if (form.getUrlDir().equals("")) {
                                StaticMembers.showNotificationError("Error", "Debe seleccionar un documento");
                            } else {
                                if (adicionar) {
                                    item.setId(id--);
                                    incidenteArchivoAdicionados.put(item.getId(), item);
                                    container.addBean(item);
                                } else {
                                    incidenteArchivoAdicionados.remove(item.getId());
                                    incidenteArchivoModificados.put(item.getId(), item);
                                    container.removeItem(item.getId());
                                    container.addBean(item);
                                }
                                StaticMembers.eliminarFicherosIncidenteInvalidos(false, form.getNombreFichero(), rutaDocumentoPrevia, fileDir, incidente.getId(), form.getFicherosCargados());
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
                        StaticMembers.eliminarFicherosIncidenteInvalidos(true, form.getNombreFichero(), rutaDocumentoPrevia, fileDir, incidente.getId(), form.getFicherosCargados());
                        mb.close();
                    }
                }, ButtonId.CANCEL, ButtonId.YES).setWidth("500px").setHeight("300px");
        mb.setModal(true);
        mb.setAutoClose(false);
        mb.getButton(ButtonId.YES).setCaption("Aceptar");
        mb.getButton(ButtonId.CANCEL).setCaption("Cancelar");
        mb.setWidth(450, Unit.PIXELS);
        mb.setHeight(370, Unit.PIXELS);
    }

    private void initExtraHeaderRow() {
        HeaderRow fistHeaderRow = this.prependHeaderRow();
        fistHeaderRow.join("fecha", "url", "ver", "descargar", "eliminar");
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSizeFull();
        buttonLayout.setSpacing(true);
        buttonLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        fistHeaderRow.getCell("fecha").setComponent(buttonLayout);

        Button adicionar = new Button("", (Button.ClickListener) event -> {
            this.adicionaModificarBoton(new Archivo(), true);
        });
        adicionar.setCaption("Adicionar documento");
        adicionar.setIcon(FontAwesome.PLUS_SQUARE);
        adicionar.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        adicionar.addStyleName(ValoTheme.BUTTON_TINY);
        buttonLayout.addComponent(adicionar);
        buttonLayout.addComponent(downloadButton);
    }
    
    public void restablecerListados() {
        incidenteArchivoAdicionados.clear();
        incidenteArchivoModificados.clear();
        incidenteArchivoEliminados.clear();
    }

    public Map<Long, Archivo> getIncidenteArchivoAdicionados() {
        return incidenteArchivoAdicionados;
    }

    public void setIncidenteArchivoAdicionados(Map<Long, Archivo> incidenteArchivoAdicionados) {
        this.incidenteArchivoAdicionados = incidenteArchivoAdicionados;
    }

    public Map<Long, Archivo> getIncidenteArchivoModificados() {
        return incidenteArchivoModificados;
    }

    public void setIncidenteArchivoModificados(Map<Long, Archivo> incidenteArchivoModificados) {
        this.incidenteArchivoModificados = incidenteArchivoModificados;
    }

    public Map<Long, Archivo> getIncidenteArchivoEliminados() {
        return incidenteArchivoEliminados;
    }

    public void setIncidenteArchivoEliminados(Map<Long, Archivo> incidenteArchivoEliminados) {
        this.incidenteArchivoEliminados = incidenteArchivoEliminados;
    }

    public Incidente getIncidente() {
        return incidente;
    }

    public void setIncidente(Incidente incidente) {
        this.incidente = incidente;
    }

    public static class Container extends BeanContainer<Long, Archivo> {
        public Container() {
            super(Archivo.class);
            setBeanIdResolver(archivo -> archivo.getId());
        }
    }
    
}
