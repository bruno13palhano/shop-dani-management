package com.bruno13palhano.core.model

import java.time.OffsetDateTime

data class DataVersion(
    override val id: Long = 0L,
    val name: String = "",
    override val timestamp: OffsetDateTime = OffsetDateTime.now()
) : Model(id = id, timestamp = timestamp)
