package com.example.msuser.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class AttributesDto(
    val email: String,
    @JsonProperty("attributes")
    val attributes: AttributesItemsDto,
    )
