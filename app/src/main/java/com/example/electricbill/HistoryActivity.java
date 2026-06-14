package com.example.electricbill;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {

    DataHelper dbHelper;
    ListView listView;
    String[] displayList;
    String[] idList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dbHelper = new DataHelper(this);
        listView = findViewById(R.id.listViewHistory);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(HistoryActivity.this, DetailActivity.class);
                i.putExtra("id", idList[position]);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    private void refreshList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM electricitybill", null);

        int count = cursor.getCount();
        displayList = new String[count];
        idList = new String[count];

        cursor.moveToFirst();
        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            idList[i] = cursor.getString(0);
            String month = cursor.getString(1);
            String finalcost = cursor.getString(5);
            displayList[i] = month + "      -      RM " + finalcost;
        }
        cursor.close();

        listView.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, displayList));
    }
}