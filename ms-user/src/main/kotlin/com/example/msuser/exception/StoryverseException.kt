package com.example.msuser.exception

data class StoryverseException(override var message: String) : RuntimeException(message)