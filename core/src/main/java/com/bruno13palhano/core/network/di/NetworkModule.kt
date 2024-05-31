package com.bruno13palhano.core.network.di

import com.bruno13palhano.core.network.access.CatalogNetwork
import com.bruno13palhano.core.network.access.CategoryNetwork
import com.bruno13palhano.core.network.access.CustomerNetwork
import com.bruno13palhano.core.network.access.ProductNetwork
import com.bruno13palhano.core.network.access.SaleNetwork
import com.bruno13palhano.core.network.access.StockNetwork
import com.bruno13palhano.core.network.access.UserNetwork
import com.bruno13palhano.core.network.access.VersionNetwork
import com.bruno13palhano.core.network.access.impl.CatalogNetworkRetrofit
import com.bruno13palhano.core.network.access.impl.CategoryNetworkRetrofit
import com.bruno13palhano.core.network.access.impl.CustomerNetworkRetrofit
import com.bruno13palhano.core.network.access.impl.DataVersionNetworkRetrofit
import com.bruno13palhano.core.network.access.impl.ProductNetworkRetrofit
import com.bruno13palhano.core.network.access.impl.SaleNetworkRetrofit
import com.bruno13palhano.core.network.access.impl.StockNetworkRetrofit
import com.bruno13palhano.core.network.access.impl.UserNetworkRetrofit
import com.bruno13palhano.core.network.access.impl.firebase.CatalogNetworkFirebase
import com.bruno13palhano.core.network.access.impl.firebase.CategoryNetworkFirebase
import com.bruno13palhano.core.network.access.impl.firebase.CustomerNetworkFirebase
import com.bruno13palhano.core.network.access.impl.firebase.DataVersionNetworkFirebase
import com.bruno13palhano.core.network.access.impl.firebase.ProductNetworkFirebase
import com.bruno13palhano.core.network.access.impl.firebase.SaleNetworkFirebase
import com.bruno13palhano.core.network.access.impl.firebase.StockNetworkFirebase
import com.bruno13palhano.core.network.access.impl.firebase.UserNetworkFirebase
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
internal annotation class DefaultUserNet

@Qualifier
internal annotation class FirebaseUserNet

@Qualifier
internal annotation class DefaultCatalogNet

@Qualifier
internal annotation class FirebaseCatalogNet

@Qualifier
internal annotation class DefaultCategoryNet

@Qualifier
internal annotation class FirebaseCategoryNet

@Qualifier
internal annotation class DefaultCustomerNet

@Qualifier
internal annotation class FirebaseCustomerNet

@Qualifier
internal annotation class DefaultProductNet

@Qualifier
internal annotation class FirebaseProductNet

@Qualifier
internal annotation class DefaultSaleNet

@Qualifier
internal annotation class FirebaseSaleNet

@Qualifier
internal annotation class DefaultStockNet

@Qualifier
internal annotation class FirebaseStockNet

@Qualifier
internal annotation class DefaultVersionNet

@Qualifier
internal annotation class FirebaseVersionNet

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NetworkModule {
    @DefaultUserNet
    @Singleton
    @Binds
    abstract fun bindUserNetwork(network: UserNetworkRetrofit): UserNetwork

    @FirebaseUserNet
    @Singleton
    @Binds
    abstract fun bindFirebaseUserNetwork(network: UserNetworkFirebase): UserNetwork

    @DefaultCatalogNet
    @Singleton
    @Binds
    abstract fun bindCatalogNetwork(network: CatalogNetworkRetrofit): CatalogNetwork

    @FirebaseCatalogNet
    @Singleton
    @Binds
    abstract fun bindFirebaseCatalogNetwork(network: CatalogNetworkFirebase): CatalogNetwork

    @DefaultCategoryNet
    @Singleton
    @Binds
    abstract fun bindCategoryNetwork(network: CategoryNetworkRetrofit): CategoryNetwork

    @FirebaseCategoryNet
    @Singleton
    @Binds
    abstract fun bindFirebaseCategoryNetwork(network: CategoryNetworkFirebase): CategoryNetwork

    @DefaultCustomerNet
    @Singleton
    @Binds
    abstract fun bindCustomerNetwork(network: CustomerNetworkRetrofit): CustomerNetwork

    @FirebaseCustomerNet
    @Singleton
    @Binds
    abstract fun bindFirebaseCustomerNetwork(network: CustomerNetworkFirebase): CustomerNetwork

    @DefaultProductNet
    @Singleton
    @Binds
    abstract fun bindProductNetwork(network: ProductNetworkRetrofit): ProductNetwork

    @FirebaseProductNet
    @Singleton
    @Binds
    abstract fun bindFirebaseProductNetwork(network: ProductNetworkFirebase): ProductNetwork

    @DefaultSaleNet
    @Singleton
    @Binds
    abstract fun bindSaleNetwork(network: SaleNetworkRetrofit): SaleNetwork

    @FirebaseSaleNet
    @Singleton
    @Binds
    abstract fun bindFirebaseSaleNetwork(network: SaleNetworkFirebase): SaleNetwork

    @DefaultStockNet
    @Singleton
    @Binds
    abstract fun bindStockNetwork(network: StockNetworkRetrofit): StockNetwork

    @FirebaseStockNet
    @Singleton
    @Binds
    abstract fun bindFirebaseStockNetwork(network: StockNetworkFirebase): StockNetwork

    @DefaultVersionNet
    @Singleton
    @Binds
    abstract fun bindVersionNetwork(network: DataVersionNetworkRetrofit): VersionNetwork

    @FirebaseVersionNet
    @Singleton
    @Binds
    abstract fun bindFirebaseVersionNetwork(network: DataVersionNetworkFirebase): VersionNetwork
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
