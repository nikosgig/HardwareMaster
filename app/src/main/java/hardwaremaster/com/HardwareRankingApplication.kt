package hardwaremaster.com

import android.app.Application
import hardwaremaster.com.data.repository.FirestoreRepositoryImpl
import hardwaremaster.com.ui.ranking.gpu.list.RankingGpuListViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class HardwareRankingApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        //provide of instances of context / services
        import(androidXModule(this@HardwareRankingApplication))

        bind() from singleton { FirestoreRepositoryImpl() }
        bind() from provider { RankingGpuListViewModelFactory(instance()) }
    }

}