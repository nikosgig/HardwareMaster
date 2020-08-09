package hardwaremaster.com.ui.ranking.gpu.list

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

import hardwaremaster.com.R
import hardwaremaster.com.data.Gpu
import hardwaremaster.com.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.ranking_gpu_list_fragment.*
import kotlinx.coroutines.Dispatchers
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
    }

    private fun bindUI() = launch(Dispatchers.Main) {
        val gpuList = viewModel.gpus.await()

        gpuList.observe(viewLifecycleOwner, Observer {gpuItems ->
            if (gpuItems == null) return@Observer

            val gpuItemsList = sortGpuList(gpuItems as List<Gpu>)
            group_loading.visibility = View.GONE

            initRecyclerView(gpuItemsList.toRankingGpuItems())
        })
    }



    private fun updateTitleBar() {

    }

    private fun sortGpuList(list: List<Gpu>): List<Gpu> {
        return list.sortedByDescending { item -> item.price }
    }

    //convert our list to groupie item
    private fun List<Gpu>.toRankingGpuItems(): List<RankingGpuItem> {
        return this.map {
            RankingGpuItem(it)
        }
    }

    private fun initRecyclerView(items: List<RankingGpuItem>) {
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(items)
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@RankingGpuListFragment.context)
            adapter = groupAdapter
        }

        groupAdapter.setOnItemClickListener { item, view ->
            (item as? RankingGpuItem)?.let {
                //showWeatherDetail(it.weatherEntry.isDay, view)
                showGpuDetail(it.gpuEntry.id!!, view)
            }
        }
    }

    private fun showGpuDetail(gpuItemID: String, view: View) {
        val actionDetail = RankingGpuListFragmentDirections.actionDetail(gpuItemID)
        Navigation.findNavController(view).navigate(actionDetail)
    }
}
