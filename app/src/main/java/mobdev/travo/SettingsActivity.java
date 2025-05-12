package com.example.finalproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings); // XML must be named correctly

        findViewById(R.id.back_button).setOnClickListener(v -> finish());
        findViewById(R.id.subscribe_button).setOnClickListener(v ->
                Toast.makeText(this, "Subscribed to Travo+", Toast.LENGTH_SHORT).show()
        );
    }
}
