package com.callos16.callscreen.colorphone.ads;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MediaView;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

public class NativeAdManager {
    private static final String TAG = "NativeAdManager";
    private AdManager adManager;
    private Context context;
    private NativeAd nativeAd;
    private NativeAdListener listener;
    
    public interface NativeAdListener {
        void onAdLoaded(NativeAd nativeAd);
        void onAdFailedToLoad();
    }
    
    public NativeAdManager(Context context) {
        this.context = context;
        this.adManager = AdManager.getInstance(context);
    }
    
    public void setListener(NativeAdListener listener) {
        this.listener = listener;
    }
    
    public void loadNativeAd() {
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
                        NativeAdManager.this.nativeAd = nativeAd;
                        if (listener != null) {
                            listener.onAdLoaded(nativeAd);
                        }
                    }
                })
                .withAdListener(new AdListener() {
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
    
    public void populateNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        // Set the media view
        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);
        
        // Set other assets
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        
        // Populate the native ad view
        adView.setNativeAd(nativeAd);
    }
    
    public void destroy() {
        if (nativeAd != null) {
            nativeAd.destroy();
        }
    }
}
