package hardwaremaster.com.old.Login;

import hardwaremaster.com.old.Base.BasePresenter;
import hardwaremaster.com.old.Base.BaseView;
import hardwaremaster.com.old.Filter.GpuFilterContract;

public interface LoginContract {

    interface View extends BaseView<GpuFilterContract.Presenter> {
        void loginSuccess();
        void loginError();
        void registerSuccess();
        void registerError();


    }


    interface Presenter extends BasePresenter<View> {

        void initUserLogin(String email, String password);
        boolean isUserLoggedIn();
        void initCreateAccount(String email, String password);
        void initGoogleSignIn();
    }
}
