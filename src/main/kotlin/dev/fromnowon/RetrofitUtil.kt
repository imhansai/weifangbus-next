package dev.fromnowon

import com.fasterxml.jackson.databind.ObjectMapper
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.*

val objectMapper by lazy { ObjectMapper() }

val encoder: Base64.Encoder by lazy { Base64.getEncoder() }

val decoder: Base64.Decoder by lazy { Base64.getDecoder() }

val retrofit: Retrofit =
    Retrofit.Builder().baseUrl("https://mobile.wfsmk.cn:3000/").addConverterFactory(JacksonConverterFactory.create())
        .build()

val loginService: LoginService = retrofit.create(LoginService::class.java)