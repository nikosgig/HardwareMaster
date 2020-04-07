package hardwaremaster.com.ui.ranking.gpu.list

import androidx.lifecycle.ViewModel
import hardwaremaster.com.data.repository.FirestoreRepository
import hardwaremaster.com.internal.lazyDeferred

class RankingGpuListViewModel(
        private val firestoreRepository: FirestoreRepository
) : ViewModel() {

    val gpus by lazyDeferred{
        firestoreRepository.getGpus()
    }

}
