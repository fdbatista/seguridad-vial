package com.earandap.vehiculos.ui.views.perfil;

import com.earandap.vehiculos.domain.Modulo;
import com.earandap.vehiculos.domain.Perfil;
import com.earandap.vehiculos.domain.PerfilRecurso;
import com.earandap.vehiculos.domain.SubModulo;
import com.earandap.vehiculos.repository.ModuloRepository;
import com.earandap.vehiculos.repository.PerfilRecursoRepository;
import com.earandap.vehiculos.repository.RecursoRespository;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import com.vaadin.ui.TreeTable;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by Angel Luis on 11/12/2015.
 */
@SpringComponent
@UIScope
public class PerfilRecursoTable extends TreeTable {

    protected Container container;

    private Perfil perfil;

    private int moduloCantidad;

    private int subModuloCantidad;

    @Autowired
    private ModuloRepository moduloRepository;

    @Autowired
    private PerfilRecursoRepository perfilRecursoRepository;

    @Autowired
    private RecursoRespository recursoRespository;

    @PostConstruct
    public void init(){

        this.setWidth(720,Unit.PIXELS);
        this.setHeight(320,Unit.PIXELS);

        this.addContainerProperty("Recurso", String.class, null);
        this.addContainerProperty("Consultar", Boolean.class, false);
        this.addContainerProperty("Crear", Boolean.class, false);
        this.addContainerProperty("Modificar", Boolean.class, false);
        this.addContainerProperty("Eliminar", Boolean.class, false);

        this.setConverter("Consultar", new MiConverter());
        this.setConverter("Crear", new MiConverter());
        this.setConverter("Modificar", new MiConverter());
        this.setConverter("Eliminar", new MiConverter());

        this.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                //if (!event.isDoubleClick())
                    if(getContainerProperty(event.getItemId(), event.getPropertyId()).getValue() instanceof Boolean)
                    getContainerProperty(event.getItemId(), event.getPropertyId()).setValue(!(boolean)getContainerProperty(event.getItemId(), event.getPropertyId()).getValue());
            }
        });

    }

    public void construirTabla(Perfil perfil){

        this.perfil = perfil;
        List<Modulo> modulos = moduloRepository.findAll();
        moduloCantidad = 0;
        int recursos = 0;

        this.removeAllItems();

        for (Modulo modulo: modulos){
            List<PerfilRecurso> perfilRecursos = new ArrayList<>();
            if (perfil.getId() != 0)
            perfilRecursos= perfilRecursoRepository.findAllByModulo(perfil,modulo);
            Set<SubModulo> subModulos = modulo.getSubModulos();
            this.addItem(new Object[]{modulo.getNombre(), null, null, null, null}, recursos++);
            moduloCantidad = recursos;
            if (subModulos.isEmpty())
                moduloCantidad++;

            for (SubModulo subModulo: subModulos){
                boolean consultar = false, crear=false, modificar=false, eliminar=false;
                PerfilRecurso perfilRecurso = buscarPerfilRecurso(perfilRecursos,subModulo);
                if (perfilRecurso != null){
                    consultar = perfilRecurso.isConsultar();
                    crear = perfilRecurso.isCrear();
                    modificar = perfilRecurso.isModificar();
                    eliminar = perfilRecurso.isEliminar();
                }
                this.addItem(new Object[]{subModulo.getNombre(), consultar, crear, modificar, eliminar}, recursos++);
                this.setParent(recursos-1, moduloCantidad-1);
            }
        }

        //Expand the tree
        for (Object itemId: this.getContainerDataSource()
                .getItemIds()) {
            this.setCollapsed(itemId, false);

            // Also disallow expanding leaves
           // if (! this.hasChildren(itemId))
              //  this.setChildrenAllowed(itemId, false);
        }
    }

    private PerfilRecurso buscarPerfilRecurso(List<PerfilRecurso> perfilRecursos,SubModulo subModulo) {
        for (PerfilRecurso perfilRecurso : perfilRecursos)
            if (perfilRecurso.getSubModulo().getNombre().equals(subModulo.getNombre()))
                return perfilRecurso;
        return null;
    }

  /* public PerfilRecursoTable(){
        //this.setCaption("Gestionar Recursos");
        this.addContainerProperty("Recurso", String.class, null);
        this.addContainerProperty("Consultar", Boolean.class, false);
        this.addContainerProperty("Crear", Boolean.class, false);
        this.addContainerProperty("Modificar", Boolean.class, false);
        this.addContainerProperty("Eliminar", Boolean.class, false);


        //this.addContainerProperty("Acciones", new GeneratedPropertyContainer(), false);

       /* this.addGeneratedColumn("Consultar", new BooleanColumnGenerator());
        this.addGeneratedColumn("Crear", new BooleanColumnGenerator());
        this.addGeneratedColumn("Modificar", new BooleanColumnGenerator());
        this.addGeneratedColumn("Eliminar", new BooleanColumnGenerator());*/



        /*this.addItem(new Object[]{"Sistema", null, null, null, null}, 0);
        this.addItem(new Object[]{"Administración", null, null, null, null}, 1);
        this.setParent(1, 0);
        this.addItem(new Object[]{"Gestionar Usuarios", true, false, true, true}, 2);
        this.setParent(2, 1);
        this.addItem(new Object[]{"Seguridad Vial", null, null, null, null}, 3);
        this.setParent(3, 0);

        this.setSelectable(true);
       // this.getContainerProperty(2,"Eliminar").setValue(false);
      // this.addItem(new Object[]{"Gestionar Usuarios", false, false, false, false}, 2);
       // this.setParent(2, 1);




        // Expand the tree
        for (Object itemId: this.getContainerDataSource()
                .getItemIds()) {
            this.setCollapsed(itemId, false);

            // Also disallow expanding leaves
            if (! this.hasChildren(itemId))
                this.setChildrenAllowed(itemId, false);
        }

        //this.setEditable(true);


        // Limit the size
        this.setPageLength(this.getContainerDataSource().size());
        this.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                if (!event.isDoubleClick())
                    getContainerProperty(2, "Eliminar").setValue(false);
                    return;
                // do something in here
            }
        });
    }*/

    public void setPerfilRecurso(Perfil perfil){
        this.addItem(new Object[]{"Sistema", null, null, null, null}, 0);
    }

    public List<PerfilRecurso> obtenerPermisos() {
        List<Modulo> modulos = moduloRepository.findAll();
        List<PerfilRecurso> perfilRecursos = new ArrayList<>();
        for (Modulo modulo: modulos){
            Set<SubModulo> subModulos = modulo.getSubModulos();
            for (SubModulo subModulo: subModulos){
                boolean consultar = false, crear=false, modificar=false, eliminar=false;
                PerfilRecurso perfilRecurso = new PerfilRecurso();
                Collection<Integer> ids = (Collection<Integer>) this.getItemIds();
                for (Integer id: ids){
                    if(this.getContainerProperty(id,"Recurso").getValue().equals(subModulo.getNombre())){
                        perfilRecurso.setConsultar((Boolean) this.getContainerProperty(id,"Consultar").getValue());
                        perfilRecurso.setCrear((Boolean) this.getContainerProperty(id,"Crear").getValue());
                        perfilRecurso.setModificar((Boolean) this.getContainerProperty(id,"Modificar").getValue());
                        perfilRecurso.setEliminar((Boolean) this.getContainerProperty(id,"Eliminar").getValue());
                        perfilRecurso.setSubModulo(subModulo);
                        perfilRecurso.setRecurso(recursoRespository.findByCodigo((String) this.getContainerProperty(id,"Recurso").getValue()));
                    }
                }
                perfilRecursos.add(perfilRecurso);
            }
        }
        return perfilRecursos;
    }

    public static class Container extends BeanContainer<PerfilRecurso.Id, PerfilRecurso> {
        public Container() {
            super(PerfilRecurso.class);
            setBeanIdResolver(perfilRecurso -> perfilRecurso.getId());
        }
    }

    /** Formats the dates in a column containing Date objects. */
    class BooleanColumnGenerator implements Table.ColumnGenerator {
        /**
         * Generates the cell containing the Date value. The column is
         * irrelevant in this use case.
         */
        public Component generateCell(Table source, Object itemId,
                                      Object columnId) {
            Property prop = source.getItem(itemId).getItemProperty(columnId);
            if (prop.getType().equals(Boolean.class)) {
                CheckBox label = new CheckBox();
                label.setValue((Boolean) prop.getValue());
                label.addStyleName("column-type-date");
                return label;
            }

            return null;
        }
    }

    private class MiConverter implements Converter<String, Boolean> {

        @Override
        public Boolean convertToModel(String value, Class<? extends Boolean> targetType, Locale locale) throws ConversionException {
            return value.equals("Si")?true:false;
        }

        @Override
        public String convertToPresentation(Boolean value, Class<? extends String> targetType, Locale locale) throws ConversionException {
            if (value == null)
                return null;
            return value?"Si":"No";
        }

        @Override
        public Class<Boolean> getModelType() {
            return Boolean.class;
        }

        @Override
        public Class<String> getPresentationType() {
            return String.class;
        }
    }


}
