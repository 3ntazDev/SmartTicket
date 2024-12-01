package com.example.smartticket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // ربط العناصر بالـ XML
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // قراءة البريد وكلمة المرور من الـ EditText
                String enteredEmail = emailEditText.getText().toString().trim();
                String enteredPassword = passwordEditText.getText().toString().trim();

                // طباعة القيم المدخلة للتحقق
                Log.d("LoginActivity", "Entered Email: " + enteredEmail);
                Log.d("LoginActivity", "Entered Password: " + enteredPassword);

                // التحقق من البريد وكلمة المرور
                if (isValidUser(enteredEmail, enteredPassword)) {
                    // إذا كانت البيانات صحيحة
                    Toast.makeText(LoginActivity.this, "تسجيل الدخول بنجاح!", Toast.LENGTH_SHORT).show();

                    // تحديد معرف المستخدم (يمكن أن يكون البريد أو معرف فريد آخر)
                    String loggedInUserId = enteredEmail; // استخدم البريد المدخل كمعرف للمستخدم

                    // الانتقال إلى الصفحة الرئيسية مع تمرير معرف المستخدم
                    Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                    intent.putExtra("USER_ID", loggedInUserId); // تمرير معرف المستخدم
                    startActivity(intent);
                    finish();  // إنهاء صفحة تسجيل الدخول
                } else {
                    // إذا كانت البيانات غير صحيحة
                    Toast.makeText(LoginActivity.this, "البريد الإلكتروني أو كلمة المرور غير صحيحة", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // دالة للتحقق من صحة المستخدم
    private boolean isValidUser(String email, String password) {
        String correctEmail1 = getString(R.string.user_email);
        String correctPassword1 = getString(R.string.user_password);

        String correctEmail2 = getString(R.string.user_email_2);
        String correctPassword2 = getString(R.string.user_password_2);

        return (email.equals(correctEmail1) && password.equals(correctPassword1)) ||
                (email.equals(correctEmail2) && password.equals(correctPassword2));
    }
}