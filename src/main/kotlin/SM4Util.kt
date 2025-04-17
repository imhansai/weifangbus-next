import Util.Companion.hexStringToByteArray
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class SM4Util {

    companion object {

        init {
            // https://www.bouncycastle.org/download/bouncy-castle-java/
            Security.addProvider(BouncyCastleProvider())
        }

        private val decoder by lazy { Base64.getDecoder() }

        private val publicKeyByteArray by lazy { hexStringToByteArray(Constants.Base.SERVER_DG_KEY) }

        fun sm4(opmode: Int, dataByteArray: ByteArray): ByteArray {
            val algorithm = "SM4"
            val secretKey = SecretKeySpec(publicKeyByteArray, algorithm)
            val provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME)
            val cipher = Cipher.getInstance(algorithm, provider)
            cipher.init(opmode, secretKey)
            return cipher.doFinal(dataByteArray)
        }

    }

}