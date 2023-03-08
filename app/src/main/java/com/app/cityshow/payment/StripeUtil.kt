package com.app.cityshow.payment

import android.app.Activity
import android.util.Log
import com.app.cityshow.ui.common.ActionBarActivity
import com.app.cityshow.utility.LocalDataHelper
import com.app.cityshow.utility.TextUtil
import com.stripe.android.PaymentSessionConfig
import com.stripe.android.Stripe
import com.stripe.android.googlepaylauncher.GooglePayPaymentMethodLauncher
import com.stripe.android.model.ShippingInformation

object StripeUtil {
    //  public static final String STRIPE_PUBLISHER_KEY = "pk_live_QsumyGsV286dzZWMD7RLvGSw";
    const val STRIPE_PUBLISHER_KEY = "pk_test_qhlpDXqdhSuAVDStY3AfkjVI"
    var paymentSessionHandler: PaymentSessionHandler? = null

    fun getPaymentSessionHandler(activity: ActionBarActivity?): PaymentSessionHandler? {
        if (paymentSessionHandler == null)
            paymentSessionHandler = PaymentSessionHandler(activity!!)
        paymentSessionHandler?.setActivity(activity!!)
        return paymentSessionHandler
    }

    fun defaultPaymentSessionConfig(): PaymentSessionConfig {
        val user = LocalDataHelper.user
        return PaymentSessionConfig.Builder()
            .setShippingInfoRequired(false)
            .setShippingMethodsRequired(false)
            .setShouldPrefetchCustomer(true)
            .setPrepopulatedShippingInfo(
                ShippingInformation(
                    null,
                    user?.firstName,
                    user?.phoneNumber
                )
            )
            .build()
    }

    fun getStripe(activity: Activity?, stripeAccountKey: String?): Stripe {
        Log.e("STRIPE_PUBLISH_KEY", "stripePublishKey")
        return if (TextUtil.isNullOrEmpty(stripeAccountKey)) {
            Stripe(activity!!, "stripePublishKey")
        } else {
            Stripe(activity!!, "stripePublishKey", stripeAccountKey)
        }
    }

    private const val COLOR_ACCENT = "#212121"
    private const val COLOR_PRIMARY = "#89BE26"
    private const val COLOR_PRIMARY_DARK = "#89BE26"
    private const val COLOR_WHITE = "#FFFFFF"
    private const val FONT_REGULAR = "proxima_nova_regular"
    private const val FONT_SEMI_BOLD = "proxima_nova_semi_bold"
    private const val FONT_BOLD = "proxima_nova_bold"
}