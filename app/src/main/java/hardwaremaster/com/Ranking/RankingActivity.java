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
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.transition.AutoTransition;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.viewpager.widget.ViewPager;

import javax.inject.Inject;

import dagger.Lazy;
import hardwaremaster.com.Base.BaseActivity;
import hardwaremaster.com.R;
import hardwaremaster.com.Ranking.CpuRanking.CpuRankingFragment;
import hardwaremaster.com.Ranking.CpuRanking.CpuRankingPresenter;
import hardwaremaster.com.Ranking.GpuRanking.Filter.GpuFilterValues;
import hardwaremaster.com.Ranking.GpuRanking.GpuRankingFragment;
import hardwaremaster.com.Ranking.GpuRanking.GpuRankingPresenter;
import hardwaremaster.com.Ranking.Settings.SettingsFragment;
import hardwaremaster.com.data.CpuFilterValues;
import hardwaremaster.com.Filter.FilterFragment;
import hardwaremaster.com.widgets.RangeSeekBar;


public class RankingActivity extends BaseActivity implements GpuRankingFragment.OnBottomDialogFilterFragmentListener {

    private RangeSeekBar cpuBarSingleScore, cpuBarMultiScore;
    private CpuFilterValues cpuFilterValues = new CpuFilterValues();
    private GpuFilterValues gpuFilterValues = new GpuFilterValues();
    private int CPU_CURRENT_TAB=0, GPU_CURRENT_TAB=1;

    @Inject
    CpuRankingPresenter mCpuRankingPresenter;
    @Inject
    GpuRankingPresenter mGpuRankingPresenter;
    @Inject
    Lazy<FilterFragment> filterFragmentProvider;
    FilterFragment filterFragment;
    @Inject
    Lazy<CpuRankingFragment> cpuRankingFragmentProvider;
    CpuRankingFragment cpuRankingFragment;
    @Inject
    Lazy<GpuRankingFragment> gpuRankingFragmentProvider;
    GpuRankingFragment gpuRankingFragment;
    @Inject
    Lazy<SettingsFragment> settingsFragmentProvider;
    SettingsFragment settingsFragment;

    private int currentTab;
    private BottomNavigationView mBottomNav;
    private ViewPager viewPager;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_cpu:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.menu_gpu:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.menu_settings:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mBottomNav = findViewById(R.id.navigation);
        mBottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        viewPager = findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        mBottomNav.setSelectedItemId(R.id.menu_cpu);
                        mCpuRankingPresenter.getCpuFromDatabase();
                        currentTab=CPU_CURRENT_TAB;
                        break;
                    case 1:
                        mBottomNav.setSelectedItemId(R.id.menu_gpu);
                        mGpuRankingPresenter.getGpuFromDatabase();
                        currentTab=GPU_CURRENT_TAB;
                        break;
                    case 2:
                        mBottomNav.setSelectedItemId(R.id.menu_settings);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        cpuRankingFragment = cpuRankingFragmentProvider.get();
        gpuRankingFragment = gpuRankingFragmentProvider.get();
        settingsFragment = settingsFragmentProvider.get();
        adapter.addFragment(cpuRankingFragment);
        adapter.addFragment(gpuRankingFragment);
        adapter.addFragment(settingsFragment);
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_order:
/*                gpufilterFragment = gpufilterFragmentProvider.get();
                // Add to layout
                gpufilterFragment.show(getSupportFragmentManager(),
                        "cpu_filter");*/
                if(mBottomNav.getVisibility()==View.VISIBLE) {
                    Transition transition = new Slide();
                    TransitionManager.beginDelayedTransition(mBottomNav, transition);
                    mBottomNav.setVisibility(View.GONE);

                } else {
                    Transition transition = new Slide();
                    TransitionManager.beginDelayedTransition(mBottomNav, transition);
                    mBottomNav.setVisibility(View.VISIBLE);
                }

