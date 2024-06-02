package com.bruno13palhano.core.model

interface Salable {
    fun sell(purchase: Purchase): Receipt
}
