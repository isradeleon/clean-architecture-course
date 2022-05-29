package com.isradeleon.todoapp.data.viewmodel.extensions

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A lifecycle-aware observable that sends only new updates after subscription, used for events like
 * navigation and Snackbar messages.
 * <p>
 * This avoids a common problem with events: on configuration change (like rotation) an update
 * can be emitted if the observer is active. This LiveData only calls the observable if there's an
 * explicit call to setValue() or call().
 * <p>
 * Note that only one observer is going to be notified of changes.
 */
class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val isPending = AtomicBoolean(false)
    private val tag = "SingleLiveEvent"

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        // Validate if it has more than one observer
        if (hasActiveObservers())
            Log.w(tag, "Multiple observers registered but only one will be notified of changes.")

        // Observe the internal MutableLiveData
        super.observe(owner) { t ->
            if (isPending.compareAndSet(true, false))
                observer.onChanged(t)
        }
    }

    @MainThread
    override fun setValue(t: T?) {
        isPending.set(true)
        super.setValue(t)
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null
    }
}