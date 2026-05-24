package com.example.hadeelzlibrary.activities;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TransactionActivity extends AppCompatActivity {

    // UI Components
    TextInputEditText editStudentName, editBookTitle;
    TextInputEditText editBorrowDate, editReturnDate;
    TextView          txtDelayDays, txtFineAmount;
    Button            btnCalculate, btnSaveTransaction, btnReset, btnBack;

    // Database
    DatabaseHelper db;

    // Calculated values
    int    delayDays  = 0;
    double fineAmount = 0.0;

    // Fine rule
    private static final int    FREE_DAYS     = 7;
    private static final double FINE_PER_DAY  = 0.500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        // Connect UI
        editStudentName = findViewById(R.id.editStudentName);
        editBookTitle   = findViewById(R.id.editBookTitle);
        editBorrowDate  = findViewById(R.id.editBorrowDate);
        editReturnDate  = findViewById(R.id.editReturnDate);
        txtDelayDays    = findViewById(R.id.txtDelayDays);
        txtFineAmount   = findViewById(R.id.txtFineAmount);
        btnCalculate    = findViewById(R.id.btnCalculate);
        btnSaveTransaction = findViewById(R.id.btnSaveTransaction);
        btnReset        = findViewById(R.id.btnReset);
        btnBack         = findViewById(R.id.btnBack);

        // Connect Database
        db = new DatabaseHelper(this);

        // ── CALCULATE FINE ────────────────────────────────
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String borrowStr = editBorrowDate.getText().toString().trim();
                String returnStr = editReturnDate.getText().toString().trim();

                if (TextUtils.isEmpty(borrowStr)) {
                    editBorrowDate.setError("Enter borrow date");
                    return;
                }
                if (TextUtils.isEmpty(returnStr)) {
                    editReturnDate.setError("Enter return date");
                    return;
                }

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    Date borrowDate = sdf.parse(borrowStr);
                    Date returnDate = sdf.parse(returnStr);

                    if (returnDate.before(borrowDate)) {
                        Toast.makeText(TransactionActivity.this,
                                "Return date cannot be before borrow date",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Calculate total days borrowed
                    long diffMillis  = returnDate.getTime() - borrowDate.getTime();
                    long totalDays   = TimeUnit.MILLISECONDS.toDays(diffMillis);

                    // Apply free days rule
                    delayDays = (int) Math.max(0, totalDays - FREE_DAYS);

                    // Calculate fine
                    fineAmount = delayDays * FINE_PER_DAY;

                    // Update UI
                    txtDelayDays.setText(delayDays + " days");
                    txtFineAmount.setText(String.format(Locale.getDefault(),
                            "%.3f OMR", fineAmount));

                } catch (ParseException e) {
                    Toast.makeText(TransactionActivity.this,
                            "Invalid date format. Use YYYY-MM-DD",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // ── SAVE TRANSACTION ──────────────────────────────
        btnSaveTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String studentName = editStudentName.getText().toString().trim();
                String bookTitle   = editBookTitle.getText().toString().trim();
                String borrowDate  = editBorrowDate.getText().toString().trim();
                String returnDate  = editReturnDate.getText().toString().trim();

                if (TextUtils.isEmpty(studentName)) {
                    editStudentName.setError("Enter student name");
                    return;
                }
                if (TextUtils.isEmpty(bookTitle)) {
                    editBookTitle.setError("Enter book title");
                    return;
                }
                if (TextUtils.isEmpty(borrowDate)) {
                    editBorrowDate.setError("Enter borrow date");
                    return;
                }
                if (TextUtils.isEmpty(returnDate)) {
                    editReturnDate.setError("Enter return date");
                    return;
                }

                boolean success = db.insertTransaction(
                        studentName, bookTitle,
                        borrowDate, returnDate,
                        delayDays, fineAmount);

                if (success) {
                    Toast.makeText(TransactionActivity.this,
                            "Transaction saved!", Toast.LENGTH_SHORT).show();
                    clearFields();
                } else {
                    Toast.makeText(TransactionActivity.this,
                            "Failed to save transaction.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // ── RESET ─────────────────────────────────────────
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFields();
            }
        });

        // ── BACK ──────────────────────────────────────────
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // ── Helper: Clear fields ──────────────────────────────
    private void clearFields() {
        editStudentName.setText("");
        editBookTitle.setText("");
        editBorrowDate.setText("");
        editReturnDate.setText("");
        txtDelayDays.setText("0 days");
        txtFineAmount.setText("0.000 OMR");
        delayDays  = 0;
        fineAmount = 0.0;
    }
}