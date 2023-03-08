package com.app.cityshow

import android.app.Activity
import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.app.cityshow.utility.ActivityLifeCycle
import com.app.cityshow.utility.Log
import com.app.cityshow.utility.RegionManager
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.stripe.android.PaymentAuthConfig
import com.stripe.android.PaymentConfiguration

class Controller : Application(), LifecycleObserver {
    private var activityLifeCycle = ActivityLifeCycle()

    override fun onCreate() {
        super.onCreate()
        instance = this
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        registerActivityLifecycleCallbacks(activityLifeCycle)
        RegionManager.init(this)
        if (!Places.isInitialized()) Places.initialize(
            applicationContext,
            getString(R.string.google_maps_key)
        )
        placesClient = Places.createClient(this)
        try {
            PaymentConfiguration.init(
                this,
                "pk_test_51KjgLRSERxmiFRLSCRCoevregNqNp5wcqlZTgTKEYnzA0qPegnfwyx1H6FRcHk2MLKJ2DMVsUNWVDdUUmOrQr9y800xcQFg6Ne"
            )
            val uiCustomization = PaymentAuthConfig.Stripe3ds2UiCustomization.Builder()
                .setLabelCustomization(
                    PaymentAuthConfig.Stripe3ds2LabelCustomization.Builder()
                        .setTextFontSize(12)
                        .build()
                )
                .build()
            PaymentAuthConfig.init(
                PaymentAuthConfig.Builder()
                    .set3ds2Config(
                        PaymentAuthConfig.Stripe3ds2Config.Builder()
                            .setTimeout(10)
                            .setUiCustomization(uiCustomization)
                            .build()
                    )
                    .build()
            )
        } catch (ex: Exception) {
            Log.e("StripeInitException", ex.message ?: "")
        }

    }

    fun getActivityLifeCycle(): ActivityLifeCycle {
        return activityLifeCycle
    }

    fun getCurrentActivity(): Activity? {
        return activityLifeCycle.currentActivity
    }

    companion object {
        lateinit var instance: Controller
        lateinit var placesClient: PlacesClient
    }
}

