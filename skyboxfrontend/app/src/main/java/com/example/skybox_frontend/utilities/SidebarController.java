package com.example.skybox_frontend.utilities;

import android.animation.ValueAnimator;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

/**
 * Manages sidebar dragging and snapping behavior.
 */
public class SidebarController {

    // <editor-fold desc="Members">
    private final View sidebar;
    private final View mainContent;
    private final int maxSidebarWidth;
    private final int animationDuration;

    private boolean isDragging = false;
    private boolean sidebarOpen = false;
    private float lastTouchX;
    // </editor-fold>

    // <editor-fold desc="Constructor">
    public SidebarController(View sidebar, View mainContent, int maxSidebarWidth, int animationDuration) {
        this.sidebar = sidebar;
        this.mainContent = mainContent;
        this.maxSidebarWidth = maxSidebarWidth;
        this.animationDuration = animationDuration;
    }
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    public void attachTouchGesture(View touchTarget) {
        touchTarget.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastTouchX = event.getX();
                    isDragging = false;
                    return true;

                case MotionEvent.ACTION_MOVE:
                    float deltaX = event.getX() - lastTouchX;

                    if (Math.abs(deltaX) > ViewConfiguration.get(touchTarget.getContext()).getScaledTouchSlop()) {
                        isDragging = true;

                        int newWidth = sidebar.getLayoutParams().width + (int) deltaX;
                        newWidth = Math.max(0, Math.min(maxSidebarWidth, newWidth));

                        resizeSidebarInstant(newWidth);
                        lastTouchX = event.getX();
                    }
                    return true;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (isDragging) {
                        boolean shouldOpen = sidebar.getLayoutParams().width > maxSidebarWidth / 2;
                        animateSidebar(shouldOpen);
                        return true;
                    }
                    return false;

                default:
                    return false;
            }
        });
    }

    public void animateOpen() {
        animateSidebar(true);
    }

    public void animateClose() {
        animateSidebar(false);
    }
    // </editor-fold>

    // <editor-fold desc="Private Helpers">
    private void animateSidebar(boolean open) {
        int start = sidebar.getLayoutParams().width;
        int end = open ? maxSidebarWidth : 0;

        sidebar.setVisibility(View.VISIBLE);

        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(animationDuration);
        animator.addUpdateListener(animation -> {
            int newWidth = (int) animation.getAnimatedValue();
            resizeSidebarInstant(newWidth);
        });

        animator.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                if (!open) {
                    sidebar.setVisibility(View.GONE);
                }
                sidebarOpen = open;
            }
        });

        animator.start();
    }

    private void resizeSidebarInstant(int widthPx) {
        sidebar.setVisibility(View.VISIBLE);

        LinearLayout.LayoutParams menuParams = (LinearLayout.LayoutParams) sidebar.getLayoutParams();
        menuParams.width = widthPx;
        menuParams.weight = 0;
        sidebar.setLayoutParams(menuParams);

        LinearLayout.LayoutParams contentParams = (LinearLayout.LayoutParams) mainContent.getLayoutParams();
        contentParams.width = 0;
        contentParams.weight = 1;
        mainContent.setLayoutParams(contentParams);
    }
    // </editor-fold>
}
