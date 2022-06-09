package com.example.moneytracker.model.plain_object.purchase

import com.google.gson.annotations.SerializedName
import com.example.moneytracker.model.BlogConstraints

data class PurchaseResponsePlainObject (
    @SerializedName(BlogConstraints.Purchase.amountSum) val amountSum: Int,
    @SerializedName(BlogConstraints.Purchase.purchases) val purchases: MutableList<PurchasePlainObject>
)
