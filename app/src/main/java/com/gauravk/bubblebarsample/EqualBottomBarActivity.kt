package com.gauravk.bubblebarsample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_equal_bottom_bar.*

class EqualBottomBarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equal_bottom_bar)

        val fragList = ArrayList<ScreenSlidePageFragment>()
        fragList.add(ScreenSlidePageFragment.newInstance(getString(R.string.shop)))
        fragList.add(ScreenSlidePageFragment.newInstance(getString(R.string.photos)))
        fragList.add(ScreenSlidePageFragment.newInstance(getString(R.string.call)))
        val pagerAdapter = ScreenSlidePagerAdapter(fragList, supportFragmentManager)
        view_pager.adapter = pagerAdapter

        equal_navigation_bar.setNavigationChangeListener { _, position ->
            view_pager.setCurrentItem(position, true)
        }
    }


}
