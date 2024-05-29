package com.bruno13palhano.shopdanimanagement.ui.screens.common

import androidx.annotation.StringRes
import com.bruno13palhano.core.model.UserCodeResponse
import com.bruno13palhano.shopdanimanagement.R

sealed class UserResponse(
    val code: Int,
    @StringRes val resourceId: Int
) {
    data object OKResponse : UserResponse(
        code = UserCodeResponse.OK,
        resourceId = R.string.ok_code_label
    )

    data object WrongPasswordValidation : UserResponse(
        code = UserCodeResponse.WRONG_PASSWORD_VALIDATION,
        resourceId = R.string.wrong_password_validation_label
    )

    data object UserNullOrInvalid : UserResponse(
        code = UserCodeResponse.USER_NULL_OR_INVALID,
        resourceId = R.string.user_null_or_invalid_label
    )

    data object UsernameAlreadyExist : UserResponse(
        code = UserCodeResponse.USERNAME_ALREADY_EXIST,
        resourceId = R.string.username_already_exist_label
    )

    data object EmailAlreadyExist : UserResponse(
        code = UserCodeResponse.EMAIL_ALREADY_EXIST,
        resourceId = R.string.email_already_exist_label
    )

    data object IncorrectUsernameOrPassword : UserResponse(
        code = UserCodeResponse.INCORRECT_USERNAME_OR_PASSWORD,
        resourceId = R.string.incorrect_username_or_password_label
    )

    data object LoginServerError : UserResponse(
        code = UserCodeResponse.LOGIN_SERVER_ERROR,
        resourceId = R.string.login_server_error
    )

    data object InsertServerError : UserResponse(
        code = UserCodeResponse.INSERT_SERVER_ERROR,
        resourceId = R.string.insert_server_error
    )

    data object UpdateServerError : UserResponse(
        code = UserCodeResponse.UPDATE_SERVER_ERROR,
        resourceId = R.string.update_server_error
    )

    data object FillMissingFields : UserResponse(
        code = UserCodeResponse.FILL_MISSING_FIELDS,
        resourceId = R.string.empty_fields_error
    )
}