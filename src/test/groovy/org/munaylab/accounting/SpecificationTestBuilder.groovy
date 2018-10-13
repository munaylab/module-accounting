package org.munaylab.accounting

import spock.lang.Specification

class SpecificationTestBuilder extends Specification implements UnitTestDataSample {

    protected Asiento crearAsientoConDatos(datos, datosCategoria) {
        def asiento = new Asiento(datos)
        asiento.categoria = new Categoria(datosCategoria)
        return asiento
    }

    protected Categoria crearCategoriaConDatos(datos) {
        return new Categoria(datos)
    }

    protected void comprobarQueElAsientoFueGuardado(Asiento asiento) {
        assert asiento.id != null
        def asientoGuardado = Asiento.get(asiento.id)
        assert asiento.id == asientoGuardado.id
        assert asiento.tipo == asientoGuardado.tipo
        assert asiento.fecha == asientoGuardado.fecha
        assert asiento.monto == asientoGuardado.monto
        assert asiento.detalle == asientoGuardado.detalle
        assert asiento.idEntity == asientoGuardado.idEntity
        assert Categoria.count() == 1
        assert Asiento.countByEnabled(true) == 1
    }

    protected void comprobarQueElAsientoFueModificado(Asiento asiento, datosAsientoModificado) {
        assert asiento.tipo == datosAsientoModificado.tipo
        assert asiento.fecha != datosAsientoModificado.fecha
        assert asiento.monto != datosAsientoModificado.monto
        assert asiento.detalle != datosAsientoModificado.detalle
        assert asiento.idEntity == datosAsientoModificado.idEntity
    }

    protected AsientoCommand crearAsientoCommandConDatos(datos, datosCategoria = null) {
        def command = new AsientoCommand(datos)
        // command.id = datos.id
        if (datosCategoria) {
            command.categoria = new CategoriaCommand(datosCategoria)
            if (datosCategoria.id)
                command.categoria.id = datosCategoria.id
        }
        return command
    }

    protected void comprobarQueAsientoYCommandSonIguales(Asiento asiento, AsientoCommand command) {
        assert asiento.id == command.id
        assert asiento.tipo == command.tipo
        assert asiento.fecha == command.fecha
        assert asiento.monto == command.monto
        assert asiento.detalle == command.detalle
        assert asiento.idEntity == command.idEntity
    }

    protected void comprobarQueElAsientoFueCancelado(Asiento asiento) {
        assert asiento.enabled == false
        assert Asiento.count() == 1
        assert Asiento.countByEnabled(true) == 0
    }

    protected void comprobarQueLaCategoriaFueGuardada(Categoria categoria) {
        assert categoria.id != null
        def categoriaGuardada = Categoria.get(categoria.id)
        assert categoria.id == categoriaGuardada.id
        assert categoria.tipo == categoriaGuardada.tipo
        assert categoria.nombre == categoriaGuardada.nombre
        assert categoria.detalle == categoriaGuardada.detalle
    }

    protected void comprobarQueLaCategoriaFueModificada(Categoria categoria, datosCategoriaModificada) {
        assert categoria.id == datosCategoriaModificada.id
        assert categoria.tipo == datosCategoriaModificada.tipo
        assert categoria.nombre == datosCategoriaModificada.nombre
        assert categoria.detalle == datosCategoriaModificada.detalle
    }

    protected Asiento crearAsiento(datos) {
        return new Asiento().with {
            fecha       = datos.fecha ?: new Date()
            detalle     = datos.detalle ?: 'asiento'
            tipo        = datos.tipo ?: TipoAsiento.EGRESO
            categoria   = datos.categoria
            idEntity    = datos.entity
            monto       = datos.monto ?: 100
            it
        }
    }

    protected Asiento crearEgreso(datos) {
        return crearAsiento(datos + [tipo: TipoAsiento.EGRESO])
    }

    protected Asiento crearIngreso(datos) {
        return crearAsiento(datos + [tipo: TipoAsiento.INGRESO])
    }

