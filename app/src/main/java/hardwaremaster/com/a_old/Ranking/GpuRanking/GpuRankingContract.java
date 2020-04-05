package hardwaremaster.com.a_old.Ranking.GpuRanking;

import android.widget.Filter;

import java.util.List;

import hardwaremaster.com.a_old.Base.BasePresenter;
import hardwaremaster.com.a_old.Base.BaseView;
import hardwaremaster.com.a_old.data.Gpu;

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
