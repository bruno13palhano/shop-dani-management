package com.bruno13palhano.core.sync.works

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import com.bruno13palhano.core.data.CatalogData
import com.bruno13palhano.core.data.CategoryData
import com.bruno13palhano.core.data.CustomerData
import com.bruno13palhano.core.data.DeliveryData
import com.bruno13palhano.core.data.ProductData
import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.data.StockOrderData
import com.bruno13palhano.core.data.di.CatalogRep
import com.bruno13palhano.core.data.di.CategoryRep
import com.bruno13palhano.core.data.di.CustomerRep
import com.bruno13palhano.core.data.di.DeliveryRep
import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.ProductRep
import com.bruno13palhano.core.data.di.SaleRep
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.data.di.StockOrderRep
import com.bruno13palhano.core.model.Catalog
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Customer
import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.model.StockOrder
import com.bruno13palhano.core.sync.Synchronizer
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

@HiltWorker
class FetchDataWork @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    @CategoryRep val categoryRepository: CategoryData<Category>,
    @CustomerRep val customerRepository: CustomerData<Customer>,
    @ProductRep val productRepository: ProductData<Product>,
    @StockOrderRep val stockOrderRepository: StockOrderData<StockOrder>,
    @SaleRep val saleRepository: SaleData<Sale>,
    @DeliveryRep val deliveryRepository: DeliveryData<Delivery>,
    @CatalogRep val catalogRepository: CatalogData<Catalog>,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
) : CoroutineWorker(context, params), Synchronizer {

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        val syncedSuccessfully = awaitAll(
            async { categoryRepository.sync() },
            async { productRepository.sync() },
            async { customerRepository.sync() },
            async { stockOrderRepository.sync() },
            async { saleRepository.sync() },
            async { deliveryRepository.sync() },
            async { catalogRepository.sync() },
        ).all { it }

        if (syncedSuccessfully) {
            Result.success()
        } else {
            Result.retry()
        }
    }

    companion object {
        fun startUpSyncWork() = OneTimeWorkRequestBuilder<FetchDataWork>()
            .setConstraints(Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            )
            .build()
    }
}