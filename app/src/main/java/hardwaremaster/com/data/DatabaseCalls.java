package hardwaremaster.com.data;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import androidx.annotation.NonNull;
import hardwaremaster.com.Ranking.RankingPresenter;
import hardwaremaster.com.data.Cpu;
import hardwaremaster.com.data.FilterValues;
import hardwaremaster.com.data.RangeSeekBarValues;

public class DatabaseCalls {

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private RankingPresenter mRankingPresenter;
    private ArrayList<Cpu> mObjectList = new ArrayList<>();

    public DatabaseCalls(RankingPresenter presenter) {
        mRankingPresenter = presenter;
    }

    public void getCpus() {
        mDatabase.getReference("cpu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mObjectList.clear();
                for (DataSnapshot cpuDataSnapshot : dataSnapshot.getChildren()) {
                    mObjectList.add(cpuDataSnapshot.getValue(Cpu.class));
                    //Log.d("hi", cpuDataSnapshot.getValue(Cpu.class).getModel());
                }
                //mCpuRankingsView.showCpuRanking(cpuList);
                mRankingPresenter.refreshCpuList(mObjectList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public RangeSeekBarValues getFilterMinMaxValues() {
        Cpu maxCpu = Collections.max(mObjectList,new Comparator<Cpu>() {

            public int compare(Cpu o1, Cpu o2) {
                return Double.compare(Double.parseDouble(o1.getSingleScore()), Double.parseDouble(o2.getSingleScore()));
            }
        });

        Cpu minCpu = Collections.min(mObjectList,new Comparator<Cpu>() {

            public int compare(Cpu o1, Cpu o2) {
                return Double.compare(Double.parseDouble(o1.getSingleScore()), Double.parseDouble(o2.getSingleScore()));
            }
        });

        RangeSeekBarValues rangeSeekBarValues = new RangeSeekBarValues();

        rangeSeekBarValues.setSingleScoreMax(Double.parseDouble(maxCpu.getSingleScore()));
        rangeSeekBarValues.setSingleScoreMin(Double.parseDouble(minCpu.getSingleScore()));

        Cpu maxCpuMulti = Collections.max(mObjectList,new Comparator<Cpu>() {

            public int compare(Cpu o1, Cpu o2) {
                return Double.compare(Double.parseDouble(o1.getMultiScore()), Double.parseDouble(o2.getMultiScore()));
            }
        });

        Cpu minCpuMulti = Collections.min(mObjectList,new Comparator<Cpu>() {

            public int compare(Cpu o1, Cpu o2) {
                return Double.compare(Double.parseDouble(o1.getMultiScore()), Double.parseDouble(o2.getMultiScore()));
            }
        });

        rangeSeekBarValues.setMultiCoreMax(Double.parseDouble(maxCpuMulti.getMultiScore()));
        rangeSeekBarValues.setMultiCoreMin(Double.parseDouble(minCpuMulti.getMultiScore()));

/*        RangeSeekBarValues rangeSeekBarValues = new RangeSeekBarValues();

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


        return rangeSeekBarValues;
    }

    public void filterItems(FilterValues filterValues) {

/*            ArrayList<Cpu> filteredCpu = (ArrayList<Cpu>) mObjectList.stream()
                    .filter(p -> Double.parseDouble(p.getSingleScore()) > filterValues.getSingleScoreLow()).collect(Collectors.toList());
            mRankingPresenter.refreshCpuList(filteredCpu);*/
        ArrayList<Cpu> filteredCpu = new ArrayList<>();
        for (Cpu cpu: mObjectList) {
            if(Double.parseDouble(cpu.getSingleScore()) > filterValues.getSingleScoreMin() &&
                    Double.parseDouble(cpu.getSingleScore()) < filterValues.getSingleScoreMax() &&
                    Double.parseDouble(cpu.getMultiScore()) > filterValues.getMultiCoreMin() &&
                    Double.parseDouble(cpu.getMultiScore()) < filterValues.getMultiCoreMax()) {
                filteredCpu.add(cpu);
            }
        }
        mRankingPresenter.refreshCpuList(filteredCpu);
    }

}
