package mobdev.travo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.ViewHolder> {

    private Context context;
    private List<DestinationModel> destinations;
    private static final String TAG = "DestinationAdapter";

    public DestinationAdapter(Context context, List<DestinationModel> destinations) {
        this.context = context;
        this.destinations = destinations;
    }

    @NonNull
    @Override
    public DestinationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_destination, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DestinationAdapter.ViewHolder holder, int position) {
        DestinationModel model = destinations.get(position);
        holder.titleTextView.setText(model.getTitle());
        holder.addressTextView.setText(model.getAddress());

        // Get the image filename from the model
        String imageFilename = model.getImageFilename();

        // Load image from assets
        loadImageFromAssets(imageFilename, holder.imageView);

        holder.itemView.setOnClickListener(v -> {
            // Create intent to start CommunityPageActivity
            Intent intent = new Intent(v.getContext(), CommunityPageActivity.class);

            // Pass data about the chosen destination
            intent.putExtra("title", model.getTitle());
            intent.putExtra("address", model.getAddress());
            intent.putExtra("imageFilename", imageFilename); // Pass the filename instead of resource ID
            intent.putExtra("description", model.getDescription());

            v.getContext().startActivity(intent);
        });
    }

    private void loadImageFromAssets(String filename, ImageView imageView) {
        try {
            // Try with .jpg extension
            String assetPath = filename + ".jpg";
            Log.d(TAG, "Trying to load: " + assetPath);

            InputStream is = context.getAssets().open(assetPath);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            imageView.setImageBitmap(bitmap);
            Log.d(TAG, "Successfully loaded image from assets");

        } catch (IOException e) {
            // Try with lowercase
            try {
                String assetPath = filename.toLowerCase() + ".jpg";
                Log.d(TAG, "Trying lowercase: " + assetPath);

                InputStream is = context.getAssets().open(assetPath);
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

    @Override
    public int getItemCount() {
        return destinations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView, addressTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
        }
    }
}