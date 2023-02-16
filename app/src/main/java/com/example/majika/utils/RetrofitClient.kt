package com.example.majika.utils

import com.example.majika.BuildConfig
import com.example.majika.model.BranchList
import com.example.majika.model.MenuList
import com.example.majika.model.PaymentStatus
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

object RetrofitClient {

    private var endpoint = String.format("http://%s:%s/%s/",
        BuildConfig.IP_ADDRESS,
        BuildConfig.PORT,
        BuildConfig.API_VERSION
    )

    private val retrofit = Retrofit.Builder()
        .baseUrl(endpoint)
        .client(getInterceptor())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getInterceptor() : OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val HttpClient = OkHttpClient.Builder().addInterceptor(logging).build()

        return HttpClient
    }

    val getBranchService = retrofit.create(Branches::class.java)
    val getMenuService = retrofit.create(Menu::class.java)
    val getPaymentService = retrofit.create(Payment::class.java)
}

interface Branches{
    @GET("branch")
    suspend fun getAll(): Response<BranchList>
}

interface Menu{
    @GET("menu/food")
    suspend fun getFood(): Response<MenuList>

    @GET("menu/drink")
    suspend fun getDrink(): Response<MenuList>
}

interface Payment {
    @POST("payment/{code}")
    suspend fun getPaymentStatus(@Path("code") code: String): Response<PaymentStatus>
}