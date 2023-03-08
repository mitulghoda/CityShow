package com.app.cityshow.ui.common

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_SECURE
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.app.cityshow.R
import com.app.cityshow.network.NetworkURL.ACTION_FOR_BIDDEN_RESPONSE
import com.app.cityshow.network.NetworkURL.ACTION_FOR_BLOCKED
import com.app.cityshow.network.NetworkURL.ACTION_FOR_INACTIVE_USER
import com.app.cityshow.ui.activity.LoginActivity
import com.app.cityshow.utility.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import com.kaopiz.kprogresshud.KProgressHUD
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.*


abstract class BaseActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    var city: String? = null
    protected val tag: String = this::class.java.simpleName
    protected lateinit var activityLauncher: BetterActivityResult<Intent, ActivityResult>
    var portraitOrientation: Int = Configuration.ORIENTATION_PORTRAIT
    var landscapeOrientation: Int = Configuration.ORIENTATION_LANDSCAPE

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var mLastLocation: Location? = null
    var longitude: Double = 0.0
    var lattitude: Double = 0.0

    abstract fun initUi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLauncher = BetterActivityResult.registerActivityForResult(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        requestPermission()
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        initUi()
    }

    open fun isTablet(): Boolean {
        val xlarge =
            resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK == 4
        val large =
            resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK == Configuration.SCREENLAYOUT_SIZE_LARGE
        return xlarge || large
    }

    fun navigateMap(
        lon: Double = 0.0,
        lat: Double = 0.0,
    ) {
        val navigation = Uri.parse("google.navigation:q=$lon,$lat")
        val navigationIntent = Intent(Intent.ACTION_VIEW, navigation)
        navigationIntent.setPackage("com.google.android.apps.maps")
        startActivity(navigationIntent)
    }

     fun requestPermission() {
        val perms = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                android.Manifest.permission.POST_NOTIFICATIONS,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
            )
        } else {
            arrayOf(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
            )
        }
        if (!EasyPermissions.hasPermissions(this, *perms)) {
            // Ask for one permission
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.rationale_permission),
                RC_AUDIO,
                *perms
            )
        } else {
            getLastLocation()
        }
    }

    private val locationPerms =
        arrayOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        )

     fun requestLocationPermission() {
         if (!EasyPermissions.hasPermissions(this, *locationPerms)) {
             // Ask for one permission
             EasyPermissions.requestPermissions(
                 this,
                 getString(R.string.location_rationale_permission),
                 LOCATION,
                 *locationPerms
             )
         } else {
             getLastLocation()
        }
    }

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(LOCATION)
    private fun getLastLocation() {
        fusedLocationProviderClient?.lastLocation?.addOnCompleteListener(this) { task ->
            if (task.isSuccessful && task.result != null) {
                mLastLocation = task.result
                Log.e("LATTITUDE-->", (mLastLocation)!!.latitude.toString())
                Log.e("LONGITUDE-->", (mLastLocation)!!.longitude.toString())
                lattitude = (mLastLocation)!!.latitude
                longitude = (mLastLocation)!!.longitude

            } else {
                toast("Enable to find your current location.")
                Log.w(
                    "LAST_LOCATION--",
                    "getLastLocation:exception${task.exception?.localizedMessage}"
                )
            }
        }
    }

    fun hideKeyBoard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }

    fun setSecureActivity() = window.setFlags(FLAG_SECURE, FLAG_SECURE)
    fun keepScreenOn() = window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

    private var alertDialog: AlertDialog? = null
    fun getAddress(latLng: LatLng): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        val address: Address?
        var fulladdress = ""
        val addresses: List<Address>? =
            geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

        if (!addresses.isNullOrEmpty()) {
            address = addresses[0]
            fulladdress =
                address.getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex
            city = address.locality
            val state = address.adminArea
            val country = address.countryName
            var postalCode = address.postalCode
            var knownName = address.featureName // Only if available else return NULL
            Log.e("CURRENT_ADDRESS-->", fulladdress)
            return "You are at $city,$state"
        }
        return ""
    }

    open fun showAlertMessage(
        title: String? = "",
        strMessage: String = "",
        isCancelable: Boolean = false,
        positiveText: String = "",
        negativeText: String = "",
        callback: ((isPositive: Boolean) -> Unit)? = null,
    ): AlertDialog? {
        try {
            if (alertDialog != null && alertDialog!!.isShowing) {
                alertDialog!!.dismiss()
            }
            val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogCustom))
                .setMessage(strMessage)
                .setCancelable(isCancelable)
                .setPositiveButton(positiveText.takeIf { positiveText.isNotBlank() }
                    ?: getString(R.string.ok)) { _, _ -> callback?.invoke(true) }
                .setNegativeButton(negativeText.takeIf { negativeText.isNotBlank() }
                    ?: getString(R.string.cancel)) { _, _ -> callback?.invoke(false) }

            if (!title.isNullOrBlank()) builder.setTitle(getString(R.string.app_name))

            alertDialog = builder.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return alertDialog
    }

    private var snackbar: Snackbar? = null
    fun showSnackBar(view: View?, str: String?) {
        if (view == null) return
        if (str.isNullOrEmpty()) return
        if (snackbar != null && snackbar!!.isShownOrQueued) {
            snackbar?.dismiss()
        }
        snackbar = Snackbar.make(view, str, Snackbar.LENGTH_SHORT)
        snackbar?.show()
    }

    fun showSnackBar(str: String?) {
        val view: View = this.window.decorView
        if (str.isNullOrEmpty()) return
        if (snackbar != null) snackbar?.dismiss()
        snackbar = Snackbar.make(view, str, Snackbar.LENGTH_SHORT)
        snackbar?.show()
    }

    fun toast(str: String?) {
        if (str.isNullOrEmpty()) return
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }

    fun loadFragmentWithClearedStack(
        fragment: Fragment,
        tag: String,
        fragmentContainer: Int,
    ) {
        justTry {
            if (supportFragmentManager.fragments.isNotEmpty())
                supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

            val currentFragment = supportFragmentManager.findFragmentById(fragmentContainer)
//        if (currentFragment?.javaClass?.simpleName == fragment.javaClass.simpleName) return
            supportFragmentManager.beginTransaction()
                .replace(fragmentContainer, fragment, tag)
                .commit()
        }
    }

    fun clearFragment(fragName: String) {
        supportFragmentManager.popBackStack(fragName, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    fun loadFragment(fragment: Fragment, tag: String, backstack: String, fragmentContainer: Int) {
        val currentFragment = supportFragmentManager.findFragmentById(fragmentContainer)
        if (currentFragment?.javaClass?.simpleName != fragment.javaClass.simpleName) {
//            fragment.apply {
//                setTargetFragment(
//                    currentFragment,
//                    Constants.FRAGMENT_ADD_CODE
//                )
//            }
            supportFragmentManager.beginTransaction()
                .replace(fragmentContainer, fragment, tag)
                .addToBackStack(backstack).commit()

        }

    }

    fun openDatePickerDialog(
        strTitle: String,
        selectedDate: Calendar,
        onDateSet: DatePickerDialog.OnDateSetListener,
        isFuture: Boolean,
        isPast: Boolean,
    ) {
        val calendar: Calendar = selectedDate
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        val datePickerDialog =
            DatePickerDialog(this, R.style.DatePickerTheme, onDateSet, year, month, day)
        if (isFuture) datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        if (isPast) datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.setTitle(strTitle)
        datePickerDialog.datePicker.firstDayOfWeek = Calendar.MONDAY
        datePickerDialog.show()
    }

    fun openDatePickerDialog(
        strTitle: String,
        onDateSet: DatePickerDialog.OnDateSetListener,
        isFuture: Boolean,
        isPast: Boolean,
    ) {
        val calendar: Calendar = Calendar.getInstance()
        openDatePickerDialog(strTitle, calendar, onDateSet, isFuture, isPast)
    }

    override fun onBackPressed() {
        if (KeyboardVisibilityEvent.isKeyboardVisible(this)) {
            KeyboardUtil.hideKeyboard(this)
            return
        }
        super.onBackPressed()
    }

    private var dialog: KProgressHUD? = null
    fun showProgressDialog() {
        showProgressDialog(getString(R.string.please_wait))
    }

    private fun showProgressDialog(msg: String?) {
        if (dialog == null) {
            dialog = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.please_wait).takeIf { msg.isNullOrEmpty() } ?: msg)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
        }
        dialog!!.show()
    }

    fun hideProgressDialog() {
        justTry { if (dialog != null) dialog!!.dismiss() }
    }

    open fun getFcmToken(callBack: (fcmToken: String, isSuccess: Boolean) -> Unit) {
        val fcmToken = LocalDataHelper.fcmToken
        if (fcmToken.isNullOrEmpty()) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    callBack.invoke(
                        task.exception?.localizedMessage
                            ?: "Fetching FCM registration token failed", false
                    )
                    return@OnCompleteListener
                }
                val token = task.result
                if (token.isNullOrEmpty()) {
                    callBack.invoke(
                        task.exception?.localizedMessage
                            ?: "Fetching FCM registration token failed", false
                    )
                    return@OnCompleteListener
                }
                LocalDataHelper.fcmToken = token
                callBack.invoke(token, true)
            })
        } else {
            callBack.invoke(fcmToken, true)
        }
    }

    fun logoutActions() {
        LocalDataHelper.authToken = ""
        LocalDataHelper.user = null
        LocalDataHelper.login = false
        openLoginActivity()
    }

    fun openLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    override fun onResume() {
        super.onResume()
        val userFilter = IntentFilter()
        userFilter.addAction(ACTION_FOR_BIDDEN_RESPONSE)
        userFilter.addAction(ACTION_FOR_BLOCKED)
        userFilter.addAction(ACTION_FOR_INACTIVE_USER)
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(unauthorizedReceiver, userFilter)

    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(unauthorizedReceiver)
    }

    private val unauthorizedReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action ?: return
            if (action.equals(ACTION_FOR_BIDDEN_RESPONSE, ignoreCase = true)) {
                showAlertMessage(
                    title = null,
                    strMessage = "Unauthorized Access.. Please login to continue",
                    isCancelable = false,
                    positiveText = "OK"
                ) { _ ->
                    logoutActions()
                    openLoginActivity()
                }
            } else if (action.equals(ACTION_FOR_BLOCKED, ignoreCase = true)) {
                showAlertMessage(
                    title = null,
                    strMessage = "Your account access is blocked by administrator.",
                    isCancelable = false,
                    positiveText = "OK"
                ) { _ ->
                    logoutActions()
                    openLoginActivity()
                }
            } else if (action.equals(ACTION_FOR_INACTIVE_USER, ignoreCase = true)) {
                showAlertMessage(
                    title = null,
                    strMessage = "Your account is inactive, Please contact to administrator.",
                    isCancelable = false,
                    positiveText = "OK"
                ) { _ ->
                    logoutActions()
                    openLoginActivity()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Log.d(
            "PERMISSION_DENIED",
            "onPermissionsDenied:" + requestCode.toString() + ":" + perms.size
        )
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        getLastLocation()
        setAddress()
    }

    open fun setAddress() {}

    companion object {
        private val WRITE_STORAGE = 123
        private val READ = 124
        private val RC_AUDIO = 125
        private const val LOCATION = 100124
    }
}