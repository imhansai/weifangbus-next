package dev.fromnowon

import org.bouncycastle.crypto.digests.SM3Digest

fun sm3(byteArray: ByteArray): String {
    val digest = SM3Digest()
    digest.update(byteArray, 0, byteArray.size)
    val hash = ByteArray(digest.digestSize) // 32 字节
    digest.doFinal(hash, 0)
    return byteArrayToHexString(hash)
}