package sk.styk.martin.apkanalyzer.activity.detailfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import java.util.ArrayList;
import java.util.List;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.activity.AppDetailFragment;
import sk.styk.martin.apkanalyzer.adapter.FileListAdapter;
import sk.styk.martin.apkanalyzer.model.FileData;
import sk.styk.martin.apkanalyzer.util.EndlessRecyclerViewOnScrollListener;

/**
 * Created by Martin Styk on 30.06.2017.
 */
public class AppDetailFragment_File extends Fragment {

    private WebView textView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_app_detail_files, container, false);
        FileData data = getArguments().getParcelable(AppDetailFragment.ARG_CHILD);


        RecyclerView rvItems = (RecyclerView) rootView.findViewById(R.id.files_recycler_view);
        final List<String> allContacts = new ArrayList<String>(data.getAllHashes().keySet());
        final FileListAdapter adapter = new FileListAdapter(allContacts.subList(0, 50));
        rvItems.setAdapter(adapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvItems.setLayoutManager(linearLayoutManager);
        rvItems.addOnScrollListener(new EndlessRecyclerViewOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                List<String> moreContacts = allContacts.subList(totalItemsCount, totalItemsCount + 50);
                int curSize = adapter.getItemCount();
                adapter.appendItems(moreContacts);
                adapter.notifyItemRangeInserted(curSize, adapter.getItemCount() - 1);
            }
        });
        return rootView;
    }

}



