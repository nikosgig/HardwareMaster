package hardwaremaster.com.CpuRanking;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import hardwaremaster.com.data.Cpu;

public class CpuRankingInteractor {

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private CpuRankingPresenter mCpuRankingPresenter;
    private ArrayList<Cpu> mObjectList = new ArrayList<>();

    CpuRankingInteractor(CpuRankingPresenter presenter) {
        mCpuRankingPresenter = presenter;
    }

    public void getCpus() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot cpuDataSnapshot : dataSnapshot.getChildren()) {
                    mObjectList.add(cpuDataSnapshot.getValue(Cpu.class));
                    //Log.d("hi", cpuDataSnapshot.getValue(Cpu.class).getModel());
                }
                //mCpuRankingsView.showCpuRanking(cpuList);
                mCpuRankingPresenter.refreshCpuList(mObjectList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
