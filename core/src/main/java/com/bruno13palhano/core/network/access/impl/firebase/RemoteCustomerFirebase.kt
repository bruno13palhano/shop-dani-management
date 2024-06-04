package com.bruno13palhano.core.network.access.impl.firebase

import com.bruno13palhano.core.network.access.RemoteCustomerData
import com.bruno13palhano.core.network.model.CustomerNet
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class RemoteCustomerFirebase
    @Inject
    constructor(
        private val firestore: FirebaseFirestore,
    ) : RemoteCustomerData {
        override suspend fun getAll(): List<CustomerNet> {
            val snapshot = firestore.collection("customers").get()

            try {
                return snapshot.await()?.let {
                    if (!it.isEmpty) {
                        it.documents.map { firebaseCustomer ->
                            CustomerNet(
                                id = firebaseCustomer["id"] as Long,
                                name = firebaseCustomer["name"].toString(),
                                photo = firebaseCustomer["photo"].toString(),
                                email = firebaseCustomer["email"].toString(),
                                address = firebaseCustomer["address"].toString(),
                                city = firebaseCustomer["city"].toString(),
                                phoneNumber = firebaseCustomer["phoneNumber"].toString(),
                                gender = firebaseCustomer["gender"].toString(),
                                age = (firebaseCustomer["age"] as Long).toInt(),
                                timestamp = firebaseCustomer["timestamp"].toString(),
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

        override suspend fun insert(data: CustomerNet) {
            try {
                firestore.collection("customers")
                    .document(data.id.toString())
                    .set(
                        hashMapOf(
                            "id" to data.id,
                            "name" to data.name,
                            "photo" to data.photo,
                            "email" to data.email,
                            "address" to data.address,
                            "city" to data.city,
                            "phoneNumber" to data.phoneNumber,
                            "gender" to data.gender,
                            "age" to data.age,
                            "timestamp" to data.timestamp,
                        ),
                    )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override suspend fun update(data: CustomerNet) {
            val docRef = firestore.collection("customers").document(data.id.toString())

            val updates =
                hashMapOf<String, Any>(
                    "id" to data.id,
                    "name" to data.name,
                    "photo" to data.photo,
                    "email" to data.email,
                    "address" to data.address,
                    "city" to data.city,
                    "phoneNumber" to data.phoneNumber,
                    "gender" to data.gender,
                    "age" to data.age,
                    "timestamp" to data.timestamp,
                )

            docRef.update(updates)
        }

        override suspend fun delete(id: Long) {
            val docRef = firestore.collection("customers").document(id.toString())

            val updates =
                hashMapOf<String, Any>(
                    "id" to FieldValue.delete(),
                    "name" to FieldValue.delete(),
                    "photo" to FieldValue.delete(),
                    "email" to FieldValue.delete(),
                    "address" to FieldValue.delete(),
                    "city" to FieldValue.delete(),
                    "phoneNumber" to FieldValue.delete(),
                    "gender" to FieldValue.delete(),
                    "age" to FieldValue.delete(),
                    "timestamp" to FieldValue.delete(),
                )

            docRef.update(updates)
            docRef.delete()
        }
    }
