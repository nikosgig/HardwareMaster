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
        //void showOrderByMenu();
    }

    interface Presenter extends BasePresenter {
        void getCpuFromDatabase();
        void applyFiltersForCpuList(CpuFilterValues cpuFilterValues);
        CpuFilterValues getCpuFilterValuesToShow();
        void onGetCpuFromDatabase(List<Cpu> cpuList);
        void setOrder(CpuRankingSortBy orderType);
        Filter getSearchBarFilter();

    }
}
