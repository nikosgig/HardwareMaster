package hardwaremaster.com.Filter;

import android.content.Context;
import android.os.Bundle;
import android.util.Range;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import hardwaremaster.com.R;
import hardwaremaster.com.widgets.RangeSeekBar;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class FilterFragment extends BottomSheetDialogFragment implements FilterContract.View {

    private OnBottomDialogFilterFragmentListener mListener;
    private FilterContract.Presenter mPresenter;
    private Button applyFilterButton;
    private RecyclerView mSeekBarsView;
    private FilterAdapter mSeekBarListAdapter = new FilterAdapter(new ArrayList<RangeSeekBar>(0));

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
        mSeekBarsView = view.findViewById(R.id.seek_bars_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mSeekBarsView.setLayoutManager(layoutManager);
        mSeekBarsView.setAdapter(mSeekBarListAdapter);

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
    public void onResume() {
        super.onResume();
        mPresenter.start();
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

    /* CpuRankingContract.View callbacks*/
    @Override
    public void showRangeSeekBars(ArrayList<RangeSeekBar> rangeSeekBars) {
        mSeekBarListAdapter.setList(rangeSeekBars);
        mSeekBarListAdapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(FilterContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    public interface OnBottomDialogFilterFragmentListener {
        void OnBottomDialogFilterFragmentInteraction();
    }
}