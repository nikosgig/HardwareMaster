package hardwaremaster.com.data.repository

import androidx.lifecycle.LiveData
import hardwaremaster.com.data.Gpu

interface FirestoreRepository {
    suspend fun getGpus(): LiveData<out List<Gpu?>>
}