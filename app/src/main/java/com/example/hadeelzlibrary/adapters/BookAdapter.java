package com.example.hadeelzlibrary.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hadeelzlibrary.models.Book;

import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter<Book> {

    Context       context;
    ArrayList<Book> bookList;

    public BookAdapter(Context context, ArrayList<Book> bookList) {
        super(context, android.R.layout.simple_list_item_2, bookList);
        this.context  = context;
        this.bookList = bookList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        Book book = bookList.get(position);

        TextView text1 = convertView.findViewById(android.R.id.text1);
        TextView text2 = convertView.findViewById(android.R.id.text2);

        text1.setText("Booke :  " + book.getTitle() + " — " + book.getAuthor());
        text2.setText("Category: " + book.getCategory() +
                "  |  Quantity: " + book.getQuantity());

        return convertView;
    }
}