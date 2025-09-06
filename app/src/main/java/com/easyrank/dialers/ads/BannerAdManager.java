package com.easyrank.dialers.ads;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

public class BannerAdManager {
    private static final String TAG = "BannerAdManager";
    private AdView adView;
    private AdManager adManager;
    private Context context;
    
    public BannerAdManager(Context context) {
        this.context = context;
        this.adManager = AdManager.getInstance(context);
    }
    
    public void createBannerAd(LinearLayout adContainer) {
        if (!adManager.shouldShowAds()) {
            Log.d(TAG, "Ads removed or not initialized, hiding banner");
            adContainer.setVisibility(View.GONE);
            return;
        }
        
        adView = new AdView(context);
        adView.setAdUnitId(adManager.getBannerAdUnitId());
        adView.setAdSize(AdSize.BANNER);
        
        adContainer.removeAllViews();
        adContainer.addView(adView);
        
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.d(TAG, "Banner ad loaded successfully");
                adContainer.setVisibility(View.VISIBLE);
            }
            
            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                Log.e(TAG, "Banner ad failed to load: " + adError.getMessage());
                adContainer.setVisibility(View.GONE);
            }
            
            @Override
            public void onAdOpened() {
                Log.d(TAG, "Banner ad opened");
            }
            
            @Override
            public void onAdClosed() {
                Log.d(TAG, "Banner ad closed");
            }
        });
        
        AdRequest adRequest = adManager.createAdRequest();
        adView.loadAd(adRequest);
    }
    
    public void destroy() {
        if (adView != null) {
            adView.destroy();
        }
    }
    
    public void pause() {
        if (adView != null) {
            adView.pause();
        }
    }
    
    public void resume() {
        if (adView != null) {
            adView.resume();
        }
    }
}
