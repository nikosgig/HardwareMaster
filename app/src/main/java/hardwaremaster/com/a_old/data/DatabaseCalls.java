package hardwaremaster.com.a_old.data;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import hardwaremaster.com.a_old.Ranking.GpuRanking.Filter.GpuFilterValues;

public interface DatabaseCalls {

    interface LoadCpusCallback {

        void onCpusLoaded(ArrayList<Cpu> cpuList);
    }

    interface LoadGpusCallback {

        void onGpusLoaded(ArrayList<Gpu> gpuList);
    }

    void getCpus(@NonNull final LoadCpusCallback callback);

    void getGpus(@NonNull final LoadGpusCallback callback);

    List<Cpu> searchFilterCpuList(CharSequence constraint);

    List<Gpu> searchFilterGpuList(CharSequence constraint);

    ArrayList<Cpu> filterCpuList(CpuFilterValues filterValues);

    ArrayList<Gpu> filterGpuList(GpuFilterValues filterValues);

    void addUserPrice(String key, double price);

    void setGpuFilters(GpuFilterValues gpuFilters);
}