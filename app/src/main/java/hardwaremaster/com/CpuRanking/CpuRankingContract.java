package hardwaremaster.com.CpuRanking;

import java.util.ArrayList;
import java.util.List;

import hardwaremaster.com.Base.BasePresenter;
import hardwaremaster.com.Base.BaseView;
import hardwaremaster.com.data.Cpu;
import hardwaremaster.com.data.FilterValues;
import hardwaremaster.com.data.RangeSeekBarValues;
import hardwaremaster.com.widgets.RangeSeekBar;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface CpuRankingContract {
    interface View extends BaseView<Presenter> {
        void showCpuRanking(List<Cpu> tasks);
        //void showOrderByMenu();
    }

    interface Presenter extends BasePresenter {
        void loadCpuRanking();
        void refreshCpuList(ArrayList<Cpu> arrayList);
        void filterItems(ArrayList<RangeSeekBar> rangeSeekBars);
        void setOrder(CpuRankingSortBy orderType);

    }
}
