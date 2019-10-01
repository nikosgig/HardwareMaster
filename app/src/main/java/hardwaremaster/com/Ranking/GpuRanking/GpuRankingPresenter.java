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
import hardwaremaster.com.data.Cpu;
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
    private GpuRankingSortBy mCurrentSortBy = GpuRankingSortBy.SORT_VFM_HIGH_TO_LOW;
    private GpuFilterValues gpuFilterValues = new GpuFilterValues();
    private boolean alreadyInit;

    @Inject
    public GpuRankingPresenter(Database database) {
        mDatabase = database;
    }



    @Override
    public void getGpuFromDatabase() {
        mDatabase.getGpus(new DatabaseCalls.LoadGpusCallback() {
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


/*                if(gpuFilterValues.getMaxPrice() != max.getPrice() || gpuFilterValues.getMinPrice() != min.getPrice()) {
                    mRankingsView.setPriceBarSelectedMinMaxValues(gpuFilterValues.getMinPrice(), gpuFilterValues.getMaxPrice());
                }*/

                switch (mCurrentSortBy) {
                    case SORT_PRICE_HIGH_TO_LOW:
                        Collections.sort(gpuList,
                                (o1, o2) -> o1.getPrice().compareTo(o2.getPrice()));
                        break;
//                    case BY_1080P:
//                        Collections.sort(gpuList,
//                                (o1, o2) -> o1.getAvgFps1080p().compareTo(o2.getAvgFps1080p()));
//                        break;
//                    case BY_2K:
//                        Collections.sort(gpuList,
//                                (o1, o2) -> o1.getAvgFps2k().compareTo(o2.getAvgFps2k()));
//                        break;
//                    case BY_4K:
//                        Collections.sort(gpuList,
//                                (o1, o2) -> o1.getAvgFps4k().compareTo(o2.getAvgFps4k()));
//                        break;
                    case SORT_VFM_HIGH_TO_LOW:
                        Collections.sort(gpuList, Collections.reverseOrder(
                                (o1, o2) -> o1.getScore().compareTo(o2.getScore())));
                        break;
                    default:
                        break;

                }
                mRankingsView.notifyGpuListChanged(gpuList);
            }
        });
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
    public void updatePrice(String key, double price) {
        mDatabase.addUserPrice(key, price);
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
