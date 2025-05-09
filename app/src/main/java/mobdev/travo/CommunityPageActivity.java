package mobdev.travo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommunityPageActivity extends AppCompatActivity {

    private ImageView btnAdd, btnSearch;

    EditText searchBar;
    private RecyclerView communityRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_community_page);

        // ✅ Move findViewById here, after setContentView
        btnAdd = findViewById(R.id.btnAdd);
        btnSearch = findViewById(R.id.btnSearch);

        searchBar = findViewById(R.id.searchBar);

        communityRecyclerView = findViewById(R.id.communityRecyclerView);
        communityRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ViewPager2 galleryViewPager = findViewById(R.id.galleryViewPager);
        TextView placeSummary = findViewById(R.id.placeSummary);

        List<CommunityItem> data = new ArrayList<>();
        data.add(new CommunityItem("Ayala Center Cebu", 4.5f, "₱15", "1 ride via 13C"));
        data.add(new CommunityItem("SM Seaside", 4.2f, "₱15", "2 rides via 12L & 10M"));
        data.add(new CommunityItem("IT Park", 4.8f, "₱20", "1 ride via 17B"));
        data.add(new CommunityItem("Colon Street", 3.9f, "₱14", "1 ride via 06B"));

        List<Integer> galleryImages = Arrays.asList(
                R.drawable.temple_leah,
                R.drawable.elevator,
                R.drawable.tales_feelings
        );

        CommunityAdapter adapter = new CommunityAdapter(this, data);
        communityRecyclerView.setAdapter(adapter);

        GalleryAdapter galleryAdapter = new GalleryAdapter(galleryImages);
        galleryViewPager.setAdapter(galleryAdapter);

        placeSummary.setText("Temple of Leah is a Roman-inspired structure built as a symbol of undying love. It offers panoramic views of Cebu and historical architecture.");

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(CommunityPageActivity.this, ShareJourneyActivity.class);
            startActivity(intent);
        });

        btnSearch.setOnClickListener(v -> {
            if (searchBar.getVisibility() == View.GONE) {
                searchBar.setVisibility(View.VISIBLE);
                searchBar.requestFocus();
            } else {
                searchBar.setVisibility(View.GONE);
            }
        });
    }
}
