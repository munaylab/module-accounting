package org.munaylab.accounting

enum TipoAsiento {
    EGRESO,
    INGRESO,
    NINGUNO

    boolean getSiEsEgreso() {
        return this == EGRESO
    }
    boolean getSiEsIngreso() {
        return this == INGRESO
    }
}
