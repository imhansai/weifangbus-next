package dev.fromnowon

import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

fun dateTimeAndRandomDigitsStr(count: Int): String {
    val dateTimeStr = dateTimeStr("yyyyMMddHHmmssSSS")
    val randomDigits = buildString {
        repeat(count) {
            append(Random.nextInt(9))
        }
    }
    return "$dateTimeStr$randomDigits"
}

fun dateTimeStr(pattern: String): String = SimpleDateFormat(pattern, Locale.ENGLISH).format(Date())