package mobdev.travo;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class ShareJourneyActivity extends NavigationBarActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private List<Uri> selectedImages = new ArrayList<>();
    private ImageView[] imageViews = new ImageView[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_journey);

        // Navbar bottom setup ni
        setupBottomNavigation(-1);

        AutoCompleteTextView startLocation = findViewById(R.id.startLocation);
        Spinner transportMode = findViewById(R.id.transportMode);
        EditText travelTime = findViewById(R.id.travelTime);
        EditText travelCost = findViewById(R.id.travelCost);
        EditText travelTips = findViewById(R.id.travelTips);
        RatingBar journeyRating = findViewById(R.id.journeyRating);
        Button submitButton = findViewById(R.id.submitButton);
        Button uploadPhotos = findViewById(R.id.uploadPhotos);

        imageViews[0] = findViewById(R.id.imageView1);
        imageViews[1] = findViewById(R.id.imageView2);
        imageViews[2] = findViewById(R.id.imageView3);

        // Mock location auto-suggestions
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, new String[]{"New York", "Paris", "Tokyo", "Dubai"});
        startLocation.setAdapter(adapter);

        uploadPhotos.setOnClickListener(v -> pickImages());

        submitButton.setOnClickListener(v -> {
            // Collect data and handle submission
        });
    }

    private void pickImages() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select up to 3 images"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            selectedImages.clear();
            ClipData clipData = data.getClipData();
            if (clipData != null) {
                for (int i = 0; i < Math.min(3, clipData.getItemCount()); i++) {
                    selectedImages.add(clipData.getItemAt(i).getUri());
                }
            } else if (data.getData() != null) {
                selectedImages.add(data.getData());
            }

            for (int i = 0; i < 3; i++) {
                if (i < selectedImages.size()) {
                    imageViews[i].setImageURI(selectedImages.get(i));
                } else {
                    imageViews[i].setImageResource(android.R.color.transparent);
                }
            }
        }
    }
}
