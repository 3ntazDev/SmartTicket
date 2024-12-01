package com.example.smartticket;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartticket.R;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // ربط الحقول والأزرار
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        // تنفيذ عملية تسجيل الدخول عند الضغط على زر تسجيل الدخول
        loginButton.setOnClickListener(v -> login());
    }

    private void login() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // التحقق من البيانات المدخلة
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "يرجى إدخال البريد الإلكتروني وكلمة المرور", Toast.LENGTH_SHORT).show();
        } else {
            // هنا يمكنك إضافة منطق تحقق من بيانات الدخول
            // على سبيل المثال، التحقق من البريد الإلكتروني وكلمة المرور
            if (email.equals("admin@domain.com") && password.equals("password123")) {
                // في حالة النجاح، افتح النشاط التالي
                Toast.makeText(this, "تم تسجيل الدخول بنجاح", Toast.LENGTH_SHORT).show();
                // ابدأ النشاط التالي (مثال: الصفحة الرئيسية للمشرف)
                // startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
            } else {
                Toast.makeText(this, "البريد الإلكتروني أو كلمة المرور غير صحيحة", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
