package com.gauravk.bubblebarsample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.gauravk.bubblebarsample.fragment.EqualBottomBarFragment

class EqualBottomBarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equal_bottom_bar)

        var fragment = supportFragmentManager.findFragmentById(R.id.content_frame) as EqualBottomBarFragment?
        if (fragment == null) {
            fragment = EqualBottomBarFragment()
            addFragment(fragment, R.id.content_frame)
        }
    }

    private fun addFragment(fragment: EqualBottomBarFragment, id: Int) {
        val ft = supportFragmentManager.beginTransaction()
        ft.add(id, fragment)
        ft.commit()
    }

}
