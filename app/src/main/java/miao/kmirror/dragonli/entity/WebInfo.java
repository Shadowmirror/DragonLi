package miao.kmirror.dragonli.entity;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class WebInfo extends LitePalSupport{
    private Integer id;
    private String webName;
    private String webLink;

    @Override
    public String toString() {
        return "WebInfo{" +
                "id=" + id +
                ", webName='" + webName + '\'' +
                ", webLink='" + webLink + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWebName() {
        return webName;
    }

    public void setWebName(String webName) {
        this.webName = webName;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }
}
