package hardwaremaster.com.old.Ranking;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import hardwaremaster.com.old.Login.LoginContract;
import hardwaremaster.com.old.Login.LoginFragment;
import hardwaremaster.com.old.Login.LoginPresenter;
import hardwaremaster.com.old.di.ActivityScoped;
import hardwaremaster.com.old.di.FragmentScoped;

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
