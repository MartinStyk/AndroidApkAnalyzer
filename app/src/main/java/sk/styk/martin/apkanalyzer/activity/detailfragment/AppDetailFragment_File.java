package sk.styk.martin.apkanalyzer.activity.detailfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.activity.AppDetailFragment;
import sk.styk.martin.apkanalyzer.adapter.detaillist.SimpleStringListAdapter;
import sk.styk.martin.apkanalyzer.model.detail.FileData;
import sk.styk.martin.apkanalyzer.model.detail.FileEntry;

/**
 * Created by Martin Styk on 30.06.2017.
 */
public class AppDetailFragment_File extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_app_detail_simple_string_list, container, false);

        FileData data = getArguments().getParcelable(AppDetailFragment.ARG_CHILD);
        List<String> allFiles = mergeFileData(data);

        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view_simple_string_list);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        RecyclerView.Adapter adapter = new SimpleStringListAdapter(allFiles);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        return rootView;
    }

    private List<String> mergeFileData(FileData data) {
        if (data == null) {
            return new ArrayList<>(0);
        }

        List<String> allFiles = new ArrayList<>(data.getMenuHashes().size() +
                data.getDrawableHashes().size() +
                data.getLayoutHashes().size() +
                data.getOtherHashes().size());

        addInternal(allFiles, data.getDrawableHashes());
        addInternal(allFiles, data.getLayoutHashes());
        addInternal(allFiles, data.getMenuHashes());
        addInternal(allFiles, data.getOtherHashes());

        return  allFiles;
    }

    private void addInternal(List<String> allFiles, List<FileEntry> fileEntries) {
        for (FileEntry fileEntry : fileEntries) {
            allFiles.add(fileEntry.getPath());
        }
    }

}



