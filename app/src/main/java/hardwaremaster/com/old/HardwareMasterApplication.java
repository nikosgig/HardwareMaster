package hardwaremaster.com.old;

import android.app.Application;

import androidx.annotation.VisibleForTesting;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import hardwaremaster.com.old.data.Database;
import hardwaremaster.com.old.di.DaggerAppComponent;

/**
 * We create a custom {@link Application} class that extends  {@link DaggerApplication}.
 * We then override applicationInjector() which tells Dagger how to make our @Singleton Component
 * We never have to call `component.inject(this)` as {@link DaggerApplication} will do that for us.
 */
public class HardwareMasterApplication extends DaggerApplication {
    @Inject
    Database database;

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }

    /**
     * Our Espresso tests need to be able to get an instance of the {@link Database}
     * so that we can delete all tasks before running each test
     */
    @VisibleForTesting
    public Database database() {
        return database;
    }
}