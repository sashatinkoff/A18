package com.isidroid.a18

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.isidroid.a18.extensions.alert
import com.isidroid.utils.extensions.md5
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.security.MessageDigest
import java.util.*
import kotlin.random.Random


private const val REQUEST_CODE_REVO = 192

class MainActivity : AppCompatActivity() {
    private val gson = GsonBuilder().create()
    private var alert: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createForm()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val response = data?.getStringExtra("json_data")
        val message =
            if (resultCode == RESULT_OK) "все прошло удачно" else "произошла какая-то ошибка"

        debug("onActivityResult response=$response\n isOk=${resultCode == RESULT_OK}")

        if (alert == null) alert(
            title = "Получен результат от Revo",
            message = "$message\n$response",
            positiveRes = android.R.string.ok
        )
    }

    private fun createForm() {
        btnError.setOnClickListener { sendRevo(withError = true) }
        btnFillFormStage.setOnClickListener { fillStageForm() }
        btnSubmit.setOnClickListener { sendRevo() }
    }

    private fun fillStageForm() {
        fillForm(
            storeId = 309,
            phone = "78884112555",
            clientPhone = "78881211111"
        )
    }


    @SuppressLint("SetTextI18n")
    private fun fillForm(
        storeId: Int = 379,
        phone: String = "99912${Random.nextInt(100_000, 999_999)}",
        merchantId: String = UUID.randomUUID().toString().substring(0, 15),
        orderId: Int = Random.nextInt(100_000, 1_000_000),
        amount: Float = Random.nextInt(1_000, 10_000).toFloat(),
        clientPhone: String = "99912${Random.nextInt(100_000, 999_999)}"
    ) {

        inputStoreId.setText("$storeId")
        inputAgentPhone.setText(phone)
        inputMerchantAgentId.setText("$merchantId@fake.lamoda2revo")
        inputOrderId.setText("$orderId")
        inputAmount.setText("$amount")
        inputPhone.setText(clientPhone)
    }


    private fun sendRevo(withError: Boolean = false) {
        val secretKey = if (withError) UUID.randomUUID().toString().hash()
        else "0aba16fd742571e091a2fb6da161ae4b"


        val request = Request(
            credentials = RequestCredentials(
                storeId = inputStoreId.text.toString().toIntOrNull() ?: 1
            ),
            payload = RequestPayload(
                agentPhone = inputAgentPhone.text.toString(),
                merchantAgentId = inputMerchantAgentId.text.toString(),
                orderId = inputOrderId.text.toString(),
                amount = inputAmount.text.toString(),
                phone = inputPhone.text.toString()
            )
        )

        val map = toMap(request.payload)
        val signature = map.keys.sorted().map { map[it] }.joinToString("") + secretKey
        request.credentials.signature = signature.hash()

        val json = gson.toJson(request)
        debug(json)

        val intent = Intent("ru.revoplus.client").putExtra("json_data", json)
        try {
            startActivityForResult(intent, REQUEST_CODE_REVO)
        } catch (e: Throwable) {
            val message = when (e) {
                is ActivityNotFoundException -> "Не найдено приложений, которые могут открыть intent"
                else -> "${e.message}"
            }

            alert = alert(
                title = "Ошибка",
                message = message,
                positiveRes = android.R.string.ok
            )
        }
    }

    private fun String.hash(): String {
        val md = MessageDigest.getInstance("SHA-1")
        val digest = md.digest(toByteArray())
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }

    private fun toMap(item: Any): Map<String, String> = with(gson.toJson(item)) {
        gson.fromJson(this, object : TypeToken<Map<String, String>>() {}.type)
    }

    private fun debug(msg: String) {
        Timber.tag("lamoda").i(msg)
    }

    data class Request(
        @SerializedName("credentials") val credentials: RequestCredentials,
        @SerializedName("payload") val payload: RequestPayload
    )

    data class RequestCredentials(
        @SerializedName("store_id") val storeId: Int,
        @SerializedName("auth_type") val authType: String = "sha-1-sorted",
        @SerializedName("signature") var signature: String = ""
    )

    data class RequestPayload(
        @SerializedName("agent_phone") val agentPhone: String,
        @SerializedName("merchant_agent_id") val merchantAgentId: String,
        @SerializedName("order_id") val orderId: String,
        @SerializedName("amount") val amount: String,
        @SerializedName("phone") val phone: String
    )
}