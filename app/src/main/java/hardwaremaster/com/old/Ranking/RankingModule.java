package hardwaremaster.com.old.Ranking;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import hardwaremaster.com.old.Filter.GpuFilterContract;
import hardwaremaster.com.old.Filter.GpuFilterFragment;
import hardwaremaster.com.old.Filter.GpuFilterPresenter;
import hardwaremaster.com.old.Ranking.CpuRanking.CpuRankingContract;
import hardwaremaster.com.old.Ranking.CpuRanking.CpuRankingFragment;
import hardwaremaster.com.old.Ranking.CpuRanking.CpuRankingPresenter;
import hardwaremaster.com.old.Ranking.GpuRanking.GpuRankingContract;
import hardwaremaster.com.old.Ranking.GpuRanking.GpuRankingFragment;
import hardwaremaster.com.old.Ranking.GpuRanking.GpuRankingFragmentFirebase;
import hardwaremaster.com.old.Ranking.GpuRanking.GpuRankingPresenter;
import hardwaremaster.com.old.Ranking.Settings.SettingsFragment;
import hardwaremaster.com.old.di.ActivityScoped;
import hardwaremaster.com.old.di.FragmentScoped;

/**
 * This is a Dagger module. We use this to pass in the View dependency to the presenters
 */
@Module
public abstract class RankingModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract CpuRankingFragment cpuRankingFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract GpuRankingFragmentFirebase gpuRankingFragmentFirebase();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract GpuRankingFragment gpuRankingFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract GpuFilterFragment filterFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract SettingsFragment settingsFragment();

    @ActivityScoped
    @Binds abstract CpuRankingContract.Presenter cpuRankingPresenter(CpuRankingPresenter presenter);

    @ActivityScoped
    @Binds abstract GpuRankingContract.Presenter gpuRankingPresenter(GpuRankingPresenter presenter);

    @ActivityScoped
    @Binds abstract GpuFilterContract.Presenter gpuFilterPresenter(GpuFilterPresenter presenter);
}
