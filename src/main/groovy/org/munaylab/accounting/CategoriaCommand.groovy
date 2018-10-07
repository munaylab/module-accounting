package org.munaylab.accounting

class CategoriaCommand implements grails.validation.Validateable {

    Long id
    String nombre
    String detalle
    TipoAsiento tipo
    Long idCategoriaPadre

    static constraints = {
        id nullable: true
        nombre size: 5..500
        detalle nullable: true
        idCategoriaPadre nullable: true
    }

    boolean getEsNuevaCategoria() {
        id == null
    }
}
