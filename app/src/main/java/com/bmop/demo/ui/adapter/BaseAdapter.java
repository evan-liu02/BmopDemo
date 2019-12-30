package com.bmop.demo.ui.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseAdapter.BaseHolder> {
    protected Context context;
    private LayoutInflater inflater;
    private List<T> dataList;
    private int[] layoutIds;

    public BaseAdapter(Context context, List<T> dataList, int layoutId) {
        this.context = context;
        this.dataList = dataList;
        this.layoutIds = new int[]{layoutId};
        inflater = LayoutInflater.from(context);
    }

    public BaseAdapter(Context context, List<T> dataList, int[] layoutIds) {
        this.context = context;
        this.dataList = dataList;
        this.layoutIds = layoutIds;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BaseHolder(inflater.inflate(layoutIds[viewType], parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        onBindData(holder, dataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    // 让子类实现具体的数据绑定方法
    protected abstract void onBindData(BaseHolder holder, T data, int position);

    public static class BaseHolder extends RecyclerView.ViewHolder
    {
        SparseArray<View> views; // 存放itemView中的子view

        BaseHolder(@NonNull View itemView) {
            super(itemView);
            views = new SparseArray<View>();
        }

        protected View getView(int id) {
            View view = views.get(id);
            if (view == null) {
                view = itemView.findViewById(id);
                views.put(id, view);
            }
            return view;
        }

        public BaseHolder setText(int viewId, int resId) {
            ((TextView) getView(viewId)).setText(resId);
            return this;
        }

        public BaseHolder setText(int viewId, String text) {
            ((TextView) getView(viewId)).setText(text);
            return this;
        }

        public View getItemView() {
            return itemView;
        }
    }
}
