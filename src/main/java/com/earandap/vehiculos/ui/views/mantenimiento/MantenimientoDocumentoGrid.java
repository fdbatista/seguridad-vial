package com.earandap.vehiculos.ui.views.mantenimiento;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.MantenimientoDocumento;
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

/**
 * Created by felix.batista.
 */
@SpringComponent
@UIScope
public class MantenimientoDocumentoGrid extends Grid {

    private String fileDir, urlDir, rutaDocumentoPrevia;
    protected Container container;
    private MessageBox mb;
    private Button downloadButton;
    private HorizontalLayout buttonLayout;
    int id;
    File downloadFile;
    FileDownloader fileDownloader;
    public Long idMantenimiento;
    public boolean downloadButtonInit;

    @Autowired
    private ParametrosRepository parametrosRepository;

    private Map<Long, MantenimientoDocumento> mantenimientoDocumentoModificados;
    private Map<Long, MantenimientoDocumento> mantenimientoDocumentoAdicionados;
    private Map<Long, MantenimientoDocumento> mantenimientoDocumentoEliminados;

    @PostConstruct
    public void init() {
        this.container = new Container();
        //container.addNestedContainerProperty("tipoDocumentoMantenimiento.descripcion");
        Responsive.makeResponsive(this);
        this.setSizeFull();
        this.setHeight("250px");
        this.setSelectionMode(SelectionMode.SINGLE);
        this.fileDir = parametrosRepository.findOne(Long.valueOf("2")).getValorParametro();
        this.urlDir = parametrosRepository.findOne(Long.valueOf("3")).getValorParametro();
        this.rutaDocumentoPrevia = "";

        initDownloadButton();

        id = -1;

        mantenimientoDocumentoAdicionados = new HashMap<Long, MantenimientoDocumento>();
        mantenimientoDocumentoModificados = new HashMap<Long, MantenimientoDocumento>();
        mantenimientoDocumentoEliminados = new HashMap<Long, MantenimientoDocumento>();

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
        //this.addColumn("tipoDocumentoMantenimiento.descripcion").setHeaderCaption("Tipo").setWidth(213);
        this.addColumn("rutaDocumento").setHeaderCaption("Documento").setWidth(435);

        this.addColumn("ver").setHeaderCaption("").setWidth(67).setRenderer(new ButtonRenderer(new RendererClickListener() {
            @Override
            public void click(RendererClickEvent event) {
                MantenimientoDocumento empDoc = container.getItem(event.getItemId()).getBean();
                File fichero = new File(StaticMembers.construirRutaDocumentoMantenimiento(fileDir, empDoc.getRutaDocumento(), idMantenimiento));
                if (fichero.exists()) {
                    //String urlTest = "http://docs.google.com/gview?url=http://www.15minutemondays.com/wp-content/uploads/2015/02/EmbeddedWordDocExample.doc&embedded=true";
                    String url = "http://docs.google.com/gview?url=" + StaticMembers.construirUrlDocumentoMantenimiento(urlDir, empDoc.getRutaDocumento(), idMantenimiento) + "&embedded=true";
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
                MantenimientoDocumento empDoc = container.getItem(event.getItemId()).getBean();
                File fichero = new File(StaticMembers.construirRutaDocumentoMantenimiento(fileDir, empDoc.getRutaDocumento(), idMantenimiento));
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
                        MantenimientoDocumento mantenimientoDocumento = container.getItem(event.getItemId()).getBean();
                        Long itemId = mantenimientoDocumento.getId();
                        if (mantenimientoDocumentoAdicionados.containsKey(itemId)) {
                            mantenimientoDocumentoAdicionados.remove(itemId);
                        }
                        if (mantenimientoDocumentoModificados.containsKey(itemId)) {
                            mantenimientoDocumentoModificados.remove(itemId);
                        }
                        mantenimientoDocumentoEliminados.put(itemId, mantenimientoDocumento);
                        container.removeItem(itemId);
                        StaticMembers.showNotificationMessage("Notificaci\u00F3n", "El documento se eliminar\u00E1 luego de guardar los cambios");
                    }
                });
            }
        }
        ));
        initExtraHeaderRow();
    }

    public Map<Long, MantenimientoDocumento> getMantenimientoDocumentoModificados() {
        return mantenimientoDocumentoModificados;
    }

    public void setMantenimientoDocumentoModificados(Map<Long, MantenimientoDocumento> mantenimientoDocumentoModificados) {
        this.mantenimientoDocumentoModificados = mantenimientoDocumentoModificados;
    }

    public Map<Long, MantenimientoDocumento> getMantenimientoDocumentoAdicionados() {
        return mantenimientoDocumentoAdicionados;
    }

    public void setMantenimientoDocumentoAdicionados(Map<Long, MantenimientoDocumento> mantenimientoDocumentoAdicionados) {
        this.mantenimientoDocumentoAdicionados = mantenimientoDocumentoAdicionados;
    }

    public Map<Long, MantenimientoDocumento> getMantenimientoDocumentoEliminados() {
        return mantenimientoDocumentoEliminados;
    }

    public void setMantenimientoDocumentoEliminados(Map<Long, MantenimientoDocumento> mantenimientoDocumentoEliminados) {
        this.mantenimientoDocumentoEliminados = mantenimientoDocumentoEliminados;
    }

    private void initDownloadButton() {
        this.downloadButton = new Button("");
        this.downloadButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        this.downloadButton.setId("downloadButtonUniqueId");
        downloadButtonInit = false;
    }

    private void initExtraHeaderRow() {
        HeaderRow firstHeaderRow = this.prependHeaderRow();
        firstHeaderRow.join(/*"tipoDocumentoMantenimiento.descripcion",*/"rutaDocumento", "ver", "descargar", "eliminar");
        buttonLayout = new HorizontalLayout();
        buttonLayout.setSizeFull();
        buttonLayout.setSpacing(true);
        buttonLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        firstHeaderRow.getCell("rutaDocumento").setComponent(buttonLayout);

        Button adicionar = new Button("", (Button.ClickListener) event -> {
            this.adicionaModificarBoton(new MantenimientoDocumento(), true);
        }
        );
        adicionar.setCaption("Adicionar documento");
        adicionar.setIcon(FontAwesome.UPLOAD);
        adicionar.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        adicionar.addStyleName(ValoTheme.BUTTON_TINY);
        buttonLayout.addComponent(adicionar);
        buttonLayout.addComponent(downloadButton);
    }

    public void restablecerListados() {
        mantenimientoDocumentoAdicionados.clear();
        mantenimientoDocumentoModificados.clear();
        mantenimientoDocumentoEliminados.clear();
    }

    private void adicionaModificarBoton(MantenimientoDocumento mantenimientoDocumento, Boolean adicionar) {
        MantenimientoDocumentoForm form = new MantenimientoDocumentoForm(adicionar, idMantenimiento, mantenimientoDocumento, fileDir, urlDir);
        rutaDocumentoPrevia = mantenimientoDocumento.getRutaDocumento();
        rutaDocumentoPrevia = (rutaDocumentoPrevia == null) ? "" : rutaDocumentoPrevia;

        mb = MessageBox.showCustomized(Icon.NONE, "Adicionar Documento", form,
                buttonId -> {
                    if (buttonId == ButtonId.YES) {
                        try {
                            form.getMantenimientoDocumentoBeanFieldGroup().commit();
                            MantenimientoDocumento item = form.getMantenimientoDocumentoBeanFieldGroup().getItemDataSource().getBean();
                            String nombreFichero = form.getNombreFichero();

                            if (nombreFichero.equals("")) {
                                StaticMembers.showNotificationError("Error", "Debe seleccionar un documento");
                            } else {
                                boolean ficheroRepetido = false;
                                Collection<MantenimientoDocumento> docs = mantenimientoDocumentoAdicionados.values();
                                for (MantenimientoDocumento doc : docs) {
                                    if (doc.getRutaDocumento().equals(nombreFichero)) {
                                        ficheroRepetido = true;
                                        break;
                                    }
                                }
                                if (!ficheroRepetido) {
                                    if (adicionar) {
                                        item.setId(id--);
                                        mantenimientoDocumentoAdicionados.put(item.getId(), item);
                                        container.addBean(item);
                                    } else {
                                        mantenimientoDocumentoAdicionados.remove(item.getId());
                                        mantenimientoDocumentoModificados.put(item.getId(), item);
                                        container.removeItem(item.getId());
                                        container.addBean(item);
                                    }
                                    StaticMembers.eliminarFicherosMantenimientoInvalidos(false, form.getNombreFichero(), rutaDocumentoPrevia, fileDir, idMantenimiento, form.getFicherosCargados());
                                    mb.close();
                                } else {
                                    StaticMembers.showNotificationError("Error", "Ya existe un fichero con ese nombre");
                                }
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
                        StaticMembers.eliminarFicherosMantenimientoInvalidos(true, form.getNombreFichero(), rutaDocumentoPrevia, fileDir, idMantenimiento, form.getFicherosCargados());
                        mb.close();
                    }
                }, ButtonId.CANCEL, ButtonId.YES).setWidth("400px").setHeight("230px");
        mb.setModal(true);
        mb.setAutoClose(false);
        mb.getButton(ButtonId.YES).setCaption("Aceptar");
        mb.getButton(ButtonId.CANCEL).setCaption("Cancelar");
    }

    public static class Container extends BeanContainer<Long, MantenimientoDocumento> {

        public Container() {
            super(MantenimientoDocumento.class);
            setBeanIdResolver(mantenimientoDocumento -> mantenimientoDocumento.getId());
        }
    }

}
