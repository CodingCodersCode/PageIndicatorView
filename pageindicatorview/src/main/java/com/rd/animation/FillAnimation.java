package com.rd.animation;

import android.animation.IntEvaluator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.view.animation.DecelerateInterpolator;

public class FillAnimation extends ColorAnimation {

    private static final String ANIMATION_COLOR_REVERSE = "ANIMATION_COLOR_REVERSE";
    private static final String ANIMATION_COLOR = "ANIMATION_COLOR";

    private static final String ANIMATION_STROKE_REVERSE = "ANIMATION_STROKE_REVERSE";
    private static final String ANIMATION_STROKE = "ANIMATION_STROKE";

    private static final int ANIMATION_DURATION = 350;

    private int radiusPx;
    private int strokePx;

    public FillAnimation(@NonNull ValueAnimation.UpdateListener listener) {
        super(listener);
    }

    @NonNull
    @Override
    public ValueAnimator createAnimator() {
        ValueAnimator animator = new ValueAnimator();
        animator.setDuration(ANIMATION_DURATION);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                onAnimateUpdated(animation);
            }
        });

        return animator;
    }

    @NonNull
    public FillAnimation with(int colorStartValue, int colorEndValue, int radiusValue, int strokeValue) {
        if (animator != null && hasChanges(colorStartValue, colorEndValue, radiusValue, strokeValue)) {

            startColor = colorStartValue;
            endColor = colorEndValue;
            radiusPx = radiusValue;
            strokePx = strokeValue;

            PropertyValuesHolder colorHolder = createColorPropertyHolder(false);
            PropertyValuesHolder reverseColorHolder = createColorPropertyHolder(true);

            PropertyValuesHolder strokeHolder = createStrokePropertyHolder(false);
            PropertyValuesHolder strokeReverseHolder = createStrokePropertyHolder(true);

            animator.setValues(colorHolder, reverseColorHolder, strokeHolder, strokeReverseHolder);
        }

        return this;
    }

    @NonNull
    private PropertyValuesHolder createStrokePropertyHolder(boolean isReverse) {
        String propertyName;
        int startStrokeValue;
        int endStrokeValue;

        if (isReverse) {
            propertyName = ANIMATION_STROKE_REVERSE;
            startStrokeValue = radiusPx * 2;
            endStrokeValue = 0;
        } else {
            propertyName = ANIMATION_STROKE;
            startStrokeValue = 0;
            endStrokeValue = radiusPx * 2;
        }

        PropertyValuesHolder holder = PropertyValuesHolder.ofInt(propertyName, startStrokeValue, endStrokeValue);
        holder.setEvaluator(new IntEvaluator());

        return holder;
    }

    private void onAnimateUpdated(@NonNull ValueAnimator animation) {
        int color = (int) animation.getAnimatedValue(ANIMATION_COLOR);
        int colorReverse = (int) animation.getAnimatedValue(ANIMATION_COLOR_REVERSE);

        int stroke = (int) animation.getAnimatedValue(ANIMATION_STROKE);
        int strokeReverse = (int) animation.getAnimatedValue(ANIMATION_STROKE_REVERSE);

        if (listener != null) {
            listener.onFillAnimationUpdated(color, colorReverse, stroke, strokeReverse);
        }
    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean hasChanges(int colorStartValue, int colorEndValue, int radiusValue, int strokeValue) {
        if (startColor != colorStartValue) {
            return true;
        }

        if (endColor != colorEndValue) {
            return true;
        }

        if (radiusPx != radiusValue) {
            return true;
        }

        if (strokePx != strokeValue) {
            return true;
        }

        return false;
    }
}
