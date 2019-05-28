package hardwaremaster.com.Ranking.GpuRanking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import hardwaremaster.com.R;
import hardwaremaster.com.data.Gpu;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class GpuRankingAdapter extends RecyclerView.Adapter<GpuRankingAdapter.ViewHolder> {
    private List<Gpu> mGpuList;


    // Provide a suitable constructor (depends on the kind of dataset)
    public GpuRankingAdapter(List<Gpu> data) {
        this.mGpuList = data;
    }

    public void setList(List<Gpu> gpuList) {
        mGpuList = checkNotNull(gpuList);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card,
                parent, false);
        GpuRankingAdapter.ViewHolder viewHolder = new GpuRankingAdapter.ViewHolder(view);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.textViewModel.setText(mGpuList.get(position).getModel());
        holder.textViewDescription.setText(mGpuList.get(position).getReleaseDate());
        holder.textViewTag1.setText(Double.toString(mGpuList.get(position).getAvgFps1080p()));
        holder.textViewTag2.setText(Integer.toString(mGpuList.get(position).getFirestrike()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mGpuList.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textViewModel;
        public TextView textViewDescription;
        public ImageView imageViewImage;
        public TextView textViewTag1;
        public TextView textViewTag2;

        public ViewHolder(View itemView) {
            super(itemView);
            this.textViewModel = itemView.findViewById(R.id.model);
            this.textViewDescription = itemView.findViewById(R.id.description);
            this.imageViewImage = itemView.findViewById(R.id.image);
            this.textViewTag1 = itemView.findViewById(R.id.tag1);
            this.textViewTag2 = itemView.findViewById(R.id.tag2);
        }
    }
}