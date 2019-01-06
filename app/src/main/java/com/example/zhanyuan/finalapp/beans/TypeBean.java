package com.example.zhanyuan.finalapp.beans;

import java.util.List;

public class TypeBean extends BaseBean{

    private float total;  // total money spent on this all types in month
    private List<TypeMoneyBean> type_money;

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public List<TypeMoneyBean> getType_money() {
        return type_money;
    }

    public void setType_money(List<TypeMoneyBean> type_money) {
        this.type_money = type_money;
    }


    public static class TypeMoneyBean {

        private float affect_money;
        private String typeName;
        private String back_color;
        private List<DetailBean.DayListBean.ListBean> rank;
        private List<String> dates;

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public float getAffect_money() {
            return affect_money;
        }

        public void setAffect_money(float affect_money) {
            this.affect_money = affect_money;
        }

        public String getBack_color() {
            return back_color;
        }

        public void setBack_color(String back_color) {
            this.back_color = back_color;
        }

        public List<DetailBean.DayListBean.ListBean> getRank() {
            return rank;
        }

        public void setRank(List<DetailBean.DayListBean.ListBean> rank) {
            this.rank = rank;
        }

        public List<String> getDates() {
            return dates;
        }

        public void setDates(List<String> dates) {
            this.dates = dates;
        }
    }
}
