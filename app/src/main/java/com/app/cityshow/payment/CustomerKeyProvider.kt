package com.app.cityshow.payment

import androidx.annotation.Size
import com.app.cityshow.model.AbstractCallback
import com.stripe.android.EphemeralKeyProvider
import com.stripe.android.EphemeralKeyUpdateListener
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject

class CustomerKeyProvider(private val ephemeralListener: EphemeralListener) : EphemeralKeyProvider {
    override fun createEphemeralKey(
        @Size(min = 4) apiVersion: String,
        keyUpdateListener: EphemeralKeyUpdateListener,
    ) {
        StripeCall.getInstance().createKey(object : AbstractCallback<ResponseBody?>() {
            override fun result(result: ResponseBody?) {
                try {
                    ephemeral = result?.string()
                    keyUpdateListener.onKeyUpdate(ephemeral!!)
                    ephemeralListener.onEphemeralUpdate(true, ephemeral)
                } catch (e: Exception) {
                    e.printStackTrace()
                    try {
                        val jsonObject = ephemeral?.let { JSONObject(it) }
                        val code = jsonObject?.getInt("statusCode")
                        val message = jsonObject?.getJSONObject("raw")?.getString("message")
                        code?.let {
                            message?.let { it1 ->
                                keyUpdateListener.onKeyUpdateFailure(
                                    it,
                                    it1
                                )
                            }
                        }
                        ephemeralListener.onEphemeralUpdate(false, message)
                    } catch (ex: JSONException) {
                        ex.printStackTrace()
                        ephemeralListener.onEphemeralUpdate(false, e.message)
                    }
                }
            }
        })
    }

    interface EphemeralListener {
        fun onEphemeralUpdate(success: Boolean, message: String?)
    }

    companion object {
        private var ephemeral: String? = null
    }
}