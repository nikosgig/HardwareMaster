package hardwaremaster.com.ui.ranking.gpu.detail

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import hardwaremaster.com.R
import hardwaremaster.com.data.Gpu
import hardwaremaster.com.internal.ArgNotFoundException
import hardwaremaster.com.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.dialog_input_price.view.*
import kotlinx.android.synthetic.main.gpu_detail_fragment.*
import kotlinx.android.synthetic.main.ranking_gpu_list_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance

class GpuDetailFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactoryInstanceFactory
            : ((String) -> GpuDetailViewModelFactory) by factory()

    private lateinit var gpuDetails: Gpu


    private lateinit var viewModel: GpuDetailViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.gpu_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val safeArgs = arguments?.let { GpuDetailFragmentArgs.fromBundle(it) }
        val gpuItemId = safeArgs?.gpuItemID ?: throw ArgNotFoundException()

        viewModel = ViewModelProviders.of(this, viewModelFactoryInstanceFactory(gpuItemId)).get(GpuDetailViewModel::class.java)

        bindUI()
    }

    private fun bindUI() = launch(Dispatchers.Main) {
        val gpuDetails = viewModel.gpuDetails.await()

        gpuDetails.observe(viewLifecycleOwner, Observer {gpuDetails ->
            if (gpuDetails == null) return@Observer
            textView_model.text = gpuDetails.model
            initRecyclerView(toRankingGpuSpec(gpuDetails))
        })
    }

    //convert our list to groupie item
    private fun toRankingGpuSpec(item: Gpu): List<RankingGpuSpec> {
        val gpuSpecs = arrayListOf<RankingGpuSpec>()

        gpuSpecs.add(RankingGpuSpec(getString(R.string.price), item.price.toString(), generatePriceClickListener()))
        gpuSpecs.add(RankingGpuSpec(getString(R.string.score_1080p), item.avgFps1080p.toString()))
        gpuSpecs.add(RankingGpuSpec(getString(R.string.score_1440p), item.avgFps2k.toString()))
        gpuSpecs.add(RankingGpuSpec(getString(R.string.score_4k), item.avgFps4k.toString()))
        gpuSpecs.add(RankingGpuSpec(getString(R.string.score_firestrike), item.firestrike.toString()))
        gpuSpecs.add(RankingGpuSpec(getString(R.string.score_passmark), item.passmark.toString()))
        gpuSpecs.add(RankingGpuSpec(getString(R.string.filter_vram), item.graphicsRamSize.toString() + " GB"))
        gpuSpecs.add(RankingGpuSpec(getString(R.string.type_vram), item.graphicsRamType.toString()))
        gpuSpecs.add(RankingGpuSpec(getString(R.string.gpu_clock), item.passmark.toString() + " MHz"))
        gpuSpecs.add(RankingGpuSpec(getString(R.string.memory_bus), item.memoryBus.toString()))
        gpuSpecs.add(RankingGpuSpec(getString(R.string.memory_clock), item.memoryClock.toString()))
        gpuSpecs.add(RankingGpuSpec(getString(R.string.release_date), item.releaseDate.toString()))

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
            generatePriceDialog()
        }
    }

    @SuppressLint("InflateParams")
    private fun generatePriceDialog() {
        context?.let { context ->
            val builder = MaterialAlertDialogBuilder(context, R.style.dialogTheme)
            builder.setTitle(getString(R.string.dialog_price_title))
            val dialogView = layoutInflater.inflate(R.layout.dialog_input_price, null) //pass null cause it's a Dialog
            builder.setView(dialogView)
            val input: TextInputEditText = dialogView.editText
            input.requestFocus()
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
            //builder.setMessage(msg)
            //builder.setCancelable(isCancellable)
            builder.setPositiveButton(getString(R.string.confirm)) { dialogInterface: DialogInterface, i: Int ->
                val price = input.text.toString().toLongOrNull()
                price?.let {
                    viewModel.updatePrice(it)
                }
                //mPresenter.updatePrice(mGpuList.get(position).getKey(), Double.valueOf(price));

            }
            builder.setNegativeButton(getString(R.string.dismiss)) { dialogInterface: DialogInterface, i: Int ->
                activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialogInterface.dismiss()
            }
            builder.setOnCancelListener {
                activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                it.dismiss()
            }
            val alertDialog = builder.create()
            alertDialog.show()
        }
    }


}
