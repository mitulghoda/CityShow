package com.app.cityshow.payment

import android.content.Intent
import android.util.Log
import com.app.cityshow.model.AbstractCallback
import com.app.cityshow.model.stripe.GeneralModel
import com.app.cityshow.model.subscription.Plan
import com.app.cityshow.ui.common.ActionBarActivity
import com.app.cityshow.utility.toJson
import com.google.gson.Gson
import com.stripe.android.*
import com.stripe.android.model.*
import okhttp3.ResponseBody

class PaymentSessionHandler internal constructor(private var activity: ActionBarActivity) :
    PaymentSession.PaymentSessionListener {
    private var destinationStripeAccountId: String? = null
    private var title: String? = null
    private var plan: Plan? = null
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


    fun setPaymentSessionListener(paymentSessionListener: PaymentSessionListener?) {
        this.paymentSessionListener = paymentSessionListener
    }


    fun setPackageType(package_type: String?) {
        this.packageType = package_type
    }

    fun setTitle(payment_description: String?) {
        title = payment_description
    }

    fun setPlan(tempPlan: Plan?) {
        plan = tempPlan
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
        } else if (stripeIntent.status == StripeIntent.Status.Succeeded) {
            if (stripeIntent is PaymentIntent) {
                val (id) = stripeIntent
                activity.hideProgressDialog()
                paymentSessionListener?.onPaymentSuccess(id, true)
                clearSession()
            } else if (stripeIntent is SetupIntent) {
                val (id) = stripeIntent
                activity.hideProgressDialog()
                paymentSessionListener?.onPaymentSuccess(id, true)
                clearSession()
            }
        } else if (stripeIntent.status == StripeIntent.Status.RequiresPaymentMethod) {
            if (isAfterConfirmation) {
                paymentSessionListener?.onPaymentFailed("Payment method not authorised or payment has been declined, Please contact your payment bank for more info.")
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
            paymentSessionListener?.onPaymentSuccess(stripeIntent.id, false)
            clearSession()
            // We suppose to capture the payment, It will be kept reserved for maximum 7 days..
            // Capture intent will be done in end ride api.
        } else {
            paymentSessionListener?.onPaymentFailed("Unhandled payment intent, Status - " + stripeIntent.status.toString())
        }
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
        paymentSessionListener?.onPaymentFailed(errorMessage)
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
        val params = HashMap<String, Any>()
        params["plan_id"] = plan?.id ?: ""
        params["price_id"] = plan?.default_price ?: ""
        params["card_id"] = paymentMethodId1 ?: ""
        StripeCall.getInstance()
            .subscribeUser(params, object : AbstractCallback<ResponseBody>() {
                override fun result(result: ResponseBody?) {
                    activity.hideProgressDialog()
                    try {
                        val response = result?.string()
                        val generalModel = Gson().fromJson(response, GeneralModel::class.java)
                        Log.e("USER_SUBSCRIBE", generalModel.toJson())
                        if (generalModel.success) {
                            paymentSessionListener?.onPaymentSuccess(paymentMethodId1, true)
                            activity.toast(generalModel.message)
                        } else {
                            paymentSessionListener?.onPaymentFailed(generalModel.message)
                        }
                        finishPayment()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        paymentSessionListener?.onPaymentFailed(e.localizedMessage)
                        finishPayment()
                    }
                }
            })
        return
    }


    private fun finishPayment() {
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
    }
}