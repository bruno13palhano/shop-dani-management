package com.bruno13palhano.core.network.model

import com.bruno13palhano.core.model.Model

data class CategoryNet(
    override val id: Long,
    val category: String,
    override val timestamp: String
) : Model(id = id, timestamp = timestamp)