//package hardwaremaster.com.Ranking.GpuRanking.Filter;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.LinearLayout;
//
//import androidx.annotation.IdRes;
//
//import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
//import com.google.android.material.chip.ChipGroup;
//import com.google.firebase.database.annotations.Nullable;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.inject.Inject;
//
//import hardwaremaster.com.R;
//import hardwaremaster.com.Ranking.GpuRanking.GpuRankingContract;
//import hardwaremaster.com.Ranking.GpuRanking.GpuRankingSortBy;
//import hardwaremaster.com.data.Gpu;
//import hardwaremaster.com.di.ActivityScoped;
//import hardwaremaster.com.widgets.RangeSeekBar;
//
//@ActivityScoped
//public class GpuFilterFragment extends BottomSheetDialogFragment implements GpuRankingContract.View {
//
//    @Inject
//    GpuRankingContract.Presenter mPresenter;
//    //private OnBottomDialogFilterFragmentListener mListener;
//    private ArrayList<RangeSeekBar> rangeSeekBars = new ArrayList<>();
//    private LinearLayout seekBarHolder;
//    private Button applyFilterButton;
//    ChipGroup chipGroup;
//
//    @Inject
//    public GpuFilterFragment() {
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //mSeekBarListAdapter = new FilterAdapter(new ArrayList<RangeSeekBar>(0));
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        mPresenter.takeView(this);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mPresenter.dropView();  //prevent leaking activity in
//        // case presenter is orchestrating a long running task
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater,
//                             @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.fragment_bottom_sheet_filtering, container,
//                false);
//
//
//        //Set up seek bars
///*        rangeSeekBars = mListener.OnGpuRangeSeekBarInit();
//        seekBarHolder = view.findViewById(R.id.seekbar_placeholder);
//        for(RangeSeekBar rangeSeekBar: rangeSeekBars) {
//            if(rangeSeekBar.getParent() != null && rangeSeekBars!= null) {
//                ((ViewGroup)rangeSeekBar.getParent()).removeView(rangeSeekBar); // <- fix
//            }
//            seekBarHolder.addView(rangeSeekBar);
//
//        }*/
//
//        ChipGroup filterChipGroupSingleSelection = view.findViewById(R.id.sort_chip_group);
//        filterChipGroupSingleSelection.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(ChipGroup group, @IdRes int checkedId) {
//                // Handle the checked chip change.
//                //Log.i("bwoah", "ID: " + group.getCheckedChipId());
//                switch (group.getCheckedChipId()) {
//                    case R.id.sort_price:
//                        mPresenter.setSorting(GpuRankingSortBy.BY_PRICE);
//                        break;
//                    case R.id.sort_1080p:
//                        mPresenter.setSorting(GpuRankingSortBy.BY_1080P);
//                        break;
//                    case R.id.sort_2k:
//                        mPresenter.setSorting(GpuRankingSortBy.BY_2K);
//                        break;
//                    case R.id.sort_4k:
//                        mPresenter.setSorting(GpuRankingSortBy.BY_4K);
//                        break;
//                    default:
//                        mPresenter.setSorting(GpuRankingSortBy.ALL);
//                        break;
//                }
//            }
//        });
//
//        //setup apply button
//        applyFilterButton = view.findViewById(R.id.apply_filter_button);
//        applyFilterButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
///*                mFilter.setSingleScoreLow(seekBarSingleScore.getSelectedMinValue());
//                mFilter.setSingleScoreHigh(seekBarSingleScore.getSelectedMaxValue());*/
//                //mListener.OnGpuFragmentInteraction();
//                mPresenter.getGpuFromDatabase();
//                dismiss();
//            }
//        });
//        return view;
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
///*        if (context instanceof OnBottomDialogFilterFragmentListener) {
//            mListener = (OnBottomDialogFilterFragmentListener) context;
//            //mFilter = new FilterValues();
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnBottomDialogFilterFragmentListener");
//        }*/
//    }
//
//    @Override
//    public void notifyGpuListChanged(List<Gpu> gpuList) {
//
//    }
//
//    @Override
//    public void showHideFiltersView() {
//
//    }
//
///*    public interface OnBottomDialogFilterFragmentListener {
//        void OnGpuFragmentInteraction();
//        //ArrayList<RangeSeekBar> OnGpuRangeSeekBarInit();
//    }*/
//}