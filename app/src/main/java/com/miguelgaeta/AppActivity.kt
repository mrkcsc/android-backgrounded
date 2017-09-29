package com.miguelgaeta

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.miguelgaeta.backgrounded.Backgrounded

class AppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Backgrounded.init(application)
        Backgrounded.get().subscribe { backgrounded -> Log.e("Test", "Backgrounded status: " + backgrounded) }

        Log.e("Test", "Currently backgrounded: " + Backgrounded.isBackgrounded())
    }

    override fun onDestroy() {
        super.onDestroy()

        Backgrounded.destroy(application)
    }
}
