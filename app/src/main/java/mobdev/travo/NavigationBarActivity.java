package mobdev.travo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class NavigationBarActivity extends AppCompatActivity {
    protected BottomNavigationView bottomNavigationView;

    protected void setupBottomNavigation(int selectedItemId) {
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Only highlight if it really exists in the menu:
        if (bottomNavigationView.getMenu().findItem(selectedItemId) != null) {
            bottomNavigationView.setSelectedItemId(selectedItemId);
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            // Navigate to Home if we’re not already there
            if (id == R.id.nav_home && !(this instanceof HomePageActivity)) {
                startActivity(new Intent(this, HomePageActivity.class));
                finish();
                return true;
            }
            // Navigate to Profile if not already and same goes with the settings below
            if (id == R.id.nav_profile && !(this instanceof ProfileActivity)) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            }

            if (id == R.id.nav_settings && !(this instanceof SettingsActivity)) {
                startActivity(new Intent(this, SettingsActivity.class));
                finish();
                return true;
            }

            return false;
        });
    }
}