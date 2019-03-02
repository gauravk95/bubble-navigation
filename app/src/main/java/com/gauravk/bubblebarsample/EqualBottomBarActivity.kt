package com.gauravk.bubblebarsample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.gauravk.bubblebarsample.adapters.ScreenSlidePagerAdapter
import com.gauravk.bubblebarsample.fragment.ScreenSlidePageFragment
import kotlinx.android.synthetic.main.activity_equal_bottom_bar.*

class EqualBottomBarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equal_bottom_bar)

        val fragList = ArrayList<ScreenSlidePageFragment>()
        fragList.add(ScreenSlidePageFragment.newInstance(getString(R.string.shop), R.color.blue_inactive))
        fragList.add(ScreenSlidePageFragment.newInstance(getString(R.string.photos), R.color.purple_inactive))
        fragList.add(ScreenSlidePageFragment.newInstance(getString(R.string.call), R.color.green_inactive))
        val pagerAdapter = ScreenSlidePagerAdapter(fragList, supportFragmentManager)
        view_pager.adapter = pagerAdapter
        //disable swipe
        view_pager.setOnTouchListener { _, _ ->
            return@setOnTouchListener true
        }

        equal_navigation_bar.setNavigationChangeListener { _, position ->
            view_pager.setCurrentItem(position, true)
        }
    }


}
