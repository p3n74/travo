package mobdev.travo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends NavigationBarActivity {

    RecyclerView popularRecyclerView, discoverRecyclerView;
    DestinationAdapter popularAdapter, discoverAdapter;
    List<DestinationModel> popularList, discoverList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);

        // Wire up the bottom nav, highlighting "Home"
        setupBottomNavigation(R.id.nav_home);

        popularRecyclerView = findViewById(R.id.popularRecyclerView);
        discoverRecyclerView = findViewById(R.id.discoverRecyclerView);

        popularList = new ArrayList<>();
        discoverList = new ArrayList<>();

        // Add sample data
        popularList.add(new DestinationModel("IT Park, Cebu City", "Cebu IT Park, Brgy Apas, Cebu City, 6000", R.drawable.it_park));
        popularList.add(new DestinationModel("Temple of Leah", "Cebu Transcentral Hwy, Cebu City, 6000", R.drawable.temple_leah));

        discoverList.add(new DestinationModel("Elevator ni King Al", "2010 Gov. M. Cuenco Ave, Cebu City", R.drawable.elevator));
        discoverList.add(new DestinationModel("Tales and Feelings", "2nd flr, above N. Ramos St, Cebu City", R.drawable.tales_feelings));
a
        // Set adapters
        popularAdapter = new DestinationAdapter(this, popularList);
        discoverAdapter = new DestinationAdapter(this, discoverList);

        popularRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        discoverRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        popularRecyclerView.setAdapter(popularAdapter);
        discoverRecyclerView.setAdapter(discoverAdapter);
    }
}