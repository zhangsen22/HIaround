
package aimi.android.com.ui.adapter.poweradapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

public class PowerHolder<T> extends RecyclerView.ViewHolder {
    public boolean enableCLick = true;

    public PowerHolder(@NonNull View itemView) {
        super(itemView);
    }

    public PowerHolder(@NonNull View itemView, boolean enableCLick) {
        super(itemView);
        this.enableCLick = enableCLick;
    }

    public void onBind() {
    }


    public void onBind(@NonNull T t) {
    }

    public void onBind(@NonNull T t, boolean isSelectMode) {

    }

    public void onBind(@NonNull T t, int position) {

    }

    public void onPartBind(@NonNull T t, @NonNull List<Object> payloads) {

    }

    public void onPartBind(@NonNull T t, boolean isSelectMode, @NonNull List<Object> payloads) {

    }

    public @NonNull
    Context getContext() {
        return itemView.getContext();
    }
}
