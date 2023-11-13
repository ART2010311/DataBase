package com.example.database;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import android.database.Cursor;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private EditText nameEditText;
    private EditText ageEditText;

    private EditText rowid;

    private Databasehelper databaseHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameEditText = findViewById(R.id.Fname);
        ageEditText = findViewById(R.id.Age);
        rowid = findViewById(R.id.Rowid);
        Button submit = findViewById(R.id.ADD);
        Button read = findViewById(R.id.button);
        Button update = findViewById(R.id.button2);
        Button delete = findViewById(R.id.button3);
        listView = findViewById(R.id.ListView);
        databaseHelper = new Databasehelper(this);


        submit.setOnClickListener(v -> insertUserData());
        read.setOnClickListener(v -> getUserData());
        update.setOnClickListener(v -> updateUserData());
        delete.setOnClickListener(v -> deleteUserData());
    }

    private void insertUserData() {
        String name = nameEditText.getText().toString().trim();
        String ageStr = ageEditText.getText().toString().trim();

        if (!name.isEmpty() && !ageStr.isEmpty()) {
            int age = Integer.parseInt(ageStr); // Convert age to an integer

            SQLiteDatabase db = databaseHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("age", age);

            long newRowId = databaseHelper.insertUserData(name, age);

            if (newRowId != -1) {
                // Insert successful
                nameEditText.setText(""); // Clear the EditText
                ageEditText.setText(""); // Clear the EditText
                Toast.makeText(this, "Insertion success", Toast.LENGTH_SHORT).show();
            } else {
                // Insert failed
                Toast.makeText(this, "Insertion failed", Toast.LENGTH_SHORT).show();
            }

            db.close();
        } else {
            // Display an error message or toast to inform the user
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void getUserData() {
        List<String> userDataList = new ArrayList<>();
        Cursor cursor = databaseHelper.getAllUserData();

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") int age = cursor.getInt(cursor.getColumnIndex("age"));
                String userData = "Name: " + name + ", Age: " + age;
                userDataList.add(userData);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // Create an ArrayAdapter to display the data in a ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userDataList);
        listView.setAdapter(adapter);
    }

    private void updateUserData() {
        String name = nameEditText.getText().toString().trim();
        String ageStr = ageEditText.getText().toString().trim();
        String idToUpdate = rowid.getText().toString().trim();

        if (!name.isEmpty() && !ageStr.isEmpty()) {
            int id = Integer.parseInt(idToUpdate); // Convert age to an integer
            int age = Integer.parseInt(ageStr);
            //SQLiteDatabase db = databaseHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("age", age);
            values.put("id", id);

            int rowsAffected = databaseHelper.updateUserData(id, name, age);

            if (rowsAffected > 0) {
                // Update successful
                Toast.makeText(this, "Update successful", Toast.LENGTH_SHORT).show();
            } else {
                // Update failed (record with the specified ID not found)
                Toast.makeText(this, "Update failed (record with the specified ID not found)", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void deleteUserData() {
        // Inside your activity, such as MainActivity.java

        try (Databasehelper dbHelper = new Databasehelper(this)) {
            int idToDelete = Integer.parseInt(rowid.getText().toString().trim());// Replace with the ID of the record you want to delete

            int rowsAffected = dbHelper.deleteUserData(idToDelete);


            if (rowsAffected > 0) {
                // Deletion successful
                Toast.makeText(this, "delete successful", Toast.LENGTH_SHORT).show();
            } else {
                // Deletion failed (record with the specified ID not found)
                Toast.makeText(this, "delete failed (record with the specified ID not found)", Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
            // Handle exceptions, such as SQLiteException
            e.printStackTrace();
        }

    }
}




