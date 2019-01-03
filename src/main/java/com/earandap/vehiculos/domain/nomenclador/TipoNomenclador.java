package com.earandap.vehiculos.domain.nomenclador;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by angel on 04/04/16.
 */
public enum TipoNomenclador {
    ACTIVIDAD,
    CARGO,
    CAUSABASICA,
    CAUSAINMEDIATA,
    CLASEVEHICULO,
    ESTADO,
    //MUNICIPIO,
    PARTESAFECTADAS,
    PERDIDA,
    RESULTADO,
    SEXO,
    TIPOACTIVIDAD,
    TIPOCAPACITACION,
    TIPOCONTRATO,
    TIPODOCUMENTO,
    TIPOEVENTO,
    TIPOJORNADA,
    TIPOLESION,
    TIPOLICENCIA,
    TIPOMANTENIMIENTO,
    TIPORECURSO,
    TIPOSANCION,
    TIPOSANGRE,
    TIPOSEGURO,
    ZONA;

    public static final List<String> getValues(){
        TipoNomenclador[] temp = TipoNomenclador.values();
        ArrayList<String> result = new ArrayList<>(50);
        for (TipoNomenclador tipoNomenclador: temp) {
            result.add(tipoNomenclador.toString());
        }
        return result;
    }

    public String getCaption(){
        return name();
    }
    public int getId(){
        return ordinal();
    }
}
