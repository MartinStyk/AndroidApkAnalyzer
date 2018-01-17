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

        ((DetailItemView) rootView.findViewById(R.id.item_all_drawables)).setValueText(String.valueOf(data.getDrawables()));
        ((DetailItemView) rootView.findViewById(R.id.item_different_drawables)).setValueText(String.valueOf(data.getDifferentDrawables()));
        ((DetailItemView) rootView.findViewById(R.id.item_png_drawables)).setValueText(String.valueOf(data.getPngDrawables()));
        ((DetailItemView) rootView.findViewById(R.id.item_nine_patch_drawables)).setValueText(String.valueOf(data.getNinePatchDrawables()));
        ((DetailItemView) rootView.findViewById(R.id.item_jpg_drawables)).setValueText(String.valueOf(data.getJpgDrawables()));
        ((DetailItemView) rootView.findViewById(R.id.item_gif_drawables)).setValueText(String.valueOf(data.getGifDrawables()));
        ((DetailItemView) rootView.findViewById(R.id.item_xml_drawables)).setValueText(String.valueOf(data.getXmlDrawables()));

        ((DetailItemView) rootView.findViewById(R.id.item_ldpi_drawables)).setValueText(String.valueOf(data.getLdpiDrawables()));
        ((DetailItemView) rootView.findViewById(R.id.item_mdpi_drawables)).setValueText(String.valueOf(data.getMdpiDrawables()));
        ((DetailItemView) rootView.findViewById(R.id.item_hdpi_drawables)).setValueText(String.valueOf(data.getHdpiDrawables()));
        ((DetailItemView) rootView.findViewById(R.id.item_xhdpi_drawables)).setValueText(String.valueOf(data.getXhdpiDrawables()));
        ((DetailItemView) rootView.findViewById(R.id.item_xxhdpi_drawables)).setValueText(String.valueOf(data.getXxhdpiDrawables()));
        ((DetailItemView) rootView.findViewById(R.id.item_xxxhdpi_drawables)).setValueText(String.valueOf(data.getXxxhdpiDrawables()));
        ((DetailItemView) rootView.findViewById(R.id.item_nodpi_drawables)).setValueText(String.valueOf(data.getNodpiDrawables()));
        ((DetailItemView) rootView.findViewById(R.id.item_tvdpi_drawables)).setValueText(String.valueOf(data.getTvdpiDrawables()));

        ((DetailItemView) rootView.findViewById(R.id.item_all_layouts)).setValueText(String.valueOf(data.getLayouts()));
        ((DetailItemView) rootView.findViewById(R.id.item_different_layouts)).setValueText(String.valueOf(data.getDifferentLayouts()));


        return rootView;
    }
}
