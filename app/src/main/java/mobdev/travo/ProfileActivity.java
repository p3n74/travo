package mobdev.travo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import mobdev.travo.CommunityPageActivity;
import mobdev.travo.HomePageActivity;

public class ProfileActivity extends NavigationBarActivity  {

    private ImageView profileImage;  // Changed from ShapeableImageView to ImageView
    private TextView nameTextView, birthdayTextView, phoneTextView, emailTextView;
    private TextView editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // This is the navigation bar part na
        setupBottomNavigation(R.id.nav_profile);

        profileImage = findViewById(R.id.profile_image); // Cast to ImageView
        nameTextView = findViewById(R.id.profile_name);
        birthdayTextView = findViewById(R.id.profile_birthday);
        phoneTextView = findViewById(R.id.profile_phone);
        emailTextView = findViewById(R.id.profile_email);
        editButton = findViewById(R.id.edit_profile_button);

        nameTextView.setText("Alex Coder");
        birthdayTextView.setText("May 12, 1997");
        phoneTextView.setText("0917 123 4567");
        emailTextView.setText("alex@codemail.com");

        // Remove ShapeableImageView logic to avoid crashing
        // Optional: Add round image behavior later with third-party library or drawable
        editButton.setOnClickListener(v -> {
            // TODO: Launch edit profile activity
        });
    }
}
