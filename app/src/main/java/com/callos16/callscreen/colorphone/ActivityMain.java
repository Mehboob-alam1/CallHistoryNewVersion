package com.callos16.callscreen.colorphone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.callos16.callscreen.colorphone.ads.AdManager;
import com.callos16.callscreen.colorphone.ads.BannerAdManager;
import com.callos16.callscreen.colorphone.ads.InterstitialAdManager;
import com.callos16.callscreen.colorphone.ads.NativeAdManager;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

public class ActivityMain extends BaseActivity {
    private static final String TAG = "ActivityMain";
    private AdManager adManager;
    private BannerAdManager bannerAdManager;
    private InterstitialAdManager interstitialAdManager;
    private NativeAdManager nativeAdManager;
    private LinearLayout bannerAdContainer;
    private LinearLayout nativeAdContainer;
    
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        
        // Initialize AdMob
        adManager = AdManager.getInstance(this);
        adManager.initialize();
        
        createMainUI();
        setupAds();
    }
    
    private void createMainUI() {
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(20, 20, 20, 20);
        
        // Title
        TextView title = new TextView(this);
        title.setText("iCall OS16 - Main Screen");
        title.setTextSize(20);
        title.setPadding(0, 0, 0, 20);
        mainLayout.addView(title);
        
        // Native Ad Container
        nativeAdContainer = new LinearLayout(this);
        nativeAdContainer.setOrientation(LinearLayout.VERTICAL);
        mainLayout.addView(nativeAdContainer);
        
        // Buttons
        Button btnCall = new Button(this);
        btnCall.setText("Make a Call");
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show interstitial ad before making call
                showInterstitialAd();
            }
        });
        mainLayout.addView(btnCall);
        
        Button btnSettings = new Button(this);
        btnSettings.setText("Settings");
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityMain.this, ActivitySettings.class));
            }
        });
        mainLayout.addView(btnSettings);
        
        Button btnDialer = new Button(this);
        btnDialer.setText("Open Dialer");
        btnDialer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityMain.this, com.easyrank.dialers.ActivityHome.class));
            }
        });
        mainLayout.addView(btnDialer);
        
        // Banner Ad Container
        bannerAdContainer = new LinearLayout(this);
        bannerAdContainer.setOrientation(LinearLayout.VERTICAL);
        bannerAdContainer.setMinimumHeight(100);
        mainLayout.addView(bannerAdContainer);
        
        setContentView(mainLayout);
    }
    
    private void setupAds() {
        // Setup Banner Ad
        bannerAdManager = new BannerAdManager(this);
        bannerAdManager.createBannerAd(bannerAdContainer);
        
        // Setup Native Ad
        nativeAdManager = new NativeAdManager(this);
        nativeAdManager.setListener(new NativeAdManager.NativeAdListener() {
            @Override
            public void onAdLoaded(NativeAd nativeAd) {
                runOnUiThread(() -> {
                    showNativeAd(nativeAd);
                });
            }
            
            @Override
            public void onAdFailedToLoad() {
                runOnUiThread(() -> {
                    nativeAdContainer.setVisibility(View.GONE);
                });
            }
        });
        nativeAdManager.loadNativeAd();
        
        // Setup Interstitial Ad
        interstitialAdManager = new InterstitialAdManager(this);
        interstitialAdManager.setListener(new InterstitialAdManager.InterstitialAdListener() {
            @Override
            public void onAdClosed() {
                // Proceed with call after ad is closed
                makeCall();
            }
            
            @Override
            public void onAdFailedToLoad() {
                // Proceed with call if ad fails to load
                makeCall();
            }
        });
        interstitialAdManager.loadInterstitialAd();
    }
    
    private void showNativeAd(NativeAd nativeAd) {
        if (!adManager.shouldShowAds()) {
            nativeAdContainer.setVisibility(View.GONE);
            return;
        }
        
        // Inflate native ad layout
        NativeAdView adView = (NativeAdView) getLayoutInflater().inflate(R.layout.native_ad_layout, null);
        nativeAdManager.populateNativeAdView(nativeAd, adView);
        
        nativeAdContainer.removeAllViews();
        nativeAdContainer.addView(adView);
        nativeAdContainer.setVisibility(View.VISIBLE);
    }
    
    private void showInterstitialAd() {
        if (adManager.shouldShowAds()) {
            interstitialAdManager.showInterstitialAd();
        } else {
            makeCall();
        }
    }
    
    private void makeCall() {
        // Simulate making a call
        android.widget.Toast.makeText(this, "Calling...", android.widget.Toast.LENGTH_SHORT).show();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (bannerAdManager != null) {
            bannerAdManager.resume();
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (bannerAdManager != null) {
            bannerAdManager.pause();
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bannerAdManager != null) {
            bannerAdManager.destroy();
        }
        if (nativeAdManager != null) {
            nativeAdManager.destroy();
        }
    }
}
