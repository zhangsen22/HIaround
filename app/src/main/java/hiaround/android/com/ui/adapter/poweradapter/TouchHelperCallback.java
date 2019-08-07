package hiaround.android.com.ui.adapter.poweradapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.growalong.util.util.GALogger;

import java.util.Collections;
import java.util.List;

public class TouchHelperCallback extends ItemTouchHelper.Callback {


    private static final String TAG = TouchHelperCallback.class.getSimpleName();
    private final PowerAdapter adapter;
    public boolean isDrag = false;//拖拽结束
    public boolean handLift = false;// 手指抬起

    public TouchHelperCallback(@NonNull PowerAdapter adapter,@NonNull ItemDragCallBack itemDragCallBack) {
        this.adapter = checkIsNull(adapter);
        this.dragCallBack = itemDragCallBack;
    }

    private <T> T checkIsNull(T t) {
        if (t == null) {
            throw new NullPointerException();
        }
        return t;
    }

    /**
     * 是否处理滑动事件 以及拖拽和滑动的方向 如果是列表类型的RecyclerView的只存在UP和DOWN，如果是网格类RecyclerView则还应该多有LEFT和RIGHT
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        } else {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            final int swipeFlags = 0;
        //                    final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }
    }

    //开始滑动功能，默认为true
    //如果设置为false，手动开启，调用startSwipe()
    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    //开启长按拖拽功能，默认为true 控制当前的viewholder是否可以被拖动
   //如果设置为false，手动开启，调用startDrag()
    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    /**
     * drag状态下，在canDropOver()返回true时，会调用该方法让我们拖动换位置的逻辑(需要自己处理变换位置的逻辑)
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        GALogger.d(TAG,"onMove");
        //得到当拖拽的viewHolder的Position
        int fromPosition = viewHolder.getAdapterPosition();
        //拿到当前拖拽到的item的viewHolder
        int toPosition = target.getAdapterPosition();
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(adapter.getList(), i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(adapter.getList(), i, i - 1);
            }
        }
        adapter.notifyItemMoved(fromPosition, toPosition);

        //drag状态下 拖动结束的业务逻辑处理在这里
        GALogger.d(TAG,"onMove  end");
        isDrag = true;
        return true;
    }


    /**
     * 长按选中Item的时候开始调用
     *
     * @param viewHolder
     * @param actionState
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        GALogger.d(TAG,"onSelectedChanged");
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
        }
        if(dragCallBack != null){
            dragCallBack.onSelectedChanged();
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    /**
     * 手指松开的时候还原
     * @param recyclerView
     * @param viewHolder
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        GALogger.d(TAG,"clearView  end   "+viewHolder.getAdapterPosition());
        handLift = true;
        if(isDrag == true && handLift == true){
            if(dragCallBack != null){
                dragCallBack.onDragEnd();
            }
        }
        if(dragCallBack != null){
            dragCallBack.clearView();
        }
        handLift = false;
        handLift = false;
    }

    /**
     * 针对drag状态，当前target对应的item是否允许移动
     * 我们一般用drag来做一些换位置的操作，就是当前对应的target对应的Item可以移动
     */
    /**
     * 用来判断 target是否可以被替换
     * @param recyclerView
     * @param current
     * @param target
     * @return  true :target可以被current替换
     *          false：不可以
     *          控制当前的targetholder 是否可以被站位
     */

    @Override
    public boolean canDropOver(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder current, @NonNull RecyclerView.ViewHolder target) {
        /**
         * 对完善资料单独做处理
         */
        GALogger.d(TAG,"canDropOver");
        List list = adapter.getList();
//        if(list != null && list.size() > 0){
//            EditMaterialBean.EditMaterialPictureBean targetEditMaterialPictureBean = (EditMaterialBean.EditMaterialPictureBean)list.get(target.getAdapterPosition());
//            String targetImage = targetEditMaterialPictureBean.image;
//            if(TextUtils.isEmpty(targetImage)){
//                return false;
//            }
//        }
        return super.canDropOver(recyclerView, current, target);
    }

    public void setItemDragSwipeCallBack(@Nullable ItemDragSwipeCallBack callBack) {
        this.callBack = callBack;
    }

    @Nullable
    private ItemDragSwipeCallBack callBack;

    public interface ItemDragSwipeCallBack {
        //数据交换
        boolean onItemMove(int fromPosition, int toPosition);

        //数据删除
        void onItemDismiss(int position);

        /**
         * Instead of composing this flag manually, you can use makeMovementFlags(int, int) or makeFlag(int, int).
         * This flag is composed of 3 sets of 8 bits, where first 8 bits are for IDLE state, next 8 bits are for
         * SWIPE state and third 8 bits are for DRAG state.
         * Each 8 bit sections can be constructed by simply OR'ing direction flags defined in ItemTouchHelper.
         * For example, if you want it to allow swiping LEFT and RIGHT but only allow starting to swipe by swiping
         * RIGHT, you can return:
         * <p>
         * <code>new  int[]{ItemTouchHelper.UP | ItemTouchHelper.DOWN |
         * ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.ACTION_STATE_IDLE};</code>
         */
        @NonNull
        int[] getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder);
    }

    /**
     * drag状态下 拖拽成功
     * 并且手指抬起时回掉
     */
    @Nullable
    private ItemDragCallBack dragCallBack;
    public interface ItemDragCallBack {
        void onDragEnd();
        void onSelectedChanged();
        void clearView();
    }
}
