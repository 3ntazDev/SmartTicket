package com.example.smartticket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        dbHelper = new DatabaseHelper(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // التحقق من بيانات الدخول
                String userRole = dbHelper.getUserRole(username, password);

                if (userRole != null) {
                    // إذا كان الدور "admin"
                    if (userRole.equals("admin")) {
                        Intent intent = new Intent(LoginActivity.this, AdminPageActivity.class);
                        startActivity(intent);
                    } else if (userRole.equals("user")) {
                        Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                        startActivity(intent);
                    }
                } else {
                    // إذا كانت البيانات خاطئة
                    Toast.makeText(LoginActivity.this, "اسم المستخدم أو كلمة المرور خاطئة", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
