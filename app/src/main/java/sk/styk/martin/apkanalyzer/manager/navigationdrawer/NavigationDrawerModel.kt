package sk.styk.martin.apkanalyzer.manager.navigationdrawer

import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationDrawerModel @Inject constructor() {

    private val flow = MutableSharedFlow<Boolean>()

    suspend fun openDrawer() = flow.emit(true)

    suspend fun closeDrawer() = flow.emit(false)

    fun handleState() = flow
}
