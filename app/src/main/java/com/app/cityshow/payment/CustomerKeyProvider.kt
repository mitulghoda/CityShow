package com.app.cityshow.payment

import androidx.annotation.Size
import com.stripe.android.EphemeralKeyProvider
import com.stripe.android.EphemeralKeyUpdateListener

class CustomerKeyProvider(private val ephemeralListener: EphemeralListener) : EphemeralKeyProvider {
    override fun createEphemeralKey(
        @Size(min = 4) apiVersion: String,
        keyUpdateListener: EphemeralKeyUpdateListener
    ) {
        val param = HashMap<String, String>()
        param["api_version"] = apiVersion
        val stripeId = ""
        param["customer_id"] = stripeId
        val id = ""
        param["user_id"] = id
//        StripeCall.getInstance().createKey(param, object : AbstractCallback<ResponseBody?>() {
//            override fun result(result: ResponseBody?) {
//                try {
//                    ephemeral = result?.string()
//                    keyUpdateListener.onKeyUpdate(ephemeral!!)
//                    ephemeralListener.onEphemeralUpdate(true, ephemeral)
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    try {
//                        val jsonObject = ephemeral?.let { JSONObject(it) }
//                        val code = jsonObject?.getInt("statusCode")
//                        val message = jsonObject?.getJSONObject("raw")?.getString("message")
//                        code?.let { message?.let { it1 ->
//                            keyUpdateListener.onKeyUpdateFailure(it,
//                                it1
//                            )
//                        } }
//                        ephemeralListener.onEphemeralUpdate(false, message)
//                    } catch (ex: JSONException) {
//                        ex.printStackTrace()
//                        ephemeralListener.onEphemeralUpdate(false, e.message)
//                    }
//                }
//            }
//        })
    }

    interface EphemeralListener {
        fun onEphemeralUpdate(success: Boolean, message: String?)
    }

    companion object {
        private var ephemeral: String? = null
    }
}