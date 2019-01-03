
INSERT INTO nomenclador VALUES ('ZONA', 1, 'U', 'URBANA');
INSERT INTO nomenclador VALUES ('ZONA', 2, 'R', 'RURAL');
INSERT INTO nomenclador VALUES ('ACTIVIDAD', 3, 'ACTIVIDAD1', 'ACTIVIDAD 1');
INSERT INTO nomenclador VALUES ('ACTIVIDAD', 4, 'ACTIVIDAD2', 'ACTIVIDAD 2');
INSERT INTO nomenclador VALUES ('ACTIVIDAD', 5, 'ACTIVIDAD3', 'ACTIVIDAD 3');
INSERT INTO nomenclador VALUES ('CARGO', 6, 'EMP', 'EMPLEADO');
INSERT INTO nomenclador VALUES ('CARGO', 7, 'COND', 'CONDUCTOR');
INSERT INTO nomenclador VALUES ('CLASEVEHICULO', 8, 'AUTO', 'AUTO');
INSERT INTO nomenclador VALUES ('CLASEVEHICULO', 9, 'MOTO', 'MOTOCICLETA');
INSERT INTO nomenclador VALUES ('ESTADO', 10, 'ESTADO1', 'ESTADO1');
INSERT INTO nomenclador VALUES ('ESTADO', 11, 'ESTADO2', 'ESTADO2');
INSERT INTO nomenclador VALUES ('RESULTADO', 12, 'RESULTADO1', 'RESULTADO1');
INSERT INTO nomenclador VALUES ('RESULTADO', 13, 'RESULTADO2', 'RESULTADO2');
INSERT INTO nomenclador VALUES ('SEXO', 14, 'F', 'Femenino');
INSERT INTO nomenclador VALUES ('SEXO', 15, 'M', 'Masculino');
INSERT INTO nomenclador VALUES ('TIPODOCUMENTO', 16, 'CE', 'Cedula Extranjeria');
INSERT INTO nomenclador VALUES ('TIPODOCUMENTO', 17, 'CC', 'Cedula Ciudadania');
INSERT INTO nomenclador VALUES ('TIPODOCUMENTO', 18, 'PA', 'Pasaporte');
INSERT INTO nomenclador VALUES ('TIPODOCUMENTO', 19, 'NIT', 'NIT');
INSERT INTO nomenclador VALUES ('TIPOEVENTO', 20, 'TIPOEVENTO1', 'TIPOEVENTO1');
INSERT INTO nomenclador VALUES ('TIPOEVENTO', 21, 'TIPOEVENTO2', 'TIPOEVENTO2');
INSERT INTO nomenclador VALUES ('TIPOJORNADA', 22, 'TIPOJORNADA1', 'TIPOJORNADA1');
INSERT INTO nomenclador VALUES ('TIPOJORNADA', 23, 'TIPOJORNADA2', 'TIPOJORNADA2');
INSERT INTO nomenclador VALUES ('TIPOMANTENIMIENTO', 24, 'MTTOPREVENTIVO', 'MANTENIMIENTO PREVENTIVO');
INSERT INTO nomenclador VALUES ('TIPOMANTENIMIENTO', 25, 'TIPOMANTENIMIENTO2', 'TIPOMANTENIMIENTO2');
INSERT INTO nomenclador VALUES ('TIPOLESION', 26, 'TIPOLESION1', 'TIPOLESION1');
INSERT INTO nomenclador VALUES ('TIPOSANGRE', 27, 'TIPOSANGRE1', 'TIPOSANGRE1');
INSERT INTO nomenclador VALUES ('TIPOSANGRE', 28, 'TIPOSANGRE2', 'TIPOSANGRE2');

/*PARTE AFECTADA*/
INSERT INTO nomenclador VALUES ('PARTEAFECTADA', 29, 'Cabeza', 'Cabeza');
INSERT INTO nomenclador VALUES ('PARTEAFECTADA', 30, 'Oidos', 'Oidos');
INSERT INTO nomenclador VALUES ('PARTEAFECTADA', 31, 'Ojos', 'Ojos');
INSERT INTO nomenclador VALUES ('PARTEAFECTADA', 32, 'Nariz', 'Nariz');
INSERT INTO nomenclador VALUES ('PARTEAFECTADA', 33, 'Cuello', 'Cuello');
INSERT INTO nomenclador VALUES ('PARTEAFECTADA', 34, 'Boca', 'Boca');
INSERT INTO nomenclador VALUES ('PARTEAFECTADA', 35, 'Torax', 'Torax');
INSERT INTO nomenclador VALUES ('PARTEAFECTADA', 36, 'Abdomen', 'Abdomen');
INSERT INTO nomenclador VALUES ('PARTEAFECTADA', 37, 'Espalda', 'Espalda');
INSERT INTO nomenclador VALUES ('PARTEAFECTADA', 38, 'Brazos', 'Brazos');
INSERT INTO nomenclador VALUES ('PARTEAFECTADA', 39, 'Manos', 'Manos');

/*CAUSAS INMEDIATAS*/
INSERT INTO nomenclador VALUES ('CAUSAINMEDIATA', 40, 'CI1', 'Causa Inmediata 1');
INSERT INTO nomenclador VALUES ('CAUSAINMEDIATA', 41, 'CI2', 'Causa Inmediata 2');
INSERT INTO nomenclador VALUES ('CAUSAINMEDIATA', 42, 'CI3', 'Causa Inmediata 3');
INSERT INTO nomenclador VALUES ('CAUSAINMEDIATA', 43, 'CI4', 'Causa Inmediata 4');
INSERT INTO nomenclador VALUES ('CAUSAINMEDIATA', 44, 'CI5', 'Causa Inmediata 5');
INSERT INTO nomenclador VALUES ('CAUSAINMEDIATA', 45, 'CI6', 'Causa Inmediata 6');
INSERT INTO nomenclador VALUES ('CAUSAINMEDIATA', 46, 'CI7', 'Causa Inmediata 7');
INSERT INTO nomenclador VALUES ('CAUSAINMEDIATA', 47, 'CI8', 'Causa Inmediata 8');
INSERT INTO nomenclador VALUES ('CAUSAINMEDIATA', 48, 'CI9', 'Causa Inmediata 9');
INSERT INTO nomenclador VALUES ('CAUSAINMEDIATA', 49, 'CI0', 'Causa Inmediata 0');

