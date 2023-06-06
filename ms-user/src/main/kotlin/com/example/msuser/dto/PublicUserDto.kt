package com.example.msuser.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class PublicUserDto(
        @JsonProperty("id")
        val id: String,
        @JsonProperty("username")
        val username: String,
        @JsonProperty("email")
        val email: String,
        @JsonProperty("attributes")
        val attributes: PublicAttributesItemsDto
        )