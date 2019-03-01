package com.gauravk.bubblebarsample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_top_bar.*

class TopBarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_bar)

        val fragList = ArrayList<ScreenSlidePageFragment>()
        fragList.add(ScreenSlidePageFragment.newInstance(getString(R.string.restaurant)))
        fragList.add(ScreenSlidePageFragment.newInstance(getString(R.string.room)))
        fragList.add(ScreenSlidePageFragment.newInstance(getString(R.string.happy)))
        val pagerAdapter = ScreenSlidePagerAdapter(fragList, supportFragmentManager)
        view_pager.adapter = pagerAdapter

        top_navigation_constraint.setNavigationChangeListener { _, position ->
            view_pager.setCurrentItem(position, true)
        }
    }


}
