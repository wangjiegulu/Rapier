package com.wangjie.rapier.app.main.module;

import com.wangjie.rapier.api.di.annotation.RNamed;
import com.wangjie.rapier.app.main.IMainPresenter;
import com.wangjie.rapier.app.main.MainPresenter;
import com.wangjie.rapier.app.main.MainViewer;
import com.wangjie.rapier.app.model.FooData;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/6/16.
 */
public class MainModule {
    public static final String FOO_DATA_A = "FOO_DATA_A";
    public static final String FOO_DATA_B = "FOO_DATA_B";

    private MainViewer mainViewer;

    public MainModule(MainViewer mainViewer) {
        this.mainViewer = mainViewer;
    }

    public IMainPresenter pickPresenter() {
        return new MainPresenter(mainViewer);
    }

    @RNamed(FOO_DATA_A)
    public FooData pickFooDataA() {
        return new FooData(112358, "hello foo A");
    }

    @RNamed(FOO_DATA_B)
    public FooData pickFooDataB() {
        return new FooData(11235813, "hello foo B");
    }

    public FooData pickFooDataNoNamed() {
        return new FooData(11235813, "hello foo no named");
    }
}
