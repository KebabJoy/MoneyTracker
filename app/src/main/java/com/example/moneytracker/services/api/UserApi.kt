package com.example.moneytracker.services.api

import io.reactivex.Single
import com.example.moneytracker.model.plain_object.user.UserCreateRequestPlainObject
import com.example.moneytracker.model.plain_object.user.UserResponsePlainObject
import com.example.moneytracker.model.plain_object.user.UserSignInPlainObject
import retrofit2.http.*

interface UserApi {

    @GET("blog/users/{id}")
    fun fetchUser(@Path("id") id: String): UserResponse

    @POST("blog/users/sign_in")
    fun signIn(@Body user: UserSignInPlainObject): Single<UserResponsePlainObject>

    @POST("blog/users")
    fun signUp(@Body user: UserCreateRequestPlainObject): UserResponse

}

typealias UserResponse = Single<UserResponsePlainObject>