package sk.styk.martin.apkanalyzer.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.business.task.ItemDetailLoadTask;
import sk.styk.martin.apkanalyzer.model.AppBasicInfo;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link AppListActivity}
 * in two-pane mode (on tablets) or a {@link AppDetailActivity}
 * on handsets.
 */
public class AppDetailFragment extends Fragment implements ItemDetailLoadTask.Callback {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    private AppBasicInfo data;

    private ProgressBar loadingBar;
    private View dataContainer;
    private CollapsingToolbarLayout appBarLayout;
    private TextView textView;

    private ItemDetailLoadTask itemDetailLoadTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            Activity activity = this.getActivity();
            itemDetailLoadTask = new ItemDetailLoadTask(activity, this);
            itemDetailLoadTask.execute(getArguments().get(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(sk.styk.martin.apkanalyzer.R.layout.fragment_app_detail, container, false);

        loadingBar = (ProgressBar) rootView.findViewById(R.id.item_detail_loading);
        dataContainer = rootView.findViewById(R.id.item_detail_container);
        textView = (TextView) rootView.findViewById(R.id.item_detail);
        appBarLayout = (CollapsingToolbarLayout) getActivity().findViewById(sk.styk.martin.apkanalyzer.R.id.toolbar_layout);

        if (data != null) {
            onTaskCompleted(data);
        }
        return rootView;
    }

    public void onStop() {
        super.onStop();

        if (itemDetailLoadTask != null && itemDetailLoadTask.getStatus() == AsyncTask.Status.RUNNING)
            itemDetailLoadTask.cancel(true);
    }

    /**
     * Callback from async task
     */
    @Override
    public void onTaskCompleted(AppBasicInfo data) {
        this.data = data;
        if (appBarLayout != null) {
            appBarLayout.setTitle(data.getPackageName());
        }
        loadingBar.setVisibility(View.GONE);
        dataContainer.setVisibility(View.VISIBLE);
        textView.setText(data.toString());
    }

    @Override
    public void onTaskStart() {
    }
}
