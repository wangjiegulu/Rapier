package com.wangjie.rapier.app.model;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/5/16.
 */
public class FooData {
    public static final String KEY_DATA_ID = "KEY_DATA_ID";
    public static final String KEY_DATA_CONTENT = "KEY_DATA_CONTENT";

    private Integer dataId;
    private String dataContent;

    public FooData(Integer dataId, String dataContent) {
        this.dataId = dataId;
        this.dataContent = dataContent;
    }

    public Integer getDataId() {
        return dataId;
    }

    public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }

    public String getDataContent() {
        return dataContent;
    }

    public void setDataContent(String dataContent) {
        this.dataContent = dataContent;
    }

    @Override
    public String toString() {
        return "FooData{" +
                "dataId=" + dataId +
                ", dataContent='" + dataContent + '\'' +
                '}';
    }
}
