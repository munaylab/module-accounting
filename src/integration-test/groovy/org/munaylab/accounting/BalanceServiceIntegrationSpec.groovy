package org.munaylab.accounting

import grails.testing.mixin.integration.Integration
import grails.transaction.Rollback
import org.springframework.beans.factory.annotation.Autowired

@Rollback
@Integration
class BalanceServiceIntegrationSpec extends SpecificationTestBuilder {

    @Autowired BalanceService service

    void 'calcular balance cero'() {
        expect:
        service.calcularBalanceTotal(1) == 0
    }

    void 'calcular balance de un egreso'() {
        given:
        long idEntity = 1; double monto = 10
        def categoriaEgresos = new Categoria(EJEMPLO_DE_CATEGORIA_EGRESO).save()
        and:
        crearEgreso([categoria: categoriaEgresos, entity: idEntity, monto: monto]).save()
        expect:
        service.calcularBalanceTotal(idEntity) == -monto
    }

    void 'calcular balance de un ingreso'() {
        given:
        long idEntity = 1; double monto = 10
        def categoriaIngresos = new Categoria(EJEMPLO_DE_CATEGORIA_INGRESO).save()
        and:
        crearIngreso([categoria: categoriaIngresos, entity: idEntity, monto: monto]).save()
        expect:
        service.calcularBalanceTotal(idEntity) == monto
    }

    void 'calcular balance sin fechas'() {
        given:
        long idEntity = 1
        def categoriaEgresos = new Categoria(EJEMPLO_DE_CATEGORIA_EGRESO).save()
        def categoriaIngresos = new Categoria(EJEMPLO_DE_CATEGORIA_INGRESO).save()
        and:
        [egreso1, egreso2, egreso3].each { monto ->
            crearEgreso([categoria: categoriaEgresos, entity: idEntity, monto: monto]).save()
        }
        [ingreso1, ingreso2, ingreso3].each { monto ->
            crearIngreso([categoria: categoriaIngresos, entity: idEntity, monto: monto]).save()
        }
        expect:
        service.calcularBalanceTotal(idEntity) == total
        where:
        egreso1 | egreso2 | egreso3 | ingreso1 | ingreso2 | ingreso3 | total
        10.0    | 10.0    | 10.0    | 20.0     | 20.0     | 20.0     | 30.0
        20.0    | 10.0    | 10.0    | 20.0     | 20.0     | 20.0     | 20.0
        20.0    | 20.0    | 10.0    | 10.0     | 10.0     | 10.0     | -20.0
    }

    void 'calcular balance con fechas'() {
        given:
        Long idEntity = 1
        def categoriaEgresos = new Categoria(EJEMPLO_DE_CATEGORIA_EGRESO).save()
        def categoriaIngresos = new Categoria(EJEMPLO_DE_CATEGORIA_INGRESO).save()
        and:
        crearEgreso([categoria: categoriaEgresos, entity: idEntity] + egreso).save()
        crearIngreso([categoria: categoriaIngresos, entity: idEntity] + ingreso).save()
        expect:
        service.calcularBalanceTotal(idEntity, desde, hasta) == total
        where:
        egreso                          | ingreso                          | total | desde     | hasta
        [monto: 40.0, fecha: fecha(-2)] | [monto: 100.0, fecha: fecha(-3)] | 60.0  | fecha(-3) | fecha(-1)
        [monto: 90.0, fecha: fecha(-5)] | [monto: 100.0, fecha: fecha(-3)] | 100.0 | fecha(-3) | fecha(-1)
        [monto: 90.0, fecha: fecha(-5)] | [monto: 100.0, fecha: fecha(-5)] | 0.0   | fecha(-1) | fecha(-1)
        [monto: 90.0, fecha: fecha(-1)] | [monto: 50.0, fecha: fecha(-1)]  | -40.0 | fecha(-2) | fecha(-1)
    }

    private Date fecha(int dias) {
        return new Date() + dias
    }
}
