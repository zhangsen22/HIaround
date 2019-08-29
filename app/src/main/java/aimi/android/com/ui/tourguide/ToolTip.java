package aimi.android.com.ui.tourguide;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;

/**
 * Created by tanjunrong on 6/17/15.
 */
public class ToolTip {
    public String mTitle, mDescription, mButtonText;
    public int mDescriptionBackgroundId = 0;
    public int mTextColor;
    public Animation mEnterAnimation, mExitAnimation;
    public boolean mShadow;
    public int mGravity;
    public View.OnClickListener mOnClickListener;
    public ViewGroup mCustomView;
    public int mOverlayWidth;
    public int mMarginLeft;
    public int mDescriptionTextColor = 99999;
    public int toolTip3LineColor;

    public ToolTip() {
        /* default values */
        mTitle = "";
        mDescription = "";
        mTextColor = Color.parseColor("#FFFFFF");

        mEnterAnimation = new AlphaAnimation(0f, 1f);
        mEnterAnimation.setDuration(0);
        mEnterAnimation.setFillAfter(true);
        mEnterAnimation.setInterpolator(new BounceInterpolator());
        mShadow = true;

        mGravity = Gravity.CENTER;
    }

    public ToolTip setButtonText(String text) {
        mButtonText = text;
        return this;
    }

    /**
     * Set title text
     *
     * @param title
     * @return return ToolTip instance for chaining purpose
     */
    public ToolTip setTitle(String title) {
        mTitle = title;
        return this;
    }

    /**
     * Set description text
     *
     * @param description
     * @return return ToolTip instance for chaining purpose
     */
    public ToolTip setDescription(String description) {
        mDescription = description;
        return this;
    }

    public ToolTip setDescriptionBackgroundColor(int color) {
        mDescriptionBackgroundId = color;
        return this;
    }

    /**
     * Set text color
     *
     * @param textColor
     * @return return ToolTip instance for chaining purpose
     */
    public ToolTip setTextColor(int textColor) {
        mTextColor = textColor;
        return this;
    }

    /**
     * Set enter animation
     *
     * @param enterAnimation
     * @return return ToolTip instance for chaining purpose
     */
    public ToolTip setEnterAnimation(Animation enterAnimation) {
        mEnterAnimation = enterAnimation;
        return this;
    }
    /**
     * Set exit animation
     * @param exitAnimation
     * @return return ToolTip instance for chaining purpose
     */
//    public ToolTip setExitAnimation(Animation exitAnimation){
//        mExitAnimation = exitAnimation;
//        return this;
//    }

    /**
     * Set the gravity, the setGravity is centered relative to the targeted button
     *
     * @param gravity Gravity.CENTER, Gravity.TOP, Gravity.BOTTOM, etc
     * @return return ToolTip instance for chaining purpose
     */
    public ToolTip setGravity(int gravity) {
        mGravity = gravity;
        return this;
    }

    /**
     * Set if you want to have setShadow
     *
     * @param shadow
     * @return return ToolTip instance for chaining purpose
     */
    public ToolTip setShadow(boolean shadow) {
        mShadow = shadow;
        return this;
    }

    public ToolTip setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
        return this;
    }

    public ViewGroup getCustomView() {
        return mCustomView;
    }

    public ToolTip setCustomView(ViewGroup view) {
        mCustomView = view;
        return this;
    }

    public ToolTip setOverlayWidth(int i) {
        mOverlayWidth = i;
        return this;
    }

    public ToolTip setMarginLeft(int i) {
        mMarginLeft = i;
        return this;
    }

    public ToolTip setToolTipDescriptionTextColor(int color) {
        mDescriptionTextColor = color;
        return this;
    }

    public void setToolTip3LineColor(int toolTip3LineColor) {
        this.toolTip3LineColor = toolTip3LineColor;
    }
}
