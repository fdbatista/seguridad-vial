package com.earandap.vehiculos.ui.views;

import com.earandap.vehiculos.domain.PerfilRecurso;
import com.earandap.vehiculos.domain.Usuario;
import com.earandap.vehiculos.ui.MainUI;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.vaadin.spring.security.VaadinSecurity;

import java.util.Set;

/**
 * @Autor Eduardo Aranda
 * @Version 0.1
 */
public abstract class SecureView extends VerticalLayout implements View {

    private static Logger logger = LoggerFactory.getLogger(SecureView.class);

    @Autowired
    private VaadinSecurity security;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        Authentication auth = security.getAuthentication();
        Set<PerfilRecurso> permisos = ((Usuario) auth.getPrincipal()).getPerfil().getPerfilRecursos();
        boolean tienePermiso = false;
        String nombreRecurso;
        for (PerfilRecurso perfilRecurso : permisos) {
            nombreRecurso = perfilRecurso.getId().getRecurso().getCodigo();
            if (nombreRecurso.equals(getViewName())) {
                if (perfilRecurso.isConsultar())
                    tienePermiso = true;
                break;
            }
        }
        if (getViewName().equals("contrasena")) {
            tienePermiso = true;
        }
        if (!tienePermiso) {
            MainUI ui = (MainUI) UI.getCurrent();
            ui.getNavigator().navigateTo("accessDeniedView");
        }
    }

    public abstract String getViewName();

    public boolean tienePermisoConsultar(String vista) {
        return true;
    }
}
