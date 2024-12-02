package com.example.smartticket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

public class AdminActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "TicketPrefs";
    private EditText usernameEditText;
    private Button checkReservationsButton;
    private TableLayout seatsTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        usernameEditText = findViewById(R.id.usernameEditText);
        checkReservationsButton = findViewById(R.id.checkReservationsButton);
        seatsTable = findViewById(R.id.seatsTable);

        checkReservationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredUsername = usernameEditText.getText().toString().trim();
                if (!enteredUsername.isEmpty()) {
                    displayReservedSeats(enteredUsername);
                } else {
                    Toast.makeText(AdminActivity.this, "الرجاء إدخال اسم المستخدم.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void displayReservedSeats(String username) {
        // مسح الجدول قبل إضافة البيانات الجديدة
        seatsTable.removeAllViews();

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Map<String, ?> allReservations = sharedPreferences.getAll();

        boolean hasReservations = false;

        // إضافة صف رأسي للعناوين
        TableRow headerRow = new TableRow(this);
        TextView headerSeat = new TextView(this);
        headerSeat.setText("رقم المقعد");
        headerSeat.setPadding(16, 16, 16, 16);
        headerRow.addView(headerSeat);
        seatsTable.addView(headerRow);

        // عرض المقاعد المحجوزة
        for (Map.Entry<String, ?> entry : allReservations.entrySet()) {
            if (entry.getValue() instanceof String && entry.getValue().equals(username)) {
                hasReservations = true;

                TableRow row = new TableRow(this);
                TextView seatNumber = new TextView(this);
                seatNumber.setText(entry.getKey());
                seatNumber.setPadding(16, 16, 16, 16);
                row.addView(seatNumber);
                seatsTable.addView(row);
            }
        }

        if (!hasReservations) {
            Toast.makeText(this, "لا توجد مقاعد محجوزة لهذا المستخدم.", Toast.LENGTH_SHORT).show();
        }
    }


}
