package sk.styk.martin.apkanalyzer.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.business.task.ItemListLoadTask;
import sk.styk.martin.apkanalyzer.model.AppBasicInfo;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity implements ItemListLoadTask.Callback {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private sk.styk.martin.apkanalyzer.activity.SimpleItemRecyclerViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(sk.styk.martin.apkanalyzer.R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.item_list);
        adapter = new sk.styk.martin.apkanalyzer.activity.SimpleItemRecyclerViewAdapter(this, new ArrayList<AppBasicInfo>());
        recyclerView.setAdapter(adapter);


        new ItemListLoadTask(this).execute();

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    @Override
    public void onTaskCompleted(List<AppBasicInfo> list) {
        adapter.dataChange(list);
    }

    @Override
    public void onTaskStart() {
    }

//
//    public class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {
//
//        private final List<ApplicationInfo> mValues;
//
//        public SimpleItemRecyclerViewAdapter(List<ApplicationInfo> items) {
//            mValues = items;
//        }
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_content, parent, false);
//            return new ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(final ViewHolder holder, final int position) {
//            holder.mItem = mValues.get(position);
//            holder.mPackageName.setText(mValues.get(position).packageName);
//
//            holder.mView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mTwoPane) {
//                        Bundle arguments = new Bundle();
//                        arguments.putInt(ItemDetailFragment.ARG_ITEM_ID, position);
//                        ItemDetailFragment fragment = new ItemDetailFragment();
//                        fragment.setArguments(arguments);
//                        getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();
//                    } else {
//                        Context context = v.getContext();
//                        Intent intent = new Intent(context, ItemDetailActivity.class);
//                        intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, position);
//
//                        context.startActivity(intent);
//                    }
//                }
//            });
//        }
//
//        @Override
//        public int getItemCount() {
//            return mValues.size();
//        }
//
//        public class ViewHolder extends RecyclerView.ViewHolder {
//            public final View mView;
//            public final TextView mPackageName;
//            public ApplicationInfo mItem;
//
//            public ViewHolder(View view) {
//                super(view);
//                mView = view;
//                mPackageName = (TextView) view.findViewById(R.id.package_name);
//            }
//
//            @Override
//            public String toString() {
//                return super.toString() + " '" + mPackageName.getText() + "'";
//            }
//        }
//    }
}
