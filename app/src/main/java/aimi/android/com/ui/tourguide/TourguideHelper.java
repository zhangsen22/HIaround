package aimi.android.com.ui.tourguide;

import android.app.Activity;
import android.view.View;

import com.growalong.util.util.DensityUtil;
import com.growalong.util.util.SharedPreferenceUtil;

import java.util.List;

import aimi.android.com.MyApplication;

/**
 * Created by yangxing on 2017/10/31.
 */

public class TourguideHelper {
    private TourGuide mTutorialHandler;

    public void showTip(final View tipCoverView, final Activity context, final String sharedPreStr, final String tipText, final int position) {
        MyApplication.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                boolean showTip = SharedPreferenceUtil.getInstance(context).getBoolean(sharedPreStr, true);

                if (showTip && tipCoverView.getVisibility() == View.VISIBLE) {
                    SharedPreferenceUtil.getInstance(context).setBoolean(sharedPreStr, false);
                    int w = DensityUtil.dip2px(context, 200);
                    mTutorialHandler = TourGuide.init(context).with(TourGuide.Technique.Click)
                            .setPointer(new Pointer())
                            .setToolTipShadow(false)
                            .setToolTipGravity(position)
                            .setToolTipDescription(tipText)
                            .setToolTipsetOverlayWidth(w)
                            .overlayDisableClick(false)
                            .overlayDisableClickThroughHole(false)
                            .setOverlayStyle(Overlay.Style.Circle)
                            .setOverlayOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mTutorialHandler != null)
                                        mTutorialHandler.cleanUp();
                                    mTutorialHandler = null;
                                }
                            })
                            .playOn(tipCoverView);
                }
            }
        }, 200);
    }

    public void showTip(final List<View> tipCoverViewList, final Activity context, final String sharedPreStr, final List<String> tipList, final List<Integer> positionList, final int index, final Overlay.Style style) {
        MyApplication.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                View tipCoverView = tipCoverViewList.get(index);
                String tipText = tipList.get(index);
                final int position = positionList.get(index);
                if (tipCoverView.getVisibility() == View.VISIBLE) {
                    SharedPreferenceUtil.getInstance(context).setBoolean(sharedPreStr, false);
                    int w = DensityUtil.dip2px(context, 200);
                    mTutorialHandler = TourGuide.init(context).with(TourGuide.Technique.Click)
                            .setPointer(new Pointer())
                            .setToolTipShadow(false)
                            .setToolTipGravity(position)
                            .setToolTipDescription(tipText)
                            .setToolTipsetOverlayWidth(w)
                            .overlayDisableClick(false)
                            .overlayDisableClickThroughHole(false)
                            .setOverlayStyle(style)
                            .setOverlayOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mTutorialHandler != null)
                                        mTutorialHandler.cleanUp();
                                    mTutorialHandler = null;
                                    if (index >= tipCoverViewList.size() - 1) {
                                    } else {
                                        showTip(tipCoverViewList, context, sharedPreStr, tipList, positionList, index + 1, style);
                                    }
                                }
                            })
                            .playOn(tipCoverView);
                }
            }
        }, 200);
    }

    public void cleanTip() {
        if (mTutorialHandler != null) {
            mTutorialHandler.cleanUp();
            mTutorialHandler = null;
        }
    }
}
