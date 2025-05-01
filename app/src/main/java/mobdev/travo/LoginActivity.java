package mobdev.travo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    ImageView imageViewAppIcon;
    TextView textViewTitle;
    EditText editTextEmail;
    EditText editTextPassword;
    TextView textViewForgotPassword;
    Button buttonLogin;
    TextView textViewOr;
    Button buttonLoginGmail;
    Button buttonLoginFacebook;
    TextView textViewSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Find views by their IDs
        imageViewAppIcon = findViewById(R.id.imageViewAppIcon);
        textViewTitle = findViewById(R.id.textViewTitle);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewOr = findViewById(R.id.textViewOr);
        buttonLoginGmail = findViewById(R.id.buttonLoginGmail);
        buttonLoginFacebook = findViewById(R.id.buttonLoginFacebook);
        textViewSignUp = findViewById(R.id.textViewSignUp);

        // Set the sign-up text with clickable link
        // Using Html.fromHtml is deprecated in newer APIs, consider using SpannableString for better practice
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            textViewSignUp.setText(Html.fromHtml(getString(R.string.sign_up_text), Html.FROM_HTML_MODE_LEGACY));
        } else {
            textViewSignUp.setText(Html.fromHtml(getString(R.string.sign_up_text)));
        }
        textViewSignUp.setMovementMethod(LinkMovementMethod.getInstance());


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Main login button click. To be done
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                // login logic here (validation, API call, etc.), toast for now just for testing
                Toast.makeText(LoginActivity.this, "Login attempted with: " + email, Toast.LENGTH_SHORT).show();
            }
        });

        // TODO: Forgot password, but focus on other areas first.
        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle forgot password click. For now it'll be just a toast sa
                Toast.makeText(LoginActivity.this, "Forgot Password clicked", Toast.LENGTH_SHORT).show();
                // Navigate to forgot password screen or show a dialog
            }
        });

        buttonLoginGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Gmail login click
                Toast.makeText(LoginActivity.this, "Login with Gmail clicked", Toast.LENGTH_SHORT).show();
                // Implement Google Sign-In maybeee
            }
        });

        buttonLoginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Facebook login click
                Toast.makeText(LoginActivity.this, "Login with Facebook clicked", Toast.LENGTH_SHORT).show();
                // Implement Facebook Login maybeee
            }
        });

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TOAST IS OPTIONAL, I just added this for now hehe
                Toast.makeText(LoginActivity.this, "Opening Sign Up…", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}