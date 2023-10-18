package com.bruno13palhano.shopdanimanagement.ui.screens.common

data class ExtendedItem(
    val id: Long,
    val photo: ByteArray,
    val title: String,
    val firstSubtitle: String,
    val secondSubtitle: String,
    val description: String,
    val footer: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ExtendedItem

        if (id != other.id) return false
        if (!photo.contentEquals(other.photo)) return false
        if (title != other.title) return false
        if (firstSubtitle != other.firstSubtitle) return false
        if (secondSubtitle != other.secondSubtitle) return false
        if (description != other.description) return false
        if (footer != other.footer) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + photo.contentHashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + firstSubtitle.hashCode()
        result = 31 * result + secondSubtitle.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + footer.hashCode()
        return result
    }
}
