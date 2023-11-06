package com.fictadvisor.android.data.remote

import com.fictadvisor.android.data.remote.api.AuthApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val timeoutRead = 30 // In seconds
private const val contentType = "Content-Type"
private const val contentTypeValue = "application/json"
private const val timeoutConnect = 30 // In seconds

object RetrofitClient {

    private const val baseUrl = "https://apidev.fictadvisor.com/v2/"

    val retrofitClient: Retrofit.Builder by lazy {

        val levelType: Level
//        if (BuildConfig.DEBUG)
//            levelType = Level.BODY else levelType = Level.NONE

        levelType = Level.BODY

        val headerInterceptor = Interceptor { chain ->
            val original = chain.request()

            val request = original.newBuilder()
                .header(contentType, contentTypeValue)
                .method(original.method, original.body)
                .build()

            chain.proceed(request)
        }

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(levelType)

        val okhttpClient = OkHttpClient.Builder()
        okhttpClient
            .addInterceptor(loggingInterceptor)
            .addInterceptor(headerInterceptor)
            .connectTimeout(timeoutConnect.toLong(), TimeUnit.SECONDS)
            .readTimeout(timeoutRead.toLong(), TimeUnit.SECONDS)

        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okhttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
    }

    val authApi: AuthApi by lazy {
        retrofitClient
            .build()
            .create(AuthApi::class.java)
    }
}
