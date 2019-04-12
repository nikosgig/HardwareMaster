package hardwaremaster.com.CpuRanking;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hardwaremaster.com.data.Cpu;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link CpuRankingFragment}), retrieves the data and updates
 * the UI as required.
 */
public class CpuRankingPresenter implements CpuRankingContract.Presenter{

    private DatabaseReference mDatabase;
    private final CpuRankingContract.View mCpuRankingsView;
    private boolean mFirstLoad = true;


    public CpuRankingPresenter(@NonNull CpuRankingContract.View cpuRankingView) {
        mCpuRankingsView = checkNotNull(cpuRankingView, "tasksView cannot be null!");
        mCpuRankingsView.setPresenter(this);
    }


    private void loadCpuRankingFirebase() {

    }

    @Override
    public void loadCpuRanking() {
        loadCpuRankingFirebase();
    }

    @Override
    public void start() {
        loadCpuRanking();
    }
}
