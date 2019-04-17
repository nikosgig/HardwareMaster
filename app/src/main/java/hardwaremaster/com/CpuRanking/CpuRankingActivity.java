package hardwaremaster.com.CpuRanking;

import android.os.Bundle;

import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import hardwaremaster.com.R;
import hardwaremaster.com.fragments.BottomDialogFilterFragment;
import hardwaremaster.com.util.ActivityUtils;


public class CpuRankingActivity extends AppCompatActivity {

    private CpuRankingPresenter mCpuRankingPresenter;
    private DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        setContentView(R.layout.activity_cpuranking);
        CpuRankingFragment cpuRankingFragment =
                (CpuRankingFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (cpuRankingFragment == null) {
            // Create the fragment
            cpuRankingFragment = CpuRankingFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), cpuRankingFragment, R.id.contentFrame);
        }

/*        BottomDialogFilterFragment bottomDialogFilterFragment =
                BottomDialogFilterFragment.newInstance();
        bottomDialogFilterFragment.show(getSupportFragmentManager(),
                "add_photo_dialog_fragment");*/

        // Create the presenter
        mCpuRankingPresenter = new CpuRankingPresenter(cpuRankingFragment);
        mCpuRankingPresenter.start();
    }
}