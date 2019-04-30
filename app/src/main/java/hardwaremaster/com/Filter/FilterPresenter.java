package hardwaremaster.com.Filter;

import java.util.ArrayList;
import java.util.logging.Filter;

import androidx.annotation.NonNull;
import hardwaremaster.com.CpuRanking.CpuRankingContract;
import hardwaremaster.com.CpuRanking.CpuRankingInteractor;
import hardwaremaster.com.widgets.RangeSeekBar;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class FilterPresenter implements FilterContract.Presenter {

    private final FilterContract.View mFilterContractView;
    private CpuRankingInteractor mCpuRankingInteractor;

    public FilterPresenter(@NonNull FilterContract.View filterContractView) {
        mFilterContractView = checkNotNull(filterContractView, "tasksView cannot be null!");
        mFilterContractView.setPresenter(this);
        //mCpuRankingInteractor = new CpuRankingInteractor(this);
    }

    @Override
    public void generateRangeSeekBars(ArrayList<RangeSeekBar> rangeSeekBars) {
        mFilterContractView.showRangeSeekBars(rangeSeekBars);
    }

    @Override
    public void start() {
    }
}
