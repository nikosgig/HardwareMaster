package hardwaremaster.com.Ranking;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import androidx.annotation.NonNull;
import hardwaremaster.com.data.Cpu;
import hardwaremaster.com.data.DatabaseCalls;
import hardwaremaster.com.data.FilterValues;
import hardwaremaster.com.data.RangeSeekBarValues;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link RankingFragment}), retrieves the data and updates
 * the UI as required.
 */
public class RankingPresenter implements RankingContract.Presenter {

    private final RankingContract.View mRankingsView;
    private DatabaseCalls mDatabaseCalls;
    private CpuRankingSortBy mCurrentOrderBy = CpuRankingSortBy.ALL;


    public RankingPresenter(@NonNull RankingContract.View RankingView) {
        mRankingsView = checkNotNull(RankingView, "cpuRankings cannot be null!");
        mRankingsView.setPresenter(this);
        mDatabaseCalls = new DatabaseCalls(this);
    }



    @Override
    public void loadCpuRanking() {
        mDatabaseCalls.getCpus();
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

        mRankingsView.showCpuRanking(cpuRankingList);
    }

    @Override
    public void filterItems(FilterValues filterValues) {


/*        filterValues.setSingleScoreMin((Double) rangeSeekBars.get(0).getSelectedMinValue());
        filterValues.setSingleScoreMax((Double) rangeSeekBars.get(0).getSelectedMaxValue());
        filterValues.setMultiCoreMin((Double) rangeSeekBars.get(1).getSelectedMinValue());
        filterValues.setMultiCoreMax((Double) rangeSeekBars.get(1).getSelectedMaxValue());*/

        mDatabaseCalls.filterItems(filterValues);
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
    public RangeSeekBarValues getFilterMinMaxValues() {
        return mDatabaseCalls.getFilterMinMaxValues();
    }

}
