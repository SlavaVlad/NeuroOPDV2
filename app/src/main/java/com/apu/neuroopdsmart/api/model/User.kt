package com.apu.neuroopdsmart.api.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("name", "email", "password", "gender", "role")
data class User(
    @JsonProperty("name") val name: String,
    @JsonProperty("email") val email: String,
    @JsonProperty("password") val password: String,
    @JsonProperty("gender") val gender: String,
    @JsonProperty("role") val role: String
) {
    companion object {
        fun createUser(
            user_name: String,
            user_email: String,
            user_password: String,
            gender: Boolean,
            isCompetent: Boolean
        ): User {
            return User(
                user_name,
                user_email,
                user_password,
                gender = if (gender) "Мужской" else "Женский",
                role = if (isCompetent) "Эксперт" else "Респондент"
            )
        }
    }
}
