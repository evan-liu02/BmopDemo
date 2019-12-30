package com.bmop.demo.ui.adapter;


import android.content.Context;

import java.util.List;

public abstract class MultiLayoutBaseAdapter<T> extends BaseAdapter<T> {

    public MultiLayoutBaseAdapter(Context context, List<T> dataList, int[] layoutIds) {
        super(context, dataList, layoutIds);
    }

    @Override
    public int getItemViewType(int position) {
        return getItemType(position);
    }

    @Override
    protected void onBindData(BaseHolder holder, T data, int position) {
        onBindData(holder, data, position, getItemType(position));
    }

    // 子类实现得到具体的子类布局的方法
    public abstract int getItemType(int position);

    // 子类实现对不同的item进行操作
    protected abstract void onBindData(BaseHolder holder, T t, int position, int itemViewType);
}
