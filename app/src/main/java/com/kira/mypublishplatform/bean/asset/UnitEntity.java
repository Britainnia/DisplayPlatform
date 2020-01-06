package com.kira.mypublishplatform.bean.asset;

import java.util.List;

/**
 * 作者       ：潘志仁
 * 创建时间   ：2019/9/19
 * 邮箱       770440536@qq.com
 */
public class UnitEntity {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 01
         * name : 汉族
         */

        private String value;
        private String label;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }
}
