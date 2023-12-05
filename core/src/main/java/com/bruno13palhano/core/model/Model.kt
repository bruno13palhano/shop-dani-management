package com.bruno13palhano.core.model

import java.time.OffsetDateTime

open class Model(
    open val id: Long,
    open val timestamp: OffsetDateTime
)

fun Model.isNew() = id == 0L