package com.troy_zh.demo;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.gson.Gson;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author: taozhang16
 * Package: com.troy_zh.demo
 * Date: 2020/8/16 16:26
 * Describe: TODO
 */
public class AttrAdapter extends RecyclerView.Adapter<AttrAdapter.ViewHolder> {
    private static final String TAG = "AttrAdapter";
    private Context mContext;
    private List<GoodDetailBean.ValueBean> data;

    public void setOnAttrDataChange(OnAttrDataChange onAttrDataChange) {
        this.onAttrDataChange = onAttrDataChange;
    }

    private OnAttrDataChange onAttrDataChange;

    public AttrAdapter(Context mContext, List<GoodDetailBean.ValueBean> data) {
        this.mContext = mContext;
        this.data = data;
    }

    public List<GoodDetailBean.ValueBean> getData() {
        return data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_attr, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(data.get(position), position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.cb_attr);
        }

        public void setData(final GoodDetailBean.ValueBean valueBean, final int position) {
            checkBox.setText(valueBean.getAttribute_value());
            checkBox.setEnabled(valueBean.isEnable());
            checkBox.setChecked(valueBean.isChecked());
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0; i < data.size(); i++) {
                        if (i == position) {
                            data.get(i).setChecked(!data.get(i).isChecked());
                        } else {
                            data.get(i).setChecked(false);
                        }
                    }
                    notifyDataSetChanged();
                    onAttrDataChange.onChange();
                }
            });
        }
    }

    interface OnAttrDataChange {
        void onChange();
    }
}
