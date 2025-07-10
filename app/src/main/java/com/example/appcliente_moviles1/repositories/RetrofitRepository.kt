package com.example.appcliente_moviles1.repositories

import android.content.Context
import com.example.appcliente_moviles1.api.ClienteApi
import com.example.appcliente_moviles1.network.AuthInterceptor // importa tu interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitRepository {
    // Cliente para endpoints que NO necesitan auth
    val publicApi: ClienteApi by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl("http://trabajos.jmacboy.com/api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ClienteApi::class.java)
    }

    // Cliente para endpoints que SÍ necesitan auth (context para obtener token)
    fun authApi(context: Context): ClienteApi {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val authInterceptor = AuthInterceptor(context)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(authInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl("http://trabajos.jmacboy.com/api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ClienteApi::class.java)
    }
}
