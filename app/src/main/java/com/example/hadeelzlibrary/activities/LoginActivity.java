package com.example.hadeelzlibrary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hadeelzlibrary.R;
import com.example.hadeelzlibrary.database.DatabaseHelper;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    // UI Components
    TextInputEditText editEmail, editPassword;
    Button            btnLogin;
    TextView          txtGoRegister;

    // Database
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Connect UI
        editEmail     = findViewById(R.id.editEmail);
        editPassword  = findViewById(R.id.editPassword);
        btnLogin      = findViewById(R.id.btnLogin);
        txtGoRegister = findViewById(R.id.txtGoRegister);

        // Connect Database
        db = new DatabaseHelper(this);

        // ── Login Button ──────────────────────────────────
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email    = editEmail.getText().toString().trim();
                String password = editPassword.getText().toString().trim();

                // Validation
                if (TextUtils.isEmpty(email)) {
                    editEmail.setError("Enter your email");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    editPassword.setError("Enter your password");
                    return;
                }
                if (password.length() < 6) {
                    editPassword.setError("Minimum 6 characters");
                    return;
                }

                // Check database
                boolean success = db.checkLogin(email, password);

                if (success) {
                    Toast.makeText(LoginActivity.this,
                            "Welcome back! ✓", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // ── Go to Register ────────────────────────────────
        txtGoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}