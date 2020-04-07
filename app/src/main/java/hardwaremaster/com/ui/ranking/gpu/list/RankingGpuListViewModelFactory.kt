package hardwaremaster.com.ui.ranking.gpu.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hardwaremaster.com.data.repository.FirestoreRepository

class RankingGpuListViewModelFactory(
        private val firestoreRepository: FirestoreRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RankingGpuListViewModel(
                firestoreRepository
        ) as T
    }
}