package io.github.cotrin8672.ktor.plugin.io.github.cotrin8672

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

val LineWebhook = createRouteScopedPlugin(
    name = "LineWebhookSignatureValidationPlugin",
    createConfiguration = ::Configuration
) {
    suspend fun ApplicationCall.verifySignature(channelSecret: String): Boolean {
        val signatureFromHeader = request.header(LineSignature) ?: return false
        val key = SecretKeySpec(channelSecret.toByteArray(), "HmacSHA256")
        val mac = Mac.getInstance("HmacSHA256").apply {
            init(key)
        }
        val source = receiveText().toByteArray(Charsets.UTF_8)
        val calculatedSignature = Base64.getEncoder().encodeToString(mac.doFinal(source))
        return signatureFromHeader == calculatedSignature
    }

    pluginConfig.apply {
        onCall { call ->
            if (!call.verifySignature(channelSecret)) {
                call.respond(HttpStatusCode.Forbidden)
                return@onCall
            }
        }
    }
}

class Configuration {
    lateinit var channelSecret: String
}

const val LineSignature = "X-Line-Signature"
