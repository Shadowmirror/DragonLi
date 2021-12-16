package miao.kmirror.dragonli.dao;

import org.litepal.LitePal;

import java.util.List;

import miao.kmirror.dragonli.entity.WebInfo;

public class WebInfoDao {
    private WebInfo webInfo;

    public boolean save(WebInfo info){
        webInfo = info;
        return webInfo.save();
    }

    public int delete(WebInfo info){
        int row = LitePal.delete(WebInfo.class, info.getId());
        return row;
    }

    public List<WebInfo> findAll(){
        return LitePal.findAll(WebInfo.class);
    }

    public WebInfo findById(Integer id){
        WebInfo webInfo = LitePal.find(WebInfo.class, id);
        return webInfo;
    }

    public boolean saveAll(List<WebInfo> webInfos){
        return LitePal.saveAll(webInfos);
    }

}
