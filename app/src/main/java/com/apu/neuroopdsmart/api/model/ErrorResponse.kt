package com.apu.neuroopdsmart.api.model

import com.fasterxml.jackson.annotation.JsonProperty

data class ErrorResponse(
    @JsonProperty("message") val message: String,
)
