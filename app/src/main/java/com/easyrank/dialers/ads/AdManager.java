package com.easyrank.dialers.ads;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.nativead.NativeAd;

public class AdManager {
    private static final String TAG = "AdManager";
    private static final String PREFS_NAME = "ad_preferences";
    private static final String KEY_ADS_REMOVED = "ads_removed";
    
    // Test Ad Unit IDs - Replace with your actual AdMob Ad Unit IDs
    private static final String BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"; // Test Banner
    private static final String INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"; // Test Interstitial
    private static final String NATIVE_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"; // Test Native
    
    private static AdManager instance;
    private Context context;
    private SharedPreferences preferences;
    private boolean adsRemoved = false;
    private boolean isInitialized = false;
    
    private AdManager(Context context) {
        this.context = context.getApplicationContext();
        this.preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.adsRemoved = preferences.getBoolean(KEY_ADS_REMOVED, false);
    }
    
    public static synchronized AdManager getInstance(Context context) {
        if (instance == null) {
            instance = new AdManager(context);
        }
        return instance;
    }
    
    public void initialize() {
        if (isInitialized) return;
        
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                isInitialized = true;
                Log.d(TAG, "AdMob initialized successfully");
            }
        });
    }
    
    public boolean areAdsRemoved() {
        return adsRemoved;
    }
    
    public void removeAds() {
        adsRemoved = true;
        preferences.edit().putBoolean(KEY_ADS_REMOVED, true).apply();
        Log.d(TAG, "Ads removed by user");
    }
    
    public void restoreAds() {
        adsRemoved = false;
        preferences.edit().putBoolean(KEY_ADS_REMOVED, false).apply();
        Log.d(TAG, "Ads restored");
    }
    
    public String getBannerAdUnitId() {
        return BANNER_AD_UNIT_ID;
    }
    
    public String getInterstitialAdUnitId() {
        return INTERSTITIAL_AD_UNIT_ID;
    }
    
    public String getNativeAdUnitId() {
        return NATIVE_AD_UNIT_ID;
    }
    
    public AdRequest createAdRequest() {
        return new AdRequest.Builder().build();
    }
    
    public boolean shouldShowAds() {
        return !adsRemoved && isInitialized;
    }
}
