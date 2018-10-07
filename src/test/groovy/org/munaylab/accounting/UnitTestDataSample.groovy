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

    // static final DATOS_EGRESO_MODIFICADO = [id: 1, monto: 10.0, fecha: new Date(), detalle: 'detalle modificado',
    //     esIngreso: false, orgId: 1]
    //
    static final EJEMPLO_DE_CATEGORIA_EGRESO = [
        nombre: 'nombre',
        detalle: 'detalle detalle',
        tipo: TipoAsiento.EGRESO
    ]
}
