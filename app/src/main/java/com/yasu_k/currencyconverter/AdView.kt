package com.yasu_k.currencyconverter

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.*

class AdBanner constructor(context: Context, activity: Activity){ //It might be better to convert this object to class so as to pass "MainActivity context"

    companion object{

    }
    private var mContext: Context
    private var mActivity: Activity

    init {
        mContext = context
        mActivity = activity
    }

    /*val mAdView: AdView = mActivity.findViewById(R.id.adView)
    MobileAds.initialize(mContext){}
    val adRequest = AdRequest.Builder().build()
    mAdView.loadAd(adRequest)
    mAdView.adListener = object : AdListener() {
        override fun onAdLoaded() {
            Log.d("AdMob", "Ad loaded")
        }

        override fun onAdFailedToLoad(errorCode: LoadAdError?) {
            Log.d("AdMob", "Failed to load the ad: $errorCode")
        }
    }*/
}