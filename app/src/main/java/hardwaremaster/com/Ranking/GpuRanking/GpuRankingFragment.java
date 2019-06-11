package hardwaremaster.com.Ranking.GpuRanking;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.chip.ChipGroup;

import javax.inject.Inject;

import hardwaremaster.com.Filter.FilterFragment;
import hardwaremaster.com.R;
import hardwaremaster.com.Ranking.CpuRanking.CpuRankingAdapter;
import hardwaremaster.com.Ranking.CpuRanking.CpuRankingContract;
import hardwaremaster.com.data.Cpu;
import hardwaremaster.com.data.Gpu;
import hardwaremaster.com.di.ActivityScoped;
import hardwaremaster.com.widgets.RangeSeekBar;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

@ActivityScoped
public class GpuRankingFragment extends Fragment implements GpuRankingContract.View {

    @Inject
    GpuRankingContract.Presenter mPresenter;
    private GpuRankingAdapter mListAdapter;
    private RecyclerView mRecyclerView;
    ImageView closeButton;
    private Button applyFilterButton;
    private View root;
    private boolean toogleFilterView;
    BottomSheetBehavior bottomSheetBehavior;
    MenuItem menuItem, filterItem;
    SearchView searchView;
    private RangeSeekBar rangeSeekBar;
    OnBottomDialogFilterFragmentListener mListener;

    @Inject
    public GpuRankingFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new GpuRankingAdapter(new ArrayList<Gpu>(0));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GpuRankingFragment.OnBottomDialogFilterFragmentListener) {
            mListener = (GpuRankingFragment.OnBottomDialogFilterFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBottomDialogFilterFragmentListener");
        }
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_recyclerview, container, false);

        //Set up gpu rankings view
        mRecyclerView = root.findViewById(R.id.recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mListAdapter);

        setHasOptionsMenu(true);

        //Set filters



        //Set up seek bars
/*        rangeSeekBars = mListener.OnGpuRangeSeekBarInit();
        seekBarHolder = view.findViewById(R.id.seekbar_placeholder);
        for(RangeSeekBar rangeSeekBar: rangeSeekBars) {
            if(rangeSeekBar.getParent() != null && rangeSeekBars!= null) {
                ((ViewGroup)rangeSeekBar.getParent()).removeView(rangeSeekBar); // <- fix
            }
            seekBarHolder.addView(rangeSeekBar);

        }*/

        MaterialButtonToggleGroup materialButtonToggleGroupSort = root.findViewById(R.id.sortByToggleGroup);
        materialButtonToggleGroupSort.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, @IdRes int checkedId,
                                        boolean isChecked) {
                if(isChecked) {
                    switch (checkedId) {
                        case R.id.sort_price:
                            mPresenter.setSorting(GpuRankingSortBy.BY_PRICE);
                            break;
                        case R.id.sort_1080p:
                            mPresenter.setSorting(GpuRankingSortBy.BY_1080P);
                            break;
                        case R.id.sort_2k:
                            mPresenter.setSorting(GpuRankingSortBy.BY_2K);
                            break;
                        case R.id.sort_4k:
                            mPresenter.setSorting(GpuRankingSortBy.BY_4K);
                            break;
                        default:
                            mPresenter.setSorting(GpuRankingSortBy.ALL);
                            break;
                    }
                }
            }
        });

        MaterialButtonToggleGroup materialButtonToggleGroupVRam = root.findViewById(R.id.vRamToggleGroup);
        materialButtonToggleGroupVRam.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, @IdRes int checkedId,
                                        boolean isChecked) {
                switch (checkedId) {
                    case R.id.vRamButton1:
                        Log.d("button", "clicked1");
                        break;
                    case R.id.vRamButton2:
                        Log.d("button", "clicked2");
                        break;
                    case R.id.vRamButton3:
                        Log.d("button", "clicked3");
                        break;
                    case R.id.vRamButton4:
                        Log.d("button", "clicked4");
                        break;
                    default:
                        Log.d("button", "clicked5");
                        break;
                }

            }
        });

        rangeSeekBar = root.findViewById(R.id.rangeSeekBar);
        rangeSeekBar.setNotifyWhileDragging(true);
        rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Number minValue,
                                                    Number maxValue) {
                mPresenter.setMaxPrice((double) maxValue);
                mPresenter.setMinPrice((double) minValue);
            }
        });
        //setup apply button
        applyFilterButton = root.findViewById(R.id.apply_filter_button);
        applyFilterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
/*                mFilter.setSingleScoreLow(seekBarSingleScore.getSelectedMinValue());
                mFilter.setSingleScoreHigh(seekBarSingleScore.getSelectedMaxValue());*/
                mListener.OnApplyGpuFilterClicked();
            }
        });

        return root;
    }

    /* CpuRankingContract.View callbacks*/
    @Override
    public void notifyGpuListChanged(List<Gpu> gpuList) {
        if(gpuList != null) {
            mListAdapter.setList(gpuList);
            mListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showHideFiltersView() {
        if(!toogleFilterView) {
            ConstraintLayout constraintLayout = root.findViewById(R.id.filters);
            TransitionManager.beginDelayedTransition(constraintLayout);
            constraintLayout.setVisibility(View.VISIBLE);
            LinearLayout mask = root.findViewById(R.id.ll_mask);
            TransitionManager.beginDelayedTransition(mask);
            mask.setVisibility(View.VISIBLE);
            filterItem.setIcon(R.drawable.ic_close);
            searchView.setVisibility(View.GONE);
            toogleFilterView=true;
        } else {
            ConstraintLayout constraintLayout = root.findViewById(R.id.filters);
            TransitionManager.beginDelayedTransition(constraintLayout);
            constraintLayout.setVisibility(View.GONE);
            LinearLayout mask = root.findViewById(R.id.ll_mask);
            TransitionManager.beginDelayedTransition(mask);
            mask.setVisibility(View.GONE);
            filterItem.setIcon(R.drawable.ic_filter_list);
            searchView.setVisibility(View.VISIBLE);
            toogleFilterView=false;
        }

    }

    @Override
    public void setPriceBarMinMaxValues(double min, double max) {
        rangeSeekBar.setRangeValues(min, max);
    }

    @Override
    public void setPriceBarSelectedMinMaxValues(double min, double max) {
        rangeSeekBar.setSelectedMinValue(min);
        rangeSeekBar.setSelectedMaxValue(max);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        /* Setup Search View */
        inflater.inflate(R.menu.menu_options, menu);
        menuItem = menu.findItem(R.id.search);
        filterItem = menu.findItem(R.id.menu_order);

        searchView = (SearchView) menuItem.getActionView();
        searchView.onActionViewExpanded();
        searchView.clearFocus();
//        searchView.setBackground(getResources().getDrawable(R.drawable.bg_white_rounded));
//        LinearLayout searchEditFrame = (LinearLayout) searchView.findViewById(R.id.search_bar); // Get the Linear Layout
// Get the associated LayoutParams and set leftMargin
        //((LinearLayout.LayoutParams) searchEditFrame.getLayoutParams()).leftMargin = 0;
        //searchView.setBackground(R.id.);

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
                mPresenter.getSearchBarFilter().filter(newText);
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
                        mPresenter.setSorting(CpuRankingSortBy.BY_MODEL);
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

    public interface OnBottomDialogFilterFragmentListener {
        void OnApplyGpuFilterClicked();
    }
}
