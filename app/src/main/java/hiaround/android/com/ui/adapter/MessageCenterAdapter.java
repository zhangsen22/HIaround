package hiaround.android.com.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import hiaround.android.com.R;
import hiaround.android.com.modle.MessageCenterItem;
import hiaround.android.com.ui.adapter.poweradapter.PowerAdapter;
import hiaround.android.com.ui.adapter.poweradapter.PowerHolder;

public class MessageCenterAdapter extends PowerAdapter<MessageCenterItem> {
    private static final String TAG = MessageCenterAdapter.class.getSimpleName();

    private Context mContext;
    private LayoutInflater inflater;

    public MessageCenterAdapter(Context context) {
        super();
        mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public PowerHolder<MessageCenterItem> onViewHolderCreate(@NonNull ViewGroup parent, int viewType) {
        return new MessageCenterHolder(inflater.inflate(R.layout.message_center_item, parent, false));
    }

    @Override
    public void onViewHolderBind(@NonNull PowerHolder<MessageCenterItem> holder, int position) {
        ((MessageCenterHolder) holder).onBind(list.get(position), position);
    }

    private class MessageCenterHolder extends PowerHolder<MessageCenterItem> {
        TextView tvMsgContent;
        TextView tvMsgTime;
        public MessageCenterHolder(View itemView) {
            super(itemView);
            tvMsgContent = itemView.findViewById(R.id.tv_msg_content);
            tvMsgTime = itemView.findViewById(R.id.tv_msg_time);
        }

        @Override
        public void onBind(@NonNull MessageCenterItem messageCenterItem, int position) {
            tvMsgContent.setText(messageCenterItem.getContent());
            tvMsgTime.setText(messageCenterItem.getDate());
        }
    }
}
