package hardwaremaster.com.Ranking.GpuRanking;


import android.widget.Filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import androidx.annotation.Nullable;

import javax.inject.Inject;

import hardwaremaster.com.Ranking.CpuRanking.CpuRankingFragment;
import hardwaremaster.com.Ranking.GpuRanking.Filter.GpuFilterValues;
import hardwaremaster.com.data.CpuFilterValues;
import hardwaremaster.com.data.Database;
import hardwaremaster.com.data.DatabaseCalls;
import hardwaremaster.com.data.Gpu;
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
    private GpuRankingSortBy mCurrentSortBy = GpuRankingSortBy.BY_SCORE;
    private GpuFilterValues gpuFilterValues = new GpuFilterValues();
    private boolean alreadyInit;

    @Inject
    public GpuRankingPresenter(Database database) {
        mDatabase = database;
    }



    @Override
    public void getGpuFromDatabase() {
        mDatabase.getGpus(gpuFilterValues, new DatabaseCalls.LoadGpusCallback() {
            @Override
            public void onGpusLoaded(ArrayList<Gpu> gpuList) {

                for ( Gpu curGpu : gpuList)
                {
                    if(curGpu.getPrice()!=0) {
                        curGpu.setScore((curGpu.getAvgFps1080p() + curGpu.getAvgFps2k() + curGpu.getAvgFps4k())/curGpu.getPrice());
                    } else {
                        curGpu.setScore(0.0);
                    }
                }

                if(!alreadyInit) {
                    Gpu max = Collections.max(gpuList, ((o1, o2) -> o1.getPrice().compareTo(o2.getPrice())));
                    Gpu min = Collections.min(gpuList, ((o1, o2) -> o1.getPrice().compareTo(o2.getPrice())));
                    mRankingsView.setPriceBarMinMaxValues(min.getPrice(), max.getPrice());
                    alreadyInit = true;
                }


/*                if(gpuFilterValues.getMaxPrice() != max.getPrice() || gpuFilterValues.getMinPrice() != min.getPrice()) {
                    mRankingsView.setPriceBarSelectedMinMaxValues(gpuFilterValues.getMinPrice(), gpuFilterValues.getMaxPrice());
                }*/

                switch (mCurrentSortBy) {
                    case BY_PRICE:
                        Collections.sort(gpuList,
                                (o1, o2) -> o1.getPrice().compareTo(o2.getPrice()));
                        break;
                    case BY_1080P:
                        Collections.sort(gpuList,
                                (o1, o2) -> o1.getAvgFps1080p().compareTo(o2.getAvgFps1080p()));
                        break;
                    case BY_2K:
                        Collections.sort(gpuList,
                                (o1, o2) -> o1.getAvgFps2k().compareTo(o2.getAvgFps2k()));
                        break;
                    case BY_4K:
                        Collections.sort(gpuList,
                                (o1, o2) -> o1.getAvgFps4k().compareTo(o2.getAvgFps4k()));
                        break;
                    case BY_SCORE:
                        Collections.sort(gpuList,
                                (o1, o2) -> o1.getScore().compareTo(o2.getScore()));
                        break;
                    default:
                        break;

                }
                mRankingsView.notifyGpuListChanged(gpuList);
            }
        });
    }

/*    @Override
    public void onGetCpuFromDatabase(List<Gpu> gpuRankingList) {
        mRankingsView.notifyGpuListChanged(gpuRankingList);
    }*/

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
    public void setSorting(GpuRankingSortBy orderType) {
        mCurrentSortBy = orderType;
    }

    @Override
    public void setMinPrice(double minPrice) {
        gpuFilterValues.setMinPrice(minPrice);

    }

    @Override
    public void setMaxPrice(double maxPrice) {
        gpuFilterValues.setMaxPrice(maxPrice);
    }

    @Override
    public void addVRamCapacity(double vRamCapacity) {
        gpuFilterValues.setvRamCapacity(vRamCapacity);
    }

    @Override
    public void showHideFilters() {
        mRankingsView.showHideFiltersView();
    }

    @Override
    public void updatePrice(String key, double price) {
        mDatabase.addUserPrice(key, price);
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
