import Util.Companion.byteArrayToHexString
import Util.Companion.hexStringToByteArray
import com.fasterxml.jackson.databind.ObjectMapper
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.*
import javax.crypto.Cipher

class MemberService {

    fun getOtp(): Pair<String, String> {
        val objectMapper = ObjectMapper()
        val objectNode = objectMapper.createObjectNode()
        objectNode.put("otptype", "01")

        val jsonObject = ReqDataUtil.reqData(objectNode)

        val retrofit = Retrofit.Builder().baseUrl("https://mobile.wfsmk.cn:3000/")
            .addConverterFactory(JacksonConverterFactory.create()).build()
        val service = retrofit.create(LoginService::class.java)
        val call = service.getOtp(jsonObject)
        val response = call.execute()
        val jsonNode = response.body()!!.get("dataenc")
        val dataenc = jsonNode.textValue()
        val dataByteArray = SM4Util.sm4(Cipher.DECRYPT_MODE, Base64.getDecoder().decode(dataenc))

        val jsonNodes = objectMapper.readTree(dataByteArray)
        val otpcode = jsonNodes.get("otpcode").textValue()
        val otp = jsonNodes.get("otp").textValue()

        println("otpcode: $otpcode")
        println("otp: $otp")

        return otpcode to otp
    }

    fun login(mobilephone: String, password: String) {
        val (otpcode, otp) = getOtp()

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