package mobdev.travo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private ImageView imageViewAppIcon;
    private TextView textViewTitle;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewForgotPassword;
    private Button buttonLogin;
    private TextView textViewOr;
    private Button buttonLoginGmail;
    private TextView textViewSignUp;

    // Temporary, not the actual db, local lng sa for now
    UserDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new UserDBHelper(this);

        initViews();
        setupSignUpText();
        setupClickListeners();
    }

    private void initViews() {
        imageViewAppIcon = findViewById(R.id.imageViewAppIcon);
        textViewTitle = findViewById(R.id.textViewTitle);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewOr = findViewById(R.id.textViewOr);
        buttonLoginGmail = findViewById(R.id.buttonLoginGmail);
        textViewSignUp = findViewById(R.id.textViewSignUp);
    }

    private void setupSignUpText() {
        // Set the sign-up text with clickable link
        // Using Html.fromHtml is deprecated in newer APIs, consider using SpannableString for better practice
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            textViewSignUp.setText(Html.fromHtml(getString(R.string.sign_up_text), Html.FROM_HTML_MODE_LEGACY));
        } else {
            textViewSignUp.setText(Html.fromHtml(getString(R.string.sign_up_text)));
        }
        textViewSignUp.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setupClickListeners() {
        // Login button
        buttonLogin.setOnClickListener(v -> {
            if (validateInputs()) {
                performLogin();
            }
        });

        // Forgot password
        textViewForgotPassword.setOnClickListener(v -> {
            Toast.makeText(LoginActivity.this, "Forgot Password clicked", Toast.LENGTH_SHORT).show();
            // TODO: Implement forgot password functionality
        });

        // Gmail login
        buttonLoginGmail.setOnClickListener(v -> {
            Toast.makeText(LoginActivity.this, "Login with Gmail clicked", Toast.LENGTH_SHORT).show();
            // TODO: Implement Google Sign-In
        });

        // Sign up (Just Navigates to Sign Up page)
        textViewSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private boolean validateInputs() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return false;
        }

        if (!isValidEmail(email)) {
            editTextEmail.setError("Enter a valid email address");
            editTextEmail.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    private void performLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (dbHelper.checkUser(email, password)) {
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
            // TODO: Navigate to dashboard/home
            // Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            // startActivity(intent);
            // finish();

            // Temporary Navigation to home page:
            Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }


}