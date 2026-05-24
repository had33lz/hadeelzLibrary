package com.example.hadeelzlibrary.models;

public class Transaction {

    private int    transId;
    private String studentName;
    private String bookTitle;
    private String borrowDate;
    private String returnDate;
    private int    delayDays;
    private double fineAmount;

    public Transaction(int transId, String studentName, String bookTitle,
                       String borrowDate, String returnDate,
                       int delayDays, double fineAmount) {
        this.transId     = transId;
        this.studentName = studentName;
        this.bookTitle   = bookTitle;
        this.borrowDate  = borrowDate;
        this.returnDate  = returnDate;
        this.delayDays   = delayDays;
        this.fineAmount  = fineAmount;
    }

    // Getters
    public int    getTransId()     { return transId; }
    public String getStudentName() { return studentName; }
    public String getBookTitle()   { return bookTitle; }
    public String getBorrowDate()  { return borrowDate; }
    public String getReturnDate()  { return returnDate; }
    public int    getDelayDays()   { return delayDays; }
    public double getFineAmount()  { return fineAmount; }

    // Setters
    public void setTransId(int transId)          { this.transId = transId; }
    public void setStudentName(String name)      { this.studentName = name; }
    public void setBookTitle(String title)       { this.bookTitle = title; }
    public void setBorrowDate(String date)       { this.borrowDate = date; }
    public void setReturnDate(String date)       { this.returnDate = date; }
    public void setDelayDays(int days)           { this.delayDays = days; }
    public void setFineAmount(double amount)     { this.fineAmount = amount; }
}