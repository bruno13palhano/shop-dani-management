package com.bruno13palhano.core.model

data class DataVersion(
    override val id: Long = 0L,
    val name: String = "",
    override val timestamp: String = "1974-01-01T00:00:00Z"
) : Model(id = id, timestamp = timestamp)