package hardwaremaster.com.ui.ranking.cpu.detail

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import hardwaremaster.com.R

class CpuDetailFragment : Fragment() {

    companion object {
        fun newInstance() = CpuDetailFragment()
    }

    private lateinit var viewModel: CpuDetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.cpu_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CpuDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
