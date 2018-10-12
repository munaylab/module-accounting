package org.munaylab.accounting

import grails.testing.mixin.integration.Integration
import grails.transaction.Rollback
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

@Rollback
@Integration
class BalanceServiceIntegrationSpec extends Specification {

    @Autowired BalanceService service

    void 'calcular balance sin fechas'() {
        given:
        def categoriaEgresos = new Categoria(EJEMPLO_DE_CATEGORIA_EGRESO).save()
        def categoriaIngresos = new Categoria(EJEMPLO_DE_CATEGORIA_INGRESO).save()
        crearAsientos(1, categoriaEgresos, TipoAsiento.EGRESO, [egreso1, egreso2, egreso3])
        crearAsientos(1, categoriaIngresos, TipoAsiento.INGRESO, [ingreso1, ingreso2, ingreso3])
        expect:
        service.calcularBalanceTotal(1) == total
        where:
        egreso1 | egreso2 | egreso3 | ingreso1 | ingreso2 | ingreso3 | total
        10.0    | 10.0    | 10.0    | 20.0     | 20.0     | 20.0     | 30.0
        20.0    | 10.0    | 10.0    | 20.0     | 20.0     | 20.0     | 20.0
        20.0    | 20.0    | 10.0    | 10.0     | 10.0     | 10.0     | -20.0
    }

    static final EJEMPLO_DE_CATEGORIA_EGRESO = [
        nombre: 'nombre',
        detalle: 'detalle detalle',
        tipo: TipoAsiento.EGRESO
    ]

    static final EJEMPLO_DE_CATEGORIA_INGRESO = [
        nombre: 'nombre',
        detalle: 'detalle detalle',
        tipo: TipoAsiento.INGRESO
    ]

    void crearAsientos(_idEntity, _categoria, tipoAsiento, values) {
        values.each { valor ->
            new Asiento().with {
                fecha           = new Date()
                detalle         = 'asiento'
                tipo            = tipoAsiento
                categoria       = _categoria
                idEntity        = _idEntity
                monto           = valor
                it
            }.save(flush: true, failOnError: true)
        }
    }

}
