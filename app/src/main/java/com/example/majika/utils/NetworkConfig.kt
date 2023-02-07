package com.example.majika.utils

import com.example.majika.BuildConfig
import com.example.majika.model.Branch
import com.example.majika.model.BranchList
import com.example.majika.model.MenuList
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

        val retrofitClient = Retrofit.Builder()
            .baseUrl(endpoint)
            .client(getInterceptor())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofitClient
    }

    fun getBranchService() = getRetrofit().create(Branches::class.java)
    fun getMenuService() = getRetrofit().create(Menu::class.java)
}

interface Branches{
    @GET("branch")
    fun getAll(): Call<BranchList>
}

interface Menu{
    @GET("menu")
    fun getAll(): Call<MenuList>

    @GET("menu/food")
    fun getFood(): Call<MenuList>

    @GET("menu/drink")
    fun getDrink(): Call<MenuList>
}