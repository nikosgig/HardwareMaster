package hardwaremaster.com.a_old.Login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

public class LoginPresenter implements LoginContract.Presenter {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    @Nullable
    private LoginContract.View mLoginView;


    @Inject
    public LoginPresenter() {
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void initUserLogin(String email, String password) {

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mLoginView.loginSuccess();

                        } else {
                            mLoginView.loginError();
                        }
                    }
                }));
        // [END sign_in_with_email]
    }

    @Override
    public boolean isUserLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    @Override
    public void initCreateAccount(String email, String password) {
            // [START create_user_with_email]
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sendVerificationEmail();
                            } else {
                                // If sign in fails, display a message to the user.
                                mLoginView.registerError();
                            }

                        }
                    });
            // [END create_user_with_email]
    }

    @Override
    public void initGoogleSignIn() {
        // Configure Google Sign In

    }

    private void sendVerificationEmail() {
        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        if (task.isSuccessful()) {
                            mAuth.signOut();
                            mLoginView.registerSuccess();
                        } else {
                            mLoginView.registerError();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }

    @Override
    public void takeView(LoginContract.View view) {
        mLoginView = view;
    }

    @Override
    public void dropView() {
        mLoginView = null;
    }
}
