package com.earandap.vehiculos.ui.views.empresas;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.Empresa;
import com.earandap.vehiculos.domain.EmpresaDocumento;
import com.earandap.vehiculos.domain.MiembroComite;
import com.earandap.vehiculos.domain.PerfilRecurso;
import com.earandap.vehiculos.domain.Usuario;
import com.earandap.vehiculos.domain.nomenclador.TipoDocumento;
import com.earandap.vehiculos.repository.EmpresaDocumentoRepository;
import com.earandap.vehiculos.repository.EmpresaRepository;
import com.earandap.vehiculos.repository.MiembroComiteRepository;
import com.earandap.vehiculos.repository.ParametrosRepository;
import com.earandap.vehiculos.ui.MainUI;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.vaadin.spring.events.EventBus;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by Angel Luis on 10/5/2015.
 */
@SpringComponent
@UIScope
public class EmpresaFormWindow extends Window {

    private BeanFieldGroup<Empresa> empresaBeanFieldGroup;
    private Label miembrosComiteField, empresaDocumentosField;
    private String fileDir;

    @Autowired
    private MiembroComiteGrid miembrosComiteGrid;

    @Autowired
    private EmpresaDocumentoGrid empresaDocumentosGrid;

    @Autowired
    private MiembroComiteRepository miembroComiteRepository;

    @Autowired
    private EmpresaDocumentoRepository empresaDocumentoRepository;

    @Autowired
    private EventBus.ApplicationEventBus applicationEventBus;

    //tab1
    private VerticalLayout content;

    @PropertyId("documento")
    private TextField documentoField;

    private Button buscarTerceroButton;

    @PropertyId("razonSocial")
    private TextField razonSocialField;

    private TextField celularField;

    @PropertyId("telefono")
    private TextField telefonoFijoField;

    @PropertyId("direccion")
    private TextField direccionEmpresaField;

    @PropertyId("correo")
    private TextField correoField;

    @PropertyId("observacion")
    private TextArea observacionesField;

    @PropertyId("inactivo")
    private CheckBox inactivoField;

    @Autowired
    private EmpresaRepository empresaRepository;
    
    @Autowired
    private ParametrosRepository parametrosRepository;

    private boolean modificar = false;

    TabSheet tabs;

    public boolean isModificar() {
        return modificar;
    }

    public void setModificar(boolean modificar) {
        this.modificar = modificar;
    }

    @PostConstruct
    public void init() {
        setWidth(700, Unit.PIXELS);
        setHeight(550, Unit.PIXELS);
        setModal(true);
        setCloseShortcut(ShortcutAction.KeyCode.ESCAPE, null);
        setResizable(false);
        setClosable(false);
        
        this.fileDir = parametrosRepository.findOne(Long.valueOf("2")).getValorParametro();

        Responsive.makeResponsive(this);

        content = new VerticalLayout();
        content.setSizeFull();
        content.setMargin(true);

        tabs = new TabSheet();
        content.addComponent(tabs);
        content.setExpandRatio(tabs, 1);

        tabs.addTab(buildInfoEmpresa(), "Informaci\u00F3n Empresa");
        tabs.addTab(buildPlanEstrategico(), "Plan Estrat\u00E9gico");
        tabs.addTab(buildDocumentos(), "Documentos del Plan");

        com.vaadin.ui.Component footer = buildFooter();
        content.addComponent(footer);
        content.setComponentAlignment(footer, Alignment.BOTTOM_CENTER);

        empresaBeanFieldGroup = new BeanFieldGroup<>(Empresa.class);
        empresaBeanFieldGroup.bindMemberFields(this);

        this.setContent(content);
    }

