package hardwaremaster.com.ui.ranking.cpu.list

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import hardwaremaster.com.R

class RankingCpuListFragment : Fragment() {

    companion object {
        fun newInstance() = RankingCpuListFragment()
    }

    private lateinit var viewModel: RankingCpuListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.ranking_cpu_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RankingCpuListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
