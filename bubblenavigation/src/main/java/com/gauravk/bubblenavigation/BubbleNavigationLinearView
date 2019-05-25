package com.superTasker.frameworkdrivers.customWidgets

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.get
import com.gauravk.bubblenavigation.BubbleToggleView
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener
import java.lang.IllegalStateException

class BubbleNavigationLinearView :LinearLayout,View.OnClickListener{


    private val CURRENT_ACTIVE_VIEW:String ="x.f"
    private var currentActiveItemPosition: Int = 0
    private val TAG = "BNLView"
    private var navigationChangeListener: BubbleNavigationChangeListener? = null

    constructor(context:Context,attrs:AttributeSet?):super(context,attrs) {
        init()
    }
    constructor(context:Context):super(context) {
        init()
    }
    constructor(context:Context,attrs:AttributeSet?,defStyleAttr:Int):super(context,attrs,defStyleAttr) {
        init()
    }
    private fun init() {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER
    }

    override fun  onFinishInflate() {
        super.onFinishInflate()
        checkViewsAreAllBubble()
        setClickListenerForItems()
        refreshActiveState()
        updateMeasurementForItems()
    }
    private fun refreshActiveState() {
        var foundActiveElement = false
        for ( i:Int in 0 until childCount) {
            val btv = this[i] as BubbleToggleView
            if (btv.isActive && !foundActiveElement) {
                foundActiveElement = true
                this.currentActiveItemPosition = i
            } else {
                btv.setInitialState(false)
            }
        }
        if (!foundActiveElement) {
            (this[this.currentActiveItemPosition] as BubbleToggleView).setInitialState(true)
        }
    }

    private fun updateMeasurementForItems() {
        val numChildViews = childCount
        if(numChildViews > 0) {
            val calculatedEachItemWidth = (measuredWidth - (paddingRight + paddingLeft)) / numChildViews
            for(i:Int in 0 until numChildViews) {
                (this[i] as BubbleToggleView).updateMeasurements(calculatedEachItemWidth)
            }
        }

    }

    private fun checkViewsAreAllBubble() {
        println("zzbc: $childCount" )
        for( i:Int in 0 until childCount) {
            val view:View = this[i]
            val wrongView:Boolean = view !is BubbleToggleView
            if(wrongView) {
                throw IllegalStateException("only BubbleToggles are allowed")
            }
        }
    }

    private fun setClickListenerForItems() {
        for(i:Int in 0 until childCount) {
            (this[i] as BubbleToggleView).setOnClickListener(this)
        }
    }

    private fun getItemPositionById(id: Int): Int {
        for(i:Int in 0 until childCount) {
            if(id == (this[i] as BubbleToggleView).id) {
                return i
            }
        }
        return -1
    }

    fun setNavigationChangeListener(navigationChangeListener: BubbleNavigationChangeListener) {
        this.navigationChangeListener = navigationChangeListener
        this.navigationChangeListener?.onNavigationChanged(this[currentActiveItemPosition],this.currentActiveItemPosition)
    }

    fun setTypeface(typeface: Typeface) {
        for(i:Int in 0 until childCount) {
            (this[i] as BubbleToggleView).setTitleTypeface(typeface)
        }
    }


    fun getCurrentActiveItemPosition(): Int {
        return currentActiveItemPosition
    }

    fun setCurrentActiveItem(position: Int) {
        if (currentActiveItemPosition == position) return
        if (position < 0 || position >= this.childCount)
            return
        val btv:BubbleToggleView =( this[(position)] as BubbleToggleView)
        btv.performClick()
    }

    override fun onClick(view:View?) {
        println("clicked ")
        val changedPosition = getItemPositionById(view?.id!!)
        if (changedPosition >= 0) {
            if (changedPosition == currentActiveItemPosition) {
                return
            }
            val currentActiveToggleView:BubbleToggleView = this[(currentActiveItemPosition)] as BubbleToggleView
            val newActiveToggleView = this[(changedPosition)] as BubbleToggleView
            currentActiveToggleView.toggle()
            newActiveToggleView.toggle()
            //changed the current active position
            currentActiveItemPosition = changedPosition
            navigationChangeListener?.onNavigationChanged(view, currentActiveItemPosition)
        } else {
            Log.w(TAG, "Selected id not found! Cannot toggle")
        }
    }

    fun onSaveInstanceState(bundle: Bundle) {
        bundle.putInt(CURRENT_ACTIVE_VIEW,currentActiveItemPosition)
    }

    fun onRestoreInstanceState(bundle: Bundle) {
        val currentActiveView = bundle.getInt(CURRENT_ACTIVE_VIEW,0)
        setCurrentActiveItem(currentActiveView)
    }
