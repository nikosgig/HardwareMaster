package hardwaremaster.com.Ranking;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import hardwaremaster.com.Base.BaseActivity;
import hardwaremaster.com.R;
import hardwaremaster.com.data.Cpu;
import hardwaremaster.com.data.FilterValues;
import hardwaremaster.com.data.RangeSeekBarValues;
import hardwaremaster.com.Filter.FilterFragment;
import hardwaremaster.com.util.ActivityUtils;
import hardwaremaster.com.widgets.RangeSeekBar;


public class RankingActivity extends BaseActivity implements FilterFragment.OnBottomDialogFilterFragmentListener{

    private RankingPresenter mRankingPresenter;
    //private FilterPresenter mFilterPresenter;
    FilterValues filterValues = new FilterValues();
    //RangeSeekBar<Double> seekBarSingleScore;
    public FilterFragment filterFragment;
    private DrawerLayout mDrawerLayout;
    private RangeSeekBar seekBarSingleScore, seekBarMultiScore;
    ArrayList<RangeSeekBar> seekBarsToShow = new ArrayList<>();
    private BottomNavigationView mBottomNav;
    private int mSelectedItem;
    private static final String SELECTED_ITEM = "arg_selected_item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        mBottomNav = findViewById(R.id.navigation);

        //setContentView(R.layout.activity_cpuranking);
        RankingFragment rankingFragment =
                (RankingFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (rankingFragment == null) {
            // Create the fragment
            rankingFragment = RankingFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), rankingFragment, R.id.contentFrame);
        }

        // Create the presenter
        mRankingPresenter = new RankingPresenter(rankingFragment);

        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return true;
            }
        });

        MenuItem selectedItem;
        if (savedInstanceState != null) {
            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 0);
            selectedItem = mBottomNav.getMenu().findItem(mSelectedItem);
        } else {
            selectedItem = mBottomNav.getMenu().getItem(0);
        }
        selectFragment(selectedItem);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_order:
                filterFragment =
                        FilterFragment.newInstance();

                // Add to layout
                filterFragment.show(getSupportFragmentManager(),
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

    private void selectFragment(MenuItem item) {
        RankingFragment frag = null;
        // init corresponding fragment
        switch (item.getItemId()) {
            case R.id.menu_cpu:
                mRankingPresenter.loadCpuRanking();
                break;
            case R.id.menu_gpu:
                //mRankingPresenter.loadGpuRanking();
                ArrayList<Cpu> arrayList = new ArrayList<>();
                Cpu cpu = new Cpu();
                cpu.setModel("test");
                arrayList.add(cpu);
                mRankingPresenter.refreshCpuList(arrayList);
                break;
            case R.id.menu_settings:
                ArrayList<Cpu> arrayList1 = new ArrayList<>();
                Cpu cpu1 = new Cpu();
                cpu1.setModel("test1");
                arrayList1.add(cpu1);
                mRankingPresenter.refreshCpuList(arrayList1);
                //load settings
                break;
        }

        // update selected item
        mSelectedItem = item.getItemId();

        // uncheck the other items.
        for (int i = 0; i< mBottomNav.getMenu().size(); i++) {
            MenuItem menuItem = mBottomNav.getMenu().getItem(i);
            menuItem.setChecked(menuItem.getItemId() == item.getItemId());
        }

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
    public void OnBottomDialogFilterFragmentInteraction() {
        filterFragment.dismiss();
        FilterValues filterValues = generateFilterValues();
        mRankingPresenter.filterItems(filterValues);
/*        final ChipGroup entryChipGroup = findViewById(R.id.chip_group);
        entryChipGroup.setVisibility(View.VISIBLE);
        final Chip entryChip = getChip(entryChipGroup, "Single Score");
        entryChipGroup.addView(entryChip);*/
    }

    @Override
    public ArrayList<RangeSeekBar> OnRangeSeekBarInit() {
        ArrayList<RangeSeekBar> seekBarsToShow = new ArrayList<>();
        RangeSeekBarValues rangeSeekBarValues = mRankingPresenter.getFilterMinMaxValues();

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

        return seekBarsToShow;
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
                mRankingPresenter.loadCpuRanking();
            }
        });
        return chip;
    }
}