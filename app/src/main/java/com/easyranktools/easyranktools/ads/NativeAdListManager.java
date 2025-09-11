package com.easyranktools.easyranktools.ads;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.easyranktools.callhistoryforanynumber.R;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

public class NativeAdListManager {
    private static final String TAG = "NativeAdListManager";
    private AdManager adManager;
    private Context context;
    private NativeAdListener listener;
    
    public interface NativeAdListener {
        void onAdLoaded(NativeAd nativeAd, View adView);
        void onAdFailedToLoad();
    }
    
    public NativeAdListManager(Context context) {
        this.context = context;
        this.adManager = AdManager.getInstance(context);
    }
    
    public void setListener(NativeAdListener listener) {
        this.listener = listener;
    }
    
    public void loadNativeAd() {
        Log.d(TAG, "Loading native ad...");
        Log.d(TAG, "AdManager shouldShowAds: " + adManager.shouldShowAds());
//        Log.d(TAG, "AdManager isInitialized: " + adManager.isInitialized);
        Log.d(TAG, "AdManager adsRemoved: " + adManager.areAdsRemoved());
        Log.d(TAG, "Native Ad Unit ID: " + adManager.getNativeAdUnitId());
        
        if (!adManager.shouldShowAds()) {
            Log.d(TAG, "Ads removed or not initialized, skipping native ad");
            if (listener != null) {
                listener.onAdFailedToLoad();
            }
            return;
        }
        
        AdLoader adLoader = new AdLoader.Builder(context, adManager.getNativeAdUnitId())
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        Log.d(TAG, "Native ad loaded successfully");
                        if (listener != null) {
                            View adView = createNativeAdView(nativeAd);
                            listener.onAdLoaded(nativeAd, adView);
                        }
                    }
                })
                .withAdListener(new com.google.android.gms.ads.AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        Log.e(TAG, "Native ad failed to load: " + adError.getMessage());
                        if (listener != null) {
                            listener.onAdFailedToLoad();
                        }
                    }
                })
                .build();
        
        AdRequest adRequest = adManager.createAdRequest();
        adLoader.loadAd(adRequest);
    }
    
    private View createNativeAdView(NativeAd nativeAd) {
        Log.d(TAG, "Creating native ad view...");
        try {
            NativeAdView adView = (NativeAdView) LayoutInflater.from(context).inflate(R.layout.native_ad_list_item, null);
            Log.d(TAG, "Native ad view created successfully");
            populateNativeAdView(nativeAd, adView);
            return adView;
        } catch (Exception e) {
            Log.e(TAG, "Error creating native ad view: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    private void populateNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        Log.d(TAG, "Populating native ad view...");
        try {
            // Set the icon view
            adView.setIconView(adView.findViewById(R.id.ad_app_icon));
            
            // Set other assets
            adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
            adView.setBodyView(adView.findViewById(R.id.ad_body));
            adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
            adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
            
            Log.d(TAG, "Native ad view populated successfully");
            
            // Populate the native ad view
            adView.setNativeAd(nativeAd);
            Log.d(TAG, "Native ad set to view successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error populating native ad view: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
