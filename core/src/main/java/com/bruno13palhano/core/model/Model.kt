package com.bruno13palhano.core.model

open class Model(
    open val id: Long
)

fun Model.isNew() = id == 0L