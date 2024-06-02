package com.bruno13palhano.core.model

interface Reversible {
    fun reverse(purchase: Purchase): Receipt
}
