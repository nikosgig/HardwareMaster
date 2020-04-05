package hardwaremaster.com.a_old.Login;

import hardwaremaster.com.a_old.Base.BasePresenter;
import hardwaremaster.com.a_old.Base.BaseView;
import hardwaremaster.com.a_old.Filter.GpuFilterContract;

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
