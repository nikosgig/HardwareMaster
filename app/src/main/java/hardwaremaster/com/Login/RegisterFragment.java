package hardwaremaster.com.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.annotations.Nullable;

import java.util.Objects;

import javax.inject.Inject;

import hardwaremaster.com.R;
import hardwaremaster.com.di.ActivityScoped;
import hardwaremaster.com.util.TextUtils;

@ActivityScoped
public class RegisterFragment extends Fragment implements LoginContract.View {

    @Inject
    LoginContract.Presenter mPresenter;
    onRegisterFragmentInteraction mListener;
    View view;
    TextInputLayout emailWrapper;
    TextInputLayout passwordWrapper;
    TextInputLayout passwordConfirmationWrapper;
    TextInputLayout displayNameWrapper;
    MaterialButton registerButton;
    MaterialButton goToLoginButton;


    @Inject
    public RegisterFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof onRegisterFragmentInteraction) {
            mListener = (onRegisterFragmentInteraction) context;
            //mFilter = new FilterValues();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBottomDialogFilterFragmentListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.takeView(this);
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mPresenter.dropView();  //prevent leaking activity in
//        // case presenter is orchestrating a long running task
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_register, container,
                false);

        emailWrapper = view.findViewById(R.id.emailWrapper);
        passwordWrapper = view.findViewById(R.id.passwordWrapper);
        passwordConfirmationWrapper = view.findViewById(R.id.passwordConfirmationWrapper);
        displayNameWrapper = view.findViewById(R.id.displayNameWrapper);

        registerButton = view.findViewById(R.id.registerButton);
        goToLoginButton = view.findViewById(R.id.goToLoginButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String email = Objects.requireNonNull(emailWrapper.getEditText()).getText().toString();
                String password = Objects.requireNonNull(passwordWrapper.getEditText()).getText().toString();
                String confirmPassword = Objects.requireNonNull(passwordConfirmationWrapper.getEditText()).getText().toString();
                String displayName = Objects.requireNonNull(displayNameWrapper.getEditText()).getText().toString();

                emailWrapper.setErrorEnabled(false);
                passwordWrapper.setErrorEnabled(false);
                passwordConfirmationWrapper.setErrorEnabled(false);
                displayNameWrapper.setErrorEnabled(false);


                if (!TextUtils.validateEmail(email)) {
                    emailWrapper.setError("Not a valid email address!");
                } else if (!TextUtils.validatePassword(password)) {
                    passwordWrapper.setError("Not a valid password!");
                } else if (!TextUtils.validateMatchPassword(password, confirmPassword)) {
                    passwordWrapper.setError("Passwords don't match!");
                    passwordConfirmationWrapper.setError("Passwords don't match!");
                } else if (TextUtils.isEmpty(displayName)){
                    displayNameWrapper.setError("Required");
                } else{
                    mPresenter.initCreateAccount(email, password);
                }
            }
        });

        goToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.OnGoToLoginButtonClicked();
            }
        });

        return view;
    }

    @Override
    public void loginSuccess() {
        //empty
    }

    @Override
    public void loginError() {
        //empty
    }

    @Override
    public void registerSuccess() {
        mListener.OnGoToLoginButtonClicked();
        Snackbar snackbar = Snackbar
                .make(view, "Please verify email", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void registerError() {
        Snackbar snackbar = Snackbar
                .make(view, "Register Error", Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public interface onRegisterFragmentInteraction {
        void OnGoToLoginButtonClicked();
    }
}
