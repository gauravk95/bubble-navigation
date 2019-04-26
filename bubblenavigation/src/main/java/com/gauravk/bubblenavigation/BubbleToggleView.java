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
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gauravk.bubblenavigation.util.ViewUtils;

/**
 * BubbleToggleView
 *
 * @author Gaurav Kumar
 */
public class BubbleToggleView extends RelativeLayout {

    private static final String TAG = "BNI_View";
    private static final int DEFAULT_ANIM_DURATION = 300;

    private BubbleToggleItem bubbleToggleItem;

    private boolean isActive = false;

    private ImageView iconView;
    private TextView titleView;
    private TextView badgeView;

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
        String title = "Title";
        Drawable icon = null;
        Drawable shape = null;
        int shapeColor = Integer.MIN_VALUE;
        int colorActive = ViewUtils.getThemeAccentColor(context);
        int colorInactive = ContextCompat.getColor(context, R.color.default_inactive_color);
        float titleSize = context.getResources().getDimension(R.dimen.default_nav_item_text_size);
        maxTitleWidth = context.getResources().getDimension(R.dimen.default_nav_item_title_max_width);
        float iconWidth = context.getResources().getDimension(R.dimen.default_icon_size);
        float iconHeight = context.getResources().getDimension(R.dimen.default_icon_size);
        int internalPadding = (int) context.getResources().getDimension(R.dimen.default_nav_item_padding);
        int titlePadding = (int) context.getResources().getDimension(R.dimen.default_nav_item_text_padding);

