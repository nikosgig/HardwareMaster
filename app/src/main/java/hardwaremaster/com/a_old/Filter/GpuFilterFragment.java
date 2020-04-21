package hardwaremaster.com.a_old.Filter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.annotations.Nullable;

import javax.inject.Inject;

import hardwaremaster.com.R;
import hardwaremaster.com.a_old.Ranking.GpuRanking.GpuRankingSortBy;
import hardwaremaster.com.a_old.di.ActivityScoped;
import hardwaremaster.com.a_old.util.FragmentUtils;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

@ActivityScoped
public class GpuFilterFragment extends BottomSheetDialogFragment implements GpuFilterContract.View {

    @Inject
    GpuFilterContract.Presenter mPresenter;
    private OnBottomDialogFilterFragmentListener mListener;
    private MaterialButton applyFilterButton;
    View view;
    private long FPS_MAX_VALUE = 144;
    private long DEFAULT_MIN_VALUE = 0;
    private long PRICE_MAX_VALUE = 1000;

    @Inject
    public GpuFilterFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                View bottomSheetInternal = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                bottomSheetInternal.setBackgroundColor(getResources().getColor(R.color.grey_1000));
                BottomSheetBehavior.from(bottomSheetInternal).setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        view = inflater.inflate(R.layout.fragment_filter, container,
                false);



        RadioGroup materialButtonToggleGroupSort =
                view.findViewById(R.id.sortByToggleGroup);
        materialButtonToggleGroupSort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.sort_price_high_to_low:
                            mPresenter.setSorting(GpuRankingSortBy.SORT_PRICE_HIGH_TO_LOW);
                            break;
                        case R.id.sort_price_low_to_high:
                            mPresenter.setSorting(GpuRankingSortBy.SORT_PRICE_LOW_TO_HIGH);
                            break;
                        case R.id.sort_vfm_high_to_low:
                            mPresenter.setSorting(GpuRankingSortBy.SORT_VFM_HIGH_TO_LOW);
                            break;
                        case R.id.sort_vfm_low_to_high:
                            mPresenter.setSorting(GpuRankingSortBy.SORT_VFM_LOW_TO_HIGH);
                            break;
                        default:
                            mPresenter.setSorting(GpuRankingSortBy.ALL);
                            break;
                }
            }

