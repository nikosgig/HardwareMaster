package hardwaremaster.com.Ranking;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

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
import hardwaremaster.com.Filter.GpuFilterFragment;
import hardwaremaster.com.util.ActivityUtils;
import hardwaremaster.com.widgets.RangeSeekBar;


public class RankingActivity extends BaseActivity implements GpuRankingFragment.OnBottomDialogFilterFragmentListener, GpuFilterFragment.OnBottomDialogFilterFragmentListener {

    private RangeSeekBar cpuBarSingleScore, cpuBarMultiScore;
    private CpuFilterValues cpuFilterValues = new CpuFilterValues();
    private GpuFilterValues gpuFilterValues = new GpuFilterValues();
    private int CPU_CURRENT_TAB=0, GPU_CURRENT_TAB=1;

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
//            bottomAppBar.performHide();
//            bottomAppBar.setVisibility(View.INVISIBLE);
            //floatingActionButton.hide();
            gpuFilterFragment.show(getSupportFragmentManager(), "tag");
            //ActivityUtils.replaceAddToBackStackFragmentToActivity(getSupportFragmentManager(), gpuFilterFragment, R.id.contentFrame);
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
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menu_order:
//                mGpuRankingPresenter.showHideFilters();
//                break;
//        }
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

    @Override
    public void OnBottomDialogFilterFragmentInteraction() {
//        //mGpuRankingPresenter.getGpuFromDatabase();
//        bottomAppBar.performShow();
//        bottomAppBar.setVisibility(View.VISIBLE);
        //floatingActionButton.show();
        //getSupportFragmentManager().popBackStack();
        gpuFilterFragment.dismiss();

    }

    @Override
    public ArrayList<RangeSeekBar> OnRangeSeekBarInit() {
        return null;
    }
}