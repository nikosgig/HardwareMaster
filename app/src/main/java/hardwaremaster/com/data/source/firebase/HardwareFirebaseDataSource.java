package hardwaremaster.com.data.source.firebase;

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
import hardwaremaster.com.data.source.HardwareDataSource;

public class HardwareFirebaseDataSource implements HardwareDataSource {

    private static HardwareFirebaseDataSource INSTANCE;
    private static DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    private List<Object> cpuList = new ArrayList<>();



    public static HardwareFirebaseDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HardwareFirebaseDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getCpus() {
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot cpuDataSnapshot : dataSnapshot.getChildren()) {
                    cpuList.add(cpuDataSnapshot.getValue(Cpu.class));
                    Log.d("hi", cpuDataSnapshot.getValue(Cpu.class).getModel());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
