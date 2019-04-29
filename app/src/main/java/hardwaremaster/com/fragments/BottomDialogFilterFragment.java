package hardwaremaster.com.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hardwaremaster.com.R;
import hardwaremaster.com.data.FilterValues;
import hardwaremaster.com.widgets.RangeSeekBar;

public class BottomDialogFilterFragment extends BottomSheetDialogFragment {

    private OnBottomDialogFilterFragmentListener mListener;
    private Button applyFilterButton;
    //private RangeSeekBar<Integer> seekBarSingleScore;
    private FrameLayout seekBarListView;
    //private FilterValues mFilter;

    public static BottomDialogFilterFragment newInstance(RangeSeekBar<Double> seekBars) {

        BottomDialogFilterFragment f = new BottomDialogFilterFragment();
        Bundle args = new Bundle();
        args.putParcelable("seekbar", seekBars);
        f.setArguments(args);

        return f;
    }

    public static BottomDialogFilterFragment newInstance() {
        return new BottomDialogFilterFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bottom_sheet_filtering, container,
                false);

        //seekBarListView = view.findViewById(R.id.seek_bar_list_view);

        FrameLayout layout = view.findViewById(R.id.seek_bar_single_score);
        layout.addView((View) getArguments().getParcelable("seekbar"));


        applyFilterButton = view.findViewById(R.id.apply_filter_button);
        applyFilterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
/*                mFilter.setSingleScoreLow(seekBarSingleScore.getSelectedMinValue());
                mFilter.setSingleScoreHigh(seekBarSingleScore.getSelectedMaxValue());*/
                mListener.OnBottomDialogFilterFragmentInteraction((RangeSeekBar<Double>) getArguments(). getParcelable("seekbar"));
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
        void OnBottomDialogFilterFragmentInteraction(RangeSeekBar<Double> seekBars);
    }
}