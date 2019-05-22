package hardwaremaster.com.Ranking;

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
import hardwaremaster.com.data.Cpu;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.ViewHolder> implements Filterable {
    private List<Cpu> mCpuList;
    private List<Cpu> mCpuListFull = new ArrayList<>();
    private Filter listFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Cpu> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mCpuListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Cpu item : mCpuListFull) {
                    if (item.getModel().toLowerCase().contains(filterPattern)) {
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
            mCpuList.clear();
            mCpuList.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };

    // Provide a suitable constructor (depends on the kind of dataset)
    public RankingAdapter(List<Cpu> data) {
        this.mCpuList = data;
    }

    public void setList(List<Cpu> cpuList) {
        mCpuList = checkNotNull(cpuList);
        mCpuListFull.clear();
        mCpuListFull.addAll(mCpuList);


    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card,
                parent, false);
        RankingAdapter.ViewHolder viewHolder = new RankingAdapter.ViewHolder(view);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.textViewModel.setText(mCpuList.get(position).getModel());
        holder.textViewDescription.setText("");
        holder.textViewTag1.setText(mCpuList.get(position).getSingleScore());
        holder.textViewTag2.setText(mCpuList.get(position).getMultiScore());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mCpuList.size();
    }

    /* */
    @Override
    public Filter getFilter() {
        return listFilter;
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
