package com.easyranktools.easyranktools.ads;

import android.content.Context;
import android.util.Log;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

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
        
        AdRequest adRequest = adManager.createAdRequest();
        
        InterstitialAd.load(context, adManager.getInterstitialAdUnitId(), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(InterstitialAd ad) {
                Log.d(TAG, "Interstitial ad loaded successfully");
                interstitialAd = ad;
                
                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        Log.d(TAG, "Interstitial ad dismissed");
                        interstitialAd = null;
                        if (listener != null) {
                            listener.onAdClosed();
                        }
                    }
                    
                    @Override
                    public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                        Log.e(TAG, "Interstitial ad failed to show: " + adError.getMessage());
                        interstitialAd = null;
                        if (listener != null) {
                            listener.onAdFailedToLoad();
                        }
                    }
                    
                    @Override
                    public void onAdShowedFullScreenContent() {
                        Log.d(TAG, "Interstitial ad showed");
                    }
                });
            }
            
            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getMessage());
                interstitialAd = null;
                if (listener != null) {
                    listener.onAdFailedToLoad();
                }
            }
        });
    }
    
    public void showInterstitialAd() {
        if (interstitialAd != null) {
            Log.d(TAG, "Showing interstitial ad");
            interstitialAd.show((android.app.Activity) context);
        } else {
            Log.d(TAG, "Interstitial ad not loaded or not available");
            if (listener != null) {
                listener.onAdFailedToLoad();
            }
        }
    }
    
    public boolean isLoaded() {
        return interstitialAd != null;
    }
}
