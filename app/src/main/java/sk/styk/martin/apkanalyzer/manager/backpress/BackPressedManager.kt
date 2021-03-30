package sk.styk.martin.apkanalyzer.manager.backpress

import sk.styk.martin.apkanalyzer.dependencyinjection.util.ActivityScope
import javax.inject.Inject

@ActivityScope
class BackPressedManager @Inject constructor() {

    private val backPressedListeners = mutableMapOf<BackPressedListener, RegistrationData>()

    fun onBackPressed(): Boolean {
        val sortedList = backPressedListeners.toList().sortedWith(compareByDescending { (_, registrationData) -> registrationData.regTime })
        for (list in sortedList) {
            if (list.first.onBackPressed()) {
                return true
            }
        }

        return false
    }

    fun registerBackPressedListener(listener: BackPressedListener) {
        backPressedListeners[listener] = RegistrationData(System.currentTimeMillis())
    }

    fun unregisterBackPressedListener(listener: BackPressedListener) {
        backPressedListeners.remove(listener)
    }

    private data class RegistrationData(val regTime: Long)
}