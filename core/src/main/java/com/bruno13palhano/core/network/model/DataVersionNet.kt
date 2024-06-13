package com.bruno13palhano.core.network.model

import com.bruno13palhano.core.model.Model

data class DataVersionNet(
    override val id: Long = 0L,
    val name: String = "",
    override val timestamp: String = "1974-03-03T00:00:00Z"
) : Model(id = id, timestamp = timestamp)