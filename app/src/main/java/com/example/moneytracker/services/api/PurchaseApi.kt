package com.example.moneytracker.services.api

import com.example.moneytracker.model.plain_object.purchase.PurchaseCreateRequestPlainObject
import com.example.moneytracker.model.plain_object.purchase.PurchasePlainObject
import com.example.moneytracker.model.plain_object.purchase.PurchaseResponsePlainObject
import io.reactivex.Single
import com.example.moneytracker.model.plain_object.user.UserResponsePlainObject
import retrofit2.http.*

interface PurchaseApi {

    @GET("blog/purchases")
    fun fetchPurchases(@Header("Authorization") token: String): PurchaseResponse

    @POST("blog/purchases")
    fun createPurchase(@Body purchase: PurchaseCreateRequestPlainObject,
                       @Header("Authorization") token: String): Single<PurchasePlainObject>
}

typealias PurchaseResponse = Single<PurchaseResponsePlainObject>