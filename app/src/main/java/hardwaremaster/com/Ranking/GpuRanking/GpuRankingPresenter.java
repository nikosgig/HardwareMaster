package hardwaremaster.com.Ranking.GpuRanking;


import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import javax.inject.Inject;

import hardwaremaster.com.Ranking.CpuRanking.CpuRankingFragment;
import hardwaremaster.com.Ranking.CpuRanking.CpuRankingSortBy;
import hardwaremaster.com.data.CpuFilterValues;
import hardwaremaster.com.data.Database;
import hardwaremaster.com.data.DatabaseCalls;
import hardwaremaster.com.data.Gpu;
import hardwaremaster.com.data.GpuFilterValues;
import hardwaremaster.com.di.ActivityScoped;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link CpuRankingFragment}), retrieves the data and updates
 * the UI as required.
 */
@ActivityScoped
public class GpuRankingPresenter implements GpuRankingContract.Presenter {

    @Nullable
    private GpuRankingContract.View mRankingsView;
    private final Database mDatabase;
    private CpuRankingSortBy mCurrentOrderBy = CpuRankingSortBy.ALL;

    @Inject
    public GpuRankingPresenter(Database database) {
        mDatabase = database;
    }



    @Override
    public void getGpuFromDatabase() {
        mDatabase.getGpus(new DatabaseCalls.LoadGpusCallback() {
            @Override
            public void onGpusLoaded(ArrayList<Gpu> gpuList) {
                mRankingsView.notifyGpuListChanged(gpuList);
            }
        });
    }

/*    @Override
    public void onGetCpuFromDatabase(List<Gpu> gpuRankingList) {
        mRankingsView.notifyGpuListChanged(gpuRankingList);
    }*/

    @Override
    public void applyFiltersForGpuList(GpuFilterValues filterValues) {
        mRankingsView.notifyGpuListChanged(mDatabase.filterGpuList(filterValues));
    }

    @Override
    public Filter getSearchBarFilter() {
        Filter listFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                results.values = mDatabase.searchFilterGpuList(constraint);
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
        return mDatabase.getFilterMinMaxValues();
    }

    @Override
    public void takeView(GpuRankingContract.View view) {
        mRankingsView = view;
        getGpuFromDatabase();
    }

    @Override
    public void dropView() {
        mRankingsView = null;
    }


}
