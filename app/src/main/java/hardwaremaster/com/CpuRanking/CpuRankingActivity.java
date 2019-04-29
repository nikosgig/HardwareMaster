package hardwaremaster.com.CpuRanking;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import hardwaremaster.com.Base.BaseActivity;
import hardwaremaster.com.R;
import hardwaremaster.com.data.FilterValues;
import hardwaremaster.com.data.RangeSeekBarValues;
import hardwaremaster.com.fragments.BottomDialogFilterFragment;
import hardwaremaster.com.util.ActivityUtils;
import hardwaremaster.com.widgets.RangeSeekBar;


public class CpuRankingActivity extends BaseActivity implements BottomDialogFilterFragment.OnBottomDialogFilterFragmentListener{

    private CpuRankingPresenter mCpuRankingPresenter;
    FilterValues filterValues = new FilterValues();
    RangeSeekBar<Double> seekBarSingleScore;
    private DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        //setContentView(R.layout.activity_cpuranking);
        CpuRankingFragment cpuRankingFragment =
                (CpuRankingFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (cpuRankingFragment == null) {
            // Create the fragment
            cpuRankingFragment = CpuRankingFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), cpuRankingFragment, R.id.contentFrame);
        }

/*        BottomDialogFilterFragment bottomDialogFilterFragment =
                BottomDialogFilterFragment.newInstance();
        bottomDialogFilterFragment.show(getSupportFragmentManager(),
                "add_photo_dialog_fragment");*/

        // Create the presenter
        mCpuRankingPresenter = new CpuRankingPresenter(cpuRankingFragment);
        mCpuRankingPresenter.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_order:
                RangeSeekBarValues rangeSeekBarValues = mCpuRankingPresenter.getFilterMinMaxValues();
                seekBarSingleScore = new RangeSeekBar<>(this);
                seekBarSingleScore.setRangeValues(rangeSeekBarValues.getMin(), rangeSeekBarValues.getMax());
                seekBarSingleScore.setTextAboveThumbsColor(R.color.colorPrimary);
                bottomDialogFilterFragment =
                        BottomDialogFilterFragment.newInstance(seekBarSingleScore);

                // Add to layout
                bottomDialogFilterFragment.show(getSupportFragmentManager(),
                        "add_photo_dialog_fragment");



                break;
/*            case R.id.order_brand:
                //mPresenter.sortByBrand();
                mPresenter.loadCpuRanking();
                break;
            case R.id.order_single:
                //mPresenter.sortBySingle();
                mPresenter.loadCpuRanking();
                break;*/
        }
        return true;
    }


    @Override
    public void OnBottomDialogFilterFragmentInteraction(RangeSeekBar<Double> seekBarSingleScore) {
        bottomDialogFilterFragment.dismiss();
        filterValues.setSingleScoreHigh(seekBarSingleScore.getSelectedMaxValue());
        filterValues.setSingleScoreLow(seekBarSingleScore.getSelectedMinValue());
        mCpuRankingPresenter.filterItems(filterValues);
    }
}