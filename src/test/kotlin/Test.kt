import org.junit.jupiter.api.Test
import java.util.*
import javax.crypto.Cipher

class Test {

    @Test
    fun decryption() {
        val dataenc =
            "oUarkWzXUL+jRW6AF9s1+Q=="
        val data = SM4Util.sm4(Cipher.DECRYPT_MODE, Base64.getDecoder().decode(dataenc))
        println("data: ${String(data)}")
    }

    @Test
    fun login() {
        // 你的手机号
        val mobilephone = "xxxxxxxxxxx"
        // 你的密码
        val password = "yyyyyyyyyyyyy"
        MemberService().login(mobilephone, password)
    }

}