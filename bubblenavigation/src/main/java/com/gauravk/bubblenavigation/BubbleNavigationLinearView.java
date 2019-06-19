/*
        Copyright 2019 Gaurav Kumar

        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at
        http://www.apache.org/licenses/LICENSE-2.0
        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.
*/
package com.gauravk.bubblenavigation;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;

import java.util.ArrayList;

/**
 * BubbleNavigationLinearView
 *
 * @author Gaurav Kumar
 */

@SuppressWarnings("unused")
public class BubbleNavigationLinearView extends LinearLayout implements View.OnClickListener {

    private String CURRENT_ACTIVE_VIEW = "x.f";
    private int currentActiveItemPosition = 0;
    private static final String TAG = "bnlview";
    private BubbleNavigationChangeListener navigationChangeListener = null;

    public BubbleNavigationLinearView(Context context, AttributeSet attrs) {
        super(context,attrs);
        init();

    }
    public BubbleNavigationLinearView(Context context) {
        super(context);
        init();
    }

    public BubbleNavigationLinearView(Context context, AttributeSet attrs, int defStyleAttr)  {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        checkViewsAreAllBubble();
        setClickListenerForItems();
        refreshActiveState();
        updateMeasurementForItems();
    }

    private void refreshActiveState() {
        boolean foundActiveElement = false;
        for (int i=0;i<this.getChildCount();++i) {
            BubbleToggleView btv = (BubbleToggleView)getChildAt(i);
            if (btv.isActive() && !foundActiveElement) {
                foundActiveElement = true;
                this.currentActiveItemPosition = i;
            } else {
                btv.setInitialState(false);
            }
        }
        if (!foundActiveElement) {
            ((BubbleToggleView)getChildAt(currentActiveItemPosition)).setInitialState(true);
        }
    }

    private void updateMeasurementForItems() {
        int  numChildViews = getChildCount();
        if(numChildViews > 0) {
            int calculatedEachItemWidth = (getMeasuredWidth() - (getPaddingRight() + getPaddingLeft())) / numChildViews;
            for(int i=0;i<numChildViews;++i) {
                ((BubbleToggleView)getChildAt(i)).updateMeasurements(calculatedEachItemWidth);
            }
        }
    }

    private void checkViewsAreAllBubble() {
        for( int i=0;i<getChildCount();++i) {
            View view = this.getChildAt(i);
            final boolean wrongView = !(view instanceof BubbleToggleView);
            if(wrongView) {
                throw new IllegalStateException("only BubbleToggles are allowed");
            }
        }
    }


    private void setClickListenerForItems() {
        for(int i=0;i<getChildCount();++i) {
            ((BubbleToggleView)getChildAt(i)).setOnClickListener(this);
        }
    }

    private int getItemPositionById(int id) {
        for(int i=0;i<getChildCount();++i) {
            if(id == this.getChildAt(i).getId()) {
                return i;
            }
        }
        return -1;
    }

    public void setNavigationChangeListener(BubbleNavigationChangeListener listener) {
        this.navigationChangeListener = listener;
        this.navigationChangeListener.onNavigationChanged(this.getChildAt(currentActiveItemPosition),this.currentActiveItemPosition);
    }

    public void setTypeface(Typeface typeface) {
        for(int i=0;i<getChildCount();++i) {
            final BubbleToggleView view = (BubbleToggleView)getChildAt(i);
            view.setTitleTypeface(typeface);
        }
    }

    public int getCurrentActiveItemPosition() {
        return currentActiveItemPosition;
    }

    public void setCurrentActiveItem(int position) {
        if(currentActiveItemPosition == position) return;
        if(position < 0 || position >= this.getChildCount()) {
            return ;
        }
        BubbleToggleView btv = (BubbleToggleView)this.getChildAt(position);
        btv.performClick();
    }
    @Override
    public void onClick(View v) {
        int changedPosition = getItemPositionById(v.getId());
        if (changedPosition >= 0) {
            if (changedPosition == currentActiveItemPosition) {
                return;
            }
            BubbleToggleView currentActiveToggleView = (BubbleToggleView)this.getChildAt(currentActiveItemPosition);
            BubbleToggleView newActiveToggleView = (BubbleToggleView)this.getChildAt(changedPosition);
            currentActiveToggleView.toggle();
            newActiveToggleView.toggle();
            //changed the current active position
            currentActiveItemPosition = changedPosition;
            navigationChangeListener.onNavigationChanged(v, currentActiveItemPosition);
        } else {
            Log.w(TAG, "Selected id not found! Cannot toggle");
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        bundle.putInt(CURRENT_ACTIVE_VIEW,currentActiveItemPosition);
    }

    public void onRestoreInstanceState(Bundle bundle) {
        int currentActiveView = bundle.getInt(CURRENT_ACTIVE_VIEW,0);
        setCurrentActiveItem(currentActiveView);
    }
}
