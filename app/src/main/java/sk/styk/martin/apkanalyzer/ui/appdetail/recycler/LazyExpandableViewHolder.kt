package sk.styk.martin.apkanalyzer.ui.appdetail.recycler

import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import net.cachapa.expandablelayout.ExpandableLayout
import sk.styk.martin.apkanalyzer.BR
import sk.styk.martin.apkanalyzer.ui.appdetail.page.activity.ARROW_ANIMATION_DURATION
import sk.styk.martin.apkanalyzer.ui.appdetail.page.activity.ROTATION_FLIPPED
import sk.styk.martin.apkanalyzer.ui.appdetail.page.activity.ROTATION_STANDARD

abstract class LazyExpandableViewHolder<BaseBinding : ViewDataBinding, ExpandBinding : ViewDataBinding, ViewModel : ExpandableItemViewModel>(val baseBinding: BaseBinding) : RecyclerView.ViewHolder(baseBinding.root) {

    protected lateinit var expandedBinding: ExpandBinding

    abstract fun baseContainer(): ViewGroup

    abstract fun expandedInflation(): ExpandBinding

    abstract fun expandableContainer(): ExpandableLayout

    abstract fun toggleArrow(): View

    abstract fun headerContainer(): View

    private fun inflateExpandedIfNeeded(viewModel: ViewModel) {
        val baseContainer = baseContainer()
        if (baseContainer.childCount == 1) {
            expandedBinding = expandedInflation()
            expandedBinding.setVariable(BR.viewModel, viewModel)
            baseContainer.addView(expandedBinding.root)
        }
    }

    private fun updateExpandedState(viewModel: ViewModel, isExpanded: Boolean) {
        if (isExpanded) {
            inflateExpandedIfNeeded(viewModel)
            expandableContainer().setExpanded(true, false)
        } else if (this::expandedBinding.isInitialized) {
            expandableContainer().setExpanded(false, false)
        }
        toggleArrow().rotation = if (isExpanded) ROTATION_FLIPPED else ROTATION_STANDARD
    }

    private fun onExpandedClickBase(viewModel: ViewModel): Boolean {
        inflateExpandedIfNeeded(viewModel)
        val newlyExpanded = !expandableContainer().isExpanded
        expandableContainer().isExpanded = newlyExpanded
        toggleArrow().animate().setDuration(ARROW_ANIMATION_DURATION).rotation(if (newlyExpanded) ROTATION_FLIPPED else ROTATION_STANDARD)
        return newlyExpanded
    }

    fun bind(viewModel: ViewModel) {
        baseBinding.setVariable(BR.viewModel, viewModel)
        updateExpandedState(viewModel, viewModel.expanded)
        headerContainer().setOnClickListener {
            viewModel.toggleExpanded(onExpandedClickBase(viewModel))
        }
    }
}
