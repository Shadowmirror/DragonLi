package miao.kmirror.dragonli.entity;

import org.litepal.crud.LitePalSupport;

/**
 * 文章跳转应用或网站关系表
 * @author：Kmirror
 */
public class TextSkip extends LitePalSupport {

    private Integer id;
    /**
     * 跳转应用还是网站
     * 0 表示应用
     * 1 表示网站
     */
    private Integer appOrWeb;
    /**
     * 文章/密码本 Id
     */
    private Integer textId;
    /**
     * 应用或者网站在各自表中的 Id
     */
    private Integer appOrWebId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAppOrWeb() {
        return appOrWeb;
    }

    public void setAppOrWeb(Integer appOrWeb) {
        this.appOrWeb = appOrWeb;
    }

    public Integer getTextId() {
        return textId;
    }

    public void setTextId(Integer textId) {
        this.textId = textId;
    }

    public Integer getAppOrWebId() {
        return appOrWebId;
    }

    public void setAppOrWebId(Integer appOrWebId) {
        this.appOrWebId = appOrWebId;
    }

    @Override
    public String toString() {
        return "TextSkip{" +
                "id=" + id +
                ", appOrWeb=" + appOrWeb +
                ", textId=" + textId +
                ", appOrWebId=" + appOrWebId +
                '}';
    }
}
