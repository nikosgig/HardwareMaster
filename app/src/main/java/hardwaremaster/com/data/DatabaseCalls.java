package hardwaremaster.com.data;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import androidx.annotation.NonNull;
import hardwaremaster.com.Base.BasePresenter;
import hardwaremaster.com.Ranking.CpuRanking.CpuRankingPresenter;
import hardwaremaster.com.Ranking.GpuRanking.GpuRankingPresenter;

public class DatabaseCalls {

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private CpuRankingPresenter mCpuRankingPresenter;
    private GpuRankingPresenter mGpuRankingPresenter;
    private ArrayList<Cpu> mCpuList = new ArrayList<>();
    private ArrayList<Gpu> mGpuList = new ArrayList<>();

    public DatabaseCalls(CpuRankingPresenter presenter) {
        mCpuRankingPresenter = presenter;
    }

    public DatabaseCalls(GpuRankingPresenter presenter) {
        mGpuRankingPresenter = presenter;
    }

    public void getCpus() {
        mDatabase.getReference("cpu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCpuList.clear();
                for (DataSnapshot cpuDataSnapshot : dataSnapshot.getChildren()) {
                    mCpuList.add(cpuDataSnapshot.getValue(Cpu.class));
                }
                //mCpuRankingsView.notifyCpuListChanged(cpuList);
                mCpuRankingPresenter.onGetCpuFromDatabase(mCpuList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public ArrayList<Gpu> getGpus() {
        mDatabase.getReference("gpu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mGpuList.clear();
                for (DataSnapshot gpuDataSnapshot : dataSnapshot.getChildren()) {
                    mGpuList.add(gpuDataSnapshot.getValue(Gpu.class));
                    //Log.d("hi", cpuDataSnapshot.getValue(Cpu.class).getModel());
                }
                //mCpuRankingsView.notifyCpuListChanged(cpuList);
                //mCpuRankingPresenter.refreshCpuList(mCpuList);
                mGpuRankingPresenter.onGetCpuFromDatabase(mGpuList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return mGpuList;
    }

    public CpuFilterValues getFilterMinMaxValues() {
        Cpu maxCpu = Collections.max(mCpuList,new Comparator<Cpu>() {

            public int compare(Cpu o1, Cpu o2) {
                return Double.compare(Double.parseDouble(o1.getSingleScore()), Double.parseDouble(o2.getSingleScore()));
            }
        });

        Cpu minCpu = Collections.min(mCpuList,new Comparator<Cpu>() {

            public int compare(Cpu o1, Cpu o2) {
                return Double.compare(Double.parseDouble(o1.getSingleScore()), Double.parseDouble(o2.getSingleScore()));
            }
        });

        CpuFilterValues cpuFilterValues = new CpuFilterValues();

        cpuFilterValues.setSingleScoreMax(Double.parseDouble(maxCpu.getSingleScore()));
        cpuFilterValues.setSingleScoreMin(Double.parseDouble(minCpu.getSingleScore()));

        Cpu maxCpuMulti = Collections.max(mCpuList,new Comparator<Cpu>() {

            public int compare(Cpu o1, Cpu o2) {
                return Double.compare(Double.parseDouble(o1.getMultiScore()), Double.parseDouble(o2.getMultiScore()));
            }
        });

        Cpu minCpuMulti = Collections.min(mCpuList,new Comparator<Cpu>() {

            public int compare(Cpu o1, Cpu o2) {
                return Double.compare(Double.parseDouble(o1.getMultiScore()), Double.parseDouble(o2.getMultiScore()));
            }
        });

        cpuFilterValues.setMultiCoreMax(Double.parseDouble(maxCpuMulti.getMultiScore()));
        cpuFilterValues.setMultiCoreMin(Double.parseDouble(minCpuMulti.getMultiScore()));

/*        CpuFilterValues cpuFilterValues = new CpuFilterValues();

        Query query = mDatabase.orderByChild("SingleScore").limitToLast(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        // do something with the individual "issues"
                        Log.d("query", issue.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/


        return cpuFilterValues;
    }

    public ArrayList<Cpu> filterCpuList(CpuFilterValues filterValues) {

/*            ArrayList<Cpu> filteredCpu = (ArrayList<Cpu>) mCpuList.stream()
                    .filter(p -> Double.parseDouble(p.getSingleScore()) > filterValues.getSingleScoreLow()).collect(Collectors.toList());
            mCpuRankingPresenter.refreshCpuList(filteredCpu);*/
        ArrayList<Cpu> filteredCpu = new ArrayList<>();
        for (Cpu cpu: mCpuList) {
            if(Double.parseDouble(cpu.getSingleScore()) > filterValues.getSingleScoreMin() &&
                    Double.parseDouble(cpu.getSingleScore()) < filterValues.getSingleScoreMax() &&
                    Double.parseDouble(cpu.getMultiScore()) > filterValues.getMultiCoreMin() &&
                    Double.parseDouble(cpu.getMultiScore()) < filterValues.getMultiCoreMax()) {
                filteredCpu.add(cpu);
            }
        }
        return filteredCpu;
    }

    public ArrayList<Gpu> filterGpuList(GpuFilterValues filterValues) {
        return mGpuList;
    }

}
