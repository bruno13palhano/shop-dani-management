package com.bruno13palhano.core.network.access.impl.firebase

import com.bruno13palhano.core.network.access.SaleNetwork
import com.bruno13palhano.core.network.model.SaleNet
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class SaleNetworkFirebase
    @Inject
    constructor(
        private val firestore: FirebaseFirestore,
    ) : SaleNetwork {
        override suspend fun getAll(): List<SaleNet> {
            val snapshot = firestore.collection("sales").get()

            try {
                return snapshot.await()?.let {
                    if (!it.isEmpty) {
                        it.documents.map { firebaseSale ->
                            SaleNet(
                                id = firebaseSale["id"] as Long,
                                productId = firebaseSale["productId"] as Long,
                                stockId = firebaseSale["stockId"] as Long,
                                customerId = firebaseSale["customerId"] as Long,
                                quantity = (firebaseSale["quantity"] as Long).toInt(),
                                purchasePrice = (firebaseSale["purchasePrice"] as Double).toFloat(),
                                salePrice = (firebaseSale["salePrice"] as Double).toFloat(),
                                deliveryPrice = (firebaseSale["deliveryPrice"] as Double).toFloat(),
                                amazonCode = firebaseSale["amazonCode"].toString(),
                                amazonRequestNumber = firebaseSale["amazonRequestNumber"] as Long,
                                amazonTax = (firebaseSale["amazonTax"] as Long).toInt(),
                                amazonProfit = (firebaseSale["amazonProfit"] as Double).toFloat(),
                                amazonSKU = firebaseSale["amazonSKU"].toString(),
                                resaleProfit = (firebaseSale["resaleProfit"] as Double).toFloat(),
                                totalProfit = (firebaseSale["totalProfit"] as Double).toFloat(),
                                dateOfSale = firebaseSale["dateOfSale"] as Long,
                                dateOfPayment = firebaseSale["dateOfPayment"] as Long,
                                shippingDate = firebaseSale["shippingDate"] as Long,
                                deliveryDate = firebaseSale["deliveryDate"] as Long,
                                isOrderedByCustomer = firebaseSale["isOrderedByCustomer"] as Boolean,
                                isPaidByCustomer = firebaseSale["isPaidByCustomer"] as Boolean,
                                delivered = firebaseSale["delivered"] as Boolean,
                                canceled = firebaseSale["canceled"] as Boolean,
                                isAmazon = firebaseSale["isAmazon"] as Boolean,
                                timestamp = firebaseSale["timestamp"].toString(),
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

        override suspend fun insert(data: SaleNet) {
            try {
                firestore.collection("sales")
                    .document(data.id.toString())
                    .set(
                        hashMapOf(
                            "id" to data.id,
                            "productId" to data.productId,
                            "stockId" to data.stockId,
                            "customerId" to data.customerId,
                            "quantity" to data.quantity,
                            "purchasePrice" to data.purchasePrice,
                            "salePrice" to data.salePrice,
                            "deliveryPrice" to data.deliveryPrice,
                            "amazonCode" to data.amazonCode,
                            "amazonRequestNumber" to data.amazonRequestNumber,
                            "amazonTax" to data.amazonTax,
                            "amazonProfit" to data.amazonProfit,
                            "amazonSKU" to data.amazonSKU,
                            "resaleProfit" to data.resaleProfit,
                            "totalProfit" to data.totalProfit,
                            "dateOfSale" to data.dateOfSale,
                            "dateOfPayment" to data.dateOfPayment,
                            "shippingDate" to data.shippingDate,
                            "deliveryDate" to data.deliveryDate,
                            "isOrderedByCustomer" to data.isOrderedByCustomer,
                            "isPaidByCustomer" to data.isPaidByCustomer,
                            "delivered" to data.delivered,
                            "canceled" to data.canceled,
                            "isAmazon" to data.isAmazon,
                            "timestamp" to data.timestamp,
                        ),
                    )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override suspend fun update(data: SaleNet) {
            val docRef = firestore.collection("sales").document(data.id.toString())

            val updates =
                hashMapOf<String, Any>(
                    "id" to data.id,
                    "productId" to data.productId,
                    "stockId" to data.stockId,
                    "customerId" to data.customerId,
                    "quantity" to data.quantity,
                    "purchasePrice" to data.purchasePrice,
                    "salePrice" to data.salePrice,
                    "deliveryPrice" to data.deliveryPrice,
                    "amazonCode" to data.amazonCode,
                    "amazonRequestNumber" to data.amazonRequestNumber,
                    "amazonTax" to data.amazonTax,
                    "amazonProfit" to data.amazonProfit,
                    "amazonSKU" to data.amazonSKU,
                    "resaleProfit" to data.resaleProfit,
                    "totalProfit" to data.totalProfit,
                    "dateOfSale" to data.dateOfSale,
                    "dateOfPayment" to data.dateOfPayment,
                    "shippingDate" to data.shippingDate,
                    "deliveryDate" to data.deliveryDate,
                    "isOrderedByCustomer" to data.isOrderedByCustomer,
                    "isPaidByCustomer" to data.isPaidByCustomer,
                    "delivered" to data.delivered,
                    "canceled" to data.canceled,
                    "isAmazon" to data.isAmazon,
                    "timestamp" to data.timestamp,
                )

            docRef.update(updates)
        }

        override suspend fun delete(id: Long) {
            val docRef = firestore.collection("sales").document(id.toString())

            val updates =
                hashMapOf<String, Any>(
                    "id" to FieldValue.delete(),
                    "productId" to FieldValue.delete(),
                    "stockId" to FieldValue.delete(),
                    "customerId" to FieldValue.delete(),
                    "quantity" to FieldValue.delete(),
                    "purchasePrice" to FieldValue.delete(),
                    "salePrice" to FieldValue.delete(),
                    "deliveryPrice" to FieldValue.delete(),
                    "amazonCode" to FieldValue.delete(),
                    "amazonRequestNumber" to FieldValue.delete(),
                    "amazonTax" to FieldValue.delete(),
                    "amazonProfit" to FieldValue.delete(),
                    "amazonSKU" to FieldValue.delete(),
                    "resaleProfit" to FieldValue.delete(),
                    "totalProfit" to FieldValue.delete(),
                    "dateOfSale" to FieldValue.delete(),
                    "dateOfPayment" to FieldValue.delete(),
                    "shippingDate" to FieldValue.delete(),
                    "deliveryDate" to FieldValue.delete(),
                    "isOrderedByCustomer" to FieldValue.delete(),
                    "isPaidByCustomer" to FieldValue.delete(),
                    "delivered" to FieldValue.delete(),
                    "canceled" to FieldValue.delete(),
                    "isAmazon" to FieldValue.delete(),
                    "timestamp" to FieldValue.delete(),
                )

            docRef.update(updates)
            docRef.delete()
        }
    }