    // static def DATOS_ORG = [nombre: 'MunayLab', tipo: TipoOrganizacion.FUNDACION, nombreURL: 'org',
    //     fechaConstitucion: new Date() -10, objeto: 'brindar soluciones a las organizaciones sociales']
    // static def DATOS_DOMICILIO = [calle: 'Peat 32', numero: '570', barrio: 'San Pedrito',
    //     localidad: 'San Salvador de Jujuy', provincia: 'Jujuy']
    //
    // static RegistroCommand getRegistroCommand() {
    //     new RegistroCommand(denominacion: 'Fundación MunayLab', tipo: TipoOrganizacion.FUNDACION,
    //         nombre: 'Augusto', apellido: 'caligares', email: 'mcaligares@gmail.com',
    //         telefono: '1234567', objeto: 'brindar soluciones a las organizaciones sociales')
    // }
    // static RegistroCommand getInvalidRegistroCommand() {
    //     new RegistroCommand(denominacion: 'Fundación MunayLab', tipo: TipoOrganizacion.FUNDACION,
    //         nombre: 'Augusto', apellido: 'caligares',
    //         objeto: 'brindar soluciones a las organizaciones sociales')
    // }
    // static ConfirmacionCommand getConfirmacionCommand() {
    //     new ConfirmacionCommand(codigo: 'codigo', password1: 'asdQWE123', password2: 'asdQWE123')
    // }
    // static Organizacion crearOrganizacionConDatos(datos = DATOS_ORG) {
    //     new Organizacion(nombre: datos.nombre, nombreURL: datos.nombreURL, objeto: datos.objeto,
    //         tipo: datos.tipo, fechaConstitucion: datos.fechaConstitucion, estado: EstadoOrganizacion.VERIFICADA)
    // }
    // static Domicilio crearDomicilioConDatos(datos = DATOS_DOMICILIO) {
    //     new Domicilio(calle: datos.calle, numero: datos.numero, barrio: datos.barrio,
    //             localidad: datos.localidad, provincia: datos.provincia)
    // }
    // static OrganizacionCommand getOrganizacionCommand() {
    //     new OrganizacionCommand(id: 1, fechaConstitucion: new Date() -100,
    //         tipo: TipoOrganizacion.ASOCIACION_CIVIL, nombre: 'Fundacion Internacional MunayLab',
    //         objeto: 'brindar soluciones innovadoras a ONGs')
    // }
    // static DomicilioCommand getDomicilioCommand() {
    //     new DomicilioCommand(id: 1, calle: 'Reconquista', numero: '1125', barrio: 'Centro',
    //         localidad: 'Cuidad Autonoma de Buenos Aires', provincia: 'Buenos Aires')
    // }
    // static OrganizacionCommand getOrganizacionConDomicilioCommand() {
    //     def command = organizacionCommand
    //     command.domicilio = domicilioCommand
    //     return command
    // }
    // static ContactoCommand getContactoCommand() {
    //     new ContactoCommand(value: 'mcaligares@gmail.com', tipo: TipoContacto.EMAIL)
    // }
    // static Contacto crearContacto() {
    //     new Contacto(value: 'mcaligares@gmail.com', tipo: TipoContacto.EMAIL)
    // }
    // static UserCommand getUserCommand() {
    //     new UserCommand(nombre: 'Augusto', apellido: 'Caligares', username: 'mcaligares@gmail.com')
    // }
    // static UserCommand getAdminCommand() {
    //     def command = userCommand
    //     command.tipo = TipoUsuario.findByNombre('ADMINISTRADOR').id
    //     return command
    // }
    // static UserCommand getMiembroCommand() {
    //     def command = userCommand
    //     command.tipo = TipoUsuario.findByNombre('MIEMBRO').id
    //     command.cargo = 'Director Ejecutivo'
    //     return command
    // }
    // static User crearUser() {
    //     new User(nombre: 'Augusto', apellido: 'Caligares',
    //         username: 'mcaligares@gmail.com', password: 'password')
    // }
    // static UserOrganizacion crearAdminOrganizacion(Organizacion org) {
    //     new UserOrganizacion(user: crearUser(), organizacion: org,
    //             tipo: TipoUsuario.findByNombre('ADMINISTRADOR'))
    // }
    // static UserOrganizacion crearMiembroOrganizacion(Organizacion org) {
    //     new UserOrganizacion(user: crearUser(), organizacion: org,
    //             tipo: TipoUsuario.findByNombre('MIEMBRO'), cargo: 'Director Ejecutivo')
    // }
    // static AsientoCommand getEgresoCommand() {
    //     new AsientoCommand(monto: 100.0, fecha: new Date(), detalle: 'detalle de asiento', esIngreso: false, orgId: 1)
    // }
    // static AsientoCommand getIngresoCommand() {
    //     new AsientoCommand(monto: 100.0, fecha: new Date(), detalle: 'detalle de asiento', esIngreso: true, orgId: 1)
    // }
    // static CategoriaCommand getCategoriaEgresoCommand() {
    //     new CategoriaCommand(nombre: 'nueva categoria', detalle: 'detalle', tipo: TipoAsiento.EGRESO)
    // }
    // static CategoriaCommand getCategoriaIngresoCommand() {
    //     new CategoriaCommand(nombre: 'nueva categoria', detalle: 'detalle', tipo: TipoAsiento.INGRESO)
    // }
    // static Categoria crearCategoria(String nombre = 'nueva categoria', TipoAsiento tipo = TipoAsiento.INGRESO) {
    //     new Categoria(nombre: nombre, tipo: tipo)
    // }
    // static Categoria crearCategoriaEgreso() {
    //     new Categoria(nombre: 'nueva categoria', tipo: TipoAsiento.EGRESO)
    // }
    // static Categoria crearCategoriaIngreso() {
    //     new Categoria(nombre: 'nueva categoria', tipo: TipoAsiento.INGRESO)
    // }
    // static Asiento crearEgreso() {
    //     new Asiento(monto: 10.0, detalle: 'egreso', fecha: new Date(), categoria: crearCategoria(), tipo: TipoAsiento.EGRESO)
    // }
    // static Asiento crearIngreso() {
    //     new Asiento(monto: 10.0, detalle: 'ingreso', fecha: new Date(), categoria: crearCategoria(), tipo: TipoAsiento.INGRESO)
    // }
    // static Organizacion crearOrganizacionCompleta() {
    //     def actividad = Builder.crearActividad()
    //     def proyecto = Builder.crearProyecto().addToActividades(actividad)
    //     def programa = Builder.crearPrograma().addToProyectos(proyecto)
    //     def evento = Builder.crearEvento()
    //     evento.direccion = Builder.crearDomicilioConDatos()
    //     def org = Builder.crearOrganizacionConDatos()
    //     org.domicilio = Builder.crearDomicilioConDatos()
    //     org.addToContactos(Builder.crearContacto())
    //     org.addToProgramas(programa)
    //     org.addToEventos(evento)
    // }

}
