package hardwaremaster.com.Ranking.GpuRanking;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TimingLogger;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen;
import com.ethanhua.skeleton.Skeleton;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import hardwaremaster.com.R;
import hardwaremaster.com.Ranking.GpuRankingActivity;
import hardwaremaster.com.data.Cpu;
import hardwaremaster.com.data.Gpu;
import hardwaremaster.com.di.ActivityScoped;
import hardwaremaster.com.widgets.RangeSeekBar;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

@ActivityScoped
public class GpuRankingFragmentFirebase extends Fragment implements GpuRankingContract.View {

    @Inject
    GpuRankingContract.Presenter mPresenter;
    @NonNull
    protected static final Query sGetAllGpuQuery =
            FirebaseDatabase.getInstance().getReference("gpu");
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    ImageView closeButton;
    private View root;
    MenuItem menuItem;
    SearchView searchView;

    @Inject
    public GpuRankingFragmentFirebase() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        ButterKnife.bind(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        long startTime = System.currentTimeMillis();
        attachRecyclerViewAdapter();
        long endTime = System.currentTimeMillis();
        Log.d("Attach time: ", "That took " + (endTime - startTime) + " milliseconds");
    }

    private void attachRecyclerViewAdapter() {
        final RecyclerView.Adapter adapter = newAdapter();

        // Scroll to bottom on new messages
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                //mRecyclerView.smoothScrollToPosition(adapter.getItemCount());
            }
        });

        mRecyclerView.setAdapter(adapter);
    }

    @NonNull
    protected RecyclerView.Adapter newAdapter() {
        FirebaseRecyclerOptions<Gpu> options =
                new FirebaseRecyclerOptions.Builder<Gpu>()
                        .setQuery(sGetAllGpuQuery, Gpu.class)
                        .setLifecycleOwner(this)
                        .build();

        return new FirebaseRecyclerAdapter<Gpu, ViewHolder>(options) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_gpu_simple, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Gpu model) {
                holder.bind(model);
            }

            @Override
            public void onDataChanged() {
                // If there are no chat messages, show a view that invites the user to add a message.
                //mEmptyListMessage.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        };
    }

    /* CpuRankingContract.View callbacks*/
    @Override
    public void notifyGpuListChanged(List<Gpu> gpuList) {
    }

    @Override
    public void notifyCpuListChanged(List<Cpu> cpuList) {

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

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView companyImage;
        public MaterialTextView titleGpuModel;

        public MaterialTextView vRamSize;
        public MaterialTextView vRamType;
        public MaterialTextView date;

        public MaterialTextView scoreVFM;
        public MaterialTextView price;

        public TextView fps1080;
        public TextView fps2k;
        public TextView fps4k;
        public TextView scoreFirestrike;
        public TextView scorePassmark;

        public int height;

        public ViewHolder(View itemView) {
            super(itemView);
            this.companyImage = itemView.findViewById(R.id.image);
            this.titleGpuModel = itemView.findViewById(R.id.model);

            this.vRamSize = itemView.findViewById(R.id.vRamSize);
            this.vRamType = itemView.findViewById(R.id.vRamType);
            this.date = itemView.findViewById(R.id.date);


            this.scoreVFM = itemView.findViewById(R.id.scoreVFM);
            this.price = itemView.findViewById(R.id.price);

            this.fps1080 = itemView.findViewById(R.id.tag_1080).findViewById(R.id.score_value);
            this.fps2k = itemView.findViewById(R.id.tag_2k).findViewById(R.id.score_value);
            this.fps4k = itemView.findViewById(R.id.tag_4k).findViewById(R.id.score_value);
            this.scoreFirestrike = itemView.findViewById(R.id.tag_firestrike).findViewById(R.id.score_value);
            this.scorePassmark = itemView.findViewById(R.id.tag_passmark).findViewById(R.id.score_value);
        }

        public void bind(@NonNull Gpu gpu) {
            titleGpuModel.setText(gpu.getModel());

//            if(gpu.getModel().contains("Radeon")) {
//                companyImage.setImageResource(R.drawable.ic_amd);
//                ImageViewCompat.setImageTintList(companyImage, ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.amd_standard)));
//            } else {
//                companyImage.setImageResource(R.drawable.ic_nvidia);
//                ImageViewCompat.setImageTintList(companyImage, ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.nvidia_standard)));
//            }

            //holder.imageVFM.setImageResource(R.drawable.ic_flash);

            vRamSize.setText(NumberFormat.getInstance().format(gpu.getGraphicsRamSize().intValue()) + " GB");
            vRamType.setText(gpu.getGraphicsRamType());
            date.setText(gpu.getReleaseDate().substring(gpu.getReleaseDate().lastIndexOf(" ")+1));
            //scoreVFM.setText(NumberFormat.getPercentInstance().format((gpu.getScore())));
            price.setText(String.valueOf(gpu.getPrice()) + " €");
            fps1080.setText(NumberFormat.getInstance().format(gpu.getAvgFps1080p()));
            fps2k.setText(NumberFormat.getInstance().format(gpu.getAvgFps2k()));
            fps4k.setText(NumberFormat.getInstance().format(gpu.getAvgFps4k()));
            scoreFirestrike.setText(String.valueOf((int) gpu.getFirestrike()));
            scorePassmark.setText(String.valueOf(((int) gpu.getPassmark())));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = getContext();
                    CharSequence text = "Hello toast!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            });

            price.setOnClickListener(new View.OnClickListener() {
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
                            gpu.setPrice(Double.valueOf(price));
                            gpu.setScore((gpu.getAvgFps1080p() + gpu.getAvgFps2k() + gpu.getAvgFps4k()) /gpu.getPrice());
//                            notifyGpuListChanged(mGpuList);
//                            mPresenter.updatePrice(mGpuList.get(position).getKey(), Double.valueOf(price));
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
    }
}
