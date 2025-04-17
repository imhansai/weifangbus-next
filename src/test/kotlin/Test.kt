import Util.Companion.byteArrayToHexString
import Util.Companion.hexStringToByteArray
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
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

        val (otpcode, otp) = OtpUtil.getOtp()

        val objectMapper = ObjectMapper()
        val objectNode = objectMapper.createObjectNode()
        objectNode.put("mobilephone", mobilephone)
        objectNode.put("logintype", "00")
        objectNode.put("operationsence", "01")

        val passwordHex = SM3Util.sm3(password.toByteArray()).uppercase()
        val passwordByteArray = hexStringToByteArray(passwordHex)
        val otpByteArray = hexStringToByteArray(otp)

        val encryptionPasswordByteArray = SM2Util.encryption(otpByteArray, passwordByteArray)
        val encryptionPasswordHex = byteArrayToHexString(encryptionPasswordByteArray).uppercase()
        println("encryptionPasswordHex: $encryptionPasswordHex")

        objectNode.put("loginpasswd", encryptionPasswordHex)
        objectNode.put("otpcode", otpcode)
        // DeviceUtils.b(xxActivity)
        val deviceno = UUID.randomUUID().toString()
        objectNode.put("deviceno", deviceno)

        val weifangpassword = MD5Util.md5(password)
        objectNode.put("weifangpassword", weifangpassword)

        val jsonObject = ReqDataUtil.reqData(objectNode)

        val retrofit = Retrofit.Builder().baseUrl("https://mobile.wfsmk.cn:3000/")
            .addConverterFactory(JacksonConverterFactory.create()).build()
        val service = retrofit.create(LoginService::class.java)
        val call = service.login(jsonObject)
        val response = call.execute()
        println(response.body())
    }

}