    private com.vaadin.ui.Component buildInfoEmpresa() {
        FormLayout infoEmpresaForm = new FormLayout();
        infoEmpresaForm.setSpacing(true);
        infoEmpresaForm.setResponsive(true);

        HorizontalLayout l1 = new HorizontalLayout();
        l1.setSpacing(true);
        infoEmpresaForm.addComponent(l1);

        documentoField = new TextField("NIT");
        documentoField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        documentoField.setWidth(25, Unit.EM);
        documentoField.setNullRepresentation("");
        documentoField.setRequired(true);
        documentoField.setMaxLength(30);
        documentoField.setRequiredError("El NIT de la empresa es requerido");

        l1.addComponent(documentoField);

        HorizontalLayout l3 = new HorizontalLayout();
        l3.setSpacing(true);
        infoEmpresaForm.addComponent(l3);

        razonSocialField = new TextField("Raz\u00F3n Social");
        razonSocialField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        razonSocialField.setWidth(35, Unit.EM);
        razonSocialField.setNullRepresentation("");
        razonSocialField.setRequired(true);
        razonSocialField.setRequiredError("La Raz\u00F3n Social de la empresa es requerida");
        razonSocialField.setMaxLength(200);
        l3.addComponent(razonSocialField);

        HorizontalLayout l2 = new HorizontalLayout();
        l2.setSpacing(true);
        infoEmpresaForm.addComponent(l2);

        telefonoFijoField = new TextField("Tel\u00E9fono Fijo");
        telefonoFijoField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        telefonoFijoField.setWidth(9, Unit.EM);
        telefonoFijoField.setNullRepresentation("");
        //telefonoFijoField.addValidator(new RegexpValidator(ssn_regexp, "Tel\u00E9fono Incorrecto"));
        telefonoFijoField.setMaxLength(50);

        l2.addComponent(telefonoFijoField);

        correoField = new TextField("Correo");
        correoField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        correoField.setWidth(15, Unit.EM);
        correoField.setNullRepresentation("");
        correoField.setMaxLength(150);
        l2.addComponent(correoField);

        HorizontalLayout l4 = new HorizontalLayout();
        l4.setSpacing(true);
        infoEmpresaForm.addComponent(l4);

        direccionEmpresaField = new TextField("Direcci\u00F3n Empresa");
        direccionEmpresaField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        direccionEmpresaField.setWidth(35, Unit.EM);
        direccionEmpresaField.setNullRepresentation("");
        direccionEmpresaField.setMaxLength(100);
        l4.addComponent(direccionEmpresaField);

        HorizontalLayout l5 = new HorizontalLayout();
        l5.setSpacing(true);
        infoEmpresaForm.addComponent(l5);

        observacionesField = new TextArea("Observaciones");
        observacionesField.addStyleName(ValoTheme.TEXTAREA_TINY);
        observacionesField.setNullRepresentation("");
        observacionesField.setColumns(35);
        observacionesField.setRows(2);
        l5.addComponent(observacionesField);

        HorizontalLayout l6 = new HorizontalLayout();
        l6.setSpacing(true);
        infoEmpresaForm.addComponent(l6);

        inactivoField = new CheckBox("Inactivo");
        inactivoField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        inactivoField.setWidth(9, Unit.EM);
        l6.addComponent(inactivoField);

        return infoEmpresaForm;
    }

    private Component buildPlanEstrategico() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(new MarginInfo(true, false, true, false));
        layout.setSpacing(true);

        //line 1
        HorizontalLayout l1 = new HorizontalLayout();
        layout.addComponent(l1);
        l1.setSpacing(true);

        miembrosComiteField = new Label("Miembros del Comit\u00E9");
        miembrosComiteField.addStyleName(ValoTheme.LABEL_BOLD);
        miembrosComiteField.setWidth(12, Unit.EM);
        l1.addComponent(miembrosComiteField);

        layout.addComponent(miembrosComiteGrid);

