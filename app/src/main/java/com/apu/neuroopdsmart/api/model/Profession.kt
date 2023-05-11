package com.apu.neuroopdsmart.api.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("occupation_name", "description", "rating")
data class Profession(
    @JsonProperty("occupation_name") val occupation_name: String,
    @JsonProperty("description") val description: String,
    @JsonProperty("rating") val rating: String? = "Not Rated",
    @JsonProperty("occupation_id") val occupation_id: Int? = -1,
) {
    @JsonIgnore var isRated: Boolean = false
    init {
        isRated = rating == "Rated"
    }
}
