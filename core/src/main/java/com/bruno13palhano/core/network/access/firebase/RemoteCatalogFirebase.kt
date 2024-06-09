package com.bruno13palhano.core.network.access.firebase

import com.bruno13palhano.core.network.access.RemoteCatalogData
import com.bruno13palhano.core.network.model.CatalogNet
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class RemoteCatalogFirebase
    @Inject
    constructor(
        private val firestore: FirebaseFirestore
    ) : RemoteCatalogData {
        override suspend fun getAll(): List<CatalogNet> {
            val snapshot = firestore.collection("catalog").get()

            try {
                return snapshot.await()?.let {
                    if (!it.isEmpty) {
                        it.documents.map { firebaseCatalog ->
                            CatalogNet(
                                id = firebaseCatalog["id"] as Long,
                                productId = firebaseCatalog["productId"] as Long,
                                title = firebaseCatalog["title"].toString(),
                                description = firebaseCatalog["description"].toString(),
                                discount = firebaseCatalog["discount"] as Long,
                                price = firebaseCatalog["price"] as Float,
                                timestamp = firebaseCatalog["timestamp"].toString()
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

        override suspend fun insert(data: CatalogNet) {
            try {
                firestore.collection("catalog")
                    .document(data.id.toString())
                    .set(
                        hashMapOf(
                            "id" to data.id,
                            "productId" to data.productId,
                            "title" to data.title,
                            "description" to data.description,
                            "discount" to data.discount,
                            "price" to data.price,
                            "timestamp" to data.timestamp
                        )
                    )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override suspend fun update(data: CatalogNet) {
            val docRef = firestore.collection("catalog").document(data.id.toString())

            val updates =
                hashMapOf<String, Any>(
                    "id" to data.id,
                    "productId" to data.productId,
                    "title" to data.title,
                    "description" to data.description,
                    "discount" to data.discount,
                    "price" to data.price,
                    "timestamp" to data.timestamp
                )

            docRef.update(updates)
        }

        override suspend fun delete(id: Long) {
            val docRef = firestore.collection("catalog").document(id.toString())

            val updates =
                hashMapOf<String, Any>(
                    "id" to FieldValue.delete(),
                    "productId" to FieldValue.delete(),
                    "title" to FieldValue.delete(),
                    "description" to FieldValue.delete(),
                    "discount" to FieldValue.delete(),
                    "price" to FieldValue.delete(),
                    "timestamp" to FieldValue.delete()
                )

            docRef.update(updates)
            docRef.delete()
        }
    }