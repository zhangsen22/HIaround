package aimi.android.com.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.growalong.util.util.GALogger;
import java.util.ArrayList;
import java.util.List;

import aimi.android.com.R;
import aimi.android.com.modle.LaCaraWenChatListItem;

public class LaCaraWebListAdapter extends RecyclerView.Adapter {
    private static final String TAG = LaCaraWebListAdapter.class.getSimpleName();
    private Context mContext;
    private LayoutInflater inflater;
    private List<LaCaraWenChatListItem> list;

    public LaCaraWebListAdapter(Context context, OnItemClickListener onItemClickListener) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        list = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
    }

    public List<LaCaraWenChatListItem> getList() {
        return list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        return new AliPayListHolder(inflater.inflate(R.layout.lacara_web_pay_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ((AliPayListHolder) viewHolder).onBind(list.get(position), position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class AliPayListHolder extends RecyclerView.ViewHolder {
        TextView tvWebchatName;
        LinearLayout llIsShixiao;

        public AliPayListHolder(View itemView) {
            super(itemView);
            tvWebchatName = itemView.findViewById(R.id.tv_webchat_name);
            llIsShixiao = itemView.findViewById(R.id.ll_is_shixiao);
        }

        public void onBind(@NonNull final LaCaraWenChatListItem laCaraWenChatListItem, final int position) {
            GALogger.d(TAG, "position           " + position);
            String account = laCaraWenChatListItem.getAccount();
            tvWebchatName.setText(account);
            boolean watchUnbind = laCaraWenChatListItem.isWatchUnbind();
            if(watchUnbind){
                llIsShixiao.setVisibility(View.VISIBLE);
                itemView.setClickable(false);
            }else {
                llIsShixiao.setVisibility(View.GONE);
                itemView.setClickable(true);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemClickListener != null){
                        onItemClickListener.onItemClick(position,laCaraWenChatListItem.getPaymentId(),account);
                    }
                }
            });
        }
    }

    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(int position, long paymentId, String account);
    }

}
