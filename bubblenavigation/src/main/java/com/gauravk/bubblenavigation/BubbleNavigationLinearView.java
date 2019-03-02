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

    //constants
    private static final String TAG = "BNLView";
    private static final int MIN_ITEMS = 2;
    private static final int MAX_ITEMS = 5;

    private ArrayList<BubbleToggleView> bubbleNavItems;
    private BubbleNavigationChangeListener navigationChangeListener;

    private int currentActiveItemPosition = 0;

    /**
     * Constructors
     */
    public BubbleNavigationLinearView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public BubbleNavigationLinearView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BubbleNavigationLinearView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putInt("current_item", currentActiveItemPosition);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            currentActiveItemPosition = bundle.getInt("current_item");
            state = bundle.getParcelable("superState");
        }
        super.onRestoreInstanceState(state);
    }

    /////////////////////////////////////////
    // PRIVATE METHODS
    /////////////////////////////////////////

    /**
     * Initialize
     *
     * @param context current context
     * @param attrs   custom attributes
     */
    private void init(Context context, AttributeSet attrs) {

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        post(new Runnable() {
            @Override
            public void run() {
                updateChildNavItems();
            }
        });
    }

    /**
     * Finds Child Elements of type {@link BubbleToggleView} and adds them to {@link #bubbleNavItems}
     */
    private void updateChildNavItems() {
        bubbleNavItems = new ArrayList<>();
        for (int index = 0; index < getChildCount(); ++index) {
            View view = getChildAt(index);
            if (view instanceof BubbleToggleView)
                bubbleNavItems.add((BubbleToggleView) view);
            else {
                Log.w(TAG, "Cannot have child bubbleNavItems other than BubbleToggleView");
                return;
            }
        }

        if (bubbleNavItems.size() < MIN_ITEMS) {
            Log.w(TAG, "The bubbleNavItems list should have at least 2 bubbleNavItems of BubbleToggleView");
        } else if (bubbleNavItems.size() > MAX_ITEMS) {
            Log.w(TAG, "The bubbleNavItems list should not have more than 5 bubbleNavItems of BubbleToggleView");
        }

        setClickListenerForItems();
        setInitialActiveState();
        updateMeasurementForItems();
    }

    /**
     * Makes sure that ONLY ONE child {@link #bubbleNavItems} is active
     */
    private void setInitialActiveState() {
        boolean foundActiveElement = false;
        for (int i = 0; i < bubbleNavItems.size(); i++) {
            if (bubbleNavItems.get(i).isActive() && !foundActiveElement) {
                foundActiveElement = true;
                currentActiveItemPosition = i;
            } else {
                bubbleNavItems.get(i).setInitialState(false);
            }
        }

        if (!foundActiveElement) {
            bubbleNavItems.get(currentActiveItemPosition).setInitialState(true);
        }
    }

    /**
     * Update the measurements of the child components {@link #bubbleNavItems}
     */
    private void updateMeasurementForItems() {
        int numChildElements = bubbleNavItems.size();
        if (numChildElements > 0) {
            int calculatedEachItemWidth = (getMeasuredWidth() - (getPaddingRight() + getPaddingLeft())) / numChildElements;
            for (BubbleToggleView btv : bubbleNavItems)
                btv.updateMeasurements(calculatedEachItemWidth);
        }
    }

    /**
     * Sets {@link android.view.View.OnClickListener} for the child views
     */
    private void setClickListenerForItems() {
        for (BubbleToggleView btv : bubbleNavItems)
            btv.setOnClickListener(this);
    }

    /**
     * Gets the Position of the Child from {@link #bubbleNavItems} from its id
     *
     * @param id of view to be searched
     * @return position of the Item
     */
    private int getItemPositionById(int id) {
        for (int i = 0; i < bubbleNavItems.size(); i++)
            if (id == bubbleNavItems.get(i).getId())
                return i;
        return -1;
    }

    ///////////////////////////////////////////
    // PUBLIC METHODS
    ///////////////////////////////////////////

    /**
     * Set the navigation change listener {@link BubbleNavigationChangeListener}
     *
     * @param navigationChangeListener sets the passed parameters as listener
     */
    public void setNavigationChangeListener(BubbleNavigationChangeListener navigationChangeListener) {
        this.navigationChangeListener = navigationChangeListener;
    }

    /**
     * Set the {@link Typeface} for the Text Elements of the View
     *
     * @param typeface to be used
     */
    public void setTypeface(Typeface typeface) {
        for (BubbleToggleView btv : bubbleNavItems)
            btv.setTitleTypeface(typeface);
    }

    /**
     * Gets the current active position
     *
     * @return active item position
     */
    public int getCurrentActiveItemPosition() {
        return currentActiveItemPosition;
    }

    /**
     * Sets the current active item
     *
     * @param position current position change
     */
    public void setCurrentActiveItem(int position) {
        if (currentActiveItemPosition == position) return;
        if (position < 0 || position >= bubbleNavItems.size())
            return;
        BubbleToggleView btv = bubbleNavItems.get(position);
        btv.performClick();
    }

    @Override
    public void onClick(View v) {
        int changedPosition = getItemPositionById(v.getId());
        if (changedPosition >= 0) {
            if (changedPosition == currentActiveItemPosition) {
                return;
            }
            BubbleToggleView currentActiveToggleView = bubbleNavItems.get(currentActiveItemPosition);
            BubbleToggleView newActiveToggleView = bubbleNavItems.get(changedPosition);
            if (currentActiveToggleView != null)
                currentActiveToggleView.toggle();
            if (newActiveToggleView != null)
                newActiveToggleView.toggle();

            //changed the current active position
            currentActiveItemPosition = changedPosition;

            if (navigationChangeListener != null)
                navigationChangeListener.onNavigationChanged(v, currentActiveItemPosition);
        } else {
            Log.w(TAG, "Selected id not found! Cannot toggle");
        }
    }
}
