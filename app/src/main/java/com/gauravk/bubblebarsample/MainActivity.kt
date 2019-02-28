package com.gauravk.bubblebarsample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //listeners for top navigation view
        top_navigation_constraint_text.text = String.format(
            getString(R.string.position_placeholder_top),
            top_navigation_constraint.currentActiveItemPosition
        )
        top_navigation_constraint.setNavigationChangeListener { _, position ->
            top_navigation_constraint_text.text = String.format(getString(R.string.position_placeholder_top), position)
        }

        //listener for bottom navigation view
        bottom_navigation_linear.text = String.format(
            getString(R.string.position_placeholder_bottom),
            bottom_navigation_view_linear.currentActiveItemPosition
        )
        bottom_navigation_view_linear.setNavigationChangeListener { _, position ->
            bottom_navigation_linear.text = String.format(getString(R.string.position_placeholder_bottom), position)
        }
    }
}
