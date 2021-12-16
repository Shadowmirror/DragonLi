package miao.kmirror.dragonli.dao;

import android.content.ContentValues;

import org.litepal.LitePal;

import java.util.List;

import miao.kmirror.dragonli.entity.TextInfo;

public class TextInfoDao {
    private TextInfo textInfo;

    public boolean save(TextInfo info) {
        return info.save();
    }

    public int delete(Integer id) {
        int row = LitePal.deleteAll(TextInfo.class, "id = ?", id.toString());
        return row;
    }

    public TextInfo findById(Integer id) {
        List<TextInfo> textInfoList = LitePal.where("id = ?", id.toString()).find(TextInfo.class);
        textInfo = textInfoList.get(0);
        return textInfo;
    }

    public List<TextInfo> findLikeTitleKey(String titleKey) {
        List<TextInfo> textInfoList = LitePal.where("title like ?", "%" + titleKey + "%").find(TextInfo.class);
        return textInfoList;
    }

    public int update(TextInfo info) {
        textInfo = info;
        updateLockState(textInfo);
        updateLockType(textInfo);
        return textInfo.update(textInfo.getId());
    }

    private int updateLockState(TextInfo info) {
        ContentValues values = new ContentValues();
        values.put("isLocked", info.getLocked());
        int update = LitePal.update(TextInfo.class, values, info.getId());
        return update;
    }

    private int updateLockType(TextInfo info) {
        ContentValues values = new ContentValues();
        values.put("lockType", info.getLockType());
        int update = LitePal.update(TextInfo.class, values, info.getId());
        return update;
    }

    public List<TextInfo> findAll() {
        return LitePal.findAll(TextInfo.class);
    }

    /**
     * 查找新增文章 / 密码本 ID
     * */
    public TextInfo findNewAddText() {
        TextInfo last = LitePal.findLast(TextInfo.class);
        return last;
    }

}
