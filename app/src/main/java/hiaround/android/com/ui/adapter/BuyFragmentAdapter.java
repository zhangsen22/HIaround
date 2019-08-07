package hiaround.android.com.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.growalong.util.util.GALogger;
import java.text.DecimalFormat;
import hiaround.android.com.MyApplication;
import hiaround.android.com.R;
import hiaround.android.com.modle.BuyItem;
import hiaround.android.com.ui.activity.BusinessBuyActivity;
import hiaround.android.com.ui.adapter.poweradapter.PowerAdapter;
import hiaround.android.com.ui.adapter.poweradapter.PowerHolder;

public class BuyFragmentAdapter extends PowerAdapter<BuyItem> {

    private static final String TAG = BuyFragmentAdapter.class.getSimpleName();
    private Context mContext;

    private LayoutInflater inflater;

    public BuyFragmentAdapter(Context context) {
        super();
        mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public PowerHolder<BuyItem> onViewHolderCreate(@NonNull ViewGroup parent, int viewType) {
        return new BuyItemHolder(inflater.inflate(R.layout.buy_item, parent, false));
    }

    @Override
    public void onViewHolderBind(@NonNull PowerHolder<BuyItem> holder, int position) {
        ((BuyItemHolder) holder).onBind(list.get(position), position);
    }

    public class BuyItemHolder extends PowerHolder<BuyItem> {
        TextView tvName;
        TextView tvTradetimes;
        TextView tvTradesuccrate;
        TextView tvNumber;
        TextView tvMinPrice;
        TextView tvMaxPrice;
        TextView tvSinglePrice;
        TextView tvBuy;
        ImageView ivIdcard;
        ImageView ivAilpay;
        ImageView ivWebpay;
        ImageView ivApiType;
        LinearLayout llBuy;

        public BuyItemHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            ivApiType = itemView.findViewById(R.id.iv_api_type);
            tvTradetimes = itemView.findViewById(R.id.tv_tradetimes);
            tvTradesuccrate = itemView.findViewById(R.id.tv_tradesuccrate);
            tvNumber = itemView.findViewById(R.id.tv_number);
            tvMinPrice = itemView.findViewById(R.id.tv_min_price);
            tvMaxPrice = itemView.findViewById(R.id.tv_max_price);
            tvSinglePrice = itemView.findViewById(R.id.tv_single_price);
            tvBuy = itemView.findViewById(R.id.tv_buy);
            ivIdcard = itemView.findViewById(R.id.iv_idcard);
            ivAilpay = itemView.findViewById(R.id.iv_ailpay);
            ivWebpay = itemView.findViewById(R.id.iv_webpay);
            llBuy = itemView.findViewById(R.id.ll_buy);
        }

        @Override
        public void onBind(@NonNull final BuyItem buyItem, int position) {
            if (buyItem != null) {
                GALogger.d(TAG, "buyItem    " + buyItem.toString());
                tvBuy.setText("去购买");
                String nickname = buyItem.getNickname();
                if (!TextUtils.isEmpty(nickname)) {
                    tvName.setText(nickname);
                }
                if (buyItem.getApiType() == 1) {
                    ivApiType.setVisibility(View.VISIBLE);
                    ivApiType.setImageResource(R.mipmap.st);
                } else {
                    ivApiType.setVisibility(View.GONE);
                }
                tvTradetimes.setText(buyItem.getTradeTimes() + "");
                tvTradesuccrate.setText(buyItem.getTradeSuccRate() + "%");
                tvNumber.setText(new DecimalFormat("0.00").format(buyItem.getMaxNum()));
                tvMinPrice.setText(MyApplication.appContext.getResources().getString(R.string.rmb) + new DecimalFormat("0.00").format(buyItem.getPrice() * buyItem.getMinNum()));
                tvMaxPrice.setText(MyApplication.appContext.getResources().getString(R.string.rmb) + new DecimalFormat("0.00").format(buyItem.getPrice() * buyItem.getMaxNum()));
                tvSinglePrice.setText(MyApplication.appContext.getResources().getString(R.string.rmb) + new DecimalFormat("0.00").format(buyItem.getPrice()));
                if (buyItem.isSupportBank()) {
                    ivIdcard.setVisibility(View.VISIBLE);
                } else {
                    ivIdcard.setVisibility(View.GONE);
                }

                if (buyItem.isSupportAli()) {
                    ivAilpay.setVisibility(View.VISIBLE);
                } else {
                    ivAilpay.setVisibility(View.GONE);
                }

                if (buyItem.isSupportWechat()) {
                    ivWebpay.setVisibility(View.VISIBLE);
                } else {
                    ivWebpay.setVisibility(View.GONE);
                }

                llBuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BusinessBuyActivity.startThis(mContext, buyItem);
                    }
                });
            }
        }
    }
}
