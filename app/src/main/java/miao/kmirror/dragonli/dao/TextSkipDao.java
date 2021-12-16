package miao.kmirror.dragonli.dao;

import android.content.ContentValues;

import org.litepal.LitePal;

import java.util.List;

import miao.kmirror.dragonli.entity.TextSkip;

/**
 * @author Kmirror
 */
public class TextSkipDao {
    private TextSkip textSkip = new TextSkip();

    /**
     * 根据文章 Id 查询与该文章对应的跳转关系
     */
    public TextSkip findByTextId(Integer textId) {
        List<TextSkip> textSkips = LitePal.where("textId = ?", textId.toString()).find(TextSkip.class);
        textSkip = textSkips.get(0);
        return textSkip;
    }

    /**
     * 保存文章/密码本跳转关系
     */
    public boolean save(Integer appOrWeb, Integer textId, Integer appOrWebId) {
        textSkip.setTextId(textId);
        textSkip.setAppOrWeb(appOrWeb);
        textSkip.setAppOrWebId(appOrWebId);
        boolean isSaved = textSkip.save();
        return isSaved;
    }

    /**
     * 删除文章时删除对应的文章跳转表对应的关系
     */
    public boolean deleteByTextId(Integer textId) {
        int i = LitePal.deleteAll(TextSkip.class, "textId = ?", textId.toString());
        return i == 1 ? true : false;
    }

    /**
     * 删除某条跳转 APP 和网站库时删除对应文章的表关系
     */
    public int deleteByAppOrWebId(Integer appOrWebId) {
        ContentValues values = new ContentValues();
        values.put("appOrWeb", 0);
        values.put("appOrWebId", 0);
        int i = LitePal.updateAll(TextSkip.class, values, "appOrWebId = ?", appOrWebId.toString());
        return i;
    }

}
