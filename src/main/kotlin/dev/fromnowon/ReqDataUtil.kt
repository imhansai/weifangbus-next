package dev.fromnowon

import com.fasterxml.jackson.databind.node.ObjectNode
import javax.crypto.Cipher

fun reqData(objectNode: ObjectNode): ObjectNode {
    val jsonObject = objectMapper.createObjectNode()
    jsonObject.put("joininstid", "00000001")
    jsonObject.put("joininstssn", dateTimeAndRandomDigitsStr(4))
    jsonObject.put("reqdate", dateTimeStr("yyyyMMdd"))
    jsonObject.put("reqtime", dateTimeStr("HHmmss"))

    val dataJsonStr = objectMapper.writeValueAsString(objectNode)
    val dataByteArray = dataJsonStr.toByteArray()
    val dataEncryptedBytes = sm4(Cipher.ENCRYPT_MODE, dataByteArray)
    jsonObject.put("dataenc", String(encoder.encode(dataEncryptedBytes)))

    val hdata = getHdata()
    val hdataJsonStr = objectMapper.writeValueAsString(hdata)
    val hdataByteArray = hdataJsonStr.toByteArray()
    val hdataEncryptedBytes = sm4(Cipher.ENCRYPT_MODE, hdataByteArray)
    jsonObject.put("hdataenc", String(encoder.encode(hdataEncryptedBytes)))

    val payload: String = sha1(
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
    val signature = signature(idByteArray, payloadByteArray)
    val signatureBase64 = String(encoder.encode(signature))
    println("签名base64: $signatureBase64")
    jsonObject.put("sign", signatureBase64)

    return jsonObject
}

fun getHdata(url: String? = null): ObjectNode {
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