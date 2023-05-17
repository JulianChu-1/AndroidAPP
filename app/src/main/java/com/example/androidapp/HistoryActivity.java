package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
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

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        dbHelper = new MyDatabaseHelper(this);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HistoryAdapter(dataList);
        recyclerView.setAdapter(adapter);

        loadDataFromDatabase();
    }


    private void loadDataFromDatabase() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {"Message", "_TYPE", "_DATETIME","BOT_NAME"};
        String selection = "API_KEY = ?";
        String[] selectionArgs = {SettingActivity.m_value}; // Replace currentAPIKey with the actual API_KEY value

        Cursor cursor = db.query(
                "Dialogue_TABLE",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                "_DATETIME ASC"
        );

        dataList.clear();
        while (cursor.moveToNext()) {
            int messageIndex = cursor.getColumnIndex("Message");
            int typeIndex = cursor.getColumnIndex("_TYPE");
            int nameIndex = cursor.getColumnIndex("BOT_NAME");
            if (messageIndex != -1 && typeIndex != -1) {
                String message = cursor.getString(messageIndex);
                String type = cursor.getString(typeIndex);
                String name = cursor.getString(nameIndex);
                if(type.equals("ME")){
                    dataList.add(type + ": " + message);
                } else if (type.equals("BOT")) {
                    dataList.add(name + ": " + message);
                }
                else{
                    dataList.add(type + ": " + message);
                }
            } else {
                Log.e("MyApp", "Columns 'Message' or '_TYPE' not found in result set");
            }
        }
        cursor.close();
    }

    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("last_close_time", System.currentTimeMillis());
        editor.apply();
        Intent serviceIntent = new Intent(this, MyService.class);
        startService(serviceIntent);
    }

    protected void onResume(){
        Intent serviceIntent = new Intent(this, MyService.class);
        stopService(serviceIntent);
        super.onResume();
    }
}
