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
import aimi.android.com.modle.LaCaraPayeeItemModel;
import aimi.android.com.modle.LaCaraPayeeItemModelPayee;
import aimi.android.com.ui.adapter.poweradapter.PowerAdapter;
import aimi.android.com.ui.adapter.poweradapter.PowerHolder;


public class LaCaraListAdapter extends PowerAdapter<LaCaraPayeeItemModel> {
    private static final String TAG = LaCaraListAdapter.class.getSimpleName();
    private Map<Integer, Boolean> map = new HashMap<>();
    private boolean onBind;
    private int checkedPosition = -1;
    private Context mContext;
    private LayoutInflater inflater;
    private OnLaCaraCheckListener onLaCaraCheckListener;
    private long defalutId;

    public LaCaraListAdapter(Context context, OnLaCaraCheckListener onLaCaraCheckListener) {
        super();
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.onLaCaraCheckListener = onLaCaraCheckListener;
    }

    //得到当前选中的位置
    public int getCheckedPosition() {
        return checkedPosition;
    }

    @Override
    public PowerHolder<LaCaraPayeeItemModel> onViewHolderCreate(@NonNull ViewGroup parent, int viewType) {
        return new LaCaraListHolder(inflater.inflate(R.layout.lacara_pay_list_item, parent, false));
    }

    @Override
    public void onViewHolderBind(@NonNull PowerHolder<LaCaraPayeeItemModel> holder, int position) {
        ((LaCaraListHolder) holder).onBind(list.get(position), position);
    }

    public void setDefaultId(long defalut) {
        this.defalutId = defalut;
    }

    private class LaCaraListHolder extends PowerHolder<LaCaraPayeeItemModel> {
        TextView tvWebchatCode;
        CheckBox tvWebchatCheck;
        TextView tvWebchatLastmoney;
        TextView tvWebchatLastnum;
        TextView tvShuoming;
        TextView tvPayDelete;
        TextView tvReedit;
        TextView tvWebchatName;

        public LaCaraListHolder(View itemView) {
            super(itemView);
            tvWebchatCode = itemView.findViewById(R.id.tv_webchat_code);
            tvWebchatCheck = itemView.findViewById(R.id.tv_webchat_check);
            tvWebchatLastmoney = itemView.findViewById(R.id.tv_webchat_lastmoney);
            tvWebchatLastnum = itemView.findViewById(R.id.tv_webchat_lastnum);
            tvShuoming = itemView.findViewById(R.id.tv_shuoming);
            tvPayDelete = itemView.findViewById(R.id.tv_pay_delete);
            tvReedit = itemView.findViewById(R.id.tv_reedit);
            tvWebchatName = itemView.findViewById(R.id.tv_webchat_name);
        }

        @Override
        public void onBind(@NonNull final LaCaraPayeeItemModel laCaraPayeeItemModel, final int position) {
            GALogger.d(TAG, "position           " + position);
            tvWebchatLastmoney.setText(new DecimalFormat("0.00").format(laCaraPayeeItemModel.getLeftMoney()/100));
            tvWebchatLastnum.setText(laCaraPayeeItemModel.getLeftTimes() + "");
            LaCaraPayeeItemModelPayee payee = laCaraPayeeItemModel.getPayee();
            if(payee != null){
                tvWebchatCode.setText(payee.getAccount());
                tvWebchatName.setText(payee.getName());
                boolean watchUnbind = payee.isWatchUnbind();
                boolean watchStop = payee.isWatchStop();
                if(watchUnbind){
                    tvWebchatCheck.setVisibility(View.GONE);
                    tvShuoming.setText("已失效");
                    tvShuoming.setTextColor(mContext.getResources().getColor(R.color.color_ff0000));
                    tvReedit.setVisibility(View.GONE);
                }else {
                    if (watchStop) {
                        tvWebchatCheck.setVisibility(View.GONE);
                        tvShuoming.setText("绑定中");
                        tvShuoming.setTextColor(mContext.getResources().getColor(R.color.color_ff0000));
//                        tvReedit.setVisibility(View.VISIBLE);
                    } else {
                        tvReedit.setVisibility(View.GONE);
                        if (defalutId > 0) {
                            if (defalutId == payee.getId()) {
                                map.clear();
                                map.put(position, true);
                                checkedPosition = position;
                                tvWebchatCheck.setVisibility(View.GONE);
                                tvShuoming.setText("默认");
                                tvShuoming.setTextColor(mContext.getResources().getColor(R.color.color_ff0000));
                            } else {
                                map.remove(position);
                                if (map.size() == 0) {
                                    checkedPosition = -1; //-1 代表一个都未选择
                                }
                                tvWebchatCheck.setVisibility(View.VISIBLE);
                                tvShuoming.setText("设为默认");
                                tvShuoming.setTextColor(mContext.getResources().getColor(R.color.color_666666));
                            }
                        }
                    }
                }

                tvReedit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onLaCaraCheckListener != null) {
                            if(watchStop){
                                onLaCaraCheckListener.onWebChatLaCaraEdit(position, payee);
                            }
                        }
                    }
                });
                tvPayDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onLaCaraCheckListener != null) {
                            onLaCaraCheckListener.onLaCaraDelete(position, payee);
                        }
                    }
                });
                tvWebchatCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (buttonView.isPressed()) {
                            GALogger.d(TAG, "isChecked           " + isChecked);
                            if (isChecked == true) {
                                map.clear();
                                map.put(position, true);
                                checkedPosition = position;
                                if (onLaCaraCheckListener != null) {
                                    onLaCaraCheckListener.onLaCaraCheck(position, payee);
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
                    tvWebchatCheck.setChecked(true);
                } else {
                    tvWebchatCheck.setChecked(false);
                }
                onBind = false;
            }
        }
    }

    public interface OnLaCaraCheckListener {
        void onLaCaraCheck(int position, LaCaraPayeeItemModelPayee laCaraPayeeItemModelPayee);
        void onLaCaraDelete(int position, LaCaraPayeeItemModelPayee laCaraPayeeItemModelPayee);
        void onWebChatLaCaraEdit(int position, LaCaraPayeeItemModelPayee laCaraPayeeItemModelPayee);
    }
}
