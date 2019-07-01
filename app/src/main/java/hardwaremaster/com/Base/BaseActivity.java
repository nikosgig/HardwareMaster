package hardwaremaster.com.Base;



import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import dagger.android.support.DaggerAppCompatActivity;
import hardwaremaster.com.R;

public class BaseActivity extends DaggerAppCompatActivity {

    protected BottomAppBar bottomAppBar;
    protected FrameLayout contentFrame;
    protected FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View baseView = inflater.inflate(R.layout.activity_base, null);
        setContentView(baseView);
        contentFrame = baseView.findViewById(R.id.contentFrame);
        bottomAppBar = baseView.findViewById(R.id.bottomAppBar);
        floatingActionButton = baseView.findViewById(R.id.floatingActionButton);
        bottomAppBar.replaceMenu(R.menu.bottom_nav_items);
        //setSupportActionBar(bottomAppBar);

    }

}


