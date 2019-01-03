package com.earandap.vehiculos.ui.views.empresas;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.EmpresaDocumento;
import com.earandap.vehiculos.repository.ParametrosRepository;
import com.earandap.vehiculos.repository.TipoDocumentoEmpresaRepository;
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
public class EmpresaDocumentoGrid extends Grid {

    private String fileDir, urlDir, rutaDocumentoPrevia;
    protected Container container;
    private MessageBox mb;
    private Button downloadButton;
    private HorizontalLayout buttonLayout;
    int id;
    File downloadFile;
    FileDownloader fileDownloader;
    public Long idEmpresa;
    public boolean downloadButtonInit;

    @Autowired
    private TipoDocumentoEmpresaRepository tipoDocumentoEmpresaRepository;

    @Autowired
    private ParametrosRepository parametrosRepository;

    private Map<Long, EmpresaDocumento> empresaDocumentoModificados;
    private Map<Long, EmpresaDocumento> empresaDocumentoAdicionados;
    private Map<Long, EmpresaDocumento> empresaDocumentoEliminados;

    @PostConstruct
    public void init() {
        this.container = new Container();
        container.addNestedContainerProperty("tipoDocumentoEmpresa.descripcion");
        Responsive.makeResponsive(this);
        this.setSizeFull();
        this.setHeight("280px");
        this.setSelectionMode(SelectionMode.SINGLE);
        this.fileDir = parametrosRepository.findOne(Long.valueOf("2")).getValorParametro();
        this.urlDir = parametrosRepository.findOne(Long.valueOf("3")).getValorParametro();
        this.rutaDocumentoPrevia = "";

        initDownloadButton();

        id = -1;

        empresaDocumentoAdicionados = new HashMap<Long, EmpresaDocumento>();
        empresaDocumentoModificados = new HashMap<Long, EmpresaDocumento>();
        empresaDocumentoEliminados = new HashMap<Long, EmpresaDocumento>();

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
        this.addColumn("tipoDocumentoEmpresa.descripcion").setHeaderCaption("Tipo").setWidth(213);
        this.addColumn("rutaDocumento").setHeaderCaption("Documento").setWidth(223);

        this.addColumn("ver").setHeaderCaption("").setWidth(67).setRenderer(new ButtonRenderer(new RendererClickListener() {
            @Override
            public void click(RendererClickEvent event) {
                EmpresaDocumento empDoc = container.getItem(event.getItemId()).getBean();
                File fichero = new File(StaticMembers.construirRutaDocumentoEmpresa(fileDir, empDoc.getRutaDocumento(), idEmpresa));
                if (fichero.exists()) {
                    //String urlTest = "http://docs.google.com/gview?url=http://www.15minutemondays.com/wp-content/uploads/2015/02/EmbeddedWordDocExample.doc&embedded=true";
                    String url = "http://docs.google.com/gview?url=" + StaticMembers.construirUrlDocumentoEmpresa(urlDir, empDoc.getRutaDocumento(), idEmpresa) + "&embedded=true";
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
                EmpresaDocumento empDoc = container.getItem(event.getItemId()).getBean();
                File fichero = new File(StaticMembers.construirRutaDocumentoEmpresa(fileDir, empDoc.getRutaDocumento(), idEmpresa));
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
                        EmpresaDocumento empresaDocumento = container.getItem(event.getItemId()).getBean();
                        Long itemId = empresaDocumento.getId();
                        if (empresaDocumentoAdicionados.containsKey(itemId)) {
                            empresaDocumentoAdicionados.remove(itemId);
                        }
                        if (empresaDocumentoModificados.containsKey(itemId)) {
                            empresaDocumentoModificados.remove(itemId);
                        }
                        empresaDocumentoEliminados.put(itemId, empresaDocumento);
                        container.removeItem(itemId);
                        StaticMembers.showNotificationMessage("Notificaci\u00F3n", "El documento se eliminar\u00E1 luego de guardar los cambios");
                    }
                });
            }
        }
        ));
        initExtraHeaderRow();
    }

    public Map<Long, EmpresaDocumento> getEmpresaDocumentoModificados() {
        return empresaDocumentoModificados;
    }

    public void setEmpresaDocumentoModificados(Map<Long, EmpresaDocumento> empresaDocumentoModificados) {
        this.empresaDocumentoModificados = empresaDocumentoModificados;
    }

    public Map<Long, EmpresaDocumento> getEmpresaDocumentoAdicionados() {
        return empresaDocumentoAdicionados;
    }

    public void setEmpresaDocumentoAdicionados(Map<Long, EmpresaDocumento> empresaDocumentoAdicionados) {
        this.empresaDocumentoAdicionados = empresaDocumentoAdicionados;
    }

    public Map<Long, EmpresaDocumento> getEmpresaDocumentoEliminados() {
        return empresaDocumentoEliminados;
    }

    public void setEmpresaDocumentoEliminados(Map<Long, EmpresaDocumento> empresaDocumentoEliminados) {
        this.empresaDocumentoEliminados = empresaDocumentoEliminados;
    }

    private void initDownloadButton() {
        this.downloadButton = new Button("");
        this.downloadButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        this.downloadButton.setId("downloadButtonUniqueId");
        downloadButtonInit = false;
    }

    private void initExtraHeaderRow() {
        HeaderRow firstHeaderRow = this.prependHeaderRow();
        firstHeaderRow.join("tipoDocumentoEmpresa.descripcion", "rutaDocumento", "ver", "descargar", "eliminar");
        buttonLayout = new HorizontalLayout();
        buttonLayout.setSizeFull();
        buttonLayout.setSpacing(true);
        buttonLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        firstHeaderRow.getCell("tipoDocumentoEmpresa.descripcion").setComponent(buttonLayout);

        Button adicionar = new Button("", (Button.ClickListener) event -> {
            this.adicionaModificarBoton(new EmpresaDocumento(), true);
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
        empresaDocumentoAdicionados.clear();
        empresaDocumentoModificados.clear();
        empresaDocumentoEliminados.clear();
    }

    private void adicionaModificarBoton(EmpresaDocumento empresaDocumento, Boolean adicionar) {
        EmpresaDocumentoForm form = new EmpresaDocumentoForm(adicionar, idEmpresa, empresaDocumento, tipoDocumentoEmpresaRepository.findAll(), fileDir, urlDir);
        form.setTipoDocumentoEmpresaRepository(tipoDocumentoEmpresaRepository);
        rutaDocumentoPrevia = empresaDocumento.getRutaDocumento();
        rutaDocumentoPrevia = (rutaDocumentoPrevia == null) ? "" : rutaDocumentoPrevia;

        mb = MessageBox.showCustomized(Icon.NONE, "Adicionar Documento", form,
                buttonId -> {
                    if (buttonId == ButtonId.YES) {
                        try {
                            form.getEmpresaDocumentoBeanFieldGroup().commit();
                            EmpresaDocumento item = form.getEmpresaDocumentoBeanFieldGroup().getItemDataSource().getBean();

                            if (form.getNombreFichero().equals("")) {
                                StaticMembers.showNotificationError("Error", "Debe seleccionar un documento");
                            } else {
                                if (adicionar) {
                                    item.setId(id--);
                                    empresaDocumentoAdicionados.put(item.getId(), item);
                                    container.addBean(item);
                                } else {
                                    empresaDocumentoAdicionados.remove(item.getId());
                                    empresaDocumentoModificados.put(item.getId(), item);
                                    container.removeItem(item.getId());
                                    container.addBean(item);
                                }
                                StaticMembers.eliminarFicherosEmpresaInvalidos(false, form.getNombreFichero(), rutaDocumentoPrevia, fileDir, idEmpresa, form.getFicherosCargados());
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
                        StaticMembers.eliminarFicherosEmpresaInvalidos(true, form.getNombreFichero(), rutaDocumentoPrevia, fileDir, idEmpresa, form.getFicherosCargados());
                        mb.close();
                    }
                }, ButtonId.CANCEL, ButtonId.YES).setWidth("500px").setHeight("300px");
        mb.setModal(true);
        mb.setAutoClose(false);
        mb.getButton(ButtonId.YES).setCaption("Aceptar");
        mb.getButton(ButtonId.CANCEL).setCaption("Cancelar");
        mb.setWidth(500, Unit.PIXELS);
        mb.setHeight(250, Unit.PIXELS);
    }

    public static class Container extends BeanContainer<Long, EmpresaDocumento> {

        public Container() {
            super(EmpresaDocumento.class);
            setBeanIdResolver(empresaDocumento -> empresaDocumento.getId());
        }
    }

}
