package com.apu.neuroopdsmart.api.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Adjective(
    @JsonProperty("trait") val trait: String,
    @JsonProperty("category") val category: String,
    @JsonProperty("id") val id: Int
)
