package hardwaremaster.com.ui.ranking.gpu.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import hardwaremaster.com.R
import hardwaremaster.com.data.Gpu
import hardwaremaster.com.internal.ArgNotFoundException
import hardwaremaster.com.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.gpu_detail_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory

class GpuDetailFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: ((Gpu) -> GpuDetailViewModelFactory) by factory()


    private lateinit var viewModel: GpuDetailViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.gpu_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val safeArgs = arguments?.let { GpuDetailFragmentArgs.fromBundle(it) }
        val gpuItem = safeArgs?.gpuItem ?: throw ArgNotFoundException()

        viewModel = ViewModelProviders.of(this, viewModelFactory(gpuItem)).get(GpuDetailViewModel::class.java)

        bindUI()
    }

    private fun bindUI() = launch(Dispatchers.Main) {
        val gpuDetails = viewModel.getGpuDetails.await()
        textView_model.text = gpuDetails.model

        initRecyclerView(toRankingGpuSpec(gpuDetails))
    }

    //convert our list to groupie item
    private fun toRankingGpuSpec(item: Gpu): List<RankingGpuSpec> {
        val gpuSpecs = arrayListOf<RankingGpuSpec>()

        gpuSpecs.add(RankingGpuSpec( getString(R.string.price), item.price.toString(), generatePriceClickListener()))
        gpuSpecs.add(RankingGpuSpec( getString(R.string.score_1080p), item.avgFps1080p.toString()))
        gpuSpecs.add(RankingGpuSpec( getString(R.string.score_1440p), item.avgFps2k.toString()))
        gpuSpecs.add(RankingGpuSpec( getString(R.string.score_4k), item.avgFps4k.toString()))
        gpuSpecs.add(RankingGpuSpec( getString(R.string.score_firestrike), item.firestrike.toString()))
        gpuSpecs.add(RankingGpuSpec( getString(R.string.score_passmark), item.passmark.toString()))
        gpuSpecs.add(RankingGpuSpec( getString(R.string.filter_vram), item.graphicsRamSize.toString()  + " GB"))
        gpuSpecs.add(RankingGpuSpec( getString(R.string.type_vram), item.graphicsRamType.toString()))
        gpuSpecs.add(RankingGpuSpec( getString(R.string.gpu_clock), item.passmark.toString() + " MHz"))
        gpuSpecs.add(RankingGpuSpec( getString(R.string.memory_bus), item.memoryBus.toString()))
        gpuSpecs.add(RankingGpuSpec( getString(R.string.memory_clock), item.memoryClock.toString()))
        gpuSpecs.add(RankingGpuSpec( getString(R.string.release_date), item.releaseDate.toString()))

        return gpuSpecs
    }

    private fun initRecyclerView(gpuDetails: List<RankingGpuSpec>) {
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(gpuDetails)
        }
        recyclerView_details.apply {
            layoutManager = LinearLayoutManager(this@GpuDetailFragment.context)
            adapter = groupAdapter
        }
    }


    private fun generatePriceClickListener(): View.OnClickListener {

        return View.OnClickListener { view: View ->
            val builder = context?.let { AlertDialog.Builder(it) }
            builder?.setMessage("Blah")?.show()
        }
    }
}
