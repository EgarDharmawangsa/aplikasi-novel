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

public class LoginActivity extends AppCompatActivity {

    private DatabaseManager databaseManager;
    private TextView login_username, login_password;
    private Button login, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        login_username = findViewById(R.id.login_username_ui);
        login_password = findViewById(R.id.login_password_ui);
        login = findViewById(R.id.login_ui);
        register = findViewById(R.id.register_ui);

        databaseManager = new DatabaseManager(this);
        databaseManager.open();

        databaseManager.defaultCategories();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data_login_username = login_username.getText().toString();
                String data_login_password = login_password.getText().toString();
                int user_id_login = databaseManager.getIdUserbyUsername(data_login_username);
                if (!data_login_username.isEmpty() && !data_login_password.isEmpty()) {
                    if (databaseManager.validationLogin(data_login_username, data_login_password)) {
                        Intent dashboardAct = new Intent(LoginActivity.this, DashboardActivity.class);
                        dashboardAct.putExtra("user_id_login", user_id_login);
                        startActivity(dashboardAct);
                    } else {
                        Toast.makeText(LoginActivity.this, "Failed to login", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerAct = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerAct);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        login_username.setText("");
        login_password.setText("");
    }
}