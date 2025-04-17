import Util.Companion.byteArrayToHexString
import org.bouncycastle.crypto.digests.SM3Digest
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security

class SM3Util {

    companion object {

        init {
            Security.addProvider(BouncyCastleProvider())
        }

        fun sm3(byteArray: ByteArray): String {
            val digest = SM3Digest()
            digest.update(byteArray, 0, byteArray.size)
            val hash = ByteArray(digest.digestSize) // 32 字节
            digest.doFinal(hash, 0)
            return byteArrayToHexString(hash)
        }

    }

}