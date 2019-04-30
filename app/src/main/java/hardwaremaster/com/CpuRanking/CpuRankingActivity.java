package hardwaremaster.com.CpuRanking;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import androidx.drawerlayout.widget.DrawerLayout;
import hardwaremaster.com.Base.BaseActivity;
import hardwaremaster.com.Filter.FilterPresenter;
import hardwaremaster.com.R;
import hardwaremaster.com.data.FilterValues;
import hardwaremaster.com.data.RangeSeekBarValues;
import hardwaremaster.com.Filter.FilterFragment;
import hardwaremaster.com.util.ActivityUtils;
import hardwaremaster.com.widgets.RangeSeekBar;


public class CpuRankingActivity extends BaseActivity implements FilterFragment.OnBottomDialogFilterFragmentListener{

    private CpuRankingPresenter mCpuRankingPresenter;
    private FilterPresenter mFilterPresenter;
    FilterValues filterValues = new FilterValues();
    RangeSeekBar<Double> seekBarSingleScore;
    public FilterFragment filterFragment;
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

        // Create the presenter
        mCpuRankingPresenter = new CpuRankingPresenter(cpuRankingFragment);
        mCpuRankingPresenter.loadCpuRanking();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_order:
                ArrayList<RangeSeekBar> list = new ArrayList<>();

                RangeSeekBarValues rangeSeekBarValues = mCpuRankingPresenter.getFilterMinMaxValues();
                seekBarSingleScore = new RangeSeekBar<>(this);
                seekBarSingleScore.setRangeSeekBarType("bwoah");
                seekBarSingleScore.setRangeValues(rangeSeekBarValues.getMin(), rangeSeekBarValues.getMax());
                seekBarSingleScore.setTextAboveThumbsColor(R.color.colorPrimary);
                seekBarSingleScore.setTag("SingleScore");
                list.add(seekBarSingleScore);

                RangeSeekBarValues rangeSeekBarValues1 = mCpuRankingPresenter.getFilterMinMaxValues();
                seekBarSingleScore = new RangeSeekBar<>(this);
                seekBarSingleScore.setRangeSeekBarType("bwoah2");
                seekBarSingleScore.setRangeValues(rangeSeekBarValues1.getMin(), rangeSeekBarValues1.getMax());
                seekBarSingleScore.setTextAboveThumbsColor(R.color.colorAccent);
                seekBarSingleScore.setTag("SingleScore2");

                list.add(seekBarSingleScore);

                // Add to layout
                filterFragment = FilterFragment.newInstance();
                filterFragment.show(getSupportFragmentManager(),
                        "add_photo_dialog_fragment");
                mFilterPresenter = new FilterPresenter(filterFragment);
                mFilterPresenter.generateRangeSeekBars(list);



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
    public void OnBottomDialogFilterFragmentInteraction() {
        filterFragment.dismiss();
        filterValues.setSingleScoreHigh(seekBarSingleScore.getSelectedMaxValue());
        filterValues.setSingleScoreLow(seekBarSingleScore.getSelectedMinValue());
        mCpuRankingPresenter.filterItems(filterValues);

        final ChipGroup entryChipGroup = findViewById(R.id.chip_group);
        entryChipGroup.setVisibility(View.VISIBLE);
        final Chip entryChip = getChip(entryChipGroup, "Single Score");
        entryChipGroup.addView(entryChip);
    }

    private Chip getChip(final ChipGroup entryChipGroup, String text) {
        final Chip chip = new Chip(this);
        chip.setChipDrawable(ChipDrawable.createFromResource(this, R.xml.filter_chip));
        int paddingDp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 10,
                getResources().getDisplayMetrics()
        );
        chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
        chip.setText(text);
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entryChipGroup.removeView(chip);
                mCpuRankingPresenter.loadCpuRanking();
            }
        });
        return chip;
    }
}