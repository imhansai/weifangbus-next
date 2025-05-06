package dev.fromnowon

import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security

object SecurityInitializer {
    init {
        // https://www.bouncycastle.org/download/bouncy-castle-java/
        // https://github.com/bcgit/bc-kotlin
        Security.addProvider(BouncyCastleProvider())
    }
}