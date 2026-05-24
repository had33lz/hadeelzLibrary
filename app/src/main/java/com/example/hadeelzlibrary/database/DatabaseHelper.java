package com.example.hadeelzlibrary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "had33lzLibrary.db";
    private static final int DATABASE_VERSION = 1;

    // ─── Table Names ───────────────────────────────────────
    private static final String TABLE_USERS        = "Users";
    private static final String TABLE_BOOKS        = "Books";
    private static final String TABLE_TRANSACTIONS = "Transactions";

    // ─── Users Columns ─────────────────────────────────────
    private static final String COL_USER_ID       = "id";
    private static final String COL_FULLNAME      = "fullname";
    private static final String COL_EMAIL         = "email";
    private static final String COL_PASSWORD      = "password";
    private static final String COL_ROLE          = "role";

    // ─── Books Columns ──────────────────────────────────────
    private static final String COL_BOOK_ID       = "bookId";
    private static final String COL_TITLE         = "title";
    private static final String COL_AUTHOR        = "author";
    private static final String COL_CATEGORY      = "category";
    private static final String COL_QUANTITY      = "quantity";

    // ─── Transactions Columns ───────────────────────────────
    private static final String COL_TRANS_ID      = "transId";
    private static final String COL_STUDENT_NAME  = "studentName";
    private static final String COL_BOOK_TITLE    = "bookTitle";
    private static final String COL_BORROW_DATE   = "borrowDate";
    private static final String COL_RETURN_DATE   = "returnDate";
    private static final String COL_DELAY_DAYS    = "delayDays";
    private static final String COL_FINE_AMOUNT   = "fineAmount";

    // ───────────────────────────────────────────────────────
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // ─── CREATE TABLES ──────────────────────────────────────
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_FULLNAME + " TEXT, " +
                COL_EMAIL    + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT, " +
                COL_ROLE     + " TEXT DEFAULT 'user')");

        db.execSQL("CREATE TABLE " + TABLE_BOOKS + " (" +
                COL_BOOK_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE    + " TEXT, " +
                COL_AUTHOR   + " TEXT, " +
                COL_CATEGORY + " TEXT, " +
                COL_QUANTITY + " INTEGER)");

        db.execSQL("CREATE TABLE " + TABLE_TRANSACTIONS + " (" +
                COL_TRANS_ID     + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_STUDENT_NAME + " TEXT, " +
                COL_BOOK_TITLE   + " TEXT, " +
                COL_BORROW_DATE  + " TEXT, " +
                COL_RETURN_DATE  + " TEXT, " +
                COL_DELAY_DAYS   + " INTEGER, " +
                COL_FINE_AMOUNT  + " REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        onCreate(db);
    }

    // ════════════════════════════════════════════════════════
    //  USER OPERATIONS
    // ════════════════════════════════════════════════════════

    // INSERT user
    public boolean registerUser(String fullname, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_FULLNAME, fullname);
        values.put(COL_EMAIL,    email);
        values.put(COL_PASSWORD, password);
        values.put(COL_ROLE,     "user");
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    // CHECK login
    public boolean checkLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USERS +
                        " WHERE " + COL_EMAIL + "=? AND " + COL_PASSWORD + "=?",
                new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }
    // CHECK if email already exists
    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USERS +
                        " WHERE " + COL_EMAIL + "=?",
                new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // ════════════════════════════════════════════════════════
    //  BOOK OPERATIONS
    // ════════════════════════════════════════════════════════

    // INSERT book
    public boolean insertBook(String title, String author, String category, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE,    title);
        values.put(COL_AUTHOR,   author);
        values.put(COL_CATEGORY, category);
        values.put(COL_QUANTITY, quantity);
        long result = db.insert(TABLE_BOOKS, null, values);
        db.close();
        return result != -1;
    }

    // UPDATE book
    public boolean updateBook(int bookId, String title, String author, String category, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE,    title);
        values.put(COL_AUTHOR,   author);
        values.put(COL_CATEGORY, category);
        values.put(COL_QUANTITY, quantity);
        int rows = db.update(TABLE_BOOKS, values, COL_BOOK_ID + "=?",
                new String[]{String.valueOf(bookId)});
        db.close();
        return rows > 0;
    }

    // DELETE book
    public boolean deleteBook(int bookId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_BOOKS, COL_BOOK_ID + "=?",
                new String[]{String.valueOf(bookId)});
        db.close();
        return rows > 0;
    }

    // VIEW all books
    public Cursor getAllBooks() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_BOOKS, null);
    }

    // ════════════════════════════════════════════════════════
    //  TRANSACTION OPERATIONS
    // ════════════════════════════════════════════════════════

    // INSERT transaction
    public boolean insertTransaction(String studentName, String bookTitle,
                                     String borrowDate, String returnDate,
                                     int delayDays, double fineAmount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_STUDENT_NAME, studentName);
        values.put(COL_BOOK_TITLE,   bookTitle);
        values.put(COL_BORROW_DATE,  borrowDate);
        values.put(COL_RETURN_DATE,  returnDate);
        values.put(COL_DELAY_DAYS,   delayDays);
        values.put(COL_FINE_AMOUNT,  fineAmount);
        long result = db.insert(TABLE_TRANSACTIONS, null, values);
        db.close();
        return result != -1;
    }

    // VIEW all transactions
    public Cursor getAllTransactions() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_TRANSACTIONS, null);


    }
    // DELETE transaction
    public boolean deleteTransaction(int transId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_TRANSACTIONS, COL_TRANS_ID + "=?",
                new String[]{String.valueOf(transId)});
        db.close();
        return rows > 0;
    }



}