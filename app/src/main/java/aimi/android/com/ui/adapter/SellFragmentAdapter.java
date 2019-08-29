package aimi.android.com.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import aimi.android.com.R;
import aimi.android.com.modle.BuyItem;
import aimi.android.com.ui.adapter.poweradapter.PowerAdapter;
import aimi.android.com.ui.adapter.poweradapter.PowerHolder;

public class SellFragmentAdapter extends PowerAdapter<BuyItem> {

    private static final String TAG = SellFragmentAdapter.class.getSimpleName();
    private Context mContext;

    private LayoutInflater inflater;

    public SellFragmentAdapter(Context context) {
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


        public BuyItemHolder(View itemView) {
            super(itemView);

        }
    }
}
