package com.example.smartticket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HomePageActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "TicketPrefs";
    private Map<String, String> seatReservations = new HashMap<>();
    private GridLayout seatsGrid;
    private String userId; // معرف المستخدم

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        seatsGrid = findViewById(R.id.seatsGrid);
        Button bookButton = findViewById(R.id.bookButton);
        Button viewTicketsButton = findViewById(R.id.viewTicketsButton);
        Button logoutButton = findViewById(R.id.logoutButton);
        Button cancelReservationButton = findViewById(R.id.cancelReservationButton);

        // استرداد معرف المستخدم
        userId = getIntent().getStringExtra("USER_ID");

        // استرداد الحجوزات المحفوظة
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        seatReservations = new HashMap<>();
        for (Map.Entry<String, ?> entry : sharedPreferences.getAll().entrySet()) {
            if (entry.getValue() instanceof String) { // التحقق من أن القيمة هي String
                seatReservations.put(entry.getKey(), (String) entry.getValue());
            }
        }

        createSeatButtons();

        // حجز المقاعد
        bookButton.setOnClickListener(view -> {
            saveReservations();
            Toast.makeText(HomePageActivity.this, "تم حجز المقاعد بنجاح!", Toast.LENGTH_SHORT).show();
            createSeatButtons(); // تحديث الأزرار بعد الحجز
        });

        // استعراض المقاعد المحجوزة
        viewTicketsButton.setOnClickListener(view -> {
            StringBuilder seats = new StringBuilder("المقاعد المحجوزة:\n");
            boolean hasReservations = false;
            for (Map.Entry<String, String> entry : seatReservations.entrySet()) {
                if (entry.getValue().equals(userId)) {
                    seats.append(entry.getKey()).append("\n");
                    hasReservations = true;
                }
            }
            if (!hasReservations) {
                seats.append("لا توجد مقاعد محجوزة");
            }
            Toast.makeText(HomePageActivity.this, seats.toString(), Toast.LENGTH_LONG).show();
        });

        // تسجيل الخروج
        logoutButton.setOnClickListener(view -> {
            Intent intent = new Intent(HomePageActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // إنهاء HomePageActivity
        });

        // إلغاء الحجز
        cancelReservationButton.setOnClickListener(view -> {
            boolean hasCancelled = false;
            Set<String> seatsToCancel = new HashSet<>();
            for (Map.Entry<String, String> entry : seatReservations.entrySet()) {
                if (entry.getValue().equals(userId)) {
                    seatsToCancel.add(entry.getKey());
                    hasCancelled = true;
                }
            }
            for (String seat : seatsToCancel) {
                seatReservations.remove(seat);
            }
            if (hasCancelled) {
                saveReservations();
                Toast.makeText(HomePageActivity.this, "تم إلغاء جميع الحجوزات!", Toast.LENGTH_SHORT).show();
                createSeatButtons(); // تحديث الواجهة
            } else {
                Toast.makeText(HomePageActivity.this, "لا توجد حجوزات للإلغاء", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveReservations() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        for (Map.Entry<String, String> entry : seatReservations.entrySet()) {
            editor.putString(entry.getKey(), entry.getValue());
        }
        editor.apply();
    }

    private void createSeatButtons() {
        seatsGrid.removeAllViews(); // إزالة الأزرار السابقة
        for (int i = 1; i <= 10; i++) {
            Button seatButton = new Button(this);
            String seatTag = "Seat" + i;
            seatButton.setText(String.valueOf(i));
            seatButton.setTag(seatTag);

            // تحديد لون الأزرار بناءً على الحجوزات
            if (seatReservations.containsKey(seatTag)) {
                if (seatReservations.get(seatTag).equals(userId)) {
                    seatButton.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light)); // اللون الأزرق
                } else {
                    seatButton.setEnabled(false);
                    seatButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light)); // اللون الأحمر
                }
            } else {
                seatButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light)); // اللون الأخضر
            }

            seatButton.setOnClickListener(view -> {
                String seatNumber = (String) view.getTag();
                if (seatReservations.containsKey(seatNumber)) {
                    if (seatReservations.get(seatNumber).equals(userId)) {
                        // إلغاء الحجز الخاص بالمستخدم
                        seatReservations.remove(seatNumber);
                        view.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light)); // اللون الأخضر
                        Toast.makeText(HomePageActivity.this, "تم إلغاء حجز المقعد " + seatNumber, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // حجز المقعد
                    seatReservations.put(seatNumber, userId);
                    view.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light)); // اللون الأزرق
                    Toast.makeText(HomePageActivity.this, "تم حجز المقعد " + seatNumber, Toast.LENGTH_SHORT).show();
                }
            });
            seatsGrid.addView(seatButton);
        }
    }
}
