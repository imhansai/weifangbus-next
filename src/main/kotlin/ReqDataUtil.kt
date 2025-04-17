import Util.Companion.hexStringToByteArray
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import java.util.*
import javax.crypto.Cipher

class ReqDataUtil {

    companion object {

        private val objectMapper by lazy { ObjectMapper() }

        private val encoder by lazy { Base64.getEncoder() }
        private val decoder by lazy { Base64.getDecoder() }

        fun reqData(objectNode: ObjectNode): ObjectNode {
            val jsonObject = objectMapper.createObjectNode()
            jsonObject.put("joininstid", "00000001")
            jsonObject.put("joininstssn", StringTool.dateTimeAndRandomDigitsStr(4))
            jsonObject.put("reqdate", StringTool.dateTimeStr(Logger.TIMESTAMP_YYYY_MM_DD))
            jsonObject.put("reqtime", StringTool.dateTimeStr("HHmmss"))

            val dataJsonStr = objectMapper.writeValueAsString(objectNode)
            val dataByteArray = dataJsonStr.toByteArray()
            val dataEncryptedBytes = SM4Util.sm4(Cipher.ENCRYPT_MODE, dataByteArray)
            jsonObject.put("dataenc", String(encoder.encode(dataEncryptedBytes)))

            val hdata = getHdata(null)
            val hdataJsonStr = objectMapper.writeValueAsString(hdata)
            val hdataByteArray = hdataJsonStr.toByteArray()
            val hdataEncryptedBytes = SM4Util.sm4(Cipher.ENCRYPT_MODE, hdataByteArray)
            jsonObject.put("hdataenc", String(encoder.encode(hdataEncryptedBytes)))

            val payload: String = SHA1Util.sha1(
                buildString {
                    append("dataenc").append(jsonObject.get("dataenc").asText())
                    append("hdataenc").append(jsonObject.get("hdataenc").asText())
                    append("joininstid").append(jsonObject.get("joininstid").asText())
                    append("joininstssn").append(jsonObject.get("joininstssn").asText())
                    append("reqdate").append(jsonObject.get("reqdate").asText())
                    append("reqtime").append(jsonObject.get("reqtime").asText())
                }
            )

            val idByteArray = "00000001".toByteArray()
            val payloadByteArray = hexStringToByteArray(payload)
            val signature = SM2Util.signature(idByteArray, payloadByteArray)
            val signatureBase64 = String(encoder.encode(signature))
            println("签名base64: $signatureBase64")
            jsonObject.put("sign", signatureBase64)

            return jsonObject
        }

        fun getHdata(url: String?): ObjectNode {
            val objectMapper = ObjectMapper()
            val objectNode = objectMapper.createObjectNode()
            objectNode.put("instid", "00000001")

            objectNode.put("chnlid", "00")
            objectNode.put("mchntid", Constants.Base.MCHNTID)

            // objectNode.put(
            //     "devicetoken",
            //     ""
            // )

            return objectNode
        }

    }

}