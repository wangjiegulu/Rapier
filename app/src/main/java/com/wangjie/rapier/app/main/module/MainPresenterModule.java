package com.wangjie.rapier.app.main.module;

import android.content.Context;
import com.wangjie.rapier.app.RapierApplication;
import com.wangjie.rapier.app.model.FooData;
import com.wangjie.rapier.app.prefs.PrefsHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/7/16.
 */
public class MainPresenterModule{
    public PrefsHelper pickPrefsHelper() {
        PrefsHelper prefsHelper = new PrefsHelper();
        prefsHelper.prefs = RapierApplication.instance.getSharedPreferences("Prefs_Test_Rapier", Context.MODE_PRIVATE);
        prefsHelper.editor = prefsHelper.prefs.edit();
        return prefsHelper;
    }

    public List<FooData> generateFooDataList(){
        List<FooData> dataList = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            dataList.add(new FooData(i, "foo_" + i));
        }
        return dataList;
    }

}
