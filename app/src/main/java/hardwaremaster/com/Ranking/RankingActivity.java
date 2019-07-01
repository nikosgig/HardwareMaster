package hardwaremaster.com.Ranking;

import android.os.Bundle;
import android.transition.Transition;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.firebase.database.FirebaseDatabase;

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
import hardwaremaster.com.util.ActivityUtils;
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

    private BottomAppBar.OnMenuItemClickListener mOnNavigationItemSelectedListener
            = new BottomAppBar.OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId()) {
                case R.id.menu_cpu:
                    cpuRankingFragment = cpuRankingFragmentProvider.get();
                    ActivityUtils.replaceFragmentToActivity(getSupportFragmentManager(), cpuRankingFragment, R.id.contentFrame);
                    return true;
                case R.id.menu_gpu:
                    gpuRankingFragment = gpuRankingFragmentProvider.get();
                    ActivityUtils.replaceFragmentToActivity(getSupportFragmentManager(), gpuRankingFragment, R.id.contentFrame);
                    return true;
                case R.id.menu_settings:
                    settingsFragment = settingsFragmentProvider.get();
                    ActivityUtils.replaceFragmentToActivity(getSupportFragmentManager(), settingsFragment, R.id.contentFrame);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        bottomAppBar.setOnMenuItemClickListener(mOnNavigationItemSelectedListener);

        cpuRankingFragment = cpuRankingFragmentProvider.get();
        ActivityUtils.replaceFragmentToActivity(getSupportFragmentManager(), cpuRankingFragment, R.id.contentFrame);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_order:
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


    @Override
    public void OnApplyGpuFilterClicked() {
        mGpuRankingPresenter.showHideFilters();
        mGpuRankingPresenter.getGpuFromDatabase();
    }

}