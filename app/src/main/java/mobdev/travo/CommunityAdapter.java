package mobdev.travo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.ViewHolder> {

    private Context context;
    private List<CommunityItem> communityList;

    public CommunityAdapter(Context context, List<CommunityItem> communityList) {
        this.context = context;
        this.communityList = communityList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.community_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommunityItem item = communityList.get(position);
        holder.locationText.setText(item.getLocationName());
        holder.ratingBar.setRating(item.getRating());
        holder.routeText.setText(item.getRoute());
        holder.fareText.setText(item.getFare());
    }

    @Override
    public int getItemCount() {
        return communityList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView locationText, routeText, fareText;
        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            locationText = itemView.findViewById(R.id.CommunityLocation);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            routeText = itemView.findViewById(R.id.tvRoute);
            fareText = itemView.findViewById(R.id.tvFare);
        }
    }
}