        int badgeTextSize = (int) context.getResources().getDimension(R.dimen.default_nav_item_badge_text_size);
        int badgeBackgroundColor = ContextCompat.getColor(context, R.color.default_badge_background_color);
        int badgeTextColor = ContextCompat.getColor(context, R.color.default_badge_text_color);
        String badgeText = null;

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BubbleToggleView, 0, 0);
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    icon = ta.getDrawable(R.styleable.BubbleToggleView_bt_icon);
                else
                    icon = AppCompatResources.getDrawable(getContext(), ta.getResourceId(R.styleable.BubbleToggleView_bt_icon, R.drawable.default_icon));
                iconWidth = ta.getDimension(R.styleable.BubbleToggleView_bt_iconWidth, iconWidth);
                iconHeight = ta.getDimension(R.styleable.BubbleToggleView_bt_iconHeight, iconHeight);
                shape = ta.getDrawable(R.styleable.BubbleToggleView_bt_shape);
                shapeColor = ta.getColor(R.styleable.BubbleToggleView_bt_shapeColor, shapeColor);
                showShapeAlways = ta.getBoolean(R.styleable.BubbleToggleView_bt_showShapeAlways, false);
                title = ta.getString(R.styleable.BubbleToggleView_bt_title);
                titleSize = ta.getDimension(R.styleable.BubbleToggleView_bt_titleSize, titleSize);
                colorActive = ta.getColor(R.styleable.BubbleToggleView_bt_colorActive, colorActive);
                colorInactive = ta.getColor(R.styleable.BubbleToggleView_bt_colorInactive, colorInactive);
                isActive = ta.getBoolean(R.styleable.BubbleToggleView_bt_active, false);
                animationDuration = ta.getInteger(R.styleable.BubbleToggleView_bt_duration, DEFAULT_ANIM_DURATION);
                internalPadding = (int) ta.getDimension(R.styleable.BubbleToggleView_bt_padding, internalPadding);
                titlePadding = (int) ta.getDimension(R.styleable.BubbleToggleView_bt_titlePadding, titlePadding);
                badgeTextSize = (int) ta.getDimension(R.styleable.BubbleToggleView_bt_badgeTextSize, badgeTextSize);
                badgeBackgroundColor = ta.getColor(R.styleable.BubbleToggleView_bt_badgeBackgroundColor, badgeBackgroundColor);
                badgeTextColor = ta.getColor(R.styleable.BubbleToggleView_bt_badgeTextColor, badgeTextColor);
                badgeText = ta.getString(R.styleable.BubbleToggleView_bt_badgeText);
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
        bubbleToggleItem.setTitlePadding(titlePadding);
        bubbleToggleItem.setShapeColor(shapeColor);
        bubbleToggleItem.setColorActive(colorActive);
        bubbleToggleItem.setColorInactive(colorInactive);
        bubbleToggleItem.setIconWidth(iconWidth);
        bubbleToggleItem.setIconHeight(iconHeight);
        bubbleToggleItem.setInternalPadding(internalPadding);
        bubbleToggleItem.setBadgeText(badgeText);
        bubbleToggleItem.setBadgeBackgroundColor(badgeBackgroundColor);
        bubbleToggleItem.setBadgeTextColor(badgeTextColor);
        bubbleToggleItem.setBadgeTextSize(badgeTextSize);

        //set the gravity
        setGravity(Gravity.CENTER);

        //set the internal padding
        setPadding(
                bubbleToggleItem.getInternalPadding(),
                bubbleToggleItem.getInternalPadding(),
                bubbleToggleItem.getInternalPadding(),
                bubbleToggleItem.getInternalPadding());
        post(new Runnable() {
            @Override
            public void run() {
                //make sure the padding is added
                setPadding(
                        bubbleToggleItem.getInternalPadding(),
                        bubbleToggleItem.getInternalPadding(),
                        bubbleToggleItem.getInternalPadding(),
                        bubbleToggleItem.getInternalPadding());
            }
        });

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
        iconView.setId(ViewCompat.generateViewId());
        LayoutParams lpIcon = new LayoutParams((int) bubbleToggleItem.getIconWidth(), (int) bubbleToggleItem.getIconHeight());
        lpIcon.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        iconView.setLayoutParams(lpIcon);
        iconView.setImageDrawable(bubbleToggleItem.getIcon());

        //create the nav title
        titleView = new TextView(context);
        LayoutParams lpTitle = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lpTitle.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            lpTitle.addRule(RelativeLayout.END_OF, iconView.getId());
        else
            lpTitle.addRule(RelativeLayout.RIGHT_OF, iconView.getId());
        titleView.setLayoutParams(lpTitle);
        titleView.setSingleLine(true);
        titleView.setTextColor(bubbleToggleItem.getColorActive());
        titleView.setText(bubbleToggleItem.getTitle());
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, bubbleToggleItem.getTitleSize());
        //get the current measured title width
        titleView.setVisibility(VISIBLE);
        //update the margin of the text view
        titleView.setPadding(bubbleToggleItem.getTitlePadding(), 0, bubbleToggleItem.getTitlePadding(), 0);
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

        updateBadge(context);

        //set the initial state
        setInitialState(isActive);
    }

    /**
     * Adds or removes the badge
     */
    private void updateBadge(Context context) {

        //remove the previous badge view
        if (badgeView != null)
            removeView(badgeView);

        if (bubbleToggleItem.getBadgeText() == null)
            return;

        //create badge
        badgeView = new TextView(context);
        LayoutParams lpBadge = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lpBadge.addRule(RelativeLayout.ALIGN_TOP, iconView.getId());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            lpBadge.addRule(RelativeLayout.ALIGN_END, iconView.getId());
        } else
            lpBadge.addRule(RelativeLayout.ALIGN_RIGHT, iconView.getId());
        badgeView.setLayoutParams(lpBadge);
        badgeView.setSingleLine(true);
        badgeView.setTextColor(bubbleToggleItem.getBadgeTextColor());
        badgeView.setText(bubbleToggleItem.getBadgeText());
        badgeView.setTextSize(TypedValue.COMPLEX_UNIT_PX, bubbleToggleItem.getBadgeTextSize());
        badgeView.setGravity(Gravity.CENTER);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.badge_background_white);
        ViewUtils.updateDrawableColor(drawable, bubbleToggleItem.getBadgeBackgroundColor());
        badgeView.setBackground(drawable);
        int badgePadding = (int) context.getResources().getDimension(R.dimen.default_nav_item_badge_padding);
        //update the margin of the text view
        badgeView.setPadding(badgePadding, 0, badgePadding, 0);
        //measure the content width
        badgeView.measure(0, 0);
        if (badgeView.getMeasuredWidth() < badgeView.getMeasuredHeight())
            badgeView.setWidth(badgeView.getMeasuredHeight());

        addView(badgeView);
    }

    /////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////

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
            } else {
                if (!showShapeAlways && bubbleToggleItem.getShapeColor() != Integer.MIN_VALUE)
                    ViewUtils.updateDrawableColor(bubbleToggleItem.getShape(), bubbleToggleItem.getShapeColor());
            }
        } else {
            ViewUtils.updateDrawableColor(iconView.getDrawable(), bubbleToggleItem.getColorInactive());
            this.isActive = false;
            titleView.setVisibility(GONE);
            if (!showShapeAlways) {
                if (!(getBackground() instanceof TransitionDrawable)) {
                    setBackground(null);
                } else {
                    TransitionDrawable trans = (TransitionDrawable) getBackground();
                    trans.resetTransition();
                }
            }
        }
    }

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
            //if not showing Shape Always and valid shape color present, use that as tint
            if (!showShapeAlways && bubbleToggleItem.getShapeColor() != Integer.MIN_VALUE)
                ViewUtils.updateDrawableColor(bubbleToggleItem.getShape(), bubbleToggleItem.getShapeColor());
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
            marginLeft = ((LayoutParams) titleViewLayoutParams).rightMargin;
            marginRight = ((LayoutParams) titleViewLayoutParams).leftMargin;
        }

        int newTitleWidth = maxWidth
                - (getPaddingRight() + getPaddingLeft())
                - (marginLeft + marginRight)
                - ((int) bubbleToggleItem.getIconWidth())
                + titleView.getPaddingRight() + titleView.getPaddingLeft();

        //if the new calculate title width is less than current one, update the titleView specs
        if (newTitleWidth > 0 && newTitleWidth < measuredTitleWidth) {
            measuredTitleWidth = titleView.getMeasuredWidth();
        }
    }

    /**
     * Set value to the Badge's
     *
     * @param value as String, null to hide
     */
    public void setBadgeText(String value) {
        bubbleToggleItem.setBadgeText(value);
        updateBadge(getContext());
    }

}
