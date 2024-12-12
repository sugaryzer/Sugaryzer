package com.sugaryzer.sugaryzer.data.dataclass

data class SignInRequest(
    val email: String,
    val password: String
)

data class ScannedData(
    val code: String,
    val sugarConsume: Double
)

data class SignUpRequest(
    val email: String,
    val password: String,
    val name: String,
    val height: Int,
    val weight: Int,
    val age: Int,
)