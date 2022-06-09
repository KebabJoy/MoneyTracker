package com.example.moneytracker.model.plain_object.purchase

import com.google.gson.annotations.SerializedName
import com.example.moneytracker.model.BlogConstraints

data class PurchaseCreateRequestPlainObject(
        @SerializedName(BlogConstraints.Purchase.title) val title: String,
        @SerializedName(BlogConstraints.Purchase.amount) val amount: Int
)
