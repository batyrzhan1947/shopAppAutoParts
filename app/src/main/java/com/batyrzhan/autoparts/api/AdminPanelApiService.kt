package com.batyrzhan.autoparts.api

import com.batyrzhan.autoparts.model.*
import com.batyrzhan.autoparts.utils.Constant
import com.google.gson.GsonBuilder
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface AdminPanelApiService {
    companion object {
        private const val url = Constant.BASE_URL
        private var retrofit: Retrofit? = null
        fun create(): AdminPanelApiService? {
            if (retrofit == null) {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BASIC
                val client = OkHttpClient.Builder().apply {
                    readTimeout(20, TimeUnit.SECONDS)
                    writeTimeout(20, TimeUnit.SECONDS)
                    connectTimeout(20, TimeUnit.SECONDS)
                    addInterceptor(interceptor)
                }
                retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .addConverterFactory(
                        GsonConverterFactory.create(GsonBuilder().setLenient().create())
                    )
                    .baseUrl(url)
                    .client(client.build())
                    .build()
            }
            return retrofit?.create(AdminPanelApiService::class.java)
        }
    }


    @GET("/api/api.php?get_category")
    fun getCategories(): Single<List<PartCategory>>

    @GET("/api/api.php?")
    fun getProduct(
        @Query("product_id") id: Int
    ): Single<Product>

    @GET("/api/api.php?")
    fun getHelpContent(
        @Query("help_id") helpId: Int
    ): Single<List<Help>>

    @GET("/api/api.php?")
    fun getProducts(
        @Query("category_id") cat_id: String
    ): Single<List<Product>>

    @GET("/api/api.php?get_recent")
    fun getRecently(): Single<List<Product>>

    @GET("/api/api.php?")
    fun getProductMarks(
        @Query("get_marks_with_id") p_id: Int
    ): Single<MarksProduct>

    @GET("/api/api.php?get_help")
    fun getHelps(): Single<List<Help>>

    @GET("/api/api.php?get_shipping")
    fun getShipping(): Single<List<Shipping>>

    @GET("/api/api.php?get_tax_currency")
    fun getTaxCurrency(): Single<TaxCurrency>

    @GET("/api/api.php?get_template")
    fun getNotifications(): Single<List<ShopNotification>>

    @GET("/api/api.php?get_marks")
    fun getMarksAuto(): Single<List<MarkaAuto>>
}