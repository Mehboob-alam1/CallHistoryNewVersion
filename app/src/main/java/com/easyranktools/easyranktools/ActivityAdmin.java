package com.easyranktools.easyranktools;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityAdmin extends AppCompatActivity {
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