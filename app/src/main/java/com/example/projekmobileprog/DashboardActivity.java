package com.example.projekmobileprog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity implements NovelRVAdapter.OnItemClickListener {

    private DatabaseManager databaseManager;
    private Button profil, list_author, list_category, manage_post;
    private TextView greeting;
    private RecyclerView recyclerView;
    private NovelRVAdapter novelRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        profil = findViewById(R.id.profil_ui);
        list_author = findViewById(R.id.list_author_ui);
        list_category = findViewById(R.id.list_category_ui);
        greeting = findViewById(R.id.greeting_ui);
        manage_post = findViewById(R.id.manage_post_ui);

        recyclerView = findViewById(R.id.recycler_view_novel);

        databaseManager = new DatabaseManager(this);
        databaseManager.open();

        int user_id_login = getIntent().getIntExtra("user_id_login", 0);
        Map<String, String> user_data = databaseManager.getUserbyIdUser(user_id_login);
        greeting.setText("Hello, " + user_data.get("name_user"));

        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profilAct = new Intent(DashboardActivity.this, ProfilActivity.class);
                profilAct.putExtra("user_id_login", user_id_login);
                startActivity(profilAct);
            }
        });

        manage_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent managePostAct = new Intent(DashboardActivity.this, NovelListActivity.class);
                managePostAct.putExtra("user_id_login", user_id_login);
                startActivity(managePostAct);
            }
        });

        list_author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userListAct = new Intent(DashboardActivity.this, UserListActivity.class);
                startActivity(userListAct);
            }
        });

        list_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent categoryAct = new Intent(DashboardActivity.this, CategoryListActivity.class);
                startActivity(categoryAct);
            }
        });

        List<Map<String, String>> all_novel_list = databaseManager.getAllNovels();
        novelRVAdapter = new NovelRVAdapter(this, all_novel_list);
        novelRVAdapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(novelRVAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        databaseManager.open();
        int user_id_login = getIntent().getIntExtra("user_id_login", 0);
        Map<String, String> user_data = databaseManager.getUserbyIdUser(user_id_login);
        greeting.setText("Hello, " + user_data.get("name_user"));
        List<Map<String, String>> all_novel_list = databaseManager.getAllNovels();
        novelRVAdapter.updateData(all_novel_list);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseManager != null) {
            databaseManager.close();
        }
    }

    @Override
    public void onItemClick(int novelId) {
        Intent intent = new Intent(DashboardActivity.this, ViewNovelActivity.class);
        intent.putExtra("novel_id", novelId);
        startActivity(intent);
    }
}