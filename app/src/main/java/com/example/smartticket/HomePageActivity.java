package com.example.smartticket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;

public class HomePageActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "TicketPrefs";
    private Set<String> reservedSeats = new HashSet<>();
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

        // استرداد المقاعد المحجوزة لهذا المستخدم فقط
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        reservedSeats = sharedPreferences.getStringSet(userId + "_RESERVED_SEATS", new HashSet<>());

        createSeatButtons();

        // حجز المقاعد
        bookButton.setOnClickListener(view -> {
            if (reservedSeats.isEmpty()) {
                Toast.makeText(HomePageActivity.this, "الرجاء اختيار مقاعد للحجز", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putStringSet(userId + "_RESERVED_SEATS", reservedSeats);
                editor.apply();
                Toast.makeText(HomePageActivity.this, "تم حجز المقاعد بنجاح!", Toast.LENGTH_SHORT).show();
                createSeatButtons(); // تحديث الأزرار بعد الحجز
            }
        });

        // استعراض المقاعد المحجوزة
        viewTicketsButton.setOnClickListener(view -> {
            StringBuilder seats = new StringBuilder("المقاعد المحجوزة:\n");
            if (reservedSeats.isEmpty()) {
                seats.append("لا توجد مقاعد محجوزة");
            } else {
                for (String seat : reservedSeats) {
                    seats.append(seat).append("\n");
                }
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
            if (!reservedSeats.isEmpty()) {
                reservedSeats.clear(); // إلغاء جميع الحجوزات
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putStringSet(userId + "_RESERVED_SEATS", reservedSeats);
                editor.apply();
                Toast.makeText(HomePageActivity.this, "تم إلغاء جميع الحجوزات!", Toast.LENGTH_SHORT).show();
                createSeatButtons(); // تحديث الواجهة
            } else {
                Toast.makeText(HomePageActivity.this, "لا توجد حجوزات للإلغاء", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createSeatButtons() {
        seatsGrid.removeAllViews(); // إزالة الأزرار السابقة
        for (int i = 1; i <= 10; i++) {
            Button seatButton = new Button(this);
            seatButton.setText(String.valueOf(i));
            seatButton.setTag("Seat" + i);

            // تغيير لون الأزرار
            if (reservedSeats.contains("Seat" + i)) {
                seatButton.setEnabled(false);
                seatButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light)); // اللون الأحمر
            } else {
                seatButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light)); // اللون الأخضر
            }

            seatButton.setOnClickListener(view -> {
                String seatNumber = (String) view.getTag();
                if (reservedSeats.contains(seatNumber)) {
                    // إلغاء الحجز
                    reservedSeats.remove(seatNumber);
                    view.setEnabled(true);
                    view.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light)); // إعادة اللون الأخضر
                    Toast.makeText(HomePageActivity.this, "تم إلغاء حجز المقعد " + seatNumber, Toast.LENGTH_SHORT).show();
                } else {
                    // حجز المقعد
                    reservedSeats.add(seatNumber);
                    view.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light)); // اللون الأزرق
                    Toast.makeText(HomePageActivity.this, "تم حجز المقعد " + seatNumber, Toast.LENGTH_SHORT).show();
                }
            });
            seatsGrid.addView(seatButton);
        }
    }
}