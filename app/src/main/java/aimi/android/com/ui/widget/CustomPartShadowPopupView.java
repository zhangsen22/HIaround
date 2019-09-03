package aimi.android.com.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.PartShadowPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectWebChatListener;
import com.lxj.xpopup.interfaces.XPopupCallback;

import java.util.List;

import aimi.android.com.BaseActivity;
import aimi.android.com.MyApplication;
import aimi.android.com.R;
import aimi.android.com.modle.LaCaraWenChatListItem;
import aimi.android.com.ui.activity.WebChatListActivity;
import aimi.android.com.ui.adapter.LaCaraWebListAdapter;

/**
 * Description:
 * Create by dance, at 2018/12/21
 */
public class CustomPartShadowPopupView extends PartShadowPopupView implements LaCaraWebListAdapter.OnItemClickListener {

    private List<LaCaraWenChatListItem> list;
    private OnSelectWebChatListener selectListener;
    private RecyclerView mRecyclerView;
    private LaCaraWebListAdapter laCaraWebListAdapter;
    private BaseActivity baseActivity;

    public CustomPartShadowPopupView(@NonNull Context context, List<LaCaraWenChatListItem> list, OnSelectWebChatListener selectListener, BaseActivity baseActivity) {
        super(context);
        this.baseActivity = baseActivity;
        this.list = list;
        this.selectListener = selectListener;
    }
    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_part_shadow_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        mRecyclerView = findViewById(R.id.rv_weblist);

        LinearLayoutManager manager = new LinearLayoutManager(MyApplication.appContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        laCaraWebListAdapter = new LaCaraWebListAdapter(MyApplication.appContext,this);
        mRecyclerView.setAdapter(laCaraWebListAdapter);
    }

    @Override
    protected void onShow() {
        super.onShow();
        Log.e("tag","CustomPartShadowPopupView onShow");
        laCaraWebListAdapter.getList().addAll(list);
        laCaraWebListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
        Log.e("tag","CustomPartShadowPopupView onDismiss");
        laCaraWebListAdapter.getList().clear();
    }

    @Override
    public void onItemClick(int position, long paymentId, String account,boolean isShiXiao) {
        if (isShiXiao) {
            //带确认和取消按钮的弹窗
            new XPopup.Builder(getContext())
                    //                         .dismissOnTouchOutside(false)
                    // 设置弹窗显示和隐藏的回调监听
                    //                         .autoDismiss(false)
                    //                        .popupAnimation(PopupAnimation.NoAnimation)
                    .setPopupCallback(new XPopupCallback() {
                        @Override
                        public void onShow() {
                            Log.e("tag", "onShow");
                        }

                        @Override
                        public void onDismiss() {
                            Log.e("tag", "onDismiss");
                        }
                    }).asConfirm("此微信收款方式已失效,请重新绑定", "",
                    "取消", "确定",
                    new OnConfirmListener() {
                        @Override
                        public void onConfirm() {
                            WebChatListActivity.startThis(baseActivity);
                            CustomPartShadowPopupView.this.dismiss();
                        }
                    }, null, false)
                    .show();
        }else {
            if(selectListener != null){
                selectListener.onSelect(position,paymentId,account);
                this.dismiss();
            }
        }
    }
}
