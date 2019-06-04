package hardwaremaster.com.Ranking.CpuRanking;


import android.widget.Filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import javax.inject.Inject;

import hardwaremaster.com.data.Cpu;
import hardwaremaster.com.data.CpuFilterValues;
import hardwaremaster.com.data.Database;
import hardwaremaster.com.data.DatabaseCalls;
import hardwaremaster.com.di.ActivityScoped;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link CpuRankingFragment}), retrieves the data and updates
 * the UI as required.
 */
@ActivityScoped
public class CpuRankingPresenter implements CpuRankingContract.Presenter {

    @Nullable
    private CpuRankingContract.View mRankingsView;
    private final Database mDatabase;
    private CpuRankingSortBy mCurrentOrderBy = CpuRankingSortBy.ALL;


    @Inject
    public CpuRankingPresenter(@NonNull Database database) {
        mDatabase = database;
    }



    @Override
    public void getCpuFromDatabase() {
        mDatabase.getCpus(new DatabaseCalls.LoadCpusCallback() {
            @Override
            public void onCpusLoaded(ArrayList<Cpu> cpuList) {
                mRankingsView.notifyCpuListChanged(cpuList);
            }
        });
    }

    @Override
    public void onGetCpuFromDatabase(List<Cpu> cpuRankingList) {
        switch (mCurrentOrderBy) {
            case ALL:
                break;
            case BY_MODEL:
                Collections.sort(cpuRankingList, new Comparator<Cpu>() {
                    public int compare(Cpu v1, Cpu v2) {
                        return v1.getModel().compareTo(v2.getModel());
                    }
                });
                break;
        }

        mRankingsView.notifyCpuListChanged(cpuRankingList);

    }

    @Override
    public void applyFiltersForCpuList(CpuFilterValues filterValues) {
        mRankingsView.notifyCpuListChanged(mDatabase.filterCpuList(filterValues));
    }

    @Override
    public void setOrder(CpuRankingSortBy orderType) {
        mCurrentOrderBy = orderType;
    }

    @Override
    public Filter getSearchBarFilter() {
        Filter listFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                results.values = mDatabase.searchFilterCpuList(constraint);
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mRankingsView.notifyCpuListChanged((List) results.values);
            }
        };
        return listFilter;
    }

    @Override
    public CpuFilterValues getCpuFilterValuesToShow() {
        return mDatabase.getFilterMinMaxValues();
    }

    @Override
    public void takeView(CpuRankingContract.View view) {
        mRankingsView = view;
        getCpuFromDatabase();
    }

    @Override
    public void dropView() {
        mRankingsView = null;
    }

}
