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

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gauravk.bubblenavigation.util.ViewUtils;

/**
 * BubbleToggleView
 *
 * @author Gaurav Kumar
 */
public class BubbleToggleView extends LinearLayout {

    private static final String TAG = "BNI_View";
    private static final int DEFAULT_ANIM_DURATION = 300;

    private BubbleToggleItem bubbleToggleItem;

    private boolean isActive = false;

    private ImageView iconView;
    private TextView titleView;

    private int animationDuration;
    private boolean showShapeAlways;

    private float maxTitleWidth;
    private float measuredTitleWidth;

    /**
     * Constructors
     */
    public BubbleToggleView(Context context) {
        super(context);
        init(context, null);
    }

    public BubbleToggleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BubbleToggleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BubbleToggleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    /////////////////////////////////////
    // PRIVATE METHODS
    /////////////////////////////////////

    /**
     * Initialize
     *
     * @param context current context
     * @param attrs   custom attributes
     */
    private void init(Context context, @Nullable AttributeSet attrs) {
        //initialize default component
        Drawable icon = null;
        Drawable shape = null;
        String title = "Title";
        int colorActive = ViewUtils.getThemeAccentColor(context);
        int colorInactive = ContextCompat.getColor(context, R.color.default_inactive_color);
        float titleSize = context.getResources().getDimension(R.dimen.default_nav_item_text_size);
        maxTitleWidth = context.getResources().getDimension(R.dimen.default_nav_item_title_max_width);
        float iconWidth = context.getResources().getDimension(R.dimen.default_icon_size);
        float iconHeight = context.getResources().getDimension(R.dimen.default_icon_size);
        int internalPadding = (int) context.getResources().getDimension(R.dimen.default_nav_item_padding);

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BubbleToggleView, 0, 0);
            try {
                icon = ta.getDrawable(R.styleable.BubbleToggleView_bt_icon);
                iconWidth = ta.getDimension(R.styleable.BubbleToggleView_bt_iconWidth, iconWidth);
                iconHeight = ta.getDimension(R.styleable.BubbleToggleView_bt_iconHeight, iconHeight);
                shape = ta.getDrawable(R.styleable.BubbleToggleView_bt_shape);
                showShapeAlways = ta.getBoolean(R.styleable.BubbleToggleView_bt_showShapeAlways, false);
                title = ta.getString(R.styleable.BubbleToggleView_bt_title);
                titleSize = ta.getDimension(R.styleable.BubbleToggleView_bt_titleSize, titleSize);
                colorActive = ta.getColor(R.styleable.BubbleToggleView_bt_colorActive, colorActive);
                colorInactive = ta.getColor(R.styleable.BubbleToggleView_bt_colorInactive, colorInactive);
                isActive = ta.getBoolean(R.styleable.BubbleToggleView_bt_active, false);
                animationDuration = ta.getInteger(R.styleable.BubbleToggleView_bt_duration, DEFAULT_ANIM_DURATION);
                internalPadding = (int) ta.getDimension(R.styleable.BubbleToggleView_bt_padding, internalPadding);
            } finally {
                ta.recycle();
            }
        }

        //set the default icon
        if (icon == null)
            icon = ContextCompat.getDrawable(context, R.drawable.default_icon);

        //set the default shape
        if (shape == null)
            shape = ContextCompat.getDrawable(context, R.drawable.transition_background_drawable);

        //create a default bubble item
        bubbleToggleItem = new BubbleToggleItem();
        bubbleToggleItem.setIcon(icon);
        bubbleToggleItem.setShape(shape);
        bubbleToggleItem.setTitle(title);
        bubbleToggleItem.setTitleSize(titleSize);
        bubbleToggleItem.setColorActive(colorActive);
        bubbleToggleItem.setColorInactive(colorInactive);
        bubbleToggleItem.setIconWidth(iconWidth);
        bubbleToggleItem.setIconHeight(iconHeight);

        //set the orientation
        setOrientation(HORIZONTAL);
        //set the gravity
        setGravity(Gravity.CENTER);
        //set the internal padding
        setPadding(internalPadding, internalPadding, internalPadding, internalPadding);

        createBubbleItemView(context);
        setInitialState(isActive);
    }

    /**
     * Create the components of the bubble item view {@link #iconView} and {@link #titleView}
     *
     * @param context current context
     */
    private void createBubbleItemView(Context context) {

        //create the nav icon
        iconView = new ImageView(context);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams((int) bubbleToggleItem.getIconWidth(), (int) bubbleToggleItem.getIconHeight());
        iconView.setLayoutParams(lp);
        iconView.setImageDrawable(bubbleToggleItem.getIcon());

        //create the nav title
        titleView = new TextView(context);
        titleView.setSingleLine(true);
        titleView.setTextColor(bubbleToggleItem.getColorActive());
        titleView.setText(bubbleToggleItem.getTitle());
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, bubbleToggleItem.getTitleSize());

        //get the current measured title width
        titleView.setVisibility(VISIBLE);
        //update the margin of the text view
        int padding = (int) context.getResources().getDimension(R.dimen.default_nav_item_text_margin);
        titleView.setPadding(padding, 0, padding, 0);
        //measure the content width
        titleView.measure(0, 0);       //must call measure!
        measuredTitleWidth = titleView.getMeasuredWidth();  //get width
        //limit measured width, based on the max width
        if (measuredTitleWidth > maxTitleWidth)
            measuredTitleWidth = maxTitleWidth;

        //change the visibility
        titleView.setVisibility(GONE);

        addView(iconView);
        addView(titleView);

        //set the initial state
        setInitialState(isActive);
    }

    /**
     * Updates the Initial State
     *
     * @param isActive current state
     */
    public void setInitialState(boolean isActive) {
        //set the background
        setBackground(bubbleToggleItem.getShape());

        if (isActive) {
            ViewUtils.updateDrawableColor(iconView.getDrawable(), bubbleToggleItem.getColorActive());
            this.isActive = true;
            titleView.setVisibility(VISIBLE);
            if (getBackground() instanceof TransitionDrawable) {
                TransitionDrawable trans = (TransitionDrawable) getBackground();
                trans.startTransition(0);
            }
        } else {
            ViewUtils.updateDrawableColor(iconView.getDrawable(), bubbleToggleItem.getColorInactive());
            this.isActive = false;
            titleView.setVisibility(GONE);
            if (!showShapeAlways) {
                if (!(getBackground() instanceof TransitionDrawable)) {
                    setBackground(null);
                }
            }
        }
    }

    /////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////

    /**
     * Toggles between Active and Inactive state
     */
    public void toggle() {
        if (!isActive)
            activate();
        else
            deactivate();
    }

    /**
     * Set Active state
     */
    public void activate() {
        ViewUtils.updateDrawableColor(iconView.getDrawable(), bubbleToggleItem.getColorActive());
        isActive = true;
        titleView.setVisibility(VISIBLE);
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(animationDuration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                titleView.setWidth((int) (measuredTitleWidth * value));
                //end of animation
                if (value >= 1.0f) {
                    //do something
                }
            }
        });
        animator.start();

        if (getBackground() instanceof TransitionDrawable) {
            TransitionDrawable trans = (TransitionDrawable) getBackground();
            trans.startTransition(animationDuration);
        } else {
            setBackground(bubbleToggleItem.getShape());
        }
    }

    /**
     * Set Inactive State
     */
    public void deactivate() {
        ViewUtils.updateDrawableColor(iconView.getDrawable(), bubbleToggleItem.getColorInactive());
        isActive = false;
        ValueAnimator animator = ValueAnimator.ofFloat(1f, 0f);
        animator.setDuration(animationDuration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                titleView.setWidth((int) (measuredTitleWidth * value));
                //end of animation
                if (value <= 0.0f)
                    titleView.setVisibility(GONE);
            }
        });
        animator.start();

        if (getBackground() instanceof TransitionDrawable) {
            TransitionDrawable trans = (TransitionDrawable) getBackground();
            trans.reverseTransition(animationDuration);
        } else {
            if (!showShapeAlways) setBackground(null);
        }
    }

    /**
     * Get the current state of the view
     *
     * @return the current state
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets the {@link Typeface} of the {@link #titleView}
     *
     * @param typeface to be used
     */
    public void setTitleTypeface(Typeface typeface) {
        titleView.setTypeface(typeface);
    }

    /**
     * Updates the measurements and fits the view
     *
     * @param maxWidth in pixels
     */
    public void updateMeasurements(int maxWidth) {
        int marginLeft = 0, marginRight = 0;
        ViewGroup.LayoutParams titleViewLayoutParams = titleView.getLayoutParams();
        if (titleViewLayoutParams instanceof LayoutParams) {
            marginLeft = ((LayoutParams) titleViewLayoutParams).getMarginStart();
            marginRight = ((LayoutParams) titleViewLayoutParams).getMarginEnd();
        }

        int newTitleWidth = maxWidth
                - (getPaddingStart() + getPaddingEnd())
                - (marginLeft + marginRight)
                - ((int) bubbleToggleItem.getIconWidth())
                + titleView.getPaddingStart() + titleView.getPaddingEnd();

        //if the new calculate title width is less than current one, update the titleView specs
        if (newTitleWidth > 0 && newTitleWidth < measuredTitleWidth) {
            measuredTitleWidth = titleView.getMeasuredWidth();
        }
    }

}
