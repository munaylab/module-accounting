package org.munaylab.accounting

import org.munaylab.accounting.Categoria
import org.munaylab.accounting.CategoriaCommand
import org.munaylab.accounting.Asiento
import org.munaylab.accounting.AsientoCommand
import org.munaylab.accounting.TipoAsiento

import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class BalanceServiceSpec extends UnitTestBuilder
        implements ServiceUnitTest<BalanceService>, DataTest {

    void setupSpec() {
        mockDomains Asiento, Categoria
    }

    void 'obtener nuevo egreso desde un command'() {
        given:
        def command = crearAsientoCommandConDatos(EJEMPLO_DE_EGRESO)
        when:
        def egreso = service.obtenerAsientoDeCommand(command)
        then:
        comprobarQueAsientoYCommandSonIguales(egreso, command)
    }

    void 'obtener egreso existente desde un command'() {
        given:
        crearAsientoConDatos(EJEMPLO_DE_EGRESO, EJEMPLO_DE_CATEGORIA_EGRESO).save()
        and:
        def command = crearAsientoCommandConDatos(EJEMPLO_DE_EGRESO + [id: 1])
        when:
        def egreso = service.obtenerAsientoDeCommand(command)
        then:
        comprobarQueAsientoYCommandSonIguales(egreso, command)
    }

    void 'agregar un nuevo egreso con una nueva categoria'() {
        given:
        def command = crearAsientoCommandConDatos(EJEMPLO_DE_EGRESO, EJEMPLO_DE_CATEGORIA_EGRESO)
        when:
        def egreso = service.actualizarAsiento(command)
        then:
        comprobarQueElAsientoSeGuardo(egreso)
    }

    void 'agregar un nuevo egreso con una categoria existente'() {
        given:
        crearCategoriaConDatos(EJEMPLO_DE_CATEGORIA_EGRESO).save(failOnError: true)
        and:
        def command = crearAsientoCommandConDatos(EJEMPLO_DE_EGRESO, [id: 1])
        when:
        def egreso = service.actualizarAsiento(command)
        then:
        comprobarQueElAsientoSeGuardo(egreso)
    }

    void 'modificar un egreso existente'() {
        given:
        crearAsientoConDatos(EJEMPLO_DE_EGRESO, EJEMPLO_DE_CATEGORIA_EGRESO).save()
        def command = crearAsientoCommandConDatos(EJEMPLO_DE_EGRESO_MODIFICADO + [id: 1], [id: 1])
        when:
        def egresoModificado = service.actualizarAsiento(command)
        then:
        comprobarQueElAsientoSeGuardo(egresoModificado)
        comprobarQueElAsientoSeModifico(egresoModificado, EJEMPLO_DE_EGRESO)
        Asiento.count() == 1
    }

    // void 'cancelar egreso'() {
    //     given:
    //     def org = new Organizacion(DATOS_ORG_VERIFICADA).save(flush: true)
    //     def categoriaEgreso = new Categoria(DATOS_CATEGORIA_EGRESO)
    //     def egreso = new Asiento(DATOS_EGRESO).with {
    //         tipo            = TipoAsiento.EGRESO
    //         categoria       = categoriaEgreso
    //         organizacion    = org
    //         it
    //     }.save(flush: true)
    //     when:
    //     service.cancelarAsiento(egreso.id)
    //     then:
    //     Asiento.countByEnabled(true) == 0
    //     Asiento.countByEnabled(false) == 1
    //     Categoria.count() == 1
    // }
    // void 'agregar ingreso'() {
    //     given:
    //     def org = new Organizacion(DATOS_ORG_VERIFICADA).save(flush: true)
    //     and:
    //     def command = crearCommand(DATOS_INGRESO, DATOS_CATEGORIA_INGRESO)
    //     when:
    //     def ingreso = service.actualizarAsiento(command)
    //     then:
    //     ingreso != null && Asiento.countByEnabled(true) == 1
    //     Categoria.count() == 1
    // }
    // void 'modificar ingreso'() {
    //     given:
    //     def org = new Organizacion(DATOS_ORG_VERIFICADA).save(flush: true)
    //     def categoriaIngreso = new Categoria(DATOS_CATEGORIA_INGRESO)
    //     def ingreso = new Asiento(DATOS_INGRESO).with {
    //         tipo            = TipoAsiento.INGRESO
    //         categoria       = categoriaIngreso
    //         organizacion    = org
    //         it
    //     }.save(flush: true)
    //     and:
    //     def command = crearCommand(DATOS_INGRESO_MODIFICADO, DATOS_CATEGORIA_INGRESO_MODIFICADO)
    //     when:
    //     ingreso = service.actualizarAsiento(command)
    //     then:
    //     assert ingreso.enabled == true
    //     ingreso != null && Asiento.countByEnabled(true) == 1
    //     ingreso.monto == command.monto && Asiento.get(1).monto == command.monto
    //     ingreso.detalle == command.detalle && Asiento.get(1).detalle == command.detalle
    //     Categoria.count() == 1
    // }
    // void 'cancelar ingreso'() {
    //     given:
    //     def org = new Organizacion(DATOS_ORG_VERIFICADA).save(flush: true)
    //     def categoriaIngreso = new Categoria(DATOS_CATEGORIA_INGRESO)
    //     def ingreso = new Asiento(DATOS_INGRESO).with {
    //         tipo            = TipoAsiento.INGRESO
    //         categoria       = categoriaIngreso
    //         organizacion    = org
    //         it
    //     }.save(flush: true)
    //     when:
    //     service.cancelarAsiento(ingreso.id)
    //     then:
    //     Asiento.countByEnabled(true) == 0
    //     Asiento.countByEnabled(false) == 1
    //     Categoria.count() == 1
    // }
    // void 'crear categoria'() {
    //     given:
    //     def command = new CategoriaCommand(DATOS_CATEGORIA_INGRESO)
    //     when:
    //     service.actualizarCategoria(command)
    //     then:
    //     Categoria.count() == 1
    // }
    // void 'crear subcategoria'() {
    //     given:
    //     new Categoria(DATOS_CATEGORIA_INGRESO).save(flush: true)
    //     def command = new CategoriaCommand(DATOS_CATEGORIA_INGRESO_MODIFICADO)
    //     command.id = null
    //     command.idCategoriaPadre = 1
    //     when:
    //     def categoria = service.actualizarCategoria(command)
    //     then:
    //     categoria != null && Categoria.count() == 2
    //     Categoria.get(1).subcategorias.size() == 1
    // }
    // void 'modificar categoria'() {
    //     given:
    //     new Categoria(DATOS_CATEGORIA_INGRESO).save(flush: true)
    //     def command = new CategoriaCommand(DATOS_CATEGORIA_INGRESO_MODIFICADO)
    //     when:
    //     def categoria = service.actualizarCategoria(command)
    //     then:
    //     categoria != null && Categoria.count() == 1
    //     categoria.nombre == command.nombre
    //     categoria.detalle == command.detalle
    //     Categoria.get(1).nombre == command.nombre
    //     Categoria.get(1).detalle == command.detalle
    // }
    // void 'obtener categorias de egresos'() {
    //     given:
    //     def categoria = new Categoria(DATOS_CATEGORIA_EGRESO).save(flush: true)
    //     5.times {
    //         def subcategoria = new Categoria().with {
    //             nombre  = "subcategoria $it"
    //             detalle = "subcategoria detalle"
    //             tipo    = TipoAsiento.EGRESO
    //             it
    //         }
    //         categoria.addToSubcategorias(subcategoria)
    //     }
    //     categoria.save(flush: true)
    //     when:
    //     def categorias = service.obtenerCategorias(TipoAsiento.EGRESO)
    //     then:
    //     categorias.size() == 1
    //     categorias.first().subcategorias.size() == 5
    // }
    // void 'obtener categorias de ingresos'() {
    //     given:
    //     def categoria = new Categoria(DATOS_CATEGORIA_INGRESO).save(flush: true)
    //     5.times {
    //         def subcategoria = new Categoria().with {
    //             nombre  = "subcategoria $it"
    //             detalle = "subcategoria detalle"
    //             tipo    = TipoAsiento.INGRESO
    //             it
    //         }
    //         categoria.addToSubcategorias(subcategoria)
    //     }
    //     categoria.save(flush: true)
    //     when:
    //     def categorias = service.obtenerCategorias(TipoAsiento.INGRESO)
    //     then:
    //     categorias.size() == 1
    //     categorias.first().subcategorias.size() == 5
    // }

    /* Metodo groupProperty no funciona en unit test
    void 'calcular balance sin fechas'() {
        given:
        def org = Builder.crearOrganizacionConDatos().save(flush: true)
        def categoriaEgresos = Builder.crearCategoriaEgreso().save(flush: true)
        def categoriaIngresos = Builder.crearCategoriaIngreso().save(flush: true)
        crearAsientos(org, categoriaEgresos, TipoAsiento.EGRESO, [egreso1, egreso2, egreso3])
        crearAsientos(org, categoriaIngresos, TipoAsiento.INGRESO, [ingreso1, ingreso2, ingreso3])
        expect:
        service.calcularBalanceTotal(org) == total
        where:
        egreso1 | egreso2 | egreso3 | ingreso1 | ingreso2 | ingreso3 | total
        10.0    | 10.0    | 10.0    | 20.0     | 20.0     | 20.0     | 30.0
        20.0    | 10.0    | 10.0    | 20.0     | 20.0     | 20.0     | 20.0
        20.0    | 20.0    | 10.0    | 10.0     | 10.0     | 10.0     | -20.0
    }
    */
    /* Metodo groupProperty no funciona en unit test
    void 'calcular balance con fechas'() {
        given:
        def org = Builder.crearOrganizacionConDatos().save(flush: true)
        def categoriaEgresos = Builder.crearCategoriaEgreso().save(flush: true)
        def categoriaIngresos = Builder.crearCategoriaIngreso().save(flush: true)
        crearAsientosConFechas(org, categoriaEgresos, TipoAsiento.EGRESO, egreso)
        crearAsientosConFechas(org, categoriaIngresos, TipoAsiento.INGRESO, ingreso)
        expect:
        service.calcularBalanceTotal(org, desde, hasta) == total
        where:
        egreso                | ingreso                | total | desde         | hasta
        [40.0, new Date() -2] | [100.0, new Date() -3] | 60.0  | new Date() -3 | new Date() -1
        [90.0, new Date() -5] | [100.0, new Date() -3] | 100.0 | new Date() -3 | new Date() -1
        [90.0, new Date() -5] | [100.0, new Date() -5] | 0.0   | new Date() -1 | new Date() -1
        [90.0, new Date() -1] | [50.0, new Date() -1]  | -40.0 | new Date() -2 | new Date() -1
    }
    */
    // void crearAsientos(org, _categoria, tipoAsiento, values) {
    //     values.each { valor ->
    //         new Asiento().with {
    //             fecha           = new Date()
    //             detalle         = 'asiento'
    //             tipo            = tipoAsiento
    //             categoria       = _categoria
    //             organizacion    = org
    //             monto           = valor
    //             it
    //         }.save(flush: true, failOnError: true)
    //     }
    // }
    // void crearAsientosConFechas(org, _categoria, tipoAsiento, value) {
    //     new Asiento().with {
    //         fecha           = value[1]
    //         detalle         = 'asiento'
    //         tipo            = tipoAsiento
    //         categoria       = _categoria
    //         organizacion    = org
    //         monto           = value[0]
    //         it
    //     }.save(flush: true, failOnError: true)
    // }
    //
    // void 'obtener egresos'() {
    //     given:
    //     def org = new Organizacion(DATOS_ORG_VERIFICADA).save(flush: true)
    //     def categoria = new Categoria(DATOS_CATEGORIA_EGRESO).save(flush: true)
    //     crearAsientos(org, categoria, TipoAsiento.EGRESO, [10.0, 20.0, 30.0, 40.0])
    //     when:
    //     def list = service.obtenerEgresos(org, categoria.nombre, new Date() -1, new Date() + 1)
    //     then:
    //     list.size() == 4
    // }
    // void 'obtener egresos de una categoria'() {
    //     given:
    //     def org = new Organizacion(DATOS_ORG_VERIFICADA).save(flush: true)
    //     def categoria = new Categoria(DATOS_CATEGORIA_EGRESO).save(flush: true)
    //     def otraCategoria = new Categoria(DATOS_CATEGORIA_EGRESO_MODIFICADO).save(flush: true)
    //     crearAsientos(org, categoria, TipoAsiento.EGRESO, [10.0, 20.0, 30.0, 40.0])
    //     crearAsientos(org, otraCategoria, TipoAsiento.EGRESO, [10.0, 20.0, 30.0, 40.0])
    //     when:
    //     def list = service.obtenerEgresos(org, otraCategoria.nombre)
    //     then:
    //     list.size() == 4
    // }
    // void 'obtener egresos entre fechas'() {
    //     given:
    //     def org = new Organizacion(DATOS_ORG_VERIFICADA).save(flush: true)
    //     def categoria = new Categoria(DATOS_CATEGORIA_EGRESO).save(flush: true)
    //     crearAsientosConFechas(org, categoria, TipoAsiento.EGRESO, egreso)
    //     crearAsientosConFechas(org, categoria, TipoAsiento.EGRESO, egreso)
    //     crearAsientosConFechas(org, categoria, TipoAsiento.EGRESO, otroEgreso)
    //     when:
    //     def list = service.obtenerEgresosEntre(org, new Date() -1, new Date() +1)
    //     then:
    //     list.size() == 2
    //     where:
    //     egreso                | otroEgreso
    //     [40.0, new Date() -1] | [30.0, new Date() -3]
    // }
    // void 'obtener egresos de categoria entre fechas'() {
    //     given:
    //     def org = new Organizacion(DATOS_ORG_VERIFICADA).save(flush: true)
    //     def categoria = new Categoria(DATOS_CATEGORIA_EGRESO).save(flush: true)
    //     def otraCategoria = new Categoria(DATOS_CATEGORIA_EGRESO_MODIFICADO).save(flush: true)
    //     crearAsientosConFechas(org, categoria, TipoAsiento.EGRESO, egreso)
    //     crearAsientosConFechas(org, otraCategoria, TipoAsiento.EGRESO, egreso)
    //     crearAsientosConFechas(org, categoria, TipoAsiento.EGRESO, otroEgreso)
    //     when:
    //     def list = service.obtenerEgresosDeCategoriaEntre(org, categoria.nombre, new Date() -1, new Date() +1)
    //     then:
    //     list.size() == 1
    //     where:
    //     egreso                | otroEgreso
    //     [40.0, new Date() -1] | [30.0, new Date() -3]
    // }
    // void 'obtener ingresos'() {
    //     given:
    //     def org = new Organizacion(DATOS_ORG_VERIFICADA).save(flush: true)
    //     def categoria = new Categoria(DATOS_CATEGORIA_INGRESO).save(flush: true)
    //     crearAsientos(org, categoria, TipoAsiento.INGRESO, [10.0, 20.0, 30.0, 40.0])
    //     when:
    //     def list = service.obtenerIngresos(org, categoria.nombre, new Date(), new Date() + 1)
    //     then:
    //     list.size() == 4
    // }
    // void 'obtener ingresos de una categoria'() {
    //     given:
    //     def org = new Organizacion(DATOS_ORG_VERIFICADA).save(flush: true)
    //     def categoria = new Categoria(DATOS_CATEGORIA_INGRESO).save(flush: true)
    //     def otraCategoria = new Categoria(DATOS_CATEGORIA_INGRESO_MODIFICADO).save(flush: true)
    //     crearAsientos(org, categoria, TipoAsiento.INGRESO, [10.0, 20.0, 30.0, 40.0])
    //     crearAsientos(org, otraCategoria, TipoAsiento.INGRESO, [10.0, 20.0, 30.0, 40.0])
    //     when:
    //     def list = service.obtenerIngresos(org, categoria.nombre)
    //     then:
    //     list.size() == 4
    // }
    // void 'obtener ingresos entre fechas'() {
    //     given:
    //     def org = new Organizacion(DATOS_ORG_VERIFICADA).save(flush: true)
    //     def categoria = new Categoria(DATOS_CATEGORIA_INGRESO).save(flush: true)
    //     crearAsientosConFechas(org, categoria, TipoAsiento.INGRESO, ingreso)
    //     crearAsientosConFechas(org, categoria, TipoAsiento.INGRESO, ingreso)
    //     crearAsientosConFechas(org, categoria, TipoAsiento.INGRESO, otroIngreso)
    //     when:
    //     def list = service.obtenerIngresosEntre(org, new Date() -1, new Date() +1)
    //     then:
    //     list.size() == 2
    //     where:
    //     ingreso               | otroIngreso
    //     [40.0, new Date() -1] | [30.0, new Date() -3]
    // }
    // void 'obtener ingresos de categoria entre fechas'() {
    //     given:
    //     def org = new Organizacion(DATOS_ORG_VERIFICADA).save(flush: true)
    //     def categoria = new Categoria(DATOS_CATEGORIA_INGRESO).save(flush: true)
    //     def otraCategoria = new Categoria(DATOS_CATEGORIA_INGRESO_MODIFICADO).save(flush: true)
    //     crearAsientosConFechas(org, categoria, TipoAsiento.INGRESO, ingreso)
    //     crearAsientosConFechas(org, otraCategoria, TipoAsiento.INGRESO, ingreso)
    //     crearAsientosConFechas(org, categoria, TipoAsiento.INGRESO, otroIngreso)
    //     when:
    //     def list = service.obtenerIngresosDeCategoriaEntre(org, categoria.nombre, new Date() -1, new Date() +1)
    //     then:
    //     list.size() == 1
    //     where:
    //     ingreso               | otroIngreso
    //     [40.0, new Date() -1] | [30.0, new Date() -3]
    // }
    /*void 'obtener reporte de ingresos mensual'() {
        given:
        def org = Builder.crearOrganizacionConDatos().save(flush: true)
        def cat = Builder.crearCategoria('categoria', TipoAsiento.INGRESO).save(flush: true)
        5.times {
            new Asiento(monto: 100.0, detalle: 'ingreso', fecha: new Date().parse('dd/MM/yyyy', "01/01/2017"), mes: 1,
                categoria: cat, tipo: TipoAsiento.INGRESO, organizacion: org).save(failOnError: true)
        }
        5.times {
            new Asiento(monto: 100.0, detalle: 'ingreso', fecha: new Date().parse('dd/MM/yyyy', "01/05/2017"), mes: 5,
                categoria: cat, tipo: TipoAsiento.INGRESO, organizacion: org).save(failOnError: true)
        }
        assert Asiento.count() == 10
        when:
        def list = service.obtenerBalancePorPeriodo(org, TipoAsiento.INGRESO, 'mes')
        then:
        list.size() == 2
    }

    private Date date(dia, mes, anio = 2017) {
        new Date().parse('dd/MM/yyyy', "$dia/$mes/$anio")
    }*/
}
