package hardwaremaster.com.data.repository

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.ptrbrynt.firestorelivedata.QueryLiveData
import com.ptrbrynt.firestorelivedata.asLiveData
import hardwaremaster.com.data.Gpu
import hardwaremaster.com.data.Price
import hardwaremaster.com.internal.await
import hardwaremaster.com.internal.toLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

const val COLLECTION_PATH_GPU = "gpu"
const val COLLECTION_PATH_USER_PRICE = "user_price"
const val FIELD_PRICE = "price"

class FirestoreRepositoryImpl : FirestoreRepository {

    val auth = FirebaseAuth.getInstance()
    val uID = auth.uid
    private val firestore = FirebaseFirestore.getInstance()

    private val refGetGpuByPriceDesc =
            firestore.collection(COLLECTION_PATH_GPU)
                    .orderBy(FIELD_PRICE, Query.Direction.DESCENDING).asLiveData<Gpu>()

    private val refGetUserPrices =
            firestore.collection(uID.toString()).asLiveData<Price>()

//    suspend fun login(email: String, password: String): AuthResult {
//        return auth.signInWithEmailAndPassword(email, password).await()
//    }
//    suspend fun register(email: String, password: String): AuthResult {
//        return auth.createUserWithEmailAndPassword(email, password).await()
//    }

    override suspend fun getGpus(): LiveData<FirestoreResource<List<Gpu>>> {
        return withContext(Dispatchers.IO) {
            return@withContext refGetGpuByPriceDesc
        }
    }

    override suspend fun getPrices(): LiveData<FirestoreResource<List<Price>>> {
        return withContext(Dispatchers.IO) {
            return@withContext refGetUserPrices
        }
    }



    //    override suspend fun getGpus(): List<Gpu>? {
//        val snapshot = try {
//            firestore.collection("gpu").get().await()
//        } catch (e: Exception) {
//            null
//        }
//        return snapshot?.toObjects(Gpu::class.java)
//    }
}