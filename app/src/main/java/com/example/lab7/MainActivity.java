package com.example.lab7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText edtTitle, edtContent, edtDate, edtType;
    Button btnAdd, btnUpdate, btnDelete, btnView;
    ListView lv;
    ArrayList<String> mylist;
    ArrayAdapter<String> TODOadapter;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        edtTitle = findViewById(R.id.edtTitle);
        edtContent = findViewById(R.id.edtContent);
        edtDate = findViewById(R.id.edtDate);
        edtType = findViewById(R.id.edtType);
        //button
        btnAdd = findViewById(R.id.btnAdd);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnView = findViewById(R.id.btnView);

        //Tao listview
        lv = findViewById(R.id.lv);
        mylist = new ArrayList<>();
        TODOadapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mylist);
        lv.setAdapter(TODOadapter);
        //Tao va mo co so du lieu
        sqLiteDatabase = openOrCreateDatabase("TODOLIST.db", MODE_PRIVATE, null);
        //Tao table chua du lieu
        try{
            String sql = "CREATE TABLE TODO(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "TITLE TEXT, CONTENT TEXT, DATE TEXT, TYPE TEXT)";
            sqLiteDatabase.execSQL(sql);
        }catch (Exception e){
            Log.e("ERROR", "Table đã tồn tại");
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String TITLE = edtTitle.getText().toString();
                String CONTENT = edtContent.getText().toString();
                String DATE = edtDate.getText().toString();
                String TYPE = edtType.getText().toString();
                ContentValues values = new ContentValues();
                values.put ("TITLE", TITLE) ;
                values.put("CONTENT", CONTENT);
                values.put ("DATE", DATE);
                values.put ("TYPE", TYPE);
                String msg = "";

                    if (sqLiteDatabase.insert ("TODO", null,values) == - 1) {
                        msg = "Fail to Add Record";
                    }
                    else {
                        msg = "Add Record Successfully";
                    }
                Toast.makeText(MainActivity.this, "msg", Toast.LENGTH_SHORT).show();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String TITLE = edtTitle.getText().toString();
                String CONTENT = edtContent.getText().toString();
                String DATE = edtDate.getText().toString();
                String TYPE = edtType.getText().toString();
                ContentValues values = new ContentValues();
                values.put ("TITLE", TITLE) ;
                values.put ("CONTENT", CONTENT);
                values.put ("DATE", DATE);
                values.put ("TYPE", TYPE);
                int n = sqLiteDatabase.update("TODO", values, "TITLE = ?", new String[]{TITLE});
                String msg = "";
                if (n==0){
                    msg = "No Record to Update";
                }
                else {
                    msg = "Record is Update";
                }
                Toast.makeText(MainActivity.this, "msg", Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String TITLE = edtTitle.getText().toString();
                int n = sqLiteDatabase.delete("TODO", "TITLE = ?", new String[]{TITLE});
                String msg ="";
                if (n==0){
                    msg = "No Record to Delete";
                }
                else {
                    msg = "Record is Delete";
                }
                Toast.makeText(MainActivity.this, "msg", Toast.LENGTH_SHORT).show();
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mylist.clear();
                Cursor c = sqLiteDatabase.query ("TODO", null, null, null, null, null, null );
                c.moveToNext();
                String data = "";
                while (c.isAfterLast() == false) {
                    data = c.getString(0) + " - " + c.getString(1) + " - " + c.getString(2)+ " - " + c.getString(3);
                    c.moveToNext();
                    mylist.add(data);
                }
                c. close();
                TODOadapter.notifyDataSetChanged();
            }
        });
    }
}