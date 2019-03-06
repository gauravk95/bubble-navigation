package com.gauravk.bubblebarsample.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gauravk.bubblebarsample.R
import com.gauravk.bubblebarsample.adapters.ScreenSlidePagerAdapter

import kotlinx.android.synthetic.main.fragment_equal_bottom_bar.*

class EqualBottomBarFragment : Fragment() {

    private lateinit var inflatedView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_equal_bottom_bar, container, false);
        return inflatedView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val fragList = ArrayList<ScreenSlidePageFragment>()
        fragList.add(ScreenSlidePageFragment.newInstance(getString(R.string.shop), R.color.blue_inactive))
        fragList.add(ScreenSlidePageFragment.newInstance(getString(R.string.photos), R.color.purple_inactive))
        fragList.add(ScreenSlidePageFragment.newInstance(getString(R.string.call), R.color.green_inactive))
        val pagerAdapter = ScreenSlidePagerAdapter(fragList, childFragmentManager)
        view_pager.adapter = pagerAdapter
        //disable swipe
        view_pager.setOnTouchListener { _, _ ->
            return@setOnTouchListener true
        }

        equal_navigation_bar.setNavigationChangeListener { _, position ->
            view_pager.setCurrentItem(position, true)
        }

        //change the initial activate element
        val newInitialPosition = 2
        equal_navigation_bar.setCurrentActiveItem(newInitialPosition)
        view_pager.setCurrentItem(newInitialPosition, false)
    }


}
