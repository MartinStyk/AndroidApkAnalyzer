package sk.styk.martin.apkanalyzer.manager.navigationdrawer

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class NavigationDrawerModel @Inject constructor() {

    private val channel = BroadcastChannel<Boolean>(Channel.CONFLATED)

    suspend fun openDrawer() = channel.send(true)

    suspend fun closeDrawer() = channel.send(false)

    fun handleState() = channel.asFlow()
}