/*MUNICIPIO*/
INSERT INTO departamento VALUES (50, 'D1', 'Dep 1');
INSERT INTO departamento VALUES (51, 'D2', 'Dep 2');
INSERT INTO municipios VALUES (50, 'M1', 'Municipios 1', 50);
INSERT INTO municipios VALUES (51, 'M2', 'Municipios 2', 51);

/*CAUSA BASICA*/
INSERT INTO nomenclador VALUES ('CAUSABASICA', 52, 'CB1', 'Causa Basica 1');
INSERT INTO nomenclador VALUES ('CAUSABASICA', 53, 'CB2', 'Causa Basica 2');
INSERT INTO nomenclador VALUES ('CAUSABASICA', 54, 'CB3', 'Causa Basica 3');
INSERT INTO nomenclador VALUES ('CAUSABASICA', 55, 'CB4', 'Causa Basica 4');
INSERT INTO nomenclador VALUES ('CAUSABASICA', 56, 'CB5', 'Causa Basica 5');
INSERT INTO nomenclador VALUES ('CAUSABASICA', 57, 'CB6', 'Causa Basica 6');
INSERT INTO nomenclador VALUES ('CAUSABASICA', 58, 'CB7', 'Causa Basica 7');

/*PERDIDA*/
INSERT INTO nomenclador VALUES ('PERDIDA', 59, 'P1', 'Materiales');
INSERT INTO nomenclador VALUES ('PERDIDA', 60, 'P2', 'Humanas');
INSERT INTO nomenclador VALUES ('PERDIDA', 61, 'P3', 'Ambientales');

/*TIPO SEGURO*/
INSERT INTO nomenclador VALUES ('TIPOSEGURO', 62, 'SOAT', 'SOAT');
INSERT INTO nomenclador VALUES ('TIPOSEGURO', 63, 'SEGUROVIDA', 'SEGURO DE VIDA');

/*TIPO TIPOACTIVIDAD*/
INSERT INTO nomenclador VALUES ('TIPOACTIVIDAD', 64, 'CAPACITACIONSEGVIAL', 'CAPACITACION DE SEGURIDAD VIAL');
INSERT INTO nomenclador VALUES ('TIPOACTIVIDAD', 65, 'PRUEBAALCOHOLEMIA', 'PRUEBA DE ALCOHOLEMIA');

/*TIPO SANCION*/
INSERT INTO nomenclador VALUES ('TIPOSANCION', 66, 'TIPOSANCION1', 'TIPOSANCION1');
INSERT INTO nomenclador VALUES ('TIPOSANCION', 67, 'TIPOSANCION2', 'TIPOSANCION2');

/*CATEGORIA LICENCIA*/
INSERT INTO nomenclador VALUES ('TIPOLICENCIA', 68, 'A1', 'Autos');
INSERT INTO nomenclador VALUES ('TIPOLICENCIA', 69, 'A2', 'Jeeps');

/*TIPO CONTRATO*/
INSERT INTO nomenclador VALUES ('TIPOCONTRATO', 70, 'TIPOCONTRATO1', 'TIPOCONTRATO1');
INSERT INTO nomenclador VALUES ('TIPOCONTRATO', 71, 'TIPOCONTRATO2', 'TIPOCONTRATO2');

/*TIPO CARROCERIA*/
INSERT INTO nomenclador VALUES ('TIPOCARROCERIA', 72, 'TIPOCARROCERIA1', 'TIPOCARROCERIA 1');
INSERT INTO nomenclador VALUES ('TIPOCARROCERIA', 73, 'TIPOCARROCERIA1', 'TIPOCARROCERIA 2');

/*TIPO COMBUSTIBLE*/
INSERT INTO nomenclador VALUES ('TIPOCOMBUSTIBLE', 74, 'GASOLINA', 'GASOLINA');
INSERT INTO nomenclador VALUES ('TIPOCOMBUSTIBLE', 75, 'DIESEL', 'DIESEL');

/*TIPO SERVICIO*/
INSERT INTO nomenclador VALUES ('TIPOSERVICIO', 76, 'TIPOSERVICIO1', 'PARTICULAR');
INSERT INTO nomenclador VALUES ('TIPOSERVICIO', 77, 'TIPOSERVICIO2', 'OTRO');

/*COLOR*/
INSERT INTO nomenclador VALUES ('COLOR', 78, 'ROJO', 'ROJO');
INSERT INTO nomenclador VALUES ('COLOR', 79, 'VERDE', 'VERDE');
INSERT INTO nomenclador VALUES ('COLOR', 80, 'AZUL', 'AZUL');
INSERT INTO nomenclador VALUES ('COLOR', 81, 'BLANCO', 'BLANCO');
INSERT INTO nomenclador VALUES ('COLOR', 82, 'NEGRO', 'NEGRO');
INSERT INTO nomenclador VALUES ('COLOR', 83, 'AMARILLO', 'AMARILLO');

/*CATEGORIA LICENCIA*/
INSERT INTO nomenclador VALUES ('TIPOLICENCIA', 84, 'B1', 'Motocicletas con cilindrada hasta 125 c.c.');
INSERT INTO nomenclador VALUES ('TIPOLICENCIA', 85, 'B2', 'Motocicletas con cilindrada superior a 125 c.c.');
INSERT INTO nomenclador VALUES ('TIPOLICENCIA', 86, 'C1', 'Vehiculos de carga con peso maximo de 3500 kgs');
INSERT INTO nomenclador VALUES ('TIPOLICENCIA', 87, 'C2', 'Vehiculos de carga con peso superior a 3500 kgs');

