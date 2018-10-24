package org.munaylab.accounting

enum TipoPeriodo {
    SEMANAL,
    MENSUAL,
    ANUAL

    boolean getSiEsSemanal() {
        return this == SEMANAL
    }
    boolean getSiEsMensual() {
        return this == MENSUAL
    }
    boolean getSiEsAnual() {
        return this == ANUAL
    }

    Date getFechaDesde() {
        use (groovy.time.TimeCategory) {
            switch (this) {
                case SEMANAL: return 6.week.ago
                case MENSUAL: return 1.year.ago
                case ANUAL: return 6.year.ago
                default: return new Date()
            }
        }
    }

}
