package mobdev.travo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
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

        List<CommunityItem> data = new ArrayList<>();
        data.add(new CommunityItem("Ayala Center Cebu", 4.5f, "₱15", "1 ride via 13C"));
        data.add(new CommunityItem("SM Seaside", 4.2f, "₱15", "2 rides via 12L & 10M"));
        data.add(new CommunityItem("IT Park", 4.8f, "₱20", "1 ride via 17B"));
        data.add(new CommunityItem("Colon Street", 3.9f, "₱14", "1 ride via 06B"));

        CommunityAdapter adapter = new CommunityAdapter(this, data);
        communityRecyclerView.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> {
            Intent intentfeedback = new Intent(CommunityPageActivity.this, CommunityFeedback.class);
            startActivity(intentfeedback);
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
