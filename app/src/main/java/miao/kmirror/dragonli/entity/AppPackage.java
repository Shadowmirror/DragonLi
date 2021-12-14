package miao.kmirror.dragonli.entity;

import com.google.gson.annotations.Expose;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class AppPackage extends LitePalSupport{
    private Integer id;
    private String appName;
    private String appPackageName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppPackageName() {
        return appPackageName;
    }

    public void setAppPackageName(String appPackageName) {
        this.appPackageName = appPackageName;
    }

    @Override
    public String toString() {
        return "AppPackage{" +
                "id=" + id +
                ", appName='" + appName + '\'' +
                ", appPackageName='" + appPackageName + '\'' +
                '}';
    }
}
