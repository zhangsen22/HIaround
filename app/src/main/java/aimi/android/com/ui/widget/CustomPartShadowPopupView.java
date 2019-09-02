package aimi.android.com.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import com.lxj.xpopup.impl.PartShadowPopupView;
import com.lxj.xpopup.interfaces.OnSelectWebChatListener;

import java.util.List;

import aimi.android.com.MyApplication;
import aimi.android.com.R;
import aimi.android.com.modle.LaCaraWenChatListItem;
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

    public CustomPartShadowPopupView(@NonNull Context context, List<LaCaraWenChatListItem> list, OnSelectWebChatListener selectListener) {
        super(context);
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
    public void onItemClick(int position, long paymentId, String account) {
        if(selectListener != null){
            selectListener.onSelect(position,paymentId,account);
            this.dismiss();
        }
    }
}