        return layout;
    }

    private Component buildDocumentos() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(new MarginInfo(true, false, true, false));
        layout.setSpacing(true);

        //line 1
        HorizontalLayout l1 = new HorizontalLayout();
        layout.addComponent(l1);
        l1.setSpacing(true);

        empresaDocumentosField = new Label("Documentos");
        empresaDocumentosField.addStyleName(ValoTheme.LABEL_BOLD);
        empresaDocumentosField.setWidth(12, Unit.EM);
        l1.addComponent(empresaDocumentosField);

        layout.addComponent(empresaDocumentosGrid);

        return layout;
    }

    private void limpiarFieldGroup() {
        empresaBeanFieldGroup.setItemDataSource(new Empresa());
    }

    private com.vaadin.ui.Component buildFooter() {
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
            Empresa empresaAux = empresaBeanFieldGroup.getItemDataSource().getBean();
            for (PerfilRecurso perfilRecurso : permisos) {
                if (perfilRecurso.getId().getRecurso().getCodigo().equals(EmpresaView.VIEW_NAME)) {
                    if ((perfilRecurso.isCrear() && empresaAux.getId() == 0) || (perfilRecurso.isModificar() && empresaAux.getId() != 0)) {
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
                    empresaBeanFieldGroup.commit();
                    Empresa empresa = empresaBeanFieldGroup.getItemDataSource().getBean();
                    if (!modificar) {
                        if (empresaRepository.searchEmpresa(empresa.getDocumento()) == null) {
                            TipoDocumento tipoDocumento = new TipoDocumento();
                            tipoDocumento.setId(19);
                            empresa.setTipoDocumento(tipoDocumento);
                        } else {
                            throw new FieldGroup.CommitException(new Validator.InvalidValueException("Ya existe la empresa con ese SID"));
                        }
                    }
                    empresaRepository.save(empresa);

                    //Guardar miembros del comite
                    Map<Long, MiembroComite> miembroComiteAdicionados = miembrosComiteGrid.getMiembroComiteAdicionados();
                    for (Map.Entry<Long, MiembroComite> miembroComiteMap : miembroComiteAdicionados.entrySet()) {
                        MiembroComite miembroComite = miembroComiteMap.getValue();
                        miembroComite.setEmpresa(empresa);
                        miembroComiteRepository.save(miembroComite);
                    }
                    Map<Long, MiembroComite> miembroComiteModificados = miembrosComiteGrid.getMiembroComiteModificados();
                    for (Map.Entry<Long, MiembroComite> miembroComiteMap : miembroComiteModificados.entrySet()) {
                        MiembroComite miembroComite = miembroComiteMap.getValue();
                        miembroComite.setEmpresa(empresa);
                        miembroComiteRepository.save(miembroComite);
                    }
                    Map<Long, MiembroComite> miembroComiteEliminados = miembrosComiteGrid.getMiembroComiteEliminados();
                    for (Map.Entry<Long, MiembroComite> miembroComiteMap : miembroComiteEliminados.entrySet()) {
                        MiembroComite miembroComite = miembroComiteMap.getValue();
                        miembroComiteRepository.delete(miembroComite);
                        //empresa.getMiembrosComite().remove(miembroComite);
                    }
                    /*Long idResponsable = miembrosComiteGrid.getIdResponsable();
                    if (!idResponsable.equals(-1))
                    {
                        for (MiembroComite miembro : empresa.getMiembrosComite()) {
                            if (miembro.getId() != idResponsable)
                            {
                                miembro.setEsResponsable(false);
                                miembroComiteRepository.save(miembro);
                            }
                        }
                    }*/
                    miembrosComiteGrid.restablecerListados();

                    //Guardar documentos
                    Map<Long, EmpresaDocumento> empresaDocumentoAdicionados = empresaDocumentosGrid.getEmpresaDocumentoAdicionados();
                    for (Map.Entry<Long, EmpresaDocumento> empresaDocumentoMap : empresaDocumentoAdicionados.entrySet()) {
                        EmpresaDocumento empresaDocumento = empresaDocumentoMap.getValue();
                        empresaDocumento.setEmpresa(empresa);
                        empresaDocumentoRepository.save(empresaDocumento);
                    }
                    Map<Long, EmpresaDocumento> empresaDocumentoModficados = empresaDocumentosGrid.getEmpresaDocumentoModificados();
                    for (Map.Entry<Long, EmpresaDocumento> empresaDocumentoMap : empresaDocumentoModficados.entrySet()) {
                        EmpresaDocumento empresaDocumento = empresaDocumentoMap.getValue();
                        empresaDocumento.setEmpresa(empresa);
                        empresaDocumentoRepository.save(empresaDocumento);
                    }
                    Map<Long, EmpresaDocumento> empresaDocumentoEliminados = empresaDocumentosGrid.getEmpresaDocumentoEliminados();
                    for (Map.Entry<Long, EmpresaDocumento> empresaDocumentoMap : empresaDocumentoEliminados.entrySet()) {
                        EmpresaDocumento empresaDocumento = empresaDocumentoMap.getValue();
                        empresaDocumentoRepository.delete(empresaDocumento);
                        StaticMembers.eliminarFichero(new File(StaticMembers.construirRutaDocumentoEmpresa(fileDir, empresaDocumento.getRutaDocumento(), empresa.getId())));
                    }
                    empresaDocumentosGrid.restablecerListados();

                    applicationEventBus.publish(EmpresaGrid.NUEVA_EMPRESA, this, empresa);
                    StaticMembers.showNotificationMessage("Notificaci\u00F3n", "Empresa guardada con \u00E9xito");
                    close();

                } catch (FieldGroup.CommitException e) {
                    if ((e instanceof FieldGroup.CommitException && e.getCause() instanceof Validator.InvalidValueException || e.getCause() instanceof Validator.EmptyValueException)) {
                        Collection<Validator.InvalidValueException> values = e.getInvalidFields().values();
                        if (!values.isEmpty()) {
                            StaticMembers.showNotificationError("Error", "Datos incorrectos");
                        } else if (e.getCause() instanceof Validator.InvalidValueException) {
                            Notification.show(e.getCause().getMessage(), Notification.Type.TRAY_NOTIFICATION);
                        }
                    } else {
                        StaticMembers.showNotificationError("Error", e.getMessage());
                    }
                }
            }
        });

        footer.setExpandRatio(guardar, 1);
        Button cancelar = new Button("Cancelar");
        cancelar.setStyleName(ValoTheme.BUTTON_PRIMARY);
        footer.addComponent(cancelar);
        footer.setComponentAlignment(cancelar, Alignment.BOTTOM_RIGHT);
        cancelar.addClickListener(event -> {
            this.limpiarFieldGroup();
            this.close();
        });
        Responsive.makeResponsive(footer);
        return footer;
    }
    
    private Button construirBoton(String caption, FontAwesome icon) {
        Button res = new Button("");
        res.setIcon(icon);
        res.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        res.addStyleName(ValoTheme.BUTTON_TINY);
        return res;
    }

    public Window show(long ID) {
        Empresa empresa = empresaRepository.findOne(ID);
        miembrosComiteGrid.container.removeAllItems();
        empresaDocumentosGrid.container.removeAllItems();
        
        if (empresa == null) {
            empresa = new Empresa();
            this.setModificar(false);
            tabs.getTab(2).setVisible(false);
        } else {
            this.setModificar(true);
            tabs.getTab(2).setVisible(true);
            miembrosComiteGrid.container.addAll(empresa.getMiembrosComite());
            empresaDocumentosGrid.container.addAll(empresa.getDocumentosEmpresa());
            documentoField.setEnabled(false);
        }
        empresaDocumentosGrid.idEmpresa = ID;
        empresaBeanFieldGroup.setItemDataSource(empresa);

        return this;
    }
}
