package com.earandap.vehiculos.domain.nativesql;

import com.earandap.vehiculos.components.StaticMembers;
import java.util.Date;
import java.util.List;

/**
 *
 * @author felix.batista
 */
public class IndicadorNative {

    private String dbUrl;
    private String dbUsername;
    private String dbPassword;
    private String dbDriver;

    public IndicadorNative(String dbUrl, String dbUsername, String dbPassword, String dbDriver) {
        this.dbUrl = dbUrl;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        this.dbDriver = dbDriver;
    }

    public List<Object[]> consultarAccidentalidadEmpresa(Date fechaInicial, Date fechaFinal) {
        String sql = String.format("select\n"
                + "                text(count(incidente_id)) total,\n"
                + "                concat(text(sum(incidente_lesionadosempresa)), ' (', to_char(sum(incidente_lesionadosempresa) / (select cast(count(empleado_id) as float) from empleado) * 100, 'FM999999990.00'), ')') lesionados,\n"
                + "                concat(text(sum(incidente_condlesionadosempresa)), ' (', to_char(sum(incidente_condlesionadosempresa) / (select cast(count(conductor_id) as float) from conductor) * 100, 'FM999999990.00'), ')') cond_lesionados,\n"
                + "                concat(text(sum(incidente_mortalidadsempresa)), ' (', to_char(sum(incidente_mortalidadsempresa) / (select cast(count(empleado_id) as float) from empleado) * 100, 'FM999999990.00'), ')') muertos,\n"
                + "                concat('$', text(sum(incidente_indemnizacion))) perdidas\n"
                + "                from incidente i\n"
                + "                where incidente_fecha between '%s' and '%s'", fechaInicial, fechaFinal);
        List<Object[]> res = StaticMembers.ejecutarConsulta(sql, dbUrl, dbUsername, dbPassword, dbDriver);
        return res;
    }

    /*public List<Object[]> consultarCapacitacionesSegVial(Date fechaInicial, Date fechaFinal) {
        String sql = String.format("select\n"
                + "	text(count(v.id)) programadas,\n"
                + "	concat(text(count(case v.realizada when true then 1 else null end)), ' (', to_char(count(case v.realizada when true then 1 else null end) / cast(count(v.id) as float) * 100, 'FM999999990.00'), ')') realizadas,\n"
                + "	text(sum(evaluados)) evaluados,\n"
                + "	concat(sum(aprobados), ' (', to_char((sum(aprobados) / case sum(evaluados) when 0 then 1 else cast(sum(evaluados) as float) end * 100), 'FM999999990.00'), ')') aprobados\n"
                + "	from v_capacitacion v\n"
                + "	where v.fecha between '%s' and '%s'", fechaInicial, fechaFinal);
        List<Object[]> res = StaticMembers.ejecutarConsulta(sql, dbUrl, dbUsername, dbPassword, dbDriver);
        return res;
    }*/
    public List<Object[]> consultarCapacitacionesSegVial(Date fechaInicial, Date fechaFinal) {
        String sql = String.format("select\n"
                + "                text(count(v.id)) programadas,\n"
                + "                concat(count(case v.realizada when true then 1 else null end), ' (', to_char(count(case v.realizada when true then 1 else null end) / cast(count(v.id) as float) * 100, 'FM999999990.00'), ')') realizadas,\n"
                + "                text(coalesce(sum(case v.realizada when true then v.evaluados else null end), 0)) evaluados,\n"
                + "                concat(coalesce(sum(case v.realizada when true then v.aprobados else null end), 0), ' (', to_char((sum(case v.realizada when true then v.aprobados else null end) / case coalesce(sum(case v.realizada when true then v.evaluados else null end), 0) when 0 then 1 else cast(coalesce(sum(case v.realizada when true then v.evaluados else null end), 0) as float) end * 100), 'FM999999990.00'), ')') aprobados\n"
                + "                from v_capacitacion v\n"
                + "	where v.fecha between '%s' and '%s'", fechaInicial, fechaFinal);
        List<Object[]> res = StaticMembers.ejecutarConsulta(sql, dbUrl, dbUsername, dbPassword, dbDriver);
        return res;
    }

