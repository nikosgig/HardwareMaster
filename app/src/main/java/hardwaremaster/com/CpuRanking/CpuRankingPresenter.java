package hardwaremaster.com.CpuRanking;


import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import androidx.annotation.NonNull;
import hardwaremaster.com.Filter.FilterContract;
import hardwaremaster.com.R;
import hardwaremaster.com.data.Cpu;
import hardwaremaster.com.data.FilterValues;
import hardwaremaster.com.data.RangeSeekBarValues;
import hardwaremaster.com.widgets.RangeSeekBar;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link CpuRankingFragment}), retrieves the data and updates
 * the UI as required.
 */
public class CpuRankingPresenter implements CpuRankingContract.Presenter, FilterContract.Presenter {

    private final CpuRankingContract.View mCpuRankingsView;
    private final FilterContract.View mFilterView;
    private CpuRankingInteractor mCpuRankingInteractor;
    private CpuRankingSortBy mCurrentOrderBy = CpuRankingSortBy.ALL;


    public CpuRankingPresenter(@NonNull CpuRankingContract.View cpuRankingView, FilterContract.View filterView) {
        mCpuRankingsView = checkNotNull(cpuRankingView, "cpuRankings cannot be null!");
        mFilterView = checkNotNull(filterView, "filterView cannot be null!");
        mCpuRankingsView.setPresenter(this);
        mFilterView.setPresenter(this);
        mCpuRankingInteractor = new CpuRankingInteractor(this);
    }

    @Override
    public void loadCpuRanking() {
        mCpuRankingInteractor.getCpus();
    }

    @Override
    public void refreshCpuList(ArrayList<Cpu> cpuRankingList) {

        switch (mCurrentOrderBy) {
            case ALL:
                break;
            case BY_MODEL:
                Collections.sort(cpuRankingList, new Comparator<Cpu>() {
                    public int compare(Cpu v1, Cpu v2) {
                        return v1.getModel().compareTo(v2.getModel());
                    }
                });
            break;
        }

        mCpuRankingsView.showCpuRanking(cpuRankingList);
    }

    @Override
    public void filterItems(ArrayList<RangeSeekBar> rangeSeekBars) {

        FilterValues filterValues = new FilterValues();
        filterValues.setSingleScoreMin((Double) rangeSeekBars.get(0).getSelectedMinValue());
        filterValues.setSingleScoreMax((Double) rangeSeekBars.get(0).getSelectedMaxValue());
        filterValues.setMultiCoreMin((Double) rangeSeekBars.get(1).getSelectedMinValue());
        filterValues.setMultiCoreMax((Double) rangeSeekBars.get(1).getSelectedMaxValue());

        mCpuRankingInteractor.filterItems(filterValues);
    }

    @Override
    public void setOrder(CpuRankingSortBy orderType) {
        mCurrentOrderBy = orderType;
    }

    @Override
    public void start() {
        loadCpuRanking();
    }

    @Override
    public void generateRangeSeekBars(ArrayList<RangeSeekBar> seekBarsToShow) {
        mFilterView.showRangeSeekBars(seekBarsToShow);
    }

    @Override
    public RangeSeekBarValues getFilterMinMaxValues() {
        return mCpuRankingInteractor.getFilterMinMaxValues();
    }
}
