package com.apu.neuroopdsmart.api.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("user_id", "profession_id", /*"adjectives"*/)
data class SurveyResult(
    @JsonProperty("user_id") val id: Int,
    @JsonProperty("profession_id") val prof_id: Int,
    //@JsonProperty("adjectives") val adjectives: List<Int>,
)
