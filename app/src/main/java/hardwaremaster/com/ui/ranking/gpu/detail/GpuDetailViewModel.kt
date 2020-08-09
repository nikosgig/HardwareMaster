package hardwaremaster.com.ui.ranking.gpu.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardwaremaster.com.data.Gpu
import hardwaremaster.com.data.Price
import hardwaremaster.com.data.repository.FirestoreRepository
import hardwaremaster.com.internal.lazyDeferred
import kotlinx.coroutines.launch

class GpuDetailViewModel(
        private val detailGpuId: String,
        private val firestoreRepository: FirestoreRepository
) : ViewModel() {

    val gpuDetails by lazyDeferred{
        firestoreRepository.getGpuDetails(detailGpuId)
    }

    fun updatePrice(price: Long) {
        viewModelScope.launch {
                firestoreRepository.updatePrice(Price(price), detailGpuId)
            }
        }

}
