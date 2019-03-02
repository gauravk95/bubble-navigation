package com.gauravk.bubblebarsample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import com.gauravk.bubblebarsample.adapters.ScreenSlidePagerAdapter
import com.gauravk.bubblebarsample.fragment.ScreenSlidePageFragment
import kotlinx.android.synthetic.main.activity_top_bar.*

class TopBarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_bar)

        val fragList = ArrayList<ScreenSlidePageFragment>()
        fragList.add(ScreenSlidePageFragment.newInstance(getString(R.string.restaurant), R.color.orange_inactive))
        fragList.add(ScreenSlidePageFragment.newInstance(getString(R.string.room), R.color.red_inactive))
        fragList.add(ScreenSlidePageFragment.newInstance(getString(R.string.happy), R.color.green_inactive))
        val pagerAdapter = ScreenSlidePagerAdapter(fragList, supportFragmentManager)
        view_pager.adapter = pagerAdapter
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(p0: Int) {
                top_navigation_constraint.setCurrentActiveItem(p0)
            }

        })

        top_navigation_constraint.setNavigationChangeListener { _, position ->
            view_pager.setCurrentItem(position, true)
        }
    }


}
