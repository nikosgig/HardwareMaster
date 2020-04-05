package hardwaremaster.com.old.Filter;

import androidx.annotation.Nullable;

import javax.inject.Inject;

import hardwaremaster.com.old.Ranking.GpuRanking.Filter.GpuFilterValues;
import hardwaremaster.com.old.Ranking.GpuRanking.GpuRankingSortBy;
import hardwaremaster.com.old.data.Database;
import hardwaremaster.com.old.di.ActivityScoped;

@ActivityScoped
public class GpuFilterPresenter implements GpuFilterContract.Presenter {

    @Nullable
    private GpuFilterContract.View mGpuFiltersView;
    private final Database mDatabase;
    GpuFilterValues gpuFilterValues = new GpuFilterValues();

    @Inject
    public GpuFilterPresenter(Database database) {
        mDatabase = database;
    }


    @Override
    public void setMin1080(Number min1080) {
        gpuFilterValues.setMinFps1080p((long) min1080);
    }

    @Override
    public void setMax1080(Number max1080) {
        gpuFilterValues.setMaxFps1080p((long) max1080);
    }

    @Override
    public void setMin2K(Number min2K) {
        gpuFilterValues.setMinFps2k((long) min2K);
    }

    @Override
    public void setMax2K(Number max2K) {
        gpuFilterValues.setMaxFps2k((long) max2K);
    }

    @Override
    public void setMin4K(Number min4K) {
        gpuFilterValues.setMinFps4k((long) min4K);
    }

    @Override
    public void setMax4K(Number max4K) {
        gpuFilterValues.setMaxFps4k((long) max4K);
    }

    @Override
    public void setMinPassmark(Number minPassmark) {
        gpuFilterValues.setMinPassmark((long) minPassmark);
    }

    @Override
    public void setMaxPassmark(Number maxPassmark) {
        gpuFilterValues.setMaxPassmark((long) maxPassmark);
    }

    @Override
    public void setMinFirestrike(Number minFirestrike) {
        gpuFilterValues.setMinFirestrike((long) minFirestrike);
    }

    @Override
    public void setMaxFirestrike(Number maxFirestrike) {
        gpuFilterValues.setMaxFirestrike((long) maxFirestrike);
    }

    @Override
    public void setMinPrice(Number minPrice) {
        gpuFilterValues.setMinPrice((long) minPrice);
    }

    @Override
    public void setMaxPrice(Number maxPrice) {
        gpuFilterValues.setMaxPrice((long) maxPrice);
    }

    @Override
    public void addVRamSize(String vRam) {

    }

    @Override
    public void removeVRamSize(String vRam) {

    }

    @Override
    public void addBrand(String brand) {

    }

    @Override
    public void removeBrand(String brand) {

    }

    @Override
    public void setSorting(GpuRankingSortBy orderType) {

    }

    @Override
    public void setDatabaseGpuFilters() {
        mDatabase.setGpuFilters(gpuFilterValues);
    }

    @Override
    public GpuFilterValues getGpuFilterValues() {
        return gpuFilterValues;
    }

    @Override
    public void takeView(GpuFilterContract.View view) {

    }

    @Override
    public void dropView() {

    }
}
