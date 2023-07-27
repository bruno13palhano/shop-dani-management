package com.bruno13palhano.core.data.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter

@ProvidedTypeConverter
class CategoryConverter {

    @TypeConverter
    fun stringToCategory(value: String): List<String> {
        val dbValues = value.split("\\s*,\\s*".toRegex()).dropLastWhile { it.isEmpty() }
        val list: MutableList<String> = ArrayList()

        for (s in dbValues)
            list.add(s)
        return list
    }

    @TypeConverter
    fun categoryToString(categories: List<String>): String = categories.toString()
}