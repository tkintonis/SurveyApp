package com.example.surveyApp.di.modules

import android.content.Context
import android.net.ConnectivityManager
import com.example.surveyApp.common.constants.Constants
import com.example.surveyApp.di.common.ApplicationScope
import com.example.surveyApp.network.NetworkServiceApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkServiceModule {

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        return Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun providesNetworkApi(retrofit: Retrofit) : NetworkServiceApi {
        return retrofit.create(NetworkServiceApi::class.java)
    }

    @Singleton
    @Provides
    fun provideConnectivityManager(@ApplicationScope context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
}