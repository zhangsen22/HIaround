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
import hiaround.android.com.app.AccountManager;
import hiaround.android.com.app.Constants;
import hiaround.android.com.modle.MySellOrBuyinfoItem;
import hiaround.android.com.modle.SellResponse;
import hiaround.android.com.ui.activity.BusinessSellDetailsActivity;
import hiaround.android.com.ui.adapter.poweradapter.PowerAdapter;
import hiaround.android.com.ui.adapter.poweradapter.PowerHolder;

public class OrderSellDetailsAdapter extends PowerAdapter<MySellOrBuyinfoItem> {
    private static final String TAG = OrderSellDetailsAdapter.class.getSimpleName();
    private BaseActivity mContext;
    private int childType;
    private LayoutInflater inflater;

    public OrderSellDetailsAdapter(BaseActivity context, int childType, OrderSellClickListenering listenering) {
        super();
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.childType = childType;
        this.listenering = listenering;
    }

    @Override
    public PowerHolder<MySellOrBuyinfoItem> onViewHolderCreate(@NonNull ViewGroup parent, int viewType) {
        return new OrderSellDetailsHolder(inflater.inflate(R.layout.order_sell_details_item, parent, false));
    }

    @Override
    public void onViewHolderBind(@NonNull PowerHolder<MySellOrBuyinfoItem> holder, int position) {
        ((OrderSellDetailsHolder) holder).onBind(list.get(position), position);
    }

    private class OrderSellDetailsHolder extends PowerHolder<MySellOrBuyinfoItem> {
        private CountDownTimer timer;
        private CountDownTimer timer1;
        TextView tvOrderSellShuoming;
        TextView tvOrderSellTime;
        TextView tvOrderSellStatus;
        TextView tvOrderSellPrice;
        TextView tvOrderSellAmount;
        TextView tvOrderSellPaycode;
        TextView tvOrderSellDjs;
        TextView tvOrderSellSstime;
        TextView tvOrderSellSs;
        TextView tvOrderSellQfb;
        LinearLayout llOrderSellButton;

        public OrderSellDetailsHolder(View itemView) {
            super(itemView);
            tvOrderSellShuoming = itemView.findViewById(R.id.tv_order_sell_shuoming);
            tvOrderSellTime = itemView.findViewById(R.id.tv_order_sell_time);
            tvOrderSellStatus = itemView.findViewById(R.id.tv_order_sell_status);
            tvOrderSellPrice = itemView.findViewById(R.id.tv_order_sell_price);
            tvOrderSellAmount = itemView.findViewById(R.id.tv_order_sell_amount);
            tvOrderSellPaycode = itemView.findViewById(R.id.tv_order_sell_paycode);
            tvOrderSellDjs = itemView.findViewById(R.id.tv_order_sell_djs);
            tvOrderSellSstime = itemView.findViewById(R.id.tv_order_sell_sstime);
            tvOrderSellSs = itemView.findViewById(R.id.tv_order_sell_ss);
            tvOrderSellQfb = itemView.findViewById(R.id.tv_order_sell_qfb);
            llOrderSellButton = itemView.findViewById(R.id.ll_order_sell_button);
        }

