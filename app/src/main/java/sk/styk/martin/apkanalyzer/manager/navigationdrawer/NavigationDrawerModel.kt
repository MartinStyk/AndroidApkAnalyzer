package sk.styk.martin.apkanalyzer.manager.navigationdrawer

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class NavigationDrawerModel @Inject constructor() {

    private val flow = MutableSharedFlow<Boolean>()

    suspend fun openDrawer() = flow.emit(true)

    suspend fun closeDrawer() = flow.emit(false)

    fun handleState() = flow
}