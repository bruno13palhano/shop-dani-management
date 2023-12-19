package com.bruno13palhano.core.model

data class User(
    val id: Long,
    val username: String,
    val email: String,
    val password: String,
    val photo: ByteArray,
    val role: String,
    val enabled: Boolean,
    val timestamp: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (username != other.username) return false
        if (email != other.email) return false
        if (password != other.password) return false
        if (!photo.contentEquals(other.photo)) return false
        if (role != other.role) return false
        if (enabled != other.enabled) return false
        if (timestamp != other.timestamp) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + photo.contentHashCode()
        result = 31 * result + role.hashCode()
        result = 31 * result + enabled.hashCode()
        result = 31 * result + timestamp.hashCode()
        return result
    }
}
