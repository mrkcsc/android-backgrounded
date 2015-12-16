package com.miguelgaeta;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.miguelgaeta.backgrounded.Backgrounded;

import rx.functions.Action1;

public class AppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Backgrounded.init(getApplication());
        Backgrounded.get().subscribe(new Action1<Boolean>() {

            @Override
            public void call(Boolean backgrounded) {

                Log.e("Test", "Backgrounded status: " + backgrounded);
            }
        });
    }
}
