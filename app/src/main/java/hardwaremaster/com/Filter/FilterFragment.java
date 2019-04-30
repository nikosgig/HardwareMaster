package hardwaremaster.com.Filter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import hardwaremaster.com.R;
import hardwaremaster.com.widgets.RangeSeekBar;

public class FilterFragment extends BottomSheetDialogFragment {

    private OnBottomDialogFilterFragmentListener mListener;
    private Button applyFilterButton;
    private RelativeLayout seekBarsView;
    private ArrayList<RangeSeekBar<Double>> rangeSeekBars;

    public static FilterFragment newInstance(RangeSeekBar<Double> seekBars) {

        FilterFragment f = new FilterFragment();
        Bundle args = new Bundle();
        args.putParcelable("seekbar", seekBars);
        f.setArguments(args);

        return f;
    }

    public static FilterFragment newInstance(ArrayList<RangeSeekBar<Double>> seekBars) {

        FilterFragment f = new FilterFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("seekbars", seekBars);
        f.setArguments(args);

        return f;
    }

    public static FilterFragment newInstance() {
        return new FilterFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bottom_sheet_filtering, container,
                false);

        //seekBarListView = view.findViewById(R.id.seek_bar_list_view);

        seekBarsView = view.findViewById(R.id.seek_bars_view);


        if (getArguments() != null && getArguments().getParcelableArrayList("seekbars") != null) {
            rangeSeekBars = getArguments().getParcelableArrayList("seekbars");
            if (rangeSeekBars != null) {
                for (RangeSeekBar<Double> rangeSeekBar : rangeSeekBars) {
                    seekBarsView.addView (rangeSeekBar);
                }
            }
        }


        applyFilterButton = view.findViewById(R.id.apply_filter_button);
        applyFilterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
/*                mFilter.setSingleScoreLow(seekBarSingleScore.getSelectedMinValue());
                mFilter.setSingleScoreHigh(seekBarSingleScore.getSelectedMaxValue());*/
                mListener.OnBottomDialogFilterFragmentInteraction(rangeSeekBars);
            }
        });
        // get the views and attach the listener

        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBottomDialogFilterFragmentListener) {
            mListener = (OnBottomDialogFilterFragmentListener) context;
            //mFilter = new FilterValues();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBottomDialogFilterFragmentListener");
        }
    }

    public interface OnBottomDialogFilterFragmentListener {
        void OnBottomDialogFilterFragmentInteraction(ArrayList<RangeSeekBar<Double>> seekBars);
    }
}