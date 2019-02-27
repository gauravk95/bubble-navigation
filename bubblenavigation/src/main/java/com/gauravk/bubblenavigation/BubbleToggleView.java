package com.gauravk.bubblenavigation;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gauravk.bubblenavigation.util.ViewUtils;

public class BubbleToggleView extends LinearLayout {

    private static final String TAG = "BNI_View";
    private static final int DEFAULT_ANIM_DURATION = 300;

    private BubbleToggleItem bubbleToggleItem;

    private boolean isActive = false;
    private boolean isFirstItem = false;

    private ImageView iconView;
    private TextView titleView;

    private int animationDuration;
    private boolean showShapeAlways;

    private float maxTitleWidth;
    private int measuredTitleWidth = 200;

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

    /////////////////////////////////
    // PRIVATE METHODS
    ////////////////////////////////
    private void init(Context context, @Nullable AttributeSet attrs) {
        //initialize default component
        Drawable icon = null;
        Drawable shape = null;
        int shapeColor = ContextCompat.getColor(context, R.color.default_inactive_shape_color);
        String title = "Title";
        int colorActive = ViewUtils.getThemeAccentColor(context);
        int colorInactive = ContextCompat.getColor(context, R.color.default_inactive_color);
        float titleSize = context.getResources().getDimension(R.dimen.default_nav_item_text_size);
        maxTitleWidth = context.getResources().getDimension(R.dimen.default_nav_item_title_max_width);

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BubbleToggleView, 0, 0);
            try {
                icon = ta.getDrawable(R.styleable.BubbleToggleView_bb_icon);
                shape = ta.getDrawable(R.styleable.BubbleToggleView_bb_shape);
                shapeColor = ta.getColor(R.styleable.BubbleToggleView_bb_shapeColor, shapeColor);
                showShapeAlways = ta.getBoolean(R.styleable.BubbleToggleView_bb_showShapeAlways, false);
                title = ta.getString(R.styleable.BubbleToggleView_bb_title);
                titleSize = ta.getDimension(R.styleable.BubbleToggleView_bb_titleSize, titleSize);
                colorActive = ta.getColor(R.styleable.BubbleToggleView_bb_colorActive, colorActive);
                colorInactive = ta.getColor(R.styleable.BubbleToggleView_bb_colorInactive, colorInactive);
                isActive = ta.getBoolean(R.styleable.BubbleToggleView_bb_active, false);
                animationDuration = ta.getInteger(R.styleable.BubbleToggleView_bb_duration, DEFAULT_ANIM_DURATION);
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
        ViewUtils.updateDrawableColor(shape, shapeColor);

        //create a default bubble item
        bubbleToggleItem = new BubbleToggleItem();
        bubbleToggleItem.setIcon(icon);
        bubbleToggleItem.setShape(shape);
        bubbleToggleItem.setTitle(title);
        bubbleToggleItem.setTitleSize(titleSize);
        bubbleToggleItem.setColorActive(colorActive);
        bubbleToggleItem.setColorInactive(colorInactive);

        //set the orientation
        setOrientation(HORIZONTAL);
        //set the gravity
        setGravity(Gravity.CENTER);
        //set the internal padding
        int padding = (int) context.getResources().getDimension(R.dimen.default_nav_item_padding);
        setPadding(padding, padding, padding, padding);

        createNavItemView(context);
        setInitialState(isActive);
    }

    private void createNavItemView(Context context) {

        //create the nav icon
        iconView = new ImageView(context);
        iconView.setImageDrawable(bubbleToggleItem.getIcon());

        //create the nav title
        titleView = new TextView(context);
        titleView.setSingleLine(true);
        titleView.setTextColor(bubbleToggleItem.getColorActive());
        titleView.setText(bubbleToggleItem.getTitle());
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, bubbleToggleItem.getTitleSize());
        titleView.setVisibility(GONE);
        //update the margin of the text view
        int paddingLeft = (int) context.getResources().getDimension(R.dimen.default_nav_item_text_margin);
        titleView.setPadding(paddingLeft, 0, 0, 0);
        //TODO: set typeface

        addView(iconView);
        addView(titleView);

        //set proper initial state
        //setInitialState();
    }

    public void setInitialState(boolean isActive) {

        //set the background
        setBackground(bubbleToggleItem.getShape());

        if (isActive) {
            ViewUtils.updateDrawableColor(iconView.getDrawable(), bubbleToggleItem.getColorActive());
            this.isActive = true;
            titleView.setVisibility(VISIBLE);
            if (!showShapeAlways) {
                if (getBackground() instanceof TransitionDrawable) {
                    TransitionDrawable trans = (TransitionDrawable) getBackground();
                    trans.startTransition(0);
                }
            }
        } else {
            ViewUtils.updateDrawableColor(iconView.getDrawable(), bubbleToggleItem.getColorInactive());
            this.isActive = false;
            titleView.setVisibility(GONE);
            if (!showShapeAlways) {
                if (getBackground() instanceof TransitionDrawable) {
                    TransitionDrawable trans = (TransitionDrawable) getBackground();
                    trans.reverseTransition(0);
                } else
                    setBackground(null);
            }
        }
    }

    /////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////
    public void toggle() {
        if (!isActive)
            activate();
        else
            deactivate();
    }

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

        if (!showShapeAlways) {
            if (getBackground() instanceof TransitionDrawable) {
                TransitionDrawable trans = (TransitionDrawable) getBackground();
                trans.startTransition(animationDuration);
            } else
                setBackground(bubbleToggleItem.getShape());
        }
    }

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

        if (!showShapeAlways) {
            if (getBackground() instanceof TransitionDrawable) {
                TransitionDrawable trans = (TransitionDrawable) getBackground();
                trans.reverseTransition(animationDuration);
            } else
                setBackground(null);
        }
    }

    public boolean isActive() {
        return isActive;
    }
}
