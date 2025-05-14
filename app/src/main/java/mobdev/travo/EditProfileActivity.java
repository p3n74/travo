package mobdev.travo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.database.Cursor;

import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {

    private UserDBHelper dbHelper;
    private EditText editName, editPhone, editEmail;
    private Button saveButton;
    private String currentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize DB helper and input fields
        dbHelper = new UserDBHelper(this);
        editName = findViewById(R.id.edit_profile_name);
        editPhone = findViewById(R.id.edit_profile_phone);
        editEmail = findViewById(R.id.edit_profile_email);
        saveButton = findViewById(R.id.save_profile_button);

        // Get the email passed from the previous activity (could also use SharedPreferences)
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        currentEmail = prefs.getString("email", null);

        // Fetch user data by email
        if (currentEmail != null) {
            loadUserData();
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileChanges(v);
            }
        });
    }

    private void loadUserData() {
        Cursor cursor = dbHelper.getUserByEmail(currentEmail);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(UserDBHelper.COL_NAME));
            @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex(UserDBHelper.COL_PHONE));
            @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex(UserDBHelper.COL_EMAIL));

            // Set the current values in the input fields
            editName.setText(name);
            editPhone.setText(phone);
            editEmail.setText(email);

            cursor.close();
        }
    }

    // Save the profile changes
    public void saveProfileChanges(View view) {
        String updatedName = editName.getText().toString();
        String updatedPhone = editPhone.getText().toString();
        String updatedEmail = editEmail.getText().toString();

        // Update the user data in the database
        boolean isUpdated = dbHelper.updateUserProfile(currentEmail, updatedName, updatedPhone, updatedEmail);

        if (isUpdated) {
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

            // Optionally, update SharedPreferences as well
            SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("email", updatedEmail);
            editor.apply();

            // Finish activity and go back to previous screen
            Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
        }
    }
}
