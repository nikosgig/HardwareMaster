package hardwaremaster.com.internal

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.firestore.*

private const val TAG = "FirestoreLiveData"

fun <T> Query.toLiveData(convert: QuerySnapshot.() -> T): LiveData<T> {
    return object : LiveData<T>(), EventListener<QuerySnapshot> {

        private var listenerRegistration: ListenerRegistration? = null

        override fun onEvent(snapshot: QuerySnapshot?, e: FirebaseFirestoreException?) {
            if (e != null) {
                Log.w(TAG, "Listen failed", e)
                return
            }
            if (snapshot != null && !snapshot.isEmpty) {
                value = convert(snapshot)
            }
        }

        override fun onActive() {
            listenerRegistration = addSnapshotListener(this)
        }

        override fun onInactive() {
            listenerRegistration?.remove()
        }
    }
}