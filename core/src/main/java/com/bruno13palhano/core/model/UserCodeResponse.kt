package com.bruno13palhano.core.model

object UserCodeResponse {
    const val OK = 0
    const val WRONG_PASSWORD_VALIDATION = 1
    const val USER_NULL_OR_INVALID = 2
    const val USERNAME_ALREADY_EXIST = 3
    const val EMAIL_ALREADY_EXIST = 4
    const val INCORRECT_USERNAME_OR_PASSWORD = 5
    const val LOGIN_SERVER_ERROR = 6
    const val INSERT_SERVER_ERROR = 7
    const val UPDATE_SERVER_ERROR = 8
    const val FILL_MISSING_FIELDS = 9
}
