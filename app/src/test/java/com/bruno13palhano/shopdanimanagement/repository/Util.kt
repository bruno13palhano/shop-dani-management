package com.bruno13palhano.shopdanimanagement.repository

import com.bruno13palhano.core.model.Model

fun <T : Model> getIndex(id: Long, list: List<T>): Int {
    var index = -1

    for (i in list.indices) {
        if (list[i].id == id) {
            index = i
            break
        }
    }

    return index
}

fun isIndexValid(index: Int) = index != -1