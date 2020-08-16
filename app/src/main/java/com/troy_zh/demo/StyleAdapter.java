package com.troy_zh.demo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author: taozhang16
 * Package: com.troy_zh.demo
 * Date: 2020/8/16 16:26
 * Describe: TODO
 */
public class StyleAdapter extends RecyclerView.Adapter<StyleAdapter.ViewHolder> {
    private static final String TAG = "StyleAdapter";
    private Context mContext;
    private List<GoodDetailBean.AttrBean> data;
    private Map<Integer, Integer> attr = new HashMap<>();
    private AttrAdapter.OnAttrDataChange onAttrDataChange;

    public StyleAdapter(Context mContext, List<GoodDetailBean.AttrBean> data) {
        this.mContext = mContext;
        this.data = data;
    }

    public void setOnAttrDataChange(AttrAdapter.OnAttrDataChange onAttrDataChange) {
        this.onAttrDataChange = onAttrDataChange;
    }

    public Map<Integer, Integer> getAttr() {
        return attr;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_style, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvAttrName;
        RecyclerView recyclerView;
        private AttrAdapter attrAdapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvAttrName = itemView.findViewById(R.id.tv_attr_name);
            recyclerView = itemView.findViewById(R.id.rv_attr);
        }

        public void setData(final GoodDetailBean.AttrBean attrBean) {
            mTvAttrName.setText(attrBean.getAttribute_name());
            attrAdapter = new AttrAdapter(mContext, attrBean.getChild());
            attrAdapter.setOnAttrDataChange(new AttrAdapter.OnAttrDataChange() {
                @Override
                public void onChange() {
                    if (attr.containsKey(attrBean.getKey_id())){
                        attr.remove(attrBean.getKey_id());
                    }
                    for (int i = 0; i < attrAdapter.getData().size(); i++) {
                        GoodDetailBean.ValueBean valueBean = attrAdapter.getData().get(i);
                        if (valueBean.isChecked()) {
                            attr.put(attrBean.getKey_id(), valueBean.getId());
                        }
                    }
                    onAttrDataChange.onChange();
                }
            });
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(attrAdapter);

        }
    }
}
