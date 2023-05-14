package com.apu.neuroopdsmart.api.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("test_id", "attempt", "result", "duration")
class TestResult(
    @JsonProperty("test_id") val test_id: Int? = null,
    @JsonProperty("attempt") val attempt: Float = 0F,
    @JsonProperty("result") val result: Float? = null,
    @JsonProperty("duration") val duration: Float? = null,
)