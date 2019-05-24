package hardwaremaster.com.Ranking.CpuRanking;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import hardwaremaster.com.R;
import hardwaremaster.com.data.Cpu;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class CpuRankingFragment extends Fragment implements CpuRankingContract.View {

    private CpuRankingContract.Presenter mPresenter;
    private CpuRankingAdapter mListAdapter;
    private RecyclerView mRecyclerView;
    private ImageView closeButton;

    public CpuRankingFragment() {
    }

    public static CpuRankingFragment newInstance() {
        return new CpuRankingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new CpuRankingAdapter(new ArrayList<Cpu>(0));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recyclerview, container, false);

        //Set up cpu rankings view
        mRecyclerView = root.findViewById(R.id.recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
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
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        /* Setup Search View */
        inflater.inflate(R.menu.menu_options, menu);
        MenuItem menuItem = menu.findItem(R.id.search);

        final SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.onActionViewExpanded();
        searchView.clearFocus();

        // Catch event on [x] button inside search view
        closeButton = searchView.findViewById(R.id.search_close_btn);
        // Set on click listener
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Manage this event.
                searchView.setQuery("", false);
                searchView.clearFocus();
            }
        });


        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mListAdapter.getFilter().filter(newText);
                if (TextUtils.isEmpty(newText)) {
                    //Text is cleared, do your thing
                    searchView.clearFocus();
                }
                return true;
            }
        });

    }

/*    @Override
    public void showOrderByMenu() {
        PopupMenu popup = new PopupMenu(getContext(), getActivity().findViewById(R.id.menu_order));
        popup.getMenuInflater().inflate(R.menu.order_by_options, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.order_brand:
                        //mPresenter.setFiltering(TasksFilterType.ACTIVE_TASKS);
                        mPresenter.setOrder(CpuRankingSortBy.BY_MODEL);
                        break;
                    case R.id.order_single:
                        //mPresenter.setFiltering(TasksFilterType.COMPLETED_TASKS);
                        break;
                }
                mPresenter.getCpuFromDatabase();
                return true;
            }
        });
        popup.show();
    }*/

    @Override
    public void setPresenter(@NonNull CpuRankingContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
