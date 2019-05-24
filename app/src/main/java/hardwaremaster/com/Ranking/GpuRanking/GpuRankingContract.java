package hardwaremaster.com.Ranking.GpuRanking;

import java.util.List;

import hardwaremaster.com.Base.BasePresenter;
import hardwaremaster.com.Base.BaseView;
import hardwaremaster.com.Ranking.CpuRanking.CpuRankingSortBy;
import hardwaremaster.com.data.CpuFilterValues;
import hardwaremaster.com.data.FilterValues;
import hardwaremaster.com.data.Gpu;
import hardwaremaster.com.data.GpuFilterValues;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface GpuRankingContract {
    interface View extends BaseView<Presenter> {
        void notifyGpuListChanged(List<Gpu> gpuList);
        //void showOrderByMenu();
    }

    interface Presenter extends BasePresenter {
        void getGpuFromDatabase();
        void applyFiltersForGpuList(GpuFilterValues gpuFilterValues);
        CpuFilterValues getGpuFilterValuesToShow();
        void onGetCpuFromDatabase(List<Gpu> cpuList);
        void setOrder(CpuRankingSortBy orderType);

    }
}
