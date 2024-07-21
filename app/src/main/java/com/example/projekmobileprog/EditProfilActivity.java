package com.example.projekmobileprog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;
import java.util.Map;

public class EditProfilActivity extends AppCompatActivity {

    private DatabaseManager databaseManager;
    private TextView edit_name, edit_username, edit_password;
    private Button save_edit, edit_to_profil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edit_name = findViewById(R.id.edit_name_ui);
        edit_username = findViewById(R.id.edit_username_ui);
        edit_password = findViewById(R.id.edit_password_ui);
        save_edit = findViewById(R.id.save_edit_ui);
        edit_to_profil = findViewById(R.id.edit_to_profil_ui);

        databaseManager = new DatabaseManager(this);
        databaseManager.open();

        int user_id_login = getIntent().getIntExtra("user_id_login", 0);
        Map<String, String> user_data = databaseManager.getUserbyIdUser(user_id_login);
        edit_name.setText(user_data.get("name_user"));
        edit_username.setText(user_data.get("username_user"));
        String past_username = edit_username.getText().toString();
        edit_password.setText(user_data.get("password_user"));

        save_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data_edit_name = edit_name.getText().toString();
                String data_edit_username = edit_username.getText().toString();
                String data_edit_password = edit_password.getText().toString();
                if (!data_edit_name.isEmpty() && !data_edit_username.isEmpty() && !data_edit_password.isEmpty()) {
                    if (past_username.equals(data_edit_username) || !databaseManager.validationUsername(data_edit_username)) {
                        databaseManager.updateUser(user_id_login, data_edit_name, data_edit_username, data_edit_password);
                        Toast.makeText(EditProfilActivity.this, "Profil has been edited", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(EditProfilActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditProfilActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        edit_to_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}