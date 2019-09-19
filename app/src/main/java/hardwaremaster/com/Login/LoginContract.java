package hardwaremaster.com.Login;

import hardwaremaster.com.Base.BasePresenter;
import hardwaremaster.com.Base.BaseView;
import hardwaremaster.com.Filter.GpuFilterContract;

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
