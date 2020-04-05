package hardwaremaster.com.a_old.Ranking.Settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.Nullable;

import androidx.fragment.app.Fragment;

import javax.inject.Inject;

import hardwaremaster.com.R;
import hardwaremaster.com.a_old.di.ActivityScoped;

@ActivityScoped
public class SettingsFragment extends Fragment {

    MaterialButton signOutButton;

    @Inject
    public SettingsFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        setHasOptionsMenu(false);

        signOutButton = root.findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                FirebaseAuth.getInstance().signOut();
            }
        });
        return root;
    }
}
