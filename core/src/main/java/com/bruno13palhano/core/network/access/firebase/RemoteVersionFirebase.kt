package com.bruno13palhano.core.network.access.firebase

import com.bruno13palhano.core.network.access.RemoteVersionData
import com.bruno13palhano.core.network.model.DataVersionNet
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class RemoteVersionFirebase
    @Inject
    constructor(
        private val firestore: FirebaseFirestore
    ) : RemoteVersionData {
        override suspend fun getAll(): List<DataVersionNet> {
            val snapshot = firestore.collection("versions").get()

            try {
                return snapshot.await()?.let {
                    if (!it.isEmpty) {
                        it.documents.map { firebaseVersion ->
                            DataVersionNet(
                                id = firebaseVersion["id"] as Long,
                                name = firebaseVersion["name"].toString(),
                                timestamp = firebaseVersion["timestamp"].toString()
                            )
                        }
                    } else {
                        emptyList()
                    }
                }!!
            } catch (e: Exception) {
                e.printStackTrace()
                return emptyList()
            }
        }

        override suspend fun insert(data: DataVersionNet) {
            try {
                firestore.collection("versions")
                    .document(data.id.toString())
                    .set(
                        hashMapOf(
                            "id" to data.id,
                            "name" to data.name,
                            "timestamp" to data.timestamp
                        )
                    )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override suspend fun update(data: DataVersionNet) {
            val docRef = firestore.collection("versions").document(data.id.toString())

            val updates =
                hashMapOf<String, Any>(
                    "id" to data.id,
                    "name" to data.name,
                    "timestamp" to data.timestamp
                )

            docRef.update(updates)
        }

        override suspend fun delete(id: Long) {
            val docRef = firestore.collection("versions").document(id.toString())

            val updates =
                hashMapOf<String, Any>(
                    "id" to FieldValue.delete(),
                    "name" to FieldValue.delete(),
                    "timestamp" to FieldValue.delete()
                )

            docRef.update(updates)
        }
    }