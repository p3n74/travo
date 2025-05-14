package mobdev.travo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends NavigationBarActivity {

    RecyclerView rvDestinations;
    DestinationAdapter destinationAdapter;
    List<DestinationModel> destinationList;
    UserDBHelper dbHelper;
    TextView tvWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);

        setupBottomNavigation(R.id.nav_home);

        tvWelcome = findViewById(R.id.tvWelcome);
        rvDestinations = findViewById(R.id.rvDestinations);
        dbHelper = new UserDBHelper(this);

// Set username if available (same as before)
        String email = getSharedPreferences("user_session", MODE_PRIVATE)
                .getString("email", null);
        if (email != null) {
            Cursor c = dbHelper.getUserByEmail(email);
            if (c != null && c.moveToFirst()) {
                String name = c.getString(c.getColumnIndexOrThrow(UserDBHelper.COL_NAME));
                tvWelcome.setText("Welcome, " + name + "!");
                c.close();
            }
        }

// Now retrieve DestinationModel objects via the new method
        destinationList = dbHelper.getAllDestinationsAsList(this);

// Set up RecyclerView
        destinationAdapter = new DestinationAdapter(this, destinationList);
        rvDestinations.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvDestinations.setAdapter(destinationAdapter);

        EditText searchInput = findViewById(R.id.tvSearchPrompt);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                destinationAdapter.getFilter().filter(s);
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });


// FAB for sharing journeys
        findViewById(R.id.fabShareJourney).setOnClickListener(v -> {
            startActivity(new Intent(this, ShareJourneyActivity.class));
        });
    }
}