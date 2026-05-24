package com.example.hadeelzlibrary.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hadeelzlibrary.models.Transaction;

import java.util.ArrayList;

public class TransactionAdapter extends ArrayAdapter<Transaction> {

    Context context;
    ArrayList<Transaction> transList;

    public TransactionAdapter(Context context, ArrayList<Transaction> transList) {
        super(context, android.R.layout.simple_list_item_2, transList);
        this.context   = context;
        this.transList = transList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        Transaction trans = transList.get(position);

        TextView text1 = convertView.findViewById(android.R.id.text1);
        TextView text2 = convertView.findViewById(android.R.id.text2);

        text1.setText("transcript:  " + trans.getStudentName() +
                " — " + trans.getBookTitle());

        text2.setText("Borrowed: " + trans.getBorrowDate() +
                "  |  Fine: " + String.format("%.3f OMR", trans.getFineAmount()));

        return convertView;
    }
}