package sk.styk.martin.apkanalyzer.activity.detailfragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList

import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.activity.AppDetailFragment
import sk.styk.martin.apkanalyzer.adapter.detaillist.SimpleStringListAdapter
import sk.styk.martin.apkanalyzer.model.detail.ClassPathData

/**
 * @author Martin Styk
 * @version 30.06.2017.
 */
class AppDetailFragment_Classes : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_app_detail_simple_string_list, container, false)

        val data = arguments.getParcelable<ClassPathData>(AppDetailFragment.ARG_CHILD)

        val allClasses = ArrayList(data!!.packageClasses)
        allClasses.addAll(data.otherClasses)

        val recyclerView = rootView.findViewById<RecyclerView>(R.id.recycler_view_simple_string_list)
        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)

        val adapter = SimpleStringListAdapter(allClasses)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        return rootView
    }
}
