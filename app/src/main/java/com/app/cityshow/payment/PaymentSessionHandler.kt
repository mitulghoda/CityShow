package com.app.cityshow.payment

import android.content.Intent
import android.util.Log
import com.app.cityshow.ui.common.ActionBarActivity
import com.app.cityshow.utility.LocalDataHelper
import com.app.cityshow.utility.TextUtil
import com.stripe.android.*
import com.stripe.android.model.*

class PaymentSessionHandler internal constructor(private var activity: ActionBarActivity) :
    PaymentSession.PaymentSessionListener {
    private var destinationStripeAccountId: String? = null
    private var title: String? = null
    var description: String? = null
    private var amount: Long = 0
    private var clientSecret: String? = null
    private var paymentMethodId1: String? = null
    private var purchaseType: String? = TYPE_PACKAGE
    private var packageType: String? = null
    var isPartner = false
    private var currency: String? = null
    private var aPackage: Package? = null
    var paymentSession: PaymentSession? = null
    private var paymentSessionListener: PaymentSessionListener? = null
    private var userInitiated = false
    fun setActivity(activity: ActionBarActivity) {
        this.activity = activity
    }

    fun setCurrency(currency: String) {
        this.currency = currency
    }

    fun setPaymentSessionListener(paymentSessionListener: PaymentSessionListener?) {
        this.paymentSessionListener = paymentSessionListener
    }

    fun destinationStripeAccountId(destination_stripe_account_id: String?) {
        this.destinationStripeAccountId = destination_stripe_account_id
    }

    fun setPurchase_type(purchase_type: String?) {
        this.purchaseType = purchase_type
        destinationStripeAccountId = null
    }

    fun setPackageType(package_type: String?) {
        this.packageType = package_type
    }

    fun setTitle(payment_description: String?) {
        title = payment_description
    }


    fun initTransaction(amount: Long) {
        this.amount = amount
        activity.showProgressDialog()
        paymentSession?.clearPaymentMethod()
        initPaymentSession()
    }

    private fun initPaymentSession() {
        userInitiated = false
        CustomerSession.initCustomerSession(
            activity,
            CustomerKeyProvider(object : CustomerKeyProvider.EphemeralListener {
                override fun onEphemeralUpdate(success: Boolean, message: String?) {
                    activity.hideProgressDialog()
                    if (success) {
                        paymentSession =
                            PaymentSession(activity, StripeUtil.defaultPaymentSessionConfig())
                        paymentSession?.init(this@PaymentSessionHandler)
                        paymentSession?.clearPaymentMethod()
                        paymentSession?.setCartTotal(amount)
                        paymentSession?.presentPaymentMethodSelection("")
                    } else {
                        paymentSessionListener?.onPaymentFailed(message)
                        clearSession()
                    }
                }
            })
        )

    }

    private val stripe: Stripe
        get() = StripeUtil.getStripe(activity, destinationStripeAccountId)
    var isResultFailure = false
    fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (intent == null) return
        if (paymentSession == null) return
        isResultFailure = false
        val isPaymentIntentResult = stripe.onPaymentResult(
            requestCode,
            intent,
            object : ApiResultCallback<PaymentIntentResult> {
                override fun onSuccess(result: PaymentIntentResult) {
                    activity.hideProgressDialog()
                    processStripeIntent(result.intent, true)
                }

                override fun onError(e: Exception) {
                    e.printStackTrace()
                    activity.hideProgressDialog()
                    isResultFailure = true
                    paymentSessionListener!!.onPaymentFailed(e.message)
                }
            })
        if (isPaymentIntentResult && !isResultFailure) {
            activity.showProgressDialog()
        } else {
            val isSetupIntentResult = stripe.onSetupResult(
                requestCode,
                intent,
                object : ApiResultCallback<SetupIntentResult> {
                    override fun onSuccess(result: SetupIntentResult) {
                        activity.hideProgressDialog()
                        processStripeIntent(result.intent, true)
                    }

                    override fun onError(e: Exception) {
                        e.printStackTrace()
                        activity.hideProgressDialog()
                        paymentSessionListener!!.onPaymentFailed(e.message)
                    }
                })
            if (!isSetupIntentResult) {
                paymentSession!!.handlePaymentData(requestCode, resultCode, intent)
            }
        }
    }

    private fun processStripeIntent(stripeIntent: StripeIntent?, isAfterConfirmation: Boolean) {
        if (stripeIntent == null) return
        if (stripeIntent.requiresAction()) {
            stripe.handleNextActionForPayment(
                activity,
                stripeIntent.clientSecret!!
            )
        } else if (stripeIntent.requiresConfirmation()) {
            confirmStripeIntent(stripeIntent.id, destinationStripeAccountId)
        } else if (stripeIntent.status == StripeIntent.Status.Succeeded) {
            if (stripeIntent is PaymentIntent) {
                val (id) = stripeIntent
                activity.hideProgressDialog()
                paymentSessionListener!!.onPaymentSuccess(id, true)
                clearSession()
            } else if (stripeIntent is SetupIntent) {
                val (id) = stripeIntent
                activity.hideProgressDialog()
                paymentSessionListener!!.onPaymentSuccess(id, true)
                clearSession()
            }
        } else if (stripeIntent.status == StripeIntent.Status.RequiresPaymentMethod) {
            if (isAfterConfirmation) {
                paymentSessionListener!!.onPaymentFailed("Payment method not authorised or payment has been declined, Please contact your payment bank for more info.")
                clearSession()
            } else {
                if (paymentMethodId1 == null) return
                if (stripeIntent is PaymentIntent) {
                    activity.hideProgressDialog()
                    stripe.confirmPayment(
                        activity, ConfirmPaymentIntentParams.createWithPaymentMethodId(
                            paymentMethodId1!!, clientSecret!!
                        )
                    )
                } else if (stripeIntent is SetupIntent) {
                    stripe.confirmSetupIntent(
                        activity, ConfirmSetupIntentParams.create(
                            paymentMethodId1!!, clientSecret!!
                        )
                    )
                }
            }
        } else if (stripeIntent.status == StripeIntent.Status.RequiresCapture) {
            paymentSessionListener!!.onPaymentSuccess(stripeIntent.id, false)
            clearSession()
            // We suppose to capture the payment, It will be kept reserved for maximum 7 days..
            // Capture intent will be done in end ride api.
        } else {
            paymentSessionListener!!.onPaymentFailed("Unhandled payment intent, Status - " + stripeIntent.status.toString())
        }
    }

    private fun confirmStripeIntent(payment_intent_id: String?, stripeAccountId: String?) {
        val param = HashMap<String, Any?>()
        param["intent_id"] = payment_intent_id
        param["user_id"] = LocalDataHelper.userId
        if (!TextUtil.isNullOrEmpty(stripeAccountId)) param["stripe_account"] = stripeAccountId
        activity.showProgressDialog()
        /*StripeCall.getInstance().confirmIntent(param, object : AbstractCallback<ResponseBody>() {
            override fun result(result: ResponseBody?) {
                activity.hideProgressDialog()
                try {
                    val response = JSONObject(result.toJson())
                    if (response.has("success")) {
                        val success = response.getBoolean("success")
                        if (success) {
                            paymentSessionListener!!.onPaymentSuccess(payment_intent_id, true)
                        } else {
                            val message = response.getJSONObject("raw").getString("message")
                            paymentSessionListener!!.onPaymentFailed(message)
                        }
                        clearSession()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    //                    paymentSessionListener.onPaymentFailed("Payment failed;");
                    paymentSessionListener!!.onPaymentFailed(e.localizedMessage)
                }
            }
        })*/
    }

    private fun clearSession() {
        if (paymentSession == null) return
        paymentSession?.clearPaymentMethod()
        paymentSession = null
    }

    override fun onCommunicatingStateChanged(isCommunicating: Boolean) {
        Log.e("PaymentSession", "onCommunicatingStateChanged : $isCommunicating")
    }

    override fun onError(errorCode: Int, errorMessage: String) {
        Log.e("PaymentSession", "onError : $errorMessage")
        paymentSessionListener!!.onPaymentFailed(errorMessage)
        clearSession()
    }

    override fun onPaymentSessionDataChanged(data: PaymentSessionData) {
        Log.e("PaymentSession", "onPaymentSessionDataChanged : $data")
        val paymentMethod = data.paymentMethod
        if (paymentMethod?.id == null) return
        if (!data.isPaymentReadyToCharge) return
        if (userInitiated) return
        userInitiated = true
        activity.showProgressDialog()
        paymentMethodId1 = paymentMethod.id
        val param = HashMap<String, Any?>()
        param["amount"] = amount.toString()
        param["customer"] = LocalDataHelper.userId
        param["capture_method"] = "manual"
        param["source"] = paymentMethodId1
        param["purchase_type"] = purchaseType
        param["description"] = description
        param["destination"] = destinationStripeAccountId
        param["statement_descriptor_suffix"] = title
        param["payment_method_types"] = arrayOf("card")
        if (purchaseType != null && purchaseType.equals(TYPE_SUBSCRIPTION, ignoreCase = true)) {
            param.clear()
            param["package_type"] = purchaseType
            param["source"] = paymentMethodId1
            param["is_live"] = true
            /*StripeCall.getInstance()
                .subscriptionPayment(param, object : AbstractCallback<ResponseBody>() {
                    override fun result(result: ResponseBody?) {
                        activity.hideProgressDialog()
                        try {
                            val response = result?.string()
                            var jsonObject = response?.let { JSONObject(it) }

                            paymentMethodId1 = jsonObject?.getString("default_payment_method")
                            if (jsonObject?.has("latest_invoice")!!) jsonObject =
                                jsonObject?.getJSONObject("latest_invoice")
                            if (jsonObject?.has("status")!! && jsonObject?.getString("status")
                                    .equals("paid", ignoreCase = true)
                            ) {
                                paymentSessionListener?.onPaymentSuccess(paymentMethodId1, true)
                            } else {
                                jsonObject = jsonObject?.getJSONObject("payment_intent")
                                if (jsonObject?.has("client_secret")!!) {
                                    clientSecret = jsonObject?.getString("client_secret")
                                    stripe.confirmPayment(
                                        activity,
                                        ConfirmPaymentIntentParams.createWithPaymentMethodId(
                                            paymentMethodId1!!,
                                            clientSecret!!

                                        )
                                    )
                                } else {
                                    val message =
                                        jsonObject?.getJSONObject("raw")?.getString("message")
                                    paymentSessionListener?.onPaymentFailed(message)
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            paymentSessionListener?.onPaymentFailed(e.localizedMessage)
                        }
                    }
                })*/
            return
        }
        createStripePayment(param)
    }

    private fun createStripePayment(param: java.util.HashMap<String, Any?>) {
        /*StripeCall.getInstance().paymentIntent(param, object : AbstractCallback<ResponseBody>() {
            override fun result(result: ResponseBody?) {
                activity.hideProgressDialog()
                try {
                    val response = result?.string()
                    val jsonObject = JSONObject(response!!)
                    if (jsonObject.has("client_secret")) {
                        clientSecret = jsonObject.getString("client_secret")
                        paymentMethodId1 = jsonObject.getString("payment_method")
                        stripe.confirmPayment(
                            activity,
                            ConfirmPaymentIntentParams.createWithPaymentMethodId(
                                paymentMethodId1!!,
                                clientSecret!!

                            )
                        )
                    } else {
                        val message = jsonObject.getJSONObject("raw").getString("message")
                        paymentSessionListener?.onPaymentFailed(message)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })*/
    }


    private fun retrieveStripeIntent(clientSecret: String): StripeIntent? {
        var stripeIntent: StripeIntent? = null
        try {
            if (clientSecret.startsWith("pi_")) {
                stripeIntent = stripe.retrievePaymentIntentSynchronous(clientSecret)
            } else if (clientSecret.startsWith("seti_")) {
                stripeIntent = stripe.retrieveSetupIntentSynchronous(clientSecret)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return stripeIntent
    }

    private fun finishPayment() {
        paymentSession?.onCompleted()
        paymentSession?.clearPaymentMethod()
        paymentSession = null
    }

    private fun finishSetup() {
        paymentSession?.onCompleted()
        paymentSession?.clearPaymentMethod()
        paymentSession = null
    }

    interface PaymentSessionListener {
        fun onPaymentSuccess(payment_intent_id: String?, captured: Boolean)
        fun onPaymentFailed(message: String?)
    }

    companion object {
        const val TYPE_PACKAGE = "package"
        const val TYPE_SUBSCRIPTION = "subscription"
        const val TYPE_EXTRA = "extra_payment"
        const val PACKAGE_RENT = "rent"
        const val PACKAGE_LEASE = "lease"
    }
}