package com.example.projekmobileprog;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity {

    private DatabaseManager databaseManager;
    private TextView register_name, register_username, register_password, register_confirm_password;
    private Button register, register_to_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        register_name = findViewById(R.id.register_name_ui);
        register_username = findViewById(R.id.register_username_ui);
        register_password = findViewById(R.id.register_password_ui);
        register_confirm_password = findViewById(R.id.register_confirm_password_ui);
        register = findViewById(R.id.register_ui);
        register_to_login = findViewById(R.id.register_to_login_ui);

        databaseManager = new DatabaseManager(this);
        databaseManager.open();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data_register_name = register_name.getText().toString();
                String data_register_username = register_username.getText().toString();
                String data_register_password = register_password.getText().toString();
                String data_register_confirm_password = register_confirm_password.getText().toString();

                if (!data_register_name.isEmpty() && !data_register_username.isEmpty() && !data_register_password.isEmpty() && !data_register_confirm_password.isEmpty()) {
                    if (data_register_password.equals(data_register_confirm_password)) {
                        if (!databaseManager.validationUsername(data_register_username)) {
                            databaseManager.insertUser(data_register_name, data_register_username, data_register_password);
                            Toast.makeText(RegisterActivity.this, "Register successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Reconfirm your password", Toast.LENGTH_SHORT).show();
                        register_password.setText("");
                        register_confirm_password.setText("");
                        }
                } else {
                    Toast.makeText(RegisterActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        register_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}