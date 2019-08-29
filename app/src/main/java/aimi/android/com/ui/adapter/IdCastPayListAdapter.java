package aimi.android.com.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.growalong.util.util.GALogger;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import aimi.android.com.R;
import aimi.android.com.modle.BankPayeeItemModel;
import aimi.android.com.modle.BankPayeeItemModelPayee;
import aimi.android.com.ui.adapter.poweradapter.PowerAdapter;
import aimi.android.com.ui.adapter.poweradapter.PowerHolder;

public class IdCastPayListAdapter extends PowerAdapter<BankPayeeItemModel> {
    private static final String TAG = IdCastPayListAdapter.class.getSimpleName();
    private Map<Integer, Boolean> map = new HashMap<>();
    private boolean onBind;
    private int checkedPosition = -1;
    private Context mContext;
    private LayoutInflater inflater;
    private OnIdCastCheckListener onIdCastCheckListener;
    private long defalutId;

    public IdCastPayListAdapter(Context context, OnIdCastCheckListener onCheckListener) {
        super();
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.onIdCastCheckListener = onCheckListener;
    }

    @Override
    public PowerHolder<BankPayeeItemModel> onViewHolderCreate(@NonNull ViewGroup parent, int viewType) {
        return new IdCastPayHolder(inflater.inflate(R.layout.id_cast_pay_item_view, parent, false));
    }

    @Override
    public void onViewHolderBind(@NonNull PowerHolder<BankPayeeItemModel> holder, int position) {
        ((IdCastPayHolder) holder).onBind(list.get(position), position);
    }

    //得到当前选中的位置
    public int getCheckedPosition() {
        return checkedPosition;
    }

    public void setDefaultId(long defalut) {
        this.defalutId = defalut;
    }

    private class IdCastPayHolder extends PowerHolder<BankPayeeItemModel> {
        TextView tvIdcastNum;
        TextView tvIdcastBankname;
        CheckBox tvIdcastCheck;
        TextView tvIdcastLastmoney;
        TextView tvIdcastLastnum;
        TextView tvShuoming;
        TextView tvPayDelete;

        public IdCastPayHolder(View itemView) {
            super(itemView);
            tvIdcastNum = itemView.findViewById(R.id.tv_idcast_num);
            tvIdcastBankname = itemView.findViewById(R.id.tv_idcast_bankname);
            tvIdcastCheck = itemView.findViewById(R.id.tv_idcast_check);
            tvIdcastLastmoney = itemView.findViewById(R.id.tv_idcast_lastmoney);
            tvIdcastLastnum = itemView.findViewById(R.id.tv_idcast_lastnum);
            tvShuoming = itemView.findViewById(R.id.tv_shuoming);
            tvPayDelete = itemView.findViewById(R.id.tv_pay_delete);
        }

        @Override
        public void onBind(@NonNull final BankPayeeItemModel bankPayeeItemModel, final int position) {
            GALogger.d(TAG, "position           " + position);
            BankPayeeItemModelPayee payee = bankPayeeItemModel.getPayee();
            if (payee != null) {
                tvIdcastNum.setText(payee.getAccount());
                tvIdcastBankname.setText(payee.getBankName());
                if (defalutId > 0) {
                    if (defalutId == payee.getId()) {
                        map.clear();
                        map.put(position, true);
                        checkedPosition = position;
                        tvShuoming.setText("默认");
                        tvIdcastCheck.setVisibility(View.GONE);
                        tvShuoming.setTextColor(mContext.getResources().getColor(R.color.color_ff0000));
                    } else {
                        map.remove(position);
                        if (map.size() == 0) {
                            checkedPosition = -1; //-1 代表一个都未选择
                        }
                        tvShuoming.setText("设为默认");
                        tvIdcastCheck.setVisibility(View.VISIBLE);
                        tvShuoming.setTextColor(mContext.getResources().getColor(R.color.color_666666));
                    }
                }
            }
            tvIdcastLastmoney.setText(new DecimalFormat("0.00").format(bankPayeeItemModel.getLeftMoney()/100));
            tvIdcastLastnum.setText(bankPayeeItemModel.getLeftTimes() + "");
            tvPayDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onIdCastCheckListener != null) {
                        onIdCastCheckListener.onIdCastDelete(position, bankPayeeItemModel);
                    }
                }
            });
            tvIdcastCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (buttonView.isPressed()) {
                        GALogger.d(TAG, "isChecked           " + isChecked);
                        if (isChecked == true) {
                            map.clear();
                            map.put(position, true);
                            checkedPosition = position;
                            if (onIdCastCheckListener != null) {
                                onIdCastCheckListener.onIdCastCheck(position, bankPayeeItemModel);
                            }
                        } else {
                            map.remove(position);
                            if (map.size() == 0) {
                                checkedPosition = -1; //-1 代表一个都未选择
                            }
                        }
                        if (!onBind) {
                            notifyDataSetChanged();
                        }
                    }
                }
            });
            onBind = true;
            if (map != null && map.containsKey(position)) {
                tvIdcastCheck.setChecked(true);
            } else {
                tvIdcastCheck.setChecked(false);
            }
            onBind = false;
        }
    }

    public interface OnIdCastCheckListener {
        void onIdCastCheck(int position, BankPayeeItemModel bankPayeeItemModel);
        void onIdCastDelete(int position, BankPayeeItemModel bankPayeeItemModel);
    }
}
