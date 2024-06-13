package com.bruno13palhano.core.network.access.firebase

import com.bruno13palhano.core.model.UserCodeResponse
import com.bruno13palhano.core.model.UserCodeResponse.INSERT_SERVER_ERROR
import com.bruno13palhano.core.network.access.RemoteUserData
import com.bruno13palhano.core.network.model.UserNet
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.Base64
import javax.inject.Inject

internal class RemoteUserFirebase
    @Inject
    constructor(
        private val auth: FirebaseAuth,
        private val firestore: FirebaseFirestore,
        private val storage: FirebaseStorage
    ) : RemoteUserData {
        override suspend fun create(
            user: UserNet,
            onError: (error: Int) -> Unit,
            onSuccess: (userNet: UserNet) -> Unit
        ) {
            try {
                val storageRef = storage.reference

                auth.createUserWithEmailAndPassword(user.email, user.password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            auth.currentUser?.let { firebaseUser ->
                                val profilePhotoRef =
                                    storageRef.child(
                                        "${firebaseUser.email}/${user.username}/profile_image.jpg"
                                    )
                                val uploadTask =
                                    profilePhotoRef.putBytes(Base64.getDecoder().decode(user.photo))

                                uploadTask.addOnSuccessListener { _ ->
                                    profilePhotoRef.downloadUrl.addOnSuccessListener { uri ->
                                        val profileUpdates =
                                            userProfileChangeRequest {
                                                displayName = user.username
                                                photoUri = uri
                                            }

                                        firebaseUser.updateProfile(profileUpdates)
                                            .addOnCompleteListener { t ->
                                                if (t.isSuccessful) {
                                                    val username = firebaseUser.displayName ?: ""
                                                    val email = firebaseUser.email ?: ""

                                                    val newUser =
                                                        UserNet(
                                                            uid = firebaseUser.uid,
                                                            username = username,
                                                            email = email,
                                                            password = "",
                                                            photo = user.photo,
                                                            role = user.role,
                                                            enabled = user.enabled,
                                                            timestamp = user.timestamp
                                                        )

                                                    val fireUser =
                                                        hashMapOf(
                                                            "uid" to newUser.uid,
                                                            "username" to newUser.username,
                                                            "email" to newUser.email,
                                                            "password" to newUser.password,
                                                            "photo" to uri,
                                                            "role" to newUser.role,
                                                            "enabled" to newUser.enabled,
                                                            "timestamp" to newUser.timestamp
                                                        )

                                                    auth.uid?.let {
                                                        firestore.collection("users")
                                                            .document(it)
                                                            .set(fireUser)
                                                    }

                                                    onSuccess(newUser)
                                                } else {
                                                    onError(INSERT_SERVER_ERROR)
                                                }
                                            }
                                    }
                                }
                            }
                        } else {
                            onError(INSERT_SERVER_ERROR)
                        }
                    }
            } catch (e: Exception) {
                e.printStackTrace()
                onError(INSERT_SERVER_ERROR)
            }
        }

        override suspend fun update(
            user: UserNet,
            onError: (error: Int) -> Unit,
            onSuccess: () -> Unit
        ) {
            try {
                val docRef = firestore.collection("users").document(user.uid)

                val updates =
                    hashMapOf<String, Any>(
                        "name" to user.username,
                        "photo" to user.photo,
                        "timestamp" to user.timestamp
                    )

                docRef.update(updates).addOnCompleteListener {
                    if (it.isSuccessful) {
                        onSuccess()
                    } else {
                        onError(UserCodeResponse.UPDATE_SERVER_ERROR)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onError(UserCodeResponse.UPDATE_SERVER_ERROR)
            }
        }

        override suspend fun login(
            user: UserNet,
            onError: (error: Int) -> Unit,
            onSuccess: (token: String) -> Unit
        ) {
            try {
                auth.signInWithEmailAndPassword(user.email, user.password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            onSuccess(auth.currentUser?.uid.toString())
                        } else {
                            onError(UserCodeResponse.LOGIN_SERVER_ERROR)
                        }
                    }
            } catch (e: Exception) {
                e.printStackTrace()
                onError(UserCodeResponse.LOGIN_SERVER_ERROR)
            }
        }

        override suspend fun authenticated(token: String): Boolean {
            return auth.currentUser != null
        }

        override suspend fun getByUsername(username: String): UserNet {
            val uid = auth.currentUser?.uid ?: ""
            val photo = auth.currentUser?.photoUrl?.toString() ?: ""
            val userRef = firestore.collection("users").document(uid)

            try {
                return userRef.get().await()?.data?.let {
                    UserNet(
                        uid = it["uid"].toString(),
                        username = it["username"].toString(),
                        email = it["email"].toString(),
                        password = "",
                        photo = photo,
                        role = it["role"].toString(),
                        enabled = it["enabled"] as Boolean,
                        timestamp = it["timestamp"].toString()
                    )
                }!!
            } catch (e: Exception) {
                e.printStackTrace()

                return UserNet(
                    uid = "",
                    username = "",
                    email = "",
                    password = "",
                    photo = "",
                    role = "",
                    enabled = false,
                    timestamp = ""
                )
            }
        }

        override suspend fun updateUserPassword(
            oldPassword: String,
            newPassword: String,
            email: String,
            onError: (error: Int) -> Unit,
            onSuccess: () -> Unit
        ) {
            try {
                val currentUser = auth.currentUser!!
                val credential = EmailAuthProvider.getCredential(email, oldPassword)

                currentUser.reauthenticate(credential).addOnCompleteListener { reAuth ->
                    if (reAuth.isSuccessful) {
                        currentUser.updatePassword(newPassword)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    onSuccess()
                                } else {
                                    onError(UserCodeResponse.UPDATE_SERVER_ERROR)
                                }
                            }
                    } else {
                        onError(UserCodeResponse.UPDATE_SERVER_ERROR)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onError(UserCodeResponse.UPDATE_SERVER_ERROR)
            }
        }

        override suspend fun logout(
            onError: (error: Int) -> Unit,
            onSuccess: () -> Unit
        ) {
            try {
                auth.signOut()
                onSuccess()
            } catch (e: Exception) {
                onError(UserCodeResponse.UPDATE_SERVER_ERROR)
                e.printStackTrace()
            }
        }
    }