package com.fictadvisor.android

import com.fictadvisor.android.data.remote.api.AuthApi
import com.fictadvisor.android.data.remote.api.ScheduleApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ScheduleApiTest {
    private val server = MockWebServer()
    private lateinit var scheduleApi: ScheduleApi

    @Before
    fun setUp() {
        server.start(MOCK_WEBSERVER_PORT)

        val levelType: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY

        val headerInterceptor = Interceptor { chain ->
            val original = chain.request()

            val request = original.newBuilder()
                .header(CONTENT_TYPE, CONTENT_TYPE_VALUE)
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
            .connectTimeout(TIMEOUT_CONNECT.toLong(), TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_READ.toLong(), TimeUnit.SECONDS)

        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .client(okhttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        scheduleApi = retrofit.create(ScheduleApi::class.java)
    }

    @After
    fun shutdown() {
        server.shutdown()
    }

    companion object {
        const val MOCK_WEBSERVER_PORT = 8080
        private const val TIMEOUT_READ = 30 // In seconds
        private const val CONTENT_TYPE = "Content-Type"
        private const val CONTENT_TYPE_VALUE = "application/json"
        private const val TIMEOUT_CONNECT = 30 // In seconds
    }
}

