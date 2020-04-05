package hardwaremaster.com.old.Ranking.CpuRanking;

import android.widget.Filter;

import java.util.List;

import hardwaremaster.com.old.Base.BasePresenter;
import hardwaremaster.com.old.Base.BaseView;
import hardwaremaster.com.old.data.Cpu;

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
