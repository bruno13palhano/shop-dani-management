package com.bruno13palhano.core.network

import com.bruno13palhano.core.network.model.CatalogNet
import com.bruno13palhano.core.network.model.CategoryNet
import com.bruno13palhano.core.network.model.CustomerNet
import com.bruno13palhano.core.network.model.DataVersionNet
import com.bruno13palhano.core.network.model.ProductNet
import com.bruno13palhano.core.network.model.SaleNet
import com.bruno13palhano.core.network.model.StockItemNet
import com.bruno13palhano.core.network.model.UserNet
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

internal interface Service {
    @POST("users/login")
    suspend fun login(@Body user: UserNet): Response<Unit>

    @POST("users/insert")
    suspend fun createUser(@Body user: UserNet): Response<UserNet>

    @PUT("users/update")
    suspend fun updateUser(@Body user: UserNet): Response<Int>

    @PUT("users/update/password")
    suspend fun updateUserPassword(@Body user: UserNet): Response<Int>

    @GET("users/user/{username}")
    suspend fun getUser(@Path("username") username: String): UserNet

    @POST("users/authenticated")
    suspend fun authenticated(@Body token: String): Boolean

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
    suspend fun insertSale(@Body saleNet: SaleNet)

    @PUT("sales/update")
    suspend fun updateSale(@Body sale: SaleNet)

    @DELETE("sales/delete/{id}")
    suspend fun deleteSale(@Path("id") id: Long)

    @GET("items/all")
    suspend fun getAllItems(): List<StockItemNet>

    @POST("items/insert")
    suspend fun insertItem(@Body item: StockItemNet)

    @PUT("items/update")
    suspend fun updateItem(@Body item: StockItemNet)

    @PUT("items/update/{id}/{quantity}")
    suspend fun updateStockItemQuantity(@Path("id") id: Long, @Path("quantity") quantity: Int)

    @DELETE("items/delete/{id}")
    suspend fun deleteItem(@Path("id") id: Long)

    @GET("version/all")
    suspend fun getVersion(): List<DataVersionNet>

    @POST("version/insert")
    suspend fun insertVersion(@Body version: DataVersionNet)

    @PUT("version/update")
    suspend fun updateVersion(@Body version: DataVersionNet)

    @DELETE("version/delete/{id}")
    suspend fun deleteVersion(@Path("id") id: Long)
}