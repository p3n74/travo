package mobdev.travo; // Your package name - make sure this is correct

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent; // Import Intent for navigation
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox; // Import CheckBox
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast; // Import Toast for temporary messages

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton; // Import FloatingActionButton
import com.google.android.material.textfield.TextInputEditText; // Import TextInputEditText
import com.google.android.material.textfield.TextInputLayout; // Import TextInputLayout


public class SignupActivity extends AppCompatActivity {

    // Declare variables for UI elements
    FloatingActionButton fabLogo;
    TextInputEditText etName;
    TextInputEditText etEmail;
    Spinner spinnerCountry;
    TextInputEditText etPhone;
    AutoCompleteTextView dropdownGender;
    TextInputEditText etPassword;
    CheckBox cbTerms;
    TextView tvTerms;
    MaterialButton btnSignUp;
    MaterialButton btnGmail;
    MaterialButton btnFacebook;
    TextView tvLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // --- Find UI elements by their IDs ---
        fabLogo = findViewById(R.id.fabLogo);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        spinnerCountry = findViewById(R.id.spinnerCountry);
        etPhone = findViewById(R.id.etPhone);
        dropdownGender = findViewById(R.id.dropdownGender);
        etPassword = findViewById(R.id.etPassword);
        cbTerms = findViewById(R.id.cbTerms);
        tvTerms = findViewById(R.id.tvTerms);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnGmail = findViewById(R.id.btnGmail);
        btnFacebook = findViewById(R.id.btnFacebook);
        tvLogin = findViewById(R.id.tvLogin);
        // --- End of Find UI elements ---


        // Set up terms link clickable
        tvTerms.setMovementMethod(LinkMovementMethod.getInstance());

        // Set up Country-code spinner adapter
        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter
                .createFromResource(this, R.array.country_codes,
                        android.R.layout.simple_spinner_item);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(countryAdapter);

        // Set up Gender dropdown adapter
        String[] genders = {"Male", "Female", "Other"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                genders
        );
        dropdownGender.setAdapter(genderAdapter);


        // Sign Up button
        btnSignUp.setOnClickListener(v -> {
            // Get user input (you'll add validation and signup logic here)
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String selectedCountryCode = spinnerCountry.getSelectedItem().toString();
            String selectedGender = dropdownGender.getText().toString().trim();
            boolean agreedToTerms = cbTerms.isChecked();

            // Basic check to show it's working
            Toast.makeText(this, "Sign Up Clicked", Toast.LENGTH_SHORT).show();
            // actual sign-up logic (validation, network calls, etc.) here after test
        });

        // Gmail Sign Up button
        btnGmail.setOnClickListener(v -> {
            Toast.makeText(this, "Sign up with Gmail Clicked", Toast.LENGTH_SHORT).show();
            // Implement Google Sign-Up maybeeeeee
        });

        // Facebook Sign Up button
        btnFacebook.setOnClickListener(v -> {
            Toast.makeText(this, "Sign up with Facebook Clicked", Toast.LENGTH_SHORT).show();
            // Implement Facebook Sign-Up maybeeeeee
        });

        // Login link (Navigate to LoginActivity)
        tvLogin.setOnClickListener(v -> {
            Toast.makeText(this, "Login Here Clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

    }
}