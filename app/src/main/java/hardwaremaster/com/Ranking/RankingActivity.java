package hardwaremaster.com.Ranking;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.Lazy;
import hardwaremaster.com.Base.BaseActivity;
import hardwaremaster.com.Filter.GpuFilterFragment;
import hardwaremaster.com.R;
import hardwaremaster.com.Ranking.CpuRanking.CpuRankingFragment;
import hardwaremaster.com.Ranking.CpuRanking.CpuRankingPresenter;
import hardwaremaster.com.Ranking.GpuRanking.Filter.GpuFilterValues;
import hardwaremaster.com.Ranking.GpuRanking.GpuRankingFragment;
import hardwaremaster.com.Ranking.GpuRanking.GpuRankingPresenter;
import hardwaremaster.com.Ranking.Settings.SettingsFragment;
import hardwaremaster.com.data.CpuFilterValues;
import hardwaremaster.com.util.ActivityUtils;
import hardwaremaster.com.widgets.RangeSeekBar;


public class RankingActivity extends BaseActivity implements GpuFilterFragment.OnBottomDialogFilterFragmentListener {

    private CpuFilterValues cpuFilterValues = new CpuFilterValues();
    private GpuFilterValues gpuFilterValues = new GpuFilterValues();

    @Inject
    CpuRankingPresenter mCpuRankingPresenter;
    @Inject
    GpuRankingPresenter mGpuRankingPresenter;
    @Inject
    Lazy<GpuFilterFragment> filterFragmentProvider;
    GpuFilterFragment gpuFilterFragment;
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
//                    Intent intent = new Intent(RankingActivity.this, GpuRankingActivity.class);
//                    startActivity(intent);
                    return true;
                case R.id.menu_settings:
                    settingsFragment = settingsFragmentProvider.get();
                    ActivityUtils.replaceFragmentToActivity(getSupportFragmentManager(), settingsFragment, R.id.contentFrame);
                    return true;
            }

            return false;
        }
    };

    private FloatingActionButton.OnClickListener mOnFloatingActionButtonListener = new FloatingActionButton.OnClickListener() {

        @Override
        public void onClick(View v) {
            gpuFilterFragment = filterFragmentProvider.get();
            gpuFilterFragment.show(getSupportFragmentManager(), "tag");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        bottomAppBar.setOnMenuItemClickListener(mOnNavigationItemSelectedListener);
        floatingActionButton.setOnClickListener(mOnFloatingActionButtonListener);

        cpuRankingFragment = cpuRankingFragmentProvider.get();
        ActivityUtils.replaceFragmentToActivity(getSupportFragmentManager(), cpuRankingFragment, R.id.contentFrame);

    }

    @Override
    public void OnBottomDialogFilterFragmentInteraction() {
        gpuFilterFragment.dismiss();

    }
}