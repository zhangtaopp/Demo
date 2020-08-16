package com.troy_zh.demo;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author: taozhang16
 * Package: com.troy_zh.demo
 * Date: 2020/8/16 13:50
 * Describe: TODO
 */
public class SelectStyleDialog extends Dialog implements View.OnClickListener {
    private static final String TAG = "SelectStyleDialog";
    private Context mContext;
    private TextView mTvPrice;
    private TextView mTvStyle;
    private RecyclerView mRvStyle;
    private StyleAdapter styleAdapter;
    private TextView mTvReserve;
    private ImageView mIvClose;
    private Button mBtnAdd;
    private TextView mTvSub;
    private TextView mTvNum;
    private TextView mTvAdd;
    private List<GoodDetailBean.AttrBean> attr;
    private List<GoodDetailBean.AttrDetailBean> attrDetail;

    public SelectStyleDialog(@NonNull Context context) {
        super(context);
        mContext = context;
        init();
    }

    private void init() {
        Window win = this.getWindow();
        win.requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_select_style);
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.windowAnimations = R.style.BottomDialog;
        lp.gravity = Gravity.BOTTOM;
        win.setAttributes(lp);
        win.setBackgroundDrawableResource(android.R.color.transparent);
        mTvPrice = findViewById(R.id.tv_price);
        mTvReserve = findViewById(R.id.tv_reserve);
        mTvStyle = findViewById(R.id.tv_style);
        mRvStyle = findViewById(R.id.rv_style);
        mIvClose = findViewById(R.id.iv_close);
        mTvSub = findViewById(R.id.tv_sub);
        mTvNum = findViewById(R.id.tv_num);
        mTvAdd = findViewById(R.id.tv_add);

        mBtnAdd = findViewById(R.id.btn_add);
        mTvSub.setEnabled(false);
        initData();
        initListener();
    }

    private void initListener() {
        mIvClose.setOnClickListener(this);
        mBtnAdd.setOnClickListener(this);
        mTvSub.setOnClickListener(this);
        mTvAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int num = Integer.valueOf(mTvNum.getText().toString());
        switch (view.getId()) {
            case R.id.iv_close:
                cancel();
                break;
            case R.id.btn_add:
                cancel();
                break;
            case R.id.tv_add:
                if (num == 1) {
                    mTvSub.setEnabled(true);
                }
                num++;
                mTvNum.setText(num + "");
                break;
            case R.id.tv_sub:
                num--;
                mTvNum.setText(num + "");
                if (num == 1) {
                    mTvSub.setEnabled(false);
                }
                break;
            default:
                break;
        }
    }

    private void initData() {
        String jsonFile = getJsonFile("json.json", mContext);
        Gson gson = new Gson();
        BaseData baseData = gson.fromJson(jsonFile, BaseData.class);
        GoodDetailBean data = baseData.getData();
        attrDetail = data.getAttrdetail();
        attr = data.getAttr();
        mRvStyle.setLayoutManager(new LinearLayoutManager(mContext));
        styleAdapter = new StyleAdapter(mContext, attr);
        mRvStyle.setAdapter(styleAdapter);
        setAttr(attrDetail);
        styleAdapter.setOnAttrDataChange(new AttrAdapter.OnAttrDataChange() {
            @Override
            public void onChange() {
                Map<Integer, Integer> attrMap = styleAdapter.getAttr();
                Log.d(TAG, "onChange: " + attrMap.size());
                String price = "0.00";
                String reserve = "0";
                String attr = "";
                List<GoodDetailBean.AttrDetailBean> list = compareAttr(attrMap);
                switch (attrMap.size()) {
                    case 0:
                    case 1:
                    case 2:
                        setAttr(list,attrMap);
                        break;
                    case 3:
                        String attrs = new Gson().toJson(attrMap);
                        Log.d(TAG, "onChange: attrs=" + attrs);
                        if (list.size() > 0) {
                            GoodDetailBean.AttrDetailBean attrDetailBean = list.get(0);
                            price = attrDetailBean.getSpecs_price();
                            reserve = attrDetailBean.getSpecs_num();
                            attr = attrDetailBean.getSpecs_goods_zn();
                        }
                        break;
                    default:
                        break;
                }
                mTvStyle.setText("已选：" + attr);
                mTvReserve.setText("库存" + reserve + "件");
                mTvPrice.setText("￥" + price);
            }
        });
    }

    private void setAttr(List<GoodDetailBean.AttrDetailBean> attrDetail, Map<Integer, Integer> attrMap) {
        List<String> validAttr = new ArrayList<>();
        for (int i = 0; i < attrDetail.size(); i++) {
            GoodDetailBean.AttrDetailBean attrDetailBean = attrDetail.get(i);
            String specs_goods = attrDetailBean.getSpecs_goods();
            String[] split = specs_goods.split(",");
            validAttr.addAll(Arrays.asList(split));
        }
        for (int j = 0; j < attr.size(); j++) {
            GoodDetailBean.AttrBean attrBean = attr.get(j);
            if (attrMap.containsKey(attrBean.getKey_id())){
                continue;
            }
            for (int k = 0; k < attrBean.getChild().size(); k++) {
                String attr = attrBean.getKey_id() + ":" + attrBean.getChild().get(k).getId();
                attrBean.getChild().get(k).setEnable(validAttr.contains(attr));
            }
        }
        styleAdapter.notifyDataSetChanged();
    }

    private void setAttr(List<GoodDetailBean.AttrDetailBean> attrDetail) {
        List<String> validAttr = new ArrayList<>();
        for (int i = 0; i < attrDetail.size(); i++) {
            GoodDetailBean.AttrDetailBean attrDetailBean = attrDetail.get(i);
            String specs_goods = attrDetailBean.getSpecs_goods();
            String[] split = specs_goods.split(",");
            validAttr.addAll(Arrays.asList(split));
        }
        for (int j = 0; j < attr.size(); j++) {
            GoodDetailBean.AttrBean attrBean = attr.get(j);
            for (int k = 0; k < attrBean.getChild().size(); k++) {
                String attr = attrBean.getKey_id() + ":" + attrBean.getChild().get(k).getId();
                /*if (validAttr.contains(attr)) {
                }else {

                }*/
                attrBean.getChild().get(k).setEnable(validAttr.contains(attr));
            }
        }
        styleAdapter.notifyDataSetChanged();
    }

    private List<GoodDetailBean.AttrDetailBean> compareAttr(Map<Integer, Integer> attrMap) {
        List<GoodDetailBean.AttrDetailBean> list = new ArrayList<>();
        if (attrMap.size() == 0) {
            return attrDetail;
        }
        for (int i = 0; i < attrDetail.size(); i++) {
            GoodDetailBean.AttrDetailBean attrDetailBean = attrDetail.get(i);
            String attrs = attrDetailBean.getSpecs_goods();
            boolean flag = true;
            for (Map.Entry<Integer, Integer> attrEntry : attrMap.entrySet()) {
                String attr = attrEntry.getKey() + ":" + attrEntry.getValue();
                if (!attrs.contains(attr)) {
                    Log.d(TAG, "compareAttr: " + attrs + "  " + attr);
                    flag = false;
                    break;
                }
            }
            if (flag) {
                list.add(attrDetailBean);
            }
        }

        return list;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public String getJsonFile(String fileName, Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

}
