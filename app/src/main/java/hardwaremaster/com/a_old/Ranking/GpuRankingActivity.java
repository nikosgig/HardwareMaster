package hardwaremaster.com.a_old.Ranking;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import hardwaremaster.com.R;
import hardwaremaster.com.a_old.data.Gpu;

/**
 * Class demonstrating how to setup a {@link RecyclerView} with an adapter while taking sign-in
 * states into consideration. Also demonstrates adding data to a ref and then reading it back using
 * the {@link FirebaseRecyclerAdapter} to build a simple chat app.
 * <p>
 * For a general intro to the RecyclerView, see <a href="https://developer.android.com/training/material/lists-cards.html">Creating
 * Lists</a>.
 */
public class GpuRankingActivity extends AppCompatActivity
        implements FirebaseAuth.AuthStateListener {
    private static final String TAG = "RealtimeDatabaseDemo";

    /**
     * Get the last 50 chat messages.
     */
    @NonNull
    protected static final Query sGetAllGpuQuery =
            FirebaseDatabase.getInstance().getReference("gpu");

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recyclerview);
        ButterKnife.bind(this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onStart() {
        super.onStart();
        if (isSignedIn()) { attachRecyclerViewAdapter(); }
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth auth) {

    }

    private boolean isSignedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
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
                        .inflate(R.layout.card_gpu, parent, false));
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

    protected void onAddMessage(@NonNull Gpu chat) {
        sGetAllGpuQuery.getRef().push().setValue(chat, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, DatabaseReference reference) {
                if (error != null) {
                    Log.e(TAG, "Failed to write message", error.toException());
                }
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

            if(gpu.getModel().contains("Radeon")) {
                companyImage.setImageResource(R.drawable.ic_amd);
                ImageViewCompat.setImageTintList(companyImage, ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.amd_standard)));
            } else {
                companyImage.setImageResource(R.drawable.ic_nvidia);
                ImageViewCompat.setImageTintList(companyImage, ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.nvidia_standard)));
            }

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
                    Context context = getApplicationContext();
                    CharSequence text = "Hello toast!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            });

            price.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getApplicationContext(), R.style.dialogTheme);
                    builder.setTitle(getString(R.string.dialog_price_title));

// Set up the input
                    //final EditText input = new EditText(getContext());
                    //input.setInputType(InputType.TYPE_CLASS_NUMBER);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    View dialogView = getLayoutInflater().inflate(R.layout.dialog_input_price, null);
                    builder.setView(dialogView);
                    final TextInputEditText input = dialogView.findViewById(R.id.editText);
                    input.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
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
                            getWindow().setSoftInputMode(
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
