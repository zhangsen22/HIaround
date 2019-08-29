
package aimi.android.com.ui.adapter.poweradapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import aimi.android.com.R;

@SuppressWarnings({"unused", "unchecked"})
public abstract class PowerAdapter<T> extends RecyclerView.Adapter<PowerHolder<T>> implements AdapterLoader<T>,
        SpanSizeCallBack {
    private static final String TAG = PowerAdapter.class.getSimpleName();
    public final static int ITEM_VIEW_TYPE_ITEM = 0;//recycleview的item的类型
    public final static int ITEM_VIEW_TYPE_TITLE = 1;
    public final static int ITEM_VIEW_TYPE_NORMAL = 2;
    public final static int ITEM_VIEW_HEAD = 11;//recycleview添加的头布局的类型
    public final List<T> list;
    public final List<T> originalDataList;//原数据  list改变前的数据
    public boolean enableLoadMore;//设置是否可以加载更多  但是会显示bottom  View布局
    public boolean isShowBottom = true;//设置不显示bottom  View布局  默认显示
    public boolean isAddHeadView = false;//设置是否添加头布局
    public boolean isSlipFixed = false;//是否锁定滑动
    public boolean isEnableSlided = true;//判断是否可以滑动  默认可以
    private int totalCount;
    public int currentType;
    @LayoutRes
    private int loadMoreLayout = RecyclerView.INVALID_TYPE;
    @LoadState
    private int loadState;
    @Nullable
    private View errorView;
    @Nullable
    private View emptyView;
    @Nullable
    public RecyclerView recyclerView;
    @Nullable
    private OnLoadMoreListener loadMoreListener;
    @Nullable
    private OnErrorClickListener errorClickListener;
    @Nullable
    private OnItemLongClickListener<T> longClickListener;
    @Nullable
    OnItemClickListener<T> clickListener;

    private Runnable loadMoreAction;

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.clickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> listener) {
        this.longClickListener = listener;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isEnableLoadMore() {
        return enableLoadMore;
    }

    public PowerAdapter() {
        list = new ArrayList<>();
        originalDataList = new ArrayList<>();
    }

    public List<T> getList() {
        return list;
    }

    public List<T> getOriginalDataList() {
        return originalDataList;
    }

    public int getCurrentType() {
        return currentType;
    }

    /**
     * list还原数据 并更新原数据集合
     *
     * @param
     */
    public void originalDataList() {
        list.clear();
        list.addAll(originalDataList);
        originalDataList.clear();
        originalDataList.addAll(list);
    }

    /**
     * list数据发生改变  更新原数据集合
     *
     * @param
     */
    public void updateOriginalDataList() {
        originalDataList.clear();
        originalDataList.addAll(list);
    }

    @Override
    public final void setList(@NonNull List<T> data) {
        if (data == null) {
            return;
        }
        currentType = 0;
        list.clear();
        originalDataList.clear();
        appendList(data);
    }

    public final void insertList(List<T> data, int startPos) {
        list.addAll(startPos, data);
        notifyItemRangeInserted(startPos, data.size());
    }

    public final void clearList() {
        list.clear();
        notifyDataSetChanged();
    }

    @Override
    public final void appendList(@NonNull List<T> data) {
        int positionStart = list.size();
        list.addAll(data);
        originalDataList.addAll(data);
        enableLoadMore = totalCount > list.size();
        int itemCount = list.size() - positionStart;

        if (positionStart == 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeInserted(isAddHeadView ? positionStart + 2 : positionStart + 1, itemCount);
        }
    }

    public final void appendList1(@NonNull List<T> data) {
        list.addAll(data);
        originalDataList.addAll(data);
        enableLoadMore = totalCount > list.size();
    }

    @Override
    public T removeItem(int position) {
        if (isAddHeadView) {
            position = position - 1;
        }
        if (position < 0 || position > list.size()) {
            return null;
        }
        T bean = list.remove(position);
        enableLoadMore = totalCount > list.size();
        notifyItemRemoved(position);
        return bean;
    }

    @Override
    public T getItem(int position) {
        if (position < 0 || position > (isAddHeadView ? list.size()+1:list.size())) {
            return null;
        }
        if(isAddHeadView){
            if(position != 0){
                return list.get(position-1);
            }else {
                return null;
            }
        }else {
            return list.get(position);
        }

    }

    @Override
    public void insertItem(int position, T bean) {
        if (position < 0) {
            position = 0;
        }
        if (position > list.size()) {
            position = list.size();
        }
        list.add(position, bean);
        notifyItemInserted(position);
    }

    @NonNull
    @Override
    public final PowerHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_BOTTOM:
                if (loadMoreLayout != -1) {
                    View view =
                            LayoutInflater.from(parent.getContext()).inflate(loadMoreLayout, parent, false);
                    AbsBottomViewHolder holder = onBottomViewHolderCreate(view);
                    if (holder == null) {
                        throw new RuntimeException(
                                "You must impl onBottomViewHolderCreate() and return your own holder ");
                    }
                    return holder;
                } else {
                    //noinspection unchecked
                    return new NewBottomViewHolder(
                            LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_footer_new, parent, false));
                }
            case TYPE_ERROR:
                if (errorView == null) {
                    throw new NullPointerException("Did you forget init ErrorView?");
                }
                return new PowerHolder<>(errorView);
            case TYPE_EMPTY:
                if (emptyView == null) {
                    throw new NullPointerException("Did you forget init EmptyView?");
                }
                return new PowerHolder<>(emptyView);
            default:
                return onViewHolderCreate(parent, viewType);
        }
    }

    @Override
    public abstract PowerHolder<T> onViewHolderCreate(@NonNull ViewGroup parent, int viewType);

    @Override
    public final void onBindViewHolder(@NonNull final PowerHolder<T> holder, int position) {
        bindViewHolder(holder, position, null);
    }

    @Override
    public final void onBindViewHolder(@NonNull PowerHolder<T> holder, int position, @NonNull List<Object> payloads) {
        bindViewHolder(holder, position, payloads);
    }

    private void bindViewHolder(@NonNull PowerHolder<T> holder, int position, List<Object> o) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case TYPE_BOTTOM:
                bindBottom(holder);
                break;
            case TYPE_EMPTY:
                onEmptyHolderBind(holder);
                break;
            case TYPE_ERROR:
                onErrorHolderBind(holder);
                break;
            default:
                bindDefaultHolder(holder, position, o);
                break;
        }
    }

    private void bindBottom(@NonNull PowerHolder<T> holder) {
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        if (params instanceof StaggeredGridLayoutManager.LayoutParams) {
            ((StaggeredGridLayoutManager.LayoutParams) params).setFullSpan(true);
        }
        loadState = isHasMore() ? STATE_LOADING : STATE_LASTED;
        try {
            if (loadMoreLayout != -1) {
                onBottomViewHolderBind((AbsBottomViewHolder) holder, loadMoreListener, loadState);
            } else {
                ((NewBottomViewHolder) holder).onBind(loadMoreListener, loadState);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindDefaultHolder(@NonNull final PowerHolder<T> holder, int position,
                                   @Nullable List<Object> payloads) {
        if (position == -1 || position >= (isAddHeadView ? list.size() + 1 : list.size())) {
            return;
        }
        handleHolderClick(holder);
        if (payloads == null || payloads.isEmpty()) {
            if (isAddHeadView) {
                position = position - 1;
            }
            onViewHolderBind(holder, position);
        } else {
            if (isAddHeadView) {
                position = position - 1;
            }
            onViewHolderBind(holder, position, payloads);
        }
    }

    void handleHolderClick(@NonNull final PowerHolder<T> holder) {
        if (clickListener != null && holder.enableCLick) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentPos = holder.getAdapterPosition();
                    if (currentPos < 0 || currentPos >= (isAddHeadView ? list.size() + 1 : list.size())) {
                        return;
                    }
                    //noinspection ConstantConditions
                    performClick(holder, v, currentPos, getItem(currentPos));
                }
            });
        }
        if (longClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int currentPos = holder.getAdapterPosition();
                    //noinspection ConstantConditions
                    return !(currentPos < 0 || currentPos >= (isAddHeadView ? list.size() + 1 : list.size())) && performLongClick(
                            holder, v, holder.getAdapterPosition(), getItem(currentPos));
                }
            });
        }
    }

    @Override
    public void onErrorHolderBind(@NonNull PowerHolder<T> holder) {
    }

    @Override
    public void onEmptyHolderBind(@NonNull PowerHolder<T> holder) {
    }

    @Override
    public AbsBottomViewHolder onBottomViewHolderCreate(@NonNull View loadMore) {
        return null;
    }

    @Override
    public void onBottomViewHolderBind(AbsBottomViewHolder holder, OnLoadMoreListener listener,
                                       @LoadState int loadState) {
        holder.onBind(listener, loadState);
    }


    @Override
    public final void setLoadMoreView(@LayoutRes int layoutRes, @NonNull boolean showBottom) {
        isShowBottom = showBottom;
        loadMoreLayout = layoutRes;
        notifyDataSetChanged();
    }

    @Override
    public void performClick(@NonNull PowerHolder<T> holder, @NonNull View itemView, int position, T item) {
        if (clickListener != null) {
            clickListener.onItemClick(holder, itemView, position, item);
        }
    }

    @Override
    public boolean performLongClick(@NonNull PowerHolder<T> holder, @NonNull View itemView, int position, T item) {
        return longClickListener != null && longClickListener.onItemLongClick(holder, itemView, position, item);
    }

    @Override
    public abstract void onViewHolderBind(@NonNull PowerHolder<T> holder, int position);

    @Override
    public void onViewHolderBind(@NonNull PowerHolder<T> holder, int position, @NonNull List<Object> payloads) {
        onViewHolderBind(holder, position);
    }

    @Override
    public final void updateLoadingMore() {
        if (loadState == STATE_LOADING) {
            return;
        }
        loadState = STATE_LOADING;
        if (loadMoreAction == null) {
            loadMoreAction = new Runnable() {
                @Override
                public void run() {
                    notifyItemRangeChanged(getItemRealCount(), 1);
                }
            };
        }
        recyclerView.post(loadMoreAction);
    }

    @Override
    public void enableLoadMore(boolean loadMore, @NonNull boolean showBottom) {
        if (enableLoadMore != loadMore) {
            enableLoadMore = loadMore;
            isShowBottom = showBottom;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return list.isEmpty() ? isAddHeadView ? 1 : (currentType == 0) ? 0 : 1 : isAddHeadView ? isShowBottom ? list.size() + 2 : list.size() + 1 : isShowBottom ? list.size() + 1 : list.size();
    }

    @Override
    public int getItemRealCount() {
        return list.size();
    }

    @Override
    public final void setEmptyView(@NonNull View emptyView) {
        this.emptyView = emptyView;
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emptyClickListener != null) {
                    emptyClickListener.onEmptyClick(v);
                }
            }
        });
    }

    @Override
    public final void setErrorView(@NonNull View errorView) {
        this.errorView = errorView;
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (errorClickListener != null) {
                    errorClickListener.onErrorClick(v);
                }
            }
        });
    }

    @Override
    public void showEmpty() {
        list.clear();
        currentType = TYPE_EMPTY;
        notifyDataSetChanged();
    }

    @Override
    public void showError(boolean force) {
        if (!force && !list.isEmpty()) {
            return;
        }
        list.clear();
        currentType = TYPE_ERROR;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (isAddHeadView) {
            if (list.isEmpty()) {
                if (position == 0) {
                    return ITEM_VIEW_HEAD;
                } else if (currentType != 0) {
                    return currentType;
                }
                return super.getItemViewType(position);
            }
        } else {
            if (list.isEmpty()) {
                if (currentType != 0) {
                    return currentType;
                }
                return super.getItemViewType(position);
            }
        }


        if (isAddHeadView) {
            if (position == 0) {
                return ITEM_VIEW_HEAD;
            } else if (position == list.size() + 1) {
                return TYPE_BOTTOM;
            } else {
                return getItemViewTypes(position);
            }
        } else {
            if (position < list.size()) {
                return getItemViewTypes(position);
            } else {
                return TYPE_BOTTOM;
            }
        }
    }

    public void setShowBottom(boolean showBottom) {
        isShowBottom = showBottom;
        notifyDataSetChanged();
    }

    @Override
    public int findFirstPositionOfType(int viewType) {
        if (list.isEmpty()) {
            return -1;
        }
        for (int i = 0; i < list.size(); i++) {
            if (getItemViewType(i) == viewType) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int findLastPositionOfType(int viewType) {
        if (list.isEmpty()) {
            return -1;
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            if (getItemViewType(i) == viewType) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getItemViewTypes(int position) {
        return ITEM_VIEW_TYPE_ITEM;
    }

    @Override
    public boolean isHasMore() {
        return getItemRealCount() < totalCount;
    }

    @Override
    public final void attachRecyclerView(@NonNull RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        recyclerView.setAdapter(this);
        final RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager == null) {
            throw new NullPointerException("Did you forget call setLayoutManager() at first?");
        }
        if (manager instanceof GridLayoutManager) {
            ((GridLayoutManager) manager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return initSpanSize(position, (GridLayoutManager) manager);
                }
            });
        }
    }

    private int initSpanSize(int position, GridLayoutManager manager) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case AdapterLoader.TYPE_BOTTOM:
            case AdapterLoader.TYPE_EMPTY:
            case AdapterLoader.TYPE_ERROR:
                return manager.getSpanCount();
            default:
                return getSpanSize(position);
        }
    }

    @Override
    public int getSpanSize(int position) {
        return 1;
    }

    public void setErrorClickListener(OnErrorClickListener errorClickListener) {
        this.errorClickListener = errorClickListener;
    }

    public interface OnErrorClickListener {
        void onErrorClick(View view);
    }

    public void setEmptyClickListener(OnEmptyClickListener emptyClickListener) {
        this.emptyClickListener = emptyClickListener;
    }

    private OnEmptyClickListener emptyClickListener;

    public interface OnEmptyClickListener {
        void onEmptyClick(View view);
    }

    public OnLoadMoreListener getLoadMoreListener() {
        return loadMoreListener;
    }

    public void setLoadMoreListener(@NonNull OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }
}
