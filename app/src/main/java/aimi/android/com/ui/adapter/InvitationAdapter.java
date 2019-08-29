package aimi.android.com.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.text.DecimalFormat;
import aimi.android.com.R;
import aimi.android.com.modle.InvitationItem;
import aimi.android.com.ui.adapter.poweradapter.PowerAdapter;
import aimi.android.com.ui.adapter.poweradapter.PowerHolder;

public class InvitationAdapter extends PowerAdapter<InvitationItem> {

    private static final String TAG = InvitationAdapter.class.getSimpleName();
    private Context mContext;
    private LayoutInflater inflater;

    public InvitationAdapter(Context context) {
        super();
        mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public PowerHolder<InvitationItem> onViewHolderCreate(@NonNull ViewGroup parent, int viewType) {
        return new InvitationHolder(inflater.inflate(R.layout.invitation_item, parent, false));
    }

    @Override
    public void onViewHolderBind(@NonNull PowerHolder<InvitationItem> holder, int position) {
        ((InvitationHolder) holder).onBind(list.get(position), position);
    }

    private class InvitationHolder extends PowerHolder<InvitationItem> {
        TextView tvInvitationName;
        TextView tvInvitationPhone;
        TextView tvInvitationMoney;

        public InvitationHolder(View itemView) {
            super(itemView);
            tvInvitationName = itemView.findViewById(R.id.tv_invitation_name);
            tvInvitationPhone = itemView.findViewById(R.id.tv_invitation_phone);
            tvInvitationMoney = itemView.findViewById(R.id.tv_invitation_money);
        }

        @Override
        public void onBind(@NonNull InvitationItem invitationItem, int position) {
            if(invitationItem != null){
                tvInvitationName.setText(invitationItem.getNickname());
                tvInvitationPhone.setText(invitationItem.getPhone());
                tvInvitationMoney.setText(new DecimalFormat("0.00").format(invitationItem.getMoney()));
            }
        }
    }
}
