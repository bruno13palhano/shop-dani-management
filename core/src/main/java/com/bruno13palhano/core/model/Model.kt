package com.bruno13palhano.core.model

open class Model(
    open val id: Long,
    open val timestamp: String,
)

fun Model.isNew() = id == 0L
