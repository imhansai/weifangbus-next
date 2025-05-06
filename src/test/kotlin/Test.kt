import dev.fromnowon.SecurityInitializer
import dev.fromnowon.decoder
import dev.fromnowon.login
import dev.fromnowon.sm4
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import javax.crypto.Cipher

// @TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Test {

    // @BeforeAll
    // fun setUp() {
    //     SecurityInitializer
    // }

    companion object {

        @BeforeAll
        @JvmStatic
        fun setUp() {
            SecurityInitializer
        }

    }

    @Test
    fun decryption() {
        val dataenc = "响应体 dataenc"
        val data = sm4(Cipher.DECRYPT_MODE, decoder.decode(dataenc))
        println("data: ${String(data)}")
    }

    @Test
    fun getOtp() {
        val (otpcode, otp) = dev.fromnowon.getOtp()

        println("otpcode: $otpcode")
        println("otp: $otp")
    }

    @Test
    fun login() {
        val mobilephone = "你的手机号"
        val password = "你的密码"
        val jsonNode = login(mobilephone, password)
        println("登录接口响应: $jsonNode")
        val dataenc = jsonNode.get("dataenc")?.textValue() ?: throw RuntimeException("dataenc not found")
        val data = sm4(Cipher.DECRYPT_MODE, decoder.decode(dataenc))
        println("登录接口数据明文: ${String(data)}")
    }

}