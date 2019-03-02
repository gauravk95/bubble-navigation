package com.gauravk.bubblebarsample

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        open_top_navigation_bar.setOnClickListener {
            launchTopBarActivity()
        }

        open_top_float_navigation_bar.setOnClickListener {
            launchFloatingBarActivity()
        }

        open_bottom_equal_navigation_bar.setOnClickListener {
            launchEqualBarActivity()
        }

        open_bottom_navigation_bar.setOnClickListener {
            launchBottomBarActivity()
        }
    }

    private fun launchBottomBarActivity() {
        val intent = Intent(this, BottomBarActivity::class.java)
        startActivity(intent)
    }

    private fun launchFloatingBarActivity() {
        val intent = Intent(this, FloatingTopBarActivity::class.java)
        startActivity(intent)
    }

    private fun launchTopBarActivity() {
        val intent = Intent(this, TopBarActivity::class.java)
        startActivity(intent)
    }

    private fun launchEqualBarActivity() {
        val intent = Intent(this, EqualBottomBarActivity::class.java)
        startActivity(intent)
    }
}