                mGpuRankingPresenter.showHideFilters();
                break;
        }
        return true;
    }

    public CpuFilterValues getCpuFilters() {
        cpuFilterValues.setSingleScoreMin((Double) cpuBarSingleScore.getSelectedMinValue());
        cpuFilterValues.setSingleScoreMax((Double) cpuBarSingleScore.getSelectedMaxValue());
        cpuFilterValues.setMultiCoreMin((Double) cpuBarMultiScore.getSelectedMinValue());
        cpuFilterValues.setMultiCoreMax((Double) cpuBarMultiScore.getSelectedMaxValue());

        cpuBarSingleScore.setSelectedMinValue(cpuBarSingleScore.getSelectedMinValue());
        cpuBarSingleScore.setSelectedMaxValue(cpuBarSingleScore.getSelectedMaxValue());

        cpuBarMultiScore.setSelectedMinValue(cpuBarMultiScore.getSelectedMinValue());
        cpuBarMultiScore.setSelectedMaxValue(cpuBarMultiScore.getSelectedMaxValue());

        return cpuFilterValues;
    }

/*    @Override
    public void OnCpuFilterFragmentInteraction() {
        filterFragment.dismiss();
        if(currentTab==CPU_CURRENT_TAB) {
            CpuFilterValues cpuFilterValues = getCpuFilters();
            mCpuRankingPresenter.applyFiltersForCpuList(cpuFilterValues);
        }


*//*        final ChipGroup entryChipGroup = findViewById(R.id.chip_group);
        entryChipGroup.setVisibility(View.VISIBLE);
        final Chip entryChip = getChip(entryChipGroup, "Single Score");
        entryChipGroup.addView(entryChip);*//*
    }*/

    /*@Override
    public ArrayList<RangeSeekBar> OnCpuRangeSeekBarInit() {
        ArrayList<RangeSeekBar> seekBarsToShow = new ArrayList<>();
        seekBarsToShow.clear();
        if(currentTab==CPU_CURRENT_TAB && cpuBarSingleScore==null && cpuBarMultiScore== null) {
            CpuFilterValues cpuFilterValues = mCpuRankingPresenter.getCpuFilterValuesToShow();
            cpuBarSingleScore = new RangeSeekBar<>(this);
            cpuBarSingleScore.setRangeSeekBarTitle(R.string.seek_bar_title_single);
            cpuBarSingleScore.setRangeValues(cpuFilterValues.getSingleScoreMin(), cpuFilterValues.getSingleScoreMax());
            cpuBarSingleScore.setTextAboveThumbsColor(R.color.colorPrimary);
            seekBarsToShow.add(cpuBarSingleScore);

            cpuBarMultiScore = new RangeSeekBar<>(this);
            cpuBarMultiScore.setRangeSeekBarTitle(R.string.seek_bar_title_multi);
            cpuBarMultiScore.setRangeValues(cpuFilterValues.getMultiCoreMin(), cpuFilterValues.getMultiCoreMax());
            cpuBarMultiScore.setTextAboveThumbsColor(R.color.colorAccent);
            cpuBarMultiScore.setTag("SingleScore2");

            seekBarsToShow.add(cpuBarMultiScore);
        } else if(cpuBarSingleScore!= null && cpuBarMultiScore != null){
            seekBarsToShow.add(cpuBarSingleScore);
            seekBarsToShow.add(cpuBarMultiScore);
        }
        return seekBarsToShow;
    }*/

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
                mCpuRankingPresenter.getCpuFromDatabase();
            }
        });
        return chip;
    }

    @Override
    public void OnApplyGpuFilterClicked() {
        Transition transition = new Slide();
        TransitionManager.beginDelayedTransition(mBottomNav, transition);
        mBottomNav.setVisibility(View.VISIBLE);
        mGpuRankingPresenter.showHideFilters();
        mGpuRankingPresenter.getGpuFromDatabase();
    }

/*    @Override
    public void OnGpuFragmentInteraction() {
        //GpuFilterValues gpuFilterValues = getGpuFilters();
        //mGpuRankingPresenter.applyFiltersForGpuList(gpuFilterValues);
    }*/

/*    @Override
    public ArrayList<RangeSeekBar> OnGpuRangeSeekBarInit() {
        return null;
    }*/

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
            if(fragment instanceof CpuRankingFragment) {
                //mCpuRankingPresenter = new CpuRankingPresenter((CpuRankingFragment) fragment);
                mCpuRankingPresenter.getCpuFromDatabase();
            } else if(fragment instanceof GpuRankingFragment) {
                //mGpuRankingPresenter = new GpuRankingPresenter((GpuRankingFragment) fragment);
            }

        }
    }
}