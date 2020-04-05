package hardwaremaster.com.a_old.Ranking.GpuRanking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import hardwaremaster.com.R;
import hardwaremaster.com.a_old.data.Gpu;
import hardwaremaster.com.a_old.di.ActivityScoped;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

@ActivityScoped
public class GpuRankingFragment extends Fragment implements GpuRankingContract.View {

    @Inject
    GpuRankingContract.Presenter mPresenter;

    private GpuRankingAdapter mListAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private View root;

    @Inject
    public GpuRankingFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new GpuRankingAdapter(new ArrayList<>(0));
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
        mProgressBar = root.findViewById(R.id.progress_bar);
        mRecyclerView = root.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setItemPrefetchEnabled(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mListAdapter);

        setHasOptionsMenu(true);
        setupSearchView();

        return root;
    }

    /* Helpers */
    private void setupSearchView() {
        SearchView customSearchView = root.findViewById(R.id.customSearchView);
        customSearchView.onActionViewExpanded(); //new Added line
        customSearchView.setIconifiedByDefault(false);
        if(!customSearchView.isFocused()) {
            customSearchView.clearFocus();
        }
        // Catch event on [x] button inside search view
        ImageView closeButton = customSearchView.findViewById(R.id.search_close_btn);
        // Set on click listener
        closeButton.setOnClickListener(v -> {
            // Manage this event.
            customSearchView.setQuery("", false);
            customSearchView.clearFocus();
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
    }

    /* GpuRankingContract.View callbacks*/
    @Override
    public void notifyGpuListChanged(List<Gpu> gpuList) {
        if(gpuList != null) {
            mListAdapter.setList(gpuList);
            mListAdapter.notifyDataSetChanged();
            mRecyclerView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
//        <------ FUTURE OPTIMIZATION ------>
//        List<Gpu> oldNews = mListAdapter.getList();
//        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new MyDiffCallback(oldNews, gpuList));
//        mListAdapter.setList(gpuList);
//        result.dispatchUpdatesTo(mListAdapter);
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
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_gpu,
                    parent, false);
            return new ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.titleGpuModel.setText(mGpuList.get(position).getModel());

            if(mGpuList.get(position).getModel().contains("Radeon")) {
                holder.companyImage.setImageResource(R.drawable.ic_amd);
                ImageViewCompat.setImageTintList(holder.companyImage, ColorStateList.valueOf(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.amd_standard)));
            } else {
                holder.companyImage.setImageResource(R.drawable.ic_nvidia);
                ImageViewCompat.setImageTintList(holder.companyImage, ColorStateList.valueOf(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.nvidia_standard)));
            }

            holder.vRamSize.setText(NumberFormat.getInstance().format(mGpuList.get(position).getGraphicsRamSize().intValue()) + getString(R.string.append_GB));
            holder.vRamType.setText(mGpuList.get(position).getGraphicsRamType());
            holder.date.setText(mGpuList.get(position).getReleaseDate().substring(mGpuList.get(position).getReleaseDate().lastIndexOf(" ")+1));
            holder.scoreVFM.setText(NumberFormat.getPercentInstance().format((mGpuList.get(position).getScore())));
            holder.price.setText(String.valueOf(mGpuList.get(position).getPrice()) + " €");
            holder.fps1080.setText(NumberFormat.getInstance().format(mGpuList.get(position).getAvgFps1080p()));
            holder.fps2k.setText(NumberFormat.getInstance().format(mGpuList.get(position).getAvgFps2k()));
            holder.fps4k.setText(NumberFormat.getInstance().format(mGpuList.get(position).getAvgFps4k()));
            holder.scoreFirestrike.setText(String.valueOf((int) mGpuList.get(position).getFirestrike()));
            holder.scorePassmark.setText(String.valueOf(((int) mGpuList.get(position).getPassmark())));

            holder.itemView.setOnClickListener(v -> {
                //todo add gpu details
                Context context = getContext();
                CharSequence text = "Item clicked";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            });

            holder.price.setOnClickListener(v -> {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext(), R.style.dialogTheme);
                builder.setTitle(getString(R.string.dialog_price_title));

                @SuppressLint("InflateParams") View dialogView = getLayoutInflater().inflate(R.layout.dialog_input_price, null); //pass null cause it's a Dialog
                builder.setView(dialogView);
                final TextInputEditText input = dialogView.findViewById(R.id.editText);
                input.requestFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                builder.setPositiveButton(getString(R.string.confirm), (dialog, which) -> {
                    if(input.getText().toString().trim().length() > 0) {
                        String price = input.getText().toString();
                        mGpuList.get(position).setPrice(Double.valueOf(price));
                        mGpuList.get(position).setScore((mGpuList.get(position).getAvgFps1080p() + mGpuList.get(position).getAvgFps2k() + mGpuList.get(position).getAvgFps4k()) /mGpuList.get(position).getPrice());
                        notifyGpuListChanged(mGpuList);
                        mPresenter.updatePrice(mGpuList.get(position).getKey(), Double.valueOf(price));
                    }
                });
                builder.setNegativeButton(getString(R.string.dismiss), (dialog, which) -> {
                    Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    dialog.cancel();
                });

                builder.show();
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

            public TextView scoreVFM;
            public TextView price;

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
