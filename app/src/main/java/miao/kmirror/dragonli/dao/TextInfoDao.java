package miao.kmirror.dragonli.dao;

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
        List<TextInfo> textInfoList = LitePal.where("like = ?", "%" + titleKey + "%").find(TextInfo.class);
        return textInfoList;
    }

    public int update(TextInfo info) {
        textInfo = info;
        return textInfo.updateAll("id = ?", textInfo.getId().toString());
    }

    public List<TextInfo> findAll(){
       return LitePal.findAll(TextInfo.class);
    }

}
