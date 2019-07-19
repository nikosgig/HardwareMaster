package hardwaremaster.com.Filter;

import hardwaremaster.com.Base.BasePresenter;
import hardwaremaster.com.Base.BaseView;
import hardwaremaster.com.Ranking.GpuRanking.GpuRankingSortBy;

public interface GpuFilterContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter<View> {
        void setMin1080(Number min1080);
        void setMax1080(Number max1080);

        void setMin2K(Number min2K);
        void setMax2K(Number max2K);

        void setMin4K(Number min4K);
        void setMax4K(Number max4K);

        void setMinPassmark(Number minPassmark);
        void setMaxPassmark(Number maxPassmark);

        void setMinFirestrike(Number minFirestrike);
        void setMaxFirestrike(Number maxFirestrike);

        void setMinPrice(Number minPrice);
        void setMaxPrice(Number maxPrice);

        void addVRamSize(String vRam);
        void removeVRamSize(String vRam);

        void addBrand(String brand);
        void removeBrand(String brand);

        void setSorting(GpuRankingSortBy orderType);
    }
}
