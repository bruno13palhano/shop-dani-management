package com.bruno13palhano.core.network.di

import com.bruno13palhano.core.network.access.RemoteCatalogData
import com.bruno13palhano.core.network.access.RemoteCategoryData
import com.bruno13palhano.core.network.access.RemoteCustomerData
import com.bruno13palhano.core.network.access.RemoteProductData
import com.bruno13palhano.core.network.access.RemoteSaleData
import com.bruno13palhano.core.network.access.RemoteStockData
import com.bruno13palhano.core.network.access.RemoteUserData
import com.bruno13palhano.core.network.access.RemoteVersionData
import com.bruno13palhano.core.network.access.firebase.RemoteCatalogFirebase
import com.bruno13palhano.core.network.access.firebase.RemoteCategoryFirebase
import com.bruno13palhano.core.network.access.firebase.RemoteCustomerFirebase
import com.bruno13palhano.core.network.access.firebase.RemoteProductFirebase
import com.bruno13palhano.core.network.access.firebase.RemoteSaleFirebase
import com.bruno13palhano.core.network.access.firebase.RemoteStockFirebase
import com.bruno13palhano.core.network.access.firebase.RemoteUserFirebase
import com.bruno13palhano.core.network.access.firebase.RemoteVersionFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
internal annotation class FirebaseUser

@Qualifier
internal annotation class FirebaseCatalog

@Qualifier
internal annotation class FirebaseCategory

@Qualifier
internal annotation class FirebaseCustomer

@Qualifier
internal annotation class FirebaseProduct

@Qualifier
internal annotation class FirebaseSale

@Qualifier
internal annotation class FirebaseStock

@Qualifier
internal annotation class FirebaseVersion

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NetworkModule {
    @FirebaseUser
    @Singleton
    @Binds
    abstract fun bindFirebaseUser(network: RemoteUserFirebase): RemoteUserData

    @FirebaseCatalog
    @Singleton
    @Binds
    abstract fun bindFirebaseCatalog(network: RemoteCatalogFirebase): RemoteCatalogData

    @FirebaseCategory
    @Singleton
    @Binds
    abstract fun bindFirebaseCategory(network: RemoteCategoryFirebase): RemoteCategoryData

    @FirebaseCustomer
    @Singleton
    @Binds
    abstract fun bindFirebaseCustomer(network: RemoteCustomerFirebase): RemoteCustomerData

    @FirebaseProduct
    @Singleton
    @Binds
    abstract fun bindFirebaseProduct(network: RemoteProductFirebase): RemoteProductData

    @FirebaseSale
    @Singleton
    @Binds
    abstract fun bindFirebaseSale(network: RemoteSaleFirebase): RemoteSaleData

    @FirebaseStock
    @Singleton
    @Binds
    abstract fun bindFirebaseStock(network: RemoteStockFirebase): RemoteStockData

    @FirebaseVersion
    @Singleton
    @Binds
    abstract fun bindFirebaseVersion(network: RemoteVersionFirebase): RemoteVersionData
}

@InstallIn(SingletonComponent::class)
@Module
internal object FirebaseProvidersModule {
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }
}