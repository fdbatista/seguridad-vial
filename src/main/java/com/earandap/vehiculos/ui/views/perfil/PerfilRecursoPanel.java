package com.earandap.vehiculos.ui.views.perfil;

import com.earandap.vehiculos.domain.Modulo;
import com.earandap.vehiculos.domain.PerfilRecurso;
import com.earandap.vehiculos.domain.SubModulo;
import com.earandap.vehiculos.repository.ModuloRepository;
import com.earandap.vehiculos.repository.PerfilRecursoRepository;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

/**
 * Created by Angel Luis on 11/12/2015.
 */
@SpringComponent
@UIScope
public class PerfilRecursoPanel extends Panel {

    private List<PerfilRecurso> perfilRecursos;

    @Autowired
    private ModuloRepository moduloRepository;

    @Autowired
    private PerfilRecursoRepository perfilRecursoRepository;

    @PostConstruct
    public void init(){

        this.setCaption("Recursos");
        this.setWidth(720,Unit.PIXELS);
        this.setHeight(300,Unit.PIXELS);
        this.setResponsive(true);

        List<Modulo> modulos = moduloRepository.findAll();

        FormLayout content = new FormLayout();
        HorizontalLayout l = new HorizontalLayout();
        l.addComponent(new Label("Todos"));
        l.addComponent(new Label("Consultar"));
        l.addComponent(new Label("Crear"));
        l.addComponent(new Label("Modificar"));
        l.addComponent(new Label("Eliminar"));
        content.addComponent(l);
        for (Modulo modulo: modulos){
            HorizontalLayout moduloHorizontalLayout = new HorizontalLayout();
            Label moduloLabel = new Label(modulo.getNombre());
            moduloHorizontalLayout.addComponent(moduloLabel);
            Set<SubModulo> subModulos = modulo.getSubModulos();
            content.addComponent(moduloHorizontalLayout);
            for (SubModulo subModulo: subModulos) {
                HorizontalLayout subModuloHorizontalLayout = new HorizontalLayout();
                subModuloHorizontalLayout.addComponent(new Label(subModulo.getNombre()));
                subModuloHorizontalLayout.addComponent(moduloLabel);
                subModuloHorizontalLayout.addComponent(new CheckBox());
                subModuloHorizontalLayout.addComponent(new CheckBox());
                subModuloHorizontalLayout.addComponent(new CheckBox());
                subModuloHorizontalLayout.addComponent(new CheckBox());
                moduloHorizontalLayout.addComponent(subModuloHorizontalLayout);
            }

        }
        this.setContent(content);

        /*FormLayout content = new FormLayout();

        HorizontalLayout l1 = new HorizontalLayout();
        Label moduloLabel = new Label("Administración");
        l1.addComponent(moduloLabel);

        HorizontalLayout l2 = new HorizontalLayout();
        CheckBox checkBox = new CheckBox("Consultar");
        l2.addComponent(checkBox);

        l1.addComponent(l2);

        content.addComponent(l1);

        this.setContent(content);*/

    }
}
