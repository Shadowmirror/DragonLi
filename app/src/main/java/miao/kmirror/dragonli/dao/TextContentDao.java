package miao.kmirror.dragonli.dao;

import org.litepal.LitePal;

import java.util.List;

import miao.kmirror.dragonli.entity.TextContent;

public class TextContentDao {

    private TextContent textContent;

    public boolean save(TextContent content){
        textContent = content;
        return textContent.save();
    }

    public int delete(Integer id){
        int row = LitePal.deleteAll(TextContent.class, "id = ?", id.toString());
        return row;
    }

    public TextContent findById(Integer id){
        List<TextContent> textContents = LitePal.where("id = ?", id.toString()).find(TextContent.class);
        textContent = textContents.get(0);
        return textContent;
    }

    public int update(TextContent content) {
        textContent = content;
        return textContent.updateAll("id = ?", textContent.getId().toString());
    }

}
