### Android Backgrounded

A simple utility class that allows you to track when your Android application has entered/exited the foreground.

### Installation

[![Download](https://api.bintray.com/packages/mrkcsc/maven/com.miguelgaeta.backgrounded/images/download.svg)](https://bintray.com/mrkcsc/maven/com.miguelgaeta.backgrounded/_latestVersion)

```groovy

compile 'com.miguelgaeta.android-backgrounded:backgrounded:1.0.4'

```

### Configuration

Initialize the backgrounded utility only once by calling `init` with an [Application](http://developer.android.com/reference/android/app/Application.html) context:

```java

class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        
        Backgrounded.init(this);
    }
}

```

Optionally, you can destroy the backgrounded utility by calling `destroy` with an Application context:

```java

public class AppActivity extends AppCompatActivity {

    public void someFunction() {
    
        Backgrounded.destroy(getApplication());
    }
}

```

### Usage

Once initialized you can query for Application foreground state in a blocking or non-blocking fashion:

```java

boolean backgrounded = Backgrounded.isBackgrounded();

Backgrounded.get().subscribe(backgrounded -> {

    boolean isBackgrounded = backgrounded;
});

```

### Intended Usage

Generally, it is considered bad practice to attempt to track the background state of an Android Application due to the fluid and often intertwined nature of Activities.

However, one might want to track the current backgrounded state to determine things such as:

* How to handle Google Cloud Messenger notifications (eg: show in app, discard, or use a standard notification).
* Shutting down long living connections such as web sockets when entering a backgrounded state.
* Pausing or resuming audio.

### Caveats

This utility considers the application in the background if an [Activity](http://developer.android.com/reference/android/app/Activity.html) calls `onPause` and another Activity of that Application does not get resumed shortly after (threshold is set to one second).  Similarly an Activity is not considered to be in the foreground until the `onResume` event of an Activity is called.

### License

*Copyright 2015 Miguel Gaeta*

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
