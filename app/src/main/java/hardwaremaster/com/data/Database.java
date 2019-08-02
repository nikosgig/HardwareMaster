package hardwaremaster.com.data;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import hardwaremaster.com.Base.BasePresenter;
import hardwaremaster.com.Ranking.CpuRanking.CpuRankingPresenter;
import hardwaremaster.com.Ranking.GpuRanking.Filter.GpuFilterValues;
import hardwaremaster.com.Ranking.GpuRanking.GpuRankingPresenter;

@Singleton
public class Database implements DatabaseCalls {

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private ArrayList<Cpu> mCpuList = new ArrayList<>();
    private ArrayList<Gpu> mGpuList = new ArrayList<>();

    @Inject
    Database() {
    }

    @Override
    public void getCpus(@NonNull final LoadCpusCallback callback) {
        mDatabase.getReference("cpu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCpuList.clear();
                for (DataSnapshot cpuDataSnapshot : dataSnapshot.getChildren()) {
                    mCpuList.add(cpuDataSnapshot.getValue(Cpu.class));
                }
                //mCpuRankingsView.notifyCpuListChanged(cpuList);
                //mCpuRankingPresenter.onGetCpuFromDatabase(mCpuList);
                callback.onCpusLoaded(mCpuList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void getGpus(GpuFilterValues gpuFilterValues, @NonNull final LoadGpusCallback callback) {
        mDatabase.getReference("price/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Long> valuesMap = new HashMap<>();
                for (DataSnapshot userPriceSnapshot : dataSnapshot.getChildren()) {
                    valuesMap.put(userPriceSnapshot.getKey(),
                            (Long) userPriceSnapshot.getValue());
                }

                mDatabase.getReference("gpu").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mGpuList.clear();
                        for (DataSnapshot gpuDataSnapshot : dataSnapshot.getChildren()) {
                            if (gpuDataSnapshot.getValue(Gpu.class).getPrice() >= gpuFilterValues.getMinPrice()
                                    && gpuDataSnapshot.getValue(Gpu.class).getPrice() <= gpuFilterValues.getMaxPrice()) {
                                Gpu curGpu = gpuDataSnapshot.getValue(Gpu.class);
                                if (curGpu != null) {
                                    curGpu.setKey(gpuDataSnapshot.getKey());

                                    for (HashMap.Entry<String, Long> entry :
                                            valuesMap.entrySet()) {
                                        if (curGpu.getKey().equals(entry.getKey())) {
                                            curGpu.setPrice((double) entry.getValue());
                                        }
                                    }
                                }
                                mGpuList.add(curGpu);
                            }
                            //Log.d("hi", cpuDataSnapshot.getValue(Cpu.class).getModel());
                        }
                        //mCpuRankingsView.notifyCpuListChanged(cpuList);
                        //mCpuRankingPresenter.refreshCpuList(mCpuList);
                        //mGpuRankingPresenter.onGetCpuFromDatabase(mGpuList);
                        callback.onGpusLoaded(mGpuList);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
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

    @Override
    public List<Cpu> searchFilterCpuList(CharSequence constraint) {
        List<Cpu> filteredList = new ArrayList<>();
        String filterPattern = constraint.toString().toLowerCase().trim();
        for (Cpu item : mCpuList) {
            if (item.getModel().toLowerCase().contains(filterPattern)) {
                filteredList.add(item);
            }
        }

        return filteredList;
    }

    @Override
    public List<Gpu> searchFilterGpuList(CharSequence constraint) {
        List<Gpu> filteredList = new ArrayList<>();
        String filterPattern = constraint.toString().toLowerCase().trim();
        for (Gpu item : mGpuList) {
            if (item.getModel().toLowerCase().contains(filterPattern)) {
                filteredList.add(item);
            }
        }

        return filteredList;
    }

    @Override
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

    @Override
    public ArrayList<Gpu> filterGpuList(GpuFilterValues filterValues) {
        return mGpuList;
    }

    @Override
    public void addUserPrice(String key, double price) {
        final DatabaseReference usersRef = mDatabase.getReference("price").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usersRef.child(key).setValue(price);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
