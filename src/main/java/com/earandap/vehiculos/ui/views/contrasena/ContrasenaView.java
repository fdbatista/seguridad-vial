package com.earandap.vehiculos.ui.views.contrasena;

import com.earandap.vehiculos.components.StaticMembers;
import com.earandap.vehiculos.domain.Usuario;
import com.earandap.vehiculos.repository.UsuarioRepository;
import com.earandap.vehiculos.ui.MainUI;
import com.earandap.vehiculos.ui.components.PortletView;
import com.earandap.vehiculos.ui.views.SecureView;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;

/**
 * Created by angel on 18/07/16.
 */
@SpringView(name = ContrasenaView.VIEW_NAME)
public class ContrasenaView extends SecureView {

    public static final String VIEW_NAME = "contrasena";

    private PortletView content;

    private MenuBar bar;

    private PasswordField contrasenaAnteriorField;
    private PasswordField contrasenaNuevaField;
    private PasswordField contrasenaNuevaConfirmarField;

    private Button guardar;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostConstruct
    public void init() {
        MainUI ui = (MainUI) UI.getCurrent();
        ui.getRoot().closeUserMenu();

        bar = new MenuBar();
        bar.setVisible(false);
        bar.addItem("", FontAwesome.PLUS_SQUARE, menuItem -> {

        });
        content = new PortletView("Cambiar Contrasena", FontAwesome.ADN, bar);
        content.setPrimaryStyleName("portlet-view");
        content.setSizeFull();

        content.addComponent(this.buildContrasena());
        addComponent(content);

    }

    private Component buildContrasena() {

        FormLayout contratoForm = new FormLayout();
        contratoForm.setSpacing(true);
        contratoForm.setResponsive(true);
        contratoForm.setEnabled(true);

        //line 1
        HorizontalLayout l1 = new HorizontalLayout();
        l1.setSpacing(true);
        contratoForm.addComponent(l1);

        contrasenaAnteriorField = new PasswordField("Contrase\u00F1a Anterior: ");
        contrasenaAnteriorField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        contrasenaAnteriorField.setWidth(18, Unit.EM);
        contrasenaAnteriorField.setValidationVisible(false);
        contrasenaAnteriorField.setNullRepresentation("");
        contrasenaAnteriorField.setRequired(true);
        contrasenaAnteriorField.setMaxLength(20);
        contrasenaAnteriorField.setRequiredError("La contrase\u00F1a anterior es requerida");
        l1.addComponent(contrasenaAnteriorField);

        //line 2
        HorizontalLayout l2 = new HorizontalLayout();
        l2.setSpacing(true);
        contratoForm.addComponent(l2);

        contrasenaNuevaField = new PasswordField("Nueva Contrase\u00F1a");
        contrasenaNuevaField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        contrasenaNuevaField.setWidth(18, Unit.EM);
        contrasenaNuevaField.setValidationVisible(false);
        contrasenaNuevaField.setNullRepresentation("");
        contrasenaNuevaField.setRequired(true);
        contrasenaNuevaField.setMaxLength(20);
        contrasenaNuevaField.setRequiredError("La nueva contrase\u00F1a es requerida");
        l2.addComponent(contrasenaNuevaField);

        //line 3
        HorizontalLayout l3 = new HorizontalLayout();
        l3.setSpacing(true);
        contratoForm.addComponent(l3);

        contrasenaNuevaConfirmarField = new PasswordField("Confirmar Contrase\u00F1a");
        contrasenaNuevaConfirmarField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        contrasenaNuevaConfirmarField.setWidth(18, Unit.EM);
        contrasenaNuevaConfirmarField.setValidationVisible(false);
        contrasenaNuevaConfirmarField.setNullRepresentation("");
        contrasenaNuevaConfirmarField.setRequired(true);
        contrasenaNuevaConfirmarField.setMaxLength(20);
        contrasenaNuevaConfirmarField.setRequiredError("La confirmaci\u00F3n es requerida");
        l3.addComponent(contrasenaNuevaConfirmarField);

        //line 3
        HorizontalLayout l4 = new HorizontalLayout();
        l4.setSpacing(true);
        contratoForm.addComponent(l4);

        guardar = new Button("Cambiar Contraseña");
        guardar.addStyleName(ValoTheme.BUTTON_PRIMARY);
        guardar.setWidth(18, Unit.EM);
        l4.addComponent(guardar);
        l4.setComponentAlignment(guardar, Alignment.BOTTOM_RIGHT);
        guardar.addClickListener(event -> {
            try {
                MainUI ui = (MainUI) UI.getCurrent();
                org.springframework.security.core.Authentication auth = ui.getSecurity().getAuthentication();
                Usuario usuario = ((Usuario) auth.getPrincipal());

                if (!BCrypt.checkpw(contrasenaAnteriorField.getValue(), usuario.getContrasena())) {
                    StaticMembers.showNotificationError("Error", "La contrase\u00F1a anterior es incorrecta");
                    contrasenaAnteriorField.focus();
                    contrasenaAnteriorField.selectAll();
                } else if (contrasenaNuevaField.getValue().isEmpty()) {
                    StaticMembers.showNotificationError("Error", "La nueva contrase\u00F1a es requerida");
                    contrasenaNuevaField.focus();
                    contrasenaNuevaField.selectAll();
                } else if (contrasenaNuevaConfirmarField.getValue().isEmpty() || !contrasenaNuevaField.getValue().equals(contrasenaNuevaConfirmarField.getValue())) {
                    StaticMembers.showNotificationError("Error", "La nueva contrase\u00F1a es incorrecta");
                    contrasenaNuevaConfirmarField.focus();
                    contrasenaNuevaConfirmarField.selectAll();
                } else {
                    String nuevaContrasena = passwordEncoder.encode(contrasenaNuevaField.getValue());
                    usuario.setContrasena(nuevaContrasena);
                    usuarioRepository.save(usuario);
                    contrasenaAnteriorField.clear();
                    contrasenaNuevaField.clear();
                    contrasenaNuevaConfirmarField.clear();
                    StaticMembers.showNotificationMessage("", "Su contrase\u00F1a ha sido actualizada");
                }
            } catch (Exception e) {
                Notification.show(e.getMessage(), Notification.Type.TRAY_NOTIFICATION);
            }
        });

        return contratoForm;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        super.enter(viewChangeEvent);
    }

    @Override
    public String getViewName() {
        return VIEW_NAME;
    }
}
