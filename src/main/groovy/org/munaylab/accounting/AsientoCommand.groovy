package org.munaylab.accounting

class AsientoCommand implements grails.validation.Validateable {

    Long id
    Long idEntity
    Date fecha
    Double monto
    String detalle
    Programable programable
    TipoAsiento tipo
    CategoriaCommand categoria

    static constraints = {
        id nullable: true
        idEntity nullable: true
        monto min: 0d, max: 999999d
        detalle size: 5..500
        programable nullable: true
    }

    boolean getEsNuevoAsiento() {
        this.id == null
    }

}
