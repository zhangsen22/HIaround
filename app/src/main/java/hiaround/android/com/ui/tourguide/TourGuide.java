package hiaround.android.com.ui.tourguide;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import hiaround.android.com.R;


/**
 * Created by tanjunrong on 2/10/15.
 */
public class TourGuide {
    private float mMarg = 10;
    private int pointLocationX;
    private int pointLocationY;
    private ToolTip toolTip = new ToolTip();
    private Overlay overlay = new Overlay();

    public TourGuide setOverlayBackgroundColor(int overlayBackgroundColor) {
        overlay.setBackgroundColor(overlayBackgroundColor);
        return this;
    }

    public TourGuide overlayDisableClick(boolean b) {
        overlay.disableClick(b);
        return this;
    }

    public TourGuide overlayDisableClickThroughHole(boolean b) {
        overlay.disableClickThroughHole(b);
        return this;
    }

    public TourGuide setOverlayStyle(Overlay.Style overlayStyle) {
        overlay.setStyle(overlayStyle);
        return this;
    }

    public TourGuide setOverlayOnClickListener(View.OnClickListener onClickListener) {
        overlay.setOnClickListener(onClickListener);
        return this;
    }

    public TourGuide setOverlayBackgroundBitmap(Bitmap overlayBackgroundBitmap) {
        overlay.setBackgroundBitmap(overlayBackgroundBitmap);
        return this;
    }

    public TourGuide setToolTipDescriptionTextColor(int color) {
        toolTip.setToolTipDescriptionTextColor(color);
        return this;
    }

    public TourGuide setToolTip3LineColor(int toolTip3LineColor) {
        toolTip.setToolTip3LineColor(toolTip3LineColor);
        return this;
    }

    /**
     * This describes the animation techniques
     */
    public enum Technique {
        Click, HorizontalLeft, HorizontalRight, VerticalUpward, VerticalDownward
    }

    /**
     * This describes the allowable motion, for example if you want the users to learn about clicking, but want to stop them from swiping, then use ClickOnly
     */
    public enum MotionType {
        AllowAll, ClickOnly, SwipeOnly
    }

    protected Technique mTechnique;
    protected View mHighlightedView;
    private Activity mActivity;
    protected MotionType mMotionType;
    protected FrameLayoutWithHole mFrameLayout;
    private ArrowTowardsLayout mToolTipViewGroup;
    public ToolTip mToolTip;
    public Pointer mPointer;
    public Overlay mOverlay;

    /*************
     *
     * Public API
     *
     *************/

    /* Static builder */
    public static TourGuide init(Activity activity) {
        return new TourGuide(activity);
    }

    /* Constructor */
    public TourGuide(Activity activity) {
        mActivity = activity;
        setToolTip(toolTip);
        setOverlay(overlay);
    }

    /**
     * Setter for the animation to be used
     *
     * @param technique Animation to be used
     * @return return TourGuide instance for chaining purpose
     */
    public TourGuide with(Technique technique) {
        mTechnique = technique;
        return this;
    }

    /**
     * Sets which motion type is motionType
     *
     * @param motionType
     * @return return TourGuide instance for chaining purpose
     */
    public TourGuide motionType(MotionType motionType) {
        mMotionType = motionType;
        return this;
    }

    /**
     * Sets the targeted view for TourGuide to play on
     *
     * @param targetView the view in which the tutorial button will be placed on top of
     * @return return TourGuide instance for chaining purpose
     */
    public TourGuide playOn(View targetView) {
        mHighlightedView = targetView;
        setupView();
        return this;
    }

    public TourGuide setToolTipGravity(int gravity) {
        toolTip.setGravity(gravity);
        return this;
    }

    public TourGuide setToolTipButtonText(String string) {
        toolTip.setButtonText(string);
        return this;
    }

    public TourGuide setToolTipMarginLeft(int marginLeft) {
        toolTip.setMarginLeft(marginLeft);
        return this;
    }

    public TourGuide setToolTipShadow(boolean b) {
        toolTip.setShadow(b);
        return this;
    }

    public TourGuide setToolTipDescription(String s) {
        toolTip.setDescription(s);
        return this;
    }

    public TourGuide setToolTipDescriptionBackgroundId(int s) {
        toolTip.setDescriptionBackgroundColor(s);
        return this;
    }

