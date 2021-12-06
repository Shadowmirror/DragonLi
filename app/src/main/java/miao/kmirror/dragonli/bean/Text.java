package miao.kmirror.dragonli.bean;

import android.content.Intent;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Text extends LitePalSupport implements Serializable {

    private String title;
    private String content;
    private String createdTime;
    private Integer id;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Text{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createdTime='" + createdTime + '\'' +
                ", id=" + id +
                '}';
    }
}
