package com.android.ofoo.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "bike")
public class BikeBean extends BaseBean {
    // 需要定义一个无参构造函数，ormlite用来查询时用反射构建对象
    public BikeBean() {
    }

    @DatabaseField(generatedId = true)
    private int id;
    // columnName定义字段名，缺省为变量名
    @DatabaseField(columnName = "bike_no")
    private String bikeNo;
    @DatabaseField(columnName = "bike_pass")
    private String bikePass;

    @DatabaseField(columnName = "create_time")
    private String createTime;
    @DatabaseField(columnName = "update_time")
    private String updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBikeNo() {
        return bikeNo;
    }

    public void setBikeNo(String bikeNo) {
        this.bikeNo = bikeNo;
    }

    public String getBikePass() {
        return bikePass;
    }

    public void setBikePass(String bikePass) {
        this.bikePass = bikePass;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

}
