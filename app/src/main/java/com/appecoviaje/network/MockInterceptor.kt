package com.appecoviaje.network

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class MockInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString()

        val responseString = when {
            url.endsWith("trips") -> """
                [
                    {
                        "id": 1,
                        "title": "Torres del Paine (Mock)",
                        "description": "Mock description from network.",
                        "location": "Mock Location",
                        "latitude": -51.0,
                        "longitude": -73.0,
                        "imageResId": 0,
                        "category": "Montaña",
                        "isFavorite": false
                    }
                ]
            """.trimIndent()
            url.endsWith("reservations") && request.method == "POST" -> "{}" // Success empty JSON
            url.endsWith("login") && request.method == "POST" -> """
                {
                    "token": "mock-token-123",
                    "userId": 1,
                    "username": "MockUser"
                }
            """.trimIndent()
            url.endsWith("register") && request.method == "POST" -> """
                {
                    "token": "mock-token-456",
                    "userId": 2,
                    "username": "NewMockUser"
                }
            """.trimIndent()
            else -> ""
        }

        return Response.Builder()
            .code(200)
            .message("OK")
            .request(chain.request())
            .protocol(Protocol.HTTP_1_1)
            .body(responseString.toResponseBody("application/json".toMediaTypeOrNull()))
            .addHeader("content-type", "application/json")
            .build()
    }
}
