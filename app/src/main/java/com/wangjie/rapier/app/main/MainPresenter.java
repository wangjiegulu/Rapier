package com.wangjie.rapier.app.main;

import android.util.Log;
import com.wangjie.rapier.api.di.annotation.Module;
import com.wangjie.rapier.app.main.module.MainPresenterModule;
import com.wangjie.rapier.app.model.FooData;
import com.wangjie.rapier.app.prefs.PrefsHelper;

import javax.inject.Inject;
import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/5/16.
 */
@Module(moduleClazz = MainPresenterModule.class)
public class MainPresenter implements IMainPresenter {
    private static final String TAG = MainPresenter.class.getSimpleName();

    private MainViewer viewer;

    @Inject
    PrefsHelper prefsHelper;

    @Inject
    List<FooData> testData;

    public MainPresenter(MainViewer viewer) {
        this.viewer = viewer;
        MainPresenter_Rapier.create().inject(new MainPresenterModule(), this);

        Log.i(TAG, "testData: " + testData);

    }

    @Override
    public void saveFooData(FooData fooData) {
        prefsHelper.putInt(FooData.KEY_DATA_ID, fooData.getDataId())
                .putString(FooData.KEY_DATA_CONTENT, fooData.getDataContent())
                .commit();
        viewer.toast(fooData.getDataContent() + " saved!");
    }
}