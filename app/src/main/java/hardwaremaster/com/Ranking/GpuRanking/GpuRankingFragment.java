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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

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
    private View root;
    MenuItem menuItem;
    SearchView searchView;
    OnBottomDialogFilterFragmentListener mListener;

    Trace myTrace = FirebasePerformance.getInstance().newTrace("GpuRankingFragment: OnCreate -> OnCreateView");

    @Inject
    public GpuRankingFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new GpuRankingAdapter(new ArrayList<Gpu>(0));
        myTrace.start();
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
        mRecyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setItemPrefetchEnabled(true);
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

        myTrace.stop();

        return root;
    }

    /* CpuRankingContract.View callbacks*/
    @Override
    public void notifyGpuListChanged(List<Gpu> gpuList) {

//        if(gpuList != null) {
//            mListAdapter.setList(gpuList);
//            mListAdapter.notifyDataSetChanged();
//        }

        List<Gpu> oldNews = mListAdapter.getList();
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new MyDiffCallback(oldNews, gpuList));
        mListAdapter.setList(gpuList);
        result.dispatchUpdatesTo(mListAdapter);
    }

    public class MyDiffCallback extends DiffUtil.Callback{

        List<Gpu> oldPersons;
        List<Gpu> newPersons;

        public MyDiffCallback(List<Gpu> newPersons, List<Gpu> oldPersons) {
            this.newPersons = newPersons;
            this.oldPersons = oldPersons;
        }

        @Override
        public int getOldListSize() {
            return oldPersons.size();
        }

        @Override
        public int getNewListSize() {
            return newPersons.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldPersons.get(oldItemPosition).getModel() == newPersons.get(newItemPosition).getModel();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldPersons.get(oldItemPosition).equals(newPersons.get(newItemPosition));
        }

        @Nullable
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            //you can return particular field for changed item.
            return super.getChangePayload(oldItemPosition, newItemPosition);
        }
    }

    @Override
    public void showHideFiltersView() {
    }

    @Override
    public void setPriceBarMinMaxValues(double min, double max) {
    }

    @Override
    public void setPriceBarSelectedMinMaxValues(double min, double max) {
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


    public interface OnBottomDialogFilterFragmentListener {
        void OnApplyGpuFilterClicked();
    }

    public class GpuRankingAdapter extends RecyclerView.Adapter<GpuRankingAdapter.ViewHolder> {
        private List<Gpu> mGpuList;


        // Provide a suitable constructor (depends on the kind of dataset)
        public GpuRankingAdapter(List<Gpu> data) {
            this.mGpuList = data;
        }

        public void setList(List<Gpu> gpuList) {
            mGpuList = checkNotNull(gpuList);
        }

        public List<Gpu> getList() {
            return mGpuList;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_gpu,
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
                holder.companyImage.setImageResource(R.drawable.ic_nvidia);
                ImageViewCompat.setImageTintList(holder.companyImage, ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.nvidia_standard)));
            }

            //holder.imageVFM.setImageResource(R.drawable.ic_flash);

            holder.vRamSize.setText(NumberFormat.getInstance().format(mGpuList.get(position).getGraphicsRamSize().intValue()) + " GB");
            holder.vRamType.setText(mGpuList.get(position).getGraphicsRamType());
            holder.date.setText(mGpuList.get(position).getReleaseDate().substring(mGpuList.get(position).getReleaseDate().lastIndexOf(" ")+1));
            holder.scoreVFM.setText(NumberFormat.getPercentInstance().format((mGpuList.get(position).getScore())));
            holder.price.setText(String.valueOf(mGpuList.get(position).getPrice()) + " €");
            holder.fps1080.setText(NumberFormat.getInstance().format(mGpuList.get(position).getAvgFps1080p()));
            holder.fps2k.setText(NumberFormat.getInstance().format(mGpuList.get(position).getAvgFps2k()));
            holder.fps4k.setText(NumberFormat.getInstance().format(mGpuList.get(position).getAvgFps4k()));
            holder.scoreFirestrike.setText(String.valueOf((int) mGpuList.get(position).getFirestrike()));
            holder.scorePassmark.setText(String.valueOf(((int) mGpuList.get(position).getPassmark())));

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

            holder.price.setOnClickListener(new View.OnClickListener() {
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
            public ImageView companyImage;
            public TextView titleGpuModel;

            public TextView vRamSize;
            public TextView vRamType;
            public TextView date;

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
                this.titleGpuModel = itemView.findViewById(R.id.model);
                this.vRamSize = itemView.findViewById(R.id.vRamSize);
                this.vRamType = itemView.findViewById(R.id.vRamType);
                this.date = itemView.findViewById(R.id.date);
                this.companyImage = itemView.findViewById(R.id.image);
                this.scoreVFM = itemView.findViewById(R.id.scoreVFM);
                this.price = itemView.findViewById(R.id.price);
                this.fps1080 = itemView.findViewById(R.id.tag_1080).findViewById(R.id.score_value);
                this.fps2k = itemView.findViewById(R.id.tag_2k).findViewById(R.id.score_value);
                this.fps4k = itemView.findViewById(R.id.tag_4k).findViewById(R.id.score_value);
                this.scoreFirestrike = itemView.findViewById(R.id.tag_firestrike).findViewById(R.id.score_value);
                this.scorePassmark = itemView.findViewById(R.id.tag_passmark).findViewById(R.id.score_value);
            }
        }
    }
}
