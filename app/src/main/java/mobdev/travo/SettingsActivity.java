package mobdev.travo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class SettingsActivity extends NavigationBarActivity {

    TextView profile;
    TextView notification;
    TextView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings); // XML must be named correctly

        // Once again, navigation part nani
        setupBottomNavigation(R.id.nav_settings);

        // settings functions

        profile = findViewById(R.id.profile);
        notification = findViewById(R.id.notif);
        logout = findViewById(R.id.log_out);


        findViewById(R.id.back_button).setOnClickListener(v -> finish());
        findViewById(R.id.subscribe_button).setOnClickListener(v ->
                Toast.makeText(this, "Subscribed to Travo+", Toast.LENGTH_SHORT).show()
        );

        profile.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();
        });

        notification.setOnClickListener(v ->
                Toast.makeText(this, "Notifications Enabled!", Toast.LENGTH_SHORT).show());

        logout.setOnClickListener(v -> {
            // Clear SharedPreferences
            getSharedPreferences("user_session", MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();

            // Go back to LoginActivity
            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clears back stack
            startActivity(intent);
            finish();
        });
    }
}
