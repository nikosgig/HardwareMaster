package hardwaremaster.com.Ranking.CpuRanking;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import hardwaremaster.com.data.Cpu;
import hardwaremaster.com.data.CpuFilterValues;
import hardwaremaster.com.data.DatabaseCalls;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link CpuRankingFragment}), retrieves the data and updates
 * the UI as required.
 */
public class CpuRankingPresenter implements CpuRankingContract.Presenter {

    private final CpuRankingContract.View mRankingsView;
    private DatabaseCalls mDatabaseCalls;
    private CpuRankingSortBy mCurrentOrderBy = CpuRankingSortBy.ALL;


    public CpuRankingPresenter(@NonNull CpuRankingContract.View RankingView) {
        mRankingsView = checkNotNull(RankingView, "cpuRankings cannot be null!");
        mRankingsView.setPresenter(this);
        mDatabaseCalls = new DatabaseCalls(this);
    }



    @Override
    public void getCpuFromDatabase() {
        mDatabaseCalls.getCpus();

    }

    @Override
    public void onGetCpuFromDatabase(List<Cpu> cpuRankingList) {
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

        mRankingsView.notifyCpuListChanged(cpuRankingList);

    }

    @Override
    public void applyFiltersForCpuList(CpuFilterValues filterValues) {
        mRankingsView.notifyCpuListChanged(mDatabaseCalls.filterCpuList(filterValues));
    }

    @Override
    public void setOrder(CpuRankingSortBy orderType) {
        mCurrentOrderBy = orderType;
    }

    @Override
    public CpuFilterValues getCpuFilterValuesToShow() {
        return mDatabaseCalls.getFilterMinMaxValues();
    }

}
