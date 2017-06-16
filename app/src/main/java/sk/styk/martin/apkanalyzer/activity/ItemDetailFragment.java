package sk.styk.martin.apkanalyzer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sk.styk.martin.apkanalyzer.business.service.InstalledAppsService;
import sk.styk.martin.apkanalyzer.model.AppBasicInfo;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    private AppBasicInfo mItem;
    private InstalledAppsService installedAppsRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            Activity activity = this.getActivity();

            installedAppsRepository = new InstalledAppsService(activity);

            mItem = installedAppsRepository.getAll().get(getArguments().getInt(ARG_ITEM_ID));

            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(sk.styk.martin.apkanalyzer.R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getPackageName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(sk.styk.martin.apkanalyzer.R.layout.item_detail, container, false);

        if (mItem != null) {
            ((TextView) rootView.findViewById(sk.styk.martin.apkanalyzer.R.id.item_detail)).setText(mItem.toString());
        }

        return rootView;
    }
}
