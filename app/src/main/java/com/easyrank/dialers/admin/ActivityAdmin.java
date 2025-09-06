package com.easyrank.dialers.admin;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.easyrank.dialers.BaseActivity;

public class ActivityAdmin extends BaseActivity {
    private static final String TAG = "ActivityAdmin";
    
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        
        android.util.Log.d("ActivityAdmin", "Admin activity created successfully!");
        
        // Create a simple admin interface
        TextView textView = new TextView(this);
        textView.setText("Admin Mode - Welcome to Admin Panel");
        textView.setTextSize(18);
        textView.setPadding(50, 50, 50, 50);
        setContentView(textView);
        
        Toast.makeText(this, "Admin mode activated", Toast.LENGTH_LONG).show();
    }
}
