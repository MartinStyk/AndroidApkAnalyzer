package sk.styk.martin.apkanalyzer.activity.detailfragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.activity.AppDetailFragment
import sk.styk.martin.apkanalyzer.model.detail.ResourceData
import sk.styk.martin.apkanalyzer.view.DetailItemView

/**
 * @author Martin Styk
 * @version 03.07.2017.
 */
class AppDetailFragment_Resource : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_app_detail_resource, container, false)

        val data = arguments.getParcelable<ResourceData>(AppDetailFragment.ARG_CHILD)

        (rootView.findViewById<View>(R.id.item_all_drawables) as DetailItemView).valueText = data!!.drawables.toString()
        (rootView.findViewById<View>(R.id.item_different_drawables) as DetailItemView).valueText = data.differentDrawables.toString()
        (rootView.findViewById<View>(R.id.item_png_drawables) as DetailItemView).valueText = data.pngDrawables.toString()
        (rootView.findViewById<View>(R.id.item_nine_patch_drawables) as DetailItemView).valueText = data.ninePatchDrawables.toString()
        (rootView.findViewById<View>(R.id.item_jpg_drawables) as DetailItemView).valueText = data.jpgDrawables.toString()
        (rootView.findViewById<View>(R.id.item_gif_drawables) as DetailItemView).valueText = data.gifDrawables.toString()
        (rootView.findViewById<View>(R.id.item_xml_drawables) as DetailItemView).valueText = data.xmlDrawables.toString()

        (rootView.findViewById<View>(R.id.item_ldpi_drawables) as DetailItemView).valueText = data.ldpiDrawables.toString()
        (rootView.findViewById<View>(R.id.item_mdpi_drawables) as DetailItemView).valueText = data.mdpiDrawables.toString()
        (rootView.findViewById<View>(R.id.item_hdpi_drawables) as DetailItemView).valueText = data.hdpiDrawables.toString()
        (rootView.findViewById<View>(R.id.item_xhdpi_drawables) as DetailItemView).valueText = data.xhdpiDrawables.toString()
        (rootView.findViewById<View>(R.id.item_xxhdpi_drawables) as DetailItemView).valueText = data.xxhdpiDrawables.toString()
        (rootView.findViewById<View>(R.id.item_xxxhdpi_drawables) as DetailItemView).valueText = data.xxxhdpiDrawables.toString()
        (rootView.findViewById<View>(R.id.item_nodpi_drawables) as DetailItemView).valueText = data.nodpiDrawables.toString()
        (rootView.findViewById<View>(R.id.item_tvdpi_drawables) as DetailItemView).valueText = data.tvdpiDrawables.toString()

        (rootView.findViewById<View>(R.id.item_all_layouts) as DetailItemView).valueText = data.layouts.toString()
        (rootView.findViewById<View>(R.id.item_different_layouts) as DetailItemView).valueText = data.differentLayouts.toString()


        return rootView
    }
}
