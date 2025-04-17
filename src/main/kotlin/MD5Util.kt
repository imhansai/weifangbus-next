import java.security.MessageDigest

class MD5Util {

    companion object {

        fun md5(input: String, length: Int = 32): String {
            val md = MessageDigest.getInstance("MD5")
            val digest = md.digest(input.toByteArray())

            val fullHash = buildString {
                for (byte in digest) {
                    // String.format("%02X", byte and 0xff.toByte())
                    append((byte.toInt() and 0xFF).toString(16).padStart(2, '0'))
                }
            }.uppercase()

            return when (length) {
                32 -> fullHash
                16 -> fullHash.substring(8, 24)
                else -> throw IllegalArgumentException("不支持的长度: $length")
            }
        }

    }

}