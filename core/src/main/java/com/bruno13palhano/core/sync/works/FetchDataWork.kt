package com.bruno13palhano.core.sync.works

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import com.bruno13palhano.core.data.repository.catalog.CatalogRepository
import com.bruno13palhano.core.data.repository.category.CategoryRepository
import com.bruno13palhano.core.data.repository.customer.CustomerRepository
import com.bruno13palhano.core.data.repository.delivery.DeliveryRepository
import com.bruno13palhano.core.data.repository.product.ProductRepository
import com.bruno13palhano.core.data.repository.sale.SaleRepository
import com.bruno13palhano.core.data.repository.stockorder.StockOrderRepository
import com.bruno13palhano.core.data.di.CatalogRep
import com.bruno13palhano.core.data.di.CategoryRep
import com.bruno13palhano.core.data.di.CustomerRep
import com.bruno13palhano.core.data.di.DeliveryRep
import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.ProductRep
import com.bruno13palhano.core.data.di.SaleRep
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.data.di.StockOrderRep
import com.bruno13palhano.core.sync.Synchronizer
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

@HiltWorker
class FetchDataWork @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    @CategoryRep val categoryRepository: CategoryRepository,
    @CustomerRep val customerRepository: CustomerRepository,
    @ProductRep val productRepository: ProductRepository,
    @StockOrderRep val stockOrderRepository: StockOrderRepository,
    @SaleRep val saleRepository: SaleRepository,
    @DeliveryRep val deliveryRepository: DeliveryRepository,
    @CatalogRep val catalogRepository: CatalogRepository,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
) : CoroutineWorker(context, params), Synchronizer {

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        val category = async { categoryRepository.sync() }.await()
        val product = async { productRepository.sync() }.await()
        val customer = async { customerRepository.sync() }.await()
        val stockOrder = async { stockOrderRepository.sync() }.await()
        val sale = async { saleRepository.sync() }.await()
        val delivery = async { deliveryRepository.sync() }.await()
        val catalog = async { catalogRepository.sync() }.await()

        val syncedSuccessfully = category && product && customer && stockOrder && sale && delivery
                && catalog

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