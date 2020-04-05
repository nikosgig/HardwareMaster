package hardwaremaster.com.a_old.Ranking.CpuRanking;


import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import javax.inject.Inject;

import hardwaremaster.com.a_old.data.Cpu;
import hardwaremaster.com.a_old.data.Database;
import hardwaremaster.com.a_old.data.DatabaseCalls;
import hardwaremaster.com.a_old.di.ActivityScoped;

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
    public void takeView(CpuRankingContract.View view) {
        mRankingsView = view;
        getCpuFromDatabase();
    }

    @Override
    public void dropView() {
        mRankingsView = null;
    }

}
