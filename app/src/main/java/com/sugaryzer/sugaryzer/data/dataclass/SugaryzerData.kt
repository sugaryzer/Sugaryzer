package com.sugaryzer.sugaryzer.data.dataclass

data class SignInRequest(
    val email: String,
    val password: String
)

data class ScannedData(
    val code: String,
    val sugarConsume: Double
)