package hardwaremaster.com.CpuRanking;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hardwaremaster.com.R;
import hardwaremaster.com.data.Cpu;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class CpuRankingFragment extends Fragment implements CpuRankingContract.View {

    private CpuRankingContract.Presenter mPresenter;
    private CpuRankingAdapter mListAdapter;
    private RecyclerView mRecyclerView;

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
        View root = inflater.inflate(R.layout.list_fragment, container, false);

        //Set up cpu rankings view
        mRecyclerView = root.findViewById(R.id.recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mListAdapter);

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    /* CpuRankingContract.View callbacks*/
    @Override
    public void showCpuRanking(List<Cpu> cpus) {
        mListAdapter.setList(cpus);
        mListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_order:
                showOrderByMenu();
                break;
/*            case R.id.order_brand:
                //mPresenter.sortByBrand();
                mPresenter.loadCpuRanking();
                break;
            case R.id.order_single:
                //mPresenter.sortBySingle();
                mPresenter.loadCpuRanking();
                break;*/
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.order_by_fragment, menu);
    }

    @Override
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
                mPresenter.loadCpuRanking();
                return true;
            }
        });
        popup.show();
    }

    @Override
    public void setPresenter(@NonNull CpuRankingContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    public class CpuRankingAdapter extends RecyclerView.Adapter<CpuRankingAdapter.ViewHolder> {
        private List<Cpu> mCpuList;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView textViewModel;
            public TextView textViewDescription;
            public ImageView imageViewImage;
            public TextView textViewTag1;
            public TextView textViewTag2;

            public ViewHolder(View itemView) {
                super(itemView);
                this.textViewModel = itemView.findViewById(R.id.model);
                this.textViewDescription = itemView.findViewById(R.id.description);
                this.imageViewImage = itemView.findViewById(R.id.image);
                this.textViewTag1 = itemView.findViewById(R.id.tag1);
                this.textViewTag2 = itemView.findViewById(R.id.tag2);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public CpuRankingAdapter(List<Cpu> data) {
            this.mCpuList = data;
        }

        private void setList(List<Cpu> cpuList) {
            mCpuList = checkNotNull(cpuList);
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
            CpuRankingAdapter.ViewHolder viewHolder = new CpuRankingAdapter.ViewHolder(view);
            return viewHolder;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element

            holder.textViewModel.setText(mCpuList.get(position).getModel());
            holder.textViewDescription.setText("");
            holder.textViewTag1.setText(mCpuList.get(position).getSingleScore());
            holder.textViewTag2.setText(mCpuList.get(position).getMultiScore());

            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Toast.makeText(getActivity(), "TODO click from presenter with interface.",
                            Toast.LENGTH_SHORT).show();
                }
            });

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mCpuList.size();
        }
    }
}
