package com.example.projekmobileprog;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;
import java.util.Map;

public class ViewNovelByUserActivity extends AppCompatActivity {

    private Button update_post, delete_post, view_to_manage;
    private TextView view_unovel_title, view_unovel_body, view_unovel_category;
    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_novel_by_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        update_post = findViewById(R.id.update_novel_ui);
        delete_post = findViewById(R.id.delete_novel_ui);
        view_to_manage = findViewById(R.id.view_to_manage_ui);
        view_unovel_title = findViewById(R.id.view_unovel_title_ui);
        view_unovel_body = findViewById(R.id.view_unovel_body_ui);
        view_unovel_category = findViewById(R.id.view_unovel_category_ui);

        databaseManager = new DatabaseManager(this);
        databaseManager.open();

        int novel_id = getIntent().getIntExtra("novel_id", 0);
        List<Map<String, String>> novel_list = databaseManager.getNovelByIdNovel(novel_id);
        String novel_name = novel_list.get(0).get("novel_name");
        String novel_body = novel_list.get(0).get("novel_body");
        String novel_category = novel_list.get(0).get("category_name");

        view_unovel_title.setText(novel_name);
        view_unovel_body.setText(novel_body);
        view_unovel_category.setText("in " + novel_category);

        update_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updatePostAct = new Intent(ViewNovelByUserActivity.this, UpdateNovelActivity.class);
                updatePostAct.putExtra("novel_id", novel_id);
                startActivity(updatePostAct);
            }
        });

        delete_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseManager.deleteNovel(novel_id);
                Toast.makeText(ViewNovelByUserActivity.this, "Novel has been deleted", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        view_to_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        databaseManager.open();
        int novel_id = getIntent().getIntExtra("novel_id", 0);
        List<Map<String, String>> novel_list = databaseManager.getNovelByIdNovel(novel_id);
        String novel_name = novel_list.get(0).get("novel_name");
        String novel_body = novel_list.get(0).get("novel_body");
        String novel_category = novel_list.get(0).get("category_name");
        view_unovel_title.setText(novel_name);
        view_unovel_body.setText(novel_body);
        view_unovel_category.setText(novel_category);
    }
}