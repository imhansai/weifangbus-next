package dev.fromnowon

import com.fasterxml.jackson.databind.node.ObjectNode
import java.util.*
import javax.crypto.Cipher

fun getOtp(): Pair<String, String> {
    val objectNode = objectMapper.createObjectNode()
    objectNode.put("otptype", "01")

    val jsonObject = reqData(objectNode)

    val call = loginService.getOtp(jsonObject)
    val response = call.execute()
    val jsonNode = response.body() ?: throw NullPointerException("Response body is null")
    val dataenc = jsonNode.get("dataenc")?.textValue() ?: throw RuntimeException("dataenc not found")
    val dataByteArray = sm4(Cipher.DECRYPT_MODE, decoder.decode(dataenc))

    val jsonNodes = objectMapper.readTree(dataByteArray)
    val otpcode = jsonNodes.get("otpcode").textValue()
    val otp = jsonNodes.get("otp").textValue()

    return otpcode to otp
}

fun login(mobilephone: String, password: String): ObjectNode {
    val (otpcode, otp) = getOtp()

    val objectNode = objectMapper.createObjectNode()
    objectNode.put("mobilephone", mobilephone)
    objectNode.put("logintype", "00")
    objectNode.put("operationsence", "01")

    val passwordHex = sm3(password.toByteArray()).uppercase()
    val passwordByteArray = hexStringToByteArray(passwordHex)
    val otpByteArray = hexStringToByteArray(otp)
    val encryptionPasswordByteArray = encryption(otpByteArray, passwordByteArray)
    val encryptionPasswordHex = byteArrayToHexString(encryptionPasswordByteArray).uppercase()
    println("encryptionPasswordHex: $encryptionPasswordHex")
    objectNode.put("loginpasswd", encryptionPasswordHex)

    objectNode.put("otpcode", otpcode)

    // DeviceUtils.b(xxActivity)
    val deviceno = UUID.randomUUID().toString()
    objectNode.put("deviceno", deviceno)

    val weifangpassword = md5(password)
    objectNode.put("weifangpassword", weifangpassword)

    val jsonObject = reqData(objectNode)

    val call = loginService.login(jsonObject)
    val response = call.execute()
    return response.body() ?: throw NullPointerException("Response body is null")
}