    public TourGuide setToolTipsetOverlayWidth(int width) {
        toolTip.setOverlayWidth(width);
        return this;
    }

    /**
     * Sets the overlay
     *
     * @param overlay this overlay object should contain the attributes of the overlay, such as background color, animation, Style, etc
     * @return return TourGuide instance for chaining purpose
     */
    public TourGuide setOverlay(Overlay overlay) {
        mOverlay = overlay;
        return this;
    }

    /**
     * Set the toolTip
     *
     * @param toolTip this toolTip object should contain the attributes of the ToolTip, such as, the title text, and the description text, background color, etc
     * @return return TourGuide instance for chaining purpose
     */
    public TourGuide setToolTip(ToolTip toolTip) {
        mToolTip = toolTip;
        return this;
    }

    /**
     * Set the Pointer
     *
     * @param pointer this pointer object should contain the attributes of the Pointer, such as the pointer color, pointer gravity, etc, refer to @Link{pointer}
     * @return return TourGuide instance for chaining purpose
     */
    public TourGuide setPointer(Pointer pointer) {
        mPointer = pointer;
        return this;
    }

    /**
     * Clean up the tutorial that is added to the activity
     */
    public void cleanUp() {
        try {
            mFrameLayout.cleanUp();
            if (mToolTipViewGroup != null) {
                ((ViewGroup) mActivity.getWindow().getDecorView()).removeView(mToolTipViewGroup);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @return FrameLayoutWithHole that is used as overlay
     */
    public FrameLayoutWithHole getOverlay() {
        return mFrameLayout;
    }

    /**
     * @return the ToolTip container View
     */
    public View getToolTip() {
        return mToolTipViewGroup;
    }

    /******
     *
     * Private methods
     *
     *******/
    private int getXBasedOnGravity(int width) {
        int[] pos = new int[2];
        mHighlightedView.getLocationOnScreen(pos);
        int x = pos[0];
        if ((mPointer.mGravity & Gravity.RIGHT) == Gravity.RIGHT) {
            return x + mHighlightedView.getWidth() - width;
        } else if ((mPointer.mGravity & Gravity.LEFT) == Gravity.LEFT) {
            return x;
        } else { // this is center
            return x + mHighlightedView.getWidth() / 2 - width / 2;
        }
    }

    private int getYBasedOnGravity(int height) {
        int[] pos = new int[2];
        mHighlightedView.getLocationInWindow(pos);
        int y = pos[1];
        if ((mPointer.mGravity & Gravity.BOTTOM) == Gravity.BOTTOM) {
            return y + mHighlightedView.getHeight() - height;
        } else if ((mPointer.mGravity & Gravity.TOP) == Gravity.TOP) {
            return y;
        } else { // this is center
            return y + mHighlightedView.getHeight() / 2 - height / 2;
        }
    }

    protected void setupView() {
        // TourGuide can only be setup after all the views is ready and obtain it's position/measurement
        // so when this is the 1st time TourGuide is being added,
        // else block will be executed, and ViewTreeObserver will make TourGuide setup process to be delayed until everything is ready
        // when this is run the 2nd or more times, if block will be executed
        if (ViewCompat.isAttachedToWindow(mHighlightedView)) {
            startView();
        } else {
            final ViewTreeObserver viewTreeObserver = mHighlightedView.getViewTreeObserver();
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        //noinspection deprecation
                        mHighlightedView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        mHighlightedView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    startView();
                }
            });
        }
    }

    private void startView() {
        /* Initialize a frame layout with a hole */
        mFrameLayout = new FrameLayoutWithHole(mActivity, mHighlightedView, mMotionType, mOverlay);
        /* handle click disable */
        handleDisableClicking(mFrameLayout);

        setupFrameLayout();
        /* setup tooltip view */
        setupToolTip();

        //计算对应点在屏幕上的坐标，上下方向不计算Y，左右不计算X
        int[] location = new int[2];
        mHighlightedView.getLocationOnScreen(location);
        if (mToolTip.mGravity == Gravity.TOP || mToolTip.mGravity == Gravity.BOTTOM) {
            pointLocationX = mHighlightedView.getWidth() / 2 + location[0];
        } else {
            pointLocationY = mHighlightedView.getHeight() / 2 + location[1];
        }
    }

