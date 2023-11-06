package com.fictadvisor.android

import com.fictadvisor.android.data.dto.LoginRequest
import com.fictadvisor.android.data.remote.api.AuthApi
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AuthApiTest {
    private val server = MockWebServer()
    private lateinit var authApi: AuthApi

    @Before
    fun setUp() {
        server.start(MOCK_WEBSERVER_PORT)

        val levelType: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY

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

        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .client(okhttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        authApi = retrofit.create(AuthApi::class.java)
    }

    @After
    fun shutdown() {
        server.shutdown()
    }

    @Test
    fun testLogin() = runBlocking {
        val responseJson = """{"accessToken": "some_access_token", "refreshToken": "some_refresh_token"}"""
        server.enqueue(MockResponse().setBody(responseJson))

        val loginRequest = LoginRequest(username = "some_username", password = "some_password")

        val response = authApi.login(loginRequest)

        assertTrue(response.isSuccessful)
        val authResponse = response.body()
        assertNotNull(authResponse)
        assertEquals("some_access_token", authResponse!!.accessToken)
        assertEquals("some_refresh_token", authResponse.refreshToken)
    }

    companion object {
        const val MOCK_WEBSERVER_PORT = 8080
        private const val timeoutRead = 30 // In seconds
        private const val contentType = "Content-Type"
        private const val contentTypeValue = "application/json"
        private const val timeoutConnect = 30 // In seconds
    }
}
