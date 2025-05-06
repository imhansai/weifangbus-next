package dev.fromnowon

import org.bouncycastle.asn1.gm.GMNamedCurves
import org.bouncycastle.crypto.engines.SM2Engine
import org.bouncycastle.crypto.params.*
import org.bouncycastle.crypto.signers.PlainDSAEncoding
import org.bouncycastle.crypto.signers.SM2Signer
import org.bouncycastle.jce.ECNamedCurveTable
import java.math.BigInteger
import java.security.SecureRandom

fun signature(idByteArray: ByteArray, payloadByteArray: ByteArray): ByteArray {

    // 获取 SM2 椭圆曲线参数
    // val spec = ECNamedCurveTable.getParameterSpec("sm2p256v1")
    val sm2ECParameters = GMNamedCurves.getByName("sm2p256v1")
    val domainParameters = ECDomainParameters(
        sm2ECParameters.curve,
        sm2ECParameters.g,
        sm2ECParameters.n,
        sm2ECParameters.h
    )

    // 私钥(十六进制字符串)
    val privateKeyD = BigInteger(Constants.Base.SERVER_PRIVATE_KEY, 16)
    val privateKeyParameters = ECPrivateKeyParameters(privateKeyD, domainParameters)

    // 计算公钥（可选，验证时需要）
    val q = domainParameters.g.multiply(privateKeyD).normalize()
    val publicKeyParameters = ECPublicKeyParameters(q, domainParameters)

    // 创建签名器并初始化
    val signer = SM2Signer(PlainDSAEncoding())
    signer.init(true, ParametersWithID(privateKeyParameters, idByteArray))

    // 生成签名
    signer.update(payloadByteArray, 0, payloadByteArray.size)
    val signature = signer.generateSignature()

    // 验证签名（可选）
    signer.init(false, ParametersWithID(publicKeyParameters, idByteArray))
    signer.update(payloadByteArray, 0, payloadByteArray.size)
    val verified = signer.verifySignature(signature)
    println("签名验证结果: $verified")

    return signature
}

fun encryption(encoded: ByteArray, dataByteArray: ByteArray): ByteArray {
    val curveParams = ECNamedCurveTable.getParameterSpec("sm2p256v1")
    val domainParams = ECDomainParameters(
        curveParams.curve,
        curveParams.g,
        curveParams.n,
        curveParams.h,
        curveParams.seed
    )
    val ecPoint = domainParams.curve.decodePoint(encoded)
    val publicKeyParams = ECPublicKeyParameters(ecPoint, domainParams)

    val sm2Engine = SM2Engine()
    sm2Engine.init(true, ParametersWithRandom(publicKeyParams, SecureRandom()))

    return sm2Engine.processBlock(dataByteArray, 0, dataByteArray.size)
}