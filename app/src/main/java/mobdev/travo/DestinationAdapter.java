package mobdev.travo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.content.Intent;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.ViewHolder> {

    private Context context;
    private List<DestinationModel> destinations;

    public DestinationAdapter(Context context, List<DestinationModel> destinations) {
        this.context = context;
        this.destinations = destinations;
    }

    @NonNull
    @Override
    public DestinationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DestinationAdapter.ViewHolder holder, int position) {
        DestinationModel model = destinations.get(position);
        holder.titleTextView.setText(model.getTitle());
        holder.addressTextView.setText(model.getAddress());
        holder.imageView.setImageResource(model.getImageResId());

        holder.itemView.setOnClickListener(v -> {
            // Create intent to start CommunityPageActivity
            Intent intent = new Intent(v.getContext(), CommunityPageActivity.class);

            // (Optional) Pass data about the chosen destination:
            intent.putExtra("title", model.getTitle());
            intent.putExtra("address", model.getAddress());
            intent.putExtra("imageResId", model.getImageResId());

            v.getContext().startActivity(intent);
        });
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
