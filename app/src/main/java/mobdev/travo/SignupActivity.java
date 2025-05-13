package mobdev.travo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent; // Import Intent for navigation
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox; // Import CheckBox
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast; // Import Toast for temporary messages

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText; // Import TextInputEditText


public class SignupActivity extends AppCompatActivity {

    // Declare variables for UI elements
    private ImageView imageViewAppIcon;
    private TextInputEditText etName;
    private TextInputEditText etEmail;
    private Spinner spinnerCountry;
    private TextInputEditText etPhone;
    private AutoCompleteTextView dropdownGender;
    private TextInputEditText etPassword;
    private CheckBox cbTerms;
    private TextView tvTerms;
    private MaterialButton btnSignUp;
    private MaterialButton btnGmail;
    private TextView tvLogin;

    // Temporary, For now local db lng sa
    private UserDBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // --- Initializing DB Helper
        dbHelper = new UserDBHelper(this);
        if (dbHelper.getAllDestinations().getCount() == 0) {
            dbHelper.insertSampleDestinations();
        }

        // --- Initializing UI elements uusing IDs ---
        initViews();

        // Set up terms link clickable
        tvTerms.setMovementMethod(LinkMovementMethod.getInstance());

        // Country-code spinner adapter
        setupCountryCodeSpinner();
        // Gender dropdown adapter
        setupGenderDropdown();

        // The click listeners (Both the signups and the link to login)
        setupClickListeners();
    }

    private void initViews() {
        imageViewAppIcon = findViewById(R.id.imageViewAppIcon);
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
        tvLogin = findViewById(R.id.tvLogin);
    }

    private void setupCountryCodeSpinner() {
        // Country-code spinner adapter
        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter
                .createFromResource(this, R.array.country_codes,
                        android.R.layout.simple_spinner_item);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(countryAdapter);
    }

    private void setupGenderDropdown() {
        // Gender dropdown adapter
        String[] genders = {"Male", "Female", "Other"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, genders);
        dropdownGender.setAdapter(genderAdapter);

        // Default value is set just for better UX
        dropdownGender.setText(genders[0], false);
    }

    private void setupClickListeners() {
        // Sign Up button
        btnSignUp.setOnClickListener(v -> {
            if (validateInputs()) {
                performSignup();
            }
        });

        // Gmail Sign Up
        btnGmail.setOnClickListener(v -> {
            Toast.makeText(this, "Sign up with Gmail Clicked", Toast.LENGTH_SHORT).show();
            // TODO: Implement Google Sign-Up integration
        });

        // Login link (Navigates to LoginActivity)
        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private boolean validateInputs() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String gender = dropdownGender.getText().toString();
        String password = etPassword.getText().toString().trim();
        boolean agreedToTerms = cbTerms.isChecked();

        // Basic validation I KNOW SO MANY IFs HAHAHAHA
        if (name.isEmpty()) {
            etName.setError("Name is required");
            etName.requestFocus();
            return false;
        }

        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return false;
        }

        if (!isValidEmail(email)) {
            etEmail.setError("Enter a valid email address");
            etEmail.requestFocus();
            return false;
        }

        if (phone.isEmpty()) {
            etPhone.setError("Phone number is required");
            etPhone.requestFocus();
            return false;
        }

        if (gender.isEmpty()) {
            dropdownGender.setError("Please select gender");
            dropdownGender.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            etPassword.setError("Password should be at least 6 characters");
            etPassword.requestFocus();
            return false;
        }

        if (!agreedToTerms) {
            Toast.makeText(this, "You must agree to the terms", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    private void performSignup() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (dbHelper.emailExists(email)) {
            Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = etName.getText().toString().trim();
        String countryCode = spinnerCountry.getSelectedItem().toString();
        String phone = etPhone.getText().toString().trim();
        String gender = dropdownGender.getText().toString();

        long result = dbHelper.insertUser(name, email, password, countryCode, phone, gender);

        if (result != -1) {
            Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Signup failed!", Toast.LENGTH_SHORT).show();
        }
    }
}