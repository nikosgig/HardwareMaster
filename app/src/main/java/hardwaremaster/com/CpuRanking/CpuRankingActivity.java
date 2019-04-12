package hardwaremaster.com.CpuRanking;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

import hardwaremaster.com.R;
import hardwaremaster.com.util.ActivityUtils;


public class CpuRankingActivity extends AppCompatActivity {

    private CpuRankingPresenter mCpuRankingPresenter;
    private DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        setContentView(R.layout.cpuranking_act);

        CpuRankingFragment cpuRankingFragment =
                (CpuRankingFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (cpuRankingFragment == null) {
            // Create the fragment
            cpuRankingFragment = CpuRankingFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), cpuRankingFragment, R.id.contentFrame);
        }

        // Create the presenter
        mCpuRankingPresenter = new CpuRankingPresenter(cpuRankingFragment);
        mCpuRankingPresenter.start();
    }
}