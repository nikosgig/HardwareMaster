package hardwaremaster.com.ui.ranking.gpu.detail

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import hardwaremaster.com.R

class GpuDetailFragment : Fragment() {

    companion object {
        fun newInstance() = GpuDetailFragment()
    }

    private lateinit var viewModel: GpuDetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.gpu_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GpuDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
