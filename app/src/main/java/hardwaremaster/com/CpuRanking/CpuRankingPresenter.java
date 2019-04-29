package hardwaremaster.com.CpuRanking;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import androidx.annotation.NonNull;
import hardwaremaster.com.data.Cpu;
import hardwaremaster.com.data.FilterValues;
import hardwaremaster.com.data.RangeSeekBarValues;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link CpuRankingFragment}), retrieves the data and updates
 * the UI as required.
 */
public class CpuRankingPresenter implements CpuRankingContract.Presenter{

    private final CpuRankingContract.View mCpuRankingsView;
    private CpuRankingInteractor mCpuRankingInteractor;
    private CpuRankingSortBy mCurrentOrderBy = CpuRankingSortBy.ALL;


    public CpuRankingPresenter(@NonNull CpuRankingContract.View cpuRankingView) {
        mCpuRankingsView = checkNotNull(cpuRankingView, "tasksView cannot be null!");
        mCpuRankingsView.setPresenter(this);
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
    public RangeSeekBarValues getFilterMinMaxValues() {
        RangeSeekBarValues rangeSeekBarValues = mCpuRankingInteractor.getFilterMinMaxValues();
        return rangeSeekBarValues;
    }

    @Override
    public void filterItems(FilterValues filterValues) {
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
}
