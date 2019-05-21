package hardwaremaster.com.Filter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Range;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import hardwaremaster.com.R;
import hardwaremaster.com.widgets.RangeSeekBar;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class FilterFragment extends BottomSheetDialogFragment {

    private OnBottomDialogFilterFragmentListener mListener;
    private ArrayList<RangeSeekBar> rangeSeekBars = new ArrayList<>();
    private LinearLayout seekBarHolder;
    private Button applyFilterButton;

    public FilterFragment() {
    }

    public static FilterFragment newInstance() {
        return new FilterFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mSeekBarListAdapter = new FilterAdapter(new ArrayList<RangeSeekBar>(0));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bottom_sheet_filtering, container,
                false);


        //Set up seek bars
        rangeSeekBars = mListener.OnRangeSeekBarInit();
        seekBarHolder = view.findViewById(R.id.seekbar_placeholder);
        for(RangeSeekBar rangeSeekBar: rangeSeekBars) {
            if(rangeSeekBar.getParent() != null) {
                ((ViewGroup)rangeSeekBar.getParent()).removeView(rangeSeekBar); // <- fix
            }
            seekBarHolder.addView(rangeSeekBar);

        }

        //setup apply button
        applyFilterButton = view.findViewById(R.id.apply_filter_button);
        applyFilterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
/*                mFilter.setSingleScoreLow(seekBarSingleScore.getSelectedMinValue());
                mFilter.setSingleScoreHigh(seekBarSingleScore.getSelectedMaxValue());*/
                mListener.OnBottomDialogFilterFragmentInteraction();
            }
        });
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
        void OnBottomDialogFilterFragmentInteraction();
        ArrayList<RangeSeekBar> OnRangeSeekBarInit();
    }
}