//            @Override
//            public void onButtonChecked(MaterialButtonToggleGroup group, @IdRes int checkedId,
//                                        boolean isChecked) {
//                if (isChecked) {
//                    switch (checkedId) {
//                        case R.id.sort_price:
//                            mPresenter.setSorting(GpuRankingSortBy.BY_PRICE);
//                            break;
//                        case R.id.sort_1080p:
//                            mPresenter.setSorting(GpuRankingSortBy.BY_1080P);
//                            break;
//                        case R.id.sort_2k:
//                            mPresenter.setSorting(GpuRankingSortBy.BY_2K);
//                            break;
//                        case R.id.sort_4k:
//                            mPresenter.setSorting(GpuRankingSortBy.BY_4K);
//                            break;
//                        default:
//                            mPresenter.setSorting(GpuRankingSortBy.ALL);
//                            break;
//                    }
//                }
//            }
        });


        //price bar
        RelativeLayout rangeSeekBarLayoutPrice = view.findViewById(R.id.rangeSeekBarPrice);
        CrystalRangeSeekbar rangeSeekbarPrice = FragmentUtils.generateRangeSeekBarView(rangeSeekBarLayoutPrice, R.string.price, DEFAULT_MIN_VALUE, PRICE_MAX_VALUE);
        rangeSeekbarPrice.setMinStartValue((float) mPresenter.getGpuFilterValues().getMinPrice());
        rangeSeekbarPrice.setMaxStartValue((float) mPresenter.getGpuFilterValues().getMaxPrice());
        rangeSeekbarPrice.apply();
        rangeSeekbarPrice.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
                mPresenter.setMinPrice(minValue);
                mPresenter.setMaxPrice(maxValue);
            }
        });

        //1080 bar
        RelativeLayout rangeSeekBarLayout1080 = view.findViewById(R.id.expandableViewFilters).findViewById(R.id.rangeSeekBar1080);
        CrystalRangeSeekbar rangeSeekbar1080 = FragmentUtils.generateRangeSeekBarView(rangeSeekBarLayout1080, R.string.sort_1080p, DEFAULT_MIN_VALUE, FPS_MAX_VALUE);
        rangeSeekbar1080.setMinStartValue((float) mPresenter.getGpuFilterValues().getMinFps1080p());
        rangeSeekbar1080.setMaxStartValue((float) mPresenter.getGpuFilterValues().getMaxFps1080p());
        rangeSeekbar1080.apply();
        rangeSeekbar1080.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
                mPresenter.setMin1080(minValue);
                mPresenter.setMax1080(maxValue);
            }
        });

        //2k bar
        RelativeLayout rangeSeekBarLayout2k = view.findViewById(R.id.expandableViewFilters).findViewById(R.id.rangeSeekBar2k);
        CrystalRangeSeekbar rangeSeekbar2k = FragmentUtils.generateRangeSeekBarView(rangeSeekBarLayout2k, R.string.sort_2k, DEFAULT_MIN_VALUE, FPS_MAX_VALUE);
        rangeSeekbar2k.setMinStartValue((float) mPresenter.getGpuFilterValues().getMinFps2k());
        rangeSeekbar2k.setMaxStartValue((float) mPresenter.getGpuFilterValues().getMaxFps2k());
        rangeSeekbar2k.apply();
        rangeSeekbar2k.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
                mPresenter.setMin2K(minValue);
                mPresenter.setMax2K(maxValue);
            }
        });

        //4k bar
        RelativeLayout rangeSeekBarLayout4k = view.findViewById(R.id.expandableViewFilters).findViewById(R.id.rangeSeekBar4k);
        CrystalRangeSeekbar rangeSeekbar4k = FragmentUtils.generateRangeSeekBarView(rangeSeekBarLayout4k, R.string.sort_4k, DEFAULT_MIN_VALUE, FPS_MAX_VALUE);
        rangeSeekbar4k.setMinStartValue((float) mPresenter.getGpuFilterValues().getMinFps4k());
        rangeSeekbar4k.setMaxStartValue((float) mPresenter.getGpuFilterValues().getMaxFps4k());
        rangeSeekbar4k.apply();
        rangeSeekbar4k.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
                mPresenter.setMin4K(minValue);
                mPresenter.setMax4K(maxValue);
            }
        });

        //Firestrike bar
        RelativeLayout rangeSeekBarLayoutFirestrike = view.findViewById(R.id.expandableViewFilters).findViewById(R.id.rangeSeekBarFirestrike);
        CrystalRangeSeekbar rangeSeekbarFirestrike = FragmentUtils.generateRangeSeekBarView(rangeSeekBarLayoutFirestrike, R.string.score_firestrike, DEFAULT_MIN_VALUE, 30000);
        rangeSeekbarFirestrike.setMinStartValue((float) mPresenter.getGpuFilterValues().getMinFirestrike());
        rangeSeekbarFirestrike.setMaxStartValue((float) mPresenter.getGpuFilterValues().getMaxFirestrike());
        rangeSeekbarFirestrike.apply();
        rangeSeekbarFirestrike.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
                mPresenter.setMinFirestrike(minValue);
                mPresenter.setMaxFirestrike(maxValue);
            }
        });

        //Passmark bar
        RelativeLayout rangeSeekBarLayoutPassmark = view.findViewById(R.id.expandableViewFilters).findViewById(R.id.rangeSeekBarPassmark);
        CrystalRangeSeekbar rangeSeekbarPassmark = FragmentUtils.generateRangeSeekBarView(rangeSeekBarLayoutPassmark, R.string.score_passmark, DEFAULT_MIN_VALUE, 15000);
        rangeSeekbarPassmark.setMinStartValue((float) mPresenter.getGpuFilterValues().getMinPassmark());
        rangeSeekbarPassmark.setMaxStartValue((float) mPresenter.getGpuFilterValues().getMaxPassmark());
        rangeSeekbarPassmark.apply();
        rangeSeekbarPassmark.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
                mPresenter.setMinPassmark(minValue);
                mPresenter.setMaxPassmark(maxValue);
            }
        });

//        MaterialButtonToggleGroup materialButtonToggleGroupVRam =
//                view.findViewById(R.id.vRamToggleGroup);
//        materialButtonToggleGroupVRam.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
//            @Override
//            public void onButtonChecked(MaterialButtonToggleGroup group, @IdRes int checkedId,
//                                        boolean isChecked) {
//                switch (checkedId) {
//                    case R.id.vRamButton1:
//                        Log.d("button", "clicked1");
//                        break;
//                    case R.id.vRamButton2:
//                        Log.d("button", "clicked2");
//                        break;
//                    case R.id.vRamButton3:
//                        Log.d("button", "clicked3");
//                        break;
//                    case R.id.vRamButton4:
//                        Log.d("button", "clicked4");
//                        break;
//                    default:
//                        Log.d("button", "clicked5");
//                        break;
//                }
//
//            }
//        });




        //setup apply button
        applyFilterButton = view.findViewById(R.id.apply_filter_button);
        applyFilterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
/*                mFilter.setSingleScoreLow(seekBarSingleScore.getSelectedMinValue());
                mFilter.setSingleScoreHigh(seekBarSingleScore.getSelectedMaxValue());*/
                mPresenter.setDatabaseGpuFilters();
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
    }
}