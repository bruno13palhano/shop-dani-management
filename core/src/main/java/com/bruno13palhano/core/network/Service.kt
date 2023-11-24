package com.bruno13palhano.core.network

import com.bruno13palhano.core.network.model.CatalogNet
import com.bruno13palhano.core.network.model.CategoryNet
import com.bruno13palhano.core.network.model.CustomerNet
import com.bruno13palhano.core.network.model.DeliveryNet
import com.bruno13palhano.core.network.model.ProductNet
import com.bruno13palhano.core.network.model.SaleNet
import com.bruno13palhano.core.network.model.StockOrderNet
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

internal interface Service {

    @GET("catalog/all")
    suspend fun getCatalog(): List<CatalogNet>

    @POST("catalog/insert")
    suspend fun insertCatalogItem(@Body catalogItem: CatalogNet)

    @PUT("catalog/update")
    suspend fun updateCatalogItem(@Body catalogItem: CatalogNet)

    @DELETE("catalog/delete/{id}")
    suspend fun deleteCatalogItem(@Path("id") id: Long)

    @GET("categories/all")
    suspend fun getAllCategories(): List<CategoryNet>

    @POST("categories/insert")
    suspend fun insertCategory(@Body category: CategoryNet)

    @PUT("categories/update")
    suspend fun updateCategory(@Body category: CategoryNet)

    @DELETE("categories/delete/{id}")
    suspend fun deleteCategory(@Path("id") id: Long)

    @GET("customers/all")
    suspend fun getAllCustomers(): List<CustomerNet>

    @POST("customers/insert")
    suspend fun insertCustomer(@Body customer: CustomerNet)

    @PUT("customers/update")
    suspend fun updateCustomer(@Body customer: CustomerNet)

    @DELETE("customers/delete/{id}")
    suspend fun deleteCustomer(@Path("id") id: Long)

    @GET("deliveries/all")
    suspend fun getAllDeliveries(): List<DeliveryNet>

    @POST("deliveries/update")
    suspend fun updateDelivery(@Body delivery: DeliveryNet)

    @DELETE("deliveries/delete/{id}")
    suspend fun deleteDelivery(@Path("id") id: Long)

    @GET("products/all")
    suspend fun getAllProducts(): List<ProductNet>

    @POST("products/insert")
    suspend fun insertProduct(@Body product: ProductNet)

    @PUT("products/update")
    suspend fun updateProduct(@Body product: ProductNet)

    @DELETE("products/delete/{id}")
    suspend fun deleteProduct(@Path("id") id: Long)

    @GET("sales/all")
    suspend fun getAllSales(): List<SaleNet>

    @POST("sales/insert")
    suspend fun insertSale(@Body sale: SaleNet)

    @PUT("sales/update")
    suspend fun updateSale(@Body sale: SaleNet)

    @DELETE("sales/delete/{id}")
    suspend fun deleteSale(@Path("id") id: Long)

    @GET("items/all")
    suspend fun getAllItems(): List<StockOrderNet>

    @POST("items/insert")
    suspend fun insertItem(@Body item: StockOrderNet)

    @PUT("items/update")
    suspend fun updateItem(@Body item: StockOrderNet)

    @DELETE("items/delete/{id}")
    suspend fun deleteItem(@Path("id") id: Long)
}