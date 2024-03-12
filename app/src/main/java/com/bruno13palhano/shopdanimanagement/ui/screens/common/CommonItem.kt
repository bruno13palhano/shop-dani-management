package com.bruno13palhano.shopdanimanagement.ui.screens.common

data class CommonItem(
    val id: Long,
    val photo: ByteArray,
    val title: String,
    val subtitle: String,
    val description: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CommonItem

        if (id != other.id) return false
        if (!photo.contentEquals(other.photo)) return false
        if (title != other.title) return false
        if (subtitle != other.subtitle) return false
        return description == other.description
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + photo.contentHashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + subtitle.hashCode()
        result = 31 * result + description.hashCode()
        return result
    }
}
