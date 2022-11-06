package com.droidot.adlibrary


/********************************************
 * Created by droidot publisher on 11/6/2022 *
 ********************************************/


import android.app.Activity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView

/********************************************
 * Created by droidot publisher on 4/16/2022 *
 ********************************************/

object Admob {
    private var nativeAd: NativeAd? = null
    var adViewAdmob: AdView? = null
    var mInterstitialAd: InterstitialAd? = null
    private var counter = 0

    fun nativeReg(
        activity: Activity,
        layNative: FrameLayout,
        nativeId: String
    ) {
        val builder = AdLoader.Builder(activity, nativeId)
        builder.forNativeAd { nativeAds: NativeAd ->
            if (nativeAd != null) {
                nativeAd!!.destroy()
            }

            nativeAd = nativeAds
            val adView = activity.layoutInflater
                .inflate(R.layout.ad_native_ads_medium, null) as NativeAdView
            populateNativeAdView(nativeAds, adView)
            layNative.removeAllViews()
            layNative.addView(adView)
        }
        val videoOptions = VideoOptions.Builder()
            .build()

        val adOptions = NativeAdOptions.Builder()
            .setVideoOptions(videoOptions)
            .build()

        builder.withNativeAdOptions(adOptions)
        val adLoader = builder.withAdListener(
            object : AdListener() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {}
                override fun onAdClicked() {}
            })
            .build()
        adLoader.loadAd(AdRequest.Builder().build())
      //  iShowLog("-->onStart: Native reg banner show")
    }


    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {

        // Set other ad assets.
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_app_icon)

        (adView.headlineView as TextView).text = nativeAd.headline

        if (nativeAd.body == null) {
            adView.bodyView!!.visibility = View.INVISIBLE
        } else {
            adView.bodyView!!.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }
        if (nativeAd.callToAction == null) {
            adView.callToActionView!!.visibility = View.INVISIBLE
        } else {
            adView.callToActionView!!.visibility = View.VISIBLE
            (adView.callToActionView as TextView).text = nativeAd.callToAction
        }
        if (nativeAd.icon == null) {
            adView.iconView!!.visibility = View.GONE
        } else {
            (adView.iconView as ImageView).setImageDrawable(
                nativeAd.icon!!.drawable
            )
            adView.iconView!!.visibility = View.VISIBLE
        }
        adView.setNativeAd(nativeAd)
    }


    private fun loadInterstitialAdmob(activity: Activity, idInterstitial: String) {
        val request = AdRequest.Builder()
            .build()
        InterstitialAd.load(activity, idInterstitial, request,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    mInterstitialAd = interstitialAd
 //                   iShowLog("--> onAdLoaded load")
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle the error
   //                 iShowLog(loadAdError.message)
                    mInterstitialAd = null
                }
            })
    }

    fun showInterstitialAdmob(
        activity: Activity, idInterstitial: String, interval: Int
    ) {
        loadInterstitialAdmob(activity, idInterstitial)
        if (counter >= interval) {
            if (mInterstitialAd != null) {
                mInterstitialAd!!.show(activity)
            }
        } else {
            counter++
        }
    }

    fun bannerAdShow(
        activity: Activity?,
        layAds: FrameLayout,
        idBanner: String?
    ) {
        val request = AdRequest.Builder().build()
        adViewAdmob = AdView(activity!!)
        adViewAdmob!!.adUnitId = idBanner!!
        layAds.addView(adViewAdmob)
        adViewAdmob!!.setAdSize(AdSize.BANNER)
        adViewAdmob!!.loadAd(request)
        adViewAdmob!!.adListener = object : AdListener() {
            override fun onAdLoaded() {}
            override fun onAdFailedToLoad(adError: LoadAdError) {}
            override fun onAdOpened() {}
            override fun onAdClicked() {}
            override fun onAdClosed() {}
        }
    }
}