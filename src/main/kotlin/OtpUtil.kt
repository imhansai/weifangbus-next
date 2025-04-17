import com.fasterxml.jackson.databind.ObjectMapper
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.*
import javax.crypto.Cipher

class OtpUtil {

    companion object {

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

    }

}