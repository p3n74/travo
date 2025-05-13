package mobdev.travo;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CommunityPageActivity extends NavigationBarActivity {

    private ImageView btnAdd, btnSearch;
    private EditText searchBar;
    private RecyclerView communityRecyclerView;
    private UserDBHelper dbHelper;
    private long destinationId = -1;
    private static final String TAG = "CommunityPageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_community_page);

        setupBottomNavigation(-1);
        dbHelper = new UserDBHelper(this);

        // UI components
        btnAdd = findViewById(R.id.btnAdd);
        btnSearch = findViewById(R.id.btnSearch);
        searchBar = findViewById(R.id.searchBar);
        communityRecyclerView = findViewById(R.id.communityRecyclerView);
        communityRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        TextView placeSummary = findViewById(R.id.placeSummary);
        TextView placeTitle = findViewById(R.id.placeTitle);
        ImageView placeImage = findViewById(R.id.placeImage);

        // Get destination info from intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");

        // Set the title immediately
        placeTitle.setText(title);

        // Find destination in database by title
        if (title != null && !title.isEmpty()) {
            Cursor cursor = dbHelper.getAllDestinations();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String destName = cursor.getString(cursor.getColumnIndexOrThrow(UserDBHelper.DEST_COL_NAME));
                    if (destName.equals(title)) {
                        // Found our destination
                        destinationId = cursor.getLong(cursor.getColumnIndexOrThrow(UserDBHelper.DEST_COL_ID));
                        String description = cursor.getString(cursor.getColumnIndexOrThrow(UserDBHelper.DEST_COL_DESC));
                        String galleryUrl = cursor.getString(cursor.getColumnIndexOrThrow(UserDBHelper.DEST_COL_GALLERY));

                        // Set the description
                        placeSummary.setText(description);

                        // Try to load the image directly from assets
                        loadImageFromAssets(galleryUrl, placeImage);

                        break;
                    }
                } while (cursor.moveToNext());
                cursor.close();
            }
        }

        // Load routes
        List<CommunityItem> routesList = getRoutesForDestination(destinationId);
        CommunityAdapter adapter = new CommunityAdapter(this, routesList);
        communityRecyclerView.setAdapter(adapter);

        // Button listeners
        btnAdd.setOnClickListener(v -> {
            Intent shareIntent = new Intent(CommunityPageActivity.this, ShareJourneyActivity.class);
            shareIntent.putExtra("dest_id", destinationId);
            shareIntent.putExtra("title", title);
            startActivity(shareIntent);
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

    private void loadImageFromAssets(String filename, ImageView imageView) {
        try {
            // Try with .jpg extension
            String assetPath = filename + ".jpg";
            Log.d(TAG, "Trying to load: " + assetPath);

            InputStream is = getAssets().open(assetPath);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            imageView.setImageBitmap(bitmap);
            Log.d(TAG, "Successfully loaded image from assets");

        } catch (IOException e) {
            // Try with lowercase
            try {
                String assetPath = filename.toLowerCase() + ".jpg";
                Log.d(TAG, "Trying lowercase: " + assetPath);

                InputStream is = getAssets().open(assetPath);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                imageView.setImageBitmap(bitmap);
                Log.d(TAG, "Successfully loaded lowercase image");

            } catch (IOException e2) {
                // Failed to load image, show placeholder
                Log.e(TAG, "Failed to load image: " + e2.getMessage());
                imageView.setImageResource(R.drawable.ic_placeholder);
            }
        }
    }

    private List<CommunityItem> getRoutesForDestination(long destId) {
        // Method remains unchanged
        List<CommunityItem> routesList = new ArrayList<>();
        if (destId == -1) {
            return routesList;
        }

        Cursor routesCursor = dbHelper.getRoutesForDestination(destId);

        if (routesCursor != null && routesCursor.moveToFirst()) {
            do {
                long userId = routesCursor.getLong(routesCursor.getColumnIndexOrThrow(UserDBHelper.ROUTE_COL_USERID));
                String userName = "User";

                Cursor userCursor = dbHelper.getUserByID(userId);
                if (userCursor != null && userCursor.moveToFirst()) {
                    userName = userCursor.getString(userCursor.getColumnIndexOrThrow(UserDBHelper.COL_NAME));
                    userCursor.close();
                }

                String startLocation = routesCursor.getString(routesCursor.getColumnIndexOrThrow(UserDBHelper.ROUTE_COL_START));
                String transportMode = routesCursor.getString(routesCursor.getColumnIndexOrThrow(UserDBHelper.ROUTE_COL_TRANSPORT));
                String duration = routesCursor.getString(routesCursor.getColumnIndexOrThrow(UserDBHelper.ROUTE_COL_DURATION));
                String cost = routesCursor.getString(routesCursor.getColumnIndexOrThrow(UserDBHelper.ROUTE_COL_COST));
                String tips = routesCursor.getString(routesCursor.getColumnIndexOrThrow(UserDBHelper.ROUTE_COL_TIPS));

                String destinationName = "";
                Cursor destCursor = dbHelper.getDestinationById(destId);
                if (destCursor != null && destCursor.moveToFirst()) {
                    destinationName = destCursor.getString(destCursor.getColumnIndexOrThrow(UserDBHelper.DEST_COL_NAME));
                    destCursor.close();
                }

                String routeSummary = "1 ride via " + transportMode;

                CommunityItem item = new CommunityItem(
                        userName,
                        4.0f,
                        cost,
                        routeSummary,
                        startLocation,
                        destinationName,
                        transportMode,
                        duration,
                        tips
                );

                routesList.add(item);
            } while (routesCursor.moveToNext());

            routesCursor.close();
        }

        return routesList;
    }
}