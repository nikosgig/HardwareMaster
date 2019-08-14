package hardwaremaster.com.Ranking.GpuRanking;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import javax.inject.Inject;

import hardwaremaster.com.R;
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

        SearchView customSearchView = (SearchView) root.findViewById(R.id.customSearchView);

        customSearchView.onActionViewExpanded(); //new Added line
        customSearchView.setIconifiedByDefault(false);

        if(!customSearchView.isFocused()) {
            customSearchView.clearFocus();
        }

        // Catch event on [x] button inside search view
        closeButton = customSearchView.findViewById(R.id.search_close_btn);
        // Set on click listener
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Manage this event.
                customSearchView.setQuery("", false);
                customSearchView.clearFocus();
            }
        });


        customSearchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        customSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                customSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mPresenter.getSearchBarFilter().filter(newText);
                if (TextUtils.isEmpty(newText)) {
                    //Text is cleared, do your thing
                    customSearchView.clearFocus();
                }
                return true;
            }
        });

        //Set filters
//
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        TextView textView = root.findViewById(R.id.appCompatTextView);
//        textView.setText(auth.getCurrentUser().getEmail());
//
//        //Set up seek bars
///*        rangeSeekBars = mListener.OnGpuRangeSeekBarInit();
//        seekBarHolder = view.findViewById(R.id.seekbar_placeholder);
//        for(RangeSeekBar rangeSeekBar: rangeSeekBars) {
//            if(rangeSeekBar.getParent() != null && rangeSeekBars!= null) {
//                ((ViewGroup)rangeSeekBar.getParent()).removeView(rangeSeekBar); // <- fix
//            }
//            seekBarHolder.addView(rangeSeekBar);
//
//        }*/
//
//        MaterialButtonToggleGroup materialButtonToggleGroupSort = root.findViewById(R.id.sortByToggleGroup);
//        materialButtonToggleGroupSort.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
//            @Override
//            public void onButtonChecked(MaterialButtonToggleGroup group, @IdRes int checkedId,
//                                        boolean isChecked) {
//                if(isChecked) {
//                    switch (checkedId) {
//                        case R.id.sort_price:
//                            mPresenter.setSorting(GpuRankingSortBy.BY_PRICE);
//                            break;
//                        case R.id.sort_1080p:
//                            mPresenter.setSorting(GpuRankingSortBy.BY_1080P);
//                            break;
//                        case R.id.sort_2k:
//                            mPresenter.setSorting(GpuRankingSortBy.BY_2K);
//                            break;
//                        case R.id.sort_4k:
//                            mPresenter.setSorting(GpuRankingSortBy.BY_4K);
//                            break;
//                        default:
//                            mPresenter.setSorting(GpuRankingSortBy.ALL);
//                            break;
//                    }
//                }
//            }
//        });
//
//        MaterialButtonToggleGroup materialButtonToggleGroupVRam = root.findViewById(R.id.vRamToggleGroup);
//        materialButtonToggleGroupVRam.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
//            @Override
//            public void onButtonChecked(MaterialButtonToggleGroup group, @IdRes int checkedId,
//                                        boolean isChecked) {
//                switch (checkedId) {
//                    case R.id.vRamButton1:
//                        Log.d("button", "clicked1");
//                        break;
//                    case R.id.vRamButton2:
//                        Log.d("button", "clicked2");
//                        break;
//                    case R.id.vRamButton3:
//                        Log.d("button", "clicked3");
//                        break;
//                    case R.id.vRamButton4:
//                        Log.d("button", "clicked4");
//                        break;
//                    default:
//                        Log.d("button", "clicked5");
//                        break;
//                }
//
//            }
//        });
//
//        rangeSeekBar = root.findViewById(R.id.rangeSeekBar);
//        rangeSeekBar.setNotifyWhileDragging(true);
//        rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
//            @Override
//            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Number minValue,
//                                                    Number maxValue) {
//                mPresenter.setMaxPrice((double) maxValue);
//                mPresenter.setMinPrice((double) minValue);
//            }
//        });
//        //setup apply button
//        applyFilterButton = root.findViewById(R.id.apply_filter_button);
//        applyFilterButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
///*                mFilter.setSingleScoreLow(seekBarSingleScore.getSelectedMinValue());
//                mFilter.setSingleScoreHigh(seekBarSingleScore.getSelectedMaxValue());*/
//                mListener.OnApplyGpuFilterClicked();
//            }
//        });

        return root;
    }

    /* CpuRankingContract.View callbacks*/
    @Override
    public void notifyGpuListChanged(List<Gpu> gpuList) {
//        for ( Gpu curGpu : gpuList)
//        {
//            if(curGpu.getPrice()!=0) {
//                curGpu.setScore((curGpu.getAvgFps1080p() + curGpu.getAvgFps2k() + curGpu.getAvgFps4k())/curGpu.getPrice());
//            } else {
//                curGpu.setScore(0.0);
//            }
//        }
//        Collections.sort(gpuList,
//                (o1, o2) -> o1.getScore().compareTo(o2.getScore()));

        if(gpuList != null) {
            mListAdapter.setList(gpuList);
            mListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showHideFiltersView() {
//        if(!toogleFilterView) {
//            ConstraintLayout constraintLayout = root.findViewById(R.id.filters);
//            TransitionManager.beginDelayedTransition(constraintLayout);
//            constraintLayout.setVisibility(View.VISIBLE);
//            LinearLayout mask = root.findViewById(R.id.ll_mask);
//            TransitionManager.beginDelayedTransition(mask);
//            mask.setVisibility(View.VISIBLE);
//            filterItem.setIcon(R.drawable.ic_close);
//            searchView.setVisibility(View.GONE);
//            toogleFilterView=true;
//        } else {
//            ConstraintLayout constraintLayout = root.findViewById(R.id.filters);
//            TransitionManager.beginDelayedTransition(constraintLayout);
//            constraintLayout.setVisibility(View.GONE);
//            LinearLayout mask = root.findViewById(R.id.ll_mask);
//            TransitionManager.beginDelayedTransition(mask);
//            mask.setVisibility(View.GONE);
//            filterItem.setIcon(R.drawable.ic_filter_list);
//            searchView.setVisibility(View.VISIBLE);
//            toogleFilterView=false;
//        }

    }

    @Override
    public void setPriceBarMinMaxValues(double min, double max) {
        //rangeSeekBar.setRangeValues(min, max);
    }

    @Override
    public void setPriceBarSelectedMinMaxValues(double min, double max) {
        //rangeSeekBar.setSelectedMinValue(min);
        //rangeSeekBar.setSelectedMaxValue(max);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        /* Setup Search View */
        inflater.inflate(R.menu.menu_options, menu);
        menuItem = menu.findItem(R.id.search);
        //filterItem = menu.findItem(R.id.menu_order);

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

//    public class GpuRankingAdapter extends RecyclerView.Adapter<GpuRankingAdapter.ViewHolder> {
//        private List<Gpu> mGpuList;
//
//
//        // Provide a suitable constructor (depends on the kind of dataset)
//        public GpuRankingAdapter(List<Gpu> data) {
//            this.mGpuList = data;
//        }
//
//        public void setList(List<Gpu> gpuList) {
//            mGpuList = checkNotNull(gpuList);
//        }
//
//        // Create new views (invoked by the layout manager)
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_with_expandable_cardview,
//                    parent, false);
//            ViewHolder viewHolder = new ViewHolder(view);
//            return viewHolder;
//        }
//
//        // Replace the contents of a view (invoked by the layout manager)
//        @Override
//        public void onBindViewHolder(ViewHolder holder, int position) {
//            // - get element from your dataset at this position
//            // - replace the contents of the view with that element
//
//            holder.titleGpuModel.setText(mGpuList.get(position).getModel());
//            holder.vRamSize.setText(Integer.toString(mGpuList.get(position).getGraphicsRamSize().intValue()) + "GB");
//            holder.vRamType.setText(mGpuList.get(position).getGraphicsRamType());
//            holder.date.setText(mGpuList.get(position).getReleaseDate().substring(mGpuList.get(position).getReleaseDate().lastIndexOf(" ")+1));
//            holder.scoreVFM.setText("100%");
//            holder.price.setText(String.valueOf(mGpuList.get(position).getPrice()) + " €");
//            holder.fps1080.setText(Double.toString(mGpuList.get(position).getAvgFps1080p()));
//            holder.fps2k.setText(Double.toString(mGpuList.get(position).getAvgFps2k()));
//            holder.fps4k.setText(Double.toString(mGpuList.get(position).getAvgFps4k()));
//            holder.scoreFirestrike.setText(String.valueOf((int) mGpuList.get(position).getFirestrike()));
//            holder.scorePassmark.setText(String.valueOf(((int) mGpuList.get(position).getPassmark())));
//
//            holder.titlePrice.setText("Price");
//            holder.titleVFM.setText("Value for money");
//            holder.title1080.setText("1080p (FPS)");
//            holder.title2k.setText("2k (FPS)");
//            holder.title4k.setText("4k (FPS)");
//            holder.titleFirestrike.setText("Firestrike Score");
//            holder.titlePassmark.setText("Passmark Score");
//
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Context context = getContext();
//                    CharSequence text = "Hello toast!";
//                    int duration = Toast.LENGTH_SHORT;
//
//                    Toast toast = Toast.makeText(context, text, duration);
//                    toast.show();
//                }
//            });
//
//        }
//
//        // Return the size of your dataset (invoked by the layout manager)
//        @Override
//        public int getItemCount() {
//            return mGpuList.size();
//        }
//
//        // Provide a reference to the views for each data item
//        // Complex data items may need more than one view per item, and
//        // you provide access to all the views for a data item in a view holder
//        public class ViewHolder extends RecyclerView.ViewHolder {
//            // each data item is just a string in this case
//            public TextView titleGpuModel;
//            public TextView vRamSize;
//            public TextView vRamType;
//            public TextView date;
//            public ImageView companyImage;
//            //        public Button textViewTag1;
////        public Button textViewTag2;
//            public TextView scoreVFM;
//            public TextView titleVFM;
//            public TextView price;
//            public TextView titlePrice;
//            public ConstraintLayout expandableView;
//            public ImageButton expandButton;
//            public TextView fps1080;
//            public TextView title1080;
//            public TextView fps2k;
//            public TextView title2k;
//            public TextView fps4k;
//            public TextView title4k;
//            public TextView scoreFirestrike;
//            public TextView titleFirestrike;
//            public TextView scorePassmark;
//            public TextView titlePassmark;
//
//            public ViewHolder(View itemView) {
//                super(itemView);
//                this.titleGpuModel = itemView.findViewById(R.id.model);
//                this.vRamSize = itemView.findViewById(R.id.vRamSize);
//                this.vRamType = itemView.findViewById(R.id.vRamType);
//                this.date = itemView.findViewById(R.id.date);
//                this.companyImage = itemView.findViewById(R.id.image);
//                this.scoreVFM = itemView.findViewById(R.id.expandable_view).findViewById(R.id.scoreVFM).findViewById(R.id.score_value);
//                this.titleVFM = itemView.findViewById(R.id.expandable_view).findViewById(R.id.scoreVFM).findViewById(R.id.score_title);
//                this.price = itemView.findViewById(R.id.expandable_view).findViewById(R.id.price).findViewById(R.id.score_value);
//                this.titlePrice = itemView.findViewById(R.id.expandable_view).findViewById(R.id.price).findViewById(R.id.score_title);
////                this.expandableView = itemView.findViewById(R.id.expandable_view);
////                this.expandButton = itemView.findViewById(R.id.expand_button);
//                this.fps1080 = itemView.findViewById(R.id.expandable_view).findViewById(R.id.tag_1080).findViewById(R.id.score_value);
//                this.title1080 = itemView.findViewById(R.id.expandable_view).findViewById(R.id.tag_1080).findViewById(R.id.score_title);
//                this.fps2k = itemView.findViewById(R.id.expandable_view).findViewById(R.id.tag_2k).findViewById(R.id.score_value);
//                this.title2k = itemView.findViewById(R.id.expandable_view).findViewById(R.id.tag_2k).findViewById(R.id.score_title);
//                this.fps4k = itemView.findViewById(R.id.expandable_view).findViewById(R.id.tag_4k).findViewById(R.id.score_value);
//                this.title4k = itemView.findViewById(R.id.expandable_view).findViewById(R.id.tag_4k).findViewById(R.id.score_title);
//                this.scoreFirestrike = itemView.findViewById(R.id.expandable_view).findViewById(R.id.tag_firestrike).findViewById(R.id.score_value);
//                this.titleFirestrike = itemView.findViewById(R.id.expandable_view).findViewById(R.id.tag_firestrike).findViewById(R.id.score_title);
//                this.scorePassmark = itemView.findViewById(R.id.expandable_view).findViewById(R.id.tag_passmark).findViewById(R.id.score_value);
//                this.titlePassmark = itemView.findViewById(R.id.expandable_view).findViewById(R.id.tag_passmark).findViewById(R.id.score_title);
//
////                expandButton.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        if (expandableView.getVisibility() == View.VISIBLE) {
////                            expandButton.setImageResource(R.drawable.ic_expand_less_black_36dp);
////                            expandableView.setVisibility(View.GONE);
////                        } else {
////                            expandButton.setImageResource(R.drawable.ic_expand_more_black_36dp);
////                            expandableView.setVisibility(View.VISIBLE);
////                        }
////                    }
////                });
//            }
//        }
//    }


    public class GpuRankingAdapter extends RecyclerView.Adapter<GpuRankingAdapter.ViewHolder> {
        private List<Gpu> mGpuList;


        // Provide a suitable constructor (depends on the kind of dataset)
        public GpuRankingAdapter(List<Gpu> data) {
            this.mGpuList = data;
        }

        public void setList(List<Gpu> gpuList) {
            mGpuList = checkNotNull(gpuList);
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_company_logo,
                    parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.titleGpuModel.setText(mGpuList.get(position).getModel());

            if(mGpuList.get(position).getModel().contains("Radeon")) {
                holder.companyImage.setImageResource(R.drawable.ic_amd);
                ImageViewCompat.setImageTintList(holder.companyImage, ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.amd_standard)));
            } else {
                ImageViewCompat.setImageTintList(holder.companyImage, ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.nvidia_standard)));
            }

            holder.vRamSize.setText(NumberFormat.getInstance().format(mGpuList.get(position).getGraphicsRamSize().intValue()) + "GB");
            holder.vRamType.setText(mGpuList.get(position).getGraphicsRamType());
            holder.date.setText(mGpuList.get(position).getReleaseDate().substring(mGpuList.get(position).getReleaseDate().lastIndexOf(" ")+1));
            holder.scoreVFM.setText(NumberFormat.getPercentInstance().format((mGpuList.get(position).getScore())));
            holder.price.setText(String.valueOf(mGpuList.get(position).getPrice()) + " €");
            holder.fps1080.setText(NumberFormat.getInstance().format(mGpuList.get(position).getAvgFps1080p()));
            holder.fps2k.setText(NumberFormat.getInstance().format(mGpuList.get(position).getAvgFps2k()));
            holder.fps4k.setText(NumberFormat.getInstance().format(mGpuList.get(position).getAvgFps4k()));
            holder.scoreFirestrike.setText(String.valueOf((int) mGpuList.get(position).getFirestrike()));
            holder.scorePassmark.setText(String.valueOf(((int) mGpuList.get(position).getPassmark())));

            holder.titlePrice.setText(R.string.sort_price);
            holder.titleVFM.setText(R.string.sort_vfm);
            holder.title1080.setText(R.string.sort_1080p);
            holder.title2k.setText(R.string.sort_2k);
            holder.title4k.setText(R.string.sort_4k);
            holder.titleFirestrike.setText(R.string.score_firestrike);
            holder.titlePassmark.setText(R.string.score_passmark);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = getContext();
                    CharSequence text = "Hello toast!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            });

            holder.itemPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext(), R.style.dialogTheme);
                    builder.setTitle(getString(R.string.dialog_price_title));

