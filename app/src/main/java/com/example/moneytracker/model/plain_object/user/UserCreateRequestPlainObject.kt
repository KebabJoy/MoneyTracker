package com.example.moneytracker.model.plain_object.user

import com.google.gson.annotations.SerializedName
import com.example.moneytracker.model.BlogConstraints

data class UserCreateRequestPlainObject(
    @SerializedName(BlogConstraints.User.email) val email: String,
    @SerializedName(BlogConstraints.User.password) val password: String,
    @SerializedName(BlogConstraints.User.passwordConfirmation) val passwordConfirmation: String
)
