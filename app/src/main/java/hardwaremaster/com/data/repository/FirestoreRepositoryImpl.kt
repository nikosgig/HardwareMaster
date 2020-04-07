package hardwaremaster.com.data.repository

import androidx.lifecycle.LiveData
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hardwaremaster.com.data.Gpu
import hardwaremaster.com.internal.await
import hardwaremaster.com.internal.toLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FirestoreRepositoryImpl : FirestoreRepository {

    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

//    suspend fun login(email: String, password: String): AuthResult {
//        return auth.signInWithEmailAndPassword(email, password).await()
//    }
//    suspend fun register(email: String, password: String): AuthResult {
//        return auth.createUserWithEmailAndPassword(email, password).await()
//    }

    override suspend fun getGpus(): LiveData<out List<Gpu?>> {
        return withContext(Dispatchers.IO) {
            return@withContext firestore.collection("gpu").toLiveData{
                documents.map { d ->
                    d.toObject(Gpu::class.java)
                }
            }
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