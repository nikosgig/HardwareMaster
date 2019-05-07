package hardwaremaster.com.Filter;

import android.content.Context;

import java.util.ArrayList;

import hardwaremaster.com.Base.BasePresenter;
import hardwaremaster.com.Base.BaseView;
import hardwaremaster.com.data.RangeSeekBarValues;
import hardwaremaster.com.widgets.RangeSeekBar;

public interface FilterContract {

    interface View extends BaseView<Presenter> {
        void showRangeSeekBars(ArrayList<RangeSeekBar> rangeSeekBars);
        //void showOrderByMenu();
    }

    interface Presenter extends BasePresenter {
        void generateRangeSeekBars(ArrayList<RangeSeekBar> rangeSeekBars);
        RangeSeekBarValues getFilterMinMaxValues();
    }
}
