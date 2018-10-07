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

    void 'actualizar un asiento con errores'() {
        given:
        def command = new AsientoCommand(detalle: detalle, monto: monto)
        expect:
        service.actualizarAsiento(command) == null
        where:
        detalle   | monto
        '.'       | 0
        'detalle' | -1
    }

    void 'cancelar egreso'() {
        given:
        def egreso = crearAsientoConDatos(EJEMPLO_DE_EGRESO, EJEMPLO_DE_CATEGORIA_EGRESO).save()
        when:
        def egresoCancelado = service.cancelarAsiento(egreso.id)
        then:
        comprobarQueElAsientoFueCancelado(egresoCancelado)
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
        comprobarQueElAsientoFueGuardado(egreso)
    }

    void 'agregar un nuevo egreso con una categoria existente'() {
        given:
        crearCategoriaConDatos(EJEMPLO_DE_CATEGORIA_EGRESO).save(failOnError: true)
        and:
        def command = crearAsientoCommandConDatos(EJEMPLO_DE_EGRESO, [id: 1])
        when:
        def egreso = service.actualizarAsiento(command)
        then:
        comprobarQueElAsientoFueGuardado(egreso)
    }

    void 'modificar un egreso existente'() {
        given:
        crearAsientoConDatos(EJEMPLO_DE_EGRESO, EJEMPLO_DE_CATEGORIA_EGRESO).save()
        def command = crearAsientoCommandConDatos(EJEMPLO_DE_EGRESO_MODIFICADO + [id: 1], [id: 1])
        when:
        def egresoModificado = service.actualizarAsiento(command)
        then:
        Asiento.count() == 1
        comprobarQueElAsientoFueGuardado(egresoModificado)
        comprobarQueElAsientoFueModificado(egresoModificado, EJEMPLO_DE_EGRESO)
    }

    void 'cancelar ingreso'() {
        given:
        def ingreso = crearAsientoConDatos(EJEMPLO_DE_INGRESO, EJEMPLO_DE_CATEGORIA_INGRESO).save()
        when:
        def ingresoCancelado = service.cancelarAsiento(ingreso.id)
        then:
        comprobarQueElAsientoFueCancelado(ingresoCancelado)
    }

    void 'obtener nuevo ingreso desde un command'() {
        given:
        def command = crearAsientoCommandConDatos(EJEMPLO_DE_INGRESO)
        when:
        def ingreso = service.obtenerAsientoDeCommand(command)
        then:
        comprobarQueAsientoYCommandSonIguales(ingreso, command)
    }

    void 'obtener ingreso existente desde un command'() {
        given:
        crearAsientoConDatos(EJEMPLO_DE_INGRESO, EJEMPLO_DE_CATEGORIA_INGRESO).save()
        and:
        def command = crearAsientoCommandConDatos(EJEMPLO_DE_INGRESO + [id: 1])
        when:
        def ingreso = service.obtenerAsientoDeCommand(command)
        then:
        comprobarQueAsientoYCommandSonIguales(ingreso, command)
    }

    void 'agregar un nuevo ingreso con una nueva categoria'() {
        given:
        def command = crearAsientoCommandConDatos(EJEMPLO_DE_INGRESO, EJEMPLO_DE_CATEGORIA_INGRESO)
        when:
        def ingreso = service.actualizarAsiento(command)
        then:
        comprobarQueElAsientoFueGuardado(ingreso)
    }

    void 'agregar un nuevo ingreso con una categoria existente'() {
        given:
        crearCategoriaConDatos(EJEMPLO_DE_CATEGORIA_INGRESO).save(failOnError: true)
        and:
        def command = crearAsientoCommandConDatos(EJEMPLO_DE_INGRESO, [id: 1])
        when:
        def ingreso = service.actualizarAsiento(command)
        then:
        comprobarQueElAsientoFueGuardado(ingreso)
    }

    void 'modificar un ingreso existente'() {
        given:
        crearAsientoConDatos(EJEMPLO_DE_INGRESO, EJEMPLO_DE_CATEGORIA_INGRESO).save()
        def command = crearAsientoCommandConDatos(EJEMPLO_DE_INGRESO_MODIFICADO + [id: 1], [id: 1])
        when:
        def ingresoModificado = service.actualizarAsiento(command)
        then:
        Asiento.count() == 1
        comprobarQueElAsientoFueGuardado(ingresoModificado)
        comprobarQueElAsientoFueModificado(ingresoModificado, EJEMPLO_DE_INGRESO)
    }

    void 'crear nueva categoria desde command'() {
        given:
        def command = new CategoriaCommand(EJEMPLO_DE_CATEGORIA_INGRESO)
        when:
        def categoria = service.actualizarCategoria(command)
        then:
        Categoria.count() == 1
        comprobarQueLaCategoriaFueGuardada(categoria)
    }

    void 'crear nueva sub-categoria desde command'() {
        given:
        new Categoria(EJEMPLO_DE_CATEGORIA_PADRE_INGRESO).save()
        def command = new CategoriaCommand(EJEMPLO_DE_CATEGORIA_INGRESO)
        command.idCategoriaPadre = 1
        when:
        def categoria = service.actualizarCategoria(command)
        then:
        comprobarQueLaCategoriaFueGuardada(categoria)
        Categoria.count() == 2 && Categoria.get(1).subcategorias.size() == 1
    }

    void 'modificar una categoria existente'() {
        given:
        new Categoria(EJEMPLO_DE_CATEGORIA_PADRE_INGRESO).save()
        def command = new CategoriaCommand(EJEMPLO_DE_CATEGORIA_INGRESO + [id: 1])
        when:
        def categoria = service.actualizarCategoria(command)
        then:
        Categoria.count() == 1
        comprobarQueLaCategoriaFueModificada(categoria, EJEMPLO_DE_CATEGORIA_INGRESO + [id: 1])
    }

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
