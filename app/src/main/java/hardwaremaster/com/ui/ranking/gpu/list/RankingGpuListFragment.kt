package hardwaremaster.com.ui.ranking.gpu.list

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import hardwaremaster.com.R
import hardwaremaster.com.data.repository.FirestoreRepository
import kotlinx.android.synthetic.main.ranking_gpu_list_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RankingGpuListFragment : Fragment() {

    companion object {
        fun newInstance() = RankingGpuListFragment()
    }

    private lateinit var viewModel: RankingGpuListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.ranking_gpu_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RankingGpuListViewModel::class.java)
        // TODO: Use the ViewModel
//        val repo = FirestoreRepository()
//        GlobalScope.launch(Dispatchers.Main) {
//            val gpu = repo.getGpus()
//            textView.text = gpu.toString()
//        }
    }

}
