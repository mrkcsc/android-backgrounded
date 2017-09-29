package com.miguelgaeta.backgrounded

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import rx.Observable
import rx.Subscription
import rx.subjects.BehaviorSubject
import rx.subjects.SerializedSubject
import java.util.concurrent.TimeUnit

/**
 * Utility class that uses [Application.ActivityLifecycleCallbacks] to heuristically
 * track whether or not the current application is in the foreground or background.
 */
object Backgrounded {

    private val lifecycle: Lifecycle = Lifecycle()

    @JvmStatic fun init(application: Application?) {
        application?.registerActivityLifecycleCallbacks(lifecycle)
    }

    @JvmStatic fun destroy(application: Application?) {
        application?.unregisterActivityLifecycleCallbacks(lifecycle)
    }

    @JvmStatic fun get(): Observable<Boolean> =
            lifecycle.emitter.distinctUntilChanged()

    @JvmStatic fun isBackgrounded(): Boolean =
            lifecycle.emitter.toBlocking().mostRecent(true).iterator().next()

    private class Lifecycle internal constructor() : Application.ActivityLifecycleCallbacks {

        internal val emitter = SerializedSubject(BehaviorSubject.create<Boolean>())
        internal var subscription: Subscription? = null

        init {
            checkBackgrounded(DELAY_SHORT)
        }

        override fun onActivityCreated(activity: Activity, bundle: Bundle) {
        }

        override fun onActivityStarted(activity: Activity) {
        }

        override fun onActivityResumed(activity: Activity) {
            subscription?.unsubscribe()

            emitter.onNext(false)
        }

        override fun onActivityPaused(activity: Activity) {
            checkBackgrounded(DELAY)
        }

        override fun onActivityStopped(activity: Activity) {
        }

        override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {
        }

        override fun onActivityDestroyed(activity: Activity) {
        }

        private fun checkBackgrounded(delay: Long) {
            subscription = Observable
                    .just(true)
                    .delay(delay, TimeUnit.MILLISECONDS)
                    .subscribe(emitter::onNext) { Log.e(TAG, TAG_ERROR) }
        }

        companion object {

            private val DELAY = 2000L
            private val DELAY_SHORT = 500L

            private val TAG = "Backgrounded"
            private val TAG_ERROR = "Subscription error in backgrounded delay."
        }
    }
}
