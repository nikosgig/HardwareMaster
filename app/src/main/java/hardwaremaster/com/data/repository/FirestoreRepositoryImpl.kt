package hardwaremaster.com.data.repository

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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

    private val refGetUserGpuPrice = firestore.collection(uID.toString()).get()
    private val refGetGpuItems = firestore.collection(COLLECTION_PATH_GPU)


//    suspend fun login(email: String, password: String): AuthResult {
//        return auth.signInWithEmailAndPassword(email, password).await()
//    }
//    suspend fun register(email: String, password: String): AuthResult {
//        return auth.createUserWithEmailAndPassword(email, password).await()
//    }

    override suspend fun getGpus(): LiveData<out List<Gpu?>> {
        return withContext(Dispatchers.IO) {
            //get user specific gpu prices
            val snapshotGetUserGpuPrice = try {
                refGetUserGpuPrice.await()
            } catch (e: Exception) {
                null
            }
            //save user specific gpu prices to list
            val userGpuPriceList = snapshotGetUserGpuPrice?.map {
                val userGpuPriceItem = it.toObject(Price::class.java)
                userGpuPriceItem.id = it.id
                return@map userGpuPriceItem
            }

            //get all Gpus as live data
            val gpuList = refGetGpuItems.toLiveData {
                documents.map { d ->
                    val gpuID = d.id
                    val gpuItem = d.toObject(Gpu::class.java)
                    //replace gpu prices with user specific prices
                    userGpuPriceList?.forEach {
                        if(gpuID == it.id) {
                            gpuItem?.price = it.price
                        }
                    }
                    return@map gpuItem
                }
            }
            //return all Gpus with user specific prices
            return@withContext gpuList
        }
    }



    /*
*
*  CALL RETURNING LIST
*
* */
    //    override suspend fun getGpus(): List<Gpu?>> {
//        val snapshotGpuList = try {
//            firestore.collection("gpu").get().await()
//        } catch (e: Exception) {
//            null
//        }
//        val gpuList = snapshotGpuList?.toObjects(Gpu::class.java)
//
//        val snapshotPriceList = try {
//            firestore.collection(uID.toString()).get().await()
//        } catch (e: Exception) {
//            null
//        }
//        val priceList = snapshotPriceList?.toObjects(Price::class.java)
//
//        gpuList?.forEach {gpuItem ->
//            priceList?.forEach {priceItem ->
//                if(gpuItem.id == priceItem.id) {
//                    gpuItem.price = priceItem.price
//                }
//            }
//        }
//        return gpuList
//    }

    /*
    *
    *  CALLS USING com.github.ptrbrynt:FirestoreLiveData
    *
    * */
//    private val refGetGpuByPriceDesc =
//            firestore.collection(COLLECTION_PATH_GPU)
//                    .orderBy(FIELD_PRICE, Query.Direction.DESCENDING).asLiveData<Gpu>()
//
//    private val refGetUserPrices =
//            firestore.collection(uID.toString()).asLiveData<Price>()
//    override suspend fun getGpus(): LiveData<FirestoreResource<List<Gpu>>> {
//        return withContext(Dispatchers.IO) {
//            return@withContext refGetGpuByPriceDesc
//        }
//    }
//
//    override suspend fun getPrices(): LiveData<FirestoreResource<List<Price>>> {
//        return withContext(Dispatchers.IO) {
//            return@withContext refGetUserPrices
//        }
//    }
}