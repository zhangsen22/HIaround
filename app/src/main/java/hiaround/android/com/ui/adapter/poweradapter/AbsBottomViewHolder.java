package hiaround.android.com.ui.adapter.poweradapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

@SuppressWarnings("WeakerAccess")
public abstract class AbsBottomViewHolder extends PowerHolder {

    public AbsBottomViewHolder(@NonNull View itemView) {
        super(itemView, false);
    }

    public abstract void onBind(@Nullable OnLoadMoreListener loadMoreListener, @LoadState int loadState);

}
