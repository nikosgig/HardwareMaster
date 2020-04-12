package hardwaremaster.com.ui.ranking.gpu.list

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

import hardwaremaster.com.R
import hardwaremaster.com.data.Gpu
import hardwaremaster.com.data.Price
import hardwaremaster.com.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.ranking_gpu_list_fragment.*
import kotlinx.coroutines.Dispatchers
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
    }

    private fun bindUI() = launch(Dispatchers.Main) {
        val gpuList = viewModel.gpus.await()
        val prices = viewModel.prices.await()


        gpuList.observe(viewLifecycleOwner, Observer {
            if (it?.data == null) return@Observer

            group_loading.visibility = View.GONE
            initRecyclerView((it.data as List<Gpu>).toRankingGpuItems())

            //textView.text = it.toString()
        })

        prices.observe(viewLifecycleOwner, Observer {
            if (it?.data == null) return@Observer

            val data = it.data as List<Price>
            //textView.text = it.toString()
        })
    }

    private fun updateTitleBar() {

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
    }
}