/*TIPO INCIDENTE*/
INSERT INTO nomenclador VALUES ('TIPOINCIDENTE', 88, 'TIPOINCIDENTE1', 'TIPO INCIDENTE 1');
INSERT INTO nomenclador VALUES ('TIPOINCIDENTE', 89, 'TIPOINCIDENTE2', 'TIPO INCIDENTE 2');
INSERT INTO nomenclador VALUES ('TIPOINCIDENTE', 90, 'TIPOINCIDENTE3', 'TIPO INCIDENTE 3');

/*TIPO DOCUMENTO EMPRESA*/
INSERT INTO nomenclador VALUES ('TIPODOCUMENTOEMPRESA', 91, 'OBJCOMITE', 'OBJETIVOS DEL COMITE');
INSERT INTO nomenclador VALUES ('TIPODOCUMENTOEMPRESA', 92, 'POLITICAS', 'POLITICAS');
INSERT INTO nomenclador VALUES ('TIPODOCUMENTOEMPRESA', 93, 'DIVULGACION', 'DIVULGACION');
INSERT INTO nomenclador VALUES ('TIPODOCUMENTOEMPRESA', 94, 'PLANRIESGOS', 'PLAN DE RIESGOS VIALES');

/*TIPO DOCUMENTO CAPACITACION*/
INSERT INTO nomenclador VALUES ('TIPODOCUMENTOCAPACITACION', 95, 'FICHA', 'FICHA DE CAPACITACION');
INSERT INTO nomenclador VALUES ('TIPODOCUMENTOCAPACITACION', 96, 'LISTAASIST', 'LISTA DE ASISTENCIA');

