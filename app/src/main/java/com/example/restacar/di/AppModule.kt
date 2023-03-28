package com.example.restacar.di

import android.content.Context
import com.example.restacar.common.AppResourceProvider
import com.example.restacar.common.Constants
import com.example.restacar.data.remote.repository.GetCoinRepositoryImpl
import com.example.restacar.data.service.GetCoinService
import com.example.restacar.domain.repository.GetCoinRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApi(): GetCoinService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GetCoinService::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(api: GetCoinService): GetCoinRepository {
        return GetCoinRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideAppResourceProvider(@ApplicationContext context: Context): AppResourceProvider {
        return AppResourceProvider(context)
    }
}