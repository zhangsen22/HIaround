package hiaround.android.com.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.growalong.util.util.DateUtil;
import java.text.DecimalFormat;
import hiaround.android.com.R;
import hiaround.android.com.modle.MyEntrustinfoItem;
import hiaround.android.com.ui.adapter.poweradapter.PowerAdapter;
import hiaround.android.com.ui.adapter.poweradapter.PowerHolder;

public class OrderEntrustDetailsAdapter extends PowerAdapter<MyEntrustinfoItem> {
    private static final String TAG = OrderEntrustDetailsAdapter.class.getSimpleName();
    private Context mContext;
    private int childType;
    private LayoutInflater inflater;

    public OrderEntrustDetailsAdapter(Context context, int childType,OrderEntrustClickListenering listenering) {
        super();
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.childType = childType;
        this.listenering = listenering;
    }

    @Override
    public PowerHolder<MyEntrustinfoItem> onViewHolderCreate(@NonNull ViewGroup parent, int viewType) {
        return new OrderSellDetailsHolder(inflater.inflate(R.layout.order_entrust_details_item, parent, false));
    }

    @Override
    public void onViewHolderBind(@NonNull PowerHolder<MyEntrustinfoItem> holder, int position) {
        ((OrderSellDetailsHolder) holder).onBind(list.get(position), position);
    }

    private class OrderSellDetailsHolder extends PowerHolder<MyEntrustinfoItem> {
        TextView tvOrderSellTime;
        TextView tvOrderSellStatus;
        TextView tvOrderSellPrice;
        TextView tvOrderSellAmount;
        TextView tvOrderSellPaycode;
        TextView tvOrderSellQfb;
        TextView tvBuySell;

        public OrderSellDetailsHolder(View itemView) {
            super(itemView);
            tvBuySell = itemView.findViewById(R.id.tv_buy_sell);
            tvOrderSellTime = itemView.findViewById(R.id.tv_order_sell_time);
            tvOrderSellStatus = itemView.findViewById(R.id.tv_order_sell_status);
            tvOrderSellPrice = itemView.findViewById(R.id.tv_order_sell_price);
            tvOrderSellAmount = itemView.findViewById(R.id.tv_order_sell_amount);
            tvOrderSellPaycode = itemView.findViewById(R.id.tv_order_sell_paycode);
            tvOrderSellQfb = itemView.findViewById(R.id.tv_order_sell_qfb);
        }

        @Override
        public void onBind(@NonNull final MyEntrustinfoItem myEntrustinfoItem, int position) {
            if(childType == 1){
                tvBuySell.setText("买");
                tvBuySell.setBackgroundResource(R.mipmap.aj);
            }else if(childType == 2){
                tvBuySell.setText("卖");
                tvBuySell.setBackgroundResource(R.mipmap.aj);
            }

            long puttime = myEntrustinfoItem.getPuttime();
            tvOrderSellTime.setText(DateUtil.getCurrentDateString1(puttime));
            int status = myEntrustinfoItem.getStatus();
            if(status == 0){
                tvOrderSellStatus.setText("当前正常挂单");
            }else if(status == 1){
                tvOrderSellStatus.setText("已完成");
            }else if(status == 2){
                tvOrderSellStatus.setText("已撤销");
            }
            tvOrderSellPrice.setText(new DecimalFormat("0.00").format(myEntrustinfoItem.getPrice()));
            tvOrderSellAmount.setText(new DecimalFormat("0.00").format(myEntrustinfoItem.getPrice()*myEntrustinfoItem.getMaxNum()));
            tvOrderSellPaycode.setText(new DecimalFormat("0.00").format(myEntrustinfoItem.getMaxNum()));

            tvOrderSellQfb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listenering != null){
                        listenering.vv(myEntrustinfoItem.getId(),childType - 1);
                    }
                }
            });
        }
    }

    private OrderEntrustClickListenering listenering;
    public interface OrderEntrustClickListenering{
        void vv(long id,int type);
    }
}
