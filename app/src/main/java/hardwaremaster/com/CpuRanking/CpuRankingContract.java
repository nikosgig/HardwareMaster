package hardwaremaster.com.CpuRanking;

import java.util.List;

import hardwaremaster.com.BasePresenter;
import hardwaremaster.com.BaseView;
import hardwaremaster.com.data.Cpu;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface CpuRankingContract {
    interface View extends BaseView<Presenter> {
        void showCpuRanking(List<Cpu> tasks);
    }

    interface Presenter extends BasePresenter {
        void loadCpuRanking();
    }
}
