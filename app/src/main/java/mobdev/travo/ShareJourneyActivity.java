package mobdev.travo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShareJourneyActivity extends NavigationBarActivity {

    private AutoCompleteTextView startLocation;
    private Spinner transportMode;
    private Spinner endLocationSpinner;
    private EditText travelTime, travelCost, travelTips;
    private RatingBar journeyRating;
    private Button submitButton;

    private Map<String, Long> destinationMap = new HashMap<>();

    private UserDBHelper dbHelper;
    private long selectedDestinationId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_journey);

        dbHelper = new UserDBHelper(this);

        // Initialize UI components
        startLocation = findViewById(R.id.startLocation);
        transportMode = findViewById(R.id.transportMode);
        endLocationSpinner = findViewById(R.id.endLocationSpinner);
        travelTime = findViewById(R.id.travelTime);
        travelCost = findViewById(R.id.travelCost);
        travelTips = findViewById(R.id.travelTips);
        journeyRating = findViewById(R.id.journeyRating);
        submitButton = findViewById(R.id.submitButton);

        // Set up transport mode spinner
        String[] transportModes = {"Bus", "Jeepney", "Train", "Flight", "Car", "Ferry", "Motorcycle", "Walking", "Other"};
        ArrayAdapter<String> transportAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, transportModes);
        transportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transportMode.setAdapter(transportAdapter);

        // Populate the destination spinner
        populateDestinationSpinner();

        // Set submit button click listener
        submitButton.setOnClickListener(v -> saveJourney());

        setupBottomNavigation(R.id.nav_home);
    }

    private void populateDestinationSpinner() {
        // Fetch all destinations from the database
        Cursor cursor = dbHelper.getAllDestinations();
        List<String> destinationNames = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(UserDBHelper.DEST_COL_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(UserDBHelper.DEST_COL_NAME));
                String location = cursor.getString(cursor.getColumnIndexOrThrow(UserDBHelper.DEST_COL_LOCATION));

                // Store full display text (name + location) and map it to the ID
                String displayText = name + " - " + location;
                destinationNames.add(displayText);
                destinationMap.put(displayText, id);
            } while (cursor.moveToNext());
            cursor.close();
        }

        // Create adapter for the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, destinationNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        endLocationSpinner.setAdapter(adapter);

        // Listen for selection
        endLocationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDestText = (String) parent.getItemAtPosition(position);
                selectedDestinationId = destinationMap.get(selectedDestText);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedDestinationId = -1;
            }
        });
    }

    private void saveJourney() {
        String start = startLocation.getText().toString().trim();

        // Add null check for transportMode.getSelectedItem()
        Object transportItem = transportMode.getSelectedItem();
        String transport = transportItem != null ? transportItem.toString() : "";

        String time = travelTime.getText().toString().trim();
        String cost = travelCost.getText().toString().trim();
        String tips = travelTips.getText().toString().trim();
        int rating = (int) journeyRating.getRating();
        String date = String.valueOf(System.currentTimeMillis());

        if (TextUtils.isEmpty(start) || TextUtils.isEmpty(transport) ||
                TextUtils.isEmpty(time) || TextUtils.isEmpty(cost) ||
                rating < 1 || selectedDestinationId == -1) {
            Toast.makeText(this, "Please fill in all required fields, select a destination, and rate your journey.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        long userId = getCurrentUserId();
        if (userId == -1) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert journey route into the db - using the selected destination ID
        // Photo path is now empty string since we removed photo functionality
        long result = dbHelper.insertRoute(userId, selectedDestinationId,
                start, transport, time, cost, tips, "", date);

        if (result != -1) {
            Toast.makeText(this, "Journey shared!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to save journey.", Toast.LENGTH_SHORT).show();
        }
    }

    // Utility: find user id by current email in session
    private long getCurrentUserId() {
        String email = getSharedPreferences("user_session", MODE_PRIVATE)
                .getString("email", null);
        if (email != null) {
            Cursor cursor = dbHelper.getUserByEmail(email);
            if (cursor != null && cursor.moveToFirst()) {
                long uid = cursor.getLong(cursor.getColumnIndexOrThrow(UserDBHelper.COL_ID));
                cursor.close();
                return uid;
            }
        }
        return -1;
    }
}