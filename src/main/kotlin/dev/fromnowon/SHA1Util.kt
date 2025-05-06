package dev.fromnowon

import java.security.MessageDigest

fun sha1(input: String): String {
    val digest = MessageDigest.getInstance("SHA-1").digest(input.toByteArray())
    return digest.joinToString("") { "%02x".format(it) }
}