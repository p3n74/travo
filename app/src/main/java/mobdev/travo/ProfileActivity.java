package mobdev.travo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import mobdev.travo.CommunityPageActivity;
import mobdev.travo.HomePageActivity;

public class ProfileActivity extends NavigationBarActivity  {

    private ImageView profileImage;  // Changed from ShapeableImageView to ImageView
    private TextView headername, nameTextView, birthdayTextView, phoneTextView, emailTextView;
    private TextView editButton;

    private UserDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // This is the navigation bar part na
        setupBottomNavigation(R.id.nav_profile);

        headername = findViewById(R.id.profile_name_title);
        profileImage = findViewById(R.id.profile_image); // Cast to ImageView
        nameTextView = findViewById(R.id.profile_name);
        birthdayTextView = findViewById(R.id.profile_birthday);
        phoneTextView = findViewById(R.id.profile_phone);
        emailTextView = findViewById(R.id.profile_email);
        editButton = findViewById(R.id.edit_profile_button);

        // SharedPreferences
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        String email = prefs.getString("email", null);

        if (email == null) {
            Toast.makeText(this, "Session expired. Please log in again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class)); // Replace with actual login activity
            finish();
            return;
        }

        dbHelper = new UserDBHelper(this);

        // Load user data from the database
        loadUserData(email);

        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
        });

        // Remove ShapeableImageView logic to avoid crashing
        // Optional: Add round image behavior later with third-party library or drawable
        editButton.setOnClickListener(v -> {
            // TODO: Launch edit profile activity
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadUserData(String email) {
        Cursor cursor = dbHelper.getUserByEmail(email);
        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(UserDBHelper.COL_NAME));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(UserDBHelper.COL_PHONE));
            String gender = cursor.getString(cursor.getColumnIndexOrThrow(UserDBHelper.COL_GENDER));

            headername.setText(name);
            nameTextView.setText(name);
            phoneTextView.setText(phone);
            birthdayTextView.setText(gender); // You can replace this with birthday if available
            emailTextView.setText(email);

            cursor.close();
        }
    }
}
