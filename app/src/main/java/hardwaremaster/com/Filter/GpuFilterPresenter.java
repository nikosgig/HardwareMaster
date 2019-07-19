package hardwaremaster.com.Filter;

import androidx.annotation.Nullable;

import javax.inject.Inject;

import hardwaremaster.com.Ranking.GpuRanking.GpuRankingSortBy;
import hardwaremaster.com.di.ActivityScoped;

@ActivityScoped
public class GpuFilterPresenter implements GpuFilterContract.Presenter {

    @Nullable
    private GpuFilterContract.View mGpuFiltersView;

    @Inject
    public GpuFilterPresenter() {

    }


    @Override
    public void setMin1080(Number min1080) {

    }

    @Override
    public void setMax1080(Number max1080) {

    }

    @Override
    public void setMin2K(Number min2K) {

    }

    @Override
    public void setMax2K(Number max2K) {

    }

    @Override
    public void setMin4K(Number min4K) {

    }

    @Override
    public void setMax4K(Number max4K) {

    }

    @Override
    public void setMinPassmark(Number minPassmark) {

    }

    @Override
    public void setMaxPassmark(Number maxPassmark) {

    }

    @Override
    public void setMinFirestrike(Number minFirestrike) {

    }

    @Override
    public void setMaxFirestrike(Number maxFirestrike) {

    }

    @Override
    public void setMinPrice(Number minPrice) {

    }

    @Override
    public void setMaxPrice(Number maxPrice) {

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
    public void takeView(GpuFilterContract.View view) {

    }

    @Override
    public void dropView() {

    }
}
