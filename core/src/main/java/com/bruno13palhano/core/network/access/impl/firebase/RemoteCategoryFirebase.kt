package com.bruno13palhano.core.network.access.impl.firebase

import com.bruno13palhano.core.network.access.RemoteCategoryData
import com.bruno13palhano.core.network.model.CategoryNet
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class RemoteCategoryFirebase
    @Inject
    constructor(
        private val firestore: FirebaseFirestore
    ) : RemoteCategoryData {
        override suspend fun getAll(): List<CategoryNet> {
            val snapshot = firestore.collection("categories").get()

            try {
                return snapshot.await()?.let {
                    if (!it.isEmpty) {
                        it.documents.map { firebaseSale ->
                            CategoryNet(
                                id = firebaseSale["id"] as Long,
                                category = firebaseSale["category"].toString(),
                                timestamp = firebaseSale["timestamp"].toString()
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

        override suspend fun insert(data: CategoryNet) {
            try {
                firestore.collection("categories")
                    .document(data.id.toString())
                    .set(
                        hashMapOf(
                            "id" to data.id,
                            "category" to data.category,
                            "timestamp" to data.timestamp
                        )
                    )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override suspend fun update(data: CategoryNet) {
            val docRef = firestore.collection("category").document(data.id.toString())

            val updates =
                hashMapOf<String, Any>(
                    "id" to data.id,
                    "category" to data.category,
                    "timestamp" to data.timestamp
                )

            docRef.update(updates)
        }

        override suspend fun delete(id: Long) {
            val docRef = firestore.collection("category").document(id.toString())

            val updates =
                hashMapOf<String, Any>(
                    "id" to FieldValue.delete(),
                    "category" to FieldValue.delete(),
                    "timestamp" to FieldValue.delete()
                )

            docRef.update(updates)
            docRef.delete()
        }
    }