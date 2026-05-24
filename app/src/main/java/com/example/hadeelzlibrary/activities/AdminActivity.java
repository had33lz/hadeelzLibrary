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

    // ================= UI COMPONENTS =================

    // Text fields for book information
    TextInputEditText editTitle, editAuthor, editCategory, editQuantity;

    // Buttons on the admin screen
    Button btnAddBook, btnReset, btnTransactions, btnViewReports, btnLogout;

    // ================= DATABASE =================

    // Database object
    DatabaseHelper db;

    // ================= UPDATE MODE =================

    // Checks if admin is updating a book
    boolean isUpdateMode = false;

    // Stores selected book ID
    int selectedBookId = -1;

    // ================= onCreate METHOD =================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Call parent activity
        super.onCreate(savedInstanceState);

        // Connect Java with XML layout
        setContentView(R.layout.activity_admin);

        // ================= CONNECT UI =================

        // Connect title input field
        editTitle = findViewById(R.id.editTitle);

        // Connect author input field
        editAuthor = findViewById(R.id.editAuthor);

        // Connect category input field
        editCategory = findViewById(R.id.editCategory);

        // Connect quantity input field
        editQuantity = findViewById(R.id.editQuantity);

        // Connect Add Book button
        btnAddBook = findViewById(R.id.btnAddBook);

        // Connect Reset button
        btnReset = findViewById(R.id.btnReset);

        // Connect Transactions button
        btnTransactions = findViewById(R.id.btnTransactions);

        // Connect Reports button
        btnViewReports = findViewById(R.id.btnViewReports);

        // Connect Logout button
        btnLogout = findViewById(R.id.btnLogout);

        // ================= DATABASE CONNECTION =================

        // Create database helper object
        db = new DatabaseHelper(this);

        // ================= CHECK UPDATE MODE =================

        // Receive data from another activity
        Intent incoming = getIntent();

        // Check if activity received a book ID
        if (incoming.hasExtra("bookId")) {

            // Turn ON update mode
            isUpdateMode = true;

            // Get selected book ID
            selectedBookId = incoming.getIntExtra("bookId", -1);

            // Set old title into text field
            editTitle.setText(incoming.getStringExtra("title"));

            // Set old author into text field
            editAuthor.setText(incoming.getStringExtra("author"));

            // Set old category into text field
            editCategory.setText(incoming.getStringExtra("category"));

            // Set old quantity into text field
            editQuantity.setText(
                    String.valueOf(
                            incoming.getIntExtra("quantity", 0)
                    )
            );

            // Change button text
            btnAddBook.setText("Update Book");

            // Change button color
            btnAddBook.setBackgroundTintList(
                    getColorStateList(R.color.olive)
            );

            // Show popup message
            Toast.makeText(
                    this,
                    "Edit the fields then click Update Book",
                    Toast.LENGTH_LONG
            ).show();
        }

        // ================= ADD / UPDATE BUTTON =================

        // Run code when Add Book button clicked
        btnAddBook.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Get title from input field
                String title =
                        editTitle.getText().toString().trim();

                // Get author from input field
                String author =
                        editAuthor.getText().toString().trim();

                // Get category from input field
                String category =
                        editCategory.getText().toString().trim();

                // Get quantity from input field
                String quantStr =
                        editQuantity.getText().toString().trim();

                // ================= VALIDATION =================

                // Check if title is empty
                if (TextUtils.isEmpty(title)) {

                    // Show error message
                    editTitle.setError("Enter book title");

                    // Stop execution
                    return;
                }

                // Check if author is empty
                if (TextUtils.isEmpty(author)) {

                    // Show error message
                    editAuthor.setError("Enter author name");

                    // Stop execution
                    return;
                }

                // Check if category is empty
                if (TextUtils.isEmpty(category)) {

                    // Show error message
                    editCategory.setError("Enter category");

                    // Stop execution
                    return;
                }

                // Check if quantity is empty
                if (TextUtils.isEmpty(quantStr)) {

                    // Show error message
                    editQuantity.setError("Enter quantity");

                    // Stop execution
                    return;
                }

                // Convert quantity text to integer
                int quantity = Integer.parseInt(quantStr);

                // ================= UPDATE BOOK =================

                // If update mode is ON
                if (isUpdateMode) {

                    // Update existing book in database
                    boolean success = db.updateBook(
                            selectedBookId,
                            title,
                            author,
                            category,
                            quantity
                    );

                    // If update successful
                    if (success) {

                        // Show success message
                        Toast.makeText(
                                AdminActivity.this,
                                "Book updated!",
                                Toast.LENGTH_SHORT
                        ).show();

                        // Clear all fields
                        clearFields();

                        // Turn OFF update mode
                        isUpdateMode = false;

                        // Reset selected ID
                        selectedBookId = -1;

                        // Restore button text
                        btnAddBook.setText("Add Book");

                        // Restore button color
                        btnAddBook.setBackgroundTintList(
                                getColorStateList(R.color.cinnamon)
                        );
                    }

                } else {

                    // ================= INSERT NEW BOOK =================

                    // Insert new book into database
                    boolean success = db.insertBook(
                            title,
                            author,
                            category,
                            quantity
                    );

                    // If insert successful
                    if (success) {

                        // Show success popup
                        Toast.makeText(
                                AdminActivity.this,
                                "Book added successfully!",
                                Toast.LENGTH_SHORT
                        ).show();

                        // Clear fields
                        clearFields();

                    } else {

                        // Show failure popup
                        Toast.makeText(
                                AdminActivity.this,
                                "Failed to add book.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
            }
        });

        // ================= RESET BUTTON =================

        // Run code when Reset button clicked
        btnReset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Clear all text fields
                clearFields();

                // Turn OFF update mode
                isUpdateMode = false;

                // Reset selected ID
                selectedBookId = -1;

                // Restore button text
                btnAddBook.setText("Add Book");

                // Restore button color
                btnAddBook.setBackgroundTintList(
                        getColorStateList(R.color.cinnamon)
                );

                // Show popup message
                Toast.makeText(
                        AdminActivity.this,
                        "Fields cleared.",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        // ================= TRANSACTIONS BUTTON =================

        // Open TransactionActivity when clicked
        btnTransactions.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        startActivity(
                                new Intent(
                                        AdminActivity.this,
                                        TransactionActivity.class
                                )
                        );
                    }
                }
        );

        // ================= REPORT BUTTON =================

        // Open ReportActivity when clicked
        btnViewReports.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        startActivity(
                                new Intent(
                                        AdminActivity.this,
                                        ReportActivity.class
                                )
                        );
                    }
                }
        );

        // ================= LOGOUT BUTTON =================

        // Open LoginActivity when clicked
        btnLogout.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        startActivity(
                                new Intent(
                                        AdminActivity.this,
                                        LoginActivity.class
                                )
                        );

                        // Close current activity
                        finish();
                    }
                }
        );
    }

    // ================= CLEAR FIELDS METHOD =================

    // Method to clear all text fields
    private void clearFields() {

        // Clear title field
        editTitle.setText("");

        // Clear author field
        editAuthor.setText("");

        // Clear category field
        editCategory.setText("");

        // Clear quantity field
        editQuantity.setText("");
    }
}