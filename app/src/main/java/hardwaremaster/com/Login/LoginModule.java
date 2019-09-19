package hardwaremaster.com.Ranking;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import hardwaremaster.com.Filter.GpuFilterContract;
import hardwaremaster.com.Filter.GpuFilterFragment;
import hardwaremaster.com.Filter.GpuFilterPresenter;
import hardwaremaster.com.Login.LoginContract;
import hardwaremaster.com.Login.LoginFragment;
import hardwaremaster.com.Login.LoginPresenter;
import hardwaremaster.com.Ranking.CpuRanking.CpuRankingContract;
import hardwaremaster.com.Ranking.CpuRanking.CpuRankingFragment;
import hardwaremaster.com.Ranking.CpuRanking.CpuRankingPresenter;
import hardwaremaster.com.Ranking.GpuRanking.GpuRankingContract;
import hardwaremaster.com.Ranking.GpuRanking.GpuRankingFragment;
import hardwaremaster.com.Ranking.GpuRanking.GpuRankingPresenter;
import hardwaremaster.com.Ranking.Settings.SettingsFragment;
import hardwaremaster.com.di.ActivityScoped;
import hardwaremaster.com.di.FragmentScoped;

/**
 * This is a Dagger module. We use this to pass in the View dependency to the presenters
 */
@Module
public abstract class LoginModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract LoginFragment loginFragment();

    @ActivityScoped
    @Binds abstract LoginContract.Presenter loginPresenter(LoginPresenter presenter);
}
