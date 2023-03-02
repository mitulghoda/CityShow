package com.app.cityshow.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.app.cityshow.R
import com.app.cityshow.databinding.HomeBinding
import com.app.cityshow.payment.PaymentSessionHandler
import com.app.cityshow.payment.StripeUtil
import com.app.cityshow.ui.adapter.PageStateAdapter
import com.app.cityshow.ui.common.BaseFragment
import com.app.cityshow.ui.fragment.HomeFragment
import com.app.cityshow.ui.common.ActionBarActivity
import com.app.cityshow.ui.fragment.FavFragment
import com.app.cityshow.ui.fragment.NotificationFragment
import com.app.cityshow.ui.fragment.ProfileFragment
import com.google.android.gms.maps.model.LatLng
import com.qamar.curvedbottomnaviagtion.CurvedBottomNavigation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList

class HomeActivity : ActionBarActivity(), View.OnClickListener {
    private lateinit var mBinding: HomeBinding
    private var pageStateAdapter: PageStateAdapter<BaseFragment>? = null
    override fun initUi() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = HomeBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mBinding.clickListener = this
        setUpToolbar("Home", false)
        setupViewPagerFragment()
        setUpCurveBottomNavigation()
    }

    private fun getFragments(): MutableList<BaseFragment?> {
        val fragments: MutableList<BaseFragment?> = ArrayList()
        fragments.add(HomeFragment())
        fragments.add(FavFragment())
        fragments.add(NotificationFragment())
        fragments.add(ProfileFragment())
        return fragments
    }

    private fun setupViewPagerFragment() {
        if (pageStateAdapter != null) return
        val fragments = getFragments()
        /*mBinding.viewPager.adapter =
            PageStateAdapter(this, fragments).also { pageStateAdapter = it }
        mBinding.viewPager.isUserInputEnabled = false
        mBinding.viewPager.offscreenPageLimit = 1*/
        fragments[0]?.let { fragment ->
            loadFragmentWithClearedStack(
                fragment,
                fragment.javaClass.simpleName,
                R.id.fragment_container
            )
        }
        mBinding.bottomView.setOnItemSelectedListener { it ->
            when (it.itemId) {
                R.id.action_home -> {
                    showFab(true)
                    setUpToolbar("Home", false)
                    //mBinding.viewPager.currentItem = 0
                    fragments[0]?.let { fragment ->
                        loadFragmentWithClearedStack(
                            fragment,
                            fragment.javaClass.simpleName,
                            R.id.fragment_container
                        )
                    }
                }
                R.id.action_fav -> {
                    showFab(true)
                    setUpToolbar("Wishlist", false)
                    //mBinding.viewPager.currentItem = 1
                    fragments[1]?.let { fragment ->
                        loadFragmentWithClearedStack(
                            fragment,
                            fragment.javaClass.simpleName,
                            R.id.fragment_container
                        )
                    }
                }
                R.id.action_not -> {
                    showFab(true)
                    setUpToolbar("Notifications", false)
                    //mBinding.viewPager.currentItem = 2
                    fragments[2]?.let { fragment ->
                        loadFragmentWithClearedStack(
                            fragment,
                            fragment.javaClass.simpleName,
                            R.id.fragment_container
                        )
                    }
                }
                R.id.action_account -> {
                    showFab(false)
                    setUpToolbar("Account", false)
                    //mBinding.viewPager.currentItem = 3
                    fragments[3]?.let { fragment ->
                        loadFragmentWithClearedStack(
                            fragment,
                            fragment.javaClass.simpleName,
                            R.id.fragment_container
                        )
                    }
                }
            }
            true
        }
    }

    private fun showFab(isShow: Boolean) {
        mBinding.fab.visibility = View.VISIBLE.takeIf { isShow } ?: View.GONE

    }

    override fun onClick(v: View?) {
        when (v) {
            mBinding.fab -> {
                openAddProductActivity()
            }

        }
    }

    private fun setUpCurveBottomNavigation() {
        val fragments = getFragments()
        val bottomNavigationItems = mutableListOf(
            CurvedBottomNavigation.Model(1, "", R.drawable.ic_home),
            CurvedBottomNavigation.Model(
                2,
                "",
                R.drawable.ic_fav
            ),
            CurvedBottomNavigation.Model(
                3,
                "",
                R.drawable.ic_notification
            ),
            CurvedBottomNavigation.Model(4, "", R.drawable.ic_baseline_account_circle_24),
        )
        mBinding.bottomNavigationCurve.apply {
            bottomNavigationItems.forEach { add(it) }
            setOnClickMenuListener {
                when (it.id) {
                    1 -> {
                        setUpToolbar("Home", false)
                        //mBinding.viewPager.currentItem = 0
                        fragments[0]?.let { fragment ->
                            loadFragmentWithClearedStack(
                                fragment,
                                fragment.javaClass.simpleName,
                                R.id.fragment_container
                            )
                        }
                    }
                    2 -> {
                        setUpToolbar("Wishlist", false)
                        //mBinding.viewPager.currentItem = 1
                        fragments[1]?.let { fragment ->
                            loadFragmentWithClearedStack(
                                fragment,
                                fragment.javaClass.simpleName,
                                R.id.fragment_container
                            )
                        }
                    }
                    3 -> {
                        setUpToolbar("Notifications", false)
                        //mBinding.viewPager.currentItem = 2
                        fragments[2]?.let { fragment ->
                            loadFragmentWithClearedStack(
                                fragment,
                                fragment.javaClass.simpleName,
                                R.id.fragment_container
                            )
                        }
                    }
                    4 -> {
                        setUpToolbar("Account", false)
                        //mBinding.viewPager.currentItem = 3
                        fragments[3]?.let { fragment ->
                            loadFragmentWithClearedStack(
                                fragment,
                                fragment.javaClass.simpleName,
                                R.id.fragment_container
                            )
                        }
                    }
                }
            }
        }
        mBinding.bottomNavigationCurve.show(1, true)
    }

    override fun onFilterClick() {
        super.onFilterClick()

    }
    private fun selectPayment() {
        val paymentSessionHandler = StripeUtil.getPaymentSessionHandler(this)
        paymentSessionHandler?.setPurchase_type(PaymentSessionHandler.TYPE_PACKAGE)
        paymentSessionHandler?.destinationStripeAccountId("lock.stripeAccountId")
        paymentSessionHandler?.initTransaction(100)
        paymentSessionHandler?.setPaymentSessionListener(object :
            PaymentSessionHandler.PaymentSessionListener {
            override fun onPaymentSuccess(payment_intent_id: String?, captured: Boolean) {
                Log.e(
                    "PaymentSessionHandler",
                    "Success : $payment_intent_id Captured - $captured"
                )
                if (payment_intent_id == null) {
                    return
                }
             //Call api for purchase subscription
            }

            override fun onPaymentFailed(message: String?) {
                Log.e("PaymentSessionHandler", message?:"")
                hideProgressDialog()
            }
        })
    }
}