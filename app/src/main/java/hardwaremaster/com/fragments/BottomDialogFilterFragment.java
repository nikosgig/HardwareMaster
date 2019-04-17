package hardwaremaster.com.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.annotations.Nullable;

import hardwaremaster.com.R;
public class BottomDialogFilterFragment extends BottomSheetDialogFragment {

    public static BottomDialogFilterFragment newInstance() {
        return new BottomDialogFilterFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bottom_sheet_filtering, container,
                false);

        // get the views and attach the listener

        return view;

    }
}