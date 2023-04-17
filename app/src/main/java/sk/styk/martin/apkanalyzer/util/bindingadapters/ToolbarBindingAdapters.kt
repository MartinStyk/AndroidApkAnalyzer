package sk.styk.martin.apkanalyzer.util.bindingadapters

import android.widget.SearchView
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.util.TextInfo
import sk.styk.martin.apkanalyzer.views.toolbar.NavigationIconState

@BindingAdapter("navigationIconState")
fun Toolbar.navigationIconState(iconState: NavigationIconState) {
    navigationIcon = when (iconState) {
        NavigationIconState.NONE -> null
        NavigationIconState.ARROW -> DrawerArrowDrawable(context).apply { progress = 1f }
        NavigationIconState.HAMBURGER -> DrawerArrowDrawable(context).apply { progress = 0f }
    }
}

@BindingAdapter("inflateMenu")
fun Toolbar.inflateMenu(@MenuRes menuResId: Int) {
    menu.clear()
    if (menuResId != 0) {
        inflateMenu(menuResId)
    }
}

@BindingAdapter("onQueryTextListener")
fun Toolbar.searchOnQueryTextListener(onQueryTextListener: SearchView.OnQueryTextListener) {
    val searchView = menu.findItem(R.id.action_search).actionView as? SearchView ?: return
    searchView.setOnQueryTextListener(onQueryTextListener)
}

@BindingAdapter("onSearchCloseListener")
fun Toolbar.onSearchCloseListener(onCloseListener: SearchView.OnCloseListener) {
    val searchView = menu.findItem(R.id.action_search)?.actionView as? SearchView ?: return
    searchView.setOnCloseListener(onCloseListener)
}

@BindingAdapter("searchHint")
fun Toolbar.searchHint(@StringRes searchHint: Int) {
    val searchView = menu.findItem(R.id.action_search)?.actionView as? SearchView ?: return
    searchView.queryHint = context.getString(searchHint)
}

@BindingAdapter("searchQuery")
fun Toolbar.searchQuery(searchQuery: String?) {
    val searchView = menu.findItem(R.id.action_search)?.actionView as? SearchView ?: return
    if (searchView.query != searchQuery) {
        searchView.setQuery(searchQuery, false)
    }
}

@BindingAdapter("title")
fun Toolbar.setTitle(textInfo: TextInfo?) {
    title = textInfo?.getText(context) ?: ""
}
