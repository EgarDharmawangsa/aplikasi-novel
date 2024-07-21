package com.example.projekmobileprog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;
import java.util.Map;

public class ProfilActivity extends AppCompatActivity {

    private DatabaseManager databaseManager;
    private TextView profil_id, profil_name, profil_username;
    private Button update_profil, log_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        profil_id = findViewById(R.id.profil_id_ui);
        profil_name = findViewById(R.id.profil_name_ui);
        profil_username = findViewById(R.id.profil_username_ui);
        update_profil = findViewById(R.id.update_profile_ui);
        log_out = findViewById(R.id.log_out_ui);

        databaseManager = new DatabaseManager(this);
        databaseManager.open();

        int user_id_login = getIntent().getIntExtra("user_id_login", 0);
        Map<String, String> user_data = databaseManager.getUserbyIdUser(user_id_login);
        profil_id.setText(user_data.get("id_user"));
        profil_name.setText(user_data.get("name_user"));
        profil_username.setText(user_data.get("username_user"));

        update_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editProfilAct = new Intent(ProfilActivity.this, EditProfilActivity.class);
                editProfilAct.putExtra("user_id_login", user_id_login);
                startActivity(editProfilAct);
            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Intent toLoginAct = new Intent(ProfilActivity.this, LoginActivity.class);
                toLoginAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(toLoginAct);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        databaseManager.open();
        int user_id_login = getIntent().getIntExtra("user_id_login", 0);
        Map<String, String> user_data = databaseManager.getUserbyIdUser(user_id_login);
        profil_id.setText(user_data.get("id_user"));
        profil_name.setText(user_data.get("name_user"));
        profil_username.setText(user_data.get("username_user"));
        databaseManager.close();
    }
}