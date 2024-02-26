package com.fictadvisor.android

import com.fictadvisor.android.data.dto.LoginRequest
import com.fictadvisor.android.data.dto.OrdinaryStudentResponse
import com.fictadvisor.android.data.dto.RegisterTelegramDTO
import com.fictadvisor.android.data.dto.RegistrationDTO
import com.fictadvisor.android.data.dto.TelegramDTO
import com.fictadvisor.android.data.dto.VerificationEmailDTO
import com.fictadvisor.android.data.remote.api.AuthApi
import com.google.gson.Gson
import kotlinx.coroutines.test.runTest
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
import org.mockito.Mockito.mock
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

        authApi = retrofit.create(AuthApi::class.java)
    }

    @After
    fun shutdown() {
        server.shutdown()
    }

    @Test
    fun `login should return access and refresh tokens`() = runTest {
        val responseJson =
            """{"accessToken": "some_access_token", "refreshToken": "some_refresh_token"}"""
        server.enqueue(MockResponse().setBody(responseJson))
        val loginRequest = mock(LoginRequest::class.java)

        val response = authApi.login(loginRequest)

        assertTrue(response.isSuccessful)
        val authResponse = response.body()
        assertNotNull(authResponse)
        assertEquals("some_access_token", authResponse!!.accessToken)
        assertEquals("some_refresh_token", authResponse.refreshToken)
    }
    @Test
    fun `loginTelegram should return access and refresh tokens`() = runTest {
        val responseJson = """{"accessToken": "some_access_token", "refreshToken": "some_refresh_token"}"""
        server.enqueue(MockResponse().setBody(responseJson))

        val telegramDTO = mock(TelegramDTO::class.java)

        val response = authApi.loginTelegram(telegramDTO)

        assert(response.isSuccessful)
        val authLoginResponse = response.body()
        assertNotNull(authLoginResponse)
        assertEquals("some_access_token", authLoginResponse!!.accessToken)
        assertEquals("some_refresh_token", authLoginResponse.refreshToken)
    }

    @Test
    fun `register should create a new user`() = runTest {
        val responseJson = """User registered successfully"""
        server.enqueue(MockResponse().setBody(responseJson))

        val registrationRequest = mock(RegistrationDTO::class.java)
        val response = authApi.register(registrationRequest)


        assertTrue(response.isSuccessful)
        val responseBody = response.body()
        assertNotNull(responseBody)
        assertEquals("User registered successfully", responseBody!!.string())
    }

    @Test
    fun `registerTelegram should register with Telegram` () = runTest {
        val responseJson = """Telegram registration successful"""
        server.enqueue(MockResponse().setBody(responseJson))

        val registrationTelegramRequest = mock(RegisterTelegramDTO::class.java)
        val response = authApi.registerTelegram(registrationTelegramRequest)

        assertTrue(response.isSuccessful)
        val responseBody = response.body()
        assertNotNull(responseBody)
        assertEquals("Telegram registration successful", responseBody!!.string())
    }

    @Test
    fun `refresh should return new tokens` () = runTest {
        val responseJson =
            """{"accessToken": "new_access_token"}"""
        server.enqueue(MockResponse().setBody(responseJson))

        val response = authApi.refresh()

        assertTrue(response.isSuccessful)
        val authRefreshResponse = response.body()
        assertNotNull(authRefreshResponse)
        assertEquals("new_access_token", authRefreshResponse!!.accessToken)
    }

    @Test
    fun `verifyEmail should verify user's email` () = runTest {
        val responseJson = """Email verification successful"""
        server.enqueue(MockResponse().setBody(responseJson))

        val verifyEmailRequest = mock(VerificationEmailDTO::class.java)
        val response = authApi.verifyEmail(verifyEmailRequest)

        assertTrue(response.isSuccessful)
        val responseBody = response.body()
        assertNotNull(responseBody)
        assertEquals("Email verification successful", responseBody!!.string())
    }

    //auth
    @Test
    fun `getStudent should return successful response` () = runTest {
        }

    @Test
    fun `verifyIsRegistered should return true when user is registered`() = runTest {
        val responseJson = "true"
        server.enqueue(MockResponse().setBody(responseJson))

        val response = authApi.verifyIsRegistered("username", "email")

        assertTrue(response.isSuccessful)
        val isRegistered = response.body()
        assertNotNull(isRegistered)
        assertTrue(isRegistered!!)
    }

    @Test
    fun `checkCaptain should return true when user is a captain`() = runTest {
        val responseJson = "true"
        server.enqueue(MockResponse().setBody(responseJson))

        val groupId = "some_group_id"
        val response = authApi.checkCaptain(groupId)

        assertTrue(response.isSuccessful)
        val isCaptain = response.body()
        assertNotNull(isCaptain)
        assertTrue(isCaptain!!)
    }

    @Test
    fun `checkResetToken should return token status`() = runTest {
        val responseJson = """{"isAvailable": true}"""
        server.enqueue(MockResponse().setBody(responseJson))

        val token = "some_token"
        val response = authApi.checkResetToken(token)

        assertTrue(response.isSuccessful)
        val tokenStatus = response.body()
        assertNotNull(tokenStatus)
        assertTrue(tokenStatus!!.isAvailable)
    }

    @Test
    fun `checkRegisterTelegram should return registration status`() = runTest {
        val responseJson = """{"isRegistered": true}"""
        server.enqueue(MockResponse().setBody(responseJson))

        val token = "some_token"
        val response = authApi.checkRegisterTelegram(token)

        assertTrue(response.isSuccessful)
        val registrationStatus = response.body()
        assertNotNull(registrationStatus)
        assertTrue(registrationStatus!!.isRegistered)
    }

    @Test
    fun `getStudent should return student information`() = runTest {
        // Prepare a sample response JSON representing a student
        val responseJson = """
            {
                "id": "123456",
                "firstName": "John",
                "middleName": "",
                "lastName": "Doe",
                "state": "APPROVED",
                "username": "johndoe",
                "email": "john.doe@example.com",
                "avatar": "http://example.com/avatar.jpg",
                "telegramId": 123456,
                "group": {
                    "id": "789",
                    "code": "group_code",
                    "state": "APPROVED",
                    "role": "USER"
                }
            }
        """.trimIndent()

        server.enqueue(MockResponse().setBody(responseJson))

        val response = authApi.getStudent()

        assertTrue(response.isSuccessful)

        val studentResponse = response.body()

        assertNotNull(studentResponse)

        assertEquals("123456", studentResponse!!.id)
        assertEquals("John", studentResponse.firstName)
        assertEquals("", studentResponse.middleName)
        assertEquals("Doe", studentResponse.lastName)
        assertEquals("johndoe", studentResponse.username)
        assertEquals("john.doe@example.com", studentResponse.email)
        assertEquals("http://example.com/avatar.jpg", studentResponse.avatar)
        assertEquals(123456, studentResponse.telegramId)
        assertEquals("789", studentResponse.group.id)
        assertEquals("group_code", studentResponse.group.code)
        assertEquals("APPROVED", studentResponse.group.state)
        assertEquals("USER", studentResponse.group.role)
    }


    companion object {
        const val MOCK_WEBSERVER_PORT = 8080
        private const val TIMEOUT_READ = 30 // In seconds
        private const val CONTENT_TYPE = "Content-Type"
        private const val CONTENT_TYPE_VALUE = "application/json"
        private const val TIMEOUT_CONNECT = 30 // In seconds
    }
}




