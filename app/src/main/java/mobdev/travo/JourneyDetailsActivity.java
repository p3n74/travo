package mobdev.travo;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class JourneyDetailsActivity extends AppCompatActivity {

    private ImageView[] detailImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_journey_details);

        TextView startLoc = findViewById(R.id.startLocationDetail);
        TextView endLoc = findViewById(R.id.endLocationDetail);
        TextView mode = findViewById(R.id.transportModeDetail);
        TextView time = findViewById(R.id.travelTimeDetail);
        TextView cost = findViewById(R.id.travelCostDetail);
        TextView tips = findViewById(R.id.travelTipsDetail);
        RatingBar rating = findViewById(R.id.journeyRatingDetail);

        detailImages = new ImageView[] {
                findViewById(R.id.detailImage1),
                findViewById(R.id.detailImage2),
                findViewById(R.id.detailImage3)
        };

        // Sample static data (replace with dynamic data from intent or model)
        startLoc.setText("New York");
        endLoc.setText("Paris");
        mode.setText("Flight");
        time.setText("8h 30m");
        cost.setText("$700");
        tips.setText("Book early for cheaper flights.");
        rating.setRating(4);

        // Example: show static image resources (replace with URIs or Glide)
        detailImages[0].setImageResource(R.drawable.elevator);
        detailImages[1].setImageResource(R.drawable.tales_feelings);
        detailImages[2].setImageResource(R.drawable.it_park);
    }
}