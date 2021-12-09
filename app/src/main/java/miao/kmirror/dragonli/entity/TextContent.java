package miao.kmirror.dragonli.entity;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * @author Kmirror
 */
public class TextContent extends LitePalSupport implements Serializable {
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String content;
}
