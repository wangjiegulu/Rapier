package com.wangjie.rapier.app.main;

import android.util.Log;
import com.wangjie.rapier.api.di.annotation.RInject;
import com.wangjie.rapier.api.di.annotation.RModule;
import com.wangjie.rapier.api.di.core.RLazy;
import com.wangjie.rapier.app.RapierApplication;
import com.wangjie.rapier.app.main.module.MainPresenterModule;
import com.wangjie.rapier.app.model.FooData;
import com.wangjie.rapier.app.prefs.PrefsHelper;

import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/5/16.
 */
@RModule(moduleClazz = MainPresenterModule.class)
public class MainPresenter implements IMainPresenter {
    private static final String TAG = MainPresenter.class.getSimpleName();

    private MainViewer viewer;

    @RInject
    RLazy<PrefsHelper> prefsHelperLazy = new RLazy<>();

    @RInject
    List<FooData> testData;

    @RInject
    RapierApplication application;

    public MainPresenter(MainViewer viewer) {
        this.viewer = viewer;
        MainPresenter_Rapier.create().inject(new MainPresenterModule(), this);

        Log.i(TAG, "testData: " + testData);

        Log.i(TAG, "application: " + application);

    }

    @Override
    public void saveFooData(FooData fooData) {
        prefsHelperLazy.get().putInt(FooData.KEY_DATA_ID, fooData.getDataId())
                .putString(FooData.KEY_DATA_CONTENT, fooData.getDataContent())
                .commit();
        viewer.toast(fooData.getDataContent() + " saved!");
    }
}
