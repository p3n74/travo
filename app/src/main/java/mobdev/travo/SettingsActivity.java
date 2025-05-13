package mobdev.travo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class SettingsActivity extends NavigationBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings); // XML must be named correctly

        // Once again, navigation part nani
        setupBottomNavigation(R.id.nav_settings);

        findViewById(R.id.back_button).setOnClickListener(v -> finish());
        findViewById(R.id.subscribe_button).setOnClickListener(v ->
                Toast.makeText(this, "Subscribed to Travo+", Toast.LENGTH_SHORT).show()
        );
    }
}
