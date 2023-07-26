package com.bruno13palhano.core.data.database.model

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.bruno13palhano.core.model.Category

@ProvidedTypeConverter
class CategoryConverter {

    @TypeConverter
    fun stringToCategory(value: String): List<Category> {
        val dbValues = value.split("\\s*,\\s*".toRegex()).dropLastWhile { it.isEmpty() }
        val enums: MutableList<Category> = ArrayList()

        for (s in dbValues)
            enums.add(Category.valueOf(s))
        return enums
    }

    @TypeConverter
    fun categoryToStoredString(categories: List<Category>): String {
        var value = ""

        for (lineType in categories)
            value += lineType.name + ","

        return value
    }
}