        @Override
        public void onBind(@NonNull final MySellOrBuyinfoItem mySellOrBuyinfoItem, int position) {
            int tradeSource = mySellOrBuyinfoItem.getTradeSource();
            if(tradeSource == 1){
                tvOrderSellShuoming.setText("卖");
                tvOrderSellShuoming.setBackgroundResource(R.mipmap.bu);
            }else {
                tvOrderSellShuoming.setText("充");
                tvOrderSellShuoming.setBackgroundResource(R.mipmap.bu);
            }
            long createTime = mySellOrBuyinfoItem.getCreateTime();
            final long payTime = mySellOrBuyinfoItem.getPayTime();
            tvOrderSellTime.setText(DateUtil.getCurrentDateString1(createTime));
            int status = mySellOrBuyinfoItem.getStatus();
            if (status == 1) {
                tvOrderSellStatus.setText("等待付款");
            } else if (status == 2) {
                tvOrderSellStatus.setText("等待放币");
            } else if (status == 10) {
                tvOrderSellStatus.setText("完成");
            } else if (status == 20) {
                tvOrderSellStatus.setText("申诉中");
            } else if (status == 30) {
                tvOrderSellStatus.setText("超时取消");
            } else if (status == 40) {
                tvOrderSellStatus.setText("已关闭");
            }
            tvOrderSellPrice.setText(new DecimalFormat("0.00").format(mySellOrBuyinfoItem.getPrice()));
            tvOrderSellAmount.setText(new DecimalFormat("0.00").format(mySellOrBuyinfoItem.getPrice() * mySellOrBuyinfoItem.getNum()));
            tvOrderSellPaycode.setText(new DecimalFormat("0.00").format(mySellOrBuyinfoItem.getNum()));

            if (childType == 1) {
                if (status == 1) {
                    if(tradeSource == 1){
                        long currentTime = System.currentTimeMillis();
                        llOrderSellButton.setVisibility(View.GONE);
                        if (currentTime >= createTime + 10 * 60 * 1000) {
                            tvOrderSellDjs.setVisibility(View.GONE);
                        } else {
                            if(timer != null){
                                timer.cancel();
                                timer = null;
                            }
                            timer = new CountDownTimer(createTime + 10 * 60 * 1000 - currentTime, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    int left = (int) ((millisUntilFinished - 1000) / 1000);
                                    GALogger.d(TAG, "left       " + left);
                                    if (left > 0) {
                                        tvOrderSellDjs.setVisibility(View.VISIBLE);
                                        tvOrderSellDjs.setText(DateUtil.getCurrentDateString2(millisUntilFinished) + "后取消订单");
                                    } else {
                                        tvOrderSellDjs.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onFinish() {
                                }
                            };
                            timer.start();
                        }
                    }else {
                        Status2ChuLi(createTime,mySellOrBuyinfoItem);
                    }
                } else if (status == 2) {
                    Status2ChuLi(payTime,mySellOrBuyinfoItem);
                } else {
                    tvOrderSellDjs.setVisibility(View.GONE);
                    llOrderSellButton.setVisibility(View.GONE);
                }
            } else if (childType == 2) {
                tvOrderSellDjs.setVisibility(View.GONE);
                llOrderSellButton.setVisibility(View.GONE);
            } else if (childType == 3) {
                tvOrderSellDjs.setVisibility(View.GONE);
                llOrderSellButton.setVisibility(View.GONE);
            } else if (childType == 4) {
                tvOrderSellDjs.setVisibility(View.GONE);
                llOrderSellButton.setVisibility(View.GONE);
            }
        }


        private void Status2ChuLi(long time,MySellOrBuyinfoItem mySellOrBuyinfoItem){
            if(timer != null){
                timer.cancel();
                timer = null;
            }
            tvOrderSellDjs.setVisibility(View.GONE);
            llOrderSellButton.setVisibility(View.VISIBLE);
            long currentTime = System.currentTimeMillis();
            if (currentTime >= time + 10 * 60 * 1000) {
                tvOrderSellSstime.setVisibility(View.GONE);
                tvOrderSellSs.setEnabled(true);
            } else {
                if(timer1 != null){
                    timer1.cancel();
                    timer1 = null;
                }
                timer1 = new CountDownTimer(time + 10 * 60 * 1000 - currentTime, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (mContext != null) {
                            int left = (int) ((millisUntilFinished - 1000) / 1000);
                            GALogger.d(TAG, "left       " + left);
                            if (left > 0) {
                                tvOrderSellSs.setEnabled(false);
                                tvOrderSellSstime.setVisibility(View.VISIBLE);
                                tvOrderSellSstime.setText(DateUtil.getCurrentDateString2(millisUntilFinished) + "后可申诉");
                            } else {
                                tvOrderSellDjs.setVisibility(View.GONE);
                                tvOrderSellSs.setEnabled(true);
                            }
                        }
                    }

                    @Override
                    public void onFinish() {
                    }
                };
                timer1.start();
            }
            tvOrderSellQfb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BusinessSellDetailsActivity.startThis(mContext, new SellResponse(mySellOrBuyinfoItem.getTradeid(), mySellOrBuyinfoItem.getPayCode(), mySellOrBuyinfoItem.getCreateTime()), mySellOrBuyinfoItem.getPrice(), mySellOrBuyinfoItem.getNum(), AccountManager.getInstance().getNickname(),Constants.REQUESTCODE_12);
                }
            });
            tvOrderSellSs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listenering != null) {
                        listenering.orderSellClick(mySellOrBuyinfoItem.getTradeid());
                    }
                }
            });

        }
    }

    private OrderSellClickListenering listenering;

    public interface OrderSellClickListenering {
        void orderSellClick(String tradeId);
    }
}
