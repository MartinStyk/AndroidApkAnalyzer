package sk.styk.martin.apkanalyzer.activity.detailfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.activity.AppDetailFragment;
import sk.styk.martin.apkanalyzer.model.detail.ResourceData;
import sk.styk.martin.apkanalyzer.view.DetailItemView;

/**
 * @author Martin Styk
 * @version 03.07.2017.
 */
public class AppDetailFragment_Resource extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_app_detail_resource, container, false);

        ResourceData data = getArguments().getParcelable(AppDetailFragment.ARG_CHILD);

        ((DetailItemView) rootView.findViewById(R.id.item_all_drawables)).setValue(String.valueOf(data.getDrawables()));
        ((DetailItemView) rootView.findViewById(R.id.item_different_drawables)).setValue(String.valueOf(data.getDifferentDrawables()));
        ((DetailItemView) rootView.findViewById(R.id.item_png_drawables)).setValue(String.valueOf(data.getPngDrawables()));
        ((DetailItemView) rootView.findViewById(R.id.item_nine_patch_drawables)).setValue(String.valueOf(data.getNinePatchDrawables()));
        ((DetailItemView) rootView.findViewById(R.id.item_jpg_drawables)).setValue(String.valueOf(data.getJpgDrawables()));
        ((DetailItemView) rootView.findViewById(R.id.item_gif_drawables)).setValue(String.valueOf(data.getGifDrawables()));
        ((DetailItemView) rootView.findViewById(R.id.item_xml_drawables)).setValue(String.valueOf(data.getXmlDrawables()));

        ((DetailItemView) rootView.findViewById(R.id.item_ldpi_drawables)).setValue(String.valueOf(data.getLdpiDrawables()));
        ((DetailItemView) rootView.findViewById(R.id.item_mdpi_drawables)).setValue(String.valueOf(data.getMdpiDrawables()));
        ((DetailItemView) rootView.findViewById(R.id.item_hdpi_drawables)).setValue(String.valueOf(data.getHdpiDrawables()));
        ((DetailItemView) rootView.findViewById(R.id.item_xhdpi_drawables)).setValue(String.valueOf(data.getXhdpiDrawables()));
        ((DetailItemView) rootView.findViewById(R.id.item_xxhdpi_drawables)).setValue(String.valueOf(data.getXxhdpiDrawables()));
        ((DetailItemView) rootView.findViewById(R.id.item_xxxhdpi_drawables)).setValue(String.valueOf(data.getXxxhdpiDrawables()));
        ((DetailItemView) rootView.findViewById(R.id.item_nodpi_drawables)).setValue(String.valueOf(data.getNodpiDrawables()));
        ((DetailItemView) rootView.findViewById(R.id.item_tvdpi_drawables)).setValue(String.valueOf(data.getTvdpiDrawables()));

        ((DetailItemView) rootView.findViewById(R.id.item_all_layouts)).setValue(String.valueOf(data.getLayouts()));
        ((DetailItemView) rootView.findViewById(R.id.item_different_layouts)).setValue(String.valueOf(data.getDifferentLayouts()));


        return rootView;
    }
}
