package com.callos16.callscreen.colorphone;

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

public class ActivityTestAds extends BaseActivity {
    private static final String TAG = "ActivityTestAds";
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
        
        createTestUI();
        setupAds();
    }
    
    private void createTestUI() {
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(20, 20, 20, 20);
        
        // Title
        TextView title = new TextView(this);
        title.setText("AdMob Ads Test Screen");
        title.setTextSize(20);
        title.setPadding(0, 0, 0, 20);
        mainLayout.addView(title);
        
        // Ad Status
        TextView adStatus = new TextView(this);
        adStatus.setText("Ad Status: " + (adManager.areAdsRemoved() ? "REMOVED" : "ACTIVE"));
        adStatus.setTextSize(16);
        adStatus.setPadding(0, 0, 0, 20);
        mainLayout.addView(adStatus);
        
        // Native Ad Container
        nativeAdContainer = new LinearLayout(this);
        nativeAdContainer.setOrientation(LinearLayout.VERTICAL);
        mainLayout.addView(nativeAdContainer);
        
        // Test Buttons
        Button btnTestInterstitial = new Button(this);
        btnTestInterstitial.setText("Test Interstitial Ad");
        btnTestInterstitial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testInterstitialAd();
            }
        });
        mainLayout.addView(btnTestInterstitial);
        
        Button btnTestBanner = new Button(this);
        btnTestBanner.setText("Test Banner Ad");
        btnTestBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testBannerAd();
            }
        });
        mainLayout.addView(btnTestBanner);
        
        Button btnTestNative = new Button(this);
        btnTestNative.setText("Test Native Ad");
        btnTestNative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testNativeAd();
            }
        });
        mainLayout.addView(btnTestNative);
        
        Button btnRemoveAds = new Button(this);
        btnRemoveAds.setText("Remove Ads");
        btnRemoveAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adManager.removeAds();
                recreate(); // Refresh the activity
            }
        });
        mainLayout.addView(btnRemoveAds);
        
        Button btnBack = new Button(this);
        btnBack.setText("Back to Main");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mainLayout.addView(btnBack);
        
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
        
        // Setup Interstitial Ad
        interstitialAdManager = new InterstitialAdManager(this);
        interstitialAdManager.setListener(new InterstitialAdManager.InterstitialAdListener() {
            @Override
            public void onAdClosed() {
                android.widget.Toast.makeText(ActivityTestAds.this, "Interstitial ad closed", android.widget.Toast.LENGTH_SHORT).show();
            }
            
            @Override
            public void onAdFailedToLoad() {
                android.widget.Toast.makeText(ActivityTestAds.this, "Interstitial ad failed to load", android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void testInterstitialAd() {
        interstitialAdManager.loadInterstitialAd();
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                interstitialAdManager.showInterstitialAd();
            }
        }, 2000); // Wait 2 seconds for ad to load
    }
    
    private void testBannerAd() {
        bannerAdManager.createBannerAd(bannerAdContainer);
    }
    
    private void testNativeAd() {
        nativeAdManager.loadNativeAd();
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
