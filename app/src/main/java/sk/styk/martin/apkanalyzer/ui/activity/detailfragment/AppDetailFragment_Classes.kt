package sk.styk.martin.apkanalyzer.ui.activity.detailfragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_app_detail_simple_string_list.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.ui.activity.AppDetailFragment
import sk.styk.martin.apkanalyzer.ui.adapter.detaillist.SimpleStringListAdapter
import sk.styk.martin.apkanalyzer.model.detail.ClassPathData
import java.util.*

/**
 * @author Martin Styk
 * @version 30.06.2017.
 */
class AppDetailFragment_Classes : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_app_detail_simple_string_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = arguments.getParcelable<ClassPathData>(AppDetailFragment.ARG_CHILD)
                ?: throw IllegalArgumentException("null data")

        val allClasses = ArrayList(data.packageClasses)
        allClasses.addAll(data.otherClasses)

        recycler_view_simple_string_list.adapter = SimpleStringListAdapter(allClasses)
        recycler_view_simple_string_list.setHasFixedSize(true)
        recycler_view_simple_string_list.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }
}
