package com.example.hadeelzlibrary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hadeelzlibrary.R;
import com.example.hadeelzlibrary.database.DatabaseHelper;
import com.google.android.material.textfield.TextInputEditText;

public class AdminActivity extends AppCompatActivity {

    // UI Components
    TextInputEditText editTitle, editAuthor, editCategory, editQuantity;
    Button btnAddBook, btnReset, btnTransactions, btnViewReports, btnLogout;

    // Database
    DatabaseHelper db;

    // Update mode
    boolean isUpdateMode = false;
    int     selectedBookId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Connect UI
        editTitle    = findViewById(R.id.editTitle);
        editAuthor   = findViewById(R.id.editAuthor);
        editCategory = findViewById(R.id.editCategory);
        editQuantity = findViewById(R.id.editQuantity);

        btnAddBook      = findViewById(R.id.btnAddBook);
        btnReset        = findViewById(R.id.btnReset);
        btnTransactions = findViewById(R.id.btnTransactions);
        btnViewReports  = findViewById(R.id.btnViewReports);
        btnLogout       = findViewById(R.id.btnLogout);

        // Connect Database
        db = new DatabaseHelper(this);

        // ── Check if coming from Report (Update mode) ─────
        Intent incoming = getIntent();
        if (incoming.hasExtra("bookId")) {
            isUpdateMode   = true;
            selectedBookId = incoming.getIntExtra("bookId", -1);

            editTitle.setText(incoming.getStringExtra("title"));
            editAuthor.setText(incoming.getStringExtra("author"));
            editCategory.setText(incoming.getStringExtra("category"));
            editQuantity.setText(String.valueOf(incoming.getIntExtra("quantity", 0)));

            btnAddBook.setText("Update Book");
            btnAddBook.setBackgroundTintList(
                    getColorStateList(R.color.olive));

            Toast.makeText(this,
                    "Edit the fields then click Update Book",
                    Toast.LENGTH_LONG).show();
        }

        // ── ADD or UPDATE BOOK ────────────────────────────
        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title    = editTitle.getText().toString().trim();
                String author   = editAuthor.getText().toString().trim();
                String category = editCategory.getText().toString().trim();
                String quantStr = editQuantity.getText().toString().trim();

                if (TextUtils.isEmpty(title)) {
                    editTitle.setError("Enter book title");
                    return;
                }
                if (TextUtils.isEmpty(author)) {
                    editAuthor.setError("Enter author name");
                    return;
                }
                if (TextUtils.isEmpty(category)) {
                    editCategory.setError("Enter category");
                    return;
                }
                if (TextUtils.isEmpty(quantStr)) {
                    editQuantity.setError("Enter quantity");
                    return;
                }

                int quantity = Integer.parseInt(quantStr);

                if (isUpdateMode) {
                    // UPDATE existing book
                    boolean success = db.updateBook(
                            selectedBookId, title, author, category, quantity);
                    if (success) {
                        Toast.makeText(AdminActivity.this,
                                "Book updated!", Toast.LENGTH_SHORT).show();
                        clearFields();
                        isUpdateMode   = false;
                        selectedBookId = -1;
                        btnAddBook.setText("Add Book");
                        btnAddBook.setBackgroundTintList(
                                getColorStateList(R.color.cinnamon));
                    }
                } else {
                    // INSERT new book
                    boolean success = db.insertBook(
                            title, author, category, quantity);
                    if (success) {
                        Toast.makeText(AdminActivity.this,
                                "Book added successfully!",
                                Toast.LENGTH_SHORT).show();
                        clearFields();
                    } else {
                        Toast.makeText(AdminActivity.this,
                                "Failed to add book.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // ── RESET ─────────────────────────────────────────
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFields();
                isUpdateMode   = false;
                selectedBookId = -1;
                btnAddBook.setText("Add Book");
                btnAddBook.setBackgroundTintList(
                        getColorStateList(R.color.cinnamon));
                Toast.makeText(AdminActivity.this,
                        "Fields cleared.", Toast.LENGTH_SHORT).show();
            }
        });

        // ── TRANSACTIONS ──────────────────────────────────
        btnTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this,
                        TransactionActivity.class));
            }
        });

        // ── VIEW REPORTS ──────────────────────────────────
        btnViewReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this,
                        ReportActivity.class));
            }
        });

        // ── LOGOUT ────────────────────────────────────────
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this,
                        LoginActivity.class));
                finish();
            }
        });
    }

    private void clearFields() {
        editTitle.setText("");
        editAuthor.setText("");
        editCategory.setText("");
        editQuantity.setText("");
    }
}