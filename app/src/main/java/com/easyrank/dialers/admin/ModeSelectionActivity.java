package com.easyrank.dialers.admin;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.easyrank.dialers.R;


public class ModeSelectionActivity extends AppCompatActivity {
    private static final String TAG = "ModeSelectionActivity";
    // Removed splash delay to avoid timing issues during startup


    private ProgressBar progressBar;
    private Handler handler;
    private boolean modeChecked = false;
    private boolean isRequestingDefaultDialer = false;


    private static final int REQUEST_CODE_SET_DEFAULT_DIALER = 123;
    private boolean isModeCheckStarted = false;
    private boolean askedThisResume = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mode_selection);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        handler = new Handler(Looper.getMainLooper());


        //maybeStartModeCheck();

        checkAppMode();
    }

    private void initViews() {
        progressBar = findViewById(R.id.progressBar);
    }




    /**
     * If the app is launched via ACTION_DIAL / ACTION_CALL / tel: link,
     * forward the number to DialerActivity
     */


    private void checkAppMode() {
//        DatabaseReference configRef = FirebaseDatabase.getInstance()
//                .getReference(Config.FIREBASE_APP_CONFIG_NODE);
//
//        configRef.child(Config.FIREBASE_ADMIN_MODE_KEY).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                modeChecked = true;
//                boolean isAdminMode = snapshot.exists() && snapshot.getValue(Boolean.class);
//
//                Log.d(TAG, "App mode: " + (isAdminMode ? "ADMIN" : "DIALER"));
//
//                if (isAdminMode) {
//                    MyApplication.getInstance().routeToAdminFlow(ModeSelectionActivity.this, true);
//                } else {
//                    MyApplication.getInstance().routeToDialerFlow(ModeSelectionActivity.this, true);
//                }
//            }

//            @Override
//            public void onCancelled(DatabaseError error) {
//                modeChecked = true;
//                Log.e(TAG, "Error checking app mode: " + error.getMessage());
//                Toast.makeText(ModeSelectionActivity.this, "Error checking app mode", Toast.LENGTH_SHORT).show();
//                launchDialerMode();
//            }
//        });

        MyApplication.getInstance().routeToAdminFlow(ModeSelectionActivity.this, true);


        // Removed fallback timeout to prevent delayed navigation conflicts
    }

    private void launchDialerMode() {
        MyApplication.getInstance().routeToDialerFlow(ModeSelectionActivity.this, true);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    private void maybeStartModeCheck() {
        if (isModeCheckStarted) return;
        isModeCheckStarted = true;
        checkAppMode();
    }

}
