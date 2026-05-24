package com.example.hadeelzlibrary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hadeelzlibrary.R;
import com.example.hadeelzlibrary.database.DatabaseHelper;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    // UI Components
    TextInputEditText editFullName, editEmail, editPassword, editConfirmPassword;
    Button            btnRegister;
    TextView          txtGoLogin;

    // Database
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Connect UI
        editFullName        = findViewById(R.id.editFullName);
        editEmail           = findViewById(R.id.editEmail);
        editPassword        = findViewById(R.id.editPassword);
        editConfirmPassword = findViewById(R.id.editConfirmPassword);
        btnRegister         = findViewById(R.id.btnRegister);
        txtGoLogin          = findViewById(R.id.txtGoLogin);

        // Connect Database
        db = new DatabaseHelper(this);

        // ── Register Button ───────────────────────────────
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fullname  = editFullName.getText().toString().trim();
                String email     = editEmail.getText().toString().trim();
                String password  = editPassword.getText().toString().trim();
                String confirm   = editConfirmPassword.getText().toString().trim();

                // ── Validations ───────────────────────────
                if (TextUtils.isEmpty(fullname)) {
                    editFullName.setError("Enter your full name");
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    editEmail.setError("Enter your email");
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    editEmail.setError("Invalid email format");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    editPassword.setError("Enter a password");
                    return;
                }
                if (password.length() < 6) {
                    editPassword.setError("Minimum 6 characters");
                    return;
                }
                if (!password.equals(confirm)) {
                    editConfirmPassword.setError("Passwords do not match");
                    return;
                }


// ── Check if email already exists ────────
                if (db.isEmailExists(email)) {
                    editEmail.setError("This email is already registered!");
                    editEmail.requestFocus();
                    Toast.makeText(RegisterActivity.this,
                            " This email already exists. Please login instead.",
                            Toast.LENGTH_LONG).show();
                    return;
                }

// ── Save to Database ──────────────────────
                boolean success = db.registerUser(fullname, email, password);

                if (success) {
                    Toast.makeText(RegisterActivity.this,
                            "Account created! Please login.",
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this,
                            "Registration failed. Try again.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        // ── Go to Login ───────────────────────────────────
        txtGoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}