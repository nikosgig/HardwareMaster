package hardwaremaster.com.data.repository

import androidx.lifecycle.LiveData
import com.ptrbrynt.firestorelivedata.FirestoreResource
import hardwaremaster.com.data.Gpu
import hardwaremaster.com.data.Price

interface FirestoreRepository {
    suspend fun getGpus(): LiveData<List<Gpu?>>
    suspend fun getGpuDetails(detailGpuId: String): LiveData<Gpu?>
    suspend fun updatePrice(price: Price, id: String): Boolean


    //suspend fun getGpus(): LiveData<FirestoreResource<List<Gpu>>>
    //suspend fun getPrices(): LiveData<FirestoreResource<List<Price>>>
}