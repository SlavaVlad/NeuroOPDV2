package com.apu.neuroopdsmart.api.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("id", "user_id", "prof_name", "adj_name")
data class SurveyProfession(
    @JsonProperty("id") val id: Int,
    @JsonProperty("user_id") val user_id: Int,
    @JsonProperty("prof_name") val profName: String,
    @JsonProperty("adj_name") val adjName: String,
) {
    var map: Map<String, String> = mapOf(
        "id" to id.toString(),
        "user_id" to user_id.toString(),
        "prof_name" to profName,
        "adj_name" to adjName,
    )
}
