package com.fictadvisor.android

import com.fictadvisor.android.data.dto.schedule.PatchEventDTO
import com.fictadvisor.android.data.dto.schedule.PostEventDTO
import com.fictadvisor.android.data.dto.schedule.TDiscipline
import com.fictadvisor.android.data.dto.schedule.TEventPeriod
import com.fictadvisor.android.data.dto.schedule.Teacher
import com.fictadvisor.android.data.remote.api.ScheduleApi
import kotlinx.coroutines.test.runTest
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
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

    @Test
    fun `deleteEventVyId should return the deleted event`() = runTest {
        // Prepare a sample response JSON representing an event
        val responseJson = """
            {
              "id": "some-event-id",
              "name": "Some event",
              "disciplineId": "some-discipline-id",
              "eventType": "LECTURE",
              "startTime": "2024-03-08T13:47:59.823Z",
              "endTime": "2024-03-08T13:48:00.823Z",
              "period": "NO_PERIOD",
              "url": "https://example.com",
              "eventInfo": "This is a test event",
              "disciplineInfo": "Some discipline",
              "teachers": [
                {
                  "id": "some-teacher-id",
                  "firstName": "Terry",
                  "middleName": "A.",
                  "lastName": "Davis"
                }
              ]
            }
        """.trimIndent()

        server.enqueue(MockResponse().setBody(responseJson))

        val response = scheduleApi.deleteEventById("some-group-id", "some-event-id")

        assertTrue(response.isSuccessful)

        val eventResponse = response.body()

        assertNotNull(eventResponse)

        assertEquals("https://example.com", eventResponse!!.url)
        assertEquals("This is a test event", eventResponse.eventInfo)
        assertEquals(TDiscipline.LECTURE, eventResponse.eventType)
        assertEquals("Some discipline", eventResponse.disciplineInfo)
        assertEquals(TEventPeriod.NO_PERIOD, eventResponse.period)
        val teacher = eventResponse.teachers[0]
        assertEquals("some-teacher-id", teacher.id)
        assertEquals("Terry", teacher.firstName)
        assertEquals("A.", teacher.middleName)
        assertEquals("Davis", teacher.lastName)
    }

    @Test
    fun `addEvent should return the added event`() = runTest {
        val testPostEventDTO = PostEventDTO(
            groupId = "some-group-id",
            teachers = listOf("some-teacher-id"),
            disciplineId = "some-discipline-id",
            url = "https://example.com",
            eventInfo = "This is a test event",
            eventType = TDiscipline.LECTURE,
            disciplineInfo = "Some discipline",
            period = TEventPeriod.NO_PERIOD
        )

        // Prepare a sample response JSON representing an event
        val responseJson = """
            {
              "id": "some-event-id",
              "name": "Some event",
              "disciplineId": "some-discipline-id",
              "eventType": "LECTURE",
              "startTime": "2024-03-08T13:47:59.823Z",
              "endTime": "2024-03-08T13:48:00.823Z",
              "period": "NO_PERIOD",
              "url": "https://example.com",
              "eventInfo": "This is a test event",
              "disciplineInfo": "Some discipline",
              "teachers": [
                {
                  "id": "some-teacher-id",
                  "firstName": "Terry",
                  "middleName": "A.",
                  "lastName": "Davis"
                }
              ]
            }
        """.trimIndent()

        server.enqueue(MockResponse().setBody(responseJson))

        val response = scheduleApi.addEvent(testPostEventDTO, "some-group-id")

        assertTrue(response.isSuccessful)

        val eventResponse = response.body()

        assertNotNull(eventResponse)

        assertEquals("https://example.com", eventResponse!!.url)
        assertEquals("This is a test event", eventResponse.eventInfo)
        assertEquals(TDiscipline.LECTURE, eventResponse.eventType)
        assertEquals("Some discipline", eventResponse.disciplineInfo)
        assertEquals(TEventPeriod.NO_PERIOD, eventResponse.period)
        val teacher = eventResponse.teachers[0]
        assertEquals("some-teacher-id", teacher.id)
        assertEquals("Terry", teacher.firstName)
        assertEquals("A.", teacher.middleName)
        assertEquals("Davis", teacher.lastName)
    }

    // TODO: test for editEvent() after discussing DTO
    // @Test
    // fun `editEvent should return the edited event`() = runTest {
    //
    // }


    companion object {
        const val MOCK_WEBSERVER_PORT = 8080
        private const val TIMEOUT_READ = 30 // In seconds
        private const val CONTENT_TYPE = "Content-Type"
        private const val CONTENT_TYPE_VALUE = "application/json"
        private const val TIMEOUT_CONNECT = 30 // In seconds
    }
}

