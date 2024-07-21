package com.example.projekmobileprog;

import android.database.Cursor;
import android.os.Bundle;
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

public class InsertNovelActivity extends AppCompatActivity {

    private Spinner spinner_category;
    private TextView insert_title, insert_body, insert_author;
    private Button post, insert_to_manage;
    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_insert_novel);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        spinner_category = findViewById(R.id.spinner_category_ui);
        insert_title = findViewById(R.id.insert_title_ui);
        insert_author = findViewById(R.id.insert_author_ui);
        insert_body = findViewById(R.id.insert_body_ui);
        post = findViewById(R.id.post_ui);
        insert_to_manage = findViewById(R.id.insert_to_manage_ui);

        databaseManager = new DatabaseManager(this);
        databaseManager.open();

        int user_id_login = getIntent().getIntExtra("user_id_login", 0);
        Map<String, String> user_data = databaseManager.getUserbyIdUser(user_id_login);
        insert_author.setText("by " + user_data.get("name_user"));

        loadSpinnerCategory();

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data_insert_title = insert_title.getText().toString();
                String data_insert_body = insert_body.getText().toString();
                String data_selected_category = spinner_category.getSelectedItem().toString();
                int data_insert_id_category = Integer.parseInt(data_selected_category.split("\\|")[0].trim());
                if (!data_insert_title.isEmpty() && !data_insert_body.isEmpty()) {
                    databaseManager.insertNovel(data_insert_title, data_insert_body, data_insert_id_category, user_id_login);
                    Toast.makeText(InsertNovelActivity.this, "Novel has been posted", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(InsertNovelActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        insert_to_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadSpinnerCategory() {
        List<String> categories = databaseManager.getSpinCategories();
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(categoryAdapter);
    }
}