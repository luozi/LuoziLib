package com.luozi.lib.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseListAdapter<T> extends BaseAdapter {

    protected Context mContext;

    protected LayoutInflater mLayoutInflater;

    private List<T> mInfoList;

    private int mResLayoutId;

    public BaseListAdapter(Context context, int resLayoutId) {

        mContext = context;
        mResLayoutId = resLayoutId;

        mLayoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mInfoList = new ArrayList<T>();
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

    public void replaceInfoList(List<T> infoList) {
        clearList();
        mInfoList.addAll(infoList);
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

    @Override
    public int getCount() {
        return mInfoList.size();
    }

    @Override
    public T getItem(int position) {
        return mInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        ViewHolder holder;
        if (v == null) {
            v = inflate(mResLayoutId, parent);
            holder = getViewHolder(v);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        setModel(position, holder.mViews);

        return v;
    }

    /**
     * Inflate a new view hierarchy from the specified xml resource. Throws {@link android.view.InflateException}
     * if there is an error.
     *
     * @param resource
     *            ID for an XML layout resource to load (e.g., <code>R.layout.main_page</code>)
     */
    protected View inflate(int resource, ViewGroup parent) {
        return mLayoutInflater.inflate(resource, parent, false);
    }

    // /**
    // * Creates a new set of layout parameters with
    // * the specified width and height.
    // */
    // protected LinearLayout.LayoutParams getLayoutParams() {
    // return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
    // ViewGroup.LayoutParams.MATCH_PARENT);
    // }

    /**
     * It's getView from BaseListAdapter.
     */
    protected abstract void setModel(int position, View[] views);

    protected abstract ViewHolder getViewHolder(View convertView);

    public class ViewHolder {
        public View[] mViews;

        public ViewHolder(View convertView, int[] ids) {
            if (ids != null) {
                int length = ids.length;
                mViews = new View[length];

                if (length > 0) {
                    int index = 0;
                    for (int id : ids) {
                        mViews[index] = convertView.findViewById(id);
                        index++;
                    }
                }
            }
        }
    }

}
