package com.easyranktools.easyranktools;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.easyranktools.callhistoryforanynumber.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContactUsActivity extends AppCompatActivity {
    private EditText etName, etEmail, etMessage;
    private LinearLayout btnSubmit;
    private TextView txtCopyRight;

    private DatabaseReference settingsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact_us);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etMessage = findViewById(R.id.etMessage);
        btnSubmit = findViewById(R.id.btn_submit);
        txtCopyRight = findViewById(R.id.txtCopyRight);


        settingsRef = FirebaseDatabase.getInstance().getReference("app_config/showPrivacy");


        // Listen for changes and update UI
        settingsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // If the value exists and is boolean, show/hide accordingly.
                try {
                    Boolean show = null;
                    if (snapshot.getValue() instanceof Boolean) {
                        show = snapshot.getValue(Boolean.class);
                    } else if (snapshot.getValue() != null) {
                        // parse string "true"/"false"
                        show = Boolean.parseBoolean(String.valueOf(snapshot.getValue()));
                    }
                    if (show != null) {
                        txtCopyRight.setVisibility(show ? View.VISIBLE : View.GONE);
                    } else {
                        // default behavior (visible) if not present
                        txtCopyRight.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    // keep visible on error
                    txtCopyRight.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // On permission errors / network errors, keep the text visible and optionally notify.
                txtCopyRight.setVisibility(View.VISIBLE);
            }
        });

        // Button click -> validate and send
        btnSubmit.setOnClickListener(v -> onSubmit());

    }

    private void onSubmit() {
        final String name = etName.getText() == null ? "" : etName.getText().toString().trim();
        final String email = etEmail.getText() == null ? "" : etEmail.getText().toString().trim();
        final String message = etMessage.getText() == null ? "" : etMessage.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Enter your name");
            etName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email");
            etEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(message)) {
            etMessage.setError("Enter a message");
            etMessage.requestFocus();
            return;
        }

        // Compose email
        String toAddress = "support@callhistoryforanynumber.com"; // <-- change to real recipient
        String subject = "Contact Us - " + name;
        String body = "Name: " + name + "\n"
                + "Email: " + email + "\n\n"
                + message + "\n\n"
                + "App: " + getPackageName();

        // Use ACTION_SENDTO with mailto: so only email apps respond
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + Uri.encode(toAddress)));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        try {
            if (emailIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(Intent.createChooser(emailIntent, getString(R.string.contact_chooser_title)));
            } else {
                Toast.makeText(this, getString(R.string.no_email_app), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Unable to open email app", Toast.LENGTH_LONG).show();
        }
    }
}

