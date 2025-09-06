package com.easyrank.dialers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.easyrank.dialers.ads.AdManager;

public class ActivitySettings extends AppCompatActivity {
    private static final String TAG = "ActivitySettings";
    private AdManager adManager;
    private Button btnRemoveAds;
    private TextView tvAdStatus;
    
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        
        adManager = AdManager.getInstance(this);
        adManager.initialize();
        
        createSettingsUI();
    }
    
    private void createSettingsUI() {
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(50, 50, 50, 50);
        
        // Title
        TextView title = new TextView(this);
        title.setText("Settings");
        title.setTextSize(24);
        title.setPadding(0, 0, 0, 30);
        mainLayout.addView(title);
        
        // Ad Status
        tvAdStatus = new TextView(this);
        updateAdStatus();
        tvAdStatus.setTextSize(16);
        tvAdStatus.setPadding(0, 0, 0, 20);
        mainLayout.addView(tvAdStatus);
        
        // Remove Ads Button
        btnRemoveAds = new Button(this);
        btnRemoveAds.setText(adManager.areAdsRemoved() ? "Ads Removed" : "Remove Ads");
        btnRemoveAds.setEnabled(!adManager.areAdsRemoved());
        btnRemoveAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAds();
            }
        });
        mainLayout.addView(btnRemoveAds);
        
        // Restore Ads Button (for testing)
        Button btnRestoreAds = new Button(this);
        btnRestoreAds.setText("Restore Ads (Testing)");
        btnRestoreAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restoreAds();
            }
        });
        mainLayout.addView(btnRestoreAds);
        
        // Back Button
        Button btnBack = new Button(this);
        btnBack.setText("Back to Home");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mainLayout.addView(btnBack);
        
        setContentView(mainLayout);
    }
    
    private void updateAdStatus() {
        if (adManager.areAdsRemoved()) {
            tvAdStatus.setText("✅ Ads have been removed");
            tvAdStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            tvAdStatus.setText("📱 Ads are currently showing");
            tvAdStatus.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        }
    }
    
    private void removeAds() {
        adManager.removeAds();
        updateAdStatus();
        btnRemoveAds.setText("Ads Removed");
        btnRemoveAds.setEnabled(false);
        
        Toast.makeText(this, "Ads have been removed successfully!", Toast.LENGTH_LONG).show();
        
        // You can add your purchase logic here
        // For now, we'll just simulate a successful removal
    }
    
    private void restoreAds() {
        adManager.restoreAds();
        updateAdStatus();
        btnRemoveAds.setText("Remove Ads");
        btnRemoveAds.setEnabled(true);
        
        Toast.makeText(this, "Ads have been restored", Toast.LENGTH_SHORT).show();
    }
}