    public List<Object[]> consultarPruebasAlcoholemia(Date fechaInicial, Date fechaFinal) {
        String sql = String.format("select\n"
                + "	text(count(v.id)) programadas,\n"
                + "	concat(text(count(case v.realizada when true then 1 else null end)), ' (', to_char(count(case v.realizada when true then 1 else null end) / cast(count(v.id) as float) * 100, 'FM999999990.00'), ')') realizadas,\n"
                + "	text(sum(evaluados)) evaluados,\n"
                + "	concat(sum(aprobados), ' (', to_char((sum(aprobados) / case sum(evaluados) when 0 then 1 else cast(sum(evaluados) as float) end * 100), 'FM999999990.00'), ')') aprobados\n"
                + "	from v_prueba_alcoholemia v\n"
                + "	where v.fecha between '%s' and '%s'", fechaInicial, fechaFinal);
        List<Object[]> res = StaticMembers.ejecutarConsulta(sql, dbUrl, dbUsername, dbPassword, dbDriver);
        return res;
    }

    public List<Object[]> consultarAccidentesInvestSoc(Date fechaInicial, Date fechaFinal) {
        String sql = String.format("select\n"
                + "	text(total) total,\n"
                + "	concat(text(investigados), ' (', to_char(investigados / (case total when 0 then 1 else cast(total as float) end) * 100, 'FM999999990.00'), ')') investigados,\n"
                + "	text(con_lesionados) con_lesionados,\n"
                + "	concat(text(socializados), ' (', to_char(socializados / (case con_lesionados when 0 then 1 else cast(con_lesionados as float) end) * 100, 'FM999999990.00'), ')') socializados\n"
                + "	from (\n"
                + "		select\n"
                + "		count(i.incidente_id) total,\n"
                + "		count(case when i.incidente_lesionadosempresa + i.incidente_condlesionadosempresa > 0 then 1 else null end) con_lesionados,\n"
                + "		count(case when (select 1 from investigacion inv where inv.incidente_id = i.incidente_id limit 1) = 1 then 1 else null end) investigados,\n"
                + "		count(case when (select 1 from investigacion inv where inv.incidente_id = i.incidente_id and inv.investigacion_socializada = true limit 1) = 1 then 1 else null end) socializados\n"
                + "		from incidente i where i.incidente_fecha between '%s' and '%s') sub", fechaInicial, fechaFinal, fechaInicial, fechaFinal, fechaInicial, fechaFinal);
        List<Object[]> res = StaticMembers.ejecutarConsulta(sql, dbUrl, dbUsername, dbPassword, dbDriver);
        return res;
    }

    public List<Object[]> consultarAccidentesPorCausas(Date fechaInicial, Date fechaFinal) {
        String sql = String.format("select\n"
                + "	'TOTAL' causa,\n"
                + "	text(count(i.incidente_id)) total,\n"
                + "	'100' porciento\n"
                + "	from incidente i where i.incidente_fecha between '%s' and '%s'\n"
                + "union all\n"
                + "select * from (select\n"
                + "	(select n.nomenclador_descripcion from nomenclador n where n.nomenclador_id = ic.causabasica_id) causa,\n"
                + "	text(count(ic.causabasica_id)) total,\n"
                + "	to_char(count(ic.causabasica_id) / (case (select count(incidente_id) from incidente i where i.incidente_fecha between '%s' and '%s') when 0 then 1 else cast((select count(incidente_id) from incidente i where i.incidente_fecha between '%s' and '%s') as float) end) * 100, 'FM999999990.00') porciento\n"
                + "	from incidente i join incidente_causabasica ic on (i.incidente_id = ic.incidente_id)\n"
                + "	where i.incidente_fecha between '%s' and '%s'\n"
                + "	group by ic.causabasica_id\n"
                + "union all\n"
                + "select\n"
                + "	(select n.nomenclador_descripcion from nomenclador n where n.nomenclador_id = ic.causainmediata_id) causa,\n"
                + "	text(count(ic.causainmediata_id)) total,\n"
                + "	to_char(count(ic.causainmediata_id) / (case (select count(incidente_id) from incidente i where i.incidente_fecha between '%s' and '%s') when 0 then 1 else cast((select count(incidente_id) from incidente i where i.incidente_fecha between '%s' and '%s') as float) end) * 100, 'FM999999990.00') porciento\n"
                + "	from incidente i join incidente_causainmediata ic on (i.incidente_id = ic.incidente_id)\n"
                + "	where i.incidente_fecha between '%s' and '%s'\n"
                + "	group by ic.causainmediata_id) sub order by porciento", fechaInicial, fechaFinal, fechaInicial, fechaFinal, fechaInicial, fechaFinal, fechaInicial, fechaFinal, fechaInicial, fechaFinal, fechaInicial, fechaFinal, fechaInicial, fechaFinal);
        List<Object[]> res = StaticMembers.ejecutarConsulta(sql, dbUrl, dbUsername, dbPassword, dbDriver);
        return res;
    }

