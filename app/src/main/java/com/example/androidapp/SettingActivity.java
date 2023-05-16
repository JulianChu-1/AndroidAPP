package com.example.androidapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {

    private EditText editKey;
    private MyDatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private List<String> dataList = new ArrayList<>();
    private PopupWindow popupWindow;
    private MyAdapter adapter_1;
    public static String m_value;
    public static boolean m_signal = false;
    private AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        editKey = findViewById(R.id.edit_key);
        dbHelper = new MyDatabaseHelper(this);
        recyclerView = findViewById(R.id.recycler_view);
        loadDataFromDatabase();
        initRecyclerView();
        iniEditText(editKey);

        Button btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(v -> {
            String value = editKey.getText().toString();
            saveDataToDatabase(value);
            m_value = value;
            m_signal = true;
            loadDataFromDatabase();
            recyclerView.setAdapter(adapter_1);
            recyclerView.getAdapter().notifyDataSetChanged();
            editKey.setText("");
            AlertShow();//提示框
        });
    }

    private void AlertShow(){
        builder = new AlertDialog.Builder(SettingActivity.this);

        builder.setTitle("提示");
        builder.setMessage("SecretKey输入成功");
        builder.setPositiveButton("确定", (dialog, which) -> {
            Toast.makeText(SettingActivity.this, "开始使用MobileChat吧！",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SettingActivity.this,MainActivity.class);
            startActivity(intent);
        });
        // builder.setNeutralButton("取消", null);
        builder.show();
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new MyAdapter(dataList, item -> editKey.setText(item));
        recyclerView.setAdapter(adapter);

        adapter_1 = new MyAdapter(dataList, item -> {
            editKey.setText(item);
            dismissPopupWindow();
        });
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

    // 存入数据到数据库
    private void saveDataToDatabase(String value) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("API_KEY", value);
        db.insertWithOnConflict("API_TABLE", null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }
    
    private void iniEditText(EditText e) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM API_TABLE";
        Cursor cursor_1 = db.rawQuery(query, null);
        cursor_1.moveToFirst();
        int count = cursor_1.getInt(0);
        cursor_1.close();
        if (count > 0) {
            String[] projection = {"API_KEY"};
            Cursor cursor = db.query("API_TABLE", projection, null, null, null, null, "ROWID DESC", "1");
            cursor.moveToLast();
            int columnIndex = cursor.getColumnIndex("API_KEY");
            if (columnIndex != -1) {
                String value = cursor.getString(columnIndex);
                e.setText(value);
            } else {
                Log.e("MyApp", "Column 'API_KEY' not found the last key");
            }
            cursor.close();
        } else {
            e.setText(" ");
        }

        e.setFocusable(true);
        e.setFocusableInTouchMode(true);
        e.setOnClickListener(v -> showPopupWindow());
    }
    private void showPopupWindow() {
        if (popupWindow == null) {
            View contentView = LayoutInflater.from(this).inflate(R.layout.popup_window, null);
            RecyclerView recyclerView = contentView.findViewById(R.id.recycler_view_pop);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter_1);
            popupWindow = new PopupWindow(contentView, editKey.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
        }
        popupWindow.showAsDropDown(editKey);
    }
    // ERROR ERROR ERROR

    private void dismissPopupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

}