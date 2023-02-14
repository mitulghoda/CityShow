package com.app.cityshow

import android.app.Activity
import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.app.cityshow.utility.ActivityLifeCycle
import com.app.cityshow.utility.RegionManager
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient

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

