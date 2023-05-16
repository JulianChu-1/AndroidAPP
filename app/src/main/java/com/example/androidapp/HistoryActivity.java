package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;

    private AutoCompleteTextView autoCompleteTextView;
    private List<String> dataList = new ArrayList<>();
    private ArrayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        dbHelper = new MyDatabaseHelper(this);
        autoCompleteTextView = findViewById(R.id.autoCompletetextview);
        String[] context = new String[]{"China","Britain","Russia","Canada","U.S.A","Chile","Germany"};
        adapter = new ArrayAdapter<String>(this, R.layout.item_layout,context);
        autoCompleteTextView.setAdapter(adapter);
    }

    private void loadDataFromDatabase() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {"API_KEY"};
        Cursor cursor = db.query("API_TABLE", projection, null, null, null, null, "ROWID DESC", "5");

        dataList.clear();
        while (cursor.moveToNext()) {
            int columnIndex = cursor.getColumnIndex("API_KEY");
            if (columnIndex != -1) {
                String value = cursor.getString(columnIndex);
                dataList.add(value);
            } else {
                Log.e("MyApp", "Column 'API_KEY' not found in result set");
            }
        }
        cursor.close();
    }
}