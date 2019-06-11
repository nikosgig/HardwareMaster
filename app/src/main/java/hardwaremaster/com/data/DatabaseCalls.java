package hardwaremaster.com.data;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import hardwaremaster.com.Ranking.GpuRanking.Filter.GpuFilterValues;

public interface DatabaseCalls {

    interface LoadCpusCallback {

        void onCpusLoaded(ArrayList<Cpu> cpuList);
    }

    interface LoadGpusCallback {

        void onGpusLoaded(ArrayList<Gpu> gpuList);
    }

    void getCpus(@NonNull final LoadCpusCallback callback);

    void getGpus(GpuFilterValues gpuFilterValues, @NonNull final LoadGpusCallback callback);

    CpuFilterValues getFilterMinMaxValues();

    List<Cpu> searchFilterCpuList(CharSequence constraint);

    List<Gpu> searchFilterGpuList(CharSequence constraint);

    ArrayList<Cpu> filterCpuList(CpuFilterValues filterValues);

    ArrayList<Gpu> filterGpuList(GpuFilterValues filterValues);
}
