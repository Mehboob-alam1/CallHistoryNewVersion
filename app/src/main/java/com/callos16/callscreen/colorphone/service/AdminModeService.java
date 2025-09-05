package com.callos16.callscreen.colorphone.service;

import android.util.Log;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.callos16.callscreen.colorphone.Config;

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
        
        DatabaseReference configRef = FirebaseDatabase.getInstance()
                .getReference(Config.FIREBASE_APP_CONFIG_NODE);

        configRef.child(Config.FIREBASE_ADMIN_MODE_KEY).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean isAdminMode = snapshot.exists() && snapshot.getValue(Boolean.class);
                
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
    }
}
