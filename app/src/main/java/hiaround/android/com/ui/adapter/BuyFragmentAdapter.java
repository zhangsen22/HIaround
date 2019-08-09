package hiaround.android.com.ui.adapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.growalong.util.util.GALogger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import hiaround.android.com.R;

public class BuyFragmentAdapter extends RecyclerView.Adapter {

    private static final String TAG = BuyFragmentAdapter.class.getSimpleName();
    private Context mContext;
    private List<String> mPriceList;
    private LayoutInflater inflater;
    private Map<Integer, Boolean> map = new HashMap<>();
    private boolean onBind;
    private int checkedPosition = -1;
    private OnBuyCheckListener onBuyCheckListener;
    private String unit;

    public BuyFragmentAdapter(Context context, List<String> priceList,OnBuyCheckListener onCheckListener) {
        super();
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.mPriceList = priceList;
        this.onBuyCheckListener = onCheckListener;
    }

    public int getCheckedPosition() {
        return checkedPosition;
    }

    public void setCheckedPosition(int checkedPosition) {
        this.checkedPosition = checkedPosition;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new BuyItemHolder(inflater.inflate(R.layout.buy_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ((BuyItemHolder) viewHolder).onBind(mPriceList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mPriceList.size();
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public class BuyItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cb_buy_check)
        CheckBox cbBuyCheck;
        @BindView(R.id.tv_buy_price)
        TextView tvBuyPrice;
        public BuyItemHolder(View itemView) {
            super(itemView);
            cbBuyCheck = itemView.findViewById(R.id.cb_buy_check);
            tvBuyPrice = itemView.findViewById(R.id.tv_buy_price);
        }

        public void onBind(String s, int position) {
            tvBuyPrice.setText(s+unit);
            cbBuyCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (buttonView.isPressed()) {
                        GALogger.d(TAG, "isChecked           " + isChecked);
                        if (isChecked == true) {
                            map.clear();
                            map.put(position, true);
                            checkedPosition = position;
                            if (onBuyCheckListener != null) {
                                onBuyCheckListener.onBuyCheck(position, s);
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
                cbBuyCheck.setChecked(true);
            } else {
                cbBuyCheck.setChecked(false);
            }
            onBind = false;
        }
    }

    public interface OnBuyCheckListener {
        void onBuyCheck(int position, String s);
    }
}
