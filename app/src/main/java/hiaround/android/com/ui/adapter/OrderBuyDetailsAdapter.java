package hiaround.android.com.ui.adapter;

import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.growalong.util.util.DateUtil;
import com.growalong.util.util.GALogger;

import java.text.DecimalFormat;
import hiaround.android.com.BaseActivity;
import hiaround.android.com.R;
import hiaround.android.com.modle.BuyBusinessResponse;
import hiaround.android.com.modle.MySellOrBuyinfoItem;
import hiaround.android.com.ui.activity.BusinessBuyDetailsActivity;
import hiaround.android.com.ui.adapter.poweradapter.PowerAdapter;
import hiaround.android.com.ui.adapter.poweradapter.PowerHolder;

public class OrderBuyDetailsAdapter extends PowerAdapter<MySellOrBuyinfoItem> {
    private static final String TAG = OrderBuyDetailsAdapter.class.getSimpleName();
    private BaseActivity mContext;
    private int childType;
    private LayoutInflater inflater;

    public OrderBuyDetailsAdapter(BaseActivity context, int childType,OrderBuyClickListenering listenering) {
        super();
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.childType = childType;
        this.listenering = listenering;
    }

    @Override
    public PowerHolder<MySellOrBuyinfoItem> onViewHolderCreate(@NonNull ViewGroup parent, int viewType) {
        return new OrderSellDetailsHolder(inflater.inflate(R.layout.order_buy_details_item, parent, false));
    }

    @Override
    public void onViewHolderBind(@NonNull PowerHolder<MySellOrBuyinfoItem> holder, int position) {
        ((OrderSellDetailsHolder) holder).onBind(list.get(position), position);
    }

    private class OrderSellDetailsHolder extends PowerHolder<MySellOrBuyinfoItem> {
        CountDownTimer sStimer;
        CountDownTimer sStimer1;
        TextView tvOrderSellTime;
        TextView tvOrderSellStatus;
        TextView tvOrderSellPrice;
        TextView tvOrderSellAmount;
        TextView tvOrderSellPaycode;
        TextView tvOrderSellSstime;
        TextView tvOrderSellSstime1;
        TextView tvOrderSellSs;
        TextView tvOrderSellSs1;
        TextView tvOrderSellQfb;
        LinearLayout llOrderSellButton;
        LinearLayout llOrderSellButton1;

        public OrderSellDetailsHolder(View itemView) {
            super(itemView);
            tvOrderSellTime = itemView.findViewById(R.id.tv_order_sell_time);
            tvOrderSellStatus = itemView.findViewById(R.id.tv_order_sell_status);
            tvOrderSellPrice = itemView.findViewById(R.id.tv_order_sell_price);
            tvOrderSellAmount = itemView.findViewById(R.id.tv_order_sell_amount);
            tvOrderSellPaycode = itemView.findViewById(R.id.tv_order_sell_paycode);
            tvOrderSellSstime = itemView.findViewById(R.id.tv_order_sell_sstime);
            tvOrderSellSstime1 = itemView.findViewById(R.id.tv_order_sell_sstime1);
            tvOrderSellSs = itemView.findViewById(R.id.tv_order_sell_ss);
            tvOrderSellSs1 = itemView.findViewById(R.id.tv_order_sell_ss1);
            tvOrderSellQfb = itemView.findViewById(R.id.tv_order_sell_qfb);
            llOrderSellButton = itemView.findViewById(R.id.ll_order_sell_button);
            llOrderSellButton1 = itemView.findViewById(R.id.ll_order_sell_button1);
        }

