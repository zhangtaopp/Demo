package com.troy_zh.demo;

import java.util.List;

/**
 * Author: taozhang16
 * Package: com.troy_zh.demo
 * Date: 2020/8/16 15:44
 * Describe: TODO
 */
public class GoodDetailBean {
    private List<AttrDetailBean> attrdetail;
    private List<AttrBean> attr;

    public List<AttrDetailBean> getAttrdetail() {
        return attrdetail;
    }

    public List<AttrBean> getAttr() {
        return attr;
    }

    class ValueBean {
        private int id;
        private String attribute_value;
        private boolean isChecked = false;
        private boolean enable = false;

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public int getId() {
            return id;
        }

        public String getAttribute_value() {
            return attribute_value;
        }
    }

    class AttrBean {
        private int key_id;
        private String attribute_name;
        private List<ValueBean> child;

        public int getKey_id() {
            return key_id;
        }

        public String getAttribute_name() {
            return attribute_name;
        }

        public List<ValueBean> getChild() {
            return child;
        }
    }

    class AttrDetailBean {
        private String specs_id;
        private String specs_goods;
        private String specs_price;
        private String specs_num;
        private String goods_id;
        private String specs_goods_zn;
        private String goods_out;

        public String getSpecs_id() {
            return specs_id;
        }

        public String getSpecs_goods() {
            return specs_goods;
        }

        public String getSpecs_price() {
            return specs_price;
        }

        public String getSpecs_num() {
            return specs_num;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public String getSpecs_goods_zn() {
            return specs_goods_zn;
        }

        public String getGoods_out() {
            return goods_out;
        }
    }
}
