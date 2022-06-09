package com.example.moneytracker.model.plain_object.user

import com.google.gson.annotations.SerializedName
import com.example.moneytracker.model.BlogConstraints


data class UserResponsePlainObject (
    @SerializedName(BlogConstraints.User.name) val name: String,
    @SerializedName(BlogConstraints.User.email) val email: String,
    @SerializedName(BlogConstraints.User.token) val token: String
)
