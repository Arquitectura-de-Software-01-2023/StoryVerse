package com.example.msuser.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.Date


data class AttributesItemsDto(
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("birthdate")
    val birthdate: Date,

    @JsonProperty("description")
    val description: String,

    @JsonProperty("library_private")
    val libraryPrivate: Boolean,

    @JsonProperty("url_header")
    val urlHeader: String,

    @JsonProperty("url_pfp")
    val urlPfp: String,
)
