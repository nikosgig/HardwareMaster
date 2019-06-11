package hardwaremaster.com.Ranking.GpuRanking;

import android.widget.Filter;

import java.util.List;

import hardwaremaster.com.Base.BasePresenter;
import hardwaremaster.com.Base.BaseView;
import hardwaremaster.com.Ranking.CpuRanking.CpuRankingSortBy;
import hardwaremaster.com.data.CpuFilterValues;
import hardwaremaster.com.data.FilterValues;
import hardwaremaster.com.data.Gpu;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface GpuRankingContract {
    interface View extends BaseView<Presenter> {
        void notifyGpuListChanged(List<Gpu> gpuList);
        void showHideFiltersView();
        void setPriceBarMinMaxValues(double min, double max);
        void setPriceBarSelectedMinMaxValues(double min, double max);
    }

    interface Presenter extends BasePresenter<View> {
        void getGpuFromDatabase();
        //void applyFiltersForGpuList(GpuFilterValues gpuFilterValues);
        CpuFilterValues getGpuFilterValuesToShow();
        //void onGetCpuFromDatabase(List<Gpu> cpuList);
        Filter getSearchBarFilter();
        void setSorting(GpuRankingSortBy orderType);
        void setMinPrice(double minPrice);
        void setMaxPrice(double maxPrice);
        void addVRamCapacity(double vRamCapacity);
        void showHideFilters();

    }
}
