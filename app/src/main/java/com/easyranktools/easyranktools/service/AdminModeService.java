package com.easyranktools.easyranktools.service;

import android.util.Log;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.easyranktools.easyranktools.Config;

public class AdminModeService {
    private static final String TAG = "AdminModeService";
    
    private AdminModeCallback callback;
    
    public interface AdminModeCallback {
        void onAdminModeChecked(boolean isAdminModeEnabled);
        void onError(String error);
    }
    
    public AdminModeService() {
        // No initialization needed for Realtime Database
    }
    
    public void checkAdminMode(AdminModeCallback callback) {
        this.callback = callback;
        
        try {
            Log.d(TAG, "Initializing Firebase Database...");
            DatabaseReference configRef = FirebaseDatabase.getInstance()
                    .getReference(Config.FIREBASE_APP_CONFIG_NODE);
            
            Log.d(TAG, "Firebase Database initialized, checking path: " + Config.FIREBASE_APP_CONFIG_NODE + "/" + Config.FIREBASE_ADMIN_MODE_KEY);

            configRef.child(Config.FIREBASE_ADMIN_MODE_KEY).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    boolean isAdminMode = false;
                    
                    if (snapshot.exists()) {
                        Boolean adminModeValue = snapshot.getValue(Boolean.class);
                        isAdminMode = adminModeValue != null && adminModeValue;
                        Log.d(TAG, "Snapshot exists, admin_mode value: " + adminModeValue + ", isAdminMode: " + isAdminMode);
                    } else {
                        Log.d(TAG, "Snapshot does not exist, defaulting to false");
                    }
                    
                    Log.d(TAG, "App mode: " + (isAdminMode ? "ADMIN" : "DIALER"));
                    
                    if (callback != null) {
                        callback.onAdminModeChecked(isAdminMode);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.e(TAG, "Error checking app mode: " + error.getMessage());
                    if (callback != null) {
                        callback.onError("Error checking app mode: " + error.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Exception during Firebase initialization: " + e.getMessage());
            if (callback != null) {
                callback.onError("Firebase initialization failed: " + e.getMessage());
            }
        }
    }
}