package com.gauravk.bubblebarsample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.gauravk.bubblenavigation.BubbleNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val view = findViewById<BubbleNavigationView>(R.id.bottom_navigation_view)
        view.setNavigationChangeListener { _, position ->
            Log.i("Main", "Nav Changed at $position")
        }
    }
}
