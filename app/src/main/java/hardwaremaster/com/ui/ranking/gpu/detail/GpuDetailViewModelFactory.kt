package hardwaremaster.com.ui.ranking.gpu.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hardwaremaster.com.data.Gpu
import hardwaremaster.com.data.repository.FirestoreRepository
import hardwaremaster.com.internal.lazyDeferred

class GpuDetailViewModelFactory(
        private val detailGpuId: String,
        private val firestoreRepository: FirestoreRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GpuDetailViewModel(
                detailGpuId,
                firestoreRepository
        ) as T
    }
}