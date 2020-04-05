package hardwaremaster.com.old.util;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

import hardwaremaster.com.old.data.Gpu;

public class MyDiffCallback extends DiffUtil.Callback{

    private List<Gpu> oldGpuList;
    private List<Gpu> newGpuList;

    public MyDiffCallback(List<Gpu> newGpuList, List<Gpu> oldGpuList) {
        this.newGpuList = newGpuList;
        this.oldGpuList = oldGpuList;
    }

    @Override
    public int getOldListSize() {
        return oldGpuList.size();
    }

    @Override
    public int getNewListSize() {
        return newGpuList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldGpuList.get(oldItemPosition).getModel().equals(newGpuList.get(newItemPosition).getModel());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldGpuList.get(oldItemPosition).equals(newGpuList.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        //you can return particular field for changed item.
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}