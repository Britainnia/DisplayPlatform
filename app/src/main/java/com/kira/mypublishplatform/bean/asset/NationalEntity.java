package com.kira.mypublishplatform.bean.asset;

import java.util.List;

/**
 * 作者       ：潘志仁
 * 创建时间   ：2019/9/19
 * 邮箱       770440536@qq.com
 */
public class NationalEntity {

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

        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
