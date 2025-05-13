package mobdev.travo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddDestination extends NavigationBarActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "AddDestinationActivity";

    private TextInputEditText destName, destLocation, destDescription;
    private Spinner categorySpinner;
    private RatingBar destRating;
    private ImageView destImagePreview;
    private MaterialButton btnSelectImage, btnSubmitDestination;

    private UserDBHelper dbHelper;
    private Uri selectedImageUri = null;
    private String savedImageFilename = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_destination);

        dbHelper = new UserDBHelper(this);

        // Initialize UI components
        destName = findViewById(R.id.destName);
        destLocation = findViewById(R.id.destLocation);
        destDescription = findViewById(R.id.destDescription);
        categorySpinner = findViewById(R.id.categorySpinner);
        destRating = findViewById(R.id.destRating);
        destImagePreview = findViewById(R.id.destImagePreview);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnSubmitDestination = findViewById(R.id.btnSubmitDestination);

        setupCategorySpinner();
        setupBottomNavigation(-1); // No selected item in bottom nav

        // Set click listeners
        btnSelectImage.setOnClickListener(v -> openImagePicker());
        btnSubmitDestination.setOnClickListener(v -> saveDestination());
    }

    private void setupCategorySpinner() {
        String[] categories = {
                "Landmark", "Nature Park", "Historical", "Lifestyle",
                "Church", "Attraction", "Viewpoint", "Other"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                destImagePreview.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveDestination() {
        // Validate inputs
        String name = destName.getText().toString().trim();
        String location = destLocation.getText().toString().trim();
        String description = destDescription.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();
        float rating = destRating.getRating();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(location) ||
                TextUtils.isEmpty(description) || rating == 0) {
            Toast.makeText(this, "Please fill all fields and provide a rating", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedImageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save the image and get filename
        try {
            savedImageFilename = saveImageToInternalStorage(selectedImageUri);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert into database without extension
        String filenameWithoutExtension = savedImageFilename.substring(0, savedImageFilename.lastIndexOf('.'));
        long result = dbHelper.insertDestination(name, description, location, category, filenameWithoutExtension);

        if (result != -1) {
            Toast.makeText(this, "Destination added successfully!", Toast.LENGTH_SHORT).show();
            finish(); // Go back to previous screen
        } else {
            Toast.makeText(this, "Failed to add destination", Toast.LENGTH_SHORT).show();
        }
    }

    private String saveImageToInternalStorage(Uri imageUri) throws IOException {
        // Create a unique filename based on timestamp
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "DEST_" + timeStamp + ".jpg";

        // Create file in the app's private directory
        File storageDir = new File(getFilesDir(), "destination_images");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        File imageFile = new File(storageDir, imageFileName);

        // Copy the image data to the file
        InputStream inputStream = getContentResolver().openInputStream(imageUri);
        OutputStream outputStream = new FileOutputStream(imageFile);

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        inputStream.close();
        outputStream.close();

        Log.d(TAG, "Image saved: " + imageFile.getAbsolutePath());
        return imageFileName;
    }
}