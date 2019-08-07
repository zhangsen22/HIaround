package hiaround.android.com.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import hiaround.android.com.MyApplication;
import hiaround.android.com.R;
import hiaround.android.com.modle.BuyItem;
import hiaround.android.com.modle.LargeAmountItem;
import hiaround.android.com.ui.activity.BusinessBuyActivity;
import hiaround.android.com.ui.adapter.poweradapter.PowerAdapter;
import hiaround.android.com.ui.adapter.poweradapter.PowerHolder;

public class LargeAmountAdapter extends PowerAdapter<LargeAmountItem> {

    private static final String TAG = BuyFragmentAdapter.class.getSimpleName();
    private Context mContext;

    private LayoutInflater inflater;

    public LargeAmountAdapter(Context context) {
        super();
        mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public PowerHolder<LargeAmountItem> onViewHolderCreate(@NonNull ViewGroup parent, int viewType) {
        return new LargeAmountItemHolder(inflater.inflate(R.layout.large_amount_item, parent, false));
    }

    @Override
    public void onViewHolderBind(@NonNull PowerHolder<LargeAmountItem> holder, int position) {
        ((LargeAmountItemHolder) holder).onBind(list.get(position), position);
    }

    private class LargeAmountItemHolder extends PowerHolder<LargeAmountItem> {
        TextView tvLargeAmountTime;
        TextView tvLargeAmountPrice;
        TextView tvLargeAmountLastNum;
        TextView tvLargeAmountNum;
        TextView tvAllPrice;
        ImageView ivIdcard;
        ImageView ivAilpay;
        ImageView ivWebpay;
        LinearLayout llQianggou;
        public LargeAmountItemHolder(View itemView) {
            super(itemView);
            tvLargeAmountTime = itemView.findViewById(R.id.tv_large_amount_time);
            tvLargeAmountPrice = itemView.findViewById(R.id.tv_large_amount_price);
            tvLargeAmountLastNum = itemView.findViewById(R.id.tv_large_amount_last_num);
            tvLargeAmountNum = itemView.findViewById(R.id.tv_large_amount_num);
            tvAllPrice = itemView.findViewById(R.id.tv_all_price);
            ivIdcard = itemView.findViewById(R.id.iv_idcard);
            ivAilpay = itemView.findViewById(R.id.iv_ailpay);
            ivWebpay = itemView.findViewById(R.id.iv_webpay);
            llQianggou = itemView.findViewById(R.id.ll_qianggou);
        }

        @Override
        public void onBind(@NonNull LargeAmountItem largeAmountItem, int position) {
            if(largeAmountItem != null){
                tvLargeAmountTime.setText(largeAmountItem.getNickname());
                tvLargeAmountPrice.setText(MyApplication.appContext.getResources().getString(R.string.rmb)+new DecimalFormat("0.00").format(largeAmountItem.getPrice()));
                tvLargeAmountLastNum.setText("1");
                tvLargeAmountNum.setText(new DecimalFormat("0.00").format(largeAmountItem.getNum()));
                tvAllPrice.setText(new DecimalFormat("0.00").format(largeAmountItem.getMoney()));
                if(largeAmountItem.isSupportBank()){
                    ivIdcard.setVisibility(View.VISIBLE);
                }else {
                    ivIdcard.setVisibility(View.GONE);
                }

                if(largeAmountItem.isSupportAli()){
                    ivAilpay.setVisibility(View.VISIBLE);
                }else {
                    ivAilpay.setVisibility(View.GONE);
                }

                if(largeAmountItem.isSupportWechat()){
                    ivWebpay.setVisibility(View.VISIBLE);
                }else {
                    ivWebpay.setVisibility(View.GONE);
                }
                llQianggou.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BuyItem buyItem = new BuyItem(largeAmountItem.getId(), largeAmountItem.getNickname(), largeAmountItem.getPrice(), largeAmountItem.getNum(), largeAmountItem.getNum(), largeAmountItem.isSupportAli(), largeAmountItem.isSupportWechat(), largeAmountItem.isSupportBank(),true);
                        BusinessBuyActivity.startThis(mContext,buyItem);
                    }
                });
            }
        }
    }
}
