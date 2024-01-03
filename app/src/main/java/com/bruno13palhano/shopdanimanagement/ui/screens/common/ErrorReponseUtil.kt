package com.bruno13palhano.shopdanimanagement.ui.screens.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun getErrors() = listOf(
    stringResource(id = DataError.InsertDatabase.resourceId),
    stringResource(id = DataError.UpdateDatabase.resourceId),
    stringResource(id = DataError.DeleteDatabase.resourceId),
    stringResource(id = DataError.InsertServer.resourceId),
    stringResource(id = DataError.UpdateServer.resourceId),
    stringResource(id = DataError.DeleteServer.resourceId),
    stringResource(id = DataError.LoginDatabase.resourceId),
    stringResource(id = DataError.LoginServer.resourceId),
    stringResource(id = DataError.FillMissingFields.resourceId),
    stringResource(id = DataError.InsufficientItemsStock.resourceId)
)

@Composable
fun getUserResponse() = listOf(
    stringResource(id = UserResponse.OKResponse.resourceId),
    stringResource(id = UserResponse.WrongPasswordValidation.resourceId),
    stringResource(id = UserResponse.UserNullOrInvalid.resourceId),
    stringResource(id = UserResponse.UsernameAlreadyExist.resourceId),
    stringResource(id = UserResponse.EmailAlreadyExist.resourceId),
    stringResource(id = UserResponse.IncorrectUsernameOrPassword.resourceId),
    stringResource(id = UserResponse.LoginServerError.resourceId),
    stringResource(id = UserResponse.InsertServerError.resourceId),
    stringResource(id = UserResponse.UpdateServerError.resourceId),
    stringResource(id = UserResponse.FillMissingFields.resourceId)
)