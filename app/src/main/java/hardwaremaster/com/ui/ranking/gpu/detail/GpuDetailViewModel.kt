package hardwaremaster.com.ui.ranking.gpu.detail

import androidx.lifecycle.ViewModel
import hardwaremaster.com.data.Gpu
import hardwaremaster.com.data.repository.FirestoreRepository
import hardwaremaster.com.internal.lazyDeferred

class GpuDetailViewModel(
        private val detailGpu: Gpu,
        private val firestoreRepository: FirestoreRepository
        ) : ViewModel() {

    val setPrice by lazyDeferred {
        //firestoreRepository.setPrice()
    }

    val getGpuDetails by lazyDeferred {
        detailGpu
    }

}
