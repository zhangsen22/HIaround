package hiaround.android.com.ui.tourguide;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.growalong.util.util.DensityUtil;

import hiaround.android.com.MyApplication;

/**
 * Created by liulian on 16/4/26.
 */
public class ArrowTowardsLayout extends LinearLayout {

    private int mTowardsAttr = -1;
    private int mY, mX;

    private int mLength; //边长
    private int lineColor = 999999;

    public ArrowTowardsLayout(Context context) {
        super(context);
    }

    public ArrowTowardsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ArrowTowardsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * @param towardsAttr Gravity
     * @param x
     * @param y
     */
    public void setArrowTowardsAttr(int towardsAttr, int x, int y) {
        mTowardsAttr = towardsAttr;
        mX = x;
        mY = y;
        mLength = (int) (15 * getResources().getDisplayMetrics().density + 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        if (mTowardsAttr != -1) {
            Paint p = new Paint();

            if (lineColor != 999999) {
                p.setColor(lineColor);
            } else {
                p.setColor(Color.WHITE);
            }
            Path path = new Path();
            int heigtIndex = DensityUtil.dip2px(MyApplication.appContext, 2) + 1;
            switch (mTowardsAttr) {
                case Gravity.TOP:
                    path.moveTo(mX - mLength / 2, getHeight() - mLength / 2 - heigtIndex);// 此点为多边形的起点
                    path.lineTo(mX, getHeight() - heigtIndex);
                    path.lineTo(mX + mLength / 2, getHeight() - mLength / 2 - heigtIndex);
                    break;
                case Gravity.BOTTOM:
                    path.moveTo(mX - mLength / 2, mLength / 2);// 此点为多边形的起点
                    path.lineTo(mX, 0);
                    path.lineTo(mX + mLength / 2, mLength / 2);
                    break;
                case Gravity.LEFT:
                    path.moveTo(getWidth() - mLength / 2, mY - mLength / 2);// 此点为多边形的起点
                    path.lineTo(getWidth(), mY);
                    path.lineTo(getWidth() - mLength / 2, mY + mLength / 2);
                    break;
                case Gravity.RIGHT:
                    path.moveTo(mLength / 2, mY - mLength / 2);// 此点为多边形的起点
                    path.lineTo(0, mY);
                    path.lineTo(mLength / 2, mY + mLength / 2);
                    break;
            }
            path.close(); // 使这些点构成封闭的多边形
            canvas.drawPath(path, p);
        }
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }
}
