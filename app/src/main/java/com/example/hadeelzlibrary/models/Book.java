package com.example.hadeelzlibrary.models;

public class Book {

    private int    bookId;
    private String title;
    private String author;
    private String category;
    private int    quantity;

    public Book(int bookId, String title, String author, String category, int quantity) {
        this.bookId   = bookId;
        this.title    = title;
        this.author   = author;
        this.category = category;
        this.quantity = quantity;
    }

    // Getters
    public int    getBookId()   { return bookId; }
    public String getTitle()    { return title; }
    public String getAuthor()   { return author; }
    public String getCategory() { return category; }
    public int    getQuantity() { return quantity; }

    // Setters
    public void setBookId(int bookId)       { this.bookId = bookId; }
    public void setTitle(String title)      { this.title = title; }
    public void setAuthor(String author)    { this.author = author; }
    public void setCategory(String cat)     { this.category = cat; }
    public void setQuantity(int quantity)   { this.quantity = quantity; }
}