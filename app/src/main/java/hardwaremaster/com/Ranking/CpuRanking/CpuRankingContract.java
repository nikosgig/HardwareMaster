package hardwaremaster.com.Ranking.CpuRanking;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import hardwaremaster.com.Base.BasePresenter;
import hardwaremaster.com.Base.BaseView;
import hardwaremaster.com.data.Cpu;
import hardwaremaster.com.data.CpuFilterValues;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface CpuRankingContract {
    interface View extends BaseView<Presenter> {
        void notifyCpuListChanged(List<Cpu> cpuList);
    }

    interface Presenter extends BasePresenter<View> {
        void getCpuFromDatabase();
        Filter getSearchBarFilter();

    }
}
