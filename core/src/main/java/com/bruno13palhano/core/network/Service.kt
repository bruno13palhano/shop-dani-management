package com.bruno13palhano.core.network

import com.bruno13palhano.core.network.model.CatalogNet
import com.bruno13palhano.core.network.model.CategoryNet
import com.bruno13palhano.core.network.model.CustomerNet
import com.bruno13palhano.core.network.model.DeliveryNet
import com.bruno13palhano.core.network.model.ProductCategoriesNet
import com.bruno13palhano.core.network.model.ProductNet
import com.bruno13palhano.core.network.model.SaleNet
import com.bruno13palhano.core.network.model.StockOrderNet
import retrofit2.http.GET

internal interface Service {

    @GET("catalog/all")
    suspend fun getCatalog(): List<CatalogNet>

    @GET("categories/all")
    suspend fun getAllCategories(): List<CategoryNet>

    @GET("customers/all")
    suspend fun getAllCustomers(): List<CustomerNet>

    @GET("deliveries/all")
    suspend fun getAllDeliveries(): List<DeliveryNet>

    @GET("products/categories/all")
    suspend fun getAllProductCategories(): List<ProductCategoriesNet>

    @GET("products/all")
    suspend fun getAllProducts(): List<ProductNet>

    @GET("sales/all")
    suspend fun getAllSales(): List<SaleNet>

    @GET("items/all")
    suspend fun getAllItems(): List<StockOrderNet>
}