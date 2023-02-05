package com.example.majika.utils

import com.example.majika.BuildConfig
import com.example.majika.model.Branch
import com.example.majika.model.BranchList
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class NetworkConfig {
    fun getInterceptor() : OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val HttpClient = OkHttpClient.Builder().addInterceptor(logging).build()

        return HttpClient
    }

    fun getRetrofit() : Retrofit {
        val endpoint = String.format("http://%s:%s/%s/",
            BuildConfig.IP_ADDRESS,
            BuildConfig.PORT,
            BuildConfig.API_VERSION
        )
//        val endpoint = "http://192.168.100.158:3000/v1/"
        val retrofitClient = Retrofit.Builder()
            .baseUrl(endpoint) // SEMENTARA DI HARD CODE DULU
            .client(getInterceptor())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofitClient;
    }

    fun getService() = getRetrofit().create(Branches::class.java)
}

interface Branches{
    @GET("branch")
    fun getBranch(): Call<BranchList>
}