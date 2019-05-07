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
import hardwaremaster.com.R;
import hardwaremaster.com.data.FilterValues;
import hardwaremaster.com.data.RangeSeekBarValues;
import hardwaremaster.com.Filter.FilterFragment;
import hardwaremaster.com.util.ActivityUtils;
import hardwaremaster.com.widgets.RangeSeekBar;


public class CpuRankingActivity extends BaseActivity implements FilterFragment.OnBottomDialogFilterFragmentListener{

    private CpuRankingPresenter mCpuRankingPresenter;
    //private FilterPresenter mFilterPresenter;
    FilterValues filterValues = new FilterValues();
    //RangeSeekBar<Double> seekBarSingleScore;
    public FilterFragment filterFragment;
    private DrawerLayout mDrawerLayout;
    private RangeSeekBar seekBarSingleScore, seekBarMultiScore;


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
        filterFragment = FilterFragment.newInstance();
        mCpuRankingPresenter = new CpuRankingPresenter(cpuRankingFragment, filterFragment);
        mCpuRankingPresenter.loadCpuRanking();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_order:
                filterFragment.show(getSupportFragmentManager(),
                        "add_photo_dialog_fragment");
                generateSeekBars();
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

    public void generateSeekBars() {
        RangeSeekBarValues rangeSeekBarValues = mCpuRankingPresenter.getFilterMinMaxValues();


        ArrayList<RangeSeekBar> seekBarsToShow = new ArrayList<>();

        seekBarSingleScore = new RangeSeekBar<>(this);
        seekBarSingleScore.setRangeSeekBarTitle(R.string.seek_bar_title_single);
        seekBarSingleScore.setRangeValues(rangeSeekBarValues.getSingleScoreMin(), rangeSeekBarValues.getSingleScoreMax());
        seekBarSingleScore.setTextAboveThumbsColor(R.color.colorPrimary);
        seekBarSingleScore.setTag("SingleScore");
        seekBarsToShow.add(seekBarSingleScore);

        seekBarMultiScore = new RangeSeekBar<>(this);
        seekBarMultiScore.setRangeSeekBarTitle(R.string.seek_bar_title_multi);
        seekBarMultiScore.setRangeValues(rangeSeekBarValues.getMultiCoreMin(), rangeSeekBarValues.getMultiCoreMax());
        seekBarMultiScore.setTextAboveThumbsColor(R.color.colorAccent);
        seekBarSingleScore.setTag("SingleScore2");

        seekBarsToShow.add(seekBarMultiScore);

        mCpuRankingPresenter.generateRangeSeekBars(seekBarsToShow);
    }

    public FilterValues generateFilterValues() {
        filterValues = new FilterValues();
        filterValues.setSingleScoreMin((Double) seekBarSingleScore.getSelectedMinValue());
        filterValues.setSingleScoreMax((Double) seekBarSingleScore.getSelectedMaxValue());
        filterValues.setMultiCoreMin((Double) seekBarMultiScore.getSelectedMinValue());
        filterValues.setMultiCoreMax((Double) seekBarMultiScore.getSelectedMaxValue());

        return filterValues;
    }


    @Override
    public void OnBottomDialogFilterFragmentInteraction(ArrayList<RangeSeekBar> rangeSeekBars) {
        filterFragment.dismiss();
        mCpuRankingPresenter.filterItems(rangeSeekBars);
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