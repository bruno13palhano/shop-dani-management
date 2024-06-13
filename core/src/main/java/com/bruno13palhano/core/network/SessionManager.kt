package com.bruno13palhano.core.network

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

private const val TOKEN_FILE_NAME = "Shopdani Management"

@Singleton
class SessionManager
    @Inject
    constructor(
        @ApplicationContext context: Context
    ) {
        private var preferences: SharedPreferences =
            context.getSharedPreferences(
                TOKEN_FILE_NAME,
                Context.MODE_PRIVATE
            )

        companion object {
            const val CURRENT_USER_ID = "current_user_uid"
            const val CURRENT_USER_PASSWORD = "current_user_password"
        }

        fun saveCurrentUserUid(uid: String) {
            val editor = preferences.edit()
            editor.putString(CURRENT_USER_ID, uid)
            editor.apply()
        }

        fun saveCurrentUserPassword(password: String) {
            val editor = preferences.edit()
            editor.putString(CURRENT_USER_PASSWORD, password)
            editor.apply()
        }

        fun fetchCurrentUserUid(): String? {
            return preferences.getString(CURRENT_USER_ID, null)
        }

        fun fetchCurrentUserPassword(): String? {
            return preferences.getString(CURRENT_USER_PASSWORD, null)
        }
    }