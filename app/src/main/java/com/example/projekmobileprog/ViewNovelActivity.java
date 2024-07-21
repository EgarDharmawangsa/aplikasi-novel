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

public class ViewNovelActivity extends AppCompatActivity {

    private Button view_to_dashboard;
    private TextView view_novel_title, view_novel_body, view_novel_author, view_novel_category;
    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_novel);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        view_to_dashboard = findViewById(R.id.view_to_dashboard_ui);
        view_novel_title = findViewById(R.id.view_novel_title_ui);
        view_novel_body = findViewById(R.id.view_novel_body_ui);
        view_novel_author = findViewById(R.id.view_novel_author_ui);
        view_novel_category = findViewById(R.id.view_novel_category_ui);

        databaseManager = new DatabaseManager(this);
        databaseManager.open();

        int novel_id = getIntent().getIntExtra("novel_id", 0);
        List<Map<String, String>> novel_list = databaseManager.getNovelByIdNovel(novel_id);
        String novel_name = novel_list.get(0).get("novel_name");
        String novel_body = novel_list.get(0).get("novel_body");
        String novel_author = novel_list.get(0).get("user_name");
        String novel_category = novel_list.get(0).get("category_name");

        view_novel_title.setText(novel_name);
        view_novel_body.setText(novel_body);
        view_novel_author.setText("by " + novel_author);
        view_novel_category.setText("in " + novel_category);

        view_to_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}