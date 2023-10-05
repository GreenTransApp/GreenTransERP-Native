package com.greensoft.kgs_printer.hilt

import com.greensoft.greentranserpnative.ENV
import com.greensoft.greentranserpnative.api.Api
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(3, TimeUnit.MINUTES).build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Api {
        return Retrofit.Builder()
            .baseUrl(ENV.getBaseUrl()).client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }
}