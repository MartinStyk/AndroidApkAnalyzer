package sk.styk.martin.apkanalyzer.util.live

import androidx.annotation.AnyThread
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

open class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val pending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            Timber.tag(TAG).w("Multiple observers registered but only one will be notified of changes.")
        }

        // Observe the internal MutableLiveData
        super.observe(owner) { t ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        }
    }

    @MainThread
    override fun setValue(t: T?) {
        pending.set(true)
        super.setValue(t)
    }

    @AnyThread
    override fun postValue(t: T?) {
        pending.set(true)
        super.postValue(t)
    }

    @MainThread
    fun call() {
        value = null
    }

    @AnyThread
    fun postCall() {
        postValue(null)
    }

    companion object {

        private const val TAG = "SingleLiveEvent"
    }
}
