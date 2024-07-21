package com.example.projekmobileprog;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class CategoryListActivity extends AppCompatActivity {


    private DatabaseManager databaseManager;

    private ListView listView;

    private SimpleCursorAdapter adapter;

    final String[] from = new String[]{DatabaseHelper.COLUMN_ID_CATEGORY,
            DatabaseHelper.COLUMN_NAME_CATEGORY};

    final int[] to = new int[]{R.id.id_category_ui, R.id.name_category_ui};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_category_list);

        databaseManager = new DatabaseManager(this);
        databaseManager.open();
        Cursor cursor = databaseManager.getAllCategories();


        listView = (ListView) findViewById(R.id.list_category_ui);
        listView.setEmptyView(findViewById(R.id.category_empty_ui));

        adapter = new SimpleCursorAdapter(this, R.layout.view_category_record, cursor, from, to, 0);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);
    }
}