package hardwaremaster.com.CpuRanking;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import hardwaremaster.com.data.Cpu;

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
    public void setOrder(CpuRankingSortBy orderType) {
        mCurrentOrderBy = orderType;
    }

    @Override
    public void start() {
        loadCpuRanking();
    }
}
