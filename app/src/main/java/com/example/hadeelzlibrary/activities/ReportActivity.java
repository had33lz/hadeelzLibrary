package com.example.hadeelzlibrary.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hadeelzlibrary.R;
import com.example.hadeelzlibrary.database.DatabaseHelper;
import com.example.hadeelzlibrary.models.Book;
import com.example.hadeelzlibrary.models.Transaction;

import java.util.ArrayList;

public class ReportActivity extends AppCompatActivity {

    // UI
    Button   btnShowBooks, btnShowTransactions, btnBack;
    ListView listViewReport;
    TextView txtSectionLabel;

    // Database
    DatabaseHelper db;

    // Data lists
    ArrayList<Book>        bookList  = new ArrayList<>();
    ArrayList<Transaction> transList = new ArrayList<>();

    // Track current mode
    boolean showingBooks = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // Connect UI
        btnShowBooks        = findViewById(R.id.btnShowBooks);
        btnShowTransactions = findViewById(R.id.btnShowTransactions);
        btnBack             = findViewById(R.id.btnBack);
        listViewReport      = findViewById(R.id.listViewReport);
        txtSectionLabel     = findViewById(R.id.txtSectionLabel);

        db = new DatabaseHelper(this);

        // Load books by default
        loadBooks();

        // ── SHOW BOOKS ────────────────────────────────────
        btnShowBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showingBooks = true;
                loadBooks();
            }
        });

        // ── SHOW TRANSACTIONS ─────────────────────────────
        btnShowTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showingBooks = false;
                loadTransactions();
            }
        });

        // ── ITEM CLICK ────────────────────────────────────
        listViewReport.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (showingBooks) {
                    showBookDialog(position);
                } else {
                    showTransactionDialog(position);
                }
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

    // ── Book Dialog: Update or Delete ─────────────────────
    private void showBookDialog(final int position) {
        final Book book = bookList.get(position);

        new AlertDialog.Builder(this)
                .setTitle("Title :  " + book.getTitle())
                .setMessage("What do you want to do?")
                .setPositiveButton("️Update", (dialog, which) -> {
                    // Send book data to AdminActivity
                    Intent intent = new Intent(ReportActivity.this,
                            AdminActivity.class);
                    intent.putExtra("bookId",   book.getBookId());
                    intent.putExtra("title",    book.getTitle());
                    intent.putExtra("author",   book.getAuthor());
                    intent.putExtra("category", book.getCategory());
                    intent.putExtra("quantity", book.getQuantity());
                    startActivity(intent);
                })
                .setNegativeButton("Delete", (dialog, which) -> {
                    // Confirm delete
                    new AlertDialog.Builder(this)
                            .setTitle("Confirm Delete")
                            .setMessage("Delete \"" + book.getTitle() + "\"?")
                            .setPositiveButton("Yes, Delete", (d, w) -> {
                                boolean success = db.deleteBook(book.getBookId());
                                if (success) {
                                    Toast.makeText(this,
                                            "Book deleted!",
                                            Toast.LENGTH_SHORT).show();
                                    loadBooks();
                                } else {
                                    Toast.makeText(this,
                                            "Delete failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                })
                .setNeutralButton("Cancel", null)
                .show();
    }

    // ── Transaction Dialog: Delete ────────────────────────
    private void showTransactionDialog(final int position) {
        final Transaction trans = transList.get(position);

        new AlertDialog.Builder(this)
                .setTitle("TR:  " + trans.getStudentName())
                .setMessage("Book: " + trans.getBookTitle() +
                        "\nFine: " + String.format("%.3f OMR",
                        trans.getFineAmount()))
                .setNegativeButton("Delete", (dialog, which) -> {
                    new AlertDialog.Builder(this)
                            .setTitle("Confirm Delete")
                            .setMessage("Delete this transaction?")
                            .setPositiveButton("Yes, Delete", (d, w) -> {
                                boolean success = db.deleteTransaction(
                                        trans.getTransId());
                                if (success) {
                                    Toast.makeText(this,
                                            "Transaction deleted!",
                                            Toast.LENGTH_SHORT).show();
                                    loadTransactions();
                                } else {
                                    Toast.makeText(this,
                                            "Delete failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                })
                .setNeutralButton("Cancel", null)
                .show();
    }

    // ── Load Books ────────────────────────────────────────
    private void loadBooks() {
        txtSectionLabel.setText("Showing: Books — tap item to edit or delete");
        bookList.clear();
        ArrayList<String> displayList = new ArrayList<>();

        Cursor cursor = db.getAllBooks();
        while (cursor.moveToNext()) {
            Book book = new Book(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getInt(4));
            bookList.add(book);
            displayList.add("📚 " + book.getTitle() +
                    "\n   Author: "   + book.getAuthor() +
                    "\n   Category: " + book.getCategory() +
                    "\n   Quantity: " + book.getQuantity());
        }
        cursor.close();

        if (displayList.isEmpty()) displayList.add("No books found.");

        listViewReport.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, displayList));
    }

    // ── Load Transactions ─────────────────────────────────
    private void loadTransactions() {
        txtSectionLabel.setText("Showing: Transactions — tap item to delete");
        transList.clear();
        ArrayList<String> displayList = new ArrayList<>();

        Cursor cursor = db.getAllTransactions();
        while (cursor.moveToNext()) {
            Transaction trans = new Transaction(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getInt(5),
                    cursor.getDouble(6));
            transList.add(trans);
            displayList.add("📋 " + trans.getStudentName() +
                    "\n   Book: "     + trans.getBookTitle() +
                    "\n   Borrowed: " + trans.getBorrowDate() +
                    "\n   Returned: " + trans.getReturnDate() +
                    "\n   Delay: "    + trans.getDelayDays() + " days" +
                    "\n   Fine: "     + String.format("%.3f OMR",
                    trans.getFineAmount()));
        }
        cursor.close();

        if (displayList.isEmpty()) displayList.add("No transactions found.");

        listViewReport.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, displayList));
    }
}