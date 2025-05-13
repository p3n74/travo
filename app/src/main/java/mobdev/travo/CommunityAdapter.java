package mobdev.travo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.ViewHolder> {

    private Context context;
    private List<CommunityItem> communityItems;

    public CommunityAdapter(Context context, List<CommunityItem> communityItems) {
        this.context = context;
        this.communityItems = communityItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_journey_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommunityItem item = communityItems.get(position);

        // Set data to views
        holder.tvUserName.setText(item.getLocationName());
        holder.rbRouteRating.setRating(item.getRating());
        holder.tvRouteSummary.setText(item.getRoute());
        holder.tvStartLocation.setText(item.getStartLocation());
        holder.tvEndLocation.setText(item.getEndLocation());
        holder.tvCost.setText(item.getFare());
        holder.tvTravelTime.setText(item.getTravelTime());

        // Handle tips (hide if empty)
        String tips = item.getTravelTips();
        if (tips != null && !tips.isEmpty()) {
            holder.layoutTips.setVisibility(View.VISIBLE);
            holder.tvTravelTips.setText(tips);
        } else {
            holder.layoutTips.setVisibility(View.GONE);
        }

        // Set click listener to expand/collapse tips
        holder.itemView.setOnClickListener(v -> {
            if (holder.tvTravelTips.getMaxLines() == 3) {
                holder.tvTravelTips.setMaxLines(Integer.MAX_VALUE);
                holder.tvTravelTips.setEllipsize(null);
            } else {
                holder.tvTravelTips.setMaxLines(3);
                holder.tvTravelTips.setEllipsize(android.text.TextUtils.TruncateAt.END);
            }
        });
    }

    @Override
    public int getItemCount() {
        return communityItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvRouteSummary, tvStartLocation, tvEndLocation,
                tvCost, tvTravelTime, tvTravelTips;
        RatingBar rbRouteRating;
        LinearLayout layoutTips;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvRouteSummary = itemView.findViewById(R.id.tvRouteSummary);
            tvStartLocation = itemView.findViewById(R.id.tvStartLocation);
            tvEndLocation = itemView.findViewById(R.id.tvEndLocation);
            tvCost = itemView.findViewById(R.id.tvCost);
            tvTravelTime = itemView.findViewById(R.id.tvTravelTime);
            tvTravelTips = itemView.findViewById(R.id.tvTravelTips);
            rbRouteRating = itemView.findViewById(R.id.rbRouteRating);
            layoutTips = itemView.findViewById(R.id.layoutTips);
        }
    }
}