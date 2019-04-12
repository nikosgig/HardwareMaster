package hardwaremaster.com.data.source;

import android.support.annotation.NonNull;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;



public class HardwareRepository implements HardwareDataSource {

    private static HardwareRepository INSTANCE = null;

    private final HardwareDataSource mHardwareFirebaseDataSource;

    private final HardwareDataSource mHardwareLocalDataSource;

    boolean mCacheIsDirty = false;

    // Prevent direct instantiation.
    private HardwareRepository(@NonNull HardwareDataSource hardwareFirebaseDataSource,
                            @NonNull HardwareDataSource hardwareLocalDataSource) {
        mHardwareFirebaseDataSource = checkNotNull(hardwareFirebaseDataSource);
        mHardwareLocalDataSource = checkNotNull(hardwareLocalDataSource);
    }

    public static HardwareRepository getInstance(HardwareDataSource hardwareFirebaseDataSource,
                                                 HardwareDataSource hardwareLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new HardwareRepository(hardwareFirebaseDataSource, hardwareLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }


    @Override
    public void getCpus() {
        mHardwareFirebaseDataSource.getCpus();

    }
}
