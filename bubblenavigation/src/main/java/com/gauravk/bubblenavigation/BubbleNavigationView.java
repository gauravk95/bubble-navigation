package com.gauravk.bubblenavigation;

import android.content.Context;
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

public class BubbleNavigationView extends LinearLayout implements View.OnClickListener {

    private static final String TAG = "BNV";
    private static final int MIN_ITEMS = 2;
    private static final int MAX_ITEMS = 5;

    private ArrayList<BubbleToggleView> navItems;
    private BubbleNavigationChangeListener navigationChangeListener;

    private int currentActiveItemPostion = 0;

    public BubbleNavigationView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public BubbleNavigationView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BubbleNavigationView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putInt("current_item", currentActiveItemPostion);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            currentActiveItemPostion = bundle.getInt("current_item");
            state = bundle.getParcelable("superState");
        }
        super.onRestoreInstanceState(state);
    }

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

    private void updateChildNavItems() {
        navItems = new ArrayList<>();
        for (int index = 0; index < getChildCount(); ++index) {
            View view = getChildAt(index);
            if (view instanceof BubbleToggleView)
                navItems.add((BubbleToggleView) view);
            else {
                Log.w(TAG, "Cannot have child navItems other than BubbleToggleView");
                return;
            }
        }

        if (navItems.size() < MIN_ITEMS) {
            Log.w(TAG, "The navItems list should have at least 2 navItems of BubbleToggleView");
        } else if (navItems.size() > MAX_ITEMS) {
            Log.w(TAG, "The navItems list should not have more than 5 navItems of BubbleToggleView");
        }

        setClickListenerForItems();
        setInitialActiveState();
    }

    //if multiple navItems are activate at the same time, only enable 1
    private void setInitialActiveState() {
        boolean foundActiveElement = false;
        for (int i = 0; i < navItems.size(); i++) {
            if (navItems.get(i).isActive() && !foundActiveElement) {
                foundActiveElement = true;
                currentActiveItemPostion = i;
            } else {
                navItems.get(i).setInitialState(false);
            }
        }

        if (!foundActiveElement) {
            navItems.get(currentActiveItemPostion).setInitialState(true);
        }
    }

    private void setClickListenerForItems() {
        for (BubbleToggleView btv : navItems)
            btv.setOnClickListener(this);
    }

    private int getItemPositionById(int id) {
        for (int i = 0; i < navItems.size(); i++)
            if (id == navItems.get(i).getId())
                return i;
        return -1;
    }

    ///////////////////////////////////////////
    // PUBLIC METHODS
    //////////////////////////////////////////
    public void setNavigationChangeListener(BubbleNavigationChangeListener navigationChangeListener) {
        this.navigationChangeListener = navigationChangeListener;
    }

    @Override
    public void onClick(View v) {
        int changedPosition = getItemPositionById(v.getId());
        if (changedPosition >= 0) {
            if (changedPosition == currentActiveItemPostion) {
                return;
            }
            BubbleToggleView currentActiveToggleView = navItems.get(currentActiveItemPostion);
            BubbleToggleView newActiveToggleView = navItems.get(changedPosition);
            if (currentActiveToggleView != null)
                currentActiveToggleView.toggle();
            if (newActiveToggleView != null)
                newActiveToggleView.toggle();

            //changed the current active position
            currentActiveItemPostion = changedPosition;

            if (navigationChangeListener != null)
                navigationChangeListener.onNavigationChanged(v, currentActiveItemPostion);
        } else {
            Log.w(TAG, "Selected id not found! Cannot toggle");
        }
    }
}
