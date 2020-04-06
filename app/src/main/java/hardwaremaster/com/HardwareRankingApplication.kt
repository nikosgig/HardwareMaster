package hardwaremaster.com

import android.app.Application
import hardwaremaster.com.data.repository.FirestoreRepository
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class HardwareRankingApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        //provide of instances of context / services
        import(androidXModule(this@HardwareRankingApplication))

        bind() from singleton { FirestoreRepository() }
    }

}