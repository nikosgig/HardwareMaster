package hardwaremaster.com.a_old.Ranking;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import hardwaremaster.com.a_old.Filter.GpuFilterContract;
import hardwaremaster.com.a_old.Filter.GpuFilterFragment;
import hardwaremaster.com.a_old.Filter.GpuFilterPresenter;
import hardwaremaster.com.a_old.Ranking.CpuRanking.CpuRankingContract;
import hardwaremaster.com.a_old.Ranking.CpuRanking.CpuRankingFragment;
import hardwaremaster.com.a_old.Ranking.CpuRanking.CpuRankingPresenter;
import hardwaremaster.com.a_old.Ranking.GpuRanking.GpuRankingContract;
import hardwaremaster.com.a_old.Ranking.GpuRanking.GpuRankingFragment;
import hardwaremaster.com.a_old.Ranking.GpuRanking.GpuRankingFragmentFirebase;
import hardwaremaster.com.a_old.Ranking.GpuRanking.GpuRankingPresenter;
import hardwaremaster.com.a_old.Ranking.Settings.SettingsFragment;
import hardwaremaster.com.a_old.di.ActivityScoped;
import hardwaremaster.com.a_old.di.FragmentScoped;

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