// Set up the input
                    //final EditText input = new EditText(getContext());
                    //input.setInputType(InputType.TYPE_CLASS_NUMBER);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    View dialogView = getLayoutInflater().inflate(R.layout.dialog_input_price, null);
                    builder.setView(dialogView);
                    final TextInputEditText input = dialogView.findViewById(R.id.editText);
                    input.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

// Set up the buttons
                    builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String price = input.getText().toString();
                            mGpuList.get(position).setPrice(Double.valueOf(price));
                            mGpuList.get(position).setScore((mGpuList.get(position).getAvgFps1080p() + mGpuList.get(position).getAvgFps2k() + mGpuList.get(position).getAvgFps4k()) /mGpuList.get(position).getPrice());
                            notifyGpuListChanged(mGpuList);
                            mPresenter.updatePrice(mGpuList.get(position).getKey(), Double.valueOf(price));
                        }
                    });
                    builder.setNegativeButton(getString(R.string.dismiss), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
            });

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mGpuList.size();
        }

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView titleGpuModel;
            public TextView vRamSize;
            public TextView vRamType;
            public TextView date;
            public ImageView companyImage;
            //        public Button textViewTag1;
//        public Button textViewTag2;
            public TextView scoreVFM;
            public TextView titleVFM;
            public ConstraintLayout itemPrice;
            public TextView price;
            public TextView titlePrice;
            public ConstraintLayout cardContainer;
            public ConstraintLayout expandableView;
            public ImageButton expandButton;
            public RelativeLayout item1080;
            public TextView fps1080;
            public TextView title1080;
            public RelativeLayout item2k;
            public TextView fps2k;
            public TextView title2k;
            public RelativeLayout item4k;
            public TextView fps4k;
            public TextView title4k;
            public RelativeLayout itemFirestrike;
            public TextView scoreFirestrike;
            public TextView titleFirestrike;
            public RelativeLayout itemPassmark;
            public TextView scorePassmark;
            public TextView titlePassmark;

            public int height;

            public ViewHolder(View itemView) {
                super(itemView);
                this.titleGpuModel = itemView.findViewById(R.id.model);
                this.vRamSize = itemView.findViewById(R.id.vRamSize);
                this.vRamType = itemView.findViewById(R.id.vRamType);
                this.date = itemView.findViewById(R.id.date);
                this.companyImage = itemView.findViewById(R.id.image);
                this.scoreVFM = itemView.findViewById(R.id.scoreVFM).findViewById(R.id.score_value);
                this.titleVFM = itemView.findViewById(R.id.scoreVFM).findViewById(R.id.score_title);
                this.itemPrice = itemView.findViewById(R.id.price);
                this.price = itemView.findViewById(R.id.price).findViewById(R.id.score_value);
                this.titlePrice = itemView.findViewById(R.id.price).findViewById(R.id.score_title);
//                this.expandableView = itemView.findViewById(R.id.expandable_view);
//                this.cardContainer = itemView.findViewById(R.id.cardContainer);
//                this.expandButton = itemView.findViewById(R.id.expand_button);
                this.item1080 = itemView.findViewById(R.id.tag_1080);
                this.fps1080 = itemView.findViewById(R.id.tag_1080).findViewById(R.id.score_value);
                this.title1080 = itemView.findViewById(R.id.tag_1080).findViewById(R.id.score_title);

                this.item2k = itemView.findViewById(R.id.tag_2k);
                this.fps2k = itemView.findViewById(R.id.tag_2k).findViewById(R.id.score_value);
                this.title2k = itemView.findViewById(R.id.tag_2k).findViewById(R.id.score_title);

                this.item4k = itemView.findViewById(R.id.tag_4k);
                this.fps4k = itemView.findViewById(R.id.tag_4k).findViewById(R.id.score_value);
                this.title4k = itemView.findViewById(R.id.tag_4k).findViewById(R.id.score_title);

                this.itemFirestrike = itemView.findViewById(R.id.tag_firestrike);
                this.scoreFirestrike = itemView.findViewById(R.id.tag_firestrike).findViewById(R.id.score_value);
                this.titleFirestrike = itemView.findViewById(R.id.tag_firestrike).findViewById(R.id.score_title);

                this.itemPassmark = itemView.findViewById(R.id.tag_passmark);
                this.scorePassmark = itemView.findViewById(R.id.tag_passmark).findViewById(R.id.score_value);
                this.titlePassmark = itemView.findViewById(R.id.tag_passmark).findViewById(R.id.score_title);


//                expandableView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//                        expandableView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//
//                        expandButton.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                                if( expandableView.getVisibility() == View.VISIBLE) {
//                                    TransitionManager.beginDelayedTransition(expandableView);
//                                    expandableView.setVisibility(View.GONE);
//                                } else {
//                                    TransitionManager.beginDelayedTransition(expandableView);
//                                    expandableView.setVisibility(View.VISIBLE);
//
//                                }
//                            }
//                        });
//
//                        expandableView.setVisibility(View.GONE);
//                    }
//                });


//                expandButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        TransitionManager.beginDelayedTransition(item1080);
//                        TransitionManager.beginDelayedTransition(itemFirestrike);
//                        TransitionManager.beginDelayedTransition(item2k);
//                        TransitionManager.beginDelayedTransition(itemPassmark);
//                        TransitionManager.beginDelayedTransition(item4k);
//
//
//                        if( item1080.getVisibility() == View.VISIBLE) {
//
//                            item4k.setVisibility(View.GONE);
//
//                            itemPassmark.setVisibility(View.GONE);
//                            item2k.setVisibility(View.GONE);
//                            itemFirestrike.setVisibility(View.GONE);
//                            item1080.setVisibility(View.GONE);
//
//                        } else {
//                            item4k.setVisibility(View.VISIBLE);
//                            itemPassmark.setVisibility(View.VISIBLE);
//                            item2k.setVisibility(View.VISIBLE);
//                            itemFirestrike.setVisibility(View.VISIBLE);
//                            item1080.setVisibility(View.VISIBLE);
//
//                        }
//                    }
//                });

//                expandButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        if( expandableView.getVisibility() == View.VISIBLE) {
//                            TransitionManager.beginDelayedTransition(expandableView);
//                            expandableView.setVisibility(View.GONE);
//                        } else {
//                            TransitionManager.beginDelayedTransition(expandableView);
//                            expandableView.setVisibility(View.VISIBLE);
//
//                        }
//                    }
//                });

//                expandButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        toggleSectionText(expandButton);
//                    }
//                });
//
//                expandableView.setVisibility(View.GONE);
//
//
//            }



//            private void toggleSectionText(View view) {
//                boolean show = toggleArrow(view);
////                if(height < expandableView.getMeasuredHeight()) {
////                    height = expandableView.getMeasuredHeight();
////                }
//
//                if (show) {
//                    ViewAnimation.expand(expandableView);
//                } else {
//                    ViewAnimation.collapse(expandableView);
//                }
//            }
//
//            public boolean toggleArrow(View view) {
//                if (view.getRotation() == 0) {
//                    view.animate().setDuration(200).rotation(180);
//                    return true;
//                } else {
//                    view.animate().setDuration(200).rotation(0);
//                    return false;
//                }
            }
        }
    }
}
