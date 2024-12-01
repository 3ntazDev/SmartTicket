package com.example.smartticket;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartticket.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // تطبيق الأنيميشن على صورة الشعار
        ImageView logoImageView = findViewById(R.id.logoImageView);
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logoImageView.startAnimation(fadeInAnimation);

        // الانتقال إلى صفحة تسجيل الدخول بعد 3 ثوانٍ
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, SplashActivity.class);
            startActivity(intent);
            finish();
        }, 3000); // 3000ms = 3 ثوانٍ
    }
}
