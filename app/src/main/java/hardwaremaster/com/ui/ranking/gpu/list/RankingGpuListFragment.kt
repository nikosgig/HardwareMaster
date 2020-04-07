package hardwaremaster.com.ui.ranking.gpu.list

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

import hardwaremaster.com.R
import hardwaremaster.com.a_old.data.Gpu
import hardwaremaster.com.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.ranking_gpu_list_fragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class RankingGpuListFragment : ScopedFragment(), KodeinAware {

    override val kodein: Kodein by closestKodein() //can have local fragment kodein
    private val viewModelFactory: RankingGpuListViewModelFactory by instance()

    private lateinit var viewModel: RankingGpuListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.ranking_gpu_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(RankingGpuListViewModel::class.java)
        bindUI()
//        val repo = FirestoreRepository()
//        GlobalScope.launch(Dispatchers.Main) {
//            val gpu = repo.getGpus()
//            textView.text = gpu.toString()
//        }
    }

    private fun bindUI() = launch {
        val gpuList = viewModel.gpus.await()
        gpuList.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer

            textView.text = it.toString()
        })
    }

}
