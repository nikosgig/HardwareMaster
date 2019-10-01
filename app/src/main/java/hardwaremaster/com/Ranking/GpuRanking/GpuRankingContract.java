package hardwaremaster.com.Ranking.GpuRanking;

import android.widget.Filter;

import java.util.List;

import hardwaremaster.com.Base.BasePresenter;
import hardwaremaster.com.Base.BaseView;
import hardwaremaster.com.Ranking.CpuRanking.CpuRankingSortBy;
import hardwaremaster.com.data.Cpu;
import hardwaremaster.com.data.CpuFilterValues;
import hardwaremaster.com.data.FilterValues;
import hardwaremaster.com.data.Gpu;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface GpuRankingContract {
    interface View extends BaseView<Presenter> {
        void notifyGpuListChanged(List<Gpu> gpuList);
    }

    interface Presenter extends BasePresenter<View> {
        void getGpuFromDatabase();
        Filter getSearchBarFilter();
        void updatePrice(String key, double price);

    }
}
