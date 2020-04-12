package hardwaremaster.com.ui.ranking.gpu.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hardwaremaster.com.data.repository.FirestoreRepository
import hardwaremaster.com.internal.lazyDeferred

class RankingGpuListViewModel(
        private val firestoreRepository: FirestoreRepository
) : ViewModel() {

    val gpus by lazyDeferred{
        firestoreRepository.getGpus()
    }

    val prices by lazyDeferred{
        firestoreRepository.getPrices()
    }

    fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }
}
