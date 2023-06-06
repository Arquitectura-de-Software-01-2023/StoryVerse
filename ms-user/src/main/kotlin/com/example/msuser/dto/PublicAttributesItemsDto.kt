package com.example.msuser.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.Date


data class PublicAttributesItemsDto(
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("birthdate")
    val birthdate: List<Date>,

    @JsonProperty("description")
    val description: List<String>,

    @JsonProperty("library_private")
    val libraryPrivate: List<Boolean>,

    @JsonProperty("url_header")
    val urlHeader: List<String>,

    @JsonProperty("url_pfp")
    val urlPfp: List<String>,
)
