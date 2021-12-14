package miao.kmirror.dragonli.dao;

import org.litepal.LitePal;

import java.util.List;

import miao.kmirror.dragonli.entity.AppPackage;

public class AppPackageDao{
    private AppPackage appPackage;

    public boolean save(AppPackage tempApp){
        appPackage = tempApp;
        return appPackage.save();
    }

    public int delete(AppPackage tempApp){
        int row = LitePal.delete(AppPackage.class, tempApp.getId());
        return row;
    }

    public List<AppPackage> findAll(){
        return LitePal.findAll(AppPackage.class);
    }

    public boolean saveAll(List<AppPackage> appPackages){
        return LitePal.saveAll(appPackages);
    }

}
