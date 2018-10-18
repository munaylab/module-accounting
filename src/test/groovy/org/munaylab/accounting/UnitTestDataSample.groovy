package org.munaylab.accounting

import spock.lang.Specification

interface UnitTestDataSample {
    static final EJEMPLO_DE_EGRESO = [
        idEntity: 1,
        monto: 100.0,
        fecha: new Date(),
        tipo: TipoAsiento.EGRESO,
        detalle: 'detalle de asiento'
    ]

    static final EJEMPLO_DE_EGRESO_MODIFICADO = [
        idEntity: 1,
        monto: 200.0,
        fecha: new Date() -7,
        tipo: TipoAsiento.EGRESO,
        detalle: 'detalle de asiento modificado'
    ]

    static final EJEMPLO_DE_CATEGORIA_EGRESO = [
        nombre: 'categoria egreso',
        detalle: 'detalle detalle',
        tipo: TipoAsiento.EGRESO
    ]

    static final EJEMPLO_DE_INGRESO = [
        idEntity: 1,
        monto: 100.0,
        fecha: new Date(),
        tipo: TipoAsiento.INGRESO,
        detalle: 'detalle de asiento'
    ]

    static final EJEMPLO_DE_INGRESO_MODIFICADO = [
        idEntity: 1,
        monto: 200.0,
        fecha: new Date() -7,
        tipo: TipoAsiento.INGRESO,
        detalle: 'detalle de asiento modificado'
    ]

    static final EJEMPLO_DE_CATEGORIA_INGRESO = [
        nombre: 'categoria ingreso',
        detalle: 'detalle detalle',
        tipo: TipoAsiento.INGRESO
    ]

    static final EJEMPLO_DE_CATEGORIA_PADRE_INGRESO = [
        nombre: 'categoria padre',
        detalle: 'detalle detalle de padre',
        tipo: TipoAsiento.INGRESO
    ]
}
