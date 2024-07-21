package com.example.projekmobileprog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;
import java.util.Map;

public class UpdateNovelActivity extends AppCompatActivity {

    private Spinner spinner_category;
    private TextView update_title, update_body, update_author;
    private Button save_novel, update_to_manage;
    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_novel);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        spinner_category = findViewById(R.id.spinner_update_category_ui);
        update_title = findViewById(R.id.update_title_ui);
        update_body = findViewById(R.id.update_body_ui);
        update_author = findViewById(R.id.update_author_ui);
        save_novel = findViewById(R.id.save_novel_ui);
        update_to_manage = findViewById(R.id.update_to_manage_ui);

        databaseManager = new DatabaseManager(this);
        databaseManager.open();

        int novel_id = getIntent().getIntExtra("novel_id", 0);
        List<Map<String, String>> novel_list = databaseManager.getNovelByIdNovel(novel_id);
        String novel_name = novel_list.get(0).get("novel_name");
        String novel_body = novel_list.get(0).get("novel_body");
        String name_category = novel_list.get(0).get("category_name");
        String name_user = novel_list.get(0).get("user_name");
        int user_id_fk = Integer.parseInt(novel_list.get(0).get("user_id_fk"));

        loadSpinnerCategory(name_category);
        update_title.setText(novel_name);
        update_body.setText(novel_body);
        update_author.setText("by " + name_user);

        save_novel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data_update_title = update_title.getText().toString();
                String data_update_body = update_body.getText().toString();
                String data_selected_category = spinner_category.getSelectedItem().toString();
                int data_update_id_category = Integer.parseInt(data_selected_category.split("\\|")[0].trim());
                if (!data_update_title.isEmpty() || !data_update_body.isEmpty()) {
                    databaseManager.updateNovel(novel_id, data_update_title, data_update_body, data_update_id_category, user_id_fk);
                    Toast.makeText(UpdateNovelActivity.this, "Novel has been updated", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(UpdateNovelActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        update_to_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadSpinnerCategory(String currentCategory) {
        List<String> categories = databaseManager.getSpinCategories();
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(categoryAdapter);

        if (currentCategory != null) {
            for (int i = 0; i < categories.size(); i++) {
                String category = categories.get(i);
                if (category.contains(currentCategory)) {
                    spinner_category.setSelection(i);
                    break;
                }
            }
        }
    }
}