        @Override
        public void onBind(@NonNull final MySellOrBuyinfoItem myBuyinfoItem, int position) {
            long createTime = myBuyinfoItem.getCreateTime();
            final long payTime = myBuyinfoItem.getPayTime();
            tvOrderSellTime.setText(DateUtil.getCurrentDateString1(createTime));
            int status = myBuyinfoItem.getStatus();
            if(status == 1){
                tvOrderSellStatus.setText("等待付款");
            }else if(status == 2){
                tvOrderSellStatus.setText("等待放币");
            }else if(status == 10){
                tvOrderSellStatus.setText("完成");
            }else if(status == 20){
                tvOrderSellStatus.setText("申诉中");
            }else if(status == 30){
                tvOrderSellStatus.setText("超时取消");
            }else if(status == 40){
                tvOrderSellStatus.setText("已关闭");
            }
            tvOrderSellPrice.setText(myBuyinfoItem.getUsdtPriceFmt());
            tvOrderSellAmount.setText(myBuyinfoItem.getUsdtTotalMoneyFmt());
            tvOrderSellPaycode.setText(myBuyinfoItem.getUsdtNumFmt());

            if(childType == 1){
                if(status == 1){
                    long currentTime = System.currentTimeMillis();
                    llOrderSellButton1.setVisibility(View.GONE);
                    llOrderSellButton.setVisibility(View.VISIBLE);
                    if(currentTime >= createTime + 10*60*1000){
                        tvOrderSellSstime.setVisibility(View.GONE);
                    }else {
                        if(sStimer != null){
                            sStimer.cancel();
                            sStimer = null;
                        }
                        sStimer = new CountDownTimer(createTime + 10*60*1000 - currentTime, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                if (mContext != null) {
                                    int left = (int) ((millisUntilFinished - 1000) / 1000);
                                    GALogger.d(TAG, "left       " + left);
                                    if (left > 0) {
                                        tvOrderSellSstime.setVisibility(View.VISIBLE);
                                        tvOrderSellSstime.setText(DateUtil.getCurrentDateString2(millisUntilFinished) + "后自动取消订单");
                                    } else {
                                        tvOrderSellSstime.setVisibility(View.GONE);
                                        if(listenering != null){
                                            listenering.orderBuyClick(2,myBuyinfoItem.getTradeid());
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onFinish() {
                            }
                        };
                        sStimer.start();
                    }
                    tvOrderSellQfb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(listenering != null){
                                listenering.orderBuyClick(2,myBuyinfoItem.getTradeid());
                            }
                        }
                    });

                    tvOrderSellSs.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                BusinessBuyDetailsActivity.startThis(mContext, new BuyBusinessResponse(myBuyinfoItem.getTradeid(), myBuyinfoItem.getPayCode(),myBuyinfoItem.getCreateTime(),myBuyinfoItem.getUsdtTotalMoneyFmt(),myBuyinfoItem.getPrice()+"", myBuyinfoItem.getNum()+"", myBuyinfoItem.getPayType(),myBuyinfoItem.getPayee()));
                        }
                    });
                }else if(status == 2){
                    llOrderSellButton.setVisibility(View.GONE);
                    llOrderSellButton1.setVisibility(View.VISIBLE);
                    long currentTime = System.currentTimeMillis();
                    if(currentTime >= payTime + 10*60*1000){
                        tvOrderSellSstime1.setVisibility(View.GONE);
                        tvOrderSellSs1.setEnabled(true);
                    }else {
                        if(sStimer1 != null){
                            sStimer1.cancel();
                            sStimer1 = null;
                        }
                        sStimer1 = new CountDownTimer(payTime + 10 * 60 * 1000 - currentTime, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                int left = (int) ((millisUntilFinished - 1000) / 1000);
                                GALogger.d(TAG,"left       "+left);
                                if (left > 0) {
                                    tvOrderSellSs1.setEnabled(false);
                                    tvOrderSellSstime1.setVisibility(View.VISIBLE);
                                    tvOrderSellSstime1.setText(DateUtil.getCurrentDateString2(millisUntilFinished)+"后可申诉");
                                } else {
                                    tvOrderSellSstime1.setVisibility(View.GONE);
                                    tvOrderSellSs1.setEnabled(true);
                                }
                            }
                            @Override
                            public void onFinish() {
                            }
                        };
                        sStimer1.start();
                    }
                    tvOrderSellSs1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(listenering != null){
                                listenering.orderBuyClick(1,myBuyinfoItem.getTradeid());
                            }
                        }
                    });
                }else {
                    llOrderSellButton1.setVisibility(View.GONE);
                    llOrderSellButton.setVisibility(View.GONE);
                }
            }else if(childType == 2){
                llOrderSellButton1.setVisibility(View.GONE);
                llOrderSellButton.setVisibility(View.GONE);
            }else if(childType == 3){
                llOrderSellButton1.setVisibility(View.GONE);
                llOrderSellButton.setVisibility(View.GONE);
            }else if(childType == 4){
                llOrderSellButton1.setVisibility(View.GONE);
                llOrderSellButton.setVisibility(View.GONE);
            }
        }
    }

    private OrderBuyClickListenering listenering;
    public interface OrderBuyClickListenering{
        void orderBuyClick(int type,String tradeId);//1:申诉  2:取消订单
    }
}
