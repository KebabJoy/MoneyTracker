package com.example.moneytracker.model

class BlogConstraints {
    object Purchase {
        const val id = "id"
        const val title = "title"
        const val amount = "amount"
        const val amountSum = "amount_sum"
        const val purchases = "purchases"
    }

    object User {
        const val name = "name"
        const val email = "email"
        const val password = "password"
        const val passwordConfirmation = "password_confirmation"
        const val token = "token"
    }
}