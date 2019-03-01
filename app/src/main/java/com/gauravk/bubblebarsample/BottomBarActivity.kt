package com.gauravk.bubblebarsample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_bottom_bar.*

class BottomBarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_bar)

        val fragList = ArrayList<ScreenSlidePageFragment>()
        fragList.add(ScreenSlidePageFragment.newInstance(getString(R.string.home)))
        fragList.add(ScreenSlidePageFragment.newInstance(getString(R.string.search)))
        fragList.add(ScreenSlidePageFragment.newInstance(getString(R.string.likes)))
        fragList.add(ScreenSlidePageFragment.newInstance(getString(R.string.notification)))
        fragList.add(ScreenSlidePageFragment.newInstance(getString(R.string.profile)))
        val pagerAdapter = ScreenSlidePagerAdapter(fragList, supportFragmentManager)
        view_pager.adapter = pagerAdapter

        bottom_navigation_view_linear.setNavigationChangeListener { _, position ->
            view_pager.setCurrentItem(position, true)
        }
    }


}
