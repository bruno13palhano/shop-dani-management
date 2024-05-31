package com.bruno13palhano.core.network.access.impl.firebase

import com.bruno13palhano.core.network.access.ProductNetwork
import com.bruno13palhano.core.network.model.CategoryNet
import com.bruno13palhano.core.network.model.ProductNet
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class ProductNetworkFirebase
    @Inject
    constructor(
        private val firestore: FirebaseFirestore,
    ) : ProductNetwork {
        override suspend fun getAll(): List<ProductNet> {
            val snapshot = firestore.collection("products").get()

            try {
                return snapshot.await()?.let {
                    if (!it.isEmpty) {
                        it.documents.map { firebaseProduct ->
                            ProductNet(
                                id = firebaseProduct["id"] as Long,
                                name = firebaseProduct["name"].toString(),
                                code = firebaseProduct["code"].toString(),
                                description = firebaseProduct["description"].toString(),
                                photo = firebaseProduct["photo"].toString(),
                                date = firebaseProduct["date"] as Long,
                                categories = firebaseProduct["categories"] as List<CategoryNet>,
                                company = firebaseProduct["company"].toString(),
                                timestamp = firebaseProduct["timestamp"].toString(),
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

        override suspend fun insert(data: ProductNet) {
            try {
                firestore.collection("products")
                    .document(data.id.toString())
                    .set(
                        hashMapOf(
                            "id" to data.id,
                            "name" to data.name,
                            "code" to data.code,
                            "description" to data.description,
                            "photo" to data.photo,
                            "date" to data.date,
                            "categories" to data.categories,
                            "company" to data.company,
                            "timestamp" to data.timestamp,
                        ),
                    )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override suspend fun update(data: ProductNet) {
            val docRef = firestore.collection("products").document(data.id.toString())

            val updates =
                hashMapOf(
                    "id" to data.id,
                    "name" to data.name,
                    "code" to data.code,
                    "description" to data.description,
                    "photo" to data.photo,
                    "date" to data.date,
                    "categories" to data.categories,
                    "company" to data.company,
                    "timestamp" to data.timestamp,
                )

            docRef.update(updates)
        }

        override suspend fun delete(id: Long) {
            val docRef = firestore.collection("products").document(id.toString())

            val updates =
                hashMapOf<String, Any>(
                    "id" to FieldValue.delete(),
                    "name" to FieldValue.delete(),
                    "code" to FieldValue.delete(),
                    "description" to FieldValue.delete(),
                    "photo" to FieldValue.delete(),
                    "date" to FieldValue.delete(),
                    "categories" to FieldValue.delete(),
                    "company" to FieldValue.delete(),
                    "timestamp" to FieldValue.delete(),
                )

            docRef.update(updates)
        }
    }