    private void handleDisableClicking(FrameLayoutWithHole frameLayoutWithHole) {
        // 1. if user provides an overlay listener, use that as 1st priority
        if (mOverlay != null && mOverlay.mOnClickListener != null) {
            frameLayoutWithHole.setClickable(true);
            frameLayoutWithHole.setOnClickListener(mOverlay.mOnClickListener);
        }
        // 2. if overlay listener is not provided, check if it's disabled
        else if (mOverlay != null && mOverlay.mDisableClick) {
            frameLayoutWithHole.setViewHole(mHighlightedView);
            frameLayoutWithHole.setSoundEffectsEnabled(false);
            frameLayoutWithHole.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                } // do nothing, disabled.
            });
        }
    }

    private void setupToolTip() {
        final FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        if (mToolTip != null) {
            /* inflate and get views */
            ViewGroup parent = (ViewGroup) mActivity.getWindow().getDecorView();
            LayoutInflater layoutInflater = mActivity.getLayoutInflater();

            if (mToolTip.getCustomView() == null) {
                mToolTipViewGroup = (ArrowTowardsLayout) layoutInflater.inflate(R.layout.tooltip, null);
                View toolTipContainer = mToolTipViewGroup.findViewById(R.id.toolTip_container);
                TextView toolTipTitleTV = (TextView) mToolTipViewGroup.findViewById(R.id.title);
                TextView toolTipDescriptionTV = (TextView) mToolTipViewGroup.findViewById(R.id.description);
                Button button = (Button) mToolTipViewGroup.findViewById(R.id.button);
                if (mToolTip.mButtonText != null) {
                    button.setText(mToolTip.mButtonText);
                    button.setVisibility(View.VISIBLE);
                } else {
                    button.setVisibility(View.GONE);
                }
                /* set tooltip attributes */

                if (mToolTip.mTitle == null || mToolTip.mTitle.isEmpty()) {
                    toolTipTitleTV.setVisibility(View.GONE);
                } else {
                    toolTipTitleTV.setVisibility(View.VISIBLE);
                    toolTipTitleTV.setText(mToolTip.mTitle);
                }

                if (mToolTip.mDescription == null || mToolTip.mDescription.isEmpty()) {
                    toolTipDescriptionTV.setVisibility(View.GONE);
                } else {
                    toolTipDescriptionTV.setVisibility(View.VISIBLE);
                    toolTipDescriptionTV.setText(mToolTip.mDescription);
                    if (mToolTip.mDescriptionBackgroundId != 0) {
                        toolTipDescriptionTV.setBackgroundResource(mToolTip.mDescriptionBackgroundId);
                        toolTipContainer.setBackgroundResource(R.color.transparent);
                    }
                    if (mToolTip.mDescriptionTextColor != 99999) {
                        toolTipDescriptionTV.setTextColor(mToolTip.mDescriptionTextColor);
                    }
                    if (mToolTip.mOverlayWidth != 0) {
                        toolTipDescriptionTV.setMaxWidth(mToolTip.mOverlayWidth);
//                        toolTipDescriptionTV.getLayoutParams().width = mToolTip.mOverlayWidth;
                    }
                }
            } else {
                mToolTipViewGroup = (ArrowTowardsLayout) mToolTip.getCustomView();
            }

            mToolTipViewGroup.startAnimation(mToolTip.mEnterAnimation);

            /* add setShadow if it's turned on */
            if (mToolTip.mShadow) {
                mToolTipViewGroup.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.drop_shadow));
            }

            /* position and size calculation */
            int[] pos = new int[2];
            mHighlightedView.getLocationOnScreen(pos);
            int targetViewX = pos[0];
            final int targetViewY = pos[1];

            // get measured size of tooltip
            mToolTipViewGroup.measure(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            int toolTipMeasuredWidth = mToolTipViewGroup.getMeasuredWidth();
            int toolTipMeasuredHeight = mToolTipViewGroup.getMeasuredHeight();

            Point resultPoint = new Point(); // this holds the final position of tooltip
            float density = mActivity.getResources().getDisplayMetrics().density;
            final float adjustment = mMarg * density; //adjustment is that little overlapping area of tooltip and targeted button

            // calculate x position, based on gravity, tooltipMeasuredWidth, parent max width, x position of target view, adjustment
            if (toolTipMeasuredWidth > parent.getWidth()) {
                resultPoint.x = getXForTooTip(mToolTip.mGravity, parent.getWidth(), targetViewX, adjustment);
            } else {
                resultPoint.x = getXForTooTip(mToolTip.mGravity, toolTipMeasuredWidth, targetViewX, adjustment);
            }

            resultPoint.y = getYForTooTip(mToolTip.mGravity, toolTipMeasuredHeight, targetViewY, adjustment);

            // add view to parent
//            ((ViewGroup) mActivity.getWindow().getDecorView().findViewById(android.R.id.content)).addView(mToolTipViewGroup, layoutParams);
            parent.addView(mToolTipViewGroup, layoutParams);

            // 1. width < screen check
            if (toolTipMeasuredWidth > parent.getWidth()) {
                mToolTipViewGroup.getLayoutParams().width = parent.getWidth();
                toolTipMeasuredWidth = parent.getWidth();
            }
            // 2. x left boundary check
            if (resultPoint.x < 0) {
                mToolTipViewGroup.getLayoutParams().width = toolTipMeasuredWidth + resultPoint.x; //since point.x is negative, use plus
                resultPoint.x = 0;
            }
            // 3. x right boundary check
            int tempRightX = resultPoint.x + toolTipMeasuredWidth;
            if (tempRightX > parent.getWidth()) {
                mToolTipViewGroup.getLayoutParams().width = parent.getWidth() - resultPoint.x; //since point.x is negative, use plus
            }

            // pass toolTip onClickListener into toolTipViewGroup
            if (mToolTip.mOnClickListener != null) {
                mToolTipViewGroup.setOnClickListener(mToolTip.mOnClickListener);
            }

            // height boundary can be fixed by user changing the gravity to the other size, since there are plenty of space vertically compared to horizontally

            // this needs an viewTreeObserver, that's because TextView measurement of it's vertical height is not accurate (didn't take into account of multiple lines yet) before it's rendered
            // re-calculate height again once it's rendered

            mToolTipViewGroup.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    // make sure this only run once
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        //noinspection deprecation
                        mToolTipViewGroup.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        mToolTipViewGroup.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }

                    int fixedY;
                    int toolTipHeightAfterLayouted = mToolTipViewGroup.getHeight();
                    fixedY = getYForTooTip(mToolTip.mGravity, toolTipHeightAfterLayouted, targetViewY, adjustment);
                    layoutParams.setMargins((int) mToolTipViewGroup.getX(), fixedY, 40, 40);
                    mToolTipViewGroup.setLayoutParams(layoutParams);
                    int[] locations = new int[2];
                    mToolTipViewGroup.getLocationOnScreen(locations);
                    if (pointLocationX != 0)
                        pointLocationX -= locations[0];
                    else if (pointLocationY != 0)
                        pointLocationY -= locations[1];
                    mToolTipViewGroup.setArrowTowardsAttr(mToolTip.mGravity, pointLocationX, pointLocationY);
                    if (mToolTip.toolTip3LineColor != 0) {
//                        toolTipDescriptionTV.setBackgroundResource(mToolTip.mDescriptionBackgroundId);
                        mToolTipViewGroup.setLineColor(mToolTip.toolTip3LineColor);
                    }
                }
            });

            // set the position using setMargins on the left and top
            layoutParams.setMargins(resultPoint.x, resultPoint.y, 40, 40);
        }

    }

    private int getXForTooTip(int gravity, int toolTipMeasuredWidth, int targetViewX, float adjustment) {
        int x;
        if ((gravity & Gravity.LEFT) == Gravity.LEFT) {
            x = targetViewX - toolTipMeasuredWidth - (int) adjustment;

        } else if ((gravity & Gravity.RIGHT) == Gravity.RIGHT) {
            x = targetViewX + mHighlightedView.getWidth() + (int) adjustment;
        } else {
            x = targetViewX + mHighlightedView.getWidth() / 2 - toolTipMeasuredWidth / 2;
            if (x + toolTipMeasuredWidth > getScreenWidth())
                x = getScreenWidth() - toolTipMeasuredWidth;
        }
        if (x >= 0)
            x += mToolTip.mMarginLeft;
        else
            x = mToolTip.mMarginLeft;
        return x;
    }

    private int getYForTooTip(int gravity, int toolTipMeasuredHeight, int targetViewY, float adjustment) {
        int y;
        if ((gravity & Gravity.TOP) == Gravity.TOP) {

            if (((gravity & Gravity.LEFT) == Gravity.LEFT) || ((gravity & Gravity.RIGHT) == Gravity.RIGHT)) {
                y = targetViewY - toolTipMeasuredHeight + (int) adjustment;
            } else {
                y = targetViewY - toolTipMeasuredHeight - (int) adjustment;
            }
        } else { // this is center
            if (((gravity & Gravity.LEFT) == Gravity.LEFT) || ((gravity & Gravity.RIGHT) == Gravity.RIGHT)) {
                y = targetViewY - mHighlightedView.getHeight() - (int) adjustment;
            } else {
                y = targetViewY + mHighlightedView.getHeight() + (int) adjustment;
            }
        }
        return y;
    }


    private void setupFrameLayout() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        ViewGroup contentArea = (ViewGroup) mActivity.getWindow().getDecorView().findViewById(android.R.id.content);
        int[] pos = new int[2];
        contentArea.getLocationOnScreen(pos);
        // frameLayoutWithHole's coordinates are calculated taking full screen height into account
        // but we're adding it to the content area only, so we need to offset it to the same Y value of contentArea

        layoutParams.setMargins(0, -pos[1], 0, 0);
        contentArea.addView(mFrameLayout, layoutParams);
    }

    private void performAnimationOn(final View view) {

        if (mTechnique != null && mTechnique == Technique.HorizontalLeft) {

            final AnimatorSet animatorSet = new AnimatorSet();
            final AnimatorSet animatorSet2 = new AnimatorSet();
            Animator.AnimatorListener lis1 = new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    view.setScaleX(1f);
                    view.setScaleY(1f);
                    view.setTranslationX(0);
                    animatorSet2.start();
                }
            };
            Animator.AnimatorListener lis2 = new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    view.setScaleX(1f);
                    view.setScaleY(1f);
                    view.setTranslationX(0);
                    animatorSet.start();
                }
            };

            long fadeInDuration = 800;
            long scaleDownDuration = 800;
            long goLeftXDuration = 2000;
            long fadeOutDuration = goLeftXDuration;
            float translationX = getScreenWidth() / 2;

            final ValueAnimator fadeInAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
            fadeInAnim.setDuration(fadeInDuration);
            final ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.85f);
            scaleDownX.setDuration(scaleDownDuration);
            final ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.85f);
            scaleDownY.setDuration(scaleDownDuration);
            final ObjectAnimator goLeftX = ObjectAnimator.ofFloat(view, "translationX", -translationX);
            goLeftX.setDuration(goLeftXDuration);
            final ValueAnimator fadeOutAnim = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
            fadeOutAnim.setDuration(fadeOutDuration);

            final ValueAnimator fadeInAnim2 = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
            fadeInAnim2.setDuration(fadeInDuration);
            final ObjectAnimator scaleDownX2 = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.85f);
            scaleDownX2.setDuration(scaleDownDuration);
            final ObjectAnimator scaleDownY2 = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.85f);
            scaleDownY2.setDuration(scaleDownDuration);
            final ObjectAnimator goLeftX2 = ObjectAnimator.ofFloat(view, "translationX", -translationX);
            goLeftX2.setDuration(goLeftXDuration);
            final ValueAnimator fadeOutAnim2 = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
            fadeOutAnim2.setDuration(fadeOutDuration);

            animatorSet.play(fadeInAnim);
            animatorSet.play(scaleDownX).with(scaleDownY).after(fadeInAnim);
            animatorSet.play(goLeftX).with(fadeOutAnim).after(scaleDownY);

            animatorSet2.play(fadeInAnim2);
            animatorSet2.play(scaleDownX2).with(scaleDownY2).after(fadeInAnim2);
            animatorSet2.play(goLeftX2).with(fadeOutAnim2).after(scaleDownY2);

            animatorSet.addListener(lis1);
            animatorSet2.addListener(lis2);
            animatorSet.start();

            /* these animatorSets are kept track in FrameLayout, so that they can be cleaned up when FrameLayout is detached from window */
            mFrameLayout.addAnimatorSet(animatorSet);
            mFrameLayout.addAnimatorSet(animatorSet2);
        } else if (mTechnique != null && mTechnique == Technique.HorizontalRight) {

        } else if (mTechnique != null && mTechnique == Technique.VerticalUpward) {

        } else if (mTechnique != null && mTechnique == Technique.VerticalDownward) {

        } else { // do click for default case
            final AnimatorSet animatorSet = new AnimatorSet();
            final AnimatorSet animatorSet2 = new AnimatorSet();
            Animator.AnimatorListener lis1 = new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    view.setScaleX(1f);
                    view.setScaleY(1f);
                    view.setTranslationX(0);
                    animatorSet2.start();
                }
            };
            Animator.AnimatorListener lis2 = new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    view.setScaleX(1f);
                    view.setScaleY(1f);
                    view.setTranslationX(0);
                    animatorSet.start();
                }
            };

            long fadeInDuration = 800;
            long scaleDownDuration = 800;
            long fadeOutDuration = 800;
            long delay = 1000;

            final ValueAnimator delayAnim = ObjectAnimator.ofFloat(view, "translationX", 0);
            delayAnim.setDuration(delay);
            final ValueAnimator fadeInAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
            fadeInAnim.setDuration(fadeInDuration);
            final ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.85f);
            scaleDownX.setDuration(scaleDownDuration);
            final ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.85f);
            scaleDownY.setDuration(scaleDownDuration);
            final ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(view, "scaleX", 0.85f, 1f);
            scaleUpX.setDuration(scaleDownDuration);
            final ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(view, "scaleY", 0.85f, 1f);
            scaleUpY.setDuration(scaleDownDuration);
            final ValueAnimator fadeOutAnim = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
            fadeOutAnim.setDuration(fadeOutDuration);

            final ValueAnimator delayAnim2 = ObjectAnimator.ofFloat(view, "translationX", 0);
            delayAnim2.setDuration(delay);
            final ValueAnimator fadeInAnim2 = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
            fadeInAnim2.setDuration(fadeInDuration);
            final ObjectAnimator scaleDownX2 = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.85f);
            scaleDownX2.setDuration(scaleDownDuration);
            final ObjectAnimator scaleDownY2 = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.85f);
            scaleDownY2.setDuration(scaleDownDuration);
            final ObjectAnimator scaleUpX2 = ObjectAnimator.ofFloat(view, "scaleX", 0.85f, 1f);
            scaleUpX2.setDuration(scaleDownDuration);
            final ObjectAnimator scaleUpY2 = ObjectAnimator.ofFloat(view, "scaleY", 0.85f, 1f);
            scaleUpY2.setDuration(scaleDownDuration);
            final ValueAnimator fadeOutAnim2 = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
            fadeOutAnim2.setDuration(fadeOutDuration);
            view.setAlpha(0);
            animatorSet.setStartDelay(mToolTip != null ? mToolTip.mEnterAnimation.getDuration() : 0);
            animatorSet.play(fadeInAnim);
            animatorSet.play(scaleDownX).with(scaleDownY).after(fadeInAnim);
            animatorSet.play(scaleUpX).with(scaleUpY).with(fadeOutAnim).after(scaleDownY);
            animatorSet.play(delayAnim).after(scaleUpY);

            animatorSet2.play(fadeInAnim2);
            animatorSet2.play(scaleDownX2).with(scaleDownY2).after(fadeInAnim2);
            animatorSet2.play(scaleUpX2).with(scaleUpY2).with(fadeOutAnim2).after(scaleDownY2);
            animatorSet2.play(delayAnim2).after(scaleUpY2);

            animatorSet.addListener(lis1);
            animatorSet2.addListener(lis2);
            animatorSet.start();

            /* these animatorSets are kept track in FrameLayout, so that they can be cleaned up when FrameLayout is detached from window */
            mFrameLayout.addAnimatorSet(animatorSet);
            mFrameLayout.addAnimatorSet(animatorSet2);
        }
    }

    private int getScreenWidth() {
        if (mActivity != null) {
            return mActivity.getResources().getDisplayMetrics().widthPixels;
        } else {
            return 0;
        }
    }

}
