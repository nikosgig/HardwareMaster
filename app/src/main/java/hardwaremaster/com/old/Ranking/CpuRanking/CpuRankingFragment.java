package hardwaremaster.com.old.Ranking.CpuRanking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import hardwaremaster.com.R;
import hardwaremaster.com.old.data.Cpu;
import hardwaremaster.com.old.di.ActivityScoped;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

@ActivityScoped
public class CpuRankingFragment extends DaggerFragment implements CpuRankingContract.View {

    @Inject
    CpuRankingContract.Presenter mPresenter;
    private CpuRankingAdapter mListAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private ImageView closeButton;

    @Inject
    public CpuRankingFragment() {
        // Requires empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new CpuRankingAdapter(new ArrayList<Cpu>(0));
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.takeView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.dropView();  //prevent leaking activity in
        // case presenter is orchestrating a long running task
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recyclerview, container, false);

        //Set up cpu rankings view
        mProgressBar = root.findViewById(R.id.progress_bar);
        mRecyclerView = root.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setItemPrefetchEnabled(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mListAdapter);

        setHasOptionsMenu(true);

        return root;
    }

    /* CpuRankingContract.View callbacks*/
    @Override
    public void notifyCpuListChanged(List<Cpu> cpus) {
        if(cpus != null) {
            mListAdapter.setList(cpus);
            mListAdapter.notifyDataSetChanged();
            mRecyclerView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
