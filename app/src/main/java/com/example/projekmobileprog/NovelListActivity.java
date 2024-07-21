package com.example.projekmobileprog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Map;

public class NovelListActivity extends AppCompatActivity {


    private DatabaseManager databaseManager;

    private ListView listView;

    private SimpleCursorAdapter adapter;

    private FloatingActionButton insert_novel;

    final String[] from = new String[]{"_id", "novel_name", "category_name"};

    final int[] to = new int[]{R.id.id_unovel_item_ui, R.id.name_unovel_item_ui, R.id.category_unovel_item_ui};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_novel_list);

        databaseManager = new DatabaseManager(this);
        databaseManager.open();

        int user_id_login = getIntent().getIntExtra("user_id_login", 0);

        Cursor cursor = databaseManager.getAllNovelsByIdUser(user_id_login);

        listView = (ListView) findViewById(R.id.list_novel_ui);
        listView.setEmptyView(findViewById(R.id.novel_empty_ui));
        insert_novel = (FloatingActionButton) findViewById(R.id.insert_novel_ui);

        adapter = new SimpleCursorAdapter(this, R.layout.view_novel_record, cursor, from, to, 0);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Cursor selectedCursor = (Cursor) adapter.getItem(position);
            int novel_id = selectedCursor.getInt(selectedCursor.getColumnIndexOrThrow("_id"));
            Intent viewNovelByUserAct = new Intent(NovelListActivity.this, ViewNovelByUserActivity.class);
            viewNovelByUserAct.putExtra("novel_id", novel_id);
            startActivity(viewNovelByUserAct);
        });
        databaseManager.close();

        insert_novel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent insertNovelAct = new Intent(NovelListActivity.this, InsertNovelActivity.class);
                insertNovelAct.putExtra("user_id_login", user_id_login);
                startActivity(insertNovelAct);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        databaseManager.open();
        int user_id_login = getIntent().getIntExtra("user_id_login", 0);
        loadData(user_id_login);
        databaseManager.close();
    }

    private void loadData(int id_user) {
        Cursor cursor = databaseManager.getAllNovelsByIdUser(id_user);
        if (adapter == null) {
            adapter = new SimpleCursorAdapter(this, R.layout.view_novel_record, cursor, from, to, 0);
            listView.setAdapter(adapter);
        } else {
            adapter.changeCursor(cursor);
            adapter.notifyDataSetChanged();
        }
    }
}