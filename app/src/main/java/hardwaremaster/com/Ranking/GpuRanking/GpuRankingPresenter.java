package hardwaremaster.com.Ranking.GpuRanking;


import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import hardwaremaster.com.Ranking.CpuRanking.CpuRankingFragment;
import hardwaremaster.com.Ranking.CpuRanking.CpuRankingSortBy;
import hardwaremaster.com.data.CpuFilterValues;
import hardwaremaster.com.data.DatabaseCalls;
import hardwaremaster.com.data.Gpu;
import hardwaremaster.com.data.GpuFilterValues;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link CpuRankingFragment}), retrieves the data and updates
 * the UI as required.
 */
public class GpuRankingPresenter implements GpuRankingContract.Presenter {

    private final GpuRankingContract.View mRankingsView;
    private DatabaseCalls mDatabaseCalls;
    private CpuRankingSortBy mCurrentOrderBy = CpuRankingSortBy.ALL;


    public GpuRankingPresenter(@NonNull GpuRankingContract.View RankingView) {
        mRankingsView = checkNotNull(RankingView, "GpuRankings cannot be null!");
        mRankingsView.setPresenter(this);
        mDatabaseCalls = new DatabaseCalls(this);
    }



    @Override
    public void getGpuFromDatabase() {
        mDatabaseCalls.getGpus();

    }

    @Override
    public void onGetCpuFromDatabase(List<Gpu> gpuRankingList) {
        mRankingsView.notifyGpuListChanged(gpuRankingList);
    }

    @Override
    public void applyFiltersForGpuList(GpuFilterValues filterValues) {
        mRankingsView.notifyGpuListChanged(mDatabaseCalls.filterGpuList(filterValues));
    }

    @Override
    public Filter getSearchBarFilter() {
        Filter listFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                results.values = mDatabaseCalls.searchFilterGpuList(constraint);
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mRankingsView.notifyGpuListChanged((List) results.values);
            }
        };
        return listFilter;
    }


    @Override
    public void setOrder(CpuRankingSortBy orderType) {
        mCurrentOrderBy = orderType;
    }

    @Override
    public CpuFilterValues getGpuFilterValuesToShow() {
        return mDatabaseCalls.getFilterMinMaxValues();
    }



}
