package com.bruno13palhano.core.model

internal interface Buyable {
    fun buy(
        salable: Salable,
        purchase: Purchase,
    ): Receipt

    fun cancel(
        reversible: Reversible,
        purchase: Purchase,
    ): Receipt
}
