package hardwaremaster.com.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.annotations.Nullable;

import java.util.Objects;

import javax.inject.Inject;

import hardwaremaster.com.R;
import hardwaremaster.com.Ranking.RankingActivity;
import hardwaremaster.com.di.ActivityScoped;
import hardwaremaster.com.util.TextUtils;

@ActivityScoped
public class LoginFragment extends Fragment implements LoginContract.View {

    private static final int RC_SIGN_IN = 100;
    @Inject
    LoginContract.Presenter mPresenter;
    private OnCreateAccountFragmentInteraction mListener;
    private View view;
    private TextInputLayout emailWrapper;
    private TextInputLayout passwordWrapper;
    private MaterialButton loginButton;
    private MaterialButton gmailButton;
    private LoginButton facebookButton;
    private MaterialButton customFacebookButton;
    private MaterialButton createAccountButton;
    private CallbackManager mCallbackManager;


    @Inject
    public LoginFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnCreateAccountFragmentInteraction) {
            mListener = (OnCreateAccountFragmentInteraction) context;
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

        view = inflater.inflate(R.layout.fragment_login, container,
                false);

        emailWrapper = view.findViewById(R.id.emailWrapper);
        passwordWrapper = view.findViewById(R.id.passwordWrapper);
        loginButton = view.findViewById(R.id.loginButton);
        createAccountButton = view.findViewById(R.id.createAccountButton);
        gmailButton = view.findViewById(R.id.gmailButton);
        facebookButton = view.findViewById(R.id.facebookButton);
        customFacebookButton = view.findViewById(R.id.customFacebookButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String email = Objects.requireNonNull(emailWrapper.getEditText()).getText().toString();
                String password = Objects.requireNonNull(passwordWrapper.getEditText()).getText().toString();
                emailWrapper.setErrorEnabled(false);
                passwordWrapper.setErrorEnabled(false);

                if (!TextUtils.validateEmail(email)) {
                    emailWrapper.setError("Not a valid email address!");
                } else if (!TextUtils.validatePassword(password)) {
                    passwordWrapper.setError("Not a valid password!");
                } else {
                    mPresenter.initUserLogin(email, password);
                }
            }
        });

        gmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                initGoogleSignIn();
            }
        });

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        facebookButton.setPermissions("email", "public_profile");
        facebookButton.setFragment(this);
        facebookButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                //TODO set error exception messages
            }
        });

        customFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                facebookButton.performClick();
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mListener.OnCreateAccountButtonClicked();
            }
        });

        return view;
    }


    private void initGoogleSignIn() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        if (getActivity() != null) {
            GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
            // Check for existing Google Sign In account, if the user is already signed in
            // the GoogleSignInAccount will be non-null.
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
            if (account != null && mPresenter.isUserLoggedIn()) {
                loginSuccess();
            } else {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN); //RC_SIGN_IN = 100;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        } else {
            //todo find requestCode for facebook and convert to elseif
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            if(account!= null) {
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),
                        null);
                FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    //Log.d(TAG, "signInWithCredential:success");
                                    loginSuccess();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    //todo handle signIn fail
                                    loginError();
                                }
                            }
                        });
            }

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //TODO handle e.getStatusCode()
        }
    }

    // [START auth_with_facebook]
    private void handleFacebookAccessToken(AccessToken token) {
        //Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            loginSuccess();
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //todo handle failure
                        }
                    }
                });
    }
    // [END auth_with_facebook]

    @Override
    public void loginSuccess() {
        Intent intent = new Intent(getActivity(), RankingActivity.class);
        startActivity(intent);
    }

    @Override
    public void loginError() {

    }

    @Override
    public void registerSuccess() {
        //empty
    }

    @Override
    public void registerError() {
        //empty
    }

    public interface OnCreateAccountFragmentInteraction {
        void OnCreateAccountButtonClicked();
    }
}
