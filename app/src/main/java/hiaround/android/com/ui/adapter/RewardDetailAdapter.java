package hiaround.android.com.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;

import hiaround.android.com.R;
import hiaround.android.com.modle.RewardDetailItem;
import hiaround.android.com.ui.adapter.poweradapter.PowerAdapter;
import hiaround.android.com.ui.adapter.poweradapter.PowerHolder;

public class RewardDetailAdapter extends PowerAdapter<RewardDetailItem> {

    private static final String TAG = SellFragmentAdapter.class.getSimpleName();
    private Context mContext;

    private LayoutInflater inflater;

    public RewardDetailAdapter(Context context) {
        super();
        mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public PowerHolder<RewardDetailItem> onViewHolderCreate(@NonNull ViewGroup parent, int viewType) {
        return new RewardDetailHolder(inflater.inflate(R.layout.reward_detail_item, parent, false));
    }

    @Override
    public void onViewHolderBind(@NonNull PowerHolder<RewardDetailItem> holder, int position) {
        ((RewardDetailHolder) holder).onBind(list.get(position), position);
    }

    private class RewardDetailHolder extends PowerHolder<RewardDetailItem> {
        TextView tvDate;
        TextView tvJiangli;
        public RewardDetailHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvJiangli = itemView.findViewById(R.id.tv_jiangli);
        }

        @Override
        public void onBind(@NonNull RewardDetailItem rewardDetailItem, int position) {
            tvDate.setText(rewardDetailItem.getDate());
            tvJiangli.setText(new DecimalFormat("0.0000").format(rewardDetailItem.getValue()));
        }
    }
}
