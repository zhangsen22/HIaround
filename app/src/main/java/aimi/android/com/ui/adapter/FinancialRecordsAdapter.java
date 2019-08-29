package aimi.android.com.ui.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.growalong.util.util.DateUtil;
import java.text.DecimalFormat;
import aimi.android.com.BaseActivity;
import aimi.android.com.R;
import aimi.android.com.modle.FinanceLogItem;
import aimi.android.com.ui.adapter.poweradapter.PowerAdapter;
import aimi.android.com.ui.adapter.poweradapter.PowerHolder;

public class FinancialRecordsAdapter extends PowerAdapter<FinanceLogItem> {
    private static final String TAG = FinancialRecordsAdapter.class.getSimpleName();
    private BaseActivity mContext;
    private LayoutInflater inflater;

    public FinancialRecordsAdapter(BaseActivity context) {
        super();
        mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public PowerHolder<FinanceLogItem> onViewHolderCreate(@NonNull ViewGroup parent, int viewType) {
        return new WalletAccountContentHolder(inflater.inflate(R.layout.wallet_account_content, parent, false));
    }

    @Override
    public void onViewHolderBind(@NonNull PowerHolder<FinanceLogItem> holder, int position) {
        ((WalletAccountContentHolder) holder).onBind(list.get(position), position);
    }

    private class WalletAccountContentHolder extends PowerHolder<FinanceLogItem> {
        ImageView ivImageType;
        TextView tvTextType;
        TextView tvAmountNum;
        TextView tvAmountStatus;
        TextView tvAmountTime;

        public WalletAccountContentHolder(View itemView) {
            super(itemView);
            ivImageType = itemView.findViewById(R.id.iv_image_type);
            tvTextType = itemView.findViewById(R.id.tv_text_type);
            tvAmountNum = itemView.findViewById(R.id.tv_amount_num);
            tvAmountStatus = itemView.findViewById(R.id.tv_amount_status);
            tvAmountTime = itemView.findViewById(R.id.tv_amount_time);
        }

        public void onBind(@NonNull FinanceLogItem financeLogItem, int position) {
            int type = financeLogItem.getType();
            if (type == 1 || type == 2) {
                ivImageType.setImageResource(R.mipmap.v);
            } else if (type == 3 || type == 4) {
                ivImageType.setImageResource(R.mipmap.w);
            }
            double num = financeLogItem.getNum();
            tvAmountNum.setText(new DecimalFormat("0.00").format(num));
            int status = financeLogItem.getStatus();
            if (status == 1) {
                tvAmountStatus.setText("确认中");
            } else if (status == 2) {
                tvAmountStatus.setText("已完成");
            } else if (status == 3) {
                tvAmountStatus.setText("取消");
            }
            if (type == 1) {
                tvTextType.setText("充币");
            } else if (type == 2) {
                tvTextType.setText("提币");
            } else if (type == 3) {
                tvTextType.setText("转入到钱包");
            } else if (type == 4) {
                tvTextType.setText("转出到交易账户");
            }
            long logtime = financeLogItem.getLogtime();
            long succtime = financeLogItem.getSucctime();
            if (succtime > 0) {
                tvAmountTime.setText(DateUtil.getCurrentDateString(succtime));
            } else {
                tvAmountTime.setText(DateUtil.getCurrentDateString(logtime));
            }
        }
    }
}
