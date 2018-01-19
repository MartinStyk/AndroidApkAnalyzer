package sk.styk.martin.apkanalyzer.activity.detailfragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_app_detail_resource.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.activity.AppDetailFragment
import sk.styk.martin.apkanalyzer.model.detail.ResourceData

/**
 * @author Martin Styk
 * @version 03.07.2017.
 */
class AppDetailFragment_Resource : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_app_detail_resource, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val data = arguments.getParcelable<ResourceData>(AppDetailFragment.ARG_CHILD)
                ?: throw IllegalArgumentException("data null")

        item_all_drawables.valueText = data.drawables.toString()
        item_different_drawables.valueText = data.differentDrawables.toString()
        item_png_drawables.valueText = data.pngDrawables.toString()
        item_nine_patch_drawables.valueText = data.ninePatchDrawables.toString()
        item_jpg_drawables.valueText = data.jpgDrawables.toString()
        item_gif_drawables.valueText = data.gifDrawables.toString()
        item_xml_drawables.valueText = data.xmlDrawables.toString()

        item_ldpi_drawables.valueText = data.ldpiDrawables.toString()
        item_mdpi_drawables.valueText = data.mdpiDrawables.toString()
        item_hdpi_drawables.valueText = data.hdpiDrawables.toString()
        item_xhdpi_drawables.valueText = data.xhdpiDrawables.toString()
        item_xxhdpi_drawables.valueText = data.xxhdpiDrawables.toString()
        item_xxxhdpi_drawables.valueText = data.xxxhdpiDrawables.toString()
        item_nodpi_drawables.valueText = data.nodpiDrawables.toString()
        item_tvdpi_drawables.valueText = data.tvdpiDrawables.toString()

        item_all_layouts.valueText = data.layouts.toString()
        item_different_layouts.valueText = data.differentLayouts.toString()
    }
}
