package com.bruno13palhano.core.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
internal annotation class DefaultWorkbook

@InstallIn(SingletonComponent::class)
@Module
internal object ExcelModule {

    init {
        System.setProperty(
            "org.apache.poi.javax.xml.stream.XMLInputFactory",
            "com.fasterxml.aalto.stax.InputFactoryImpl"
        )
        System.setProperty(
            "org.apache.poi.javax.xml.stream.XMLOutputFactory",
            "com.fasterxml.aalto.stax.OutputFactoryImpl"
        )
        System.setProperty(
            "org.apache.poi.javax.xml.stream.XMLEventFactory",
            "com.fasterxml.aalto.stax.EventFactoryImpl"
        )
    }
    @DefaultWorkbook
    @Provides
    @Singleton
    fun provideXSSFWorkbook(): Workbook = XSSFWorkbook()
}