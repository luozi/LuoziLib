package com.luozi.lib.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luozi.lib.R;
import com.luozi.lib.widget.dialog.LoadMoreView;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Luozi
 * Date: 2015-05-22
 * Content:
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerAdapter.ViewHolder> {

    public static final int LOAD_MORE = -1;
    public static final int DEFAULT_TYPE = 0;

    private int mItemType = DEFAULT_TYPE;
    private boolean mLoadMore;

    private OnLoadMoreListener mOnLoadMoreListener;

    private List<T> mInfoList;

    public BaseRecyclerAdapter() {
        mInfoList = new ArrayList<>();
    }

    public void addInfo(T t) {
        mInfoList.add(t);
    }

    public void addInfoList(List<T> infoList) {
        mInfoList.addAll(infoList);
    }

    public void addInfo(int index, T t) {
        mInfoList.add(index, t);
    }

    public void addInfoList(int index, List<T> infoList) {
        mInfoList.addAll(index, infoList);
    }

    public void deleteInfoList(List<T> infoList) {
        mInfoList.removeAll(infoList);
    }

    public void deleteInfo(int index) {
        mInfoList.remove(index);
    }

    public List<T> getAllList() {
        return mInfoList;
    }

    public void setAllList(List<T> infoList) {
        mInfoList = infoList;
    }

    public void clearList() {
        mInfoList.clear();
    }

    public void replaceInfoList(List<T> infoList) {
        clearList();
        mInfoList.addAll(infoList);
    }

    public T getItem(int position) {
        return mInfoList.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == LOAD_MORE) {
            return new ViewHolder(parent.getContext(), viewType, inflate(R.layout.item_load_more, parent),
                    new int[]{R.id.title, R.id.progress});
        }
        return getViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.mViewType == LOAD_MORE) {
            LoadMoreView.getInstance(holder.mContext, holder.mViews[0], holder.mViews[1]).start();
        } else {
            onBindViewHolder(holder.mViewType, position, holder.mViews);
        }
    }

    @Override
    public int getItemCount() {
        return mInfoList.size();
    }

    protected abstract void onBindViewHolder(int viewType, int position, View[] views);

    protected abstract ViewHolder getViewHolder(ViewGroup parent, int viewType);

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private Context mContext;
        private View[] mViews;
        private int mViewType;

        public ViewHolder(View view, int[] ids) {
            this(DEFAULT_TYPE, view, ids);
        }

        public ViewHolder(int viewType, View view, int[] ids) {
            this(null, viewType, view, ids);
        }

        public ViewHolder(Context context, int viewType, View view, int[] ids) {
            super(view);
            mContext = context;
            mViewType = viewType;

            if (ids != null) {
                int length = ids.length;
                mViews = new View[length];

                if (length > 0) {
                    int index = 0;
                    for (int id : ids) {
                        mViews[index] = view.findViewById(id);
                        index++;
                    }
                }
            }
        }
    }

    public View inflate(int resId, ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(resId, parent, false);
    }

    public void addLoadMoreView() {
        mLoadMore = true;
        mInfoList.add(null);
        notifyDataSetChanged();
    }

    public void removeLoadMoreView() {
        mLoadMore = false;
        mInfoList.remove(getItemCount() - 1);
        notifyDataSetChanged();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener l) {
        this.mOnLoadMoreListener = l;
    }

    public interface OnLoadMoreListener {
        void onLoadMore(int position);
    }

    @Override
    public int getItemViewType(int position) {
        boolean isBottom = (position + 1) == getItemCount();
        if (mLoadMore && isBottom) {
            this.mOnLoadMoreListener.onLoadMore(position);
            return LOAD_MORE;
        }
        return mItemType;
    }
}
