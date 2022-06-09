package com.example.moneytracker.model.plain_object.user

import com.example.moneytracker.model.BlogConstraints
import com.google.gson.annotations.SerializedName

data class UserSignInPlainObject (
    @SerializedName(BlogConstraints.User.email) val email: String,
    @SerializedName(BlogConstraints.User.password) val password: String
)