/*TERCERO*/
INSERT INTO "tercero" VALUES ('EMPRESA', 1, 'nec.mollis@purusDuiselementum.org', '108-544 Eget, Carretera', '757', false, 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Curabitur', '056-129-0567', 19);
INSERT INTO "tercero" VALUES ('EMPRESA', 2, 'tellus.sem@ametanteVivamus.com', 'Apartado núm.: 569, 1236 Libero Avenida', '269', false, 'Lorem ipsum dolor sit amet, consectetuer adipiscing', '052-383-0083', 19);
INSERT INTO "tercero" VALUES ('EMPRESA', 3, 'cursus@anteVivamusnon.net', '2428 Odio Av.', '170', false, 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Curabitur sed', '056-691-3601', 19);
INSERT INTO "tercero" VALUES ('EMPRESA', 4, 'eu.tempor@eu.org', '588 Fermentum Avenida', '996', false, 'Lorem ipsum dolor sit amet, consectetuer', '031-019-4311', 19);
INSERT INTO "tercero" VALUES ('EMPRESA', 5, 'dolor@nisia.org', '600-8876 Cursus ', '411', false, 'Lorem ipsum', '073-465-3037', 19);
INSERT INTO "tercero" VALUES ('PERSONA', 6, 'vel.turpis@Nunccommodo.co.uk', 'Apartado núm.: 772, 9579 Vivamus ', '608', false, 'Lorem', '081-782-5837', 19);
INSERT INTO "tercero" VALUES ('PERSONA', 7, 'fdbatista@gmail.com', '649-9903 Turpis C/', '226', false, 'Lorem ipsum dolor', '007-345-2825', 18);
INSERT INTO "tercero" VALUES ('PERSONA', 8, 'nec.mauris.blandit@euismodac.ca', 'Apdo.:538-4537 Et, Calle', '254', false, 'Lorem ipsum dolor sit amet, ', '040-171-5846', 16);
INSERT INTO "tercero" VALUES ('PERSONA', 9, 'lorem.tristique.aliquet@nibh.com', 'Apdo.:799-7800 Semper. Av.', '445', false, 'Lorem ipsum dolor sit amet, ', '092-197-6118', 17);
INSERT INTO "tercero" VALUES ('PERSONA', 10, 'lacus.Ut.nec@liberoProinmi.net', '3496 Augue ', '611', false, 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit.', '033-563-8191', 19);
INSERT INTO "tercero" VALUES ('COMPANHIA-SEGUROS', 11, 'test.insurance@safe.net', '1046 Vogue ', '611', false, 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit.', '033-561-8151', 19);
INSERT INTO "tercero" VALUES ('COMPANHIA-SEGUROS', 12, 'all.insurance@home.net', '34531 Vogue ', '611', false, 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit.', '053-861-2151', 19);
INSERT INTO "tercero" VALUES ('COMPANHIA-SEGUROS', 13, 'best.insurance@crawl.net', '223 Baker St. ', '611', false, 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit.', '034-161-9101', 19);

/*COMPANHIAS*/
INSERT INTO companhia_seguros VALUES ('BETSAFE', 11);
INSERT INTO companhia_seguros VALUES ('LIFE INSURANCE', 12);
INSERT INTO companhia_seguros VALUES ('SUREBET', 13);

/*CONDUCTOR*/
INSERT INTO "conductor" VALUES (1, '2015-10-11 00:00:00.000000', '0x0001', '', '');
INSERT INTO "conductor" VALUES (2, '2009-10-11 00:00:00.000000', '0x0002', '', '');
INSERT INTO "conductor" VALUES (3, '2013-10-11 00:00:00.000000', '0x0003', '', '');
INSERT INTO "conductor" VALUES (4, '2015-10-11 00:00:00.000000', '0x0004', '', '');

/*EMPLEADO*/
INSERT INTO "empleado" VALUES (1, 6);
INSERT INTO "empleado" VALUES (2, 7);

/*PERSONA*/
INSERT INTO "persona" VALUES ('09 17 35 97 57', '2016-08-11T08:40:52+00:00', 'lacus.', 'Cantu', 'Janna', 'Dean', 'Jael', 6, 1, 1, 51, 51, 15, 27);
INSERT INTO "persona" VALUES ('09 81 88 28 25', '2015-11-05T20:27:33+00:00', 'ipsum.', 'Kelly', 'Wesley', 'Macias', 'Colin', 7, 2, 2, 50, 50, 15, 28);
INSERT INTO "persona" VALUES ('03 95 44 92 88', '2015-11-14T09:17:10+00:00', 'Praesent', 'Poole', 'Malik', 'Warren', 'Zena', 8, 3, NULL, 50, 50, 14, 28);
INSERT INTO "persona" VALUES ('05 92 82 25 03', '2015-08-16T09:54:43+00:00', 'Nulla', 'Schultz', 'Montana', 'Rowland', 'Zenia', 9, 4, NULL, 51, 51, 15, 27);
INSERT INTO "persona" VALUES ('03 56 27 22 05', '2015-10-08T11:21:20+00:00', 'libero.', 'Cabrera', 'Orson', 'Vega', 'Xerxes', 10, NULL, NULL, 51, 50, 15, 27);

/*EMPRESA*/
INSERT INTO "empresa" VALUES ('EMPRESA 1', 1);
INSERT INTO "empresa" VALUES ('EMPRESA 2', 2);
INSERT INTO "empresa" VALUES ('EMPRESA 3', 3);
INSERT INTO "empresa" VALUES ('EMPRESA 4', 4);
INSERT INTO "empresa" VALUES ('EMPRESA 5', 5);

/*EMPRESA DOCUMENTOS*/
INSERT INTO "empresa_documento" VALUES (1, 'test.pdf', 1, 93);
INSERT INTO "empresa_documento" VALUES (2, 'test.doc', 1, 92);
INSERT INTO "empresa_documento" VALUES (3, 'test.txt', 1, 94);

/*MARCA VEHICULO*/
INSERT INTO marca (id, codigo, nombre) VALUES (1, 'MBENZ', 'MERCEDES-BENZ');
INSERT INTO marca (id, codigo, nombre) VALUES (2, 'FIAT', 'FIAT');
INSERT INTO marca (id, codigo, nombre) VALUES (3, 'AUDI', 'AUDI');
INSERT INTO marca (id, codigo, nombre) VALUES (4, 'TOYOTA', 'TOYOTA');
INSERT INTO marca (id, codigo, nombre) VALUES (5, 'HYUNDAI', 'HYUNDAI');
INSERT INTO marca (id, codigo, nombre) VALUES (6, 'KIA', 'KIA');

/*LINEA VEHICULO*/
INSERT INTO linea (id, codigo, nombre, id_marca) VALUES (1, 'ACTROSS', 'ACTROSS', 1);
INSERT INTO linea (id, codigo, nombre, id_marca) VALUES (2, 'FIORINO', 'FIORINO', 2);
INSERT INTO linea (id, codigo, nombre, id_marca) VALUES (3, 'PUNTO', 'PUNTO', 2);
INSERT INTO linea (id, codigo, nombre, id_marca) VALUES (4, 'SEICENTO', 'SEICENTO', 2);
INSERT INTO linea (id, codigo, nombre, id_marca) VALUES (5, 'A4', 'A4', 3);
INSERT INTO linea (id, codigo, nombre, id_marca) VALUES (6, 'A6', 'A6', 3);
INSERT INTO linea (id, codigo, nombre, id_marca) VALUES (7, 'COROLLA', 'COROLLA', 4);
INSERT INTO linea (id, codigo, nombre, id_marca) VALUES (8, 'ACCENT', 'ACCENT', 5);
INSERT INTO linea (id, codigo, nombre, id_marca) VALUES (9, 'PICANTO', 'PICANTO', 6);


/*VEHICULO*/
INSERT INTO "vehiculo" (vehiculo_id, vehiculo_kminicial, vehiculo_kmactual, vehiculo_placa, vehiculo_seriemotor, empresa_id, tipovehiculo_id, linea_id, marca_id, propietario_id, vehiculo_modelo, vehiculo_cilindrada, vehiculo_capacidad, vehiculo_vin, carroceria_id, combustible_id, color_id, tiposervicio_id, vehiculo_chasis, vehiculo_serie)
  VALUES ('1', '0', '10', 'HK2115', 12341, 2, 8, 1, 1, null, 'm23123', 78.4, 2500, '00-00-0001', 72, 74, 82, 77, '9034222', '3214475');

/*INCIDENTE*/
INSERT INTO incidente (incidente_id, incidente_accidentetrabajo, incidente_accidentetransito, incidente_diasincapacidad, incidente_direccion, incidente_fecha, incidente_incapacidad, incidente_observaciones, incidente_personalexterno, incidente_tiempoconduccion, conductor_id, reportero_id, responsable_id, tipoevento_id, tipojornada_id, vehiculo_id, zona_id, incidente_lesionadosempresa, incidente_mortalidadsempresa, incidente_mortalidadterceros, incidente_indemnizacion, incidente_condlesionadosempresa)
    VALUES (1, true, false, 3, 'Lugar donde ocurrio', '2016-11-22', true, 'observaciones', true, 5, 1, 10, null, 20, 22, 1, 2, 0, 0, 3, 3500, 1);
INSERT INTO incidente (incidente_id, incidente_accidentetrabajo, incidente_accidentetransito, incidente_diasincapacidad, incidente_direccion, incidente_fecha, incidente_incapacidad, incidente_observaciones, incidente_personalexterno, incidente_tiempoconduccion, conductor_id, reportero_id, responsable_id, tipoevento_id, tipojornada_id, vehiculo_id, zona_id, incidente_lesionadosempresa, incidente_mortalidadsempresa, incidente_mortalidadterceros, incidente_indemnizacion, incidente_condlesionadosempresa)
    VALUES (2, true, false, 3, 'Lugar donde ocurrio', '2016-11-22', true, 'observaciones', true, 15, 2, 10, null, 20, 22, 1, 2, 0, 0, 3, 500, 1);
INSERT INTO incidente (incidente_id, incidente_accidentetrabajo, incidente_accidentetransito, incidente_diasincapacidad, incidente_direccion, incidente_fecha, incidente_incapacidad, incidente_observaciones, incidente_personalexterno, incidente_tiempoconduccion, conductor_id, reportero_id, responsable_id, tipoevento_id, tipojornada_id, vehiculo_id, zona_id, incidente_lesionadosempresa, incidente_mortalidadsempresa, incidente_mortalidadterceros, incidente_indemnizacion, incidente_condlesionadosempresa)
    VALUES (3, true, false, 3, 'Lugar donde ocurrio', '2016-11-22', true, 'observaciones', true, 7, 3, 10, null, 20, 22, 1, 2, 0, 0, 3, 2400, 1);

/*SEGURO*/
INSERT INTO "seguro" (seguro_id, seguro_fechaemision, seguro_fechavencimiento, seguro_numero, tiposeguro_id, vehiculo_id, companhia_id) VALUES ('1', '2015-08-25T08:40:52+00:00', '2016-12-15T08:40:52+00:00', '1', 62, 1, 11);
INSERT INTO "seguro" (seguro_id, seguro_fechaemision, seguro_fechavencimiento, seguro_numero, tiposeguro_id, vehiculo_id, companhia_id) VALUES ('2', '2016-10-25T08:40:52+00:00', '2016-12-13T08:40:52+00:00', '2', 63, 1, 11);

/*CAPACITACION*/
INSERT INTO "capacitacion" (capacitacion_id, capacitacion_fechacapacitacion, capacitacion_fechavencimiento, capacitacion_nombre, capacitacion_observaciones, tipo_capacitacion, capacitacion_realizada, capacitacion_puntajemaximo, capacitacion_fechaprogramacion)
    VALUES ('1', '2016-11-25T08:40:52+00:00', '2016-12-13T08:40:52+00:00', 'Curso 1', 'Observacion 1', 64, true, 100, '2016-11-25T08:40:52+00:00');
INSERT INTO "capacitacion" (capacitacion_id, capacitacion_fechacapacitacion, capacitacion_fechavencimiento, capacitacion_nombre, capacitacion_observaciones, tipo_capacitacion, capacitacion_realizada, capacitacion_puntajemaximo, capacitacion_fechaprogramacion)
    VALUES ('2', '2016-11-25T08:40:52+00:00', '2016-12-19T08:40:52+00:00', 'Curso 2', 'Observacion 2', 64, true, 100, '2016-11-25T08:40:52+00:00');
INSERT INTO "capacitacion" (capacitacion_id, capacitacion_fechacapacitacion, capacitacion_fechavencimiento, capacitacion_nombre, capacitacion_observaciones, tipo_capacitacion, capacitacion_realizada, capacitacion_puntajemaximo, capacitacion_fechaprogramacion)
    VALUES ('3', '2016-11-25T08:40:52+00:00', '2016-12-25T08:40:52+00:00', 'Prueba 1', 'Observacion 3', 65, true, 1, '2016-11-25T08:40:52+00:00');
INSERT INTO "capacitacion" (capacitacion_id, capacitacion_fechacapacitacion, capacitacion_fechavencimiento, capacitacion_nombre, capacitacion_observaciones, tipo_capacitacion, capacitacion_realizada, capacitacion_puntajemaximo, capacitacion_fechaprogramacion)
    VALUES ('4', '2016-11-25T08:40:52+00:00', '2016-12-20T08:40:52+00:00', 'Prueba 2', 'Observacion 4', 65, false, 1, '2016-11-25T08:40:52+00:00');

/*ACCION*/
INSERT INTO "accion" VALUES ('ACCIONMANTENIMIENTO', 1, '2016-11-25', 10, 1);
INSERT INTO "accion" VALUES ('ACCIONALISTAMIENTO', 2, '2016-11-24', 10, 1);

/*ACCION-MANTENIMIENTO*/
INSERT INTO "accion_mantenimiento" VALUES (20, '', 1, 24);

/*ACCION-ALISTAMIENTO*/
INSERT INTO "accion_alistamiento" VALUES ('Detalles recogidos', 25, true, 2);

/*MANTENIMIENTODETALLE*/
INSERT INTO "mantenimiento_detalle" VALUES (1, 3, 1, 13);

/*PERFIL*/
INSERT INTO "perfil" VALUES (1, 'Administrador');
INSERT INTO "perfil" VALUES (2, 'Usuario');

/*USUARIO*/
INSERT INTO "usuario" VALUES (1, '$2a$10$.ud.Xlvupn5eFXukGt2rm.9mMFCN5gxgYBal3.AGyqCrhgLoVvgUC', '2013-07-15T08:40:52+00:00', false, 'admin', 1, 7);
INSERT INTO "usuario" VALUES (2, '$2a$10$b95qh1Fst9BbhbMtPiwMG.YYC1qChjQzOFdpoCyUipNwMkki5mEIq', '2013-07-15T08:40:52+00:00', false, 'usuario', 2, null);

/*MODULO*/
INSERT INTO "modulo" VALUES (1, 'Seguridad Vial');
INSERT INTO "modulo" VALUES (2, 'Configuracion de Transporte');
INSERT INTO "modulo" VALUES (3, 'Configuracion de Empresa');
INSERT INTO "modulo" VALUES (4, 'Administracion');
INSERT INTO "modulo" VALUES (5, 'Indicadores');

/*SUBMODULO*/
INSERT INTO "submodulo" VALUES (1, 'alistamiento', '1');
INSERT INTO "submodulo" VALUES (2, 'mantenimiento', '1');
INSERT INTO "submodulo" VALUES (3, 'incidentes', '1');
INSERT INTO "submodulo" VALUES (4, 'capacitacion', '1');
INSERT INTO "submodulo" VALUES (5, 'infracciones', '1');
INSERT INTO "submodulo" VALUES (6, 'contratos', '1');
INSERT INTO "submodulo" VALUES (7, 'vehiculos', '2');
INSERT INTO "submodulo" VALUES (8, 'conductores', '2');
INSERT INTO "submodulo" VALUES (9, 'alistamientoactividad', '2');
INSERT INTO "submodulo" VALUES (10, 'mantenimientoactividad', '2');
INSERT INTO "submodulo" VALUES (11, 'empresas', '3');
INSERT INTO "submodulo" VALUES (12, 'empleados', '3');
INSERT INTO "submodulo" VALUES (13, 'sucursales', '3');
INSERT INTO "submodulo" VALUES (14, 'usuarios', '4');
INSERT INTO "submodulo" VALUES (15, 'perfiles', '4');
INSERT INTO "submodulo" VALUES (22, 'persona', '4');
INSERT INTO "submodulo" VALUES (16, 'notificaciones', '4');
INSERT INTO "submodulo" VALUES (17, 'tipo de mensaje', '4');
INSERT INTO "submodulo" VALUES (18, 'tipo-notificacion', '4');
INSERT INTO "submodulo" VALUES (19, 'notificacion-mensaje', '4');
INSERT INTO "submodulo" VALUES (20, 'nomencladores', '4');
INSERT INTO "submodulo" VALUES (21, 'terceronotificacion', '4');
INSERT INTO "submodulo" VALUES (23, 'parametros', '4');
INSERT INTO "submodulo" VALUES (24, 'accidentalidad', '5');
INSERT INTO "submodulo" VALUES (25, 'capacitaciones-seg-vial', '5');
INSERT INTO "submodulo" VALUES (26, 'pruebas-alcoholemia', '5');
INSERT INTO "submodulo" VALUES (27, 'inspecciones-seguridad', '5');
INSERT INTO "submodulo" VALUES (28, 'causas-inmediatas-basicas', '5');
INSERT INTO "submodulo" VALUES (29, 'infracciones-transito', '5');

/*RECURSO*/
INSERT INTO "recurso" VALUES (1, 'alistamiento', 'Gestionar Alistamientos');
INSERT INTO "recurso" VALUES (2, 'mantenimiento', 'Gestionar Mantenimientos');
INSERT INTO "recurso" VALUES (3, 'incidentes', 'Gestionar Incidentes');
INSERT INTO "recurso" VALUES (4, 'capacitacion', 'Gestionar Capacitaciones');
INSERT INTO "recurso" VALUES (5, 'infracciones', 'Gestionar Infracciones');
INSERT INTO "recurso" VALUES (6, 'contratos', 'Gestionar Contratos');
INSERT INTO "recurso" VALUES (7, 'vehiculos', 'Gestionar Vehiculos');
INSERT INTO "recurso" VALUES (8, 'conductores', 'Gestionar Conductores');
INSERT INTO "recurso" VALUES (9, 'alistamientoactividad', 'Gestionar Alistamientos-Actividad');
INSERT INTO "recurso" VALUES (10, 'mantenimientoactividad', 'Gestionar Mantenimientos-Actividad');
INSERT INTO "recurso" VALUES (11, 'empresas', 'Gestionar Empresas');
INSERT INTO "recurso" VALUES (12, 'empleados', 'Gestionar Empleados');
INSERT INTO "recurso" VALUES (13, 'sucursales', 'Gestionar Sucursales');
INSERT INTO "recurso" VALUES (14, 'usuarios', 'Gestionar Usuarios');
INSERT INTO "recurso" VALUES (15, 'perfiles', 'Gestionar Perfiles');
INSERT INTO "recurso" VALUES (16, 'notificaciones', 'Gestionar Notificaciones');
INSERT INTO "recurso" VALUES (17, 'tipo de mensaje', 'Gestionar Tipos de Mensajes');
INSERT INTO "recurso" VALUES (18, 'tipo-notificacion', 'Gestionar Tipos de Notificaciones');
INSERT INTO "recurso" VALUES (19, 'notificacion-mensaje', 'Gestionar Notificaciones-Mensajes');
INSERT INTO "recurso" VALUES (20, 'nomencladores', 'Gestionar Nomencladores');
INSERT INTO "recurso" VALUES (21, 'terceronotificacion', 'Alertas');
INSERT INTO "recurso" VALUES (22, 'persona', 'Personas');
INSERT INTO "recurso" VALUES (23, 'parametros', 'Parametros');
INSERT INTO "recurso" VALUES (24, 'accidentalidad', 'Accidentalidad');
INSERT INTO "recurso" VALUES (25, 'capacitaciones-seg-vial', 'Capacitaciones');
INSERT INTO "recurso" VALUES (26, 'pruebas-alcoholemia', 'Pruebas Alcoholemia');
INSERT INTO "recurso" VALUES (27, 'inspecciones-seguridad', 'Inspecciones Seguridad');
INSERT INTO "recurso" VALUES (28, 'causas-inmediatas-basicas', 'Causas Inmediatas y B\u00e1sicas');
INSERT INTO "recurso" VALUES (29, 'infracciones-transito', 'Infracciones de Tr\u00e1nsito');

/*PERFILRECURSO*/
INSERT INTO "perfil_recurso" VALUES (true, true, true, true, 1, 1, 1);
INSERT INTO "perfil_recurso" VALUES (true, true, true, true, 1, 2, 2);
INSERT INTO "perfil_recurso" VALUES (true, true, true, true, 1, 3, 3);
INSERT INTO "perfil_recurso" VALUES (true, true, true, true, 1, 4, 4);
INSERT INTO "perfil_recurso" VALUES (true, true, true, true, 1, 5, 5);
INSERT INTO "perfil_recurso" VALUES (true, true, true, true, 1, 6, 6);

INSERT INTO "perfil_recurso" VALUES (true, true, true, true, 1, 7, 7);
INSERT INTO "perfil_recurso" VALUES (true, true, true, true, 1, 8, 8);
INSERT INTO "perfil_recurso" VALUES (true, true, true, true, 1, 9, 9);
INSERT INTO "perfil_recurso" VALUES (true, true, true, true, 1, 10, 10);

INSERT INTO "perfil_recurso" VALUES (true, true, true, true, 1, 11, 11);
INSERT INTO "perfil_recurso" VALUES (true, true, true, true, 1, 12, 12);
INSERT INTO "perfil_recurso" VALUES (true, true, true, true, 1, 13, 13);

INSERT INTO "perfil_recurso" VALUES (true, true, true, true, 1, 14, 14);
INSERT INTO "perfil_recurso" VALUES (true, true, true, true, 1, 15, 15);
INSERT INTO "perfil_recurso" VALUES (true, true, true, true, 1, 16, 16);
INSERT INTO "perfil_recurso" VALUES (true, true, true, true, 1, 17, 17);
INSERT INTO "perfil_recurso" VALUES (true, true, true, true, 1, 18, 18);
INSERT INTO "perfil_recurso" VALUES (true, true, true, true, 1, 19, 19);
INSERT INTO "perfil_recurso" VALUES (true, true, true, true, 1, 20, 20);
INSERT INTO "perfil_recurso" VALUES (true, true, true, true, 1, 21, 21);
INSERT INTO "perfil_recurso" VALUES (true, true, true, true, 1, 22, 22);
INSERT INTO "perfil_recurso" VALUES (true, true, true, true, 1, 23, 23);
INSERT INTO "perfil_recurso" VALUES (true, true, true, true, 1, 24, 24);
INSERT INTO "perfil_recurso" VALUES (true, true, true, true, 1, 25, 25);
INSERT INTO "perfil_recurso" VALUES (true, true, true, true, 1, 26, 26);
INSERT INTO "perfil_recurso" VALUES (true, true, true, true, 1, 27, 27);
INSERT INTO "perfil_recurso" VALUES (true, true, true, true, 1, 28, 28);
INSERT INTO "perfil_recurso" VALUES (true, true, true, true, 1, 29, 29);

INSERT INTO "perfil_recurso" VALUES (false, false, false, false, 2, 1, 1);
INSERT INTO "perfil_recurso" VALUES (true, true, true, true, 2, 2, 2);

INSERT INTO "tipo_notificacion" (tiponotificacion_id, tiponotificacion_icono, tiponotificacion_descripcion, tiponotificacion_codigo, tiponotificacion_admin)
    VALUES (1, 'icono1.png', 'Vencimiento de poliza de seguro', 'COD1', true);
INSERT INTO "tipo_notificacion" (tiponotificacion_id, tiponotificacion_icono, tiponotificacion_descripcion, tiponotificacion_codigo, tiponotificacion_admin)
    VALUES (2, 'icono2.png', 'Vencimiento de licencia de conduccion', 'COD2', true);
INSERT INTO "tipo_notificacion" (tiponotificacion_id, tiponotificacion_icono, tiponotificacion_descripcion, tiponotificacion_codigo, tiponotificacion_admin)
    VALUES (3, 'icono3.png', 'Vencimiento de capacitacion', 'COD3', true);

--TIPO MENSAJE
INSERT INTO "tipo_mensaje" VALUES (1, 'Web', 'Notificacion via Web');
INSERT INTO "tipo_mensaje" VALUES (2, 'Mail', 'Notificacion via email');

/*INSERT INTO "tiponotificacion_cargo" VALUES (1, 6);
INSERT INTO "tiponotificacion_cargo" VALUES (1, 7);
INSERT INTO "tiponotificacion_cargo" VALUES (1, 97);
INSERT INTO "tiponotificacion_cargo" VALUES (2, 6);
INSERT INTO "tiponotificacion_cargo" VALUES (2, 7);
INSERT INTO "tiponotificacion_cargo" VALUES (2, 97);*/

INSERT INTO "tipo_notificacion_mensaje" VALUES (true, 1, 1);
INSERT INTO "tipo_notificacion_mensaje" VALUES (true, 1, 2);
INSERT INTO "tipo_notificacion_mensaje" VALUES (true, 1, 3);
INSERT INTO "tipo_notificacion_mensaje" VALUES (true, 2, 1);
INSERT INTO "tipo_notificacion_mensaje" VALUES (true, 2, 2);
INSERT INTO "tipo_notificacion_mensaje" VALUES (true, 2, 3);

--MIEBMROS COMITE
INSERT INTO "miembro_comite" VALUES (1, 'Jefe de Recursos Humanos', false, 1, 6);
INSERT INTO "miembro_comite" VALUES (2, 'Jefe de Transporte', false, 1, 7);
INSERT INTO "miembro_comite" VALUES (3, 'Director General', true, 1, 8);
INSERT INTO "miembro_comite" VALUES (4, 'Secretario', false, 2, 9);
INSERT INTO "miembro_comite" VALUES (5, 'Jefe de Asuntos sin Importancia', true, 2, 10);

--CONFIG DE LA APP
INSERT INTO "parametros" VALUES (1, 'Dias de antelacion para notificar eventos', '7');
INSERT INTO "parametros" VALUES (2, 'Ruta local para la carga de ficheros', 'C:\xampp\tomcat\webapps\uploads\');
INSERT INTO "parametros" VALUES (3, 'URL de acceso a los ficheros cargados', 'http://localhost:9090/uploads');
INSERT INTO "parametros" VALUES (4, 'Servidor de correo', 'smtp.gmail.com');
INSERT INTO "parametros" VALUES (5, 'Puerto del servidor de correo', '465');
INSERT INTO "parametros" VALUES (6, 'Nombre de usuario', 'fdbatista@gmail.com');
INSERT INTO "parametros" VALUES (7, 'Clave', 'bt91rc35');
INSERT INTO "parametros" VALUES (8, 'Usar autenticacion', 'Si');
INSERT INTO "parametros" VALUES (9, 'Usar TLS', 'No');
INSERT INTO "parametros" VALUES (10, 'Usar SSL', 'Si');

--INFRACCIONES
insert into infraccion (infraccion_id, infraccion_causa, infraccion_detalle, infraccion_fechainfraccion, infraccion_fechapago, infraccion_valormulta, conductor_id, incidente_id, tipoincidente_id, tiposancion_id, vehiculo_id)
  values (1, 'Saltarse la luz roja', 'El conductor ha olvidado parar ante la roja', '25/11/2016', null, 30.00, 7, 1, 88, 66, 1);
insert into infraccion (infraccion_id, infraccion_causa, infraccion_detalle, infraccion_fechainfraccion, infraccion_fechapago, infraccion_valormulta, conductor_id, incidente_id, tipoincidente_id, tiposancion_id, vehiculo_id)
  values (2, 'Saltarse la luz roja', 'El conductor ha cruzado por encima de la doble continua', '25/11/2016', null, 30.00, 8, 1, 89, 66, 1);
insert into infraccion (infraccion_id, infraccion_causa, infraccion_detalle, infraccion_fechainfraccion, infraccion_fechapago, infraccion_valormulta, conductor_id, incidente_id, tipoincidente_id, tiposancion_id, vehiculo_id)
  values (3, 'Saltarse la luz roja', 'El conductor ha circulado de noche sin luces', '25/11/2016', null, 30.00, 7, 1, 90, 67, 1);

--NOTIFICACIONES

INSERT INTO notificacion values (1, 'En un lugar de La Mancha, de cuyo nombre no quiero acordarme, vivian dos hermanos que se llamaban Hansel y Gretel.', 'Alerta', '2016-12-01', '2016-12-01', true, 2);
INSERT INTO tercero_notificacion select true, false, false, 1, tercero_id from persona;

--REINICIAR SECUENCIAS A VALORES SEGUROS

ALTER SEQUENCE accidenteinvolucrado_seq RESTART WITH 100;
ALTER SEQUENCE accion_seq RESTART WITH 100;
ALTER SEQUENCE alistamiento_seq RESTART WITH 100;
ALTER SEQUENCE archivo_seq RESTART WITH 100;
ALTER SEQUENCE capacitacion_seq RESTART WITH 100;
ALTER SEQUENCE conductor_seq RESTART WITH 100;
ALTER SEQUENCE contrato_seq RESTART WITH 100;
ALTER SEQUENCE departamento_seq RESTART WITH 100;
ALTER SEQUENCE empleado_seq RESTART WITH 100;
ALTER SEQUENCE incidente_seq RESTART WITH 100;
ALTER SEQUENCE investigacion_seq RESTART WITH 100;
ALTER SEQUENCE infraccion_seq RESTART WITH 100;
ALTER SEQUENCE licencia_seq RESTART WITH 100;
ALTER SEQUENCE linea_seq RESTART WITH 100;
ALTER SEQUENCE mantenimientodetalle_seq RESTART WITH 100;
ALTER SEQUENCE marca_seq RESTART WITH 100;
ALTER SEQUENCE modulo_seq RESTART WITH 100;
ALTER SEQUENCE municipios_seq RESTART WITH 100;
ALTER SEQUENCE nomenclador_seq RESTART WITH 150;
ALTER SEQUENCE notifiacion_seq RESTART WITH 100;
ALTER SEQUENCE params_seq RESTART WITH 50;
ALTER SEQUENCE perfil_seq RESTART WITH 100;
ALTER SEQUENCE recurso_seq RESTART WITH 100;
ALTER SEQUENCE seguro_seq RESTART WITH 100;
ALTER SEQUENCE submodulo_seq RESTART WITH 100;
ALTER SEQUENCE sucursal_seq RESTART WITH 100;
ALTER SEQUENCE tercero_seq RESTART WITH 100;
ALTER SEQUENCE tipomensaje_seq RESTART WITH 100;
ALTER SEQUENCE tiponotificacion_seq RESTART WITH 100;
ALTER SEQUENCE usuario_seq RESTART WITH 100;
ALTER SEQUENCE vehiculo_seq RESTART WITH 100;
ALTER SEQUENCE incidente_lesion_seq RESTART WITH 100;
ALTER SEQUENCE miembro_comite_seq RESTART WITH 100;
ALTER SEQUENCE empresa_documento_seq RESTART WITH 100;
ALTER SEQUENCE capacitacion_documento_seq RESTART WITH 100;

-- INDICES PARA OPTIMIZAR CONSULTAS Y BUSQUEDAS

CREATE INDEX ind_incidente_fecha ON incidente (incidente_fecha);
CREATE INDEX ind_notificacion_detalle ON notificacion (notificacion_detalle);

-- VISTAS

CREATE VIEW v_capacitacion as
    select
        c.capacitacion_id id,
        c.capacitacion_realizada realizada,
        c.capacitacion_fechacapacitacion fecha,
        (select n.nomenclador_descripcion from nomenclador n where n.nomenclador_id = c.tipo_capacitacion) tipo,
        (select count(persona_id) from persona_capacitacion pc where pc.capacitacion_id = c.capacitacion_id) evaluados,
        (select count(persona_id) from persona_capacitacion pc where pc.capacitacion_id = c.capacitacion_id and capacitacion_puntajeobtenido > 70) aprobados
        from capacitacion c 
        where c.tipo_capacitacion = 64;


CREATE VIEW v_prueba_alcoholemia as
    select
        c.capacitacion_id id,
        c.capacitacion_realizada realizada,
        c.capacitacion_fechacapacitacion fecha,
        (select n.nomenclador_descripcion from nomenclador n where n.nomenclador_id = c.tipo_capacitacion) tipo,
        (select count(persona_id) from persona_capacitacion pc where pc.capacitacion_id = c.capacitacion_id) evaluados,
        (select count(persona_id) from persona_capacitacion pc where pc.capacitacion_id = c.capacitacion_id and capacitacion_puntajeobtenido > 0) aprobados
        from capacitacion c 
        where c.tipo_capacitacion = 65;


create view v_mantenimiento as
select
    *
    from accion_mantenimiento join accion using (accion_id);


create view v_alistamiento as
select
    *
    from accion_alistamiento join accion using (accion_id);
