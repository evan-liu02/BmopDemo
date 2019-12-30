package com.bmop.demo.ui.adapter;

import android.content.Context;
import android.view.View;

import com.bmop.demo.R;
import com.bmop.demo.data.PersonalData;

import java.util.List;

public class MineAdapter extends BaseAdapter<PersonalData> {
    public MineAdapter(Context context, List dataList, int layoutId) {
        super(context, dataList, layoutId);
    }

    @Override
    protected void onBindData(BaseHolder holder, PersonalData data, int position) {
        String title = data.getTitle();
        holder.setText(R.id.title, title);
        View arrow = holder.getView(R.id.arrow);
        if ("昵称".equals(title) || "签名".equals(title)) {
            arrow.setVisibility(View.VISIBLE);
        } else {
            arrow.setVisibility(View.GONE);
        }
    }
}
