package miao.kmirror.dragonli.entity;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * @author Kmirror
 */
public class TextInfo extends LitePalSupport implements Serializable {
    private Integer id;
    private String title;
    private Boolean isLocked = false;
    private Integer lockType = 0;
    private String password;
    private String updateDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getLocked() {
        return isLocked;
    }

    public void setLocked(Boolean locked) {
        isLocked = locked;
    }

    public Integer getLockType() {
        return lockType;
    }

    public void setLockType(Integer lockType) {
        this.lockType = lockType;
    }

    public String getPassword() {
        return password;
    }


    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        return "TextInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", isLocked=" + isLocked +
                ", lockType='" + lockType + '\'' +
                ", password='" + password + '\'' +
                ", updateDate='" + updateDate + '\'' +
                '}';
    }
}
