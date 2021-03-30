package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.resource

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.google.android.material.snackbar.Snackbar
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.manager.clipboard.ClipBoardManager
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.adapters.DetailInfoAdapter
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.base.AppDetailFragmentViewModel
import sk.styk.martin.apkanalyzer.util.TextInfo
import sk.styk.martin.apkanalyzer.util.components.DialogComponent
import sk.styk.martin.apkanalyzer.util.components.SnackBarComponent

class AppResourceDetailsFragmentViewModel @AssistedInject constructor(
        @Assisted private val appDetailFragmentViewModel: AppDetailFragmentViewModel,
        val detailInfoAdapter: DetailInfoAdapter,
        private val clipBoardManager: ClipBoardManager,
) : ViewModel(), DefaultLifecycleObserver {

    val openDescription = detailInfoAdapter.openDescription
            .map {
                DialogComponent(it.name, it.description, TextInfo.from(R.string.close))
            }

    val showSnackbar = detailInfoAdapter.copyToClipboard
            .map {
                clipBoardManager.copyToClipBoard(it.value, it.name)
                SnackBarComponent(TextInfo.from(R.string.copied_to_clipboard), Snackbar.LENGTH_SHORT)
            }

    private val appDetailsObserver = Observer<AppDetailData> {
        val data = it.resourceData
        detailInfoAdapter.info = listOfNotNull(
                DetailInfoAdapter.DetailInfo(
                        TextInfo.from(R.string.all_drawables),
                        TextInfo.from(data.drawables.toString()),
                        TextInfo.from(R.string.all_drawables_description),
                ),
                DetailInfoAdapter.DetailInfo(
                        TextInfo.from(R.string.different_drawables),
                        TextInfo.from(data.differentDrawables.toString()),
                        TextInfo.from(R.string.different_drawables_description),
                ),
                DetailInfoAdapter.DetailInfo(
                        TextInfo.from(R.string.png_drawables),
                        TextInfo.from(data.pngDrawables.toString()),
                        TextInfo.from(R.string.png_drawables_description),
                ),
                DetailInfoAdapter.DetailInfo(
                        TextInfo.from(R.string.nine_patch_drawables),
                        TextInfo.from(data.ninePatchDrawables.toString()),
                        TextInfo.from(R.string.nine_patch_drawables_description),
                ),
                DetailInfoAdapter.DetailInfo(
                        TextInfo.from(R.string.jpg_drawables),
                        TextInfo.from(data.jpgDrawables.toString()),
                        TextInfo.from(R.string.jpg_drawables_description),
                ),
                DetailInfoAdapter.DetailInfo(
                        TextInfo.from(R.string.gif_drawables),
                        TextInfo.from(data.gifDrawables.toString()),
                        TextInfo.from(R.string.gif_drawables_description),
                ),
                DetailInfoAdapter.DetailInfo(
                        TextInfo.from(R.string.xml_drawables),
                        TextInfo.from(data.xmlDrawables.toString()),
                        TextInfo.from(R.string.xml_drawables_description),
                ),
                DetailInfoAdapter.DetailInfo(
                        TextInfo.from(R.string.ldpi_drawables),
                        TextInfo.from(data.ldpiDrawables.toString()),
                        TextInfo.from(R.string.ldpi_drawables),
                ),
                DetailInfoAdapter.DetailInfo(
                        TextInfo.from(R.string.mdpi_drawables),
                        TextInfo.from(data.mdpiDrawables.toString()),
                        TextInfo.from(R.string.mdpi_drawables_description),
                ),
                DetailInfoAdapter.DetailInfo(
                        TextInfo.from(R.string.hdpi_drawables),
                        TextInfo.from(data.hdpiDrawables.toString()),
                        TextInfo.from(R.string.hdpi_drawables_description),
                ),
                DetailInfoAdapter.DetailInfo(
                        TextInfo.from(R.string.xhdpi_drawables),
                        TextInfo.from(data.xhdpiDrawables.toString()),
                        TextInfo.from(R.string.xhdpi_drawables_description),
                ),
                DetailInfoAdapter.DetailInfo(
                        TextInfo.from(R.string.xxhdpi_drawables),
                        TextInfo.from(data.xxhdpiDrawables.toString()),
                        TextInfo.from(R.string.xxhdpi_drawables_description),
                ),
                DetailInfoAdapter.DetailInfo(
                        TextInfo.from(R.string.xxxhdpi_drawables),
                        TextInfo.from(data.xxxhdpiDrawables.toString()),
                        TextInfo.from(R.string.xxxhdpi_drawables_description),
                ),
                DetailInfoAdapter.DetailInfo(
                        TextInfo.from(R.string.nodpi_drawables),
                        TextInfo.from(data.nodpiDrawables.toString()),
                        TextInfo.from(R.string.nodpi_drawables_description),
                ),
                DetailInfoAdapter.DetailInfo(
                        TextInfo.from(R.string.tvdpi_drawables),
                        TextInfo.from(data.tvdpiDrawables.toString()),
                        TextInfo.from(R.string.tvdpi_drawables_description),
                ),
                DetailInfoAdapter.DetailInfo(
                        TextInfo.from(R.string.all_layouts),
                        TextInfo.from(data.layouts.toString()),
                        TextInfo.from(R.string.all_layouts_description),
                ),
                DetailInfoAdapter.DetailInfo(
                        TextInfo.from(R.string.different_layouts),
                        TextInfo.from(data.differentLayouts.toString()),
                        TextInfo.from(R.string.different_layouts_description),
                ),
        )
    }

    init {
        appDetailFragmentViewModel.appDetails.observeForever(appDetailsObserver)
    }

    override fun onCleared() {
        super.onCleared()
        appDetailFragmentViewModel.appDetails.removeObserver(appDetailsObserver)
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(appDetailFragmentViewModel: AppDetailFragmentViewModel): AppResourceDetailsFragmentViewModel
    }
}