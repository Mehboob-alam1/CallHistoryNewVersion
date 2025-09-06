package com.callos16.callscreen.colorphone.ads;

import android.content.Context;
import android.util.Log;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;

public class InterstitialAdManager {
    private static final String TAG = "InterstitialAdManager";
    private InterstitialAd interstitialAd;
    private AdManager adManager;
    private Context context;
    private InterstitialAdListener listener;
    
    public interface InterstitialAdListener {
        void onAdClosed();
        void onAdFailedToLoad();
    }
    
    public InterstitialAdManager(Context context) {
        this.context = context;
        this.adManager = AdManager.getInstance(context);
    }
    
    public void setListener(InterstitialAdListener listener) {
        this.listener = listener;
    }
    
    public void loadInterstitialAd() {
        if (!adManager.shouldShowAds()) {
            Log.d(TAG, "Ads removed or not initialized, skipping interstitial");
            if (listener != null) {
                listener.onAdFailedToLoad();
            }
            return;
        }
        
        interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId(adManager.getInterstitialAdUnitId());
        
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.d(TAG, "Interstitial ad loaded successfully");
            }
            
            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getMessage());
                if (listener != null) {
                    listener.onAdFailedToLoad();
                }
            }
            
            @Override
            public void onAdOpened() {
                Log.d(TAG, "Interstitial ad opened");
            }
            
            @Override
            public void onAdClosed() {
                Log.d(TAG, "Interstitial ad closed");
                if (listener != null) {
                    listener.onAdClosed();
                }
            }
        });
        
        AdRequest adRequest = adManager.createAdRequest();
        interstitialAd.loadAd(adRequest);
    }
    
    public void showInterstitialAd() {
        if (interstitialAd != null && interstitialAd.isLoaded()) {
            Log.d(TAG, "Showing interstitial ad");
            interstitialAd.show();
        } else {
            Log.d(TAG, "Interstitial ad not loaded or not available");
            if (listener != null) {
                listener.onAdFailedToLoad();
            }
        }
    }
    
    public boolean isLoaded() {
        return interstitialAd != null && interstitialAd.isLoaded();
    }
}
