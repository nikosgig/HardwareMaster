package hardwaremaster.com.ui.ranking.gpu.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardwaremaster.com.data.Gpu
import hardwaremaster.com.data.Price
import hardwaremaster.com.data.repository.FirestoreRepository
import hardwaremaster.com.internal.lazyDeferred
import kotlinx.coroutines.launch

class GpuDetailViewModel(
        private val detailGpu: Gpu,
        private val firestoreRepository: FirestoreRepository
) : ViewModel() {

    fun updatePrice(price: Long) {
        viewModelScope.launch {
            detailGpu.id?.let {

                firestoreRepository.updatePrice(Price(price), it)
                detailGpu.price = price
            }
        }
    }

    val getGpuDetails by lazyDeferred {
        detailGpu
    }

}
