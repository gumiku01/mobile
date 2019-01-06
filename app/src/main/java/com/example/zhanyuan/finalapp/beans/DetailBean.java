package com.example.zhanyuan.finalapp.beans;

import java.util.List;

public class DetailBean extends  BaseBean{

    private float t_income;
    private float t_outcome;
    private int year;
    private int month;
    private List<DayListBean> daylist;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public float getT_income() {
        return t_income;
    }

    public void setT_income(float t_income) {
        this.t_income = t_income;
    }

    public float getT_outcome() {
        return t_outcome;
    }

    public void setT_outcome(float t_outcome) {
        this.t_outcome = t_outcome;
    }

    public List<DayListBean> getDaylist() {
        return daylist;
    }

    public void setDaylist(List<DayListBean> daylist) {
        this.daylist = daylist;
    }

    // define DayListBean
    public static class DayListBean{
        private String day;
        private String money;
        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String time) {
            this.day = time;
        }



        //define ListBean
        public static class ListBean{
            private int id;
            private float affect_money;
            private String type;
            private String description;
            private String detailtype;


            public float getAffect_money() {
                return affect_money;
            }

            public void setAffect_money(float affect_money) {
                this.affect_money = affect_money;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getType() {
                return type;
            }

            public void setType(String typeName) {
                this.type = typeName;
            }

            public void setDetailtype(String detailtype) {
                this.detailtype = detailtype;
            }

            public String getDetailtype() {
                return detailtype;
            }
        }
    }
}
