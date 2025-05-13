package mobdev.travo;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShareJourneyActivity extends NavigationBarActivity {

    private static final int PICK_IMAGES_REQUEST = 101;

    private AutoCompleteTextView startLocation;
    private Spinner transportMode;
    private Spinner endLocationSpinner;
    private EditText travelTime, travelCost, travelTips;
    private RatingBar journeyRating;
    private Button uploadPhotos, submitButton;
    private ImageView imageView1, imageView2, imageView3;

    private List<Uri> selectedImageUris = new ArrayList<>();
    private Map<String, Long> destinationMap = new HashMap<>();

    private UserDBHelper dbHelper;
    private long selectedDestinationId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_journey);

        dbHelper = new UserDBHelper(this);

        startLocation = findViewById(R.id.startLocation);
        transportMode = findViewById(R.id.transportMode);
        endLocationSpinner = findViewById(R.id.endLocationSpinner);
        travelTime = findViewById(R.id.travelTime);
        travelCost = findViewById(R.id.travelCost);
        travelTips = findViewById(R.id.travelTips);
        journeyRating = findViewById(R.id.journeyRating);
        uploadPhotos = findViewById(R.id.uploadPhotos);
        submitButton = findViewById(R.id.submitButton);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);

        // Set up transport mode spinner
        String[] transportModes = {"Bus", "Jeepney", "Train", "Flight", "Car", "Ferry", "Motorcycle", "Walking", "Other"};
        ArrayAdapter<String> transportAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, transportModes);
        transportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transportMode.setAdapter(transportAdapter);

        // Populate the destination spinner
        populateDestinationSpinner();

        uploadPhotos.setOnClickListener(v -> openImagePicker());
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

    private void openImagePicker() {
        // Allow user to pick up to 3 images from gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select up to 3 Images"), PICK_IMAGES_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGES_REQUEST && resultCode == Activity.RESULT_OK) {
            selectedImageUris.clear();
            imageView1.setImageResource(android.R.color.transparent);
            imageView2.setImageResource(android.R.color.transparent);
            imageView3.setImageResource(android.R.color.transparent);

            if (data != null) {
                // Multiple images
                if (data.getClipData() != null) {
                    int count = Math.min(data.getClipData().getItemCount(), 3);
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        selectedImageUris.add(imageUri);
                    }
                } else if (data.getData() != null) { // Single image
                    selectedImageUris.add(data.getData());
                }

                // Preview images
                try {
                    if (selectedImageUris.size() > 0)
                        imageView1.setImageBitmap(getBitmapFromUri(selectedImageUris.get(0)));
                    if (selectedImageUris.size() > 1)
                        imageView2.setImageBitmap(getBitmapFromUri(selectedImageUris.get(1)));
                    if (selectedImageUris.size() > 2)
                        imageView3.setImageBitmap(getBitmapFromUri(selectedImageUris.get(2)));
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        return MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
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

        String photoPath = "";
        if (selectedImageUris.size() > 0) {
            photoPath = selectedImageUris.get(0).toString();
        }

        // Insert journey route into the db - using the selected destination ID
        long result = dbHelper.insertRoute(userId, selectedDestinationId,
                start, transport, time, cost, tips, photoPath, date);

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