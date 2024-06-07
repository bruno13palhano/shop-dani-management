package com.bruno13palhano.core.network.di

import com.bruno13palhano.core.network.access.RemoteCatalogData
import com.bruno13palhano.core.network.access.RemoteCategoryData
import com.bruno13palhano.core.network.access.RemoteCustomerData
import com.bruno13palhano.core.network.access.RemoteProductData
import com.bruno13palhano.core.network.access.RemoteSaleData
import com.bruno13palhano.core.network.access.RemoteStockData
import com.bruno13palhano.core.network.access.RemoteUserData
import com.bruno13palhano.core.network.access.RemoteVersionData
import com.bruno13palhano.core.network.access.impl.RemoteCatalogRetrofit
import com.bruno13palhano.core.network.access.impl.RemoteCategoryRetrofit
import com.bruno13palhano.core.network.access.impl.RemoteCustomerRetrofit
import com.bruno13palhano.core.network.access.impl.RemoteProductRetrofit
import com.bruno13palhano.core.network.access.impl.RemoteSaleRetrofit
import com.bruno13palhano.core.network.access.impl.RemoteStockRetrofit
import com.bruno13palhano.core.network.access.impl.RemoteUserRetrofit
import com.bruno13palhano.core.network.access.impl.RemoteVersionRetrofit
import com.bruno13palhano.core.network.access.impl.firebase.RemoteCatalogFirebase
import com.bruno13palhano.core.network.access.impl.firebase.RemoteCategoryFirebase
import com.bruno13palhano.core.network.access.impl.firebase.RemoteCustomerFirebase
import com.bruno13palhano.core.network.access.impl.firebase.RemoteProductFirebase
import com.bruno13palhano.core.network.access.impl.firebase.RemoteSaleFirebase
import com.bruno13palhano.core.network.access.impl.firebase.RemoteStockFirebase
import com.bruno13palhano.core.network.access.impl.firebase.RemoteUserFirebase
import com.bruno13palhano.core.network.access.impl.firebase.RemoteVersionFirebase
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
internal annotation class RetrofitUser

@Qualifier
internal annotation class FirebaseUser

@Qualifier
internal annotation class RetrofitCatalog

@Qualifier
internal annotation class FirebaseCatalog

@Qualifier
internal annotation class RetrofitCategory

@Qualifier
internal annotation class FirebaseCategory

@Qualifier
internal annotation class RetrofitCustomer

@Qualifier
internal annotation class FirebaseCustomer

@Qualifier
internal annotation class RetrofitProduct

@Qualifier
internal annotation class FirebaseProduct

@Qualifier
internal annotation class RetrofitSale

@Qualifier
internal annotation class FirebaseSale

@Qualifier
internal annotation class RetrofitStock

@Qualifier
internal annotation class FirebaseStock

@Qualifier
internal annotation class RetrofitVersion

@Qualifier
internal annotation class FirebaseVersion

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NetworkModule {
    @RetrofitUser
    @Singleton
    @Binds
    abstract fun bindRetrofitUser(network: RemoteUserRetrofit): RemoteUserData

    @FirebaseUser
    @Singleton
    @Binds
    abstract fun bindFirebaseUser(network: RemoteUserFirebase): RemoteUserData

    @RetrofitCatalog
    @Singleton
    @Binds
    abstract fun bindRetrofitCatalog(network: RemoteCatalogRetrofit): RemoteCatalogData

    @FirebaseCatalog
    @Singleton
    @Binds
    abstract fun bindFirebaseCatalog(network: RemoteCatalogFirebase): RemoteCatalogData

    @RetrofitCategory
    @Singleton
    @Binds
    abstract fun bindRetrofitCategory(network: RemoteCategoryRetrofit): RemoteCategoryData

    @FirebaseCategory
    @Singleton
    @Binds
    abstract fun bindFirebaseCategory(network: RemoteCategoryFirebase): RemoteCategoryData

    @RetrofitCustomer
    @Singleton
    @Binds
    abstract fun bindRetrofitCustomer(network: RemoteCustomerRetrofit): RemoteCustomerData

    @FirebaseCustomer
    @Singleton
    @Binds
    abstract fun bindFirebaseCustomer(network: RemoteCustomerFirebase): RemoteCustomerData

    @RetrofitProduct
    @Singleton
    @Binds
    abstract fun bindRetrofitProduct(network: RemoteProductRetrofit): RemoteProductData

    @FirebaseProduct
    @Singleton
    @Binds
    abstract fun bindFirebaseProduct(network: RemoteProductFirebase): RemoteProductData

    @RetrofitSale
    @Singleton
    @Binds
    abstract fun bindRetrofitSale(network: RemoteSaleRetrofit): RemoteSaleData

    @FirebaseSale
    @Singleton
    @Binds
    abstract fun bindFirebaseSale(network: RemoteSaleFirebase): RemoteSaleData

    @RetrofitStock
    @Singleton
    @Binds
    abstract fun bindRetrofitStock(network: RemoteStockRetrofit): RemoteStockData

    @FirebaseStock
    @Singleton
    @Binds
    abstract fun bindFirebaseStock(network: RemoteStockFirebase): RemoteStockData

    @RetrofitVersion
    @Singleton
    @Binds
    abstract fun bindRetrofitVersion(network: RemoteVersionRetrofit): RemoteVersionData

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
