package com.bruno13palhano.core.data.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val shopDaniDispatcher: ShopDaniManagementDispatchers)

enum class ShopDaniManagementDispatchers {
    IO,
    DEFAULT
}