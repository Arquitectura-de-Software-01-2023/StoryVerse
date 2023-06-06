package com.example.msstory.service

import com.example.msstory.dto.ResponseDto
import com.example.msstory.dto.UserDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader


@FeignClient(name = "ms-user")
interface UserService {
    @GetMapping("api/v1/user/")
    fun getUser(@RequestHeader headers: Map<String, String>): ResponseDto<UserDto>

    @GetMapping("api/v1/user/{username}")
    fun getUserByUsername(@PathVariable username: String ,@RequestHeader headers: Map<String, String>): ResponseDto<UserDto>

    @GetMapping("api/v1/user/userId/{userId}")
    fun getUserById(@PathVariable userId: String ,@RequestHeader headers: Map<String, String>): ResponseDto<UserDto>
}