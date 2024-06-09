package com.bruno13palhano.core.network.access.impl.firebase

import com.bruno13palhano.core.network.access.RemoteStockData
import com.bruno13palhano.core.network.model.StockItemNet
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class RemoteStockFirebase
    @Inject
    constructor(
        private val firestore: FirebaseFirestore
    ) : RemoteStockData {
        override suspend fun updateItemQuantity(
            id: Long,
            quantity: Int
        ) {
            TODO("Not yet implemented")
        }

        override suspend fun getAll(): List<StockItemNet> {
            val snapshot = firestore.collection("stock").get()

            try {
                return snapshot.await()?.let {
                    if (!it.isEmpty) {
                        it.documents.map { firebaseStock ->
                            StockItemNet(
                                id = firebaseStock["id"] as Long,
                                productId = firebaseStock["productId"] as Long,
                                date = firebaseStock["date"] as Long,
                                dateOfPayment = firebaseStock["dateOfPayment"] as Long,
                                validity = firebaseStock["validity"] as Long,
                                quantity = (firebaseStock["quantity"] as Long).toInt(),
                                purchasePrice = (firebaseStock["purchasePrice"] as Double).toFloat(),
                                salePrice = (firebaseStock["salePrice"] as Double).toFloat(),
                                isPaid = firebaseStock["isPaid"] as Boolean,
                                timestamp = firebaseStock["timestamp"].toString()
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

        override suspend fun insert(data: StockItemNet) {
            try {
                firestore.collection("stock")
                    .document(data.id.toString())
                    .set(
                        hashMapOf(
                            "id" to data.id,
                            "productId" to data.productId,
                            "date" to data.date,
                            "dateOfPayment" to data.dateOfPayment,
                            "validity" to data.validity,
                            "quantity" to data.quantity,
                            "purchasePrice" to data.purchasePrice,
                            "salePrice" to data.salePrice,
                            "isPaid" to data.isPaid,
                            "timestamp" to data.timestamp
                        )
                    )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override suspend fun update(data: StockItemNet) {
            val docRef = firestore.collection("stock").document(data.id.toString())

            val updates =
                hashMapOf<String, Any>(
                    "id" to data.id,
                    "productId" to data.productId,
                    "date" to data.date,
                    "dateOfPayment" to data.dateOfPayment,
                    "validity" to data.validity,
                    "quantity" to data.quantity,
                    "purchasePrice" to data.purchasePrice,
                    "salePrice" to data.salePrice,
                    "isPaid" to data.isPaid,
                    "timestamp" to data.timestamp
                )

            docRef.update(updates)
        }

        override suspend fun delete(id: Long) {
            val docRef = firestore.collection("stock").document(id.toString())

            val updates =
                hashMapOf<String, Any>(
                    "id" to FieldValue.delete(),
                    "productId" to FieldValue.delete(),
                    "date" to FieldValue.delete(),
                    "dateOfPayment" to FieldValue.delete(),
                    "validity" to FieldValue.delete(),
                    "quantity" to FieldValue.delete(),
                    "purchasePrice" to FieldValue.delete(),
                    "salePrice" to FieldValue.delete(),
                    "isPaid" to FieldValue.delete(),
                    "timestamp" to FieldValue.delete()
                )

            docRef.update(updates)
            docRef.delete()
        }
    }