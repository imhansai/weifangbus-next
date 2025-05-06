package dev.fromnowon

import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

private val publicKeyByteArray by lazy { hexStringToByteArray(Constants.Base.SERVER_DG_KEY) }

fun sm4(opmode: Int, dataByteArray: ByteArray): ByteArray {
    val algorithm = "SM4"
    val secretKey = SecretKeySpec(publicKeyByteArray, algorithm)
    val provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME)
    val cipher = Cipher.getInstance(algorithm, provider)
    cipher.init(opmode, secretKey)
    return cipher.doFinal(dataByteArray)
}