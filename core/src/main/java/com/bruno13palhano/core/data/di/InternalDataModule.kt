package com.bruno13palhano.core.data.di

import com.bruno13palhano.core.data.repository.category.CategoryLight
import com.bruno13palhano.core.data.repository.category.CategoryRoom
import com.bruno13palhano.core.data.repository.category.InternalCategoryData
import com.bruno13palhano.core.data.repository.customer.CustomerLight
import com.bruno13palhano.core.data.repository.customer.CustomerRoom
import com.bruno13palhano.core.data.repository.customer.InternalCustomerData
import com.bruno13palhano.core.data.repository.delivery.DeliveryLight
import com.bruno13palhano.core.data.repository.delivery.DeliveryRoom
import com.bruno13palhano.core.data.repository.delivery.InternalDeliveryData
import com.bruno13palhano.core.data.repository.product.InternalProductData
import com.bruno13palhano.core.data.repository.product.ProductLight
import com.bruno13palhano.core.data.repository.product.ProductRoom
import com.bruno13palhano.core.data.repository.sale.InternalSaleData
import com.bruno13palhano.core.data.repository.sale.SaleLight
import com.bruno13palhano.core.data.repository.sale.SaleRoom
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
internal annotation class InternalCategoryRoom

@Qualifier
internal annotation class InternalCategoryLight

@Qualifier
internal annotation class InternalCustomerRoom

@Qualifier
internal annotation class InternalCustomerLight

@Qualifier
internal annotation class InternalDeliveryRoom

@Qualifier
internal annotation class InternalDeliveryLight

@Qualifier
internal annotation class InternalProductRoom

@Qualifier
internal annotation class InternalProductLight

@Qualifier
internal annotation class InternalSaleRoom

@Qualifier
internal annotation class InternalSaleLight

@InstallIn(SingletonComponent::class)
@Module
internal abstract class InternalDataModule {

    @InternalCategoryRoom
    @Singleton
    @Binds
    abstract fun bindInternalCategoryRoom(repository: CategoryRoom): InternalCategoryData

    @InternalCategoryLight
    @Singleton
    @Binds
    abstract fun bindInternalCategoryLight(repository: CategoryLight): InternalCategoryData

    @InternalCustomerRoom
    @Singleton
    @Binds
    abstract fun bindInternalCustomerRoom(repository: CustomerRoom): InternalCustomerData

    @InternalCustomerLight
    @Singleton
    @Binds
    abstract fun bindInternalCustomerLight(repository: CustomerLight): InternalCustomerData

    @InternalDeliveryRoom
    @Singleton
    @Binds
    abstract fun bindInternalDeliveryRoom(repository: DeliveryRoom): InternalDeliveryData

    @InternalDeliveryLight
    @Singleton
    @Binds
    abstract fun bindInternalDeliveryLight(repository: DeliveryLight): InternalDeliveryData

    @InternalProductRoom
    @Singleton
    @Binds
    abstract fun bindInternalProductRoom(repository: ProductRoom): InternalProductData

    @InternalProductLight
    @Singleton
    @Binds
    abstract fun bindInternalProductLight(repository: ProductLight): InternalProductData

    @InternalSaleRoom
    @Singleton
    @Binds
    abstract fun bindInternalSaleRoom(repository: SaleRoom): InternalSaleData

    @InternalSaleLight
    @Singleton
    @Binds
    abstract fun bindInternalSaleLight(repository: SaleLight): InternalSaleData
}