    public List<Object[]> consultarInfracciones(Date fechaInicial, Date fechaFinal) {
        String sql = String.format("select\n"
                + "	text((select count(conductor_id) from conductor)) conductores,\n"
                + "	concat(count(conductor_id), ' (', to_char(count(conductor_id) / (case (select count(conductor_id) from conductor) when 0 then 1 else cast((select count(conductor_id) from conductor) as float) end) * 100, 'FM999999990.00'), ')') infractores,\n"
                + "	text(sum(infracciones)) infracciones,\n"
                + "	concat(sum(pagadas), ' (', to_char(sum(pagadas) / (case sum(infracciones) when 0 then 1 else cast(sum(infracciones) as float) end) * 100, 'FM999999990.00'), ')') canceladas\n"
                + "	from (select\n"
                + "		conductor_id,\n"
                + "		count(*) infracciones,\n"
                + "		count(infraccion_fechapago) pagadas\n"
                + "		from infraccion\n"
                + "		where infraccion_fechainfraccion between '%s' and '%s'\n"
                + "		group by conductor_id) sub", fechaInicial, fechaFinal);
        List<Object[]> res = StaticMembers.ejecutarConsulta(sql, dbUrl, dbUsername, dbPassword, dbDriver);
        return res;
    }

    public List<Object[]> consultarInspecionesSeguridad(Date fechaInicial, Date fechaFinal) {
        String sql = String.format("select\n"
                + "	text((select count(vehiculo_id) from vehiculo)) total_vehs,\n"
                + "	concat(count(distinct vehiculo_id), ' (', to_char(count(distinct vehiculo_id) / (case (select count(vehiculo_id) from vehiculo) when 0 then 1 else cast((select count(vehiculo_id) from vehiculo) as float) end) * 100, 'FM999999990.00'), ')') mtto_preventivo\n"
                + "	from v_mantenimiento \n"
                + "	where tipomantenimiento_id = 24 and accion_fecha between '%s' and '%s'", fechaInicial, fechaFinal);
        List<Object[]> res = StaticMembers.ejecutarConsulta(sql, dbUrl, dbUsername, dbPassword, dbDriver);

        sql = String.format("select\n"
                + "	text(count(vehiculo_id)) total_inspecciones,\n"
                + "	concat(count(case accionalistamiento_operativo when false then 1 else null end), ' (', to_char(count(case accionalistamiento_operativo when false then 1 else null end) / (case count(vehiculo_id) when 0 then 1 else cast(count(vehiculo_id) as float) end) * 100, 'FM999999990.00'), ')') no_operativos\n"
                + "	from v_alistamiento\n"
                + "	where accion_fecha between '%s' and '%s'", fechaInicial, fechaFinal);

        List<Object[]> resAux = StaticMembers.ejecutarConsulta(sql, dbUrl, dbUsername, dbPassword, dbDriver);

        if (res.size() > 0) {
            Object[] fila = res.get(0);
            int cantCols = fila.length, nuevaCant = cantCols * 2;
            Object[] nuevaFila = new Object[nuevaCant];
            for (int i = 0; i < cantCols; i++) {
                nuevaFila[i] = fila[i];
            }

            if (resAux.size() > 0) {
                fila = resAux.get(0);
                for (int i = 0; i < cantCols; i++) {
                    nuevaFila[i + cantCols] = fila[i];
                }
            } else {
                for (int i = cantCols; i < nuevaCant; i++) {
                    nuevaFila[i] = "0";
                }
            }
            res.set(0, nuevaFila);
        } else {
            res = resAux;
        }
        return res;
    }
}
