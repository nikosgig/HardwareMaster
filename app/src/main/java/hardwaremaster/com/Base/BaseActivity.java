package hardwaremaster.com.Base;



import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.AppBarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import hardwaremaster.com.R;
import hardwaremaster.com.fragments.BottomDialogFilterFragment;

public class BaseActivity extends AppCompatActivity {
    Toolbar toolbar;
    AppBarLayout appBarLayout;
    public BottomDialogFilterFragment bottomDialogFilterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View baseView = inflater.inflate(R.layout.activity_base, null);
        toolbar =  baseView.findViewById(R.id.toolbar);
        appBarLayout =  baseView.findViewById(R.id.appbar);
        setContentView(baseView);
        initInstances();

    }

    public void initInstances() {
        //toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void hideAppBar() {
        appBarLayout.removeAllViews();
    }


}


