package com.callos16.callscreen.colorphone;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityAdmin extends BaseActivity {
    private static final String TAG = "ActivityAdmin";
    
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        
        // Create a simple admin interface
        TextView textView = new TextView(this);
        textView.setText("Admin Mode - Welcome to Admin Panel");
        textView.setTextSize(18);
        textView.setPadding(50, 50, 50, 50);
        setContentView(textView);
        
        Toast.makeText(this, "Admin mode activated", Toast.LENGTH_SHORT).show();
    }
}
