package mobdev.travo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<CommunityItem> communityItems;

    private List<CommunityItem> communityItemsFull; // full copy for filtering


    public CommunityAdapter(Context context, List<CommunityItem> communityItems) {
        this.context = context;
        this.communityItems = communityItems;
        this.communityItemsFull = new ArrayList<>(communityItems);
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<CommunityItem> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(communityItemsFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (CommunityItem item : communityItemsFull) {
                        if (item.getStartLocation().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                communityItems.clear();
                communityItems.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
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