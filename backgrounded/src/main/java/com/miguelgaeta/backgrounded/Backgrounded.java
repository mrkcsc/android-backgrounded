package com.miguelgaeta.backgrounded;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by Miguel Gaeta on 12/15/15.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Backgrounded {

    private static Lifecycle lifecycle;

    public static void init(Application application) {

        lifecycle = new Lifecycle();

        if (application != null) {
            application.registerActivityLifecycleCallbacks(lifecycle);
        }
    }

    @SuppressWarnings("unused")
    public static void destroy(Application application) {

        if (application != null) {
            application.unregisterActivityLifecycleCallbacks(lifecycle);
        }
    }

    public static Observable<Boolean> get() {

        checkInitialized();

        return lifecycle.emitter.distinctUntilChanged();
    }

    public static boolean isBackgrounded() {

        checkInitialized();

        return lifecycle.emitter.toBlocking().mostRecent(true).iterator().next();
    }

    private static void checkInitialized() {

        if (lifecycle == null) {

            throw new RuntimeException("Backgrounded utility is not initialized.");
        }
    }

    private static class Lifecycle implements Application.ActivityLifecycleCallbacks {

        private static final int DELAY = 2000;
        private static final int DELAY_SHORT = 500;

        private static final String TAG = "Backgrounded";

        private final Subject<Boolean, Boolean> emitter = new SerializedSubject<>(BehaviorSubject.<Boolean>create());
        private Subscription subscription;

        Lifecycle() {

            checkBackgrounded(DELAY_SHORT);
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

            if (subscription != null) {
                subscription.unsubscribe();
            }

            emitter.onNext(false);
        }

        @Override
        public void onActivityPaused(Activity activity) {

            checkBackgrounded(DELAY);
        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }

        private void checkBackgrounded(final int delay) {

            subscription = Observable.timer(delay, TimeUnit.MILLISECONDS).subscribe(new Action1<Long>() {

                @Override
                public void call(Long aLong) {

                    emitter.onNext(true);
                }

            }, new Action1<Throwable>() {

                @Override
                public void call(Throwable throwable) {

                    Log.e(TAG, "Subscription error in backgrounded delay.");
                }
            });
        }
    }
}
