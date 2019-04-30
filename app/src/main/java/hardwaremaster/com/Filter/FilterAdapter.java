package hardwaremaster.com.Filter;

import android.util.Range;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import hardwaremaster.com.R;
import hardwaremaster.com.widgets.RangeSeekBar;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

    private ArrayList<RangeSeekBar> mRangeSeekBarList;

    // Provide a suitable constructor (depends on the kind of dataset)
    public FilterAdapter(ArrayList<RangeSeekBar> data) {
        this.mRangeSeekBarList = data;
    }

    public void setList(ArrayList<RangeSeekBar> rangeSeekBars) {
        mRangeSeekBarList = checkNotNull(rangeSeekBars);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seek_bar,
                parent, false);
        FilterAdapter.ViewHolder viewHolder = new FilterAdapter.ViewHolder(view);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.textViewType.setText(mRangeSeekBarList.get(position).getRangeSeekBarType());
        holder.rangeSeekBar.setRangeValues(mRangeSeekBarList.get(position).getAbsoluteMinValue(), mRangeSeekBarList.get(position).getAbsoluteMaxValue());
        holder.rangeSeekBar.setTextAboveThumbsColor(R.color.colorPrimary);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mRangeSeekBarList.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textViewType;
        public RangeSeekBar rangeSeekBar;

        public ViewHolder(View itemView) {
            super(itemView);
            this.textViewType = itemView.findViewById(R.id.tvType);
            this.rangeSeekBar = itemView.findViewById(R.id.rangeSeekBar);
        }
    }
}
