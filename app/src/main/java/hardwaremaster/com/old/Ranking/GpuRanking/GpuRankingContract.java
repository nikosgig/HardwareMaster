package hardwaremaster.com.old.Ranking.GpuRanking;

import android.widget.Filter;

import java.util.List;

import hardwaremaster.com.old.Base.BasePresenter;
import hardwaremaster.com.old.Base.BaseView;
import hardwaremaster.com.old.data.